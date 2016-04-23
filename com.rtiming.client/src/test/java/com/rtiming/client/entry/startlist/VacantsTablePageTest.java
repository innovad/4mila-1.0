package com.rtiming.client.entry.startlist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.entry.EntryForm;
import com.rtiming.client.entry.startlist.StartlistSettingsTablePage.Table.CreateStartlistMenu;
import com.rtiming.client.entry.startlist.VacantsTablePage.Table.NewEntryMenu;
import com.rtiming.client.runner.RunnerForm.MainBox.FirstNameField;
import com.rtiming.client.runner.RunnerForm.MainBox.LastNameField;
import com.rtiming.client.test.AbstractTablePageTest;
import com.rtiming.client.test.FMilaClientTestUtility;
import com.rtiming.client.test.data.CurrencyTestDataProvider;
import com.rtiming.client.test.data.EventTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualClassTestDataProvider;
import com.rtiming.client.test.data.RunnerTestDataProvider;
import com.rtiming.client.test.field.FieldValue;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class VacantsTablePageTest extends AbstractTablePageTest<VacantsTablePage> {

  private EventTestDataProvider event;
  private RunnerTestDataProvider runner;
  private CurrencyTestDataProvider currency;

  @Override
  protected VacantsTablePage getTablePage() throws ProcessingException {
    event = new EventTestDataProvider();
    return new VacantsTablePage(event.getEventNr());
  }

  @Test
  public void testNewEntry() throws Exception {
    List<FieldValue> list = new ArrayList<FieldValue>();
    list.add(new FieldValue(LastNameField.class, "VACANT RUNNER"));
    list.add(new FieldValue(FirstNameField.class, "FIRST"));
    runner = new RunnerTestDataProvider(list);
    event = new EventWithIndividualClassTestDataProvider();
    currency = new CurrencyTestDataProvider();
    EventWithIndividualClassTestDataProvider eventWithClass = (EventWithIndividualClassTestDataProvider) event;
    final Date firstStart = DateUtility.truncDate(new Date());

    // create startlist setting
    StartlistSettingForm setting = new StartlistSettingForm();
    setting.setNewClassUid(eventWithClass.getClassUid());
    setting.setParticipationCount(0L);
    setting.setEventNr(event.getEventNr());
    setting.startNew();

    setting.getVacantAbsoluteField().setValue(1L);
    setting.getFirstStartField().setValue(firstStart);
    setting.getBibNoFromField().setValue(100L);

    setting.doOk();

    // create startlists
    StartlistSettingsTablePage page = new StartlistSettingsTablePage(event.getEventNr());
    page.nodeAddedNotify();
    page.loadChildren();
    page.getTable().selectFirstRow();
    boolean run = page.getTable().runMenu(CreateStartlistMenu.class);
    assertTrue("Startlist created", run);

    // vacant page
    VacantsTablePage vacants = new VacantsTablePage(event.getEventNr());
    vacants.nodeAddedNotify();
    vacants.loadChildren();
    assertEquals("1 Vacant", 1, vacants.getTable().getRowCount());
    vacants.getTable().selectFirstRow();

    Runnable runnableAfterMenu = new Runnable() {
      @Override
      public void run() {
        try {
          EntryForm form = FMilaClientTestUtility.findLastAddedForm(EntryForm.class);
          assertNotNull("Entry Form open", form);
          assertEquals("1 Runner Row", 1, form.getRacesField().getTable().getRowCount());
          assertEquals("Event set", event.getEventNr(), form.getEventsField().getTable().getEventNrColumn().getValue(0));
          assertEquals("Start Time set", firstStart, form.getEventsField().getTable().getStartTimeColumn().getValue(0));
          assertEquals("Start Time set", firstStart, form.getRacesField().getTable().getLegStartTimeColumn().getValue(0));
          assertEquals("Bib number set", "100", form.getRacesField().getTable().getBibNumberColumn().getValue(0));

          form.getRunnerField().setValue(runner.getRunnerNr());
          form.getCurrencyField().setValue(currency.getCurrencyUid());
          form.doOk();
        }
        catch (ProcessingException e) {
          e.printStackTrace();
          fail(e.getMessage());
        }
      }
    };
    FMilaClientTestUtility.runBlockingMenu(vacants.getTable(), NewEntryMenu.class, runnableAfterMenu);

    vacants.nodeAddedNotify();
    vacants.loadChildren();
    assertEquals("1 Vacant", 1, vacants.getTable().getRowCount());
    assertEquals("<html><body>VACANT RUNNER, FIRST</body></html>", vacants.getTable().getRunnerColumn().getValue(0));
    vacants.getTable().selectFirstRow();
    run = vacants.getTable().runMenu(NewEntryMenu.class);
    assertFalse("Menu disabled", run);
  }

  @After
  public void after() throws ProcessingException {
    event.remove();
    if (runner != null) {
      runner.remove();
    }
    if (currency != null) {
      currency.remove();
    }
  }

}
