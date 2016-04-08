package com.rtiming.client.dataexchange.iof;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.NumberUtility;

import com.rtiming.client.ClientSession;
import com.rtiming.client.dataexchange.AbstractXMLInterface;
import com.rtiming.client.race.RaceControlsTablePage;
import com.rtiming.client.result.ResultsClassesTablePage;
import com.rtiming.client.result.ResultsTablePage;
import com.rtiming.client.result.SingleEventSearchForm;
import com.rtiming.shared.dataexchange.AbstractDataBean;
import com.rtiming.shared.dataexchange.DataExchangeStartFormData;
import com.rtiming.shared.dataexchange.iof203.xml.ResultList;

public class IOF203ResultListInterface extends AbstractXMLInterface<IOF203ResultListDataBean> {

  private final Hashtable<Long, RaceControlsTablePage> splitsTables;
  private final Hashtable<Long, ResultsTablePage> resultsTables;

  public IOF203ResultListInterface(DataExchangeStartFormData interfaceConfig) throws ProcessingException {
    super(interfaceConfig);
    resultsTables = new Hashtable<Long, ResultsTablePage>();
    splitsTables = new Hashtable<Long, RaceControlsTablePage>();
  }

  @Override
  public Object createXMLRootObject() throws ProcessingException {
    ResultList resultList = new ResultList();
    resultList.setIOFVersion(IOF203Utility.getIOFVersion203());
    resultList.setModifyDate(IOF203Utility.getModifyDate());
    resultList.getEventIdOrEvent().add(IOF203Utility.getEvent(getEventNr()));
    return resultList;
  }

  @Override
  public AbstractDataBean createNewBean(Long primaryKeyNr) {
    ResultsTablePage results = resultsTables.get(primaryKeyNr);
    RaceControlsTablePage splits = splitsTables.get(primaryKeyNr);
    return new IOF203ResultListDataBean(primaryKeyNr, getEventNr(), results, splits);
  }

  @Override
  public ArrayList<String> getColumnHeaders() {
    ArrayList<String> list = new ArrayList<String>();
    list.add("Kategorie");
    list.add("Anzahl");
    return list;
  }

  @Override
  public List<Long> getPrimaryKeyNrs() throws ProcessingException {

    ResultsClassesTablePage classes = new ResultsClassesTablePage(ClientSession.get().getSessionClientNr());
    SingleEventSearchForm search = (SingleEventSearchForm) classes.getSearchFormInternal();
    search.getEventField().setValue(getEventNr());
    search.resetSearchFilter();
    classes.loadChildren();

    ArrayList<Long> classUids = new ArrayList<Long>();
    for (int k = 0; k < classes.getTable().getRowCount(); k++) {
      if (NumberUtility.nvl(classes.getTable().getProcessedColumn().getValue(k), 0) > 0 && classes.getTable().getClassUidColumn().getValue(k) != null) {
        classUids.add(classes.getTable().getClassUidColumn().getValue(k));
      }
    }

    for (Long classUid : classUids) {
      // Load Result List
      ResultsTablePage results = new ResultsTablePage(ClientSession.get().getSessionClientNr(), classUid, null, null);
      SingleEventSearchForm eventSearch = (SingleEventSearchForm) results.getSearchFormInternal();
      eventSearch.getEventField().setValue(getEventNr());
      eventSearch.resetSearchFilter();
      results.loadChildren();
      resultsTables.put(classUid, results);

      // Load Splits for all Races
      RaceControlsTablePage splits = new RaceControlsTablePage(ClientSession.get().getSessionClientNr(), results.getTable().getRaceNrColumn().getValues().toArray(new Long[0]));
      splits.loadChildren();

      splitsTables.put(classUid, splits);
    }

    return classUids;
  }

}
