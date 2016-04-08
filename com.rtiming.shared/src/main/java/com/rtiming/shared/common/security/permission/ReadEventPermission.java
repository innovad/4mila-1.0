package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class ReadEventPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public ReadEventPermission() {
  super("ReadEvent");
  }
}
