package com.rtiming.client.common.infodisplay;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.eclipse.scout.rt.client.services.common.file.IFileService;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.StringUtility;

import com.rtiming.client.ClientSession;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.common.file.AllFilenameFilter;

/**
 * 
 */
public final class InfoDisplayUtility {

  public InfoDisplayUtility() {
  }

  private static boolean isActive = false;
  private static boolean filesAreSynced = false;
  private static Object lock = new Object();

  public static boolean isActive() {
    return isActive;
  }

  public static void setActive(boolean isActive) {
    InfoDisplayUtility.isActive = isActive;
  }

  public static boolean useBrowserInfoWindow() {
    return FMilaUtility.getPlatform().equals(FMilaUtility.OperatingSystem.MACOSX);
  }

  public static InfoDisplayForm getWindow() throws ProcessingException {
    InfoDisplayForm form = ClientSession.get().getDesktop().findForm(InfoDisplayForm.class);
    if (form == null) {
      form = new InfoDisplayForm();
      form.startNew();
    }
    return form;
  }

  public static void closeWindow() throws ProcessingException {
    InfoDisplayForm form = ClientSession.get().getDesktop().findForm(InfoDisplayForm.class);
    if (form != null) {
      form.doClose();
    }
  }

  public static String addParameter(String location, String key, String value) throws ProcessingException {
    try {
      value = URLEncoder.encode(StringUtility.emptyIfNull(value), "UTF8").replace("+", "%20");
    }
    catch (UnsupportedEncodingException e) {
      throw new ProcessingException("Unsupported Value", e);
    }
    if (location.contains("?")) {
      // second and more params
      return location + "&" + key + "=" + value;
    }
    else {
      // first param
      return location + "?" + key + "=" + value;
    }
  }

  public static String getInfoDisplayHtmlUrl(String fileName) throws ProcessingException {
    String path = getHtmlRootPath();

    // files are synced once
    synchronized (lock) {
      if (!filesAreSynced) {
        // Sync Remote Info Display Files
        BEANS.get(IFileService.class).syncRemoteFiles(path, new AllFilenameFilter());
        filesAreSynced = true;
      }
    }

    File file = BEANS.get(IFileService.class).getRemoteFile(path, fileName);

    return file.getAbsolutePath();
  }

  public static String getHtmlRootPath() {
    // TODO MIG
//    String version = Platform.getBundle(com.rtiming.shared.Activator.PLUGIN_ID).getVersion().toString();
//    String path = "/" + "infodisplay" + "/" + version;
//    return path;
    return null;
  }

}
