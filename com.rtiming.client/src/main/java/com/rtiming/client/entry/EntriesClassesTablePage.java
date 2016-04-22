package com.rtiming.client.entry;

import java.util.Set;

import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
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
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.ClientSession;
import com.rtiming.client.common.EmptyWorkaroundTablePage;
import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.common.report.jrxml.StartListReport;
import com.rtiming.client.common.ui.table.AbstractSummaryTable;
import com.rtiming.client.common.ui.table.IIgnoreSummaryColumn;
import com.rtiming.client.result.SingleEventSearchForm;
import com.rtiming.shared.Icons;
import com.rtiming.shared.Texts;
import com.rtiming.shared.entry.IRegistrationsOutlineService;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.results.SingleEventSearchFormData;

public class EntriesClassesTablePage extends AbstractPageWithTable<EntriesClassesTablePage.Table> implements IHelpEnabledPage {

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
    if (getTable().getClassUidColumn().getValue(row) != null) {
      return new EntriesTablePage(((SingleEventSearchFormData) getSearchFilter().getFormData()).getEvent().getValue(), ClientSession.get().getSessionClientNr(), null, getTable().getClassUidColumn().getValue(row), null, null);
    }
    else {
      return new EmptyWorkaroundTablePage();
    }
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    importTableData(BEANS.get(IRegistrationsOutlineService.class).getEntryClassTableData(filter));
  }

  @Order(10.0)
  public class Table extends AbstractSummaryTable {

    public EntriesColumn getEntriesColumn() {
      return getColumnSet().getColumnByClass(EntriesColumn.class);
    }

    public RunnersColumn getRunnersColumn() {
      return getColumnSet().getColumnByClass(RunnersColumn.class);
    }

    @Override
    protected String getConfiguredDefaultIconId() {
      return Icons.RUNNER;
    }

    public SummaryOrderColumn getSummaryOrderColumn() {
      return getColumnSet().getColumnByClass(SummaryOrderColumn.class);
    }

    public ClazzColumn getClazzColumn() {
      return getColumnSet().getColumnByClass(ClazzColumn.class);
    }

    public ClassUidColumn getClassUidColumn() {
      return getColumnSet().getColumnByClass(ClassUidColumn.class);
    }

    @Override
    protected Class<? extends AbstractBooleanColumn> getConfiguredSummaryOrderColumn() {
      return SummaryOrderColumn.class;
    }

    @Override
    protected Class<? extends AbstractStringColumn> getConfiguredSummaryTotalColumn() {
      return ClazzColumn.class;
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
    public class ClassUidColumn extends AbstractSmartColumn<Long> {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return ClassCodeType.class;

      }

      @Override
      protected int getConfiguredWidth() {
        return 180;
      }
    }

    @Order(30.0)
    public class ClazzColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Class");
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

    @Order(35.0)
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

    @Order(40.0)
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

    @Order(50.0)
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
        StartListReport report = new StartListReport(((SingleEventSearchForm) getSearchFormInternal()).getEventField().getValue(), getTable().getClassUidColumn().getSelectedValues(), null, null);
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
        StartListReport report = new StartListReport(((SingleEventSearchForm) getSearchFormInternal()).getEventField().getValue(), getTable().getClassUidColumn().getSelectedValues(), null, null);
        report.printReport();
      }
    }

  }
}
