package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class UpdateCourseControlPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public UpdateCourseControlPermission() {
  super("UpdateCourseControl");
  }
}
