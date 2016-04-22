package com.rtiming.server.common.security;

import java.io.FilePermission;
import java.security.Permission;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sound.sampled.AudioPermission;

import org.junit.Assert;
import org.junit.Test;

import com.rtiming.server.common.security.PermissionUtility.CRUD;
import com.rtiming.shared.common.security.permission.CreateEventClassPermission;
import com.rtiming.shared.common.security.permission.CreateRunnerPermission;
import com.rtiming.shared.common.security.permission.EventsOutlinePermission;
import com.rtiming.shared.common.security.permission.ReadEventClassPermission;
import com.rtiming.shared.common.security.permission.ReadRunnerPermission;
import com.rtiming.shared.common.security.permission.RegistrationOutlinePermission;
import com.rtiming.shared.common.security.permission.ResultsOutlinePermission;
import com.rtiming.shared.common.security.permission.SettingsOutlinePermission;
import com.rtiming.shared.common.security.permission.UpdateEventClassPermission;
import com.rtiming.shared.settings.user.RoleCodeType;

public class PermissionUtilityTest {

  @Test
  public void testGetPermissionsNull1() throws Exception {
    Set<Class<? extends Permission>> permissionClassList = new HashSet<>();
    Set<Class<? extends Permission>> result = PermissionUtility.getPermissions(CRUD.Create, permissionClassList);
    Assert.assertEquals("0 Permissions", 0, result.size());
  }

  @Test
  public void testGetPermissionsNull2() throws Exception {
    Set<Class<? extends Permission>> result = PermissionUtility.getPermissions(CRUD.Create, null);
    Assert.assertEquals("0 Permissions", 0, result.size());
  }

  @Test
  public void testGetPermissionsNull3() throws Exception {
    Set<Class<? extends Permission>> permissionClassList = new HashSet<>();
    Set<Class<? extends Permission>> result = PermissionUtility.getPermissions(null, permissionClassList);
    Assert.assertEquals("0 Permissions", 0, result.size());
  }

  @Test
  public void testGetPermissions1() throws Exception {
    Set<Class<? extends Permission>> permissionClassList = new HashSet<>();
    permissionClassList.add(CreateEventClassPermission.class);
    permissionClassList.add(UpdateEventClassPermission.class);
    Set<Class<? extends Permission>> result = PermissionUtility.getPermissions(CRUD.Create, permissionClassList);
    Assert.assertEquals("1 Permission", 1, result.size());
  }

  @Test
  public void testGetPermissions2() throws Exception {
    Set<Class<? extends Permission>> permissionClassList = new HashSet<>();
    permissionClassList.add(CreateEventClassPermission.class);
    permissionClassList.add(ReadEventClassPermission.class);
    permissionClassList.add(UpdateEventClassPermission.class);
    Set<Class<? extends Permission>> result = PermissionUtility.getPermissions(CRUD.Update, permissionClassList);
    Assert.assertEquals("1 Permission", 1, result.size());
  }

  @Test
  public void testGetPermissions3() throws Exception {
    Set<Class<? extends Permission>> permissionClassList = new HashSet<>();
    permissionClassList.add(CreateEventClassPermission.class);
    permissionClassList.add(UpdateEventClassPermission.class);
    permissionClassList.add(ReadEventClassPermission.class);
    Set<Class<? extends Permission>> result = PermissionUtility.getPermissions(CRUD.Read, permissionClassList);
    Assert.assertEquals("1 Permission", 1, result.size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddRolePermissions1() throws Exception {
    PermissionUtility.addRolePermissions(null, null);
  }

  @Test
  public void testAddRolePermissions2() throws Exception {
    Set<Class<? extends Permission>> permissionClassList = new HashSet<>();
    Set<Class<? extends Permission>> result = PermissionUtility.addRolePermissions(null, permissionClassList);
    Assert.assertEquals("0 Permissions", 0, result.size());
  }

  @Test
  public void testAddRolePermissions3() throws Exception {
    Set<Class<? extends Permission>> permissionClassList = new HashSet<>();
    permissionClassList.add(FilePermission.class);
    Set<Class<? extends Permission>> result = PermissionUtility.addRolePermissions(null, permissionClassList);
    Assert.assertEquals("1 Permission", 1, result.size());
  }

  @Test
  public void testAddRolePermissions4() throws Exception {
    Set<Class<? extends Permission>> permissionClassList = new HashSet<Class<? extends Permission>>();
    Set<Class<? extends Permission>> result = PermissionUtility.addRolePermissions(new ArrayList<Long>(), permissionClassList);
    Assert.assertEquals("0 Permissions", 0, result.size());
  }

  @Test
  public void testAddRolePermissionsAdministrator() throws Exception {
    Set<Class<? extends Permission>> permissionClassList = new HashSet<>();
    permissionClassList.add(FilePermission.class);
    permissionClassList.add(AudioPermission.class);
    List<Long> roleUids = new ArrayList<Long>();
    roleUids.add(RoleCodeType.AdministratorCode.ID);
    Set<Class<? extends Permission>> result = PermissionUtility.addRolePermissions(roleUids, permissionClassList);
    Assert.assertEquals("2 Permissions", 2, result.size());
  }

  @Test
  public void testAddRolePermissionsSpeaker1() throws Exception {
    Set<Class<? extends Permission>> permissionClassList = new HashSet<>();
    permissionClassList.add(FilePermission.class);
    permissionClassList.add(ReadRunnerPermission.class);
    permissionClassList.add(CreateRunnerPermission.class);
    List<Long> roleUids = new ArrayList<Long>();
    roleUids.add(RoleCodeType.SpeakerCode.ID);
    Set<Class<? extends Permission>> result = PermissionUtility.addRolePermissions(roleUids, permissionClassList);
    Assert.assertEquals("3 Permissions", 3, result.size());
  }

  @Test
  public void testAddRolePermissionsSpeaker2() throws Exception {
    Set<Class<? extends Permission>> permissionClassList = new HashSet<>();
    List<Long> roleUids = new ArrayList<Long>();
    roleUids.add(RoleCodeType.SpeakerCode.ID);
    Set<Class<? extends Permission>> result = PermissionUtility.addRolePermissions(roleUids, permissionClassList);
    Assert.assertEquals("2 Permissions", 2, result.size());
  }

  @Test
  public void testAddRolePermissionsECardDownload() throws Exception {
    Set<Class<? extends Permission>> permissionClassList = new HashSet<>();
    permissionClassList.add(EventsOutlinePermission.class);
    permissionClassList.add(SettingsOutlinePermission.class);
    permissionClassList.add(RegistrationOutlinePermission.class);
    permissionClassList.add(ResultsOutlinePermission.class);
    List<Long> roleUids = new ArrayList<Long>();
    roleUids.add(RoleCodeType.ECardDownloadCode.ID);
    Set<Class<? extends Permission>> result = PermissionUtility.addRolePermissions(roleUids, permissionClassList);
    Assert.assertEquals("1 Permission", 1, result.size());
  }

  @Test
  public void testAddRolePermissionsRegistration() throws Exception {
    Set<Class<? extends Permission>> permissionClassList = new HashSet<>();
    permissionClassList.add(EventsOutlinePermission.class);
    permissionClassList.add(SettingsOutlinePermission.class);
    permissionClassList.add(RegistrationOutlinePermission.class);
    permissionClassList.add(ResultsOutlinePermission.class);
    List<Long> roleUids = new ArrayList<Long>();
    roleUids.add(RoleCodeType.RegistrationCode.ID);
    Set<Class<? extends Permission>> result = PermissionUtility.addRolePermissions(roleUids, permissionClassList);
    Assert.assertEquals("1 Permission", 1, result.size());
  }

  @Test
  public void testAddRolePermissionsUnknown() throws Exception {
    Set<Class<? extends Permission>> permissionClassList = new HashSet<>();
    List<Long> roleUids = new ArrayList<Long>();
    roleUids.add(-1L);
    Set<Class<? extends Permission>> result = PermissionUtility.addRolePermissions(roleUids, permissionClassList);
    Assert.assertEquals("0 Permissions", 0, result.size());
  }

}
