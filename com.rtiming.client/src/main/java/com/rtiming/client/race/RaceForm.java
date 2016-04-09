package com.rtiming.client.race;

import java.util.Date;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.booleanfield.AbstractBooleanField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.client.ui.form.fields.tabbox.AbstractTabBox;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.BooleanUtility;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

import com.rtiming.client.club.AbstractClubField;
import com.rtiming.client.common.help.AbstractHelpLinkButton;
import com.rtiming.client.common.ui.fields.AbstractMillisecondsDateTimeField;
import com.rtiming.client.race.RaceForm.MainBox.BibNumberField;
import com.rtiming.client.race.RaceForm.MainBox.CancelButton;
import com.rtiming.client.race.RaceForm.MainBox.ClubField;
import com.rtiming.client.race.RaceForm.MainBox.EventNrField;
import com.rtiming.client.race.RaceForm.MainBox.NationField;
import com.rtiming.client.race.RaceForm.MainBox.OkButton;
import com.rtiming.client.race.RaceForm.MainBox.RunnerNrField;
import com.rtiming.client.race.RaceForm.MainBox.TabBox.AddressBox;
import com.rtiming.client.race.RaceForm.MainBox.TabBox.RaceStatusBox;
import com.rtiming.client.race.RaceForm.MainBox.TabBox.RaceStatusBox.ECardNrField;
import com.rtiming.client.race.RaceForm.MainBox.TabBox.RaceStatusBox.LegClassUidField;
import com.rtiming.client.race.RaceForm.MainBox.TabBox.RaceStatusBox.LegFinishTimeField;
import com.rtiming.client.race.RaceForm.MainBox.TabBox.RaceStatusBox.LegStartTimeField;
import com.rtiming.client.race.RaceForm.MainBox.TabBox.RaceStatusBox.LegTimeField;
import com.rtiming.client.race.RaceForm.MainBox.TabBox.RaceStatusBox.ManualStatusField;
import com.rtiming.client.race.RaceForm.MainBox.TabBox.RaceStatusBox.RaceStatusField;
import com.rtiming.client.runner.AbstractAddressBox;
import com.rtiming.client.settings.city.AbstractCountryField;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.database.sql.BeanUtility;
import com.rtiming.shared.common.database.sql.EventBean;
import com.rtiming.shared.common.database.sql.RaceBean;
import com.rtiming.shared.common.database.sql.RunnerBean;
import com.rtiming.shared.common.security.permission.UpdateRacePermission;
import com.rtiming.shared.ecard.ECardLookupCall;
import com.rtiming.shared.ecard.download.PunchingSystemCodeType;
import com.rtiming.shared.event.EventClassFormData;
import com.rtiming.shared.event.EventLookupCall;
import com.rtiming.shared.event.IEventClassProcessService;
import com.rtiming.shared.event.IEventProcessService;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.race.IRaceProcessService;
import com.rtiming.shared.race.IRaceService;
import com.rtiming.shared.race.RaceFormData;
import com.rtiming.shared.race.RaceStatusCodeType;
import com.rtiming.shared.runner.RunnerLookupCall;
import com.rtiming.shared.settings.city.AbstractAddressBoxData;

@FormData(value = RaceFormData.class, sdkCommand = SdkCommand.CREATE)
public class RaceForm extends AbstractForm {

  private Long raceNr;
  private Long m_rawLegTime;
  private Long m_rawLegStartTime;
  private Long m_clientNr;
  private Long m_entryNr;
  private Long m_addressNr;

  private EventBean event;
  private EventClassFormData eventClass;
  private RaceBean raceBean;
  private RunnerBean runner;

  public RaceForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Race");
  }

  public BibNumberField getBibNumberField() {
    return getFieldByClass(BibNumberField.class);
  }

  public AddressBox getAddressBox() {
    return getFieldByClass(AddressBox.class);
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  @FormData
  public Long getRaceNr() {
    return raceNr;
  }

  @FormData
  public void setRaceNr(Long raceNr) {
    this.raceNr = raceNr;
  }

  @FormData
  public RunnerBean getRunner() {
    return runner;
  }

  @FormData
  public void setRunner(RunnerBean runner) {
    this.runner = runner;
  }

  public void startModify(boolean persistent) throws ProcessingException {
    startInternal(new ModifyHandler(persistent));
  }

  public void startModify() throws ProcessingException {
    startInternal(new ModifyHandler(true));
  }

  public void startNew() throws ProcessingException {
    startInternal(new NewHandler());
  }

  public ClubField getClubField() {
    return getFieldByClass(ClubField.class);
  }

  public RaceStatusBox getRaceStatusBox() {
    return getFieldByClass(RaceStatusBox.class);
  }

  public ECardNrField getECardNrField() {
    return getFieldByClass(ECardNrField.class);
  }

  public EventNrField getEventNrField() {
    return getFieldByClass(EventNrField.class);
  }

  public LegClassUidField getLegClassUidField() {
    return getFieldByClass(LegClassUidField.class);
  }

  public LegFinishTimeField getLegFinishTimeField() {
    return getFieldByClass(LegFinishTimeField.class);
  }

  public LegStartTimeField getLegStartTimeField() {
    return getFieldByClass(LegStartTimeField.class);
  }

  public LegTimeField getLegTimeField() {
    return getFieldByClass(LegTimeField.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public ManualStatusField getManualStatusField() {
    return getFieldByClass(ManualStatusField.class);
  }

  public NationField getNationField() {
    return getFieldByClass(NationField.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  public RaceStatusField getRaceStatusField() {
    return getFieldByClass(RaceStatusField.class);
  }

  public RunnerNrField getRunnerNrField() {
    return getFieldByClass(RunnerNrField.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class EventNrField extends AbstractSmartField<Long> {

      @Override
      protected boolean getConfiguredEnabled() {
        return false;
      }

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Event");
      }

      @Override
      protected Class<? extends LookupCall<Long>> getConfiguredLookupCall() {
        return EventLookupCall.class;
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

    }

    @Order(20.0)
    public class RunnerNrField extends AbstractSmartField<Long> {

      @Override
      protected boolean getConfiguredEnabled() {
        return false;
      }

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Runner");
      }

      @Override
      protected Class<? extends LookupCall<Long>> getConfiguredLookupCall() {
        return RunnerLookupCall.class;
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }
    }

    @Order(50.0)
    public class BibNumberField extends AbstractStringField {

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("BibNumber");
      }

      @Override
      protected int getConfiguredMaxLength() {
        return 60;
      }
    }

    @Order(60.0)
    public class NationField extends AbstractCountryField {

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Nation");
      }

    }

    @Order(70.0)
    public class ClubField extends AbstractClubField {

    }

    @Order(100.0)
    public class TabBox extends AbstractTabBox {

      @Order(10.0)
      public class RaceStatusBox extends AbstractGroupBox {

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("ControlStatus");
        }

        @Order(10.0)
        public class LegClassUidField extends AbstractSmartField<Long> {

          @Override
          protected boolean getConfiguredEnabled() {
            return false;
          }

          @Override
          protected String getConfiguredLabel() {
            return Texts.get("Class");
          }

          @Override
          protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
            return ClassCodeType.class;
          }

          @Override
          protected boolean getConfiguredMandatory() {
            return true;
          }
        }

        @Order(20.0)
        public class LegFinishTimeField extends AbstractMillisecondsDateTimeField {

          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("FinishTime");
          }

          @Override
          public Date getConfiguredDefaultDate() {
            return event.getEvtZero();
          }

          @Override
          protected void execChangedValue() throws ProcessingException {
            calculateLegTime();
          }

        }

        @Order(30.0)
        public class LegStartTimeField extends AbstractMillisecondsDateTimeField {

          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("StartTime");
          }

          @Override
          public Date getConfiguredDefaultDate() {
            return event.getEvtZero();
          }

          @Override
          protected void execChangedValue() throws ProcessingException {
            if (!getForm().isFormLoading()) {
              Long legTime = null;
              Long finish = FMilaUtility.getDateDifferenceInMilliSeconds(event.getEvtZero(), getLegFinishTimeField().getValue());
              Long start = FMilaUtility.getDateDifferenceInMilliSeconds(event.getEvtZero(), getLegStartTimeField().getValue());
              if (finish != null && start != null) {
                legTime = finish - start;
              }
              setRawLegTime(legTime);
              formatTimeFields();
            }
          }

        }

        @Order(40.0)
        public class LegTimeField extends AbstractStringField {

          @Override
          protected boolean getConfiguredEnabled() {
            return false;
          }

          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("Time");
          }
        }

        @Order(50.0)
        public class ECardNrField extends AbstractSmartField<Long> {

          @Override
          protected String getConfiguredLabel() {
            return Texts.get("ECard");
          }

          @Override
          protected Class<? extends LookupCall<Long>> getConfiguredLookupCall() {
            return ECardLookupCall.class;
          }
        }

        @Order(60.0)
        public class RaceStatusField extends AbstractSmartField<Long> {

          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("RaceStatus");
          }

          @Override
          protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
            return RaceStatusCodeType.class;
          }

          @Override
          protected void execChangedValue() throws ProcessingException {
            validateManualStatus();
          }

        }

        @Order(70.0)
        public class ManualStatusField extends AbstractBooleanField {

          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("ManualRaceStatus");
          }

          @Override
          protected void execChangedValue() throws ProcessingException {
            if (BooleanUtility.nvl(getManualStatusField().getValue()) == false && getRaceNr() != null) {
              RaceBean bean = BEANS.get(IRaceService.class).validateRace(getRaceNr());
              getRaceStatusField().setValue(bean.getStatusUid());
              setRawLegStartTime(bean.getLegStartTime());
              setRawLegTime(bean.getLegTime());
              formatTimeFields();
            }
            validateManualStatus();
            validateTimeFieldsEditable();
          }

        }
      }

      @Order(20.0)
      @FormData(value = AbstractAddressBoxData.class, sdkCommand = SdkCommand.CREATE)
      public class AddressBox extends AbstractAddressBox {

      }

    }

    @Order(108.0)
    public class HelpLink extends AbstractHelpLinkButton {
    }

    @Order(110.0)
    public class OkButton extends AbstractOkButton {
    }

    @Order(120.0)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  public class ModifyHandler extends AbstractFormHandler {

    private final boolean usePersistentStorage;

    public ModifyHandler(boolean usePersistentStorage) {
      this.usePersistentStorage = usePersistentStorage;
    }

    @Override
    public void execLoad() throws ProcessingException {
      RaceFormData formData = new RaceFormData();
      exportFormData(formData);
      if (usePersistentStorage) {
        IRaceProcessService service = BEANS.get(IRaceProcessService.class);
        raceBean = service.load(BeanUtility.raceFormData2bean(formData));
      }
      formData = BeanUtility.raceBean2formData(raceBean);
      importFormData(formData);
      setEnabledPermission(new UpdateRacePermission());

      initSettings();
      formatTimeFields();
    }

    @Override
    protected void execPostLoad() throws ProcessingException {
      validateManualStatus();
    }

    @Override
    public void execStore() throws ProcessingException {
      RaceFormData formData = new RaceFormData();
      exportFormData(formData);
      if (formData.getLegStartTime().getValue() == null) {
        formData.setRawLegStartTime(null);
      }
      if (formData.getLegFinishTime().getValue() == null) {
        formData.setRawLegTime(null);
      }
      raceBean = BeanUtility.raceFormData2bean(formData);
      if (usePersistentStorage) {
        IRaceProcessService service = BEANS.get(IRaceProcessService.class);
        formData = BeanUtility.raceBean2formData(service.store(raceBean));
      }
    }
  }

  public RaceBean getRaceBean() {
    return raceBean;
  }

  public void setRaceBean(RaceBean raceBean) {
    this.raceBean = raceBean;
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IRaceProcessService service = BEANS.get(IRaceProcessService.class);
      RaceFormData formData = new RaceFormData();
      exportFormData(formData);
      formData = BeanUtility.raceBean2formData(service.prepareCreate(BeanUtility.raceFormData2bean(formData)));
      importFormData(formData);

      initSettings();
    }

    @Override
    public void execStore() throws ProcessingException {
      IRaceProcessService service = BEANS.get(IRaceProcessService.class);
      RaceFormData formData = new RaceFormData();
      exportFormData(formData);
      formData = BeanUtility.raceBean2formData(service.create(BeanUtility.raceFormData2bean(formData)));
      importFormData(formData);
    }
  }

  @FormData
  public Long getRawLegTime() {
    return m_rawLegTime;
  }

  @FormData
  public void setRawLegTime(Long legTime) {
    m_rawLegTime = legTime;
  }

  @FormData
  public Long getRawLegStartTime() {
    return m_rawLegStartTime;
  }

  @FormData
  public void setRawLegStartTime(Long legStartTime) {
    m_rawLegStartTime = legStartTime;
  }

  @FormData
  public Long getClientNr() {
    return m_clientNr;
  }

  @FormData
  public void setClientNr(Long clientNr) {
    m_clientNr = clientNr;
  }

  @FormData
  public Long getEntryNr() {
    return m_entryNr;
  }

  @FormData
  public void setEntryNr(Long entryNr) {
    m_entryNr = entryNr;
  }

  @FormData
  public Long getAddressNr() {
    return m_addressNr;
  }

  @FormData
  public void setAddressNr(Long addressNr) {
    m_addressNr = addressNr;
  }

  private void validateManualStatus() {
    getRaceStatusField().setEnabled(BooleanUtility.nvl(getManualStatusField().getValue()));
    getRaceStatusField().setMandatory(BooleanUtility.nvl(getManualStatusField().getValue()));

    boolean ok = CompareUtility.equals(getRaceStatusField().getValue(), RaceStatusCodeType.OkCode.ID);
    getLegStartTimeField().setMandatory(ok);
    getLegFinishTimeField().setMandatory(ok);
    getLegTimeField().setMandatory(ok);
  }

  private void initSettings() throws ProcessingException {
    Long eventNr = getEventNrField().getValue();
    event = new EventBean();
    event.setEventNr(eventNr);
    event = BEANS.get(IEventProcessService.class).load(event);

    eventClass = new EventClassFormData();
    eventClass.getEvent().setValue(event.getEventNr());
    eventClass.getClazz().setValue(getLegClassUidField().getValue());
    eventClass = BEANS.get(IEventClassProcessService.class).load(eventClass);

    validateManualPunching();
    validateTimeFieldsEditable();
  }

  private void validateManualPunching() {
    boolean isManualPunching = CompareUtility.equals(event.getPunchingSystemUid(), PunchingSystemCodeType.PunchingSystemNoneCode.ID);
    if (isManualPunching) {
      getManualStatusField().setEnabled(false);
      getManualStatusField().setValue(true);
    }
  }

  private void validateTimeFieldsEditable() {
    boolean timeFieldEditable = BooleanUtility.nvl(getManualStatusField().getValue());
    getLegStartTimeField().setEnabled(timeFieldEditable);
    getLegFinishTimeField().setEnabled(timeFieldEditable);
  }

  private void formatTimeFields() throws ProcessingException {
    if (getRawLegStartTime() != null && !getLegStartTimeField().isValueChanging()) {
      Date startTime = FMilaUtility.addMilliSeconds(event.getEvtZero(), getRawLegStartTime());
      getLegStartTimeField().setValue(startTime);
    }

    if (getRawLegStartTime() != null && getRawLegTime() != null && !getLegFinishTimeField().isValueChanging()) {
      Date finishTime = FMilaUtility.addMilliSeconds(event.getEvtZero(), getRawLegStartTime() + getRawLegTime());
      getLegFinishTimeField().setValue(finishTime);
    }

    String time = FMilaUtility.formatTime(getRawLegTime(), eventClass.getTimePrecision().getValue());
    if (StringUtility.isNullOrEmpty(time)) {
      time = null;
    }
    getLegTimeField().setValue(time);
  }

  private void calculateLegTime() throws ProcessingException {
    if (!isFormLoading()) {
      Long legTime = null;
      Long finish = FMilaUtility.getDateDifferenceInMilliSeconds(event.getEvtZero(), getLegFinishTimeField().getValue());
      Long start = FMilaUtility.getDateDifferenceInMilliSeconds(event.getEvtZero(), getLegStartTimeField().getValue());
      if (finish != null && start != null) {
        legTime = finish - start;
      }
      setRawLegTime(legTime);
      formatTimeFields();
    }
  }
}
