package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class UpdateAdditionalInformationPermission extends BasicPermission{

  private static final long serialVersionUID = 0L;

  public UpdateAdditionalInformationPermission() {
  super("UpdateAdditionalInformation");
  }
}
