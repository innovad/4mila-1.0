package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class UpdateEventClassPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public UpdateEventClassPermission() {
  super("UpdateEventClass");
  }
}
