package com.rtiming.server.common.database;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.eclipse.scout.commons.CompareUtility;
import org.eclipse.scout.commons.FileUtility;
import org.eclipse.scout.commons.IOUtility;
import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.TypeCastUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.exception.VetoException;
import org.eclipse.scout.commons.logger.IScoutLogger;
import org.eclipse.scout.commons.logger.ScoutLogManager;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.server.services.common.file.RemoteFileService;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.clientnotification.IClientNotificationService;

import com.rtiming.server.common.database.setup.JPASetup;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.common.database.jpa.JPAStyle;
import com.rtiming.shared.settings.IDefaultProcessService;

public final class ServerBackupUtility {

  private static final String TIMESTAMP = "yyyyMMdd-HH.mm.ss";
  private static final String DATABASE = "database";
  private static final String FILESTORE = "filestore";
  private static String backupSuffix = JPAStyle.H2_EMBEDDED.equals(JPASetup.get().getStyle()) ? ".zip" : ".backup";
  private static IScoutLogger logger = ScoutLogManager.getLogger(ServerBackupUtility.class);
  private static int PATH = 0;
  private static int DATE = 1;
  private static int SIZE = 2;

  private ServerBackupUtility() {
  }

  public static Object[][] getBackupTableData() throws ProcessingException {
    Object[][] table = new Object[0][3];

    String backupPath = getBackupPath();
    if (backupPath != null) {
      File backupDir = new File(backupPath);
      FileFilter fileFilter = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
          return pathname.getAbsolutePath().contains("-");
        }
      };
      File[] list = backupDir.listFiles(fileFilter);
      table = new Object[list.length][3];
      int k = 0;
      for (File file : list) {
        table[k][PATH] = file.getName();

        String datePart = StringUtility.removeSuffixes(file.getName(), backupSuffix);
        Date date = null;
        try {
          date = DateUtility.parse(datePart, TIMESTAMP);
        }
        catch (Exception e) {
          logger.error(e.getMessage(), e);
        }

        table[k][DATE] = date;

        long size = 0;
        try {
          List<File> files = FileUtility.listTree(file, true, false);
          for (File f : files) {
            size += IOUtility.getFileSize(f.getAbsolutePath());
          }
        }
        catch (IOException e) {
          logger.error(e.getMessage(), e);
        }

        table[k][SIZE] = size / 1024;
        k++;
      }
    }
    return table;

  }

  public static void backup() throws ProcessingException {
    BackupFileInfo backupFileInfo = buildBackupFolderName();
    File backupFile = null;

    String backupPath = getBackupPath();
    if (backupPath == null) {
      throw new VetoException(ScoutTexts.get("FormEmptyMandatoryFieldsMessage") + FMilaUtility.LINE_SEPARATOR + FMilaUtility.LINE_SEPARATOR + "- " + TEXTS.get("BackupDirectory"));
    }
    File backupRootDir = new File(backupPath);
    if (backupRootDir.exists()) {
      // Check if backup file already exists, otherwise delete
      backupFile = new File(backupPath + FMilaUtility.FILE_SEPARATOR + backupFileInfo.getFilename());
      if (backupFile.exists()) {
        backupFile.delete();
      }
      IOUtility.createDirectory(backupFile.getAbsolutePath());

      // database dump
      JPASetup.get().getDatabase().createBackup(backupFile.getAbsolutePath() + FMilaUtility.FILE_SEPARATOR + DATABASE + backupSuffix);

      // filestore zip
      try {
        FileUtility.compressArchive(new File(BEANS.get(RemoteFileService.class).getRootPath()), new File(backupFile.getAbsolutePath() + FMilaUtility.FILE_SEPARATOR + FILESTORE + ".zip"));
      }
      catch (IOException e) {
        throw new ProcessingException("Filestore Backup failed", e);
      }
    }

    // notify clients
    if (backupFile != null && IOUtility.fileExists(backupFile.getAbsolutePath())) {
      sendClientNotification(backupFileInfo.getTimestamp());
    }

    // delete backup history
    deleteMaxBackups();
  }

  public static void restore(String backupFileName) throws ProcessingException {
    File backupFolder = new File(getBackupPath() + FMilaUtility.FILE_SEPARATOR + backupFileName);
    if (backupFolder.exists()) {
      // filestore
      IOUtility.deleteDirectory(BEANS.get(RemoteFileService.class).getRootPath());
      try {
        FileUtility.extractArchive(new File(getBackupPath() + FMilaUtility.FILE_SEPARATOR + backupFileName + FMilaUtility.FILE_SEPARATOR + FILESTORE + ".zip"), new File(BEANS.get(RemoteFileService.class).getRootPath()));
      }
      catch (IOException e) {
        throw new ProcessingException("Unable to restore filestore zip", e);
      }

      // restore database
      JPASetup.get().getDatabase().restoreBackup(backupFolder.getAbsolutePath() + FMilaUtility.FILE_SEPARATOR + DATABASE + backupSuffix);
    }
    else {
      throw new ProcessingException("Cannot find file: " + backupFolder.getAbsolutePath());
    }
  }

  private static void sendClientNotification(Date lastBackup) {
    IClientNotificationService notifyService = BEANS.get(IClientNotificationService.class);
    // TODO MIG notifyService.putNotification(new NewBackupNotification(lastBackup), new AllUserFilter(30000L));
  }

  public static void delete(String... files) throws ProcessingException {
    Date lastBackup1 = getLastBackup();
    for (String file : files) {
      IOUtility.deleteDirectory(getBackupPath() + FMilaUtility.FILE_SEPARATOR + file);
    }
    Date lastBackup2 = getLastBackup();
    if (CompareUtility.notEquals(lastBackup1, lastBackup2)) {
      sendClientNotification(lastBackup2);
    }
  }

  public static void deleteMaxBackups() throws ProcessingException {
    Long maxBackupNumber = BEANS.get(IDefaultProcessService.class).getBackupMaxNumber();
    Object[][] data = getBackupTableData();
    Date min = null;
    String minFileName = null;
    while (data.length > maxBackupNumber) {
      for (int k = 0; k < data.length; k++) {
        Date candidate = TypeCastUtility.castValue(data[k][DATE], Date.class);
        min = DateUtility.min(min, candidate);
        if (CompareUtility.equals(min, candidate)) {
          minFileName = TypeCastUtility.castValue(data[k][PATH], String.class);
          min = null;
          break;
        }
      }
      delete(minFileName);
      data = getBackupTableData();
    }
  }

  public static Date getLastBackup() throws ProcessingException {
    Object[][] data = getBackupTableData();
    // find last backup date in table data
    Date max = null;
    for (int k = 0; k < data.length; k++) {
      Date candidate = TypeCastUtility.castValue(data[k][DATE], Date.class);
      max = DateUtility.max(max, candidate);
    }
    return max;
  }

  private static BackupFileInfo buildBackupFolderName() {
    Calendar cal = GregorianCalendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat(TIMESTAMP);
    StringBuffer date = new StringBuffer();
    Date timestamp = cal.getTime();
    date.append(df.format(timestamp));
    String backupFileName = date.toString();
    return new BackupFileInfo(backupFileName, timestamp);
  }

  private static String getBackupPath() throws ProcessingException {
    String pathStr = BEANS.get(IDefaultProcessService.class).getBackupDirectory();
    if (!StringUtility.isNullOrEmpty(pathStr)) {
      File path = new File(pathStr);
      if (!path.exists()) {
        IOUtility.createDirectory(pathStr);
      }
      if (!path.canWrite()) {
        throw new ProcessingException("Cannot write to backup directory: " + path.getAbsolutePath());
      }
    }
    return pathStr;
  }

}
