package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class CreateParticipationPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public CreateParticipationPermission() {
  super("CreateParticipation");
  }
}
