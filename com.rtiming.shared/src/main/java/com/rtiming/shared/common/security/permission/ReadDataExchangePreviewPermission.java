package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class ReadDataExchangePreviewPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public ReadDataExchangePreviewPermission() {
  super("ReadDataExchangePreview");
  }
}
