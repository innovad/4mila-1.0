package com.rtiming.client.ranking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.basic.table.HeaderCell;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ExceptionHandler;
import org.eclipse.scout.rt.platform.util.collection.OrderedCollection;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.ranking.RankingSummaryResultsTablePage.Table.SummaryRankingColumn;
import com.rtiming.client.result.AbstractResultsTable;
import com.rtiming.shared.common.database.sql.BeanUtility;
import com.rtiming.shared.dao.RtRankingKey;
import com.rtiming.shared.ranking.IRankingProcessService;
import com.rtiming.shared.ranking.RankingFormData;
import com.rtiming.shared.ranking.RankingFormData.RankingBox;

public class RankingSummaryResultsTablePage extends AbstractPageWithTable<RankingSummaryResultsTablePage.Table> implements IHelpEnabledPage {

  private static final String RACES = "races";
  private static final String EVENT_COUNT = "eventCount";

  private final Long classUid;
  private final Long rankingNr;
  private final Long lastEventNr;
  private final FormulaScript formula;
  private final RankingBox formulaSettings;
  private final Long[] eventNrs;
  private final RankingEventsTablePage rankingEvent;

  public RankingSummaryResultsTablePage(Long rankingNr, Long lastEventNr, Long classUid) throws ProcessingException {
    super(false);

    this.classUid = classUid;
    this.rankingNr = rankingNr;
    this.lastEventNr = lastEventNr;

    // Settings
    RtRankingKey key = RtRankingKey.create(rankingNr);
    RankingFormData setting = BeanUtility.rankingBean2FormData(BEANS.get(IRankingProcessService.class).load(key));
    formulaSettings = setting.getRankingBox();

    // Formula
    formula = new FormulaScript(formulaSettings.getFormula().getValue());

    rankingEvent = new RankingEventsTablePage(rankingNr);
    rankingEvent.nodeAddedNotify();
    rankingEvent.loadChildren();

    List<Long> eventNrsList = new ArrayList<Long>();
    int i = 0;
    while (!eventNrsList.contains(lastEventNr) && i < rankingEvent.getTable().getRowCount()) {
      eventNrsList.add(rankingEvent.getTable().getEventNrColumn().getValue(i));
      i++;
    }
    eventNrs = eventNrsList.toArray(new Long[eventNrsList.size()]);

    callInitializer();
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Results");
  }

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Override
  protected void execInitTreeNode() {
    try {
      RtRankingKey key = RtRankingKey.create(rankingNr);
      RankingFormData rankingFormData = BeanUtility.rankingBean2FormData(BEANS.get(IRankingProcessService.class).load(key));
      String rankingTitle = rankingFormData.getName().getValue();
      getCellForUpdate().setText(rankingTitle + " (" + TEXTS.get("Total") + ")");
    }
    catch (ProcessingException e) {
      BEANS.get(ExceptionHandler.class).handle(e);
    }
  }

  @Override
  protected void execInitPage() throws ProcessingException {
    // show
    getTable().getRaceStatusColumn().setInitialVisible(true);

    // hide
    getTable().getTimeColumn().setInitialVisible(false);
    getTable().getTimeBehindColumn().setInitialVisible(false);
    getTable().getPercentColumn().setInitialVisible(false);
    getTable().getCityColumn().setInitialVisible(false);

    getTable().resetColumnVisibilities();
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {

    HashMap<Long, SummaryRanking> rankings = new HashMap<Long, SummaryRanking>();
    List<SummaryRanking> rankingsList = new ArrayList<SummaryRanking>();
    // calculate each event
    for (Long eventNr : eventNrs) {

      RankingEventResultsTablePage page = new RankingEventResultsTablePage(rankingNr, eventNr, classUid);
      page.loadChildren();

      for (EventRanking eventRanking : page.getEventRankings()) {
        SummaryRanking summaryRanking = null;
        if (rankings.get(eventRanking.getRunnerNr()) == null) {
          summaryRanking = new SummaryRanking(eventNrs, eventRanking.getEntryNr(), eventRanking.getRunnerNr(), formulaSettings);
          summaryRanking.setResultRow(eventRanking.getResultRow());
          rankings.put(eventRanking.getRunnerNr(), summaryRanking);
          rankingsList.add(summaryRanking);
        }
        else {
          summaryRanking = rankings.get(eventRanking.getRunnerNr());
        }
        summaryRanking.addEventRanking(eventNr, eventRanking);
      }
    }

    // do summary
    formula.putBinding(EVENT_COUNT, eventNrs.length);
    formula.putBinding(RACES, rankingsList);
    formula.eval();

    // Sorting, Ranking
    Collections.sort(rankingsList);
    RankingUtility.calculateRankings(rankingsList);

    // Prepare Data
    Object[][] data = new Object[rankingsList.size()][getTable().getColumnCount()];
    for (int k = 0; k < rankingsList.size(); k++) {
      SummaryRanking summaryRanking = rankingsList.get(k);
      data[k] = summaryRanking.getResultRow();
      data[k][getTable().getColumnSet().getColumnByClass(SummaryRankingColumn.class).getColumnIndex()] = summaryRanking.getPointsFormatted();
      data[k][getTable().getRankColumn().getColumnIndex()] = summaryRanking.getRank();
      data[k][getTable().getRaceStatusColumn().getColumnIndex()] = summaryRanking.getStatusUid();
      int stage = 1;
      for (EventRanking eventRanking : summaryRanking.getEventRankings()) {
        Object pointsFormatted = eventRanking.getPointsFormatted();
        if (!eventRanking.isBestRanking() && !StringUtility.isNullOrEmpty(StringUtility.emptyIfNull(pointsFormatted))) {
          data[k][getTable().getColumnSet().getColumnByClass(SummaryRankingColumn.class).getColumnIndex() + stage] = "(" + eventRanking.getPointsFormatted() + ")";
        }
        else {
          data[k][getTable().getColumnSet().getColumnByClass(SummaryRankingColumn.class).getColumnIndex() + stage] = pointsFormatted;
        }
        stage++;
        data[k][getTable().getColumnSet().getColumnByClass(SummaryRankingColumn.class).getColumnIndex() + stage] = eventRanking.getRank();
        stage++;
      }
    }

    importTableData(data);
  }

  @Order(10.0)
  public class Table extends AbstractResultsTable {

    @Override
    protected void injectColumnsInternal(OrderedCollection<IColumn<?>> columnList) {
      columnList.addLast(new SummaryRankingColumn());
      for (int i = 0; i < eventNrs.length; i++) {
        columnList.addLast(new EventRankingColumn(eventNrs[i], i + 1));
        columnList.addLast(new EventRankColumn(eventNrs[i], i + 1));
      }
    }

    @Order(99999999997.0)
    class SummaryRankingColumn extends AbstractStringColumn {

      @Override
      protected int getConfiguredWidth() {
        return 80;
      }

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Ranking");
      }

      @Override
      protected int getConfiguredHorizontalAlignment() {
        return 1;
      }

    }

    @Order(99999999998.0)
    class EventRankingColumn extends AbstractStringColumn {

      private final Long eventNr;
      private final int id;

      public EventRankingColumn(Long eventNr, int id) {
        super();
        this.eventNr = eventNr;
        this.id = id;
      }

      @Override
      protected void execDecorateHeaderCell(HeaderCell cell) throws ProcessingException {
        cell.setText(rankingEvent.getTable().getEventColumn().getValue(rankingEvent.getTable().getEventNrColumn().findRow(eventNr)));
      }

      @Override
      public String getColumnId() {
        return super.getColumnId() + id;
      }

      @Override
      protected int getConfiguredWidth() {
        return 80;
      }

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Event");
      }

      @Override
      protected int getConfiguredHorizontalAlignment() {
        return 1;
      }

    }

    @Order(99999999999.0)
    class EventRankColumn extends AbstractLongColumn {

      private final Long eventNr;
      private final int id;

      public EventRankColumn(Long eventNr, int id) {
        super();
        this.eventNr = eventNr;
        this.id = id;
      }

      @Override
      public String getColumnId() {
        return super.getColumnId() + id;
      }

      @Override
      protected int getConfiguredWidth() {
        return 40;
      }

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Rank");
      }

    }
  }
}
