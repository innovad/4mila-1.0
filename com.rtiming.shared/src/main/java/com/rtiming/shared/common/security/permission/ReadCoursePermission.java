package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class ReadCoursePermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public ReadCoursePermission() {
  super("ReadCourse");
  }
}
