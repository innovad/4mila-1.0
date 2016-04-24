package com.rtiming.client.entry;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.TestClientSession;
import com.rtiming.client.race.RaceForm;
import com.rtiming.client.runner.RunnerForm;
import com.rtiming.client.runner.RunnerForm.MainBox.ClubField;
import com.rtiming.client.runner.RunnerForm.MainBox.NationUidField;
import com.rtiming.client.test.FMilaClientTestUtility;
import com.rtiming.client.test.data.ClubTestDataProvider;
import com.rtiming.client.test.data.CountryTestDataProvider;
import com.rtiming.client.test.data.CurrencyTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualClassTestDataProvider;
import com.rtiming.client.test.data.RunnerTestDataProvider;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.race.RaceStatusCodeType;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class EntryFormClubNationTest {

  private RunnerTestDataProvider runner;
  private EventWithIndividualClassTestDataProvider event;
  private ClubTestDataProvider club1;
  private CountryTestDataProvider country1;
  private ClubTestDataProvider club2;
  private CountryTestDataProvider country2;
  private CurrencyTestDataProvider currency;

  @Test
  public void testClubAndNationChangesBetweenEntryRaceAndRunner() throws Exception {
    event = new EventWithIndividualClassTestDataProvider();
    club1 = new ClubTestDataProvider();
    club2 = new ClubTestDataProvider();
    country1 = new CountryTestDataProvider();
    country2 = new CountryTestDataProvider();
    currency = new CurrencyTestDataProvider();

    List<FieldValue> list = new ArrayList<FieldValue>();
    list.add(new FieldValue(NationUidField.class, country1.getCountryUid()));
    list.add(new FieldValue(ClubField.class, club1.getClubNr()));
    runner = new RunnerTestDataProvider(list);

    EntryForm entry = new EntryForm();
    entry.startNew();
    entry.getCurrencyField().setValue(currency.getCurrencyUid());
    FMilaClientTestUtility.doColumnEdit(entry.getEventsField().getTable().getEventNrColumn(), 0, event.getEventNr());

    entry.getRunnerField().setValue(runner.getRunnerNr());
    entry.getClazzField().setValue(event.getClassUid());

    Assert.assertEquals("Club", club1.getClubNr(), entry.getClubField().getValue());
    Assert.assertEquals("Club", club1.getClubNr(), entry.getRacesField().getTable().getClubNrColumn().getValue(0));
    Assert.assertEquals("Nation", country1.getCountryUid(), entry.getRacesField().getTable().getNationColumn().getValue(0));

    entry.doOk();
    Long entryNr = entry.getEntryNr();
    Assert.assertNotNull("EntryNr created", entryNr);

    // find raceNr
    entry = new EntryForm();
    entry.setEntryNr(entryNr);
    entry.startModify();
    Long raceNr = entry.getRacesField().getTable().getRaceNrColumn().getValue(0);
    Assert.assertNotNull("RaceNr created", raceNr);
    entry.doClose();

    // check race
    RaceForm race = new RaceForm();
    race.setRaceNr(raceNr);
    race.startModify();
    Assert.assertEquals("Club", club1.getClubNr(), race.getClubField().getValue());
    Assert.assertEquals("Nation", country1.getCountryUid(), race.getNationField().getValue());

    // modify race
    race.getClubField().setValue(club2.getClubNr());
    race.getNationField().setValue(country2.getCountryUid());
    race.getManualStatusField().setValue(true);
    race.getRaceStatusField().setValue(RaceStatusCodeType.DisqualifiedCode.ID);
    race.doOk();

    // check changes on entry
    entry = new EntryForm();
    entry.setEntryNr(entryNr);
    entry.startModify();

    Assert.assertEquals("Club", club2.getClubNr(), entry.getClubField().getValue());
    Assert.assertEquals("Club", club2.getClubNr(), entry.getRacesField().getTable().getClubNrColumn().getValue(0));
    Assert.assertEquals("Nation", country2.getCountryUid(), entry.getRacesField().getTable().getNationColumn().getValue(0));

    entry.doClose();

    // check existing values on runner
    RunnerForm runnerForm = new RunnerForm();
    runnerForm.setRunnerNr(runner.getRunnerNr());
    runnerForm.startModify();
    Assert.assertEquals("Club", club1.getClubNr(), runnerForm.getClubField().getValue());
    Assert.assertEquals("Nation", country1.getCountryUid(), runnerForm.getNationUidField().getValue());
    runnerForm.doClose();

    // modify entry
    entry = new EntryForm();
    entry.setEntryNr(entryNr);
    entry.startModify();
    entry.getClubField().setValue(club1.getClubNr());
    // modification of nation is not possible on entry form
    Assert.assertEquals("Club", club1.getClubNr(), entry.getRacesField().getTable().getClubNrColumn().getValue(0));
    entry.doOk();

    // check race
    race = new RaceForm();
    race.setRaceNr(raceNr);
    race.startModify();
    Assert.assertEquals("Club", club1.getClubNr(), race.getClubField().getValue());
    Assert.assertEquals("Nation", country2.getCountryUid(), race.getNationField().getValue());
    race.doClose();

    // check runner (runner is updated by changing entry)
    runnerForm = new RunnerForm();
    runnerForm.setRunnerNr(runner.getRunnerNr());
    runnerForm.startModify();
    Assert.assertEquals("Club", club1.getClubNr(), runnerForm.getClubField().getValue());
    Assert.assertEquals("Nation", country2.getCountryUid(), runnerForm.getNationUidField().getValue());

    // modify runner
    runnerForm.getClubField().setValue(club2.getClubNr());
    runnerForm.getNationUidField().setValue(country1.getCountryUid());
    runnerForm.doOk();

    // verify that the entry has not changed
    entry = new EntryForm();
    entry.setEntryNr(entryNr);
    entry.startModify();
    Assert.assertEquals("Club", club1.getClubNr(), entry.getClubField().getValue());
    Assert.assertEquals("Club", club1.getClubNr(), entry.getRacesField().getTable().getClubNrColumn().getValue(0));
    Assert.assertEquals("Nation", country2.getCountryUid(), entry.getRacesField().getTable().getNationColumn().getValue(0));
    entry.doClose();

    // verify race has not changed
    race = new RaceForm();
    race.setRaceNr(raceNr);
    race.startModify();
    Assert.assertEquals("Club", club1.getClubNr(), race.getClubField().getValue());
    Assert.assertEquals("Nation", country2.getCountryUid(), race.getNationField().getValue());
    race.doClose();
  }

  @After
  public void after() throws ProcessingException {
    if (event != null) {
      event.remove();
    }
    if (runner != null) {
      runner.remove();
    }
    if (club1 != null) {
      club1.remove();
    }
    if (country1 != null) {
      country1.remove();
    }
    if (club2 != null) {
      club2.remove();
    }
    if (country2 != null) {
      country2.remove();
    }
    if (currency != null) {
      currency.remove();
    }
  }

}
