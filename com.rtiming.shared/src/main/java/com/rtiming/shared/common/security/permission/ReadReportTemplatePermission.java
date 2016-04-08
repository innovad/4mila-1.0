package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class ReadReportTemplatePermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public ReadReportTemplatePermission() {
    super("ReadReportTemplate");
  }
}
