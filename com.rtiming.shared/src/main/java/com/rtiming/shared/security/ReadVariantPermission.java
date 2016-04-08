package com.rtiming.shared.security;

import java.security.BasicPermission;

public class ReadVariantPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public ReadVariantPermission() {
    super("ReadVariant");
  }
}
