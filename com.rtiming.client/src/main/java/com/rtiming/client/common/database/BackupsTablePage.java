package com.rtiming.client.common.database;

import java.util.Set;

import org.eclipse.scout.commons.CollectionUtility;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.IDesktop;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.messagebox.IMessageBox;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBox;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.ClientSession;
import com.rtiming.client.FMilaClientUtility;
import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.common.ui.action.AbstractSeparatorMenu;
import com.rtiming.client.common.ui.columns.AbstractDateTimeWithSecondsColumn;
import com.rtiming.client.common.ui.desktop.Desktop;
import com.rtiming.shared.common.database.IDatabaseService;

public class BackupsTablePage extends AbstractPageWithTable<BackupsTablePage.Table> implements IHelpEnabledPage {

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Backups");
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    importTableData(BEANS.get(IDatabaseService.class).getBackupsTableData());
  }

  @Override
  protected void execInitPage() throws ProcessingException {
    BackupSettingsForm form = new BackupSettingsForm();
    setDetailForm(form);
    form.startNew();
  }

  @Override
  protected void execPageDataLoaded() throws ProcessingException {
    IDesktop desktop = ClientSession.get().getDesktop();
    if (desktop instanceof Desktop) {
      Desktop fmilaDesktop = (Desktop) desktop;
      fmilaDesktop.updateApplicationWindowTitle();
    }
  }

  @Order(10.0)
  public class Table extends AbstractTable {

    @Override
    protected String getConfiguredDefaultIconId() {
      return AbstractIcons.Folder;
    }

    public FolderColumn getFolderColumn() {
      return getColumnSet().getColumnByClass(FolderColumn.class);
    }

    public FileSizeColumn getFileSizeColumn() {
      return getColumnSet().getColumnByClass(FileSizeColumn.class);
    }

    public DateColumn getDateColumn() {
      return getColumnSet().getColumnByClass(DateColumn.class);
    }

    @Order(10.0)
    public class FolderColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Folder");
      }

      @Override
      protected int getConfiguredWidth() {
        return 250;
      }
    }

    @Order(20.0)
    public class DateColumn extends AbstractDateTimeWithSecondsColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Date");
      }

      @Override
      protected boolean getConfiguredSortAscending() {
        return false;
      }

      @Override
      protected int getConfiguredSortIndex() {
        return 1;
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }
    }

    @Order(30.0)
    public class FileSizeColumn extends AbstractLongColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("FileSize");
      }

      @Override
      protected int getConfiguredWidth() {
        return 90;
      }

// TODO MIG      
//      @Override
//      protected String getConfiguredFormat() {
//        return "#.## kB";
//      }

    }

    @Order(10.0)
    public class CreateNewBackupMenu extends AbstractMenu {

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(TableMenuType.EmptySpace);
      }

      @Override
      protected String getConfiguredText() {
        return TEXTS.get("CreateNewBackup");
      }

      @Override
      protected void execAction() throws ProcessingException {
        BEANS.get(IDatabaseService.class).createBackup();
        reloadPage();
      }
    }

    @Order(20.0)
    public class RestoreBackupMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return TEXTS.get("RestoreBackup");
      }

      @Override
      protected void execAction() throws ProcessingException {
        IMessageBox mbox = FMilaClientUtility.createMessageBox(TEXTS.get("Backup"), null, TEXTS.get("RestoreDatabaseMessage"), TEXTS.get("RestoreDatabase"), null, ScoutTexts.get("CancelButton"));
        int askRestore = mbox.show();

        if (askRestore == MessageBox.YES_OPTION) {
          String backupFileName = getTable().getFolderColumn().getSelectedValue();
          BEANS.get(IDatabaseService.class).restoreBackup(backupFileName);
          // TODO MIG ClientSession.get().stopSession(IApplication.EXIT_RESTART);
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
        return TEXTS.get("DeleteMenu");
      }

      @Override
      protected void execAction() throws ProcessingException {
        if (MessageBoxes.showDeleteConfirmationMessage(TEXTS.get("Backups"), getFolderColumn().getSelectedValues())) {
          BEANS.get(IDatabaseService.class).deleteBackup(getFolderColumn().getSelectedValues().toArray(new String[0]));
          reloadPage();
        }
      }

    }
  }

}
