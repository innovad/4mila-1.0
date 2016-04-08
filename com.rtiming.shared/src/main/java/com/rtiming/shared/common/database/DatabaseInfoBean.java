package com.rtiming.shared.common.database;

import java.io.Serializable;
import java.util.Date;

import com.rtiming.shared.common.database.jpa.JPAStyle;

public class DatabaseInfoBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long clientSessionNr;
  private String dataDirectory;
  private String fileStoreRootPath;
  private Date lastBackup;
  private JPAStyle style;
  private String jdbcUrl;

  public void setClientSessionNr(Long clientSessionNr) {
    this.clientSessionNr = clientSessionNr;
  }

  public Long getClientSessionNr() {
    return clientSessionNr;
  }

  public String getDataDirectory() {
    return dataDirectory;
  }

  public void setDataDirectory(String dataDirectory) {
    this.dataDirectory = dataDirectory;
  }

  public void setFileStoreRootPath(String fileStoreRootPath) {
    this.fileStoreRootPath = fileStoreRootPath;
  }

  public String getFileStoreRootPath() {
    return fileStoreRootPath;
  }

  public Date getLastBackup() {
    return lastBackup;
  }

  public void setLastBackup(Date lastBackup) {
    this.lastBackup = lastBackup;
  }

  public JPAStyle getStyle() {
    return style;
  }

  public void setStyle(JPAStyle style) {
    this.style = style;
  }

  public String getJdbcUrl() {
    return jdbcUrl;
  }

  public void setJdbcUrl(String jdbcUrl) {
    this.jdbcUrl = jdbcUrl;
  }

}
