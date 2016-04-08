package com.rtiming.client.event.course;

import org.eclipse.scout.commons.CompareUtility;
import org.eclipse.scout.commons.annotations.FormData;
import org.eclipse.scout.commons.annotations.FormData.SdkCommand;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.platform.BEANS;

import com.rtiming.client.common.help.AbstractHelpLinkButton;
import com.rtiming.client.event.AbstractEventField;
import com.rtiming.client.event.course.ReplacementControlForm.MainBox.CancelButton;
import com.rtiming.client.event.course.ReplacementControlForm.MainBox.ControlField;
import com.rtiming.client.event.course.ReplacementControlForm.MainBox.EventField;
import com.rtiming.client.event.course.ReplacementControlForm.MainBox.OkButton;
import com.rtiming.client.event.course.ReplacementControlForm.MainBox.ReplacementControlField;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.UpdateReplacementControlPermission;
import com.rtiming.shared.event.course.IReplacementControlProcessService;
import com.rtiming.shared.event.course.ReplacementControlFormData;

@FormData(value = ReplacementControlFormData.class, sdkCommand = SdkCommand.CREATE)
public class ReplacementControlForm extends AbstractForm {

  public ReplacementControlForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("ReplacementControl");
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

  public ControlField getControlField() {
    return getFieldByClass(ControlField.class);
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

  public ReplacementControlField getReplacementControlField() {
    return getFieldByClass(ReplacementControlField.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class EventField extends AbstractEventField {

      @Override
      protected boolean getConfiguredEnabled() {
        return false;
      }
    }

    @Order(20.0)
    public class ControlField extends AbstractControlField {

      @Override
      protected boolean getConfiguredEnabled() {
        return false;
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

      @Override
      protected Long getControlEventNr() {
        return getEventField().getValue();
      }
    }

    @Order(30.0)
    public class ReplacementControlField extends AbstractControlField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("ReplacementControl");
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

      @Override
      protected void execChangedValue() throws ProcessingException {
        if (CompareUtility.equals(getReplacementControlField().getValue(), getControlField().getValue())) {
          getReplacementControlField().setErrorStatus("Es muss ein anderer Posten gew�hlt werden."); // TODO
        }
        else {
          getReplacementControlField().clearErrorStatus();
        }
      }

      @Override
      protected Long getControlEventNr() {
        return getEventField().getValue();
      }
    }

    @Order(38.0)
    public class HelpLink extends AbstractHelpLinkButton {
    }

    @Order(40.0)
    public class OkButton extends AbstractOkButton {
    }

    @Order(50.0)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  public class ModifyHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IReplacementControlProcessService service = BEANS.get(IReplacementControlProcessService.class);
      ReplacementControlFormData formData = new ReplacementControlFormData();
      exportFormData(formData);
      formData = service.load(formData);
      importFormData(formData);
      setEnabledPermission(new UpdateReplacementControlPermission());
    }

    @Override
    public void execStore() throws ProcessingException {
      IReplacementControlProcessService service = BEANS.get(IReplacementControlProcessService.class);
      ReplacementControlFormData formData = new ReplacementControlFormData();
      exportFormData(formData);
      formData = service.store(formData);
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IReplacementControlProcessService service = BEANS.get(IReplacementControlProcessService.class);
      ReplacementControlFormData formData = new ReplacementControlFormData();
      exportFormData(formData);
      formData = service.prepareCreate(formData);
      importFormData(formData);
    }

    @Override
    public void execStore() throws ProcessingException {
      IReplacementControlProcessService service = BEANS.get(IReplacementControlProcessService.class);
      ReplacementControlFormData formData = new ReplacementControlFormData();
      exportFormData(formData);
      formData = service.create(formData);
    }
  }
}
