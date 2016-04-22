package com.rtiming.client.test.data;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.client.entry.RegistrationForm;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.shared.entry.IRegistrationProcessService;
import com.rtiming.shared.entry.RegistrationFormData;

public class RegistrationTestDataProvider extends AbstractTestDataProvider<RegistrationForm> {

  public RegistrationTestDataProvider() throws ProcessingException {
    callInitializer();
  }

  @Override
  protected RegistrationForm createForm() throws ProcessingException {
    RegistrationForm registration = new RegistrationForm();
    registration.startNew();
    FormTestUtility.fillFormFields(registration);
    registration.doOk();
    return registration;
  }

  @Override
  public void remove() throws ProcessingException {
    RegistrationFormData formData = new RegistrationFormData();
    formData.setRegistrationNr(getRegistrationNr());
    BEANS.get(IRegistrationProcessService.class).delete(formData);
  }

  public Long getRegistrationNr() throws ProcessingException {
    return getForm().getRegistrationNr();
  }

}
