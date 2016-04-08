package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class CreateRankingEventPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public CreateRankingEventPermission() {
    super("CreateRankingEvent");
  }
}
