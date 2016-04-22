package com.rtiming.client.entry;

import java.util.Date;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.test.AbstractFormTest;
import com.rtiming.shared.entry.IRegistrationProcessService;
import com.rtiming.shared.entry.RegistrationFormData;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestEnvironmentClientSession.class)
public class RegistrationFormTest extends AbstractFormTest<RegistrationForm> {

  @Override
  protected RegistrationForm getStartedForm() throws ProcessingException {
    RegistrationForm form = new RegistrationForm();
    form.startNew();
    return form;
  }

  @Override
  protected RegistrationForm getModifyForm() throws ProcessingException {
    RegistrationForm form = new RegistrationForm();
    form.setRegistrationNr(getForm().getRegistrationNr());
    form.startModify();
    return form;
  }

  @Override
  public void cleanup() throws ProcessingException {
    RegistrationFormData formData = new RegistrationFormData();
    formData.setRegistrationNr(getForm().getRegistrationNr());
    BEANS.get(IRegistrationProcessService.class).delete(formData);
  }

  @Test
  public void testDefault() throws Exception {
    RegistrationForm form = new RegistrationForm();
    form.startNew();
    Assert.assertNull("Startlist Option Null", form.getStartlistSettingOptionGroupBox().getValue());
    Assert.assertTrue("Default Today", DateUtility.isSameDay(form.getEvtRegistrationField().getValue(), new Date()));
    form.doClose();
  }

}
