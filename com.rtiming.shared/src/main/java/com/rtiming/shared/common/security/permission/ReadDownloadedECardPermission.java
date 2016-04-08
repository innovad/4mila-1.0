package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class ReadDownloadedECardPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public ReadDownloadedECardPermission() {
  super("ReadDownloadedECard");
  }
}
