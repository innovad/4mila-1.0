package com.rtiming.client.event.course;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.testing.client.ScoutClientAssert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.test.AbstractFormTest;
import com.rtiming.client.test.data.ControlTestDataProvider;
import com.rtiming.client.test.data.EventTestDataProvider;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.event.course.ControlTypeCodeType;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class ReplacementControlFormTest extends AbstractFormTest<ReplacementControlForm> {

  private EventTestDataProvider event;
  private ControlTestDataProvider control;
  private ControlTestDataProvider replacement;

  @Override
  public void setUpForm() throws ProcessingException {
    event = new EventTestDataProvider();
    control = new ControlTestDataProvider(event.getEventNr(), ControlTypeCodeType.ControlCode.ID, "31");
    replacement = new ControlTestDataProvider(event.getEventNr(), ControlTypeCodeType.ControlCode.ID, "32");
    super.setUpForm();
  }

  @Override
  protected ReplacementControlForm getStartedForm() throws ProcessingException {
    ReplacementControlForm form = new ReplacementControlForm();
    form.getEventField().setValue(event.getEventNr());
    form.getControlField().setValue(control.getControlNr());
    form.startNew();
    return form;
  }

  @Override
  protected ReplacementControlForm getModifyForm() throws ProcessingException {
    ReplacementControlForm form = new ReplacementControlForm();
    form.getEventField().setValue(event.getEventNr());
    form.getControlField().setValue(control.getControlNr());
    form.getReplacementControlField().setValue(replacement.getControlNr()); // fake modify
    form.startModify();
    return form;
  }

  @Override
  protected List<FieldValue> getFixedValues() throws ProcessingException {
    List<FieldValue> list = new ArrayList<FieldValue>();
    list.add(new FieldValue(ReplacementControlForm.MainBox.ReplacementControlField.class, replacement.getControlNr()));
    return list;
  }

  @Override
  public void cleanup() throws ProcessingException {
    event.remove();
  }

  @Test
  public void testSameControlNotAllowed() throws Exception {
    ReplacementControlForm form = new ReplacementControlForm();
    form.getEventField().setValue(event.getEventNr());
    form.getControlField().setValue(control.getControlNr());
    form.startNew();

    form.getReplacementControlField().setValue(replacement.getControlNr());
    ScoutClientAssert.assertValid(form.getReplacementControlField());

    form.getReplacementControlField().setValue(control.getControlNr());
    ScoutClientAssert.assertInvalid(form.getReplacementControlField());
  }

  @Test(expected = VetoException.class)
  public void testDuplicateEntry() throws Exception {
    ReplacementControlForm form = new ReplacementControlForm();
    form.getEventField().setValue(event.getEventNr());
    form.getControlField().setValue(control.getControlNr());
    form.startNew();
    form.getReplacementControlField().setValue(replacement.getControlNr());
    form.doOk();

    ReplacementControlForm form2 = new ReplacementControlForm();
    form2.getEventField().setValue(event.getEventNr());
    form2.getControlField().setValue(control.getControlNr());
    form2.startNew();
    form2.getReplacementControlField().setValue(replacement.getControlNr());
    form2.doOk();
  }

}
