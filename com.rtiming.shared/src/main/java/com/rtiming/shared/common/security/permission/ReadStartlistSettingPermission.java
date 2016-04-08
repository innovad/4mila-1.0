package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class ReadStartlistSettingPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public ReadStartlistSettingPermission() {
  super("ReadStartlistSetting");
  }
}
