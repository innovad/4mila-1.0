package com.rtiming.client.event.course;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.test.AbstractFormTest;
import com.rtiming.client.test.data.EventTestDataProvider;
import com.rtiming.shared.event.course.ControlTypeCodeType;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestEnvironmentClientSession.class)
public class ControlFormTest extends AbstractFormTest<ControlForm> {

  private EventTestDataProvider event;

  @Override
  public void setUpForm() throws ProcessingException {
    event = new EventTestDataProvider();
    super.setUpForm();
  }

  @Override
  protected ControlForm getStartedForm() throws ProcessingException {
    ControlForm form = new ControlForm();
    form.getEventField().setValue(event.getEventNr());
    form.startNew();
    return form;
  }

  @Override
  protected ControlForm getModifyForm() throws ProcessingException {
    ControlForm form = new ControlForm();
    form.getEventField().setValue(event.getEventNr());
    form.setControlNr(getForm().getControlNr());
    form.startModify();
    return form;
  }

  @Override
  public void cleanup() throws ProcessingException {
    event.remove();
  }

  @Test(expected = VetoException.class)
  public void testUniqueControlNumber() throws ProcessingException {
    event = new EventTestDataProvider();

    ControlForm start = new ControlForm();
    start.getEventField().setValue(event.getEventNr());
    start.startNew();
    start.getTypeField().setValue(ControlTypeCodeType.StartCode.ID);
    start.getNumberField().setValue("12345");
    start.doOk();
    Assert.assertNotNull(start.getControlNr());

    ControlForm control = new ControlForm();
    control.getEventField().setValue(event.getEventNr());
    control.startNew();
    control.getTypeField().setValue(ControlTypeCodeType.ControlCode.ID);
    control.getNumberField().setValue("12345");
    control.doOk();
    Assert.assertNotNull(control.getControlNr());

    event.remove();
  }

}
