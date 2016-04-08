package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class ReadStartblockPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public ReadStartblockPermission() {
  super("ReadStartblock");
  }
}
