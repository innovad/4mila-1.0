package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class ReadMapPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public ReadMapPermission() {
  super("ReadMap");
  }
}
