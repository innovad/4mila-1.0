package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class ReadEventClassPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public ReadEventClassPermission() {
  super("ReadEventClass");
  }
}
