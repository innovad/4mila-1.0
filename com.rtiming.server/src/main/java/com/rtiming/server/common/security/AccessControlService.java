package com.rtiming.server.common.security;

import java.security.Permissions;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.eclipse.scout.rt.platform.Replace;
import org.eclipse.scout.rt.shared.security.BasicHierarchyPermission;
import org.eclipse.scout.rt.shared.services.common.security.UserIdAccessControlService;

import com.rtiming.server.ServerSession;

@Replace
public class AccessControlService extends UserIdAccessControlService {

  @Override
  protected Permissions execLoadPermissions(String userId) {
    // Idee: Alle Permissions zusammenbauen
    // Davon je nach Rolle einige Permissions abziehen
    // AccessControlUtility

    // all 4mila Permissions
    List<String> permissionClassList = PermissionUtility.findPermissions(""); // TODO MIG

    List<Long> roleUids = null;
    if (ServerSession.get().getRoleUids() != null) {
      roleUids = Arrays.asList(ServerSession.get().getRoleUids());
    }
    Set<String> permissions = PermissionUtility.addRolePermissions(roleUids, permissionClassList);
    permissions = PermissionUtility.addScoutPermissions(permissions);

    Object[][] permissionData = buildArray(permissions);

    // TODO MIG return AccessControlUtility.createPermissions(permissionData);
    return null;
  }

  private Object[][] buildArray(Set<String> filteredList) {
    Object[][] permissionData = new Object[filteredList.size()][2];
    int r = 0;
    for (String s : filteredList) {
      permissionData[r][0] = s;
      permissionData[r][1] = BasicHierarchyPermission.LEVEL_ALL;
      r++;
    }
    return permissionData;
  }

}
