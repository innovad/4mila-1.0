package com.rtiming.client.ranking;

import java.util.Set;

import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.ClientSession;
import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.common.ui.action.AbstractSeparatorMenu;
import com.rtiming.shared.Icons;
import com.rtiming.shared.dao.RtRankingEventKey;
import com.rtiming.shared.entry.startlist.BibNoOrderCodeType;
import com.rtiming.shared.event.IEventsOutlineService;
import com.rtiming.shared.ranking.IRankingEventProcessService;
import com.rtiming.shared.ranking.RankingFormatCodeType;

public class RankingEventsTablePage extends AbstractPageWithTable<RankingEventsTablePage.Table> implements IHelpEnabledPage {

  private final Long rankingNr;

  public RankingEventsTablePage(Long rankingNr) {
    super();
    this.rankingNr = rankingNr;
  }

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Events");
  }

  @Override
  protected IPage execCreateChildPage(ITableRow row) throws ProcessingException {
    return new RankingNodePage(rankingNr, getTable().getEventNrColumn().getValue(row));
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    importTableData(BEANS.get(IEventsOutlineService.class).getRankingEventTableData(rankingNr));
  }

  @Order(10.0)
  public class Table extends AbstractTable {

    @Override
    protected Class<? extends IMenu> getConfiguredDefaultMenu() {
      return EditMenu.class;
    }

    @Override
    protected String getConfiguredDefaultIconId() {
      return Icons.EVENT;
    }

    public FormatColumn getFormatColumn() {
      return getColumnSet().getColumnByClass(FormatColumn.class);
    }

    public SortingColumn getSortingColumn() {
      return getColumnSet().getColumnByClass(SortingColumn.class);
    }

    public SortCodeColumn getSortCodeColumn() {
      return getColumnSet().getColumnByClass(SortCodeColumn.class);
    }

    public FormulaColumn getFormulaColumn() {
      return getColumnSet().getColumnByClass(FormulaColumn.class);
    }

    public EventColumn getEventColumn() {
      return getColumnSet().getColumnByClass(EventColumn.class);
    }

    public EventNrColumn getEventNrColumn() {
      return getColumnSet().getColumnByClass(EventNrColumn.class);
    }

    @Order(10.0)
    public class EventNrColumn extends AbstractLongColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }

      @Override
      protected boolean getConfiguredPrimaryKey() {
        return true;
      }
    }

    @Order(20.0)
    public class EventColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Event");
      }

      @Override
      protected int getConfiguredWidth() {
        return 250;
      }
    }

    @Order(30.0)
    public class FormatColumn extends AbstractSmartColumn<Long> {

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return RankingFormatCodeType.class;
      }

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Format");
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }
    }

    @Order(40.0)
    public class SortingColumn extends AbstractSmartColumn<Long> {

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return BibNoOrderCodeType.class;
      }

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Sorting");
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }
    }

    @Order(50.0)
    public class SortCodeColumn extends AbstractLongColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("SortCode");
      }

      @Override
      protected int getConfiguredSortIndex() {
        return 1;
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }
    }

    @Order(60.0)
    public class FormulaColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Formula");
      }

      @Override
      protected boolean getConfiguredTextWrap() {
        return true;
      }

      @Override
      protected int getConfiguredWidth() {
        return 250;
      }
    }

    @Order(10.0)
    public class AddMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return TEXTS.get("AddMenu");
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(TableMenuType.EmptySpace);
      }

      @Override
      protected void execAction() throws ProcessingException {
        RankingEventForm form = new RankingEventForm();
        RtRankingEventKey key = new RtRankingEventKey();
        key.setRankingNr(rankingNr);
        key.setClientNr(ClientSession.get().getSessionClientNr());
        form.setKey(key);
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
        return TEXTS.get("EditMenu");
      }

      @Override
      protected void execAction() throws ProcessingException {
        RankingEventForm form = new RankingEventForm();
        RtRankingEventKey key = new RtRankingEventKey();
        key.setRankingNr(rankingNr);
        key.setEventNr(getEventNrColumn().getSelectedValue());
        key.setClientNr(ClientSession.get().getSessionClientNr());
        form.setKey(key);
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

    @Order(40.0)
    public class RemoveMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return TEXTS.get("Remove");
      }

      @Override
      protected void execAction() throws ProcessingException {
        if (MessageBoxes.showDeleteConfirmationMessage(TEXTS.get("Ranking"), getEventColumn().getSelectedValues())) {
          for (Long eventNr : getEventNrColumn().getSelectedValues()) {
            RtRankingEventKey key = new RtRankingEventKey();
            key.setEventNr(eventNr);
            key.setClientNr(ClientSession.get().getSessionClientNr());
            key.setRankingNr(rankingNr);
            BEANS.get(IRankingEventProcessService.class).delete(key);
          }
          reloadPage();
        }
      }
    }
  }
}
