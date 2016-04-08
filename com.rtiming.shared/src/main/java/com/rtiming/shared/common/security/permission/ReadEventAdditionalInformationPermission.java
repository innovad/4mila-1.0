package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class ReadEventAdditionalInformationPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public ReadEventAdditionalInformationPermission() {
  super("ReadEventAdditionalInformation");
  }
}
