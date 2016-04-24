package com.rtiming.client.settings.addinfo;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.runner.RunWith;

import com.rtiming.client.TestClientSession;
import com.rtiming.client.settings.addinfo.EventAdditionalInformationForm.MainBox.AdditionalInformationField;
import com.rtiming.client.settings.addinfo.EventAdditionalInformationForm.MainBox.EventField;
import com.rtiming.client.test.AbstractFormTest;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.client.test.data.EventTestDataProvider;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.client.test.field.MaxFormFieldValueProvider;
import com.rtiming.shared.settings.addinfo.AdditionalInformationAdministrationFormData;
import com.rtiming.shared.settings.addinfo.EventAdditionalInformationFormData;
import com.rtiming.shared.settings.addinfo.IAdditionalInformationAdministrationProcessService;
import com.rtiming.shared.settings.addinfo.IEventAdditionalInformationProcessService;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class EventAdditionalInformationFormTest extends AbstractFormTest<EventAdditionalInformationForm> {

  private AdditionalInformationAdministrationForm addInfo;
  private EventTestDataProvider event;

  @Override
  public void setUpForm() throws ProcessingException {
    event = new EventTestDataProvider();

    addInfo = new AdditionalInformationAdministrationForm();
    addInfo.startNew();
    FormTestUtility.fillFormFields(addInfo, new MaxFormFieldValueProvider());
    addInfo.touch();
    addInfo.doOk();

    super.setUpForm();
  }

  @Override
  protected EventAdditionalInformationForm getStartedForm() throws ProcessingException {
    EventAdditionalInformationForm form = new EventAdditionalInformationForm();
    form.getEventField().setValue(event.getEventNr());
    form.startNew();
    return form;
  }

  @Override
  protected EventAdditionalInformationForm getModifyForm() throws ProcessingException {
    EventAdditionalInformationForm form = new EventAdditionalInformationForm();
    form.getEventField().setValue(event.getEventNr());
    form.getAdditionalInformationField().setValue(addInfo.getAdditionalInformationUid());
    form.startModify();
    return form;
  }

  @Override
  public void cleanup() throws ProcessingException {
    EventAdditionalInformationFormData formData = new EventAdditionalInformationFormData();
    formData.getEvent().setValue(event.getEventNr());
    formData.getAdditionalInformation().setValue(addInfo.getAdditionalInformationUid());
    BEANS.get(IEventAdditionalInformationProcessService.class).delete(formData);

    AdditionalInformationAdministrationFormData defFormData = new AdditionalInformationAdministrationFormData();
    defFormData.setAdditionalInformationUid(addInfo.getAdditionalInformationUid());
    BEANS.get(IAdditionalInformationAdministrationProcessService.class).delete(defFormData);

    // Event
    event.remove();
  }

  @Override
  protected List<FieldValue> getFixedValues() throws ProcessingException {
    List<FieldValue> result = new ArrayList<FieldValue>();
    FieldValue eventField = new FieldValue(EventField.class, event.getEventNr());
    result.add(eventField);
    FieldValue mapField = new FieldValue(AdditionalInformationField.class, addInfo.getAdditionalInformationUid());
    result.add(mapField);
    return result;
  }

}
