package com.rtiming.client.ecard.download;

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
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

import com.rtiming.client.common.help.AbstractHelpLinkButton;
import com.rtiming.client.common.ui.fields.AbstractDefaultDateTimeField;
import com.rtiming.client.common.ui.fields.AbstractMillisecondsDateTimeField;
import com.rtiming.client.ecard.AbstractECardField;
import com.rtiming.client.ecard.download.DownloadedECardForm.MainBox.CancelButton;
import com.rtiming.client.ecard.download.DownloadedECardForm.MainBox.CheckField;
import com.rtiming.client.ecard.download.DownloadedECardForm.MainBox.ClearField;
import com.rtiming.client.ecard.download.DownloadedECardForm.MainBox.ECardField;
import com.rtiming.client.ecard.download.DownloadedECardForm.MainBox.ECardStationField;
import com.rtiming.client.ecard.download.DownloadedECardForm.MainBox.EventField;
import com.rtiming.client.ecard.download.DownloadedECardForm.MainBox.EvtDownloadField;
import com.rtiming.client.ecard.download.DownloadedECardForm.MainBox.FinishField;
import com.rtiming.client.ecard.download.DownloadedECardForm.MainBox.OkButton;
import com.rtiming.client.ecard.download.DownloadedECardForm.MainBox.RaceField;
import com.rtiming.client.ecard.download.DownloadedECardForm.MainBox.RawDataField;
import com.rtiming.client.ecard.download.DownloadedECardForm.MainBox.StartField;
import com.rtiming.client.event.AbstractEventField;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.UpdateDownloadedECardPermission;
import com.rtiming.shared.ecard.ECardStationLookupCall;
import com.rtiming.shared.ecard.download.DownloadedECardFormData;
import com.rtiming.shared.ecard.download.IDownloadedECardProcessService;
import com.rtiming.shared.race.RaceLookupCall;

@FormData(value = DownloadedECardFormData.class, sdkCommand = SdkCommand.CREATE)
public class DownloadedECardForm extends AbstractForm {

  private Long punchSessionNr;
  private Long m_rawStart;
  private Long m_rawFinish;
  private Long m_rawClear;
  private Long m_rawCheck;

  public DownloadedECardForm() throws ProcessingException {
    super();
  }

  @Override
  protected void execFormActivated() throws ProcessingException {
    getRaceField().requestFocus();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("DownloadedECard");
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  @FormData
  public Long getPunchSessionNr() {
    return punchSessionNr;
  }

  @FormData
  public void setPunchSessionNr(Long punchSessionNr) {
    this.punchSessionNr = punchSessionNr;
  }

  public void startModify() throws ProcessingException {
    startInternal(new ModifyHandler());
  }

  public void startNew() throws ProcessingException {
    startInternal(new NewHandler());
  }

  public EventField getEventField() {
    return getFieldByClass(EventField.class);
  }

  public ECardField getECardField() {
    return getFieldByClass(ECardField.class);
  }

  public EvtDownloadField getEvtDownloadField() {
    return getFieldByClass(EvtDownloadField.class);
  }

  public FinishField getFinishField() {
    return getFieldByClass(FinishField.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  public RaceField getRaceField() {
    return getFieldByClass(RaceField.class);
  }

  public RawDataField getRawDataField() {
    return getFieldByClass(RawDataField.class);
  }

  public CheckField getCheckField() {
    return getFieldByClass(CheckField.class);
  }

  public ClearField getClearField() {
    return getFieldByClass(ClearField.class);
  }

  public ECardStationField getECardStationField() {
    return getFieldByClass(ECardStationField.class);
  }

  public StartField getStartField() {
    return getFieldByClass(StartField.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class EventField extends AbstractEventField {

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }
    }

    @Order(20.0)
    public class ECardField extends AbstractECardField {

      @Override
      protected boolean getConfiguredEnabled() {
        return false;
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

    }

    @Order(30.0)
    public class ECardStationField extends AbstractSmartField<Long> {

      @Override
      protected boolean getConfiguredEnabled() {
        return false;
      }

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("ECardStation");
      }

      @Override
      protected Class<? extends LookupCall<Long>> getConfiguredLookupCall() {
        return ECardStationLookupCall.class;
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }
    }

    @Order(40.0)
    public class RaceField extends AbstractSmartField<Long> {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Runner");
      }

      @Override
      protected Class<? extends LookupCall<Long>> getConfiguredLookupCall() {
        return RaceLookupCall.class;
      }

      @Override
      protected Class<? extends IValueField> getConfiguredMasterField() {
        return EventField.class;
      }

      @Override
      protected boolean getConfiguredMasterRequired() {
        return true;
      }

      @Override
      protected void execPrepareLookup(ILookupCall call) throws ProcessingException {
        ((RaceLookupCall) call).setEventNr(getEventField().getValue());
      }
    }

    @Order(50.0)
    public class EvtDownloadField extends AbstractDefaultDateTimeField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("DownloadedOn");
      }
    }

    @Order(60.0)
    public class ClearField extends AbstractMillisecondsDateTimeField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Clear");
      }
    }

    @Order(70.0)
    public class CheckField extends AbstractMillisecondsDateTimeField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Check");
      }
    }

    @Order(80.0)
    public class StartField extends AbstractMillisecondsDateTimeField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Start");
      }
    }

    @Order(90.0)
    public class FinishField extends AbstractMillisecondsDateTimeField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Finish");
      }

    }

    @Order(100.0)
    public class RawDataField extends AbstractStringField {

      @Override
      protected boolean getConfiguredEnabled() {
        return false;
      }

      @Override
      protected int getConfiguredGridH() {
        return 4;
      }

      @Override
      protected int getConfiguredGridW() {
        return 2;
      }

      @Override
      protected boolean getConfiguredMultilineText() {
        return true;
      }

      @Override
      protected boolean getConfiguredWrapText() {
        return true;
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

    @Override
    public void execLoad() throws ProcessingException {
      IDownloadedECardProcessService service = BEANS.get(IDownloadedECardProcessService.class);
      DownloadedECardFormData formData = new DownloadedECardFormData();
      exportFormData(formData);
      formData = service.load(formData);
      importFormData(formData);
      setEnabledPermission(new UpdateDownloadedECardPermission());
    }

    @Override
    public void execStore() throws ProcessingException {
      IDownloadedECardProcessService service = BEANS.get(IDownloadedECardProcessService.class);
      DownloadedECardFormData formData = new DownloadedECardFormData();
      exportFormData(formData);
      formData = service.store(formData);
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IDownloadedECardProcessService service = BEANS.get(IDownloadedECardProcessService.class);
      DownloadedECardFormData formData = new DownloadedECardFormData();
      exportFormData(formData);
      formData = service.prepareCreate(formData);
      importFormData(formData);
    }

    @Override
    public void execStore() throws ProcessingException {
      IDownloadedECardProcessService service = BEANS.get(IDownloadedECardProcessService.class);
      DownloadedECardFormData formData = new DownloadedECardFormData();
      exportFormData(formData);
      formData = service.create(formData);
      importFormData(formData);
    }
  }

  @FormData
  public Long getRawStart() {
    return m_rawStart;
  }

  @FormData
  public void setRawStart(Long rawStart) {
    m_rawStart = rawStart;
  }

  @FormData
  public Long getRawFinish() {
    return m_rawFinish;
  }

  @FormData
  public void setRawFinish(Long rawFinish) {
    m_rawFinish = rawFinish;
  }

  @FormData
  public Long getRawClear() {
    return m_rawClear;
  }

  @FormData
  public void setRawClear(Long rawClear) {
    m_rawClear = rawClear;
  }

  @FormData
  public Long getRawCheck() {
    return m_rawCheck;
  }

  @FormData
  public void setRawCheck(Long rawCheck) {
    m_rawCheck = rawCheck;
  }
}
