package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class CreateAdditionalInformationPermission extends BasicPermission{

  private static final long serialVersionUID = 0L;

  public CreateAdditionalInformationPermission() {
  super("CreateAdditionalInformation");
  }
}
