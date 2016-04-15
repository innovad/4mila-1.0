package com.rtiming.server.common.security;

import java.security.Permission;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.scout.rt.platform.Platform;

import com.rtiming.shared.common.security.permission.CreateDataExchangePermission;
import com.rtiming.shared.common.security.permission.EventsOutlinePermission;
import com.rtiming.shared.common.security.permission.RegistrationOutlinePermission;
import com.rtiming.shared.common.security.permission.ResultsOutlinePermission;
import com.rtiming.shared.common.security.permission.SettingsOutlinePermission;
import com.rtiming.shared.settings.user.RoleCodeType;

public final class PermissionUtility {

  private PermissionUtility() {
  }

  public enum CRUD {
    Create, Read, Update, Delete
  }

  public static Set<Class<? extends Permission>> getPermissions(CRUD crud, Set<Class<? extends Permission>> permissionClassList) {
    Set<Class<? extends Permission>> filteredList = new HashSet<Class<? extends Permission>>();
    if (permissionClassList != null) {
      for (Class<? extends Permission> permission : permissionClassList) {
        boolean isCreate = permission.getName().contains(".Create");
        boolean isRead = permission.getName().contains(".Read");
        boolean isUpdate = permission.getName().contains(".Update");

        if (isCreate && CRUD.Create.equals(crud)) {
          filteredList.add(permission);
        }
        else if (isRead && CRUD.Read.equals(crud)) {
          filteredList.add(permission);
        }
        else if (isUpdate && CRUD.Update.equals(crud)) {
          filteredList.add(permission);
        }
      }
    }
    return filteredList;
  }

  public static Set<Class<? extends Permission>> addRolePermissions(List<Long> roleUids, Set<Class<? extends Permission>> permissionClassList) {
    if (permissionClassList == null) {
      throw new IllegalArgumentException("permissionClassList should not be null");
    }

    // TODO MIG
    if (Platform.get().inDevelopmentMode()) {
      return permissionClassList;
    }

    Set<Class<? extends Permission>> permissions = new HashSet<Class<? extends Permission>>();

    if (roleUids != null && roleUids.size() > 0) {
      // Admin
      if (roleUids.contains(RoleCodeType.AdministratorCode.ID)) {
        permissions.addAll(permissionClassList);
      }
      // Speaker
      else if (roleUids.contains(RoleCodeType.SpeakerCode.ID)) {
        permissions.addAll(PermissionUtility.getPermissions(CRUD.Read, permissionClassList));
        permissions.add(RegistrationOutlinePermission.class);
        permissions.add(ResultsOutlinePermission.class);
        permissions.remove(CreateDataExchangePermission.class);
      }
      // E-Card Download
      else if (roleUids.contains(RoleCodeType.ECardDownloadCode.ID)) {
        permissions.addAll(permissionClassList);
        permissions.remove(EventsOutlinePermission.class);
        permissions.remove(SettingsOutlinePermission.class);
        permissions.remove(RegistrationOutlinePermission.class);
        permissions.remove(CreateDataExchangePermission.class);
      }
      // Registration
      else if (roleUids.contains(RoleCodeType.RegistrationCode.ID)) {
        permissions.addAll(permissionClassList);
        permissions.remove(EventsOutlinePermission.class);
        permissions.remove(SettingsOutlinePermission.class);
        permissions.remove(ResultsOutlinePermission.class);
        permissions.remove(CreateDataExchangePermission.class);
      }
      permissions.addAll(PermissionUtility.getPermissions(CRUD.Read, permissionClassList));
    }
    else {
      permissions.addAll(permissionClassList);
    }

    return permissions;
  }

}
