package com.rtiming.shared.common.security;

import java.util.Date;

import com.rtiming.shared.FMilaUtility.Architecture;

public class Download {

  public Download() {
  }

  private String file;
  private String url;
  private String version;
  private String versionText;
  private Date date;
  private Architecture architecture;
  private String size;
  private String type;

  public String getFile() {
    return file;
  }

  public void setFile(String file) {
    this.file = file;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getSize() {
    return size;
  }

  public void setSize(String size) {
    this.size = size;
  }

  public Architecture getArchitecture() {
    return architecture;
  }

  public void setArchitecture(Architecture architecture) {
    this.architecture = architecture;
  }

  public String getVersionText() {
    return versionText;
  }

  public void setVersionText(String versionText) {
    this.versionText = versionText;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

}
