package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class CreateEventMapPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public CreateEventMapPermission() {
  super("CreateEventMap");
  }
}
