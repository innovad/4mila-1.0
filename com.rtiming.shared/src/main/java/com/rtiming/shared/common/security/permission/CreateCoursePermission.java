package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class CreateCoursePermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public CreateCoursePermission() {
  super("CreateCourse");
  }
}
