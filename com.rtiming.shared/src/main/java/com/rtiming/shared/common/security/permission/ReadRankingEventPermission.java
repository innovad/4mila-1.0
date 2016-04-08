package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class ReadRankingEventPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public ReadRankingEventPermission() {
    super("ReadRankingEvent");
  }
}
