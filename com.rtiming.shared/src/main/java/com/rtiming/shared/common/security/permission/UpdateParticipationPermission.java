package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class UpdateParticipationPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public UpdateParticipationPermission() {
  super("UpdateParticipation");
  }
}
