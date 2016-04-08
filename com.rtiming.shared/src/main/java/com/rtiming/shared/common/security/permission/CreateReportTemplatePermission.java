package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class CreateReportTemplatePermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public CreateReportTemplatePermission() {
    super("CreateReportTemplate");
  }
}
