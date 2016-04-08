package com.rtiming.client.entry.startblock;

import java.util.Set;

import org.eclipse.scout.commons.CollectionUtility;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.common.ui.action.AbstractSeparatorMenu;
import com.rtiming.shared.Texts;
import com.rtiming.shared.entry.IRegistrationsOutlineService;
import com.rtiming.shared.entry.startblock.EventStartblockFormData;
import com.rtiming.shared.entry.startblock.IEventStartblockProcessService;
import com.rtiming.shared.entry.startblock.StartblockCodeType;

public class EventStartblocksTablePage extends AbstractPageWithTable<EventStartblocksTablePage.Table> implements IHelpEnabledPage {

  private final Long eventNr;

  public EventStartblocksTablePage(Long eventNr) {
    super();
    this.eventNr = eventNr;
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    importTableData(BEANS.get(IRegistrationsOutlineService.class).getEventStartblockTableData(getEventNr()));
  }

  public Long getEventNr() {
    return eventNr;
  }

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Startblocks");
  }

  @Order(10.0)
  public class Table extends AbstractTable {

    @Override
    protected String getConfiguredDefaultIconId() {
      return AbstractIcons.Star;
    }

    @Override
    protected Class<? extends IMenu> getConfiguredDefaultMenu() {
      return EditMenu.class;
    }

    public StartblockColumn getStartblockColumn() {
      return getColumnSet().getColumnByClass(StartblockColumn.class);
    }

    public CountColumn getCountColumn() {
      return getColumnSet().getColumnByClass(CountColumn.class);
    }

    public SortCodeColumn getSortCodeColumn() {
      return getColumnSet().getColumnByClass(SortCodeColumn.class);
    }

    public StartblockUidColumn getStartblockUidColumn() {
      return getColumnSet().getColumnByClass(StartblockUidColumn.class);
    }

    @Order(10.0)
    public class StartblockUidColumn extends AbstractSmartColumn<Long> {

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return StartblockCodeType.class;
      }

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Startblock");
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }
    }

    @Order(20.0)
    public class StartblockColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Startblock");
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }
    }

    @Order(30.0)
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

    @Order(40.0)
    public class CountColumn extends AbstractLongColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Count");
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }
    }

    @Order(10.0)
    public class NewMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return ScoutTexts.get("NewButton");
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(TableMenuType.EmptySpace);
      }

      @Override
      protected void execAction() throws ProcessingException {
        EventStartblockForm form = new EventStartblockForm();
        form.setEventNr(getEventNr());
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
      protected void execOwnerValueChanged(Object newOwnerValue) throws ProcessingException {
        setEnabled(getStartblockUidColumn().getSelectedValue() != null);
      }

      @Override
      protected void execAction() throws ProcessingException {
        EventStartblockForm form = new EventStartblockForm();
        form.setEventNr(getEventNr());
        form.getStartblockUidField().setValue(getStartblockUidColumn().getSelectedValue());
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
    public class DeleteMenu extends AbstractMenu {

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(TableMenuType.MultiSelection, TableMenuType.SingleSelection);
      }

      @Override
      protected void execOwnerValueChanged(Object newOwnerValue) throws ProcessingException {
        setEnabled(true);
        for (Long startblockUid : getStartblockUidColumn().getSelectedValues()) {
          if (startblockUid == null) {
            setEnabled(false);
          }
        }
      }

      @Override
      protected String getConfiguredText() {
        return ScoutTexts.get("DeleteMenu");
      }

      @Override
      protected void execAction() throws ProcessingException {
        if (MessageBoxes.showDeleteConfirmationMessage(Texts.get("Startblocks"), getStartblockColumn().getSelectedValues())) {
          for (Long startblockNr : getStartblockUidColumn().getSelectedValues()) {
            EventStartblockFormData formData = new EventStartblockFormData();
            formData.setEventNr(getEventNr());
            formData.getStartblockUid().setValue(startblockNr);
            BEANS.get(IEventStartblockProcessService.class).delete(formData);
          }
          reloadPage();
        }
      }
    }
  }
}
