package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class UpdateRankingPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public UpdateRankingPermission() {
    super("UpdateRanking");
  }
}
