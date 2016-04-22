package com.rtiming.client.entry;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.entry.EntryForm.MainBox.TabBox.RacesBox.RacesField;
import com.rtiming.client.entry.EntryForm.MainBox.TabBox.RacesBox.RacesField.Table.AddRunnerMenu;
import com.rtiming.client.event.EventClassForm;
import com.rtiming.client.test.FMilaClientTestUtility;
import com.rtiming.client.test.data.CityTestDataProvider;
import com.rtiming.client.test.data.ClubTestDataProvider;
import com.rtiming.client.test.data.CurrencyTestDataProvider;
import com.rtiming.client.test.data.DownloadedECardTestDataProvider;
import com.rtiming.client.test.data.ECardStationTestDataProvider;
import com.rtiming.client.test.data.ECardTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualClassTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualValidatedRaceTestDataProvider;
import com.rtiming.client.test.data.RunnerTestDataProvider;
import com.rtiming.shared.event.course.ClassTypeCodeType;
import com.rtiming.shared.runner.SexCodeType;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestEnvironmentClientSession.class)
public class EntryFormAddRunnerTest {

  private EventWithIndividualClassTestDataProvider event;
  private RunnerTestDataProvider runner;
  private RunnerTestDataProvider runner2;
  private ECardTestDataProvider ecard;
  private ClubTestDataProvider club;
  private CityTestDataProvider city;
  private ECardStationTestDataProvider station;
  private DownloadedECardTestDataProvider download;
  private CurrencyTestDataProvider currency;

  private EntryForm form;

  @Test
  public void testAddRunner() throws Exception {
    event = new EventWithIndividualClassTestDataProvider();
    runner = new RunnerTestDataProvider();
    runner2 = new RunnerTestDataProvider();

    form = new EntryForm();
    form.startNew();
    FMilaClientTestUtility.doColumnEdit(form.getEventsField().getTable().getEventNrColumn(), 0, event.getEventNr());

    form.getRunnerField().setValue(runner.getRunnerNr());
    Assert.assertEquals("1 Row", 1, form.getRacesField().getTable().getRowCount());
    ITableRow firstRunnerRow = form.getRacesField().getTable().getRow(0);

    boolean runMenu = form.getRacesField().getTable().runMenu(AddRunnerMenu.class);
    Assert.assertTrue("Menu was run", runMenu);
    Assert.assertEquals("2 Rows", 2, form.getRacesField().getTable().getRowCount());

    ITableRow secondRunnerRow = form.getRacesField().getTable().getSelectedRow();
    Assert.assertNotNull("2nd Row selected", secondRunnerRow);
    Assert.assertEquals("1st Runner Name", runner.getForm().getLastNameField().getValue(), form.getRacesField().getTable().getLastNameColumn().getValue(firstRunnerRow));
    Assert.assertEquals("1st Runner Nr", runner.getRunnerNr(), form.getRacesField().getTable().getRunnerNrColumn().getValue(firstRunnerRow));
    Assert.assertNull("2nd Runner Name empty", form.getRacesField().getTable().getLastNameColumn().getValue(secondRunnerRow));
    Assert.assertNull("2nd Runner Nr empty", form.getRacesField().getTable().getRunnerNrColumn().getValue(secondRunnerRow));

    Assert.assertFalse("1st Row selection", form.getRacesField().getTable().getRow(firstRunnerRow.getRowIndex()).isSelected());
    Assert.assertTrue("2nd Row selection", form.getRacesField().getTable().getRow(secondRunnerRow.getRowIndex()).isSelected());

    form.getRunnerField().setValue(runner2.getRunnerNr());
    Assert.assertEquals("2 Rows", 2, form.getRacesField().getTable().getRowCount());

    ITableRow row1 = form.getRacesField().getTable().getRunnerNrColumn().findRow(runner.getRunnerNr());
    ITableRow row2 = form.getRacesField().getTable().getRunnerNrColumn().findRow(runner2.getRunnerNr());

    Assert.assertEquals("1st Runner Name", runner.getForm().getLastNameField().getValue(), form.getRacesField().getTable().getLastNameColumn().getValue(row1));
    Assert.assertEquals("1st Runner Nr", runner.getRunnerNr(), form.getRacesField().getTable().getRunnerNrColumn().getValue(row1));
    Assert.assertEquals("2nd Runner Name", runner2.getForm().getLastNameField().getValue(), form.getRacesField().getTable().getLastNameColumn().getValue(row2));
    Assert.assertEquals("2nd Runner Nr", runner2.getRunnerNr(), form.getRacesField().getTable().getRunnerNrColumn().getValue(row2));
  }

  @Test
  public void testSetLastNameFirst() throws Exception {
    EntryForm entry = createEntryForm();
    entry.getLastNameField().setValue("ABC");
    assertAndCloseForm(entry);
  }

  @Test
  public void testSetFirstNameFirst() throws Exception {
    EntryForm entry = createEntryForm();
    entry.getFirstNameField().setValue("ABC");
    assertAndCloseForm(entry);
  }

  @Test
  public void testSetClazzFirst() throws Exception {
    EntryForm entry = createEntryForm();
    entry.getClazzField().setValue(event.getClassUid());
    assertAndCloseForm(entry);
  }

  @Test
  public void testSetSexFirst() throws Exception {
    EntryForm entry = createEntryForm();
    entry.getSexField().setValue(SexCodeType.WomanCode.ID);
    assertAndCloseForm(entry);
  }

  @Test
  public void testSetYearFirst() throws Exception {
    EntryForm entry = createEntryForm();
    entry.getYearField().setValue(1975L);
    assertAndCloseForm(entry);
  }

  @Test
  public void testSetClubFirst() throws Exception {
    EntryForm entry = createEntryForm();
    club = new ClubTestDataProvider();
    entry.getClubField().setValue(club.getClubNr());
    assertAndCloseForm(entry);
  }

  @Test
  public void testSetCityFirst() throws Exception {
    EntryForm entry = createEntryForm();
    city = new CityTestDataProvider();
    entry.getCityField().setValue(city.getCityNr());
    assertAndCloseForm(entry);
  }

  private EntryForm createEntryForm() throws ProcessingException {
    event = new EventWithIndividualClassTestDataProvider();
    EntryForm entry = new EntryForm();
    entry.startNew();
    FMilaClientTestUtility.doColumnEdit(entry.getEventsField().getTable().getEventNrColumn(), 0, event.getEventNr());
    return entry;
  }

  private void assertAndCloseForm(EntryForm entry) throws ProcessingException {
    Assert.assertEquals("Row added", 1, entry.getRacesField().getTable().getRowCount());
    Assert.assertTrue("Row selected", entry.getRacesField().getTable().getRow(0).isSelected());

    entry.doClose();
  }

  @Test
  public void testRaceDeletion() throws Exception {
    event = new EventWithIndividualValidatedRaceTestDataProvider(new String[]{"31"}, new String[]{"31"});
    runner = new RunnerTestDataProvider();
    currency = new CurrencyTestDataProvider();

    EventClassForm eventClass = new EventClassForm();
    eventClass.getEventField().setValue(event.getEventNr());
    eventClass.getClazzField().setValue(event.getClassUid());
    eventClass.startModify();
    eventClass.getTypeField().setValue(ClassTypeCodeType.TeamCombinedCourseCode.ID);
    eventClass.getTeamSizeMinField().setValue(1L);
    eventClass.getTeamSizeMaxField().setValue(3L);
    eventClass.doOk();

    form = new EntryForm();
    form.startNew();
    FMilaClientTestUtility.doColumnEdit(form.getEventsField().getTable().getEventNrColumn(), 0, event.getEventNr());

    form.getRunnerField().setValue(runner.getRunnerNr());
    form.getClazzField().setValue(event.getClassUid());
    boolean run = form.getRacesField().getTable().runMenu(AddRunnerMenu.class);
    Assert.assertTrue("Menu did run", run);
    form.getRunnerField().setValue(runner.getRunnerNr());
    form.getClazzField().setValue(event.getClassUid());
    Assert.assertEquals("2 races", 2, form.getRacesField().getTable().getRowCount());
    form.getCurrencyField().setValue(currency.getCurrencyUid());

    form.doOk();
    Long entryNr = form.getEntryNr();

    form = new EntryForm();
    form.setEntryNr(entryNr);
    form.startModify();

    Long raceNr = form.getRacesField().getTable().getRaceNrColumn().getValue(0);
    form.getRacesField().getTable().selectFirstRow();
    run = form.getRacesField().getTable().runMenu(RacesField.Table.DeleteRunnerMenu.class);

    Assert.assertTrue("Menu did run", run);

    // add punch session
    station = new ECardStationTestDataProvider();
    ecard = new ECardTestDataProvider();
    download = new DownloadedECardTestDataProvider(event.getEventNr(), raceNr, ecard.getECardNr(), station.getECardStationNr());

    form.doOk(); // should delete race and race controls as well
  }

  @After
  public void after() throws Exception {
    if (form != null && form.isFormOpen()) {
      form.doClose();
    }
    if (download != null) {
      download.remove();
    }
    if (event != null) {
      event.remove();
    }
    if (runner != null) {
      runner.remove();
    }
    if (runner2 != null) {
      runner2.remove();
    }
    if (club != null) {
      club.remove();
    }
    if (city != null) {
      city.remove();
    }
    if (ecard != null) {
      ecard.remove();
    }
    if (station != null) {
      station.remove();
    }
    if (currency != null) {
      currency.remove();
    }
  }

}
