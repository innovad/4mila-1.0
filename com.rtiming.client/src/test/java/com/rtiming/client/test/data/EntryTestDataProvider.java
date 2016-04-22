package com.rtiming.client.test.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.data.basic.table.SortSpec;
import org.junit.Assert;

import com.rtiming.client.entry.EntryForm;
import com.rtiming.client.entry.EntryForm.MainBox.TabBox.RacesBox.RacesField.Table.AddRunnerMenu;
import com.rtiming.client.runner.RunnerForm.MainBox.ECardField;
import com.rtiming.client.test.FMilaClientTestUtility;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.common.database.sql.EntryBean;
import com.rtiming.shared.entry.IEntryProcessService;
import com.rtiming.shared.event.IEventProcessService;

public class EntryTestDataProvider extends AbstractTestDataProvider<EntryForm> {

  private final Long eventNr;
  private final Long[] classUids;
  private final CurrencyTestDataProvider currency;
  private final ECardTestDataProvider[] ecards;
  private final RunnerTestDataProvider[] runners;

  private Long[] raceNrs;
  private EntryForm entry;

  public EntryTestDataProvider(Long eventNr, Long classUid) throws ProcessingException {
    this(eventNr, new Long[]{classUid});
  }

  public EntryTestDataProvider(Long eventNr, Long... classUids) throws ProcessingException {
    this.eventNr = eventNr;
    this.classUids = classUids;
    this.currency = new CurrencyTestDataProvider();
    this.ecards = new ECardTestDataProvider[classUids.length];
    this.runners = new RunnerTestDataProvider[classUids.length];
    this.raceNrs = new Long[classUids.length];
    callInitializer();
  }

  @Override
  protected EntryForm createForm() throws ProcessingException {

    Assert.assertNotNull(eventNr);
    Assert.assertNotNull(classUids);

    // ECards
    for (int k = 0; k < ecards.length; k++) {
      ecards[k] = new ECardTestDataProvider();
    }

    // Runner
    for (int k = 0; k < runners.length; k++) {
      List<FieldValue> value = new ArrayList<FieldValue>();
      value.add(new FieldValue(ECardField.class, ecards[k].getECardNr()));
      runners[k] = new RunnerTestDataProvider(value);
    }

    // Entry
    entry = new EntryForm();
    entry.startNew();
    entry.getCurrencyField().setValue(currency.getCurrencyUid());

    // set Event
    Assert.assertEquals(1, entry.getEventsField().getTable().getRowCount());
    Date evtZero = BEANS.get(IEventProcessService.class).getZeroTime(eventNr);
    Assert.assertNotNull(evtZero);
    FMilaClientTestUtility.doColumnEdit(entry.getEventsField().getTable().getEventNrColumn(), 0, eventNr);
    FMilaClientTestUtility.doColumnEdit(entry.getEventsField().getTable().getEventClassColumn(), 0, classUids[0]);

    // set Runner and Class
    entry.getRunnerField().setValue(runners[0].getRunnerNr());
    int rowCount = entry.getRacesField().getTable().getRowCount();
    Assert.assertEquals(1, rowCount);
    entry.getClazzField().setValue(classUids[0]);

    for (int k = 1; k < classUids.length; k++) {
      boolean add = entry.getRacesField().getTable().runMenu(AddRunnerMenu.class);
      Assert.assertTrue("Row added", add);
      entry.getRunnerField().setValue(runners[k].getRunnerNr());
      rowCount = entry.getRacesField().getTable().getRowCount();
      Assert.assertEquals("Row exists", 1 + k, rowCount);
      entry.getClazzField().setValue(classUids[k]);
    }

    entry.doOk();
    Assert.assertNotNull(entry.getEntryNr());

    SortSpec spec = new SortSpec(entry.getRacesField().getTable().getLegColumn().getColumnIndex(), true);
    entry.getRacesField().getTable().getColumnSet().setSortSpec(spec);
    entry.getRacesField().getTable().sort();
    for (int i = 0; i < entry.getRacesField().getTable().getRowCount(); i++) {
      raceNrs[i] = entry.getRacesField().getTable().getRaceNrColumn().getValue(i);
      Assert.assertNotNull(raceNrs[i]);
    }

    return entry;
  }

  @Override
  public void remove() throws ProcessingException {
    EntryBean formData = new EntryBean();
    formData.setEntryNr(getEntryNr());
    BEANS.get(IEntryProcessService.class).delete(formData);

    for (RunnerTestDataProvider runner : runners) {
      runner.remove();
    }
    for (ECardTestDataProvider ecard : ecards) {
      ecard.remove();
    }
    currency.remove();
  }

  public Long getRunnerNr() throws ProcessingException {
    Assert.assertEquals("Allowed only for 1 race entries", 1, runners.length);
    return runners[0].getRunnerNr();
  }

  public Long getClubNr() throws ProcessingException {
    return runners[0].getClubNr();
  }

  public Long getEntryNr() throws ProcessingException {
    return entry.getEntryNr();
  }

  public Long getECardNr() throws ProcessingException {
    Assert.assertEquals("Allowed only for 1 race entries", 1, ecards.length);
    return ecards[0].getECardNr();
  }

  public Long[] getECardNrs() throws ProcessingException {
    List<Long> result = new ArrayList<Long>();
    for (ECardTestDataProvider ecard : ecards) {
      result.add(ecard.getECardNr());
    }
    return result.toArray(new Long[result.size()]);
  }

  public Long getRaceNr() throws ProcessingException {
    Assert.assertEquals("Allowed only for 1 race entries", 1, raceNrs.length);
    return raceNrs[0];
  }

  public Long[] getRaceNrs() throws ProcessingException {
    return raceNrs;
  }

  public Long getRegistrationNr() throws ProcessingException {
    return entry.getRegistrationField().getValue();
  }

}
