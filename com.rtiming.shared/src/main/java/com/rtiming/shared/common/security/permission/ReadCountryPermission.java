package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class ReadCountryPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public ReadCountryPermission() {
  super("ReadCountry");
  }
}
