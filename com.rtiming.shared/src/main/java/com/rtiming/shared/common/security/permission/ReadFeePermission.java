package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class ReadFeePermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public ReadFeePermission() {
  super("ReadFee");
  }
}
