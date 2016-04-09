package com.rtiming.client.common.database;

import java.util.Set;

import org.eclipse.scout.rt.client.services.common.clipboard.IClipboardService;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.ClientSession;
import com.rtiming.client.FMilaClientUtility;
import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.shared.common.database.DatabaseInfoBean;
import com.rtiming.shared.common.database.IDatabaseService;
import com.rtiming.shared.common.database.jpa.JPAStyle;

public class DatabaseInfoTablePage extends AbstractPageWithTable<DatabaseInfoTablePage.Table> implements IHelpEnabledPage {

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Info");
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    DatabaseInfoBean bean = BEANS.get(IDatabaseService.class).getDatabaseInfo();

    Object[][] table = new Object[6][2];
    int k = 0;

    table[k][0] = "Id";
    table[k][1] = bean.getClientSessionNr() + " / " + ClientSession.get().getSessionClientNr();

    k++;
    table[k][0] = "Type";
    table[k][1] = bean.getStyle().toString();

    k++;
    table[k][0] = "JDBC URL";
    table[k][1] = bean.getJdbcUrl();

    k++;
    table[k][0] = "Data Directory";
    table[k][1] = bean.getDataDirectory();

    k++;
    table[k][0] = "File Store Path";
    table[k][1] = bean.getFileStoreRootPath();

    k++;
    table[k][0] = "Last Backup";
    if (bean.getLastBackup() != null) {
      table[k][1] = DateUtility.formatDateTime(bean.getLastBackup());
    }
    else {
      table[k][1] = TEXTS.get("NoBackup");
    }

    importTableData(table);
  }

  @Order(10.0)
  public class Table extends AbstractTable {

    public ValueColumn getValueColumn() {
      return getColumnSet().getColumnByClass(ValueColumn.class);
    }

    public TypeColumn getTypeColumn() {
      return getColumnSet().getColumnByClass(TypeColumn.class);
    }

    @Order(10.0)
    public class TypeColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Type");
      }

      @Override
      protected int getConfiguredWidth() {
        return 200;
      }
    }

    @Order(20.0)
    public class ValueColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Value");
      }

      @Override
      protected int getConfiguredWidth() {
        return 500;
      }
    }

    @Order(10.0)
    public class OpenDatabaseConsoleMenu extends AbstractMenu {

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(TableMenuType.EmptySpace);
      }

      @Override
      protected String getConfiguredText() {
        return TEXTS.get("OpenDatabaseConsole");
      }

      @Override
      protected void execInitAction() throws ProcessingException {
        DatabaseInfoBean bean = BEANS.get(IDatabaseService.class).getDatabaseInfo();
        boolean webConsoleExists = JPAStyle.H2_EMBEDDED.equals(bean.getStyle());
        setVisible(webConsoleExists);
      }

      @Override
      protected void execAction() throws ProcessingException {
        DatabaseInfoBean bean = BEANS.get(IDatabaseService.class).getDatabaseInfo();
        FMilaClientUtility.showOkMessage(null, null, TEXTS.get("PasteJdbcUrlIntoWebConsoleMessage"));
        BEANS.get(IClipboardService.class).setTextContents(bean.getJdbcUrl());
        FMilaClientUtility.openDocument("http://localhost:8085");
      }

    }

    @Order(20.0)
    public class CopyMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return TEXTS.get("Copy");
      }

      @Override
      protected void execAction() throws ProcessingException {
        BEANS.get(IClipboardService.class).setTextContents(getTable().getValueColumn().getSelectedDisplayText());
      }
    }
  }
}
