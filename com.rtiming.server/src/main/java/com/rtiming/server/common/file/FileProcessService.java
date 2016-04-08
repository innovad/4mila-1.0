package com.rtiming.server.common.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import org.eclipse.scout.commons.IOUtility;
import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.server.services.common.file.RemoteFileService;

import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.common.file.IFileProcessService;

public class FileProcessService  implements IFileProcessService {

  private static final String SEPARATOR = System.getProperty("file.separator");

  @Override
  public String writeDataToFile(Long pkNr, Long clientNr, String format, byte[] imageData, String path) throws ProcessingException {

    String dir = BEANS.get(RemoteFileService.class).getRootPath();

    // ensure directory exists
    ensureClientNrDirectoryExists(dir, path, clientNr);

    // remove all files
    try {
      for (String s : FMilaUtility.getSupportedImageFormats()) {
        File f = new File(buildFilePath(pkNr, clientNr, dir, path, s));
        if (f.exists()) {
          f.delete();
        }
      }
    }
    catch (Exception e) {
      throw new ProcessingException(e.getMessage());
    }

    // save file
    if (!StringUtility.isNullOrEmpty(format) && imageData != null) {
      try {
        File f = new File(buildFilePath(pkNr, clientNr, dir, path, format));
        FileOutputStream fr;
        fr = new FileOutputStream(f);
        IOUtility.writeContent(fr, imageData);
        return f.getAbsolutePath();
      }
      catch (Exception e) {
        throw new ProcessingException(e.getMessage());
      }
    }
    return null;
  }

  private String buildFilePath(Long primaryKeyNr, Long clientNr, String root, String path, String suffix) {
    return root + SEPARATOR + path + SEPARATOR + clientNr + SEPARATOR + primaryKeyNr + "." + suffix;
  }

  private void ensureClientNrDirectoryExists(String root, String path, Long clientNr) throws ProcessingException {
    File f = new File(root + SEPARATOR + path + SEPARATOR + clientNr);
    if (!f.exists()) {
      // create directory
      try {
        IOUtility.createDirectory(f.getAbsolutePath());
      }
      catch (Exception e) {
        throw new ProcessingException(e.getMessage());
      }
    }
    if (!f.isDirectory()) {
      throw new ProcessingException("Could not create directory for clientNr:" + root + path + SEPARATOR + clientNr);
    }
  }

  @Override
  public byte[] loadFile(Long pkNr, Long clientNr, String format, String path) throws ProcessingException {

    if (!StringUtility.isNullOrEmpty(format)) {
      try {
        String dir = BEANS.get(RemoteFileService.class).getRootPath();
        String filePath = buildFilePath(pkNr, clientNr, dir, path, format);
        if (IOUtility.fileExists(filePath)) {
          File f = new File(filePath);
          FileInputStream fr;
          fr = new FileInputStream(f);
          return IOUtility.getContent(fr);
        }
      }
      catch (Exception e) {
        throw new ProcessingException(e.getMessage());
      }
    }

    return null;
  }

  @Override
  public void deleteFile(Long pkNr, Long clientNr, String path, String[] suffixes) throws ProcessingException {
    String dir = BEANS.get(RemoteFileService.class).getRootPath();
    for (String suffix : suffixes) {
      String filePath = buildFilePath(pkNr, clientNr, dir, path, suffix);
      if (IOUtility.fileExists(filePath)) {
        File f = new File(filePath);
        f.delete();
      }
    }
  }

  @Override
  public void cleanUpFiles(List<Long> existingPkNrs, Long clientNr, String path) throws ProcessingException {

    String dir = BEANS.get(RemoteFileService.class).getRootPath();
    dir = dir + SEPARATOR + path + SEPARATOR + clientNr;
    File folder = new File(dir);

    if (!folder.exists()) {
      return;
    }

    if (!folder.isDirectory()) {
      throw new ProcessingException("Must be a directory: " + path);
    }

    String[] files = folder.list();
    for (String file : files) {
      String[] fileParts = file.split("\\.");
      if (fileParts.length > 0) {
        Long pkNr = null;
        try {
          pkNr = Long.valueOf(fileParts[0]);
        }
        catch (NumberFormatException e) {
          // nop
        }
        if (pkNr != null && !existingPkNrs.contains(pkNr)) {
          File toBeDeleted = new File(dir + SEPARATOR + file);
          if (toBeDeleted.exists()) {
            toBeDeleted.delete();
          }
          else {
            throw new ProcessingException("Could not clean up files");
          }
        }
      }
    }

  }
}
