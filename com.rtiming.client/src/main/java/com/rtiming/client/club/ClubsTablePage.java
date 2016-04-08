package com.rtiming.client.club;

import java.util.List;
import java.util.Set;

import org.eclipse.scout.commons.CollectionUtility;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.ISearchForm;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.ClientSession;
import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.common.ui.action.AbstractSeparatorMenu;
import com.rtiming.client.settings.addinfo.AbstractTableWithAdditionalColumns;
import com.rtiming.shared.Icons;
import com.rtiming.shared.Texts;
import com.rtiming.shared.club.ClubRowData;
import com.rtiming.shared.club.ClubsSearchFormData;
import com.rtiming.shared.club.IClubProcessService;
import com.rtiming.shared.common.EntityCodeType;
import com.rtiming.shared.common.database.sql.AdditionalInformationValueBean;
import com.rtiming.shared.dao.RtClubKey;
import com.rtiming.shared.entry.SharedCache;
import com.rtiming.shared.event.IEventsOutlineService;

public class ClubsTablePage extends AbstractPageWithTable<ClubsTablePage.Table> implements IHelpEnabledPage {

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Override
  protected Class<? extends ISearchForm> getConfiguredSearchForm() {
    return ClubsSearchForm.class;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Clubs");
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    List<ClubRowData> list = BEANS.get(IEventsOutlineService.class).getClubTableData((ClubsSearchFormData) getSearchFilter().getFormData());

    Object[][] table = new Object[list.size()][getTable().getColumnCount()];

    int k = 0;
    for (ClubRowData row : list) {
      table[k] = new Object[getTable().getColumnCount()];
      table[k][getTable().getClubNrColumn().getColumnIndex()] = row.getClubNr();
      table[k][getTable().getContactPersonColumn().getColumnIndex()] = row.getContactPerson();
      table[k][getTable().getExtKeyColumn().getColumnIndex()] = row.getExtKey();
      table[k][getTable().getNameColumn().getColumnIndex()] = row.getName();
      table[k][getTable().getShortcutColumn().getColumnIndex()] = row.getShortcut();

      // additional values
      List<AdditionalInformationValueBean> ais = SharedCache.getAddInfoForEntity(EntityCodeType.ClubCode.ID, ClientSession.get().getSessionClientNr());
      for (int a = 0; a < ais.size(); a++) {
        table[k][getTable().getColumnCount() - ais.size() + a] = row.getAdditionalValues()[a];
      }
      k++;
    }
    importTableData(table);
  }

  @Order(10.0)
  public class Table extends AbstractTableWithAdditionalColumns {

    @Override
    public Long[] getEntityUids() throws ProcessingException {
      return new Long[]{EntityCodeType.ClubCode.ID};
    }

    @Override
    protected Class<? extends IMenu> getConfiguredDefaultMenu() {
      return EditMenu.class;
    }

    @Override
    protected String getConfiguredDefaultIconId() {
      return Icons.CLUB;
    }

    public ExtKeyColumn getExtKeyColumn() {
      return getColumnSet().getColumnByClass(ExtKeyColumn.class);
    }

    public ClubNrColumn getClubNrColumn() {
      return getColumnSet().getColumnByClass(ClubNrColumn.class);
    }

    public ContactPersonColumn getContactPersonColumn() {
      return getColumnSet().getColumnByClass(ContactPersonColumn.class);
    }

    public NameColumn getNameColumn() {
      return getColumnSet().getColumnByClass(NameColumn.class);
    }

    public ShortcutColumn getShortcutColumn() {
      return getColumnSet().getColumnByClass(ShortcutColumn.class);
    }

    @Order(10.0)
    public class ClubNrColumn extends AbstractLongColumn {

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
    public class ShortcutColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Shortcut");
      }

      @Override
      protected int getConfiguredWidth() {
        return 80;
      }
    }

    @Order(30.0)
    public class NameColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return ScoutTexts.get("Name");
      }

      @Override
      protected int getConfiguredWidth() {
        return 250;
      }
    }

    @Order(40.0)
    public class ExtKeyColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Number");
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

      @Override
      protected int getConfiguredWidth() {
        return 80;
      }
    }

    @Order(50.0)
    public class ContactPersonColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("ContactPerson");
      }

      @Override
      protected int getConfiguredWidth() {
        return 250;
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
        ClubForm form = new ClubForm();
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
        ClubForm form = new ClubForm();
        form.setClubNr(getClubNrColumn().getSelectedValue());
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
      protected String getConfiguredText() {
        return ScoutTexts.get("DeleteMenu");
      }

      @Override
      protected void execAction() throws ProcessingException {
        if (MessageBoxes.showDeleteConfirmationMessage(Texts.get("Clubs"), getNameColumn().getSelectedValues())) {
          for (Long clubNr : getClubNrColumn().getSelectedValues()) {
            RtClubKey key = RtClubKey.create(clubNr);
            BEANS.get(IClubProcessService.class).delete(key);
          }
          reloadPage();
        }
      }
    }
  }
}
