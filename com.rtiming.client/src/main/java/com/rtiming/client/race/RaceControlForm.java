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
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

import com.rtiming.client.common.help.AbstractHelpLinkButton;
import com.rtiming.client.common.ui.fields.AbstractDefaultDateTimeField;
import com.rtiming.client.common.ui.fields.AbstractDefaultDateTimeWithSecondsField;
import com.rtiming.client.common.ui.fields.AbstractSortCodeField;
import com.rtiming.client.common.ui.fields.AbstractTimeDifferenceField;
import com.rtiming.client.event.course.AbstractControlField;
import com.rtiming.client.event.course.ControlStatusManualLookupCall;
import com.rtiming.client.race.RaceControlForm.MainBox.CancelButton;
import com.rtiming.client.race.RaceControlForm.MainBox.ControlField;
import com.rtiming.client.race.RaceControlForm.MainBox.ControlStatusField;
import com.rtiming.client.race.RaceControlForm.MainBox.ManualStatusField;
import com.rtiming.client.race.RaceControlForm.MainBox.OkButton;
import com.rtiming.client.race.RaceControlForm.MainBox.RaceField;
import com.rtiming.client.race.RaceControlForm.MainBox.ShiftTimeField;
import com.rtiming.client.race.RaceControlForm.MainBox.SortCodeField;
import com.rtiming.client.race.RaceControlForm.MainBox.TimeField;
import com.rtiming.client.race.RaceControlForm.MainBox.ZeroTimeField;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.UpdateRaceControlPermission;
import com.rtiming.shared.race.IRaceControlProcessService;
import com.rtiming.shared.race.RaceControlFormData;
import com.rtiming.shared.race.RaceLookupCall;

@FormData(value = RaceControlFormData.class, sdkCommand = SdkCommand.CREATE)
public class RaceControlForm extends AbstractForm {

  private Long raceControlNr;
  private Long m_courseControlNr;
  private Long legStartTime;

  public RaceControlForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("RaceControl");
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  @FormData
  public Long getRaceControlNr() {
    return raceControlNr;
  }

  @FormData
  public void setRaceControlNr(Long raceControlNr) {
    this.raceControlNr = raceControlNr;
  }

  @FormData
  public void setLegStartTime(Long legStartTime) {
    this.legStartTime = legStartTime;
  }

  @FormData
  public Long getLegStartTime() {
    return legStartTime;
  }

  public void startModify() throws ProcessingException {
    startInternal(new ModifyHandler());
  }

  public void startNew() throws ProcessingException {
    startInternal(new NewHandler());
  }

  public ControlField getControlField() {
    return getFieldByClass(ControlField.class);
  }

  public ControlStatusField getControlStatusField() {
    return getFieldByClass(ControlStatusField.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public ManualStatusField getManualStatusField() {
    return getFieldByClass(ManualStatusField.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  public RaceField getRaceField() {
    return getFieldByClass(RaceField.class);
  }

  public ShiftTimeField getShiftTimeField() {
    return getFieldByClass(ShiftTimeField.class);
  }

  public SortCodeField getSortCodeField() {
    return getFieldByClass(SortCodeField.class);
  }

  public TimeField getTimeField() {
    return getFieldByClass(TimeField.class);
  }

  public ZeroTimeField getZeroTimeField() {
    return getFieldByClass(ZeroTimeField.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class RaceField extends AbstractSmartField<Long> {

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
        return RaceLookupCall.class;
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }
    }

    @Order(20.0)
    public class ZeroTimeField extends AbstractDefaultDateTimeField {

      @Override
      protected boolean getConfiguredEnabled() {
        return false;
      }

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("ZeroTime");
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

    @Order(30.0)
    public class TimeField extends AbstractDefaultDateTimeWithSecondsField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Time");
      }

      @Override
      protected boolean getConfiguredEnabled() {
        return false;
      }

      @Override
      public Date getConfiguredDefaultDate() {
        return DateUtility.nvl(getZeroTimeField().getValue(), new Date());
      }

    }

    @Order(40.0)
    public class ShiftTimeField extends AbstractTimeDifferenceField {

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("ShiftTime");
      }

    }

    @Order(50.0)
    public class ControlField extends AbstractControlField {

      @Override
      protected boolean getConfiguredEnabled() {
        return false;
      }

      @Override
      protected Long getControlEventNr() {
        // we use the field disabled only, therefore null is ok
        return null;
      }
    }

    @Order(60.0)
    public class SortCodeField extends AbstractSortCodeField {

      @Override
      protected boolean getConfiguredEnabled() {
        return false;
      }

    }

    @Order(70.0)
    public class ControlStatusField extends AbstractSmartField<Long> {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("ControlStatus");
      }

      @Override
      protected Class<? extends LookupCall<Long>> getConfiguredLookupCall() {
        return ControlStatusManualLookupCall.class;
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }
    }

    @Order(80.0)
    public class ManualStatusField extends AbstractBooleanField {

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("ManualControlStatus");
      }

      @Override
      protected void execChangedValue() throws ProcessingException {
        validateManualStatus();
      }

    }

    @Order(88.0)
    public class HelpLink extends AbstractHelpLinkButton {
    }

    @Order(90.0)
    public class OkButton extends AbstractOkButton {
    }

    @Order(100.0)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  public class ModifyHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IRaceControlProcessService service = BEANS.get(IRaceControlProcessService.class);
      RaceControlFormData formData = new RaceControlFormData();
      exportFormData(formData);
      formData = service.load(formData);
      importFormData(formData);
      setEnabledPermission(new UpdateRaceControlPermission());
    }

    @Override
    public void execStore() throws ProcessingException {
      IRaceControlProcessService service = BEANS.get(IRaceControlProcessService.class);
      RaceControlFormData formData = new RaceControlFormData();
      exportFormData(formData);
      formData = service.store(formData);
    }

    @Override
    protected void execPostLoad() throws ProcessingException {
      validateManualStatus();
    }

  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IRaceControlProcessService service = BEANS.get(IRaceControlProcessService.class);
      RaceControlFormData formData = new RaceControlFormData();
      exportFormData(formData);
      formData = service.prepareCreate(formData);
      importFormData(formData);
    }

    @Override
    public void execStore() throws ProcessingException {
      IRaceControlProcessService service = BEANS.get(IRaceControlProcessService.class);
      RaceControlFormData formData = new RaceControlFormData();
      exportFormData(formData);
      formData = service.create(formData);
      importFormData(formData);
    }

    @Override
    protected void execPostLoad() throws ProcessingException {
      validateManualStatus();
    }

  }

  private void validateManualStatus() {
    ControlStatusField controlStatusField = getControlStatusField();
    TimeField timeField = getTimeField();
    ManualStatusField manualStatusField = getManualStatusField();

    boolean reset = RaceControlFormUtility.validateManualStatus(manualStatusField, controlStatusField, timeField);
    if (reset) {
      controlStatusField.setValue(controlStatusField.getInitValue());
      timeField.setValue(timeField.getInitValue());
    }
  }

  @FormData
  public Long getCourseControlNr() {
    return m_courseControlNr;
  }

  @FormData
  public void setCourseControlNr(Long courseControlNr) {
    m_courseControlNr = courseControlNr;
  }

}
