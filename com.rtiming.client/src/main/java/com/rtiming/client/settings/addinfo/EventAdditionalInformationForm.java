package com.rtiming.client.settings.addinfo;

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
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

import com.rtiming.client.common.help.AbstractHelpLinkButton;
import com.rtiming.client.event.AbstractEventField;
import com.rtiming.client.settings.addinfo.EventAdditionalInformationForm.MainBox.AdditionalInformationField;
import com.rtiming.client.settings.addinfo.EventAdditionalInformationForm.MainBox.CancelButton;
import com.rtiming.client.settings.addinfo.EventAdditionalInformationForm.MainBox.EventField;
import com.rtiming.client.settings.addinfo.EventAdditionalInformationForm.MainBox.OkButton;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.EntityCodeType;
import com.rtiming.shared.common.security.permission.UpdateEventAdditionalInformationPermission;
import com.rtiming.shared.settings.addinfo.AdditionalInformationLookupCall;
import com.rtiming.shared.settings.addinfo.EventAdditionalInformationFormData;
import com.rtiming.shared.settings.addinfo.IEventAdditionalInformationProcessService;

@FormData(value = EventAdditionalInformationFormData.class, sdkCommand = SdkCommand.CREATE)
public class EventAdditionalInformationForm extends AbstractForm {

  public EventAdditionalInformationForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("EventAdditionalInformation");
  }

  public AdditionalInformationField getAdditionalInformationField() {
    return getFieldByClass(AdditionalInformationField.class);
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

  public EventField getEventField() {
    return getFieldByClass(EventField.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
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
    public class AdditionalInformationField extends AbstractSmartField<Long> {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("AdditionalInformation");
      }

      @Override
      protected Class<? extends LookupCall<Long>> getConfiguredLookupCall() {
        return AdditionalInformationLookupCall.class;
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

      @Override
      protected void execPrepareLookup(ILookupCall call) throws ProcessingException {
        ((AdditionalInformationLookupCall) call).setEntityUid(EntityCodeType.EntryCode.ID);
      }
    }

    @Order(28.0)
    public class HelpLink extends AbstractHelpLinkButton {
    }

    @Order(30.0)
    public class OkButton extends AbstractOkButton {
    }

    @Order(40.0)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  public class ModifyHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IEventAdditionalInformationProcessService service = BEANS.get(IEventAdditionalInformationProcessService.class);
      EventAdditionalInformationFormData formData = new EventAdditionalInformationFormData();
      exportFormData(formData);
      formData = service.load(formData);
      importFormData(formData);
      setEnabledPermission(new UpdateEventAdditionalInformationPermission());
    }

    @Override
    public void execStore() throws ProcessingException {
      IEventAdditionalInformationProcessService service = BEANS.get(IEventAdditionalInformationProcessService.class);
      EventAdditionalInformationFormData formData = new EventAdditionalInformationFormData();
      exportFormData(formData);
      formData = service.store(formData);
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IEventAdditionalInformationProcessService service = BEANS.get(IEventAdditionalInformationProcessService.class);
      EventAdditionalInformationFormData formData = new EventAdditionalInformationFormData();
      exportFormData(formData);
      formData = service.prepareCreate(formData);
      importFormData(formData);
    }

    @Override
    public void execStore() throws ProcessingException {
      IEventAdditionalInformationProcessService service = BEANS.get(IEventAdditionalInformationProcessService.class);
      EventAdditionalInformationFormData formData = new EventAdditionalInformationFormData();
      exportFormData(formData);
      formData = service.create(formData);
    }
  }
}
