package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class ReadCourseAndClassFromECardPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public ReadCourseAndClassFromECardPermission() {
  super("ReadCourseAndClassFromECard");
  }
}
