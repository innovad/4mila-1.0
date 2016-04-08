package com.rtiming.server.common.security;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.scout.commons.CompareUtility;
import org.eclipse.scout.commons.FileUtility;
import org.eclipse.scout.commons.IOUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.exception.VetoException;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.server.services.common.file.RemoteFileService;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.FMilaUtility.Architecture;
import com.rtiming.shared.common.security.Download;
import com.rtiming.shared.common.security.FMilaVersion;
import com.rtiming.shared.common.security.IUpdateService;
import com.rtiming.shared.common.security.UpdateInfo;

public class UpdateService implements IUpdateService {

  @Override
  public UpdateInfo checkForUpdate() throws ProcessingException {
    String versionToBeChecked = FMilaUtility.getVersion();

    UpdateInfo info = new UpdateInfo();
    List<String> availableDownloadFiles = null;
    String serverUrl = null;
    List<Download> availableDownloads = new ArrayList<Download>();

    try {
// TODO MIG      
//      OnlineServiceSoap onlineService = BEANS.get(OnlineServiceClient.class).getPortType();
//      availableDownloadFiles = onlineService.checkForUpdate(versionToBeChecked);
//      serverUrl = BEANS.get(OnlineServiceClient.class).getUrl();
    }
    catch (Exception e) {
      throw new VetoException(TEXTS.get("ServerNetworkError", e.getMessage()), e);
    }

    String downloadLink = null;
    String newVersion = null;
    for (String availableDownloadFile : availableDownloadFiles) {
      Download download = parseDownloadObject(availableDownloadFile, 0);
      availableDownloads.add(download);
      if (CompareUtility.equals(download.getArchitecture(), FMilaUtility.getArchitecture())) {
        downloadLink = download.getUrl();
        newVersion = download.getVersion();
      }
    }

    // compare version
    // TODO MIG
//    Version current = Version.parseVersion(versionToBeChecked);
//    Version update = Version.parseVersion(newVersion);
//    boolean updateAvailable = update.compareTo(current) > 0;

    // build update info for client
    info.setServerVersion(versionToBeChecked);
    info.setNewVersion(newVersion);
    // TODO MIG
//    if (updateAvailable) {
//      serverUrl = StringUtility.replace(serverUrl, "jaxws/onlineService", "web/");
//      info.setDownloadLink(serverUrl + downloadLink);
//    }
    return info;
  }

  @Override
  public List<Download> loadDownloads() throws ProcessingException {

    ArrayList<Download> results = new ArrayList<Download>();

    String rootPath = BEANS.get(RemoteFileService.class).getRootPath();
    rootPath += System.getProperty("file.separator") + "downloads";
    File directory = new File(rootPath);
    try {
      List<File> downloads = FileUtility.listTree(directory, true, false);
      for (File download : downloads) {
        // for some unknown reason, Scout delivers the directory itself when the directory/root is empty
        // therefore, exclude the directory/root path itself
        if (!rootPath.equalsIgnoreCase(download.getAbsolutePath())) {
          // parse file name and get size
          String fileName = download.getName();
          long size = IOUtility.getFileSize(download.getAbsolutePath());
          Download item = parseDownloadObject(fileName, size);
          results.add(item);
        }
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }

    return results;
  }

  public Download parseDownloadObject(String fileName, long size) throws ProcessingException {
    // file name convention, example
    // 4mila-1.0.0.201205131046-win32-install.exe
    // software-version-os

    String[] tokens = fileName.split("-");
    if (tokens.length != 4) {
      throw new ProcessingException("Invalid file in directory: " + fileName);
    }
    String version = tokens[1];
    String os = tokens[2];
    Architecture arch = Architecture.UNKNOWN;

    // set operating system
    if (os.contains("win32")) {
      arch = Architecture.WIN32;
    }
    else if (os.contains("win64")) {
      arch = Architecture.WIN64;
    }
    else if (os.contains("macosx")) {
      arch = Architecture.MACOSX;
    }

    // build version
    FMilaVersion fmilaVersion = new FMilaVersion(version);
    String versionSimple = fmilaVersion.getSimpleVersion();
    Date date = fmilaVersion.getDate();

    // analyze size
    String sizeString = NumberUtility.format(size / 1024 / 1024) + "MB";

    // analyze type
    String type = "";
    if (fileName.contains("install")) {
      type = "Client/Server";
    }
    else if (fileName.contains("standalone")) {
      type = "Standalone";
    }

    // create file object
    Download item = new Download();
    item.setArchitecture(arch);
    item.setDate(date);
    item.setFile(fileName);
    item.setUrl("static/downloads/" + fileName);
    item.setVersion(version);
    // TODO MIG item.setVersionText(versionSimple + " (Build " + fmilaVersion.getQualifier() + ")");
    item.setSize(sizeString);
    item.setType(type);
    return item;
  }
}
