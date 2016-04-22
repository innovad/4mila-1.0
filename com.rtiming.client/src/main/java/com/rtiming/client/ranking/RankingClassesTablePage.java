package com.rtiming.client.ranking;

import java.util.Set;

import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.common.report.jrxml.RankingReport;
import com.rtiming.shared.Icons;
import com.rtiming.shared.Texts;
import com.rtiming.shared.event.IEventsOutlineService;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.ranking.AbstractFormulaCode.RankingType;

public class RankingClassesTablePage extends AbstractPageWithTable<RankingClassesTablePage.Table> implements IHelpEnabledPage {

  private final Long rankingNr;
  private final Long eventNr;
  private final RankingType type;

  public RankingClassesTablePage(Long rankingNr, Long eventNr, RankingType type) {
    super();
    this.rankingNr = rankingNr;
    this.eventNr = eventNr;
    this.type = type;
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Classes");
  }

  @Override
  protected void execInitPage() throws ProcessingException {
    if (RankingType.EVENT.equals(type)) {
      getCellForUpdate().setText(TEXTS.get("Event"));
    }
    else if (RankingType.SUMMARY.equals(type)) {
      getCellForUpdate().setText(TEXTS.get("Total"));
    }
  }

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Override
  protected IPage<?> execCreateChildPage(ITableRow row) throws ProcessingException {
    Long classUid = getTable().getClazzUidColumn().getValue(row);
    if (RankingType.EVENT.equals(type)) {
      return new RankingEventResultsTablePage(rankingNr, eventNr, classUid);
    }
    else if (RankingType.SUMMARY.equals(type)) {
      return new RankingSummaryResultsTablePage(rankingNr, eventNr, classUid);
    }
    return null;
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    importTableData(BEANS.get(IEventsOutlineService.class).getRankingClassesTableData(rankingNr));
  }

  @Order(10.0)
  public class Table extends AbstractTable {

    @Override
    protected String getConfiguredDefaultIconId() {
      return Icons.CLAZZ;
    }

    public ClazzUidColumn getClazzUidColumn() {
      return getColumnSet().getColumnByClass(ClazzUidColumn.class);
    }

    @Order(10.0)
    public class ClazzUidColumn extends AbstractSmartColumn<Long> {

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return ClassCodeType.class;
      }

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Class");
      }

      @Override
      protected int getConfiguredWidth() {
        return 200;
      }
    }

    @Order(10.0)
    public class CreatePDFMenu extends AbstractMenu {

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(TableMenuType.MultiSelection, TableMenuType.SingleSelection);
      }

      @Override
      protected String getConfiguredText() {
        return Texts.get("CreatePDFMenu");
      }

      @Override
      protected void execAction() throws ProcessingException {
        RankingReport report = new RankingReport(eventNr, rankingNr, type, getClazzUidColumn().getSelectedValues());
        report.openReport();
      }
    }

    @Order(20.0)
    public class PrintMenu extends AbstractMenu {

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(TableMenuType.MultiSelection, TableMenuType.SingleSelection);
      }

      @Override
      protected String getConfiguredText() {
        return Texts.get("PrintMenu");
      }

      @Override
      protected void execAction() throws ProcessingException {
        RankingReport report = new RankingReport(eventNr, rankingNr, type, getClazzUidColumn().getSelectedValues());
        report.printReport();
      }
    }

  }
}
