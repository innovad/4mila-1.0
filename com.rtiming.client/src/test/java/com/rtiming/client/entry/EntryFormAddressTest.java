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

import com.rtiming.client.ClientSession;
import com.rtiming.client.runner.AbstractAddressBox.CityField;
import com.rtiming.client.runner.AbstractAddressBox.FaxField;
import com.rtiming.client.runner.AbstractAddressBox.MobileField;
import com.rtiming.client.runner.AbstractAddressBox.PhoneField;
import com.rtiming.client.runner.RunnerForm;
import com.rtiming.client.test.data.CityTestDataProvider;
import com.rtiming.client.test.data.CurrencyTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualClassTestDataProvider;
import com.rtiming.client.test.data.RunnerTestDataProvider;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.common.database.sql.RunnerBean;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class EntryFormAddressTest {

  private RunnerTestDataProvider runner;
  private EventWithIndividualClassTestDataProvider event;
  private CityTestDataProvider city;
  private CityTestDataProvider city2;
  private CurrencyTestDataProvider currency;

  @Test
  public void testNewEntryAndAddressChangeOnRunner() throws Exception {
    event = new EventWithIndividualClassTestDataProvider();
    city = new CityTestDataProvider();
    city2 = new CityTestDataProvider();
    currency = new CurrencyTestDataProvider();
    List<FieldValue> values = new ArrayList<FieldValue>();
    values.add(new FieldValue(FaxField.class, "Fax ABC"));
    values.add(new FieldValue(PhoneField.class, "Phone ABC"));
    values.add(new FieldValue(MobileField.class, "Mobile ABC"));
    values.add(new FieldValue(CityField.class, city.getCityNr()));
    runner = new RunnerTestDataProvider(values);

    EntryForm entry = new EntryForm();
    entry.startNew();
    entry.getEventsField().getTable().getEventNrColumn().fill(event.getEventNr());

    entry.getRunnerField().setValue(runner.getRunnerNr());
    entry.getClazzField().setValue(event.getClassUid());
    entry.getCurrencyField().setValue(currency.getCurrencyUid());

    RunnerBean runnerBean = entry.getRacesField().getTable().getRaceBeanColumn().getValue(0).getRunner();
    Assert.assertEquals("Address Fax propagated", "Fax ABC", runnerBean.getAddress().getFax());

    entry.doOk();

    // now change runner
    RunnerForm runner2 = new RunnerForm();
    runner2.setRunnerNr(runner.getRunnerNr());
    runner2.startModify();
    Assert.assertEquals("Address Fax before change", "Fax ABC", runner2.getAddressBox().getFaxField().getValue());
    Assert.assertEquals("Address Phone before change", "Phone ABC", runner2.getAddressBox().getPhoneField().getValue());
    Assert.assertEquals("Address Mobile before change", "Mobile ABC", runner2.getAddressBox().getMobileField().getValue());
    Assert.assertEquals("Address City before change", city.getCityNr(), runner2.getAddressBox().getCityField().getValue());

    runner2.getAddressBox().getFaxField().setValue("Fax DEF");
    runner2.getAddressBox().getPhoneField().setValue("Phone DEF");
    runner2.getAddressBox().getMobileField().setValue("Mobile DEF");
    runner2.getAddressBox().getCityField().setValue(city2.getCityNr());
    runner2.doOk();

    // verify address has not changed on entry
    EntryForm entry2 = new EntryForm();
    entry2.setEntryNr(entry.getEntryNr());
    entry2.startModify();

    runnerBean = entry.getRacesField().getTable().getRaceBeanColumn().getValue(0).getRunner();
    Assert.assertEquals("Address Fax after change", "Fax ABC", runnerBean.getAddress().getFax());
    Assert.assertEquals("Address Phone after change", "Phone ABC", runnerBean.getAddress().getPhone());
    Assert.assertEquals("Address Mobile after change", "Mobile ABC", runnerBean.getAddress().getMobile());
    Assert.assertEquals("Address City after change", city.getCityNr(), runnerBean.getAddress().getCityNr());

    entry2.doClose();

    // verify start list
    EntriesTablePage entries = new EntriesTablePage(event.getEventNr(), ClientSession.get().getSessionClientNr(), null, null, null, null);
    entries.nodeAddedNotify();
    entries.loadChildren();

    Assert.assertEquals("Event Search", event.getEventNr(), entries.getSearchForm().getEventField().getValue());
    Assert.assertEquals("Event", entries.getEventNr(), entry2.getEventsField().getTable().getEventNrColumn().getValue(0));
    Assert.assertEquals("1 Entry", 1, entries.getTable().getRowCount());

    Assert.assertEquals("Entry exists", entry2.getEntryNr(), entries.getTable().getEntryNrColumn().getValue(0));
    Assert.assertEquals("ZIP correct", city.getForm().getZipField().getValue(), entries.getTable().getZipColumn().getValue(0));
    Assert.assertEquals("Fax correct", "Fax ABC", entries.getTable().getFaxColumn().getValue(0));

    // check address nrs
    entry2 = new EntryForm();
    entry2.setEntryNr(entry.getEntryNr());
    entry2.startModify();

    Long entryAddressNr = entry.getRacesField().getTable().getRaceBeanColumn().getValue(0).getAddress().getAddressNr();
    Long entry2AddressNr = entry2.getRacesField().getTable().getRaceBeanColumn().getValue(0).getAddress().getAddressNr();
    Assert.assertEquals("Race Address Nr.", entry2AddressNr, entryAddressNr);

    runnerBean = entry2.getRacesField().getTable().getRaceBeanColumn().getValue(0).getRunner();
    Assert.assertEquals("Runner Address Nr.", runner2.getAddressNr(), runner.getForm().getAddressNr());
    Assert.assertEquals("Runner Address Nr.", runnerBean.getAddress().getAddressNr(), runner.getForm().getAddressNr());
    Assert.assertEquals("Runner Address Nr.", runnerBean.getAddress().getAddressNr(), runner2.getAddressNr());
    Assert.assertTrue("Address Nr. Entry/Runner different", entry2AddressNr.longValue() != runner2.getAddressNr());
  }

  @After
  public void after() throws ProcessingException {
    if (event != null) {
      event.remove();
    }
    if (runner != null) {
      runner.remove();
    }
    if (city != null) {
      city.remove();
    }
    if (currency != null) {
      currency.remove();
    }
  }

}
