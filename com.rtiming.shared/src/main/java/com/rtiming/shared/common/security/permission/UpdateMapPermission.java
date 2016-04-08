package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class UpdateMapPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public UpdateMapPermission() {
  super("UpdateMap");
  }
}
