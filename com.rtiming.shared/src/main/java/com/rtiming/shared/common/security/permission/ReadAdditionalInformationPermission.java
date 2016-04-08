package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class ReadAdditionalInformationPermission extends BasicPermission{

  private static final long serialVersionUID = 0L;

  public ReadAdditionalInformationPermission() {
  super("ReadAdditionalInformation");
  }
}
