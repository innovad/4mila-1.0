package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class UpdateCountryPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public UpdateCountryPermission() {
  super("UpdateCountry");
  }
}
