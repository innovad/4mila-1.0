package com.rtiming.shared.common.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Update Information: Current Server Shared Version and Download Link from 4mila.com in case of update
 */
public class UpdateInfo implements Serializable {

  private static final long serialVersionUID = 1L;
  private String serverVersion;
  private String newVersion;
  private String downloadLink;
  private final List<Download> downlodads = new ArrayList<Download>();

  public UpdateInfo() {
  }

  public String getServerVersion() {
    return serverVersion;
  }

  public void setServerVersion(String serverVersion) {
    this.serverVersion = serverVersion;
  }

  public String getDownloadLink() {
    return downloadLink;
  }

  public void setDownloadLink(String downloadLink) {
    this.downloadLink = downloadLink;
  }

  public void setNewVersion(String newVersion) {
    this.newVersion = newVersion;
  }

  public String getNewVersion() {
    return newVersion;
  }

}
