package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class UpdateRegistrationPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public UpdateRegistrationPermission() {
  super("UpdateRegistration");
  }
}
