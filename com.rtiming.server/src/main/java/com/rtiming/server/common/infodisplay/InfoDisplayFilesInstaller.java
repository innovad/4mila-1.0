package com.rtiming.server.common.infodisplay;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.FileUtility;
import org.eclipse.scout.rt.platform.util.IOUtility;
import org.eclipse.scout.rt.server.services.common.file.RemoteFileService;

import com.rtiming.shared.FMilaUtility;

/**
 * 
 */
public class InfoDisplayFilesInstaller {

  public static void installFilesOnLocation() throws ProcessingException {
    String rootPath = BEANS.get(RemoteFileService.class).getRootPath();
    String version = FMilaUtility.getVersion();

    installFiles(rootPath, version);
  }

  public static boolean installFiles(String rootPath, String version) throws ProcessingException {
    // try to create cached folder (root/infodisplay/version)
    String dir = rootPath + FMilaUtility.FILE_SEPARATOR + "infodisplay" + FMilaUtility.FILE_SEPARATOR + version;
    boolean created = IOUtility.createDirectory(dir);

    if (created) {
      // get original files inside JAR
      URL templateFile = FMilaUtility.findFileLocation("resources/infodisplay", ""); // TODO MIG

      try {
        FileUtility.copyTree(new File(templateFile.getFile()), new File(dir));
      }
      catch (IOException e) {
        throw new ProcessingException("Failed to copy Info Display Files", e);
      }
    }

    return created;
  }
}
