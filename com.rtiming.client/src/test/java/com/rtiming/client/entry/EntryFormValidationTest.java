package com.rtiming.client.entry;

import org.eclipse.scout.rt.client.ui.form.fields.IFormField;
import org.eclipse.scout.rt.client.ui.form.fields.IValueField;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.test.FMilaClientTestUtility;
import com.rtiming.client.test.data.EventTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualClassTestDataProvider;
import com.rtiming.client.test.data.RunnerTestDataProvider;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class EntryFormValidationTest {

  private EventTestDataProvider event;
  private RunnerTestDataProvider runner;
  private EntryForm entry;

  @Test
  public void testPrepareStartTime() throws Exception {
    event = new EventTestDataProvider();

    entry = new EntryForm();
    entry.startNew();

    FMilaClientTestUtility.doColumnEdit(entry.getEventsField().getTable().getEventNrColumn(), 0, event.getEventNr());
    Assert.assertNull("Start Time null", entry.getEventsField().getTable().getStartTimeColumn().getValue(0));

    IFormField field = entry.getEventsField().getTable().getStartTimeColumn().prepareEdit(entry.getEventsField().getTable().getRow(0));
    Assert.assertEquals("Start Time = Evt Zero", event.getForm().getZeroTimeField().getValue(), ((IValueField<?>) field).getValue());

    entry.doClose();
  }

  @Test(expected = VetoException.class)
  public void testEventRequired() throws Exception {
    event = new EventWithIndividualClassTestDataProvider();
    runner = new RunnerTestDataProvider();
    Long classUid = ((EventWithIndividualClassTestDataProvider) event).getClassUid();

    entry = new EntryForm();
    entry.startNew();
    FMilaClientTestUtility.doColumnEdit(entry.getEventsField().getTable().getEventNrColumn(), 0, event.getEventNr());

    entry.getRunnerField().setValue(runner.getRunnerNr());
    entry.getClazzField().setValue(classUid);

    entry.getEventsField().getTable().deleteAllRows();

    entry.doOk();
  }

  @Test(expected = VetoException.class)
  public void testEventEmpty() throws Exception {
    event = new EventWithIndividualClassTestDataProvider();
    runner = new RunnerTestDataProvider();
    Long classUid = ((EventWithIndividualClassTestDataProvider) event).getClassUid();

    entry = new EntryForm();
    entry.startNew();
    FMilaClientTestUtility.doColumnEdit(entry.getEventsField().getTable().getEventNrColumn(), 0, event.getEventNr());

    entry.getRunnerField().setValue(runner.getRunnerNr());
    entry.getClazzField().setValue(classUid);

    entry.getEventsField().getTable().getEventNrColumn().setValue(0, null);

    entry.doOk();
  }

  @After
  public void after() throws ProcessingException {
    if (entry != null && entry.isFormOpen()) {
      entry.doClose();
    }
    if (event != null) {
      event.remove();
    }
    if (runner != null) {
      runner.remove();
    }
  }

}
