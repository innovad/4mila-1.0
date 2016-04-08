package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class DeleteReportTemplatePermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public DeleteReportTemplatePermission() {
    super("DeleteReportTemplate");
  }
}
