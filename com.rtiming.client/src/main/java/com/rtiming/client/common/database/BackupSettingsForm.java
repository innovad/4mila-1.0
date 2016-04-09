package com.rtiming.client.common.database;

import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.booleanfield.AbstractBooleanField;
import org.eclipse.scout.rt.client.ui.form.fields.filechooserfield.AbstractFileChooserField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.longfield.AbstractLongField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.BooleanUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.client.ClientSession;
import com.rtiming.client.FMilaClientUtility;
import com.rtiming.client.common.database.BackupSettingsForm.MainBox.BackupDirectoryField;
import com.rtiming.client.common.database.BackupSettingsForm.MainBox.BackupIntervalField;
import com.rtiming.client.common.database.BackupSettingsForm.MainBox.BackupMaxNumberField;
import com.rtiming.client.common.database.BackupSettingsForm.MainBox.ScheduledBackupsField;
import com.rtiming.shared.common.database.IDatabaseService;
import com.rtiming.shared.common.database.SharedBackupUtility;
import com.rtiming.shared.settings.IDefaultProcessService;

public class BackupSettingsForm extends AbstractForm {

  public BackupSettingsForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Backup");
  }

  public void startNew() throws ProcessingException {
    startInternal(new NewHandler());
  }

  public BackupDirectoryField getBackupDirectoryField() {
    return getFieldByClass(BackupDirectoryField.class);
  }

  public BackupIntervalField getBackupIntervalField() {
    return getFieldByClass(BackupIntervalField.class);
  }

  public BackupMaxNumberField getBackupMaxNumberField() {
    return getFieldByClass(BackupMaxNumberField.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public ScheduledBackupsField getScheduledBackupsField() {
    return getFieldByClass(ScheduledBackupsField.class);
  }

  private void validateFields(boolean reloadBackupList, boolean forceNewJob) throws ProcessingException {
    boolean backupActive = BooleanUtility.nvl(getScheduledBackupsField().getValue());
    getBackupIntervalField().setEnabled(backupActive);

    getBackupIntervalField().clearErrorStatus();
    getBackupDirectoryField().clearErrorStatus();
    if (backupActive) {
      if (getBackupIntervalField().getValue() == null) {
        getBackupIntervalField().setErrorStatus(TEXTS.get("BackupNotActiveWarning"));
      }
      if (getBackupDirectoryField().getValue().getContentLength() <= 0) {
        getBackupDirectoryField().setErrorStatus(TEXTS.get("BackupNotActiveWarning"));
      }
    }
    else {
      // not active => interval is null
      if (!getBackupIntervalField().isValueChanging()) {
        getBackupIntervalField().setValue(null);
      }
    }

    BEANS.get(IDatabaseService.class).setScheduledBackupStatus(forceNewJob, !FMilaClientUtility.isTestEnvironment());

    if (reloadBackupList && !StringUtility.isNullOrEmpty(getBackupDirectoryField().getValue().getFilename())) {
      IPage activePage = ClientSession.get().getDesktop().getOutline().getActivePage();
      if (activePage != null) {
        activePage.reloadPage();
      }
    }
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class ScheduledBackupsField extends AbstractBooleanField {

      @Override
      protected int getConfiguredGridW() {
        return 2;
      }

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("ScheduledBackups");
      }

      @Override
      protected int getConfiguredLabelWidthInPixel() {
        return 200;
      }

      @Override
      protected void execChangedValue() throws ProcessingException {
        if (getBackupIntervalField().getValue() == null) {
          getBackupIntervalField().setValue(2L);
        }
        validateFields(false, true);
      }

    }

    @Order(20.0)
    public class BackupIntervalField extends AbstractLongField {

      @Override
      protected Long getConfiguredMaxValue() {
        return 30L;
      }

      @Override
      protected Long getConfiguredMinValue() {
        return 1L;
      }

      @Override
      protected void execChangedValue() throws ProcessingException {
        Long backupInterval = getBackupIntervalField().getValue();
        BEANS.get(IDefaultProcessService.class).setBackupInterval(backupInterval);

        validateFields(false, true);
      }

      @Override
      protected int getConfiguredLabelWidthInPixel() {
        return 200;
      }

      @Override
      protected int getConfiguredGridW() {
        return 2;
      }

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("BackupInterval");
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

    }

    @Order(30.0)
    public class BackupDirectoryField extends AbstractFileChooserField {

      @Override
      protected void execChangedValue() throws ProcessingException {
        String backupDirectory = getBackupDirectoryField().getValue().getFilename();
        BEANS.get(IDefaultProcessService.class).setBackupDirectory(backupDirectory);

        validateFields(true, false);
      }

      @Override
      protected int getConfiguredGridW() {
        return 2;
      }

      @Override
      protected int getConfiguredLabelWidthInPixel() {
        return 200;
      }

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("BackupDirectory");
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

// TODO MIG      
//      @Override
//      protected boolean getConfiguredFolderMode() {
//        return true;
//      }
//
//      @Override
//      protected boolean getConfiguredShowDirectory() {
//        return true;
//      }

    }

    @Order(40.0)
    public class BackupMaxNumberField extends AbstractLongField {

      @Override
      protected int getConfiguredGridW() {
        return 2;
      }

      @Override
      protected int getConfiguredLabelWidthInPixel() {
        return 200;
      }

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("BackupMaxNumber");
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

      @Override
      protected Long getConfiguredMaxValue() {
        return 999999L;
      }

      @Override
      protected Long getConfiguredMinValue() {
        return 20L;
      }

      @Override
      protected void execChangedValue() throws ProcessingException {
        Long backupMaxNumber = getBackupMaxNumberField().getValue();
        BEANS.get(IDefaultProcessService.class).setBackupMaxNumber(backupMaxNumber);

        validateFields(false, false);
      }
    }

  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    protected void execLoad() throws ProcessingException {
      IDefaultProcessService service = BEANS.get(IDefaultProcessService.class);
      String directory = service.getBackupDirectory();
      Long interval = service.getBackupInterval();
      Long maxNumber = service.getBackupMaxNumber();

      // set directory
      try {
        getBackupDirectoryField().setValueChangeTriggerEnabled(false);
        // TODO MIG getBackupDirectoryField().setValue(directory);
      }
      finally {
        getBackupDirectoryField().setValueChangeTriggerEnabled(true);
      }

      // set interval
      try {
        getBackupIntervalField().setValueChangeTriggerEnabled(false);
        getBackupIntervalField().setValue(interval);
      }
      finally {
        getBackupIntervalField().setValueChangeTriggerEnabled(true);
      }

      // set max number
      try {
        getBackupMaxNumberField().setValueChangeTriggerEnabled(false);
        getBackupMaxNumberField().setValue(maxNumber);
      }
      finally {
        getBackupMaxNumberField().setValueChangeTriggerEnabled(true);
      }

      if (SharedBackupUtility.backupIsActive(interval)) {
        getScheduledBackupsField().setValue(true);
      }

      // validate
      validateFields(false, false);
    }

  }
}
