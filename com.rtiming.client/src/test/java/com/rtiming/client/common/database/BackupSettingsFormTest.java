package com.rtiming.client.common.database;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.testing.client.ScoutClientAssert;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.TestClientSession;
import com.rtiming.shared.settings.IDefaultProcessService;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class BackupSettingsFormTest {

  @Test
  public void testValidation() throws Exception {
    BackupSettingsForm form = new BackupSettingsForm();
    form.startNew();

    form.getScheduledBackupsField().setValue(true);
    ScoutClientAssert.assertEnabled(form.getBackupIntervalField());
    ScoutClientAssert.assertEnabled(form.getBackupDirectoryField());

    form.getScheduledBackupsField().setValue(false);
    ScoutClientAssert.assertDisabled(form.getBackupIntervalField());
    ScoutClientAssert.assertEnabled(form.getBackupDirectoryField());

    form.doClose();
  }

  @Test
  public void testIntervalZero() throws Exception {
    BackupSettingsForm form = new BackupSettingsForm();
    form.startNew();

    form.getScheduledBackupsField().setValue(true);
    form.getBackupIntervalField().setValue(0L);
    ScoutClientAssert.assertInvalid(form.getBackupIntervalField());

    form.doClose();
  }

  @Test
  public void testIntervalOk() throws Exception {
    BackupSettingsForm form = new BackupSettingsForm();
    form.startNew();

    form.getScheduledBackupsField().setValue(true);
    form.getBackupIntervalField().setValue(1L);
    ScoutClientAssert.assertValid(form.getBackupIntervalField());

    form.getBackupIntervalField().setValue(30L);
    ScoutClientAssert.assertValid(form.getBackupIntervalField());

    form.doClose();
  }

  @Test
  public void testDefaultOff() throws Exception {
    IDefaultProcessService service = BEANS.get(IDefaultProcessService.class);
    service.setBackupInterval(null);
    String directory = service.getBackupDirectory();

    BackupSettingsForm form = new BackupSettingsForm();
    form.startNew();

    ScoutClientAssert.assertDisabled(form.getBackupIntervalField());
    ScoutClientAssert.assertEnabled(form.getBackupDirectoryField());
    Assert.assertFalse("Backup active", form.getScheduledBackupsField().getValue());
    Assert.assertEquals("Directory", directory, form.getBackupDirectoryField().getValue());
    Assert.assertNull("Interval", form.getBackupIntervalField().getValue());

    form.doClose();
  }

  @Test
  public void testDefaultOn() throws Exception {
    IDefaultProcessService service = BEANS.get(IDefaultProcessService.class);
    service.setBackupInterval(29L);
    String directory = service.getBackupDirectory();

    BackupSettingsForm form = new BackupSettingsForm();
    form.startNew();

    ScoutClientAssert.assertEnabled(form.getBackupIntervalField());
    ScoutClientAssert.assertEnabled(form.getBackupDirectoryField());
    Assert.assertTrue("Backup active", form.getScheduledBackupsField().getValue());
    Assert.assertEquals("Directory", directory, form.getBackupDirectoryField().getValue());
    Assert.assertEquals("Interval", 29, form.getBackupIntervalField().getValue().longValue());

    form.doClose();
  }

  @Test
  public void testIntervalNegative() throws Exception {
    BackupSettingsForm form = new BackupSettingsForm();
    form.startNew();

    form.getScheduledBackupsField().setValue(true);
    form.getBackupIntervalField().setValue(-5L);
    ScoutClientAssert.assertInvalid(form.getBackupIntervalField());

    form.doClose();
  }

  @AfterClass
  public static void afterClass() throws ProcessingException {
    IDefaultProcessService service = BEANS.get(IDefaultProcessService.class);
    service.setBackupInterval(null);
  }

}
