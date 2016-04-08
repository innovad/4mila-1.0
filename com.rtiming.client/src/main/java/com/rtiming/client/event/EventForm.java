package com.rtiming.client.event;

import java.util.Date;
import java.util.TimeZone;

import org.eclipse.scout.commons.annotations.FormData;
import org.eclipse.scout.commons.annotations.FormData.SdkCommand;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.IValueField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

import com.rtiming.client.common.help.AbstractHelpLinkButton;
import com.rtiming.client.common.ui.fields.AbstractDefaultDateTimeField;
import com.rtiming.client.common.ui.fields.AbstractImageGroupBox;
import com.rtiming.client.event.EventForm.MainBox.CancelButton;
import com.rtiming.client.event.EventForm.MainBox.EventTypeField;
import com.rtiming.client.event.EventForm.MainBox.FinishTimeField;
import com.rtiming.client.event.EventForm.MainBox.LocationField;
import com.rtiming.client.event.EventForm.MainBox.LogoField;
import com.rtiming.client.event.EventForm.MainBox.MappField;
import com.rtiming.client.event.EventForm.MainBox.NameField;
import com.rtiming.client.event.EventForm.MainBox.OkButton;
import com.rtiming.client.event.EventForm.MainBox.PunchingSystemField;
import com.rtiming.client.event.EventForm.MainBox.TimezoneField;
import com.rtiming.client.event.EventForm.MainBox.ZeroTimeField;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.database.sql.BeanUtility;
import com.rtiming.shared.common.security.permission.UpdateEventPermission;
import com.rtiming.shared.ecard.download.PunchingSystemCodeType;
import com.rtiming.shared.event.EventFormData;
import com.rtiming.shared.event.EventTypeCodeType;
import com.rtiming.shared.event.IEventProcessService;

@FormData(value = EventFormData.class, sdkCommand = SdkCommand.CREATE)
public class EventForm extends AbstractForm {

  private Long eventNr;
  private byte[] m_logoData;
  private String m_format;
  private Long m_clientNr;
  private Date m_evtLastUpload;

  public EventForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Event");
  }

  @FormData
  public Long getEventNr() {
    return eventNr;
  }

  @FormData
  public void setEventNr(Long eventNr) {
    this.eventNr = eventNr;
  }

  @FormData
  public String getFormat() {
    return m_format;
  }

  @FormData
  public void setFormat(String format) {
    m_format = format;
  }

  @FormData
  public byte[] getLogoData() {
    return m_logoData;
  }

  @FormData
  public void setLogoData(byte[] mapData) {
    m_logoData = mapData;
  }

  public void startModify() throws ProcessingException {
    startInternal(new ModifyHandler());
  }

  public void startNew() throws ProcessingException {
    startInternal(new NewHandler());
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  public EventTypeField getEventTypeField() {
    return getFieldByClass(EventTypeField.class);
  }

  public FinishTimeField getFinishTimeField() {
    return getFieldByClass(FinishTimeField.class);
  }

  public LocationField getLocationField() {
    return getFieldByClass(LocationField.class);
  }

  public LogoField getLogoField() {
    return getFieldByClass(LogoField.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public MappField getMappField() {
    return getFieldByClass(MappField.class);
  }

  public NameField getNameField() {
    return getFieldByClass(NameField.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  public PunchingSystemField getPunchingSystemField() {
    return getFieldByClass(PunchingSystemField.class);
  }

  public TimezoneField getTimezoneField() {
    return getFieldByClass(TimezoneField.class);
  }

  public ZeroTimeField getZeroTimeField() {
    return getFieldByClass(ZeroTimeField.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class NameField extends AbstractStringField {

      @Override
      protected String getConfiguredLabel() {
        return ScoutTexts.get("Name");
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

      @Override
      protected int getConfiguredMaxLength() {
        return 250;
      }
    }

    @Order(20.0)
    public class EventTypeField extends AbstractSmartField<Long> {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Type");
      }

      @Override
      protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
        return EventTypeCodeType.class;
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }
    }

    @Order(30.0)
    public class LocationField extends AbstractStringField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Location");
      }

      @Override
      protected int getConfiguredMaxLength() {
        return 250;
      }
    }

    @Order(40.0)
    public class MappField extends AbstractStringField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Map");
      }

      @Override
      protected int getConfiguredMaxLength() {
        return 250;
      }
    }

    @Order(50.0)
    public class ZeroTimeField extends AbstractDefaultDateTimeField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("ZeroTime");
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

    }

    @Order(60.0)
    public class FinishTimeField extends AbstractDefaultDateTimeField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("FinishClosing");
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

      @Override
      protected Class<? extends IValueField> getConfiguredMasterField() {
        return EventForm.MainBox.ZeroTimeField.class;
      }

      @Override
      protected boolean getConfiguredMasterRequired() {
        return true;
      }

      private void checkEventDates(Date evtZero, Date finishTime) {
        getZeroTimeField().clearErrorStatus();
        if (evtZero != null && finishTime != null) {
          if (evtZero.after(finishTime)) {
            getZeroTimeField().setErrorStatus(Texts.get("EventDatesErrorMessage"));
          }
        }
      }

      @Override
      protected void execChangedMasterValue(Object newMasterValue) throws ProcessingException {
        checkEventDates((Date) newMasterValue, getFinishTimeField().getValue());
      }

      @Override
      protected void execChangedValue() throws ProcessingException {
        checkEventDates(getZeroTimeField().getValue(), getFinishTimeField().getValue());
      }

    }

    @Order(70.0)
    public class TimezoneField extends AbstractSmartField<Integer> {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Timezone");
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

      @Override
      protected Class<? extends IValueField> getConfiguredMasterField() {
        return EventForm.MainBox.ZeroTimeField.class;
      }

      @Override
      protected boolean getConfiguredMasterRequired() {
        return true;
      }

      @Override
      protected void execPrepareLookup(ILookupCall call) throws ProcessingException {
        ((TimezoneLookupCall) call).setDate(getZeroTimeField().getValue());
        ((TimezoneLookupCall) call).setLongText(true);
      }

      @Override
      protected void execChangedMasterValue(Object newMasterValue) throws ProcessingException {
        Date date = DateUtility.nvl((Date) newMasterValue, new Date());
        setValue(TimeZone.getDefault().getOffset(date.getTime()));
      }

      @Override
      protected Class<? extends LookupCall<Integer>> getConfiguredLookupCall() {
        return TimezoneLookupCall.class;
      }
    }

    @Order(80.0)
    public class PunchingSystemField extends AbstractSmartField<Long> {

      @Override
      protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
        return PunchingSystemCodeType.class;
      }

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("PunchingSystem");
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }
    }

    @Order(90.0)
    public class LogoField extends AbstractImageGroupBox {

      @Override
      protected int getConfiguredGridH() {
        return 4;
      }

      @Override
      protected int getConfiguredGridW() {
        return 2;
      }

      @Override
      protected String getImageFormat() {
        return EventForm.this.getFormat();
      }

      @Override
      protected byte[] getImageData() {
        return getLogoData();
      }

      @Override
      protected void setImageFormat(String format) {
        EventForm.this.setFormat(format);
      }

      @Override
      protected void updateFileChanged(boolean fileHasChanged) {
        // not implemented yet
      }
    }

    @Order(95.0)
    public class HelpLink extends AbstractHelpLinkButton {
    }

    @Order(100.0)
    public class OkButton extends AbstractOkButton {
    }

    @Order(110.0)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  public class ModifyHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IEventProcessService service = BEANS.get(IEventProcessService.class);
      EventFormData formData = new EventFormData();
      exportFormData(formData);
      formData = BeanUtility.eventBean2formData(service.load(BeanUtility.eventFormData2bean(formData)));
      importFormData(formData);
      getLogoField().getImageField().setImage(formData.getLogoData(), formData.getFormat(), false);
      setEnabledPermission(new UpdateEventPermission());
    }

    @Override
    public void execStore() throws ProcessingException {
      IEventProcessService service = BEANS.get(IEventProcessService.class);
      EventFormData formData = new EventFormData();
      exportFormData(formData);
      formData.setLogoData(getLogoField().getImageField().getByteArrayValue());
      formData = BeanUtility.eventBean2formData(service.store(BeanUtility.eventFormData2bean(formData)));
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IEventProcessService service = BEANS.get(IEventProcessService.class);
      EventFormData formData = new EventFormData();
      exportFormData(formData);
      formData = BeanUtility.eventBean2formData(service.prepareCreate(BeanUtility.eventFormData2bean(formData)));
      importFormData(formData);
    }

    @Override
    public void execStore() throws ProcessingException {
      IEventProcessService service = BEANS.get(IEventProcessService.class);
      EventFormData formData = new EventFormData();
      exportFormData(formData);
      formData.setLogoData(getLogoField().getImageField().getByteArrayValue());
      formData = BeanUtility.eventBean2formData(service.create(BeanUtility.eventFormData2bean(formData)));
      importFormData(formData);
    }
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
  public Date getEvtLastUpload() {
    return m_evtLastUpload;
  }

  @FormData
  public void setEvtLastUpload(Date evtLastUpload) {
    m_evtLastUpload = evtLastUpload;
  }
}
