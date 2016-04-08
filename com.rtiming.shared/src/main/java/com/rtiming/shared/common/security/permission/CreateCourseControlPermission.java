package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class CreateCourseControlPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public CreateCourseControlPermission() {
  super("CreateCourseControl");
  }
}
