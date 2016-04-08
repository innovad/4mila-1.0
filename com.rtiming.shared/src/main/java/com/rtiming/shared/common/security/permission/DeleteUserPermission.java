package com.rtiming.shared.common.security.permission;

import org.eclipse.scout.rt.shared.security.BasicHierarchyPermission;

public class DeleteUserPermission extends BasicHierarchyPermission{

  private static final long serialVersionUID = 0L;

  public DeleteUserPermission() {
  super("DeleteUser");
  }
}
