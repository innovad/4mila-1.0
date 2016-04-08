package com.rtiming.client.ecard.download;

import org.eclipse.scout.commons.annotations.FormData;
import org.eclipse.scout.commons.annotations.FormData.SdkCommand;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.client.common.help.AbstractHelpLinkButton;
import com.rtiming.client.common.ui.fields.AbstractMillisecondsDateTimeField;
import com.rtiming.client.common.ui.fields.AbstractSortCodeField;
import com.rtiming.client.ecard.download.PunchForm.MainBox.CancelButton;
import com.rtiming.client.ecard.download.PunchForm.MainBox.ControlNoField;
import com.rtiming.client.ecard.download.PunchForm.MainBox.EventField;
import com.rtiming.client.ecard.download.PunchForm.MainBox.OkButton;
import com.rtiming.client.ecard.download.PunchForm.MainBox.PunchSessionField;
import com.rtiming.client.ecard.download.PunchForm.MainBox.SortCodeField;
import com.rtiming.client.ecard.download.PunchForm.MainBox.TimeField;
import com.rtiming.client.event.AbstractEventField;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.UpdatePunchPermission;
import com.rtiming.shared.ecard.download.IPunchProcessService;
import com.rtiming.shared.ecard.download.PunchFormData;

@FormData(value = PunchFormData.class, sdkCommand = SdkCommand.CREATE)
public class PunchForm extends AbstractForm {

  private Long m_rawTime;

  public PunchForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Control");
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  public void startModify() throws ProcessingException {
    startInternal(new ModifyHandler());
  }

  public void startNew() throws ProcessingException {
    startInternal(new NewHandler());
  }

  public ControlNoField getControlNoField() {
    return getFieldByClass(ControlNoField.class);
  }

  public PunchSessionField getPunchSessionField() {
    return getFieldByClass(PunchSessionField.class);
  }

  public EventField getEventField() {
    return getFieldByClass(EventField.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  public SortCodeField getSortCodeField() {
    return getFieldByClass(SortCodeField.class);
  }

  public TimeField getTimeField() {
    return getFieldByClass(TimeField.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class EventField extends AbstractEventField {

      @Override
      protected boolean getConfiguredEnabled() {
        return false;
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }
    }

    @Order(20.0)
    public class PunchSessionField extends AbstractSmartField<Long> {

      @Override
      protected boolean getConfiguredEnabled() {
        return false;
      }

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("DownloadedECard");
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
    public class SortCodeField extends AbstractSortCodeField {

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

    }

    @Order(40.0)
    public class ControlNoField extends AbstractStringField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Number");
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

      @Override
      protected int getConfiguredMaxLength() {
        return 60;
      }
    }

    @Order(50.0)
    public class TimeField extends AbstractMillisecondsDateTimeField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Time");
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
      IPunchProcessService service = BEANS.get(IPunchProcessService.class);
      PunchFormData formData = new PunchFormData();
      exportFormData(formData);
      formData = service.load(formData);
      importFormData(formData);
      setEnabledPermission(new UpdatePunchPermission());
    }

    @Override
    protected void execPostLoad() throws ProcessingException {
      getSortCodeField().setEnabled(false);
    }

    @Override
    public void execStore() throws ProcessingException {
      IPunchProcessService service = BEANS.get(IPunchProcessService.class);
      PunchFormData formData = new PunchFormData();
      exportFormData(formData);
      formData = service.store(formData);
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IPunchProcessService service = BEANS.get(IPunchProcessService.class);
      PunchFormData formData = new PunchFormData();
      exportFormData(formData);
      formData = service.prepareCreate(formData);
      importFormData(formData);
    }

    @Override
    public void execStore() throws ProcessingException {
      IPunchProcessService service = BEANS.get(IPunchProcessService.class);
      PunchFormData formData = new PunchFormData();
      exportFormData(formData);
      formData = service.create(formData);
    }
  }

  @FormData
  public Long getRawTime() {
    return m_rawTime;
  }

  @FormData
  public void setRawTime(Long rawTime) {
    m_rawTime = rawTime;
  }
}
