package com.rtiming.client.runner;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.testing.client.ScoutClientAssert;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.test.AbstractFormTest;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.shared.common.database.sql.BeanUtility;
import com.rtiming.shared.common.database.sql.RunnerBean;
import com.rtiming.shared.runner.IRunnerProcessService;
import com.rtiming.shared.runner.RunnerFormData;

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(ClientSession.class)
public class RunnerFormTest extends AbstractFormTest<RunnerForm> {

  @Override
  protected RunnerForm getStartedForm() throws ProcessingException {
    RunnerForm form = new RunnerForm();
    form.startNew();
    return form;
  }

  @Override
  protected RunnerForm getModifyForm() throws ProcessingException {
    RunnerForm form = new RunnerForm();
    form.setRunnerNr(getForm().getRunnerNr());
    form.startModify();
    return form;
  }

  @Override
  public void cleanup() throws ProcessingException {
    RunnerBean runner = new RunnerBean();
    runner.setRunnerNr(getForm().getRunnerNr());
    BEANS.get(IRunnerProcessService.class).delete(runner);
  }

  @Test
  public void testFind() throws ProcessingException {
    FormTestUtility.fillFormFields(getForm());
    getForm().getExtKeyField().setValue("JUnitTestKey123456");
    getForm().doOk();

    Long runnerNr = BEANS.get(IRunnerProcessService.class).findRunner("JUnitTestKey123456");
    Assert.assertNotNull(runnerNr);
    Assert.assertEquals(getForm().getRunnerNr(), runnerNr);
    RunnerFormData formData = new RunnerFormData();
    formData.setRunnerNr(runnerNr);
    formData = BeanUtility.runnerBean2formData(BEANS.get(IRunnerProcessService.class).load(BeanUtility.runnerFormData2bean(formData)));

    Assert.assertEquals(getForm().getFirstNameField().getValue(), formData.getFirstName().getValue());
    Assert.assertEquals(getForm().getLastNameField().getValue(), formData.getLastName().getValue());
    Assert.assertEquals(ClientSession.get().getSessionClientNr(), formData.getClientNr());
    Assert.assertEquals(getForm().getSexField().getValue(), formData.getSex().getValue());
    Assert.assertEquals(getForm().getAddressBox().getStreetField().getValue(), formData.getAbstractAddressBoxData().getStreet().getValue());
    Assert.assertEquals(getForm().getAddressNr(), formData.getAddressNr());
  }

  @Test
  public void testEMailField() throws Exception {
    RunnerForm form = new RunnerForm();
    form.startNew();
    form.getAddressBox().getEMailField().setValue("ABC");
    ScoutClientAssert.assertInvalid(form.getAddressBox().getEMailField());
    form.getAddressBox().getEMailField().setValue("adi.moser@gmail.com");
    ScoutClientAssert.assertValid(form.getAddressBox().getEMailField());
  }

}
