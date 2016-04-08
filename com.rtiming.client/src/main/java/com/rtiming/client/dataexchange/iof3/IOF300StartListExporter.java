package com.rtiming.client.dataexchange.iof3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.TreeSet;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.data.basic.table.SortSpec;

import com.rtiming.client.ClientSession;
import com.rtiming.client.dataexchange.AbstractXMLInterface;
import com.rtiming.client.entry.AbstractEntriesTablePage;
import com.rtiming.client.entry.EntriesTablePage;
import com.rtiming.shared.dataexchange.AbstractDataBean;
import com.rtiming.shared.dataexchange.DataExchangeStartFormData;
import com.rtiming.shared.dataexchange.iof3.IOF300Utility;
import com.rtiming.shared.dataexchange.iof3.xml.StartList;

public class IOF300StartListExporter extends AbstractXMLInterface {

  private final Hashtable<Long, ITableRow> entryList;

  public IOF300StartListExporter(DataExchangeStartFormData interfaceConfig) throws ProcessingException {
    super(interfaceConfig);
    entryList = new Hashtable<Long, ITableRow>();
  }

  @Override
  public List<Long> getPrimaryKeyNrs() throws ProcessingException {

    EntriesTablePage entries = new EntriesTablePage(getEventNr(), ClientSession.get().getSessionClientNr(), null, null, null, null);
    entries.nodeAddedNotify();
    entries.getTable().getColumnSet().setSortSpec(sortByClassTimeName(entries));
    entries.loadChildren();

    TreeSet<Long> entryNrs = new TreeSet<Long>();

    // unique numbers
    for (long k = 0; k < entries.getTable().getRowCount(); k++) {
      entryNrs.add(k);
      entryList.put(k, entries.getTable().getRow(NumberUtility.longToInt(k)));
    }

    return Arrays.asList(entryNrs.toArray(new Long[0]));
  }

  private SortSpec sortByClassTimeName(AbstractEntriesTablePage entries) {
    int[] indices = new int[3];
    indices[0] = entries.getTable().getClazzShortcutColumn().getColumnIndex();
    indices[1] = entries.getTable().getRelativeStartTimeColumn().getColumnIndex();
    indices[2] = entries.getTable().getRunnerColumn().getColumnIndex();
    boolean[] sort = new boolean[3];
    sort[0] = true;
    sort[1] = true;
    sort[2] = true;
    SortSpec spec = new SortSpec(indices, sort);
    return spec;
  }

  @Override
  public ArrayList<String> getColumnHeaders() {
    ArrayList<String> list = new ArrayList<String>();
    list.add(TEXTS.get("Class"));
    list.add(TEXTS.get("Runner"));
    return list;
  }

  @Override
  public AbstractDataBean createNewBean(Long primaryKeyNr) {
    ITableRow row = null;

    if (primaryKeyNr != null) {
      row = entryList.get(primaryKeyNr);
    }

    return new IOF300StartListDataBean(primaryKeyNr, row, getEventNr());
  }

  @Override
  public Object createXMLRootObject() throws ProcessingException {
    StartList startList = new StartList();
    startList.setIofVersion(IOF300Utility.getIOFVersion());
    startList.setCreateTime(IOF300Utility.getCurrentModifyDateTime());
    startList.setCreator(IOF300Utility.getCreator());
    startList.setEvent(IOF300Utility.convertToXmlEvent(getEventNr()));
    return startList;
  }
}
