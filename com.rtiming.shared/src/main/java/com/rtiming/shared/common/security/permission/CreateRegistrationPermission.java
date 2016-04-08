package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class CreateRegistrationPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public CreateRegistrationPermission() {
  super("CreateRegistration");
  }
}
