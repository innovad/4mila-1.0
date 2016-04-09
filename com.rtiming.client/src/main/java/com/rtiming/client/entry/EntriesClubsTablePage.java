package com.rtiming.client.entry;

import java.util.Set;

import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.ISearchForm;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.ClientSession;
import com.rtiming.client.common.EmptyWorkaroundTablePage;
import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.common.report.jrxml.StartListReport;
import com.rtiming.client.result.SingleEventSearchForm;
import com.rtiming.shared.Icons;
import com.rtiming.shared.Texts;
import com.rtiming.shared.entry.IRegistrationsOutlineService;
import com.rtiming.shared.results.SingleEventSearchFormData;

public class EntriesClubsTablePage extends AbstractPageWithTable<EntriesClubsTablePage.Table> implements IHelpEnabledPage {

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
    return Texts.get("Clubs");
  }

  @Override
  protected IPage execCreateChildPage(ITableRow row) throws ProcessingException {
    if (getTable().getClubNrColumn().getValue(row) != null) {
      return new EntriesTablePage(((SingleEventSearchFormData) getSearchFilter().getFormData()).getEvent().getValue(), ClientSession.get().getSessionClientNr(), null, null, null, getTable().getClubNrColumn().getValue(row));
    }
    else {
      return new EmptyWorkaroundTablePage();
    }
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    importTableData(BEANS.get(IRegistrationsOutlineService.class).getEntryClubTableData(filter));
  }

  @Order(10.0)
  public class Table extends AbstractTable {

    public EntriesColumn getEntriesColumn() {
      return getColumnSet().getColumnByClass(EntriesColumn.class);
    }

    public RunnersColumn getRunnersColumn() {
      return getColumnSet().getColumnByClass(RunnersColumn.class);
    }

    @Override
    protected String getConfiguredDefaultIconId() {
      return Icons.CLUB;
    }

    public ClubColumn getClubColumn() {
      return getColumnSet().getColumnByClass(ClubColumn.class);
    }

    public ClubNrColumn getClubNrColumn() {
      return getColumnSet().getColumnByClass(ClubNrColumn.class);
    }

    @Order(20.0)
    public class ClubNrColumn extends AbstractLongColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }
    }

    @Order(30.0)
    public class ClubColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Club");
      }

      @Override
      protected int getConfiguredSortIndex() {
        return 2;
      }

      @Override
      protected int getConfiguredWidth() {
        return 180;
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
        setVisible(getTable().getClubNrColumn().getValue(getSelectedRow()) != null);
      }

      @Override
      protected void execAction() throws ProcessingException {
        StartListReport report = new StartListReport(((SingleEventSearchForm) getSearchFormInternal()).getEventField().getValue(), null, null, getTable().getClubNrColumn().getSelectedValues());
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
      protected void execOwnerValueChanged(Object newOwnerValue) throws ProcessingException {
        setVisible(getTable().getClubNrColumn().getValue(getSelectedRow()) != null);
      }

      @Override
      protected void execAction() throws ProcessingException {
        StartListReport report = new StartListReport(((SingleEventSearchForm) getSearchFormInternal()).getEventField().getValue(), null, null, getTable().getClubNrColumn().getSelectedValues());
        report.printReport();
      }
    }

  }
}
