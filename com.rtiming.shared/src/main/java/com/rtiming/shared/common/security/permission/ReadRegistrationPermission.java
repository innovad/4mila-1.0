package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class ReadRegistrationPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public ReadRegistrationPermission() {
  super("ReadRegistration");
  }
}
