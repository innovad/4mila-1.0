package com.rtiming.client.ecard.download;

import java.util.Set;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractDateColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.common.ui.action.AbstractSeparatorMenu;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Icons;
import com.rtiming.shared.Texts;
import com.rtiming.shared.ecard.download.IPunchProcessService;
import com.rtiming.shared.results.IResultsOutlineService;

public class PunchesTablePage extends AbstractPageWithTable<PunchesTablePage.Table> implements IHelpEnabledPage {

  private final Long m_punchSessionNr;

  public PunchesTablePage(Long punchSessionNr) {
    super();
    m_punchSessionNr = punchSessionNr;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("DownloadedData");
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    importTableData(BEANS.get(IResultsOutlineService.class).getPunchTableData(getPunchSessionNr()));
  }

  @FormData
  public Long getPunchSessionNr() {
    return m_punchSessionNr;
  }

  @Order(10.0)
  public class Table extends AbstractTable {

    @Override
    protected String getConfiguredDefaultIconId() {
      return Icons.TIMEFIELDTIME;
    }

    @Override
    protected Class<? extends IMenu> getConfiguredDefaultMenu() {
      return EditMenu.class;
    }

    public NumberColumn getNumberColumn() {
      return getColumnSet().getColumnByClass(NumberColumn.class);
    }

    public SortCodeColumn getSortCodeColumn() {
      return getColumnSet().getColumnByClass(SortCodeColumn.class);
    }

    public TimeColumn getTimeColumn() {
      return getColumnSet().getColumnByClass(TimeColumn.class);
    }

    @Order(10.0)
    public class SortCodeColumn extends AbstractLongColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("SortCode");
      }

      @Override
      protected int getConfiguredSortIndex() {
        return 1;
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }
    }

    @Order(20.0)
    public class NumberColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Number");
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }
    }

    @Order(30.0)
    public class TimeColumn extends AbstractDateColumn {

      @Override
      protected boolean getConfiguredHasTime() {
        return true;
      }

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Time");
      }

      @Override
      protected int getConfiguredWidth() {
        return 180;
      }

      @Override
      protected void execInitColumn() throws ProcessingException {
        setFormat(FMilaUtility.DEFAULT_TIME_FORMAT);
      }
    }

    @Order(10.0)
    public class NewMenu extends AbstractMenu {

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(TableMenuType.EmptySpace);
      }

      @Override
      protected String getConfiguredText() {
        return ScoutTexts.get("NewButton");
      }

      @Override
      protected void execAction() throws ProcessingException {
        PunchForm form = new PunchForm();
        form.getPunchSessionField().setValue(getPunchSessionNr());
        form.startNew();
        form.waitFor();
        if (form.isFormStored()) {
          reloadPage();
        }
      }
    }

    @Order(20.0)
    public class EditMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return ScoutTexts.get("EditMenu");
      }

      @Override
      protected void execAction() throws ProcessingException {
        PunchForm form = new PunchForm();
        form.getPunchSessionField().setValue(getPunchSessionNr());
        form.getSortCodeField().setValue(getTable().getSortCodeColumn().getSelectedValue());
        form.startModify();
        form.waitFor();
        if (form.isFormStored()) {
          reloadPage();
        }
      }
    }

    @Order(30.0)
    public class SeparatorMenu extends AbstractSeparatorMenu {
    }

    @Order(60.0)
    public class DeleteMenu extends AbstractMenu {

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(TableMenuType.MultiSelection, TableMenuType.SingleSelection);
      }

      @Override
      protected String getConfiguredText() {
        return ScoutTexts.get("DeleteMenu");
      }

      @Override
      protected void execAction() throws ProcessingException {
        if (MessageBoxes.showDeleteConfirmationMessage(Texts.get("DownloadedData"), getNumberColumn().getSelectedValues())) {
          BEANS.get(IPunchProcessService.class).delete(getPunchSessionNr(), getTable().getSortCodeColumn().getSelectedValues().toArray(new Long[0]));
          reloadPage();
        }
      }
    }
  }
}
