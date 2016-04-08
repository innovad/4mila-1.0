package com.rtiming.shared.security;

import java.security.BasicPermission;

public class CreateVariantPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public CreateVariantPermission() {
    super("CreateVariant");
  }
}
