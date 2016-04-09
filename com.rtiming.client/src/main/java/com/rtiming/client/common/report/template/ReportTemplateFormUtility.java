package com.rtiming.client.common.report.template;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.scout.rt.client.ui.messagebox.IMessageBox;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.util.IOUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.client.FMilaClientUtility;
import com.rtiming.client.common.report.TemplateSelectionUtility;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.dao.RtReportTemplateFile;

/**
 * 
 */
public final class ReportTemplateFormUtility {

  private ReportTemplateFormUtility() {
  }

  public static boolean loadFilesFromRemoteServer(Set<RtReportTemplateFile> templateFiles, List<ITemplateField> templateFields, File localTemporaryReportDir) throws ProcessingException {
    if (templateFiles == null || templateFields == null || localTemporaryReportDir == null) {
      throw new IllegalArgumentException("Arguments must not be null");
    }

    List<RtReportTemplateFile> sortedTemplates = TemplateSelectionUtility.sortTemplatesMasterFirst(templateFiles);

    Map<ITemplateField, File> workingFiles = new HashMap<>();
    Map<File, String> content = new HashMap<>();
    int count = 0;
    for (ITemplateField templateField : templateFields) {
      if (templateField.isVisible() && count < sortedTemplates.size()) {
        RtReportTemplateFile templateFile = CollectionUtility.toArray(sortedTemplates, RtReportTemplateFile.class)[count];
        if (!StringUtility.isNullOrEmpty(templateFile.getTemplateFileName())) {
          File file = new File(localTemporaryReportDir.getAbsolutePath() + FMilaUtility.FILE_SEPARATOR + templateFile.getTemplateFileName());
          workingFiles.put(templateField, file);
          content.put(file, templateFile.getTemplateContent());
          templateField.setValue(file.getAbsolutePath());
        }
        templateField.setDirectory(localTemporaryReportDir);
        templateField.setServerLastModified(templateFile.getLastModified());
        count++;
      }
    }

    // check working files
    List<Object> existingFilePathsAndModifyTime = new ArrayList<>();
    for (ITemplateField templateField : workingFiles.keySet()) {
      File file = workingFiles.get(templateField);
      // check if local file exists and is newer than server version
      long localFileLastModified = IOUtility.getFileLastModified(file.getAbsolutePath());
      if (file.exists() && localFileLastModified > templateField.getServerLastModified()) {
        byte[] localFileData = IOUtility.getContent(file.getAbsolutePath());
        byte[] serverFileData = content.get(file).getBytes();
        if (!CompareUtility.equals(localFileData, serverFileData)) {
          String localFileLastModifiedFormatted = DateUtility.formatDateTime(new Date(localFileLastModified));
          String serverFileLastModifiedFormatted = DateUtility.formatDateTime(new Date(templateField.getServerLastModified()));
          existingFilePathsAndModifyTime.add(TEXTS.get("TemplateFileLastModified", file.getAbsolutePath(), localFileLastModifiedFormatted, serverFileLastModifiedFormatted));
        }
      }
    }
    boolean replaceExistingFiles = true;
    if (existingFilePathsAndModifyTime.size() > 0) {
      String delimiter = FMilaUtility.LINE_SEPARATOR + FMilaUtility.LINE_SEPARATOR;
      IMessageBox box = FMilaClientUtility.createMessageBox(TEXTS.get("ApplicationName"), null, TEXTS.get("OverwriteExistingLocalFiles", CollectionUtility.format(existingFilePathsAndModifyTime, delimiter)), TEXTS.get("Overwrite"), TEXTS.get("UseLocalVersion"), TEXTS.get("Cancel"));
      int result = box.show();
      if (result == IMessageBox.CANCEL_OPTION) {
        return true;
      }
      else if (result == IMessageBox.NO_OPTION) {
        replaceExistingFiles = false;
      }
    }

    // load working files
    for (ITemplateField templateField : workingFiles.keySet()) {
      File file = workingFiles.get(templateField);
      if (!file.exists() || replaceExistingFiles) {
        IOUtility.writeContent(file.getAbsolutePath(), content.get(file));
      }
      templateField.setLocalLastModified(file.lastModified());
    }

    return false;
  }

}
