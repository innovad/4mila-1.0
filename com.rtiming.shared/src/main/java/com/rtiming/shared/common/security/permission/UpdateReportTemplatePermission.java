package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class UpdateReportTemplatePermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public UpdateReportTemplatePermission() {
    super("UpdateReportTemplate");
  }
}
