package com.rtiming.client.settings.account;

import java.util.Set;

import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.ValueFieldMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.shared.Icons;
import com.rtiming.shared.settings.ISettingsOutlineService;

public class AccountTablePage extends AbstractPageWithTable<AccountTablePage.Table> {

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Account");
  }

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Override
  protected IPage execCreateChildPage(ITableRow row) throws ProcessingException {
    return new AccountClientTablePage(getTable().getAccountNrColumn().getValue(row));
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    importTableData(BEANS.get(ISettingsOutlineService.class).getAccountTableData());
  }

  @Order(10.0)
  public class Table extends AbstractTable {

    @Override
    protected Class<? extends IMenu> getConfiguredDefaultMenu() {
      return EditMenu.class;
    }

    @Override
    protected String getConfiguredDefaultIconId() {
      return Icons.RUNNER;
    }

    public FirstNameColumn getFirstNameColumn() {
      return getColumnSet().getColumnByClass(FirstNameColumn.class);
    }

    public LastNameColumn getLastNameColumn() {
      return getColumnSet().getColumnByClass(LastNameColumn.class);
    }

    public EMailColumn getEMailColumn() {
      return getColumnSet().getColumnByClass(EMailColumn.class);
    }

    public GlobalClientNumberColumn getGlobalClientNumberColumn() {
      return getColumnSet().getColumnByClass(GlobalClientNumberColumn.class);
    }

    public LocalClientNumberColumn getLocalClientNumberColumn() {
      return getColumnSet().getColumnByClass(LocalClientNumberColumn.class);
    }

    public AccountNrColumn getAccountNrColumn() {
      return getColumnSet().getColumnByClass(AccountNrColumn.class);
    }

    public AccountNameColumn getAccountNameColumn() {
      return getColumnSet().getColumnByClass(AccountNameColumn.class);
    }

    @Order(10.0)
    public class AccountNrColumn extends AbstractLongColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Number");
      }

      @Override
      protected int getConfiguredWidth() {
        return 70;
      }
    }

    @Order(20.0)
    public class AccountNameColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Account");
      }

      @Override
      protected int getConfiguredSortIndex() {
        return 1;
      }

      @Override
      protected int getConfiguredWidth() {
        return 96;
      }
    }

    @Order(30.0)
    public class FirstNameColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("FirstName");
      }

      @Override
      protected int getConfiguredWidth() {
        return 170;
      }
    }

    @Order(40.0)
    public class LastNameColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("LastName");
      }

      @Override
      protected int getConfiguredWidth() {
        return 170;
      }
    }

    @Order(50.0)
    public class EMailColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("EMail");
      }

      @Override
      protected int getConfiguredWidth() {
        return 166;
      }
    }

    @Order(60.0)
    public class GlobalClientNumberColumn extends AbstractLongColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Global");
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }
    }

    @Order(70.0)
    public class LocalClientNumberColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Local");
      }

      @Override
      protected int getConfiguredHorizontalAlignment() {
        return 1;
      }
    }

    @Order(10.0)
    public class NewMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return TEXTS.get("NewButton");
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(ValueFieldMenuType.Null);
      }

      @Override
      protected void execAction() throws ProcessingException {
        AccountForm form = new AccountForm();
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
        AccountForm form = new AccountForm();
        form.setAccountNr(getAccountNrColumn().getSelectedValue());
        form.startModify();
        form.waitFor();
        if (form.isFormStored()) {
          reloadPage();
        }
      }
    }

  }
}
