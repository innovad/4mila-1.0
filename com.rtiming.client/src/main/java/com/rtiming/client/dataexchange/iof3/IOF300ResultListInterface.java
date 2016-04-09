package com.rtiming.client.dataexchange.iof3;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.NumberUtility;

import com.rtiming.client.ClientSession;
import com.rtiming.client.dataexchange.AbstractXMLInterface;
import com.rtiming.client.result.ResultsClassesTablePage;
import com.rtiming.client.result.ResultsTablePage;
import com.rtiming.client.result.SingleEventSearchForm;
import com.rtiming.shared.dataexchange.DataExchangeStartFormData;
import com.rtiming.shared.dataexchange.iof3.IOF300Utility;
import com.rtiming.shared.dataexchange.iof3.xml.ResultList;
import com.rtiming.shared.event.IEventsOutlineService;
import com.rtiming.shared.event.RaceControlRowData;

public class IOF300ResultListInterface extends AbstractXMLInterface<IOF300ResultListDataBean> {

  private final Hashtable<Long, List<RaceControlRowData>> splitsTables;
  private final Hashtable<Long, ResultsTablePage> resultsTables;

  public IOF300ResultListInterface(DataExchangeStartFormData interfaceConfig) throws ProcessingException {
    super(interfaceConfig);
    resultsTables = new Hashtable<Long, ResultsTablePage>();
    splitsTables = new Hashtable<Long, List<RaceControlRowData>>();
  }

  @Override
  public Object createXMLRootObject() throws ProcessingException {
    ResultList resultList = new ResultList();
    resultList.setIofVersion(IOF300Utility.getIOFVersion());
    resultList.setCreateTime(IOF300Utility.getCurrentModifyDateTime());
    resultList.setCreator(IOF300Utility.getCreator());
    resultList.setEvent(IOF300Utility.convertToXmlEvent(getEventNr()));
    return resultList;
  }

  @Override
  public IOF300ResultListDataBean createNewBean(Long primaryKeyNr) {
    ResultsTablePage results = resultsTables.get(primaryKeyNr);
    List<RaceControlRowData> splits = splitsTables.get(primaryKeyNr);
    return new IOF300ResultListDataBean(primaryKeyNr, getEventNr(), results, splits);
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
      List<RaceControlRowData> splits = BEANS.get(IEventsOutlineService.class).getRaceControlTableData(ClientSession.get().getSessionClientNr(), results.getTable().getRaceNrColumn().getValues().toArray(new Long[0]));
      splitsTables.put(classUid, splits);
    }

    return classUids;
  }

}
