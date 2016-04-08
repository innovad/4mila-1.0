package com.rtiming.shared.security;

import java.security.BasicPermission;

public class UpdateVariantPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public UpdateVariantPermission() {
    super("UpdateVariant");
  }
}
