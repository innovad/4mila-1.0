package com.rtiming.client.event.course;

import org.eclipse.scout.commons.annotations.FormData;
import org.eclipse.scout.commons.annotations.FormData.SdkCommand;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.bigdecimalfield.AbstractBigDecimalField;
import org.eclipse.scout.rt.client.ui.form.fields.booleanfield.AbstractBooleanField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;

import com.rtiming.client.common.help.AbstractHelpLinkButton;
import com.rtiming.client.common.ui.fields.AbstractGoogleMapsButton;
import com.rtiming.client.common.ui.fields.AbstractLongLatField;
import com.rtiming.client.event.AbstractEventField;
import com.rtiming.client.event.course.ControlForm.MainBox.ActiveField;
import com.rtiming.client.event.course.ControlForm.MainBox.CancelButton;
import com.rtiming.client.event.course.ControlForm.MainBox.EventField;
import com.rtiming.client.event.course.ControlForm.MainBox.GoogleMapsButton;
import com.rtiming.client.event.course.ControlForm.MainBox.LongitudeLatitudeBox;
import com.rtiming.client.event.course.ControlForm.MainBox.LongitudeLatitudeBox.GlobalXField;
import com.rtiming.client.event.course.ControlForm.MainBox.LongitudeLatitudeBox.GlobalYField;
import com.rtiming.client.event.course.ControlForm.MainBox.NumberField;
import com.rtiming.client.event.course.ControlForm.MainBox.OkButton;
import com.rtiming.client.event.course.ControlForm.MainBox.PositionLocalCoordinatesBox;
import com.rtiming.client.event.course.ControlForm.MainBox.PositionLocalCoordinatesBox.PositionXField;
import com.rtiming.client.event.course.ControlForm.MainBox.PositionLocalCoordinatesBox.PositionYField;
import com.rtiming.client.event.course.ControlForm.MainBox.TypeField;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.UpdateControlPermission;
import com.rtiming.shared.event.course.ControlFormData;
import com.rtiming.shared.event.course.ControlTypeCodeType;
import com.rtiming.shared.event.course.IControlProcessService;

@FormData(value = ControlFormData.class, sdkCommand = SdkCommand.CREATE)
public class ControlForm extends AbstractForm {

  private Long controlNr;
  private Long m_clientNr;

  public ControlForm() throws ProcessingException {
    super();
  }

  @Override
  protected void execFormActivated() throws ProcessingException {
    getGoogleMapsButton().setEnabled(getGlobalXField().getValue() != null && getGlobalYField().getValue() != null);
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Control");
  }

  public ActiveField getActiveField() {
    return getFieldByClass(ActiveField.class);
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  @FormData
  public Long getControlNr() {
    return controlNr;
  }

  @FormData
  public void setControlNr(Long controlNr) {
    this.controlNr = controlNr;
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

  public GlobalXField getGlobalXField() {
    return getFieldByClass(GlobalXField.class);
  }

  public GlobalYField getGlobalYField() {
    return getFieldByClass(GlobalYField.class);
  }

  public GoogleMapsButton getGoogleMapsButton() {
    return getFieldByClass(GoogleMapsButton.class);
  }

  public LongitudeLatitudeBox getLongitudeLatitudeBox() {
    return getFieldByClass(LongitudeLatitudeBox.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public NumberField getNumberField() {
    return getFieldByClass(NumberField.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  public PositionLocalCoordinatesBox getPositionLocalCoordinatesBox() {
    return getFieldByClass(PositionLocalCoordinatesBox.class);
  }

  public PositionXField getPositionXField() {
    return getFieldByClass(PositionXField.class);
  }

  public PositionYField getPositionYField() {
    return getFieldByClass(PositionYField.class);
  }

  public TypeField getTypeField() {
    return getFieldByClass(TypeField.class);
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
    public class NumberField extends AbstractStringField {

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

      @Override
      protected boolean getConfiguredUpdateDisplayTextOnModify() {
        return true;
      }

      @Override
      protected void execChangedValue() throws ProcessingException {
        if (getHandler() instanceof NewHandler) {
          getTypeField().setValue(ControlFormUtility.parseControlType(getValue()));
        }
      }

    }

    @Order(30.0)
    public class TypeField extends AbstractSmartField<Long> {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Type");
      }

      @Override
      protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
        return ControlTypeCodeType.class;
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }
    }

    @Order(40.0)
    public class ActiveField extends AbstractBooleanField {

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Active");
      }

    }

    @Order(50.0)
    public class PositionLocalCoordinatesBox extends AbstractGroupBox {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("MapPosition");
      }

      @Order(10.0)
      public class PositionXField extends AbstractBigDecimalField {

        @Override
        protected String getConfiguredLabel() {
          return Texts.get("X");
        }
      }

      @Order(20.0)
      public class PositionYField extends AbstractBigDecimalField {

        @Override
        protected String getConfiguredLabel() {
          return Texts.get("Y");
        }
      }
    }

    @Order(60.0)
    public class LongitudeLatitudeBox extends AbstractGroupBox {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("LongitudeLatitude");
      }

      @Order(10.0)
      public class GlobalXField extends AbstractLongLatField {

        @Override
        protected String getConfiguredLabel() {
          return Texts.get("Longitude");
        }

        @Override
        protected void execChangedValue() throws ProcessingException {
          getGoogleMapsButton().setEnabled(getGlobalXField().getValue() != null && getGlobalYField().getValue() != null);
        }

      }

      @Order(20.0)
      public class GlobalYField extends AbstractLongLatField {

        @Override
        protected String getConfiguredLabel() {
          return Texts.get("Latitude");
        }

        @Override
        protected void execChangedValue() throws ProcessingException {
          getGoogleMapsButton().setEnabled(getGlobalXField().getValue() != null && getGlobalYField().getValue() != null);
        }

      }
    }

    @Order(68.0)
    public class HelpLink extends AbstractHelpLinkButton {
    }

    @Order(70.0)
    public class GoogleMapsButton extends AbstractGoogleMapsButton {

      @Override
      protected Double getX() {
        if (getGlobalXField().getValue() != null) {
          return getGlobalXField().getValue().doubleValue();
        }
        return null;
      }

      @Override
      protected Double getY() {
        if (getGlobalYField().getValue() != null) {
          return getGlobalYField().getValue().doubleValue();
        }
        return null;
      }

      @Override
      protected String getNumber() {
        return getNumberField().getValue();
      }
    }

    @Order(80.0)
    public class OkButton extends AbstractOkButton {
    }

    @Order(90.0)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  public class ModifyHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IControlProcessService service = BEANS.get(IControlProcessService.class);
      ControlFormData formData = new ControlFormData();
      exportFormData(formData);
      formData = service.load(formData);
      importFormData(formData);
      setEnabledPermission(new UpdateControlPermission());
    }

    @Override
    public void execStore() throws ProcessingException {
      IControlProcessService service = BEANS.get(IControlProcessService.class);
      ControlFormData formData = new ControlFormData();
      exportFormData(formData);
      formData = service.store(formData);
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IControlProcessService service = BEANS.get(IControlProcessService.class);
      ControlFormData formData = new ControlFormData();
      exportFormData(formData);
      formData = service.prepareCreate(formData);
      importFormData(formData);
    }

    @Override
    protected void execPostLoad() throws ProcessingException {
      touch();
    }

    @Override
    public void execStore() throws ProcessingException {
      IControlProcessService service = BEANS.get(IControlProcessService.class);
      ControlFormData formData = new ControlFormData();
      exportFormData(formData);
      formData = service.create(formData);
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
}
