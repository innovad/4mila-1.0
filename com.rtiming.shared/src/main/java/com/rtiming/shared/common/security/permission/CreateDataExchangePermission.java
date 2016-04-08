package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class CreateDataExchangePermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public CreateDataExchangePermission() {
    super("CreateImportExport");
  }
}
