package com.rtiming.server.common.security;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.security.CreateGlobalBookmarkPermission;
import org.eclipse.scout.rt.shared.security.DeleteGlobalBookmarkPermission;
import org.eclipse.scout.rt.shared.security.PublishUserBookmarkPermission;
import org.eclipse.scout.rt.shared.security.ReadGlobalBookmarkPermission;
import org.eclipse.scout.rt.shared.security.UpdateGlobalBookmarkPermission;

import com.rtiming.shared.common.security.permission.CreateDataExchangePermission;
import com.rtiming.shared.common.security.permission.EventsOutlinePermission;
import com.rtiming.shared.common.security.permission.RegistrationOutlinePermission;
import com.rtiming.shared.common.security.permission.ResultsOutlinePermission;
import com.rtiming.shared.common.security.permission.SettingsOutlinePermission;
import com.rtiming.shared.settings.user.RoleCodeType;

/**
 *
 */
public final class PermissionUtility {

  private PermissionUtility() {
  }

  public enum CRUD {
    Create, Read, Update, Delete
  }

  public static Set<String> getPermissions(CRUD crud, List<String> permissionClassList) {
    Set<String> filteredList = new HashSet<String>();
    if (permissionClassList != null) {
      for (String permission : permissionClassList) {
        boolean isCreate = permission.contains(".Create");
        boolean isRead = permission.contains(".Read");
        boolean isUpdate = permission.contains(".Update");

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

  public static Set<String> addRolePermissions(List<Long> roleUids, List<String> permissionClassList) {
    if (permissionClassList == null) {
      throw new IllegalArgumentException("permissionClassList should not be null");
    }

    Set<String> permissions = new HashSet<String>();

    if (roleUids != null && roleUids.size() > 0) {
      // Admin
      if (roleUids.contains(RoleCodeType.AdministratorCode.ID)) {
        permissions.addAll(permissionClassList);
      }
      // Speaker
      else if (roleUids.contains(RoleCodeType.SpeakerCode.ID)) {
        permissions.addAll(PermissionUtility.getPermissions(CRUD.Read, permissionClassList));
        permissions.add(RegistrationOutlinePermission.class.getCanonicalName());
        permissions.add(ResultsOutlinePermission.class.getCanonicalName());
        permissions.remove(CreateDataExchangePermission.class.getCanonicalName());
      }
      // E-Card Download
      else if (roleUids.contains(RoleCodeType.ECardDownloadCode.ID)) {
        permissions.addAll(permissionClassList);
        permissions.remove(EventsOutlinePermission.class.getCanonicalName());
        permissions.remove(SettingsOutlinePermission.class.getCanonicalName());
        permissions.remove(RegistrationOutlinePermission.class.getCanonicalName());
        permissions.remove(CreateDataExchangePermission.class.getCanonicalName());
      }
      // Registration
      else if (roleUids.contains(RoleCodeType.RegistrationCode.ID)) {
        permissions.addAll(permissionClassList);
        permissions.remove(EventsOutlinePermission.class.getCanonicalName());
        permissions.remove(SettingsOutlinePermission.class.getCanonicalName());
        permissions.remove(ResultsOutlinePermission.class.getCanonicalName());
        permissions.remove(CreateDataExchangePermission.class.getCanonicalName());
      }
      permissions.addAll(PermissionUtility.getPermissions(CRUD.Read, permissionClassList));
    }
    else {
      permissions.addAll(permissionClassList);
    }

    return permissions;
  }

  public static Set<String> addScoutPermissions(Set<String> permissions) {
    // add all Eclipse Scout Permissions
    // TODO MIG permissions.addAll(findPermissions(org.eclipse.scout.rt.shared.Activator.PLUGIN_ID));

    // remove Eclipse Scout Global Bookmark Permissions
    permissions.remove(CreateGlobalBookmarkPermission.class.getCanonicalName());
    permissions.remove(ReadGlobalBookmarkPermission.class.getCanonicalName());
    permissions.remove(UpdateGlobalBookmarkPermission.class.getCanonicalName());
    permissions.remove(DeleteGlobalBookmarkPermission.class.getCanonicalName());
    permissions.remove(PublishUserBookmarkPermission.class.getCanonicalName());

    return permissions;
  }

  public static List<String> findPermissions(String bundleId) {
    List<String> permissionClassList = new ArrayList<String>();

// TODO MIG    
//    Enumeration p = Platform.getBundle(bundleId).findEntries("/", "*Permission.class", true);
//    while (p.hasMoreElements()) {
//      Object obj = p.nextElement();
//      if (obj instanceof URL) {
//        URL url = (URL) obj;
//        String permission = extractPermissionClassName(url.getFile());
//        permissionClassList.add(permission);
//      }
//    }
    return permissionClassList;
  }

  private static String extractPermissionClassName(String filepath) {
    String permission = StringUtility.removePrefixes(filepath, "/bin/"); // Development Standard
    permission = StringUtility.removePrefixes(permission, "/target/classes/"); // Development Maven
    permission = StringUtility.removePrefixes(permission, "/"); // Build
    permission = StringUtility.removeSuffixes(permission, ".class");
    permission = StringUtility.replace(permission, "/", ".");
    return permission;
  }

}
