package com.rtiming.client.entry.startlist;

import java.util.Date;

import org.eclipse.scout.commons.CompareUtility;
import org.eclipse.scout.commons.annotations.FormData;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.listbox.AbstractListBox;
import org.eclipse.scout.rt.client.ui.form.fields.longfield.AbstractLongField;
import org.eclipse.scout.rt.client.ui.form.fields.radiobuttongroup.AbstractRadioButtonGroup;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;

import com.rtiming.client.common.help.AbstractHelpLinkButton;
import com.rtiming.client.common.ui.fields.AbstractDefaultDateTimeWithSecondsField;
import com.rtiming.client.entry.startlist.StartlistSettingForm.MainBox.BibNoBox;
import com.rtiming.client.entry.startlist.StartlistSettingForm.MainBox.BibNoBox.BibNoFromField;
import com.rtiming.client.entry.startlist.StartlistSettingForm.MainBox.BibNoBox.BibNoOrderUidField;
import com.rtiming.client.entry.startlist.StartlistSettingForm.MainBox.BibNoBox.LastBibNoField;
import com.rtiming.client.entry.startlist.StartlistSettingForm.MainBox.OptionsBox;
import com.rtiming.client.entry.startlist.StartlistSettingForm.MainBox.OptionsBox.OptionsField;
import com.rtiming.client.entry.startlist.StartlistSettingForm.MainBox.StartTimeBox;
import com.rtiming.client.entry.startlist.StartlistSettingForm.MainBox.StartTimeBox.FirstStartField;
import com.rtiming.client.entry.startlist.StartlistSettingForm.MainBox.StartTimeBox.LastStartField;
import com.rtiming.client.entry.startlist.StartlistSettingForm.MainBox.StartTimeBox.StartIntervalField;
import com.rtiming.client.entry.startlist.StartlistSettingForm.MainBox.TypeUidField;
import com.rtiming.client.entry.startlist.StartlistSettingForm.MainBox.VacantBox;
import com.rtiming.client.entry.startlist.StartlistSettingForm.MainBox.VacantBox.VacantAbsoluteField;
import com.rtiming.client.entry.startlist.StartlistSettingForm.MainBox.VacantBox.VacantPercentField;
import com.rtiming.client.entry.startlist.StartlistSettingForm.MainBox.VacantBox.VacantPositionGroup;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.UpdateStartlistSettingPermission;
import com.rtiming.shared.entry.startlist.BibNoOrderCodeType;
import com.rtiming.shared.entry.startlist.IStartlistSettingProcessService;
import com.rtiming.shared.entry.startlist.StartlistSettingFormData;
import com.rtiming.shared.entry.startlist.StartlistSettingOptionCodeType;
import com.rtiming.shared.entry.startlist.StartlistSettingUtility;
import com.rtiming.shared.entry.startlist.StartlistSettingVacantPositionCodeType;
import com.rtiming.shared.entry.startlist.StartlistTypeCodeType;

@FormData(value = StartlistSettingFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class StartlistSettingForm extends AbstractForm {

  private Long startlistSettingNr;
  private Long m_eventNr;
  private Long m_newClassUid;
  private Long m_participationCount;

  public StartlistSettingForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("StartlistSetting");
  }

  @FormData
  public Long getStartlistSettingNr() {
    return startlistSettingNr;
  }

  @FormData
  public void setStartlistSettingNr(Long startlistSettingNr) {
    this.startlistSettingNr = startlistSettingNr;
  }

  public void startModify() throws ProcessingException {
    startInternal(new StartlistSettingForm.ModifyHandler());
  }

  public void startNew() throws ProcessingException {
    startInternal(new StartlistSettingForm.NewHandler());
  }

  public BibNoBox getBibNoBox() {
    return getFieldByClass(BibNoBox.class);
  }

  public BibNoFromField getBibNoFromField() {
    return getFieldByClass(BibNoFromField.class);
  }

  public com.rtiming.client.entry.startlist.StartlistSettingForm.MainBox.CancelButton getCancelButton() {
    return getFieldByClass(com.rtiming.client.entry.startlist.StartlistSettingForm.MainBox.CancelButton.class);
  }

  public OptionsBox getOptionsBox() {
    return getFieldByClass(OptionsBox.class);
  }

  public FirstStartField getFirstStartField() {
    return getFieldByClass(FirstStartField.class);
  }

  public OptionsField getOptionsField() {
    return getFieldByClass(OptionsField.class);
  }

  public VacantPositionGroup getVacantPositionGroup() {
    return getFieldByClass(VacantPositionGroup.class);
  }

  public StartIntervalField getStartIntervalField() {
    return getFieldByClass(StartIntervalField.class);
  }

  public LastBibNoField getLastBibNoField() {
    return getFieldByClass(LastBibNoField.class);
  }

  public BibNoOrderUidField getBibNoOrderUidField() {
    return getFieldByClass(BibNoOrderUidField.class);
  }

  public LastStartField getLastStartField() {
    return getFieldByClass(LastStartField.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public StartTimeBox getStartTimeBox() {
    return getFieldByClass(StartTimeBox.class);
  }

  public TypeUidField getTypeUidField() {
    return getFieldByClass(TypeUidField.class);
  }

  public VacantAbsoluteField getVacantAbsoluteField() {
    return getFieldByClass(VacantAbsoluteField.class);
  }

  public VacantBox getVacantBox() {
    return getFieldByClass(VacantBox.class);
  }

  public VacantPercentField getVacantPercentField() {
    return getFieldByClass(VacantPercentField.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class TypeUidField extends AbstractSmartField<Long> {

      @Override
      protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
        return StartlistTypeCodeType.class;
      }

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("StartlistType");
      }

      @Override
      protected void execChangedValue() throws ProcessingException {
        validateTypeUid();
      }

    }

    @Order(20.0)
    public class StartTimeBox extends AbstractGroupBox {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("StartTime");
      }

      @Order(10.0)
      public class FirstStartField extends AbstractDefaultDateTimeWithSecondsField {

        @Override
        protected String getConfiguredLabel() {
          return Texts.get("FirstStart");
        }

        @Override
        protected boolean getConfiguredMandatory() {
          return true;
        }

        @Override
        protected void execChangedValue() throws ProcessingException {
          updateLastStart();
        }

      }

      @Order(20.0)
      public class StartIntervalField extends AbstractLongField {

        @Override
        protected String getConfiguredLabel() {
          return Texts.get("Interval");
        }

        @Override
        protected boolean getConfiguredMandatory() {
          return true;
        }

        @Override
        protected Long getConfiguredMaxValue() {
          return 3600L;
        }

        @Override
        protected Long getConfiguredMinValue() {
          return -3600L;
        }

        @Override
        protected void execChangedValue() throws ProcessingException {
          updateLastStart();
        }

      }

      @Order(30.0)
      public class LastStartField extends AbstractDefaultDateTimeWithSecondsField {

        @Override
        protected String getConfiguredLabel() {
          return Texts.get("LastStart");
        }

        @Override
        protected boolean getConfiguredMandatory() {
          return true;
        }

        @Override
        protected void execChangedValue() throws ProcessingException {
          updateFirstStart();
        }
      }
    }

    @Order(30.0)
    public class VacantBox extends AbstractGroupBox {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Vacant");
      }

      @Order(10.0)
      public class VacantPercentField extends AbstractLongField {

        @Override
        protected String getConfiguredLabel() {
          return Texts.get("VacantPercentageOfRunners");
        }

        @Override
        protected boolean getConfiguredMandatory() {
          return true;
        }

        @Override
        protected Long getConfiguredMaxValue() {
          return 100L;
        }

        @Override
        protected Long getConfiguredMinValue() {
          return 0L;
        }

        @Override
        protected void execChangedValue() throws ProcessingException {
          updateLastBibNo();
          updateLastStart();
        }
      }

      @Order(20.0)
      public class VacantAbsoluteField extends AbstractLongField {

        @Override
        protected String getConfiguredLabel() {
          return Texts.get("VacantAbsolute");
        }

        @Override
        protected boolean getConfiguredMandatory() {
          return true;
        }

        @Override
        protected Long getConfiguredMaxValue() {
          return 1000L;
        }

        @Override
        protected Long getConfiguredMinValue() {
          return 0L;
        }

        @Override
        protected void execChangedValue() throws ProcessingException {
          updateLastBibNo();
          updateLastStart();
        }

      }

      @Order(30.0)
      public class VacantPositionGroup extends AbstractRadioButtonGroup<Long> {

        @Override
        protected int getConfiguredGridW() {
          return 2;
        }

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Position");
        }

        @Override
        protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
          return StartlistSettingVacantPositionCodeType.class;
        }

      }

    }

    @Order(40.0)
    public class BibNoBox extends AbstractGroupBox {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("BibNumber");
      }

      @Order(10.0)
      public class BibNoFromField extends AbstractLongField {

        @Override
        protected String getConfiguredLabel() {
          return Texts.get("BibNumber");
        }

        @Override
        protected void execChangedValue() throws ProcessingException {
          updateLastBibNo();
          if (getBibNoFromField().getValue() == null) {
            getBibNoOrderUidField().setValue(BibNoOrderCodeType.AscendingCode.ID);
            getBibNoOrderUidField().setEnabled(false);
            getLastBibNoField().setValue(null);
          }
          else {
            getBibNoOrderUidField().setEnabled(true);
          }
        }

      }

      @Order(20.0)
      public class LastBibNoField extends AbstractLongField {

        @Override
        protected boolean getConfiguredEnabled() {
          return false;
        }

        @Override
        protected String getConfiguredLabel() {
          return Texts.get("LastBibNumber");
        }
      }

      @Order(30.0)
      public class BibNoOrderUidField extends AbstractSmartField<Long> {

        @Override
        protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
          return BibNoOrderCodeType.class;
        }

        @Override
        protected String getConfiguredLabel() {
          return Texts.get("SortCode");
        }

        @Override
        protected boolean getConfiguredMandatory() {
          return true;
        }

        @Override
        protected void execChangedValue() throws ProcessingException {
          updateLastBibNo();
        }

      }
    }

    @Order(50.0)
    public class OptionsBox extends AbstractGroupBox {

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Options");
      }

      @Order(10.0)
      public class OptionsField extends AbstractListBox<Long> {

        @Override
        protected int getConfiguredGridH() {
          return 4;
        }

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Options");
        }

        @Override
        protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
          return StartlistSettingOptionCodeType.class;
        }
      }
    }

    @Order(58.0)
    public class HelpLink extends AbstractHelpLinkButton {
    }

    @Order(60.0)
    public class OkButton extends AbstractOkButton {
    }

    @Order(70.0)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  public class ModifyHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IStartlistSettingProcessService service = BEANS.get(IStartlistSettingProcessService.class);
      StartlistSettingFormData formData = new StartlistSettingFormData();
      exportFormData(formData);
      formData = service.load(formData);
      importFormData(formData);
      setEnabledPermission(new UpdateStartlistSettingPermission());

      validateTypeUid();
      getFirstStartField().execChangedValue();
      getBibNoFromField().execChangedValue();
    }

    @Override
    public void execStore() throws ProcessingException {
      IStartlistSettingProcessService service = BEANS.get(IStartlistSettingProcessService.class);
      StartlistSettingFormData formData = new StartlistSettingFormData();
      exportFormData(formData);
      formData = service.store(formData);
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IStartlistSettingProcessService service = BEANS.get(IStartlistSettingProcessService.class);
      StartlistSettingFormData formData = new StartlistSettingFormData();
      exportFormData(formData);
      formData = service.prepareCreate(formData);
      importFormData(formData);

      getFirstStartField().execChangedValue();
      getBibNoFromField().execChangedValue();
    }

    @Override
    protected void execPostLoad() throws ProcessingException {
      touch();
      validateTypeUid();
    }

    @Override
    public void execStore() throws ProcessingException {
      IStartlistSettingProcessService service = BEANS.get(IStartlistSettingProcessService.class);
      StartlistSettingFormData formData = new StartlistSettingFormData();
      exportFormData(formData);
      formData = service.create(formData);
      importFormData(formData);
    }
  }

  @FormData
  public Long getEventNr() {
    return m_eventNr;
  }

  @FormData
  public void setEventNr(Long eventNr) {
    m_eventNr = eventNr;
  }

  @FormData
  public Long getNewClassUid() {
    return m_newClassUid;
  }

  @FormData
  public void setNewClassUid(Long newClassUid) {
    m_newClassUid = newClassUid;
  }

  @FormData
  public Long getParticipationCount() {
    return m_participationCount;
  }

  @FormData
  public void setParticipationCount(Long participationCount) {
    m_participationCount = participationCount;
  }

  private void updateLastStart() {
    try {
      getLastStartField().setValueChangeTriggerEnabled(false);
      Date firstStart = getFirstStartField().getValue();
      Long interval = getStartIntervalField().getValue();
      Long entriesCount = getParticipationCount();
      Date lastStart = StartlistSettingUtility.calculateLastStart(firstStart, interval, entriesCount, getVacantPercentField().getValue(), getVacantAbsoluteField().getValue());
      getLastStartField().setValue(lastStart);
    }
    finally {
      getLastStartField().setValueChangeTriggerEnabled(true);
    }
  }

  private void updateFirstStart() {
    try {
      if (!getFirstStartField().isValueChanging()) {
        getFirstStartField().setValueChangeTriggerEnabled(false);
        Date lastStart = getLastStartField().getValue();
        Long interval = getStartIntervalField().getValue();
        Long entriesCount = getParticipationCount();
        Date firstStart = StartlistSettingUtility.calculateFirstStart(lastStart, interval, entriesCount, getVacantPercentField().getValue(), getVacantAbsoluteField().getValue());
        getFirstStartField().setValue(firstStart);
      }
    }
    finally {
      getFirstStartField().setValueChangeTriggerEnabled(true);
    }
  }

  private void updateLastBibNo() {
    if (getBibNoFromField().getValue() != null && getBibNoOrderUidField().getValue() != null) {
      int nextBibNo = 1;
      if (CompareUtility.equals(getBibNoOrderUidField().getValue(), BibNoOrderCodeType.DescendingCode.ID)) {
        nextBibNo = -1;
      }
      Long entriesCount = NumberUtility.nvl(getParticipationCount(), 0L);
      Long totalCount = entriesCount + StartlistSettingUtility.calculateVacantCount(getParticipationCount(), getVacantPercentField().getValue(), getVacantAbsoluteField().getValue());
      getLastBibNoField().setValue(getBibNoFromField().getValue() + nextBibNo * (Math.max(totalCount - 1, 0)));
    }
  }

  private void validateTypeUid() {
    if (CompareUtility.equals(getTypeUidField().getValue(), StartlistTypeCodeType.DrawingCode.ID)) {
      // Drawing Box
      getOptionsBox().setVisible(true);
      getVacantPositionGroup().setVisible(true);

      // Interval
      getStartIntervalField().setEnabled(true);
    }
    else if (CompareUtility.equals(getTypeUidField().getValue(), StartlistTypeCodeType.MassStartCode.ID)) {
      // Drawing Box
      getOptionsBox().setVisible(false);
      getOptionsField().uncheckAllKeys();
      getVacantPositionGroup().setVisible(false);
      getVacantPositionGroup().setValue(StartlistSettingVacantPositionCodeType.EarlyStartCode.ID);

      // Interval
      getStartIntervalField().setValue(0L);
      getStartIntervalField().setEnabled(false);
    }
    else if (CompareUtility.equals(getTypeUidField().getValue(), StartlistTypeCodeType.SingleEventCode.ID)) {
      // Drawing Box
      getOptionsBox().setVisible(true);

      // Interval
      getStartIntervalField().setEnabled(true);
    }
    else if (CompareUtility.equals(getTypeUidField().getValue(), StartlistTypeCodeType.MultidayEventCode.ID)) {
      // Drawing Box
      getOptionsBox().setVisible(true);

      // Interval
      getStartIntervalField().setEnabled(true);
    }
  }
}
