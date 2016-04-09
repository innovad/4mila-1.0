package com.rtiming.client.result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.scout.rt.client.ui.basic.table.HeaderCell;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.customizer.ITableCustomizer;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.data.basic.FontSpec;
import org.eclipse.scout.rt.shared.data.basic.table.SortSpec;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.ClientSession;
import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.event.course.EventClassesTablePage;
import com.rtiming.client.result.RelayTableCustomizer.RelayPercentColumn;
import com.rtiming.client.result.RelayTableCustomizer.RelayRankColumn;
import com.rtiming.client.result.RelayTableCustomizer.RelayTimeBehindColumn;
import com.rtiming.client.result.RelayTableCustomizer.RelayTimeColumn;
import com.rtiming.client.result.split.LegResult;
import com.rtiming.shared.event.EventClassFormData;
import com.rtiming.shared.event.IEventClassProcessService;
import com.rtiming.shared.race.RaceStatusCodeType;

public class RelayAfterLegResultsTablePage extends AbstractPageWithTable<RelayAfterLegResultsTablePage.Table> implements IHelpEnabledPage {

  private final Long eventNr;
  private final Long parentUid;
  private final Long classUid;

  public RelayAfterLegResultsTablePage(Long eventNr, Long parentUid, Long classUid) {
    this.eventNr = eventNr;
    this.parentUid = parentUid;
    this.classUid = classUid;
  }

  @Override
  protected void execInitPage() throws ProcessingException {
    // there columns are not visible initially for relay results
    getTable().getClubColumn().setVisibleGranted(false);
    getTable().getCityColumn().setVisibleGranted(false);
    // add "Leg" to header cells
    ((HeaderCell) getTable().getRankColumn().getHeaderCell()).setText(getTable().getRankColumn().getHeaderCell().getText() + " " + TEXTS.get("Leg"));
    ((HeaderCell) getTable().getTimeColumn().getHeaderCell()).setText(getTable().getTimeColumn().getHeaderCell().getText() + " " + TEXTS.get("Leg"));
    ((HeaderCell) getTable().getTimeBehindColumn().getHeaderCell()).setText(getTable().getTimeBehindColumn().getHeaderCell().getText() + " " + TEXTS.get("Leg"));
    ((HeaderCell) getTable().getPercentColumn().getHeaderCell()).setText(getTable().getPercentColumn().getHeaderCell().getText() + " " + TEXTS.get("Leg"));

    getTable().resetColumnVisibilities();
  }

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("RelayResults");
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    // parent settings
    EventClassFormData eventClass = new EventClassFormData();
    eventClass.getEvent().setValue(eventNr);
    eventClass.getClazz().setValue(classUid);
    eventClass = BEANS.get(IEventClassProcessService.class).load(eventClass);

    // get leg uid of selected legs
    // all legs before and including the selected legs are considered for the report
    EventClassesTablePage legs = new EventClassesTablePage(eventNr, parentUid);
    SortSpec spec = new SortSpec(legs.getTable().getSortCodeColumn().getColumnIndex(), true);
    legs.getTable().getColumnSet().setSortSpec(spec);
    legs.nodeAddedNotify();
    legs.loadChildren();

    List<Long> reportedLegUids = new ArrayList<Long>();
    boolean selectedLegPassed = false;
    for (int k = 0; k < legs.getTable().getRowCount(); k++) {
      if (!selectedLegPassed) {
        reportedLegUids.add(legs.getTable().getClazzColumn().getValue(k));
      }
      if (CompareUtility.equals(legs.getTable().getClazzColumn().getValue(k), classUid)) {
        selectedLegPassed = true;
      }
    }

    List<ResultsTablePage> list = new ArrayList<ResultsTablePage>();
    Map<Long, RelayResult> relays = new HashMap<Long, RelayResult>();
    List<RelayResult> relayList = new ArrayList<RelayResult>();
    Set<Long> entryNrs = new HashSet<Long>();
    int rowCount = 0;
    for (Long legUid : reportedLegUids) {
      ResultsTablePage legResults = new ResultsTablePage(ClientSession.get().getSessionClientNr(), legUid, null, null);
      SingleEventSearchForm searchForm = (SingleEventSearchForm) legResults.getSearchFormInternal();
      searchForm.getEventField().setValue(eventNr);
      searchForm.resetSearchFilter();
      legResults.loadChildren();
      list.add(legResults);
      rowCount += legResults.getTable().getRowCount();
      entryNrs.addAll(new HashSet<Long>(legResults.getTable().getEntryNrColumn().getValues()));

      // analyze data
      for (int k = 0; k < legResults.getTable().getRowCount(); k++) {
        Long entryNr = legResults.getTable().getEntryNrColumn().getValue(k);
        RelayResult relay = relays.get(entryNr);
        if (relay == null) {
          relay = new RelayResult(entryNr, eventClass.getTimePrecision().getValue());
          relayList.add(relay);
          relay.setTeamName(legResults.getTable().getClubColumn().getValue(k));
          relays.put(entryNr, relay);
        }
        relay.addLegResult(legUid, legResults.getTable().getTableData()[k], legResults.getTable().getLegTimeColumn().getValue(k), legResults.getTable().getRaceStatusColumn().getValue(k));
      }
    }

    // calculate summary result
    for (RelayResult relay : relayList) {
      relay.calculate(reportedLegUids);
    }
    // sort by rank and status
    Collections.sort(relayList);
    // winning times
    HashMap<Long, Long> winningTimes = new HashMap<Long, Long>();
    for (RelayResult relay : relayList) {
      for (LegResult leg : relay.getLegs()) {
        Long time = leg.getSummaryTime();
        Long winningTime = winningTimes.get(leg.getLegUid());
        if (winningTime == null) {
          winningTime = time;
        }
        else if (time != null && winningTime != null) {
          winningTime = Math.min(winningTime, time);
        }
        if (winningTime != null) {
          winningTimes.put(leg.getLegUid(), winningTime);
        }
      }
    }
    Long winningTime = winningTimes.get(reportedLegUids.get(reportedLegUids.size() - 1));
    for (RelayResult relay : relayList) {
      relay.calculateTimeBehind(winningTime, winningTimes);
    }
    // calculate rank
    long rank = 1;
    long lastRank = 1;
    Long lastTime = null;
    int validRelayCount = 0;
    int validRowCount = 0;
    for (RelayResult relay : relayList) {
      if (CompareUtility.equals(relay.getStatusUid(), RaceStatusCodeType.OkCode.ID)) {
        if (CompareUtility.equals(lastTime, relay.getSummaryTime())) {
          relay.setRank(lastRank);
        }
        else {
          relay.setRank(rank);
          lastRank = rank;
        }
        rank++;
      }
      if (isValidStatus(relay.getStatusUid())) {
        validRelayCount++;
        validRowCount += relay.getLegs().size();
      }
      lastTime = relay.getSummaryTime();
    }

    Object[][] data = new Object[validRowCount + validRelayCount][];
    rowCount = 0;
    for (RelayResult relay : relayList) {
      if (isValidStatus(relay.getStatusUid())) {
        Object[] summaryRow = new Object[getTable().getColumnCount()];
        summaryRow[getTable().getColumnSet().getColumnByClass(RelayTimeColumn.class).getColumnIndex()] = relay.getFormattedSummaryTime();
        summaryRow[getTable().getRunnerColumn().getColumnIndex()] = relay.getTeamName();
        summaryRow[getTable().getColumnSet().getColumnByClass(RelayRankColumn.class).getColumnIndex()] = relay.getRank();
        summaryRow[getTable().getColumnSet().getColumnByClass(RelayTimeBehindColumn.class).getColumnIndex()] = relay.getFormattedTimeBehind();
        summaryRow[getTable().getColumnSet().getColumnByClass(RelayPercentColumn.class).getColumnIndex()] = relay.getPercentBehind();
        data[rowCount] = summaryRow;
        rowCount++;
        for (LegResult leg : relay.getLegs()) {
          data[rowCount] = leg.getResultRow();
          data[rowCount][getTable().getColumnSet().getColumnByClass(RelayTimeColumn.class).getColumnIndex()] = leg.getFormattedSummaryTime();
          data[rowCount][getTable().getColumnSet().getColumnByClass(RelayTimeBehindColumn.class).getColumnIndex()] = leg.getFormattedTimeBehind();
          data[rowCount][getTable().getColumnSet().getColumnByClass(RelayPercentColumn.class).getColumnIndex()] = leg.getPercentBehind();
          rowCount++;
        }
      }
    }

    importTableData(data);
  }

  private boolean isValidStatus(Long statusUid) {
    return statusUid != null && statusUid != RaceStatusCodeType.DidNotStartCode.ID;
  }

  @Order(10.0)
  public class Table extends AbstractResultsTable {

    @Override
    public ITableCustomizer getTableCustomizer() {
      return new RelayTableCustomizer(RelayAfterLegResultsTablePage.this);
    }

    @Override
    protected void execDecorateRow(ITableRow row) throws ProcessingException {
      super.execDecorateRow(row);
      if (getEntryNrColumn().getValue(row) == null) {
        row.setFont(FontSpec.parse("bold"));
      }
    }

  }

}
