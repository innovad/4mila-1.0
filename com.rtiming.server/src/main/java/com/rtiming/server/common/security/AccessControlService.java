package com.rtiming.server.common.security;

import java.security.Permission;
import java.security.Permissions;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Replace;
import org.eclipse.scout.rt.server.services.common.security.PermissionService;
import org.eclipse.scout.rt.shared.security.BasicHierarchyPermission;
import org.eclipse.scout.rt.shared.security.RemoteServiceAccessPermission;
import org.eclipse.scout.rt.shared.services.common.security.UserIdAccessControlService;

import com.rtiming.server.ServerSession;

@Replace
public class AccessControlService extends UserIdAccessControlService {

  @Override
  protected Permissions execLoadPermissions(String userId) {
    // Idea: Fetch all permissions
    // Then remove some depending on role

    // all Permissions
    Set<Class<? extends Permission>> allPermissionClasses = BEANS.get(PermissionService.class).getAllPermissionClasses();

    List<Long> roleUids = null;
    if (ServerSession.get() != null && ServerSession.get().getRoleUids() != null) {
      roleUids = Arrays.asList(ServerSession.get().getRoleUids());
    }
    Set<Class<? extends Permission>> filteredPermissionClasses = PermissionUtility.addRolePermissions(roleUids, allPermissionClasses);

    Permissions permissions = new Permissions();
    for (Class<? extends Permission> clazz : filteredPermissionClasses) {
      try {
        Permission permission;
        if (clazz == RemoteServiceAccessPermission.class) {
          permission = new RemoteServiceAccessPermission("*", "*");
        }
        else {
          permission = clazz.newInstance();
        }
        if (permission instanceof BasicHierarchyPermission) {
          ((BasicHierarchyPermission) permission).setLevel(BasicHierarchyPermission.LEVEL_ALL);
        }
        permissions.add(permission);
      }
      catch (InstantiationException | IllegalAccessException e) {
        throw new RuntimeException("failed creating permission", e);
      }
    }
    return permissions;
  }

}
