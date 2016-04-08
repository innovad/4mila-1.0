package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class UpdateDownloadedECardPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public UpdateDownloadedECardPermission() {
  super("UpdateDownloadedECard");
  }
}
