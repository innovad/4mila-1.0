package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class ReadParticipationPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public ReadParticipationPermission() {
  super("ReadParticipation");
  }
}
