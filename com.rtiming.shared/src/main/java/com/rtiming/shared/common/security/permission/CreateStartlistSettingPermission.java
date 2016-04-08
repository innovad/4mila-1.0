package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class CreateStartlistSettingPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public CreateStartlistSettingPermission() {
  super("CreateStartlistSetting");
  }
}
