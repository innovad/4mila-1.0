package com.rtiming.client.result;

import java.util.List;
import java.util.Set;

import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.HeaderCell;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBooleanColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.ISearchForm;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.ClientSession;
import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.common.report.jrxml.ResultReport;
import com.rtiming.client.common.ui.table.AbstractSummaryTable;
import com.rtiming.client.common.ui.table.IIgnoreSummaryColumn;
import com.rtiming.shared.Icons;
import com.rtiming.shared.Texts;
import com.rtiming.shared.event.course.ClassTypeCodeType;
import com.rtiming.shared.results.IResultsOutlineService;
import com.rtiming.shared.results.ResultClazzRowData;

public class ResultsClassesTablePage extends AbstractPageWithTable<ResultsClassesTablePage.Table> implements IHelpEnabledPage {

  private final Long clientNr;

  public ResultsClassesTablePage(Long clientNr) {
    this.clientNr = clientNr;
  }

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Override
  protected Class<? extends ISearchForm> getConfiguredSearchForm() {
    return SingleEventSearchForm.class;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Classes");
  }

  @Override
  protected IPage<?> execCreateChildPage(ITableRow row) throws ProcessingException {
    Long eventNr = ((SingleEventSearchForm) getSearchFormInternal()).getEventField().getValue();
    return new ResultTypeNodePage(ClientSession.get().getSessionClientNr(), eventNr, getTable().getParentUidColumn().getValue(row), getTable().getClassUidColumn().getValue(row), getTable().getClassTypeColumn().getValue(row), null, null);
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    List<ResultClazzRowData> list = BEANS.get(IResultsOutlineService.class).getResultClassTableData(clientNr, filter);

    Object[][] table = new Object[list.size()][getTable().getColumnCount()];
    int k = 0;
    for (ResultClazzRowData row : list) {
      table[k] = new Object[getTable().getColumnCount()];
      table[k][getTable().getSummaryOrderColumn().getColumnIndex()] = 0;
      table[k][getTable().getParentUidColumn().getColumnIndex()] = row.getParentUid();
      table[k][getTable().getClassUidColumn().getColumnIndex()] = row.getClazzUid();
      table[k][getTable().getOutlineColumn().getColumnIndex()] = row.getOutline();
      table[k][getTable().getParentColumn().getColumnIndex()] = row.getParent();
      table[k][getTable().getClazzColumn().getColumnIndex()] = row.getClazz();
      table[k][getTable().getClassTypeColumn().getColumnIndex()] = row.getClazzTypeUid();
      table[k][getTable().getSortCodeColumn().getColumnIndex()] = row.getSortCode();
      table[k][getTable().getEntriesColumn().getColumnIndex()] = row.getEntries();
      table[k][getTable().getRunnersColumn().getColumnIndex()] = row.getRunners();
      table[k][getTable().getProcessedColumn().getColumnIndex()] = row.getProcessed();
      table[k][getTable().getMissingColumn().getColumnIndex()] = row.getMissing();
      k++;
    }
    importTableData(table);
  }

  @Override
  protected void execPopulateTable() throws ProcessingException {
    super.execPopulateTable();
    updateColumnVisibility();
  }

  private void updateColumnVisibility() throws ProcessingException {
    boolean atLeastOneRelay = false;
    // reset columns to standard state
    getTable().getParentColumn().setDisplayable(false);
    ((HeaderCell) getTable().getClazzColumn().getHeaderCell()).setText(getTable().getClazzColumn().getConfiguredHeaderText());
    for (int i = 0; i < getTable().getRowCount(); i++) {
      boolean isRelayRow = CompareUtility.notEquals(getTable().getClassUidColumn().getValue(i), getTable().getParentUidColumn().getValue(i));
      if (getTable().getClassUidColumn().getValue(i) != null && getTable().getParentUidColumn().getValue(i) != null && isRelayRow) {
        // one or more relay classes exist => show relay parent column
        getTable().getParentColumn().setDisplayable(true);
        getTable().getParentColumn().setVisible(true);
        ((HeaderCell) getTable().getClazzColumn().getHeaderCell()).setText(TEXTS.get("Leg"));
        atLeastOneRelay = true;
      }
    }
    if (atLeastOneRelay) {
      for (int i = 0; i < getTable().getRowCount(); i++) {
        boolean isIndividualRow = getTable().getParentUidColumn().getValue(i) == null;
        if (isIndividualRow) {
          getTable().getClazzColumn().setValue(i, null);
        }
      }
    }
  }

  @Order(10.0)
  public class Table extends AbstractSummaryTable {

    @Override
    protected String getConfiguredDefaultIconId() {
      return Icons.CLAZZ;
    }

    public RunnersColumn getRunnersColumn() {
      return getColumnSet().getColumnByClass(RunnersColumn.class);
    }

    public ProcessedColumn getProcessedColumn() {
      return getColumnSet().getColumnByClass(ProcessedColumn.class);
    }

    public MissingColumn getMissingColumn() {
      return getColumnSet().getColumnByClass(MissingColumn.class);
    }

    public SummaryOrderColumn getSummaryOrderColumn() {
      return getColumnSet().getColumnByClass(SummaryOrderColumn.class);
    }

    public ClazzColumn getClazzColumn() {
      return getColumnSet().getColumnByClass(ClazzColumn.class);
    }

    public ParentColumn getParentColumn() {
      return getColumnSet().getColumnByClass(ParentColumn.class);
    }

    public EntriesColumn getEntriesColumn() {
      return getColumnSet().getColumnByClass(EntriesColumn.class);
    }

    public OutlineColumn getOutlineColumn() {
      return getColumnSet().getColumnByClass(OutlineColumn.class);
    }

    public SortCodeColumn getSortCodeColumn() {
      return getColumnSet().getColumnByClass(SortCodeColumn.class);
    }

    public ClassTypeColumn getClassTypeColumn() {
      return getColumnSet().getColumnByClass(ClassTypeColumn.class);
    }

    public ClassUidColumn getClassUidColumn() {
      return getColumnSet().getColumnByClass(ClassUidColumn.class);
    }

    public ParentUidColumn getParentUidColumn() {
      return getColumnSet().getColumnByClass(ParentUidColumn.class);
    }

    @Order(10.0)
    public class SummaryOrderColumn extends AbstractBooleanColumn {

      @Override
      protected boolean getConfiguredAlwaysIncludeSortAtBegin() {
        return true;
      }

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }

      @Override
      protected int getConfiguredSortIndex() {
        return 2;
      }
    }

    @Order(20.0)
    public class ParentUidColumn extends AbstractSmartColumn<Long> {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }

    }

    @Order(30.0)
    public class ClassUidColumn extends AbstractSmartColumn<Long> {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }

    }

    @Order(40.0)
    public class OutlineColumn extends AbstractStringColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }

      @Override
      protected boolean getConfiguredSummary() {
        return true;
      }
    }

    @Order(50.0)
    public class ParentColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Class");
      }

      @Override
      protected int getConfiguredSortIndex() {
        return 3;
      }

      @Override
      protected int getConfiguredWidth() {
        return 180;
      }

    }

    @Order(60.0)
    public class ClazzColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Class");
      }

      @Override
      protected int getConfiguredSortIndex() {
        return 4;
      }

      @Override
      protected int getConfiguredWidth() {
        return 180;
      }
    }

    @Order(70.0)
    public class ClassTypeColumn extends AbstractSmartColumn<Long> {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("ClassType");
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return ClassTypeCodeType.class;
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }

    }

    @Order(80.0)
    public class SortCodeColumn extends AbstractLongColumn implements IIgnoreSummaryColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("SortCode");
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }

      @Override
      protected int getConfiguredSortIndex() {
        return 1;
      }

    }

    @Order(90.0)
    public class EntriesColumn extends AbstractLongColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Entries");
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }
    }

    @Order(100.0)
    public class RunnersColumn extends AbstractLongColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Runners");
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }
    }

    @Order(110.0)
    public class ProcessedColumn extends AbstractLongColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Processed");
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }
    }

    @Order(120.0)
    public class MissingColumn extends AbstractLongColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Missing");
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
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
      protected void execOwnerValueChanged(Object newOwnerValue) throws ProcessingException {
        setVisible(getTable().getClassUidColumn().getValue(getSelectedRow()) != null);
      }

      @Override
      protected void execAction() throws ProcessingException {
        ResultReport report = new ResultReport(((SingleEventSearchForm) getSearchFormInternal()).getEventField().getValue(), getTable().getClassUidColumn().getSelectedValues(), null, null);
        report.openReport();
      }
    }

    @Order(20.0)
    public class PrintMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return Texts.get("PrintMenu");
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(TableMenuType.MultiSelection, TableMenuType.SingleSelection);
      }

      @Override
      protected void execOwnerValueChanged(Object newOwnerValue) throws ProcessingException {
        setVisible(getTable().getClassUidColumn().getValue(getSelectedRow()) != null);
      }

      @Override
      protected void execAction() throws ProcessingException {
        ResultReport report = new ResultReport(((SingleEventSearchForm) getSearchFormInternal()).getEventField().getValue(), getTable().getClassUidColumn().getSelectedValues(), null, null);
        report.printReport();
      }
    }

    @Override
    protected Class<? extends AbstractBooleanColumn> getConfiguredSummaryOrderColumn() {
      return SummaryOrderColumn.class;
    }

    @Override
    protected Class<? extends AbstractStringColumn> getConfiguredSummaryTotalColumn() {
      return ClazzColumn.class;
    }
  }
}
