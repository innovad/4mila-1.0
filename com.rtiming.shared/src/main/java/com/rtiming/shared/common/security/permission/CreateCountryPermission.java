package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class CreateCountryPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public CreateCountryPermission() {
  super("CreateCountry");
  }
}
