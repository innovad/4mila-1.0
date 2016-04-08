package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class ReadCourseControlPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public ReadCourseControlPermission() {
  super("ReadCourseControl");
  }
}
