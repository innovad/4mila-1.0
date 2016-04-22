package com.rtiming.client.entry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.entry.EntryForm.MainBox.TabBox.RacesBox.RacesField.Table.AddRunnerMenu;
import com.rtiming.client.entry.startlist.StartlistSettingForm;
import com.rtiming.client.runner.RunnerForm.MainBox.BirthdateField;
import com.rtiming.client.runner.RunnerForm.MainBox.ECardField;
import com.rtiming.client.runner.RunnerForm.MainBox.FirstNameField;
import com.rtiming.client.runner.RunnerForm.MainBox.LastNameField;
import com.rtiming.client.runner.RunnerForm.MainBox.YearField;
import com.rtiming.client.settings.currency.CurrencyForm;
import com.rtiming.client.test.FMilaClientTestUtility;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.client.test.data.CurrencyTestDataProvider;
import com.rtiming.client.test.data.ECardTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualClassTestDataProvider;
import com.rtiming.client.test.data.RunnerTestDataProvider;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.common.database.sql.RunnerBean;
import com.rtiming.shared.entry.startlist.StartlistTypeCodeType;
import com.rtiming.shared.settings.IDefaultProcessService;
import com.rtiming.shared.settings.currency.CurrencyFormData;
import com.rtiming.shared.settings.currency.ICurrencyProcessService;

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(TestEnvironmentClientSession.class)
public class EntryFormDBTest {

  private EventWithIndividualClassTestDataProvider event;
  private CurrencyTestDataProvider currency;

  @Before
  public void before() throws ProcessingException {
    currency = new CurrencyTestDataProvider();
    event = new EventWithIndividualClassTestDataProvider();
  }

  @Test
  public void testClassMandatory() throws ProcessingException {
    Long defaultEventNr = BEANS.get(IDefaultProcessService.class).getDefaultEventNr();
    BEANS.get(IDefaultProcessService.class).setDefaultEventNr(event.getEventNr());
    RunnerTestDataProvider runner = new RunnerTestDataProvider();

    EntryForm form = new EntryForm();
    form.startNew();

    form.getRunnerField().setValue(runner.getRunnerNr());
    form.getClazzField().setValue(event.getClassUid());
    form.getCurrencyField().setValue(currency.getCurrencyUid());

    Assert.assertEquals(event.getClassUid(), form.getRacesField().getTable().getLegColumn().getValue(0));
    // validate OK
    form.validateForm();

    form.getRacesField().getTable().getLegColumn().setValue(0, null);

    try {
      // validate NOK
      form.validateForm();
      Assert.fail();
    }
    catch (ProcessingException e) {
      Assert.assertEquals(VetoException.class, e.getClass());
    }
    finally {
      BEANS.get(IDefaultProcessService.class).setDefaultEventNr(defaultEventNr);
      runner.remove();
    }

  }

  @Test
  public void testDefaultEventPrepare() throws ProcessingException {
    Long defaultEventNr = BEANS.get(IDefaultProcessService.class).getDefaultEventNr();
    BEANS.get(IDefaultProcessService.class).setDefaultEventNr(event.getEventNr());
    EntryForm form = new EntryForm();
    form.startNew();
    Assert.assertEquals(1, form.getEventsField().getTable().getRowCount());
    Assert.assertEquals(event.getEventNr(), form.getEventsField().getTable().getEventNrColumn().getValue(0));
    Assert.assertNull(form.getEventsField().getTable().getEventClassColumn().getValue(0));
    Assert.assertNotNull(form.getEvtEntryField().getValue());
    BEANS.get(IDefaultProcessService.class).setDefaultEventNr(defaultEventNr);
    form.doClose();
  }

  /**
   * When making an entry from a SI Station, the SI Card Number should not change when selecting a runner
   * 
   * @throws ProcessingException
   */
  @Test
  public void testDownloadStationEntry() throws ProcessingException {
    Long defaultEventNr = BEANS.get(IDefaultProcessService.class).getDefaultEventNr();
    BEANS.get(IDefaultProcessService.class).setDefaultEventNr(event.getEventNr());

    List<FieldValue> list = new ArrayList<FieldValue>();
    list.add(new FieldValue(ECardField.class, 0L));
    RunnerTestDataProvider runner = new RunnerTestDataProvider(list);
    ECardTestDataProvider ecard = new ECardTestDataProvider();

    EntryForm entry = new EntryForm();
    entry.startNew();
    entry.setDownloadStationEntry(true);
    entry.getECardField().setValue(ecard.getECardNr());
    entry.getRunnerField().setValue(runner.getRunnerNr());

    Assert.assertNull("Runner without E-Card", runner.getForm().getECardField().getValue());
    Assert.assertNotNull("E-Card must exists", ecard.getECardNr());
    Assert.assertEquals("E-Card must be set on field", ecard.getECardNr(), entry.getECardField().getValue());
    Assert.assertEquals("E-Card must be set in Team Table", ecard.getECardNr(), entry.getRacesField().getTable().getECardColumn().getValue(0));

    BEANS.get(IDefaultProcessService.class).setDefaultEventNr(defaultEventNr);
    ecard.remove();
    runner.remove();
  }

  @Test
  public void testDownloadStationEntryWithDefaultEvent() throws ProcessingException {
    Long defaultEventNr = BEANS.get(IDefaultProcessService.class).getDefaultEventNr();
    BEANS.get(IDefaultProcessService.class).setDefaultEventNr(event.getEventNr());

    List<FieldValue> list = new ArrayList<FieldValue>();
    list.add(new FieldValue(ECardField.class, 0L));
    RunnerTestDataProvider runner = new RunnerTestDataProvider(list);
    ECardTestDataProvider ecard = new ECardTestDataProvider();

    EntryForm entry = new EntryForm();
    entry.startNew();
    entry.setDownloadStationEntry(true);
    entry.getECardField().setValue(ecard.getECardNr());
    entry.getRunnerField().setValue(runner.getRunnerNr());

    Assert.assertNull("Runner without E-Card", runner.getForm().getECardField().getValue());
    Assert.assertNotNull("E-Card must exists", ecard.getECardNr());
    Assert.assertEquals("E-Card must be set on field", ecard.getECardNr(), entry.getECardField().getValue());
    Assert.assertEquals("E-Card must be set in Team Table", ecard.getECardNr(), entry.getRacesField().getTable().getECardColumn().getValue(0));

    ecard.remove();
    runner.remove();

    BEANS.get(IDefaultProcessService.class).setDefaultEventNr(defaultEventNr);
  }

  @Test
  public void testNoDownloadStationEntry() throws ProcessingException {
    RunnerTestDataProvider runner = new RunnerTestDataProvider();
    ECardTestDataProvider ecard = new ECardTestDataProvider();

    EntryForm entry = new EntryForm();
    entry.startNew();
    FMilaClientTestUtility.doColumnEdit(entry.getEventsField().getTable().getEventNrColumn(), 0, event.getEventNr());

    entry.getECardField().setValueChangeTriggerEnabled(false);
    entry.getECardField().setValue(ecard.getECardNr());
    entry.getRunnerField().setValue(runner.getRunnerNr());

    Assert.assertTrue(ecard.getECardNr() != NumberUtility.nvl(entry.getECardField().getValue(), 0));

    ecard.remove();
    runner.remove();
  }

  @Test
  public void testClickTeamRow() throws ProcessingException {
    RunnerTestDataProvider runner = new RunnerTestDataProvider();

    EntryForm entry = new EntryForm();
    entry.startNew();
    FMilaClientTestUtility.doColumnEdit(entry.getEventsField().getTable().getEventNrColumn(), 0, event.getEventNr());

    entry.getRunnerField().setValue(runner.getRunnerNr());

    entry.getFirstNameField().setValueChangeTriggerEnabled(false);
    entry.getFirstNameField().setValue(null);

    Assert.assertEquals(1, entry.getRacesField().getTable().getRowCount());
    // TODO MIG entry.getRacesField().getTable().execRowsSelected(new ITableRow[]{entry.getRacesField().getTable().getRow(0)});
    Assert.assertEquals(runner.getForm().getFirstNameField().getValue(), entry.getFirstNameField().getValue());

    runner.remove();
  }

  @Test
  public void testDefaultCurrency() throws ProcessingException {
    Long defaultCurrencyUid = BEANS.get(IDefaultProcessService.class).getDefaultCurrencyUid();

    CurrencyForm currency2 = new CurrencyForm();
    currency2.startNew();
    FormTestUtility.fillFormFields(currency2);
    currency2.doOk();
    Assert.assertNotNull(currency2.getCurrencyUid());

    BEANS.get(IDefaultProcessService.class).setDefaultCurrencyUid(currency2.getCurrencyUid());

    EntryForm form = new EntryForm();
    form.startNew();
    Assert.assertEquals(form.getCurrencyField().getValue(), currency2.getCurrencyUid());
    Assert.assertEquals(DateUtility.truncDate(form.getEvtEntryField().getValue()), DateUtility.truncDate(new Date()));

    form.doClose();
    BEANS.get(IDefaultProcessService.class).setDefaultCurrencyUid(defaultCurrencyUid);

    CurrencyFormData formData = new CurrencyFormData();
    formData.setCurrencyUid(currency.getCurrencyUid());
    BEANS.get(ICurrencyProcessService.class).delete(formData);
  }

  @Test
  public void testRunnerSmartfield() throws ProcessingException {
    RunnerTestDataProvider runner = new RunnerTestDataProvider();

    EntryForm form = new EntryForm();
    form.startNew();
    Assert.assertNull(form.getRunnerField().getValue());
    Assert.assertNull(form.getFirstNameField().getValue());
    Assert.assertNull(form.getLastNameField().getValue());
    Assert.assertEquals(0, form.getRacesField().getTable().getRowCount());
    Assert.assertEquals(0, form.getFeesField().getTable().getRowCount());

    // select runner
    form.getRunnerField().setValue(runner.getRunnerNr());
    Assert.assertEquals(runner.getForm().getFirstNameField().getValue(), form.getFirstNameField().getValue());
    Assert.assertEquals(runner.getForm().getLastNameField().getValue(), form.getLastNameField().getValue());
    Assert.assertEquals(runner.getForm().getECardField().getValue(), form.getECardField().getValue());
    Assert.assertEquals(runner.getForm().getClubField().getValue(), form.getClubField().getValue());
    Assert.assertEquals(runner.getForm().getSexField().getValue(), form.getSexField().getValue());
    Assert.assertEquals(runner.getForm().getYearField().getValue(), form.getYearField().getValue());
    Assert.assertEquals(runner.getForm().getAddressBox().getCityField().getValue(), form.getCityField().getValue());

    // assert team table
    Assert.assertEquals(1, form.getRacesField().getTable().getRowCount());
    Assert.assertEquals(0, form.getRacesField().getTable().getSelectedRow().getRowIndex());
    Assert.assertEquals(runner.getForm().getLastNameField().getValue(), form.getRacesField().getTable().getLastNameColumn().getValue(0));
    Assert.assertNotNull(form.getRacesField().getTable().getRaceBeanColumn().getValue(0).getRunner());
    RunnerBean runnerFormData = form.getRacesField().getTable().getRaceBeanColumn().getValue(0).getRunner();
    Assert.assertEquals(runner.getRunnerNr(), runnerFormData.getRunnerNr());
    Assert.assertEquals(runner.getForm().getFirstNameField().getValue(), runnerFormData.getFirstName());
    Assert.assertEquals(runner.getForm().getLastNameField().getValue(), runnerFormData.getLastName());
    Assert.assertEquals(runner.getForm().getECardField().getValue(), runnerFormData.getECardNr());
    Assert.assertEquals(runner.getForm().getClubField().getValue(), runnerFormData.getClubNr());
    Assert.assertEquals(runner.getForm().getSexField().getValue(), runnerFormData.getSexUid());
    Assert.assertEquals(runner.getForm().getYearField().getValue(), runnerFormData.getYear());
    Assert.assertEquals(runner.getForm().getAddressBox().getCityField().getValue(), runnerFormData.getAddress().getCityNr());

    form.doClose();
    runner.remove();
  }

  @Test
  public void testClassField() throws ProcessingException {
    RunnerTestDataProvider runner = new RunnerTestDataProvider();

    EntryForm form = new EntryForm();
    form.startNew();
    FMilaClientTestUtility.doColumnEdit(form.getEventsField().getTable().getEventNrColumn(), 0, event.getEventNr());

    // assert class can be selected from smartfield
    List<? extends ILookupRow<?>> rows = form.getClazzField().callBrowseLookup(null, 100);
    Assert.assertEquals(1, rows.size());
    Assert.assertEquals(event.getClassUid().longValue(), (rows.get(0)).getKey());

    form.getRunnerField().setValue(runner.getRunnerNr());
    form.getClazzField().setValue(event.getClassUid());

    // assert event table field
    Assert.assertEquals(1, form.getEventsField().getTable().getRowCount());
    Assert.assertEquals(event.getEventNr(), form.getEventsField().getTable().getEventNrColumn().getValue(0));
    Assert.assertEquals(event.getClassUid(), form.getEventsField().getTable().getEventClassColumn().getValue(0));

    // assert team table field
    Assert.assertEquals(1, form.getRacesField().getTable().getRowCount());
    Assert.assertEquals(event.getClassUid(), form.getRacesField().getTable().getLegColumn().getValue(0));
    Assert.assertEquals(event.getEventNr(), form.getRacesField().getTable().getRaceEventColumn().getValue(0));

    form.doClose();
    runner.remove();
  }

  @Test
  public void testTeamEntry() throws ProcessingException {
    RunnerTestDataProvider runner1 = new RunnerTestDataProvider();
    List<FieldValue> values = new ArrayList<FieldValue>();
    values.add(new FieldValue(LastNameField.class, "LastName"));
    values.add(new FieldValue(FirstNameField.class, "FirstName"));
    values.add(new FieldValue(YearField.class, 1999L));
    values.add(new FieldValue(BirthdateField.class, DateUtility.truncDate(DateUtility.parse("01.01.1999", "dd.mm.yyyy"))));
    RunnerTestDataProvider runner2 = new RunnerTestDataProvider(values);

    EntryForm form = new EntryForm();
    form.startNew();
    FMilaClientTestUtility.doColumnEdit(form.getEventsField().getTable().getEventNrColumn(), 0, event.getEventNr());

    // add first runner
    form.getRunnerField().setValue(runner1.getRunnerNr());
    form.getClazzField().setValue(event.getClassUid());
    Assert.assertEquals(1, form.getRacesField().getTable().getRowCount());
    Assert.assertEquals(true, form.getRacesField().getTable().getRow(0).isSelected());

    // add another runner
    form.getRacesField().getTable().getMenu(AddRunnerMenu.class).doAction();

    ITableRow row1 = form.getRacesField().getTable().getRunnerNrColumn().findRow(runner1.getRunnerNr());
    ITableRow row2 = form.getRacesField().getTable().getSelectedRow();

    Assert.assertEquals(false, form.getRacesField().getTable().getRow(row1.getRowIndex()).isSelected());
    Assert.assertEquals(2, form.getRacesField().getTable().getRowCount());
    Assert.assertTrue("The 2nd new row is selected", row1.getRowIndex() != row2.getRowIndex());
    Assert.assertNull(form.getFirstNameField().getValue());
    Assert.assertNull(form.getLastNameField().getValue());
    Assert.assertNull(form.getECardField().getValue());
    Assert.assertNull(form.getClubField().getValue());
    Assert.assertNull(form.getSexField().getValue());
    Assert.assertNull(form.getYearField().getValue());
    Assert.assertNull(form.getCityField().getValue());

    // select another runner
    form.getRunnerField().setValue(runner2.getRunnerNr());
    Assert.assertEquals(runner2.getForm().getFirstNameField().getValue(), form.getFirstNameField().getValue());
    Assert.assertEquals(runner2.getForm().getLastNameField().getValue(), form.getLastNameField().getValue());
    Assert.assertEquals(runner2.getForm().getECardField().getValue(), form.getECardField().getValue());
    Assert.assertEquals(runner2.getForm().getClubField().getValue(), form.getClubField().getValue());
    Assert.assertEquals(runner2.getForm().getSexField().getValue(), form.getSexField().getValue());
    Assert.assertEquals(runner2.getForm().getYearField().getValue(), form.getYearField().getValue());
    Assert.assertEquals(runner2.getForm().getAddressBox().getCityField().getValue(), form.getCityField().getValue());

    form.doClose();

    runner1.remove();
    runner2.remove();
  }

  @Test
  public void testModifyRunner() throws ProcessingException {
    RunnerTestDataProvider runner = new RunnerTestDataProvider();

    EntryForm form = new EntryForm();
    form.startNew();
    FMilaClientTestUtility.doColumnEdit(form.getEventsField().getTable().getEventNrColumn(), 0, event.getEventNr());
    FMilaClientTestUtility.doColumnEdit(form.getEventsField().getTable().getEventClassColumn(), 0, event.getClassUid());

    form.getRunnerField().setValue(runner.getRunnerNr());
    form.getLastNameField().setValue("CHANGE_LASTNAME");
    form.getFirstNameField().setValue("CHANGE_FIRSTNAME");
    form.getYearField().setValue(1999L);

    RunnerBean formData = form.getRacesField().getTable().getRaceBeanColumn().getValue(0).getRunner();
    Assert.assertEquals("CHANGE_LASTNAME", formData.getLastName());
    Assert.assertEquals("CHANGE_FIRSTNAME", formData.getFirstName());
    Assert.assertEquals(1999L, formData.getYear().longValue());

    form.doClose();
    runner.remove();
  }

  @Test
  public void testMassStart() throws Exception {
    StartlistSettingForm form = new StartlistSettingForm();
    form.setNewClassUid(event.getClassUid());
    form.setParticipationCount(7L);
    form.setEventNr(event.getEventNr());
    form.startNew();
    form.getTypeUidField().setValue(StartlistTypeCodeType.MassStartCode.ID);
    form.getFirstStartField().setValue(DateUtility.parse("02072012", "ddMMyyyy"));
    form.doOk();

    RunnerTestDataProvider runner = new RunnerTestDataProvider();

    EntryForm entry = new EntryForm();
    entry.startNew();
    entry.getRunnerField().setValue(runner.getRunnerNr());
    entry.getClazzField().setValue(event.getClassUid());

    Assert.assertEquals("Mass Start Time", form.getFirstStartField().getValue(), entry.getEventsField().getTable().getStartTimeColumn().getValue(0));
    Assert.assertEquals("Mass Start Time", form.getFirstStartField().getValue(), entry.getRacesField().getTable().getLegStartTimeColumn().getValue(0));
    entry.getCurrencyField().setValue(currency.getCurrencyUid());
    entry.doOk();

    EntryForm entry2 = new EntryForm();
    entry2.setEntryNr(entry.getEntryNr());
    entry2.startModify();
    Assert.assertEquals("Mass Start Time", form.getFirstStartField().getValue(), entry2.getEventsField().getTable().getStartTimeColumn().getValue(0));
    Assert.assertEquals("Mass Start Time", form.getFirstStartField().getValue(), entry2.getRacesField().getTable().getLegStartTimeColumn().getValue(0));
    FMilaClientTestUtility.doColumnEdit(entry2.getEventsField().getTable().getStartTimeColumn(), 0, null);
    entry2.doOk();

    EntryForm entry3 = new EntryForm();
    entry3.setEntryNr(entry.getEntryNr());
    entry3.startModify();
    Assert.assertNull("Mass Start Time", entry3.getEventsField().getTable().getStartTimeColumn().getValue(0));
    Assert.assertNull("Mass Start Time", entry3.getRacesField().getTable().getLegStartTimeColumn().getValue(0));
    entry3.doOk();
  }

  @After
  public void after() throws ProcessingException {
    event.remove();
    currency.remove();
  }

}
