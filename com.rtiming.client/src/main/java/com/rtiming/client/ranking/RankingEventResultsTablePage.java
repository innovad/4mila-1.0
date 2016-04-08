package com.rtiming.client.ranking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.scout.commons.CompareUtility;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ExceptionHandler;
import org.eclipse.scout.rt.platform.util.collection.OrderedCollection;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.ClientSession;
import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.race.RaceControlsTablePage;
import com.rtiming.client.ranking.RankingEventResultsTablePage.Table.RankingColumn;
import com.rtiming.client.result.AbstractResultsTable;
import com.rtiming.client.result.ResultsTablePage;
import com.rtiming.client.result.SingleEventSearchForm;
import com.rtiming.shared.common.database.sql.BeanUtility;
import com.rtiming.shared.dao.RtRankingEventKey;
import com.rtiming.shared.dao.RtRankingKey;
import com.rtiming.shared.race.RaceStatusCodeType;
import com.rtiming.shared.ranking.IRankingEventProcessService;
import com.rtiming.shared.ranking.IRankingProcessService;
import com.rtiming.shared.ranking.RankingEventFormData;
import com.rtiming.shared.ranking.RankingEventFormData.RankingBox;
import com.rtiming.shared.ranking.RankingFormData;

public class RankingEventResultsTablePage extends AbstractPageWithTable<RankingEventResultsTablePage.Table> implements IHelpEnabledPage {

  private static final String RACES = "races";
  private static final String WINNING_TIME = "winningTime";

  private final Long rankingNr;
  private final Long eventNr;
  private final Long classUid;
  private final List<EventRanking> eventRankings;

  public RankingEventResultsTablePage(Long rankingNr, Long eventNr, Long classUid) {
    super(false);
    this.rankingNr = rankingNr;
    this.eventNr = eventNr;
    this.classUid = classUid;
    eventRankings = new ArrayList<EventRanking>();
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
    if (rankingNr != null) {
      try {
        RtRankingKey key = RtRankingKey.create(rankingNr);
        RankingFormData rankingFormData = BeanUtility.rankingBean2FormData(BEANS.get(IRankingProcessService.class).load(key));
        String rankingTitle = rankingFormData.getName().getValue();
        getCellForUpdate().setText(rankingTitle);
      }
      catch (ProcessingException e) {
        BEANS.get(ExceptionHandler.class).handle(e);
      }
    }
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    eventRankings.clear();

    // Settings
    RtRankingEventKey key = new RtRankingEventKey();
    key.setRankingNr(rankingNr);
    key.setEventNr(eventNr);
    key.setClientNr(ClientSession.get().getSessionClientNr());
    RankingEventFormData setting = BeanUtility.rankingEventBean2FormData(BEANS.get(IRankingEventProcessService.class).load(key));
    RankingBox formulaSettings = setting.getRankingBox();

    // Formula
    FormulaScript formula = new FormulaScript(formulaSettings.getFormula().getValue());

    // Event Results
    ResultsTablePage results = new ResultsTablePage(ClientSession.get().getSessionClientNr(), classUid, null, null);
    results.nodeAddedNotify();
    SingleEventSearchForm search = (SingleEventSearchForm) results.getSearchFormInternal();
    search.getEventField().setValue(eventNr);
    search.resetSearchFilter();
    results.loadChildren();

    // Controls
    Map<Long, List<Control>> controls = new HashMap<>();
    RaceControlsTablePage races = new RaceControlsTablePage(ClientSession.get().getSessionClientNr(), results.getTable().getRaceNrColumn().getValues().toArray(new Long[0]));
    races.nodeAddedNotify();
    races.loadChildren();
    for (int k = 0; k < races.getTable().getRowCount(); k++) {
      Control control = new Control();
      control.setControlNo(races.getTable().getControlColumn().getValue(k));
      control.setLegTime(races.getTable().getLegTimeRawColumn().getValue(k));
      control.setOverallTime(races.getTable().getOverallTimeRawColumn().getValue(k));
      control.setStatusUid(races.getTable().getControlStatusColumn().getValue(k));
      control.setTypeUid(races.getTable().getControlTypeColumn().getValue(k));

      Long raceNr = races.getTable().getRaceNrColumn().getValue(k);
      List<Control> raceControls = controls.get(raceNr);
      if (raceControls == null) {
        raceControls = new ArrayList<Control>();
        controls.put(raceNr, raceControls);
      }
      raceControls.add(control);
    }

    // find winning time
    Long winningTime = null;
    if (results.getTable().getRowCount() > 0 && CompareUtility.equals(results.getTable().getRaceStatusColumn().getValue(0), RaceStatusCodeType.OkCode.ID)) {
      // get winning time (no rounding here)
      winningTime = results.getTable().getLegTimeColumn().getValue(0);
    }
    formula.putBinding(WINNING_TIME, winningTime);

    // evaluate formula for all races
    for (int k = 0; k < results.getTable().getRowCount(); k++) {
      Long time = results.getTable().getLegTimeColumn().getValue(k);
      Long statusUid = results.getTable().getRaceStatusColumn().getValue(k);
      Long runnerNr = results.getTable().getRunnerNrColumn().getValue(k);
      Long entryNr = results.getTable().getEntryNrColumn().getValue(k);
      Long raceNr = results.getTable().getRaceNrColumn().getValue(k);

      EventRanking ranking = new EventRanking(entryNr, runnerNr, time, statusUid, formulaSettings);
      ranking.setResultRow(results.getTable().getTableData()[k]);
      ranking.setControls(controls.get(raceNr));
      eventRankings.add(ranking);
    }

    formula.putBinding(RACES, eventRankings);
    formula.eval();

    Collections.sort(eventRankings);

    // calculate rank
    RankingUtility.calculateRankings(eventRankings);

    // prepare data
    Object[][] data = new Object[results.getTable().getRowCount()][];
    int rowCount = 0;
    for (AbstractRanking ranking : eventRankings) {
      data[rowCount] = ranking.getResultRow();
      data[rowCount][getTable().getColumnSet().getColumnByClass(RankingColumn.class).getColumnIndex()] = ranking.getPointsFormatted();
      data[rowCount][getTable().getRankColumn().getColumnIndex()] = ranking.getRank();
      data[rowCount][getTable().getRaceStatusColumn().getColumnIndex()] = ranking.getStatusUid();
      rowCount++;
    }

    importTableData(data);
  }

  public List<EventRanking> getEventRankings() {
    return eventRankings;
  }

  @Override
  protected void execInitPage() throws ProcessingException {
    // show
    getTable().getRaceStatusColumn().setInitialVisible(true);

    // hide
    getTable().getTimeBehindColumn().setInitialVisible(false);
    getTable().getPercentColumn().setInitialVisible(false);

    getTable().resetColumnVisibilities();
  }

  @Order(10.0)
  public class Table extends AbstractResultsTable {

    @Override
    protected void injectColumnsInternal(OrderedCollection<IColumn<?>> columnList) {
      columnList.addLast(new RankingColumn());
    }

    @Order(8.0)
    class RankingColumn extends AbstractStringColumn {

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

  }
}
