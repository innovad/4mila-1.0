package com.rtiming.client.map;

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
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

import com.rtiming.client.common.help.AbstractHelpLinkButton;
import com.rtiming.client.event.AbstractEventField;
import com.rtiming.client.map.EventMapForm.MainBox.CancelButton;
import com.rtiming.client.map.EventMapForm.MainBox.EventField;
import com.rtiming.client.map.EventMapForm.MainBox.MapField;
import com.rtiming.client.map.EventMapForm.MainBox.OkButton;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.UpdateEventMapPermission;
import com.rtiming.shared.event.EventMapFormData;
import com.rtiming.shared.map.IEventMapProcessService;
import com.rtiming.shared.map.MapLookupCall;

@FormData(value = EventMapFormData.class, sdkCommand = SdkCommand.CREATE)
public class EventMapForm extends AbstractForm {

  public EventMapForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("EventMap");
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

  public MapField getMapField() {
    return getFieldByClass(MapField.class);
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
    public class MapField extends AbstractSmartField<Long> {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Map");
      }

      @Override
      protected Class<? extends LookupCall<Long>> getConfiguredLookupCall() {
        return MapLookupCall.class;
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
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
      IEventMapProcessService service = BEANS.get(IEventMapProcessService.class);
      EventMapFormData formData = new EventMapFormData();
      exportFormData(formData);
      formData = service.load(formData);
      importFormData(formData);
      setEnabledPermission(new UpdateEventMapPermission());
    }

    @Override
    public void execStore() throws ProcessingException {
      IEventMapProcessService service = BEANS.get(IEventMapProcessService.class);
      EventMapFormData formData = new EventMapFormData();
      exportFormData(formData);
      formData = service.store(formData);
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IEventMapProcessService service = BEANS.get(IEventMapProcessService.class);
      EventMapFormData formData = new EventMapFormData();
      exportFormData(formData);
      formData = service.prepareCreate(formData);
      importFormData(formData);
    }

    @Override
    public void execStore() throws ProcessingException {
      IEventMapProcessService service = BEANS.get(IEventMapProcessService.class);
      EventMapFormData formData = new EventMapFormData();
      exportFormData(formData);
      formData = service.create(formData);
    }
  }
}
