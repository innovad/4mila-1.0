package com.rtiming.client.entry;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.entry.EntryForm.MainBox.TabBox.RacesBox.RacesField.Table.AddRunnerMenu;
import com.rtiming.client.test.FMilaClientTestUtility;
import com.rtiming.client.test.data.CurrencyTestDataProvider;
import com.rtiming.client.test.data.EventWithTeamCombinedCourseClassTestDataProvider;
import com.rtiming.client.test.data.RunnerTestDataProvider;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestEnvironmentClientSession.class)
public class EntryFormTeamSizeTest {

  private static CurrencyTestDataProvider currency;
  private EventWithTeamCombinedCourseClassTestDataProvider event;
  private RunnerTestDataProvider runner;
  private EntryForm form;

  @BeforeClass
  public static void beforeClass() throws ProcessingException {
    currency = new CurrencyTestDataProvider();
  }

  @Test(expected = VetoException.class)
  public void testTeamSizeLess() throws Exception {
    event = new EventWithTeamCombinedCourseClassTestDataProvider(2L, 3L);
    runner = new RunnerTestDataProvider();

    form = new EntryForm();
    form.startNew();
    FMilaClientTestUtility.doColumnEdit(form.getEventsField().getTable().getEventNrColumn(), 0, event.getEventNr());
    form.getCurrencyField().setValue(currency.getCurrencyUid());

    form.getRunnerField().setValue(runner.getRunnerNr());
    form.getClazzField().setValue(event.getClassUid());

    Assert.assertEquals("1 Row", 1, form.getRacesField().getTable().getRowCount());

    form.doOk();
  }

  @Test
  public void testTeamSizeOk() throws Exception {
    event = new EventWithTeamCombinedCourseClassTestDataProvider(2L, 3L);
    runner = new RunnerTestDataProvider();

    form = new EntryForm();
    form.startNew();
    FMilaClientTestUtility.doColumnEdit(form.getEventsField().getTable().getEventNrColumn(), 0, event.getEventNr());
    form.getCurrencyField().setValue(currency.getCurrencyUid());

    form.getRunnerField().setValue(runner.getRunnerNr());
    form.getClazzField().setValue(event.getClassUid());
    form.getRacesField().getTable().runMenu(AddRunnerMenu.class);
    form.getRunnerField().setValue(runner.getRunnerNr());
    form.getClazzField().setValue(event.getClassUid());
    form.getRacesField().getTable().runMenu(AddRunnerMenu.class);
    form.getRunnerField().setValue(runner.getRunnerNr());
    form.getClazzField().setValue(event.getClassUid());

    Assert.assertEquals("3 Row", 3, form.getRacesField().getTable().getRowCount());

    form.doOk();
  }

  @Test(expected = VetoException.class)
  public void testTeamSizeMore() throws Exception {
    event = new EventWithTeamCombinedCourseClassTestDataProvider(1L, 2L);
    runner = new RunnerTestDataProvider();

    form = new EntryForm();
    form.startNew();
    FMilaClientTestUtility.doColumnEdit(form.getEventsField().getTable().getEventNrColumn(), 0, event.getEventNr());
    form.getCurrencyField().setValue(currency.getCurrencyUid());

    form.getRunnerField().setValue(runner.getRunnerNr());
    form.getClazzField().setValue(event.getClassUid());
    form.getRacesField().getTable().runMenu(AddRunnerMenu.class);
    form.getRunnerField().setValue(runner.getRunnerNr());
    form.getClazzField().setValue(event.getClassUid());
    form.getRacesField().getTable().runMenu(AddRunnerMenu.class);
    form.getRunnerField().setValue(runner.getRunnerNr());
    form.getClazzField().setValue(event.getClassUid());

    Assert.assertEquals("3 Row", 3, form.getRacesField().getTable().getRowCount());

    form.doOk();
  }

  @After
  public void after() throws ProcessingException {
    if (form != null && form.isFormOpen()) {
      form.doClose();
    }
    event.remove();
    runner.remove();
  }

  @AfterClass
  public static void afterClass() throws ProcessingException {
    currency.remove();
  }

}
