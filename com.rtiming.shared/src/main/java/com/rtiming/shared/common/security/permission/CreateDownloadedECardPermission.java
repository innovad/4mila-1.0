package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class CreateDownloadedECardPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public CreateDownloadedECardPermission() {
  super("CreateDownloadedECard");
  }
}
