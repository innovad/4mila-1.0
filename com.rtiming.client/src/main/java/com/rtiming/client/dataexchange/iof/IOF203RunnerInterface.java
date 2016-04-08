package com.rtiming.client.dataexchange.iof;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.client.dataexchange.AbstractXMLInterface;
import com.rtiming.client.runner.RunnersTablePage;
import com.rtiming.client.runner.RunnersTablePage.Table;
import com.rtiming.shared.dataexchange.AbstractDataBean;
import com.rtiming.shared.dataexchange.DataExchangeStartFormData;
import com.rtiming.shared.dataexchange.iof203.xml.Competitor;
import com.rtiming.shared.dataexchange.iof203.xml.CompetitorList;

public class IOF203RunnerInterface extends AbstractXMLInterface<IOF203RunnerDataBean> {

  private final Hashtable<Long, ITableRow> runnerList;

  public IOF203RunnerInterface(DataExchangeStartFormData interfaceConfig) throws ProcessingException {
    super(interfaceConfig);
    this.setJaxbObject(new CompetitorList());
    runnerList = new Hashtable<Long, ITableRow>();
  }

  @Override
  public Object createXMLRootObject() throws ProcessingException {

    CompetitorList competitorList = new CompetitorList();
    competitorList.setIOFVersion(IOF203Utility.getIOFVersion203());

    return competitorList;
  }

  @Override
  public AbstractDataBean createNewBean(Long primaryKeyNr) {
    ITableRow row = null;
    if (primaryKeyNr != null) {
      row = runnerList.get(primaryKeyNr);
    }
    return new IOF203RunnerDataBean(primaryKeyNr, row);
  }

  @Override
  protected ArrayList<? extends IOF203RunnerDataBean> createBeansFromXMLObject(Object xmlData) throws ProcessingException {
    ArrayList<IOF203RunnerDataBean> list = new ArrayList<IOF203RunnerDataBean>();

    CompetitorList competitorList = (CompetitorList) xmlData;
    for (Competitor competitor : competitorList.getCompetitor()) {
      IOF203RunnerDataBean runner = new IOF203RunnerDataBean(getEventNr(), null);
      runner.setCompetitor(competitor);
      list.add(runner);
    }

    return list;
  }

  @Override
  public List<Long> getPrimaryKeyNrs() throws ProcessingException {
    RunnersTablePage runner = new RunnersTablePage();
    runner.nodeAddedNotify();
    runner.loadChildren();

    Table runnerTable = runner.getTable();
    for (int k = 0; k < runnerTable.getRowCount(); k++) {
      runnerList.put(runnerTable.getRunnerNrColumn().getValue(k), runnerTable.getRow(k));
    }

    return runnerTable.getRunnerNrColumn().getValues();
  }

  @Override
  public ArrayList getColumnHeaders() {
    ArrayList<String> list = new ArrayList<String>();
    if (isImport()) {
      list.add(TEXTS.get("Replace"));
    }
    list.add(TEXTS.get("Number"));
    list.add(TEXTS.get("LastName"));
    list.add(TEXTS.get("FirstName"));
    list.add(TEXTS.get("ECard"));
    return list;
  }

}
