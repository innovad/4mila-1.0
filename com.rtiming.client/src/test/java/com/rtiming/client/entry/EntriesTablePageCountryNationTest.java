package com.rtiming.client.entry;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.race.RaceForm;
import com.rtiming.client.result.ResultsTablePage;
import com.rtiming.client.result.SingleEventSearchForm;
import com.rtiming.client.settings.city.CityForm;
import com.rtiming.client.settings.city.CountryForm;
import com.rtiming.client.test.data.CityTestDataProvider;
import com.rtiming.client.test.data.CountryTestDataProvider;
import com.rtiming.client.test.data.EntryTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualClassTestDataProvider;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.race.RaceStatusCodeType;
import com.rtiming.shared.settings.IDefaultProcessService;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class EntriesTablePageCountryNationTest {

  private EventWithIndividualClassTestDataProvider event;
  private EntryTestDataProvider entry;
  private CountryTestDataProvider country1;
  private CountryTestDataProvider country2;
  private CityTestDataProvider city;
  private Long defaultEventNr;

  @Before
  public void before() throws ProcessingException {
    defaultEventNr = BEANS.get(IDefaultProcessService.class).getDefaultEventNr();
    event = new EventWithIndividualClassTestDataProvider();
    BEANS.get(IDefaultProcessService.class).setDefaultEventNr(event.getEventNr());

    entry = new EntryTestDataProvider(event.getEventNr(), event.getClassUid());
    city = new CityTestDataProvider();

    // country 1
    List<FieldValue> list = new ArrayList<FieldValue>();
    list.add(new FieldValue(CountryForm.MainBox.CountryCodeField.class, "AA"));
    list.add(new FieldValue(CountryForm.MainBox.NationField.class, "AAA"));
    country1 = new CountryTestDataProvider(list);

    // country 2
    list = new ArrayList<FieldValue>();
    list.add(new FieldValue(CountryForm.MainBox.CountryCodeField.class, "QQ"));
    list.add(new FieldValue(CountryForm.MainBox.NationField.class, "QQQ"));
    country2 = new CountryTestDataProvider(list);
  }

  @Test
  public void testCorrectNation() throws Exception {
    // Modify City
    CityForm cityForm = new CityForm();
    cityForm.setCityNr(city.getCityNr());
    cityForm.startModify();
    cityForm.getCountryField().setValue(country2.getCountryUid());
    cityForm.doOk();

    RaceForm race = new RaceForm();
    race.setRaceNr(entry.getRaceNr());
    race.startModify();

    race.getNationField().setValue(country1.getCountryUid());
    race.getAddressBox().getCityField().setValue(city.getCityNr());
    race.getManualStatusField().setValue(true);
    race.getRaceStatusField().setValue(RaceStatusCodeType.DisqualifiedCode.ID);

    race.doOk();

    EntriesTablePage entries = new EntriesTablePage(event.getEventNr(), ClientSession.get().getSessionClientNr(), null, event.getClassUid(), null, null);
    entries.loadChildren();

    // Assert Nation AND Country Different and correct
    Assert.assertEquals("1 row", 1, entries.getTable().getRowCount());
    Assert.assertEquals("Nation correct", country1.getForm().getNationField().getValue(), entries.getTable().getNationColumn().getValue(0));
    Assert.assertEquals("Country correct", country2.getCountryUid().longValue(), entries.getTable().getCountryColumn().getValue(0).longValue());

    ResultsTablePage results = new ResultsTablePage(ClientSession.get().getSessionClientNr(), event.getClassUid(), null, null);
    ((SingleEventSearchForm) results.getSearchFormInternal()).getEventField().setValue(event.getEventNr());
    results.loadChildren();

    // Assert Nation AND Country Different and correct
    Assert.assertEquals("1 row", 1, results.getTable().getRowCount());
    Assert.assertEquals("Nation correct", country1.getForm().getNationField().getValue(), results.getTable().getNationColumn().getValue(0));
    Assert.assertEquals("Country correct", country2.getCountryUid().longValue(), results.getTable().getCountryColumn().getValue(0).longValue());
  }

  @After
  public void after() throws ProcessingException {
    event.remove();
    entry.remove();
    city.remove();
    country1.remove();
    country2.remove();

    if (defaultEventNr != null) {
      BEANS.get(IDefaultProcessService.class).setDefaultEventNr(defaultEventNr);
    }
  }

}
