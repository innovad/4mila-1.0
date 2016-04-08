package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class CreateRankingPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public CreateRankingPermission() {
    super("CreateRanking");
  }
}
