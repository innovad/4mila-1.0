package com.rtiming.client.settings;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.longfield.AbstractLongField;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

import com.rtiming.client.event.AbstractEventField;
import com.rtiming.client.settings.TestDataForm.MainBox.CancelButton;
import com.rtiming.client.settings.TestDataForm.MainBox.ClazzField;
import com.rtiming.client.settings.TestDataForm.MainBox.CountField;
import com.rtiming.client.settings.TestDataForm.MainBox.EventField;
import com.rtiming.client.settings.TestDataForm.MainBox.OkButton;
import com.rtiming.shared.Texts;
import com.rtiming.shared.settings.ITestDataProcessService;
import com.rtiming.shared.settings.TestDataFormData;
import com.rtiming.shared.settings.clazz.ClazzLookupCall;

@FormData(value = TestDataFormData.class, sdkCommand = SdkCommand.CREATE)
public class TestDataForm extends AbstractForm {

  public TestDataForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("CreateTestData");
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  public void startNew() throws ProcessingException {
    startInternal(new NewHandler());
  }

  public ClazzField getClazzField() {
    return getFieldByClass(ClazzField.class);
  }

  public CountField getCountField() {
    return getFieldByClass(CountField.class);
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
      protected boolean getConfiguredMandatory() {
        return true;
      }
    }

    @Order(20.0)
    public class ClazzField extends AbstractSmartField<Long> {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Class");
      }

      @Override
      protected Class<? extends LookupCall<Long>> getConfiguredLookupCall() {
        return ClazzLookupCall.class;
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

      @Override
      protected void execPrepareLookup(ILookupCall<Long> call) throws ProcessingException {
        ((ClazzLookupCall) call).setEventNr(getEventField().getValue());
      }
    }

    @Order(30.0)
    public class CountField extends AbstractLongField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Count");
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }
    }

    @Order(40.0)
    public class OkButton extends AbstractOkButton {
    }

    @Order(50.0)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      ITestDataProcessService service = BEANS.get(ITestDataProcessService.class);
      TestDataFormData formData = new TestDataFormData();
      exportFormData(formData);
      formData = service.prepareCreate(formData);
      importFormData(formData);
    }

    @Override
    public void execStore() throws ProcessingException {
      ITestDataProcessService service = BEANS.get(ITestDataProcessService.class);
      TestDataFormData formData = new TestDataFormData();
      exportFormData(formData);
      formData = service.create(formData);
    }
  }
}
