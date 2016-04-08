package com.rtiming.server.common.database;

import java.util.Date;

public class BackupFileInfo {

  private final String foldername;
  private final Date timestamp;

  public BackupFileInfo(String foldername, Date timestamp) {
    super();
    this.foldername = foldername;
    this.timestamp = timestamp;
  }

  public String getFilename() {
    return foldername;
  }

  public Date getTimestamp() {
    return timestamp;
  }

}
