package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class CreateEventPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public CreateEventPermission() {
  super("CreateEvent");
  }
}
