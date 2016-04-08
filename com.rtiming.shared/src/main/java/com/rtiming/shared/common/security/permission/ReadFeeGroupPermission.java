package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class ReadFeeGroupPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public ReadFeeGroupPermission() {
  super("ReadFeeGroup");
  }
}
