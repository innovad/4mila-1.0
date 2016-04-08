package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class UpdateEventMapPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public UpdateEventMapPermission() {
  super("UpdateEventMap");
  }
}
