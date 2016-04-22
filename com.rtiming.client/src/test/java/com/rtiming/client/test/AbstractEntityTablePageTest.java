package com.rtiming.client.test;

import org.eclipse.scout.rt.client.ui.action.menu.IMenu;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn;
import org.eclipse.scout.rt.client.ui.desktop.DesktopEvent;
import org.eclipse.scout.rt.client.ui.desktop.DesktopListener;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.client.ui.messagebox.IMessageBox;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.junit.Assert;
import org.junit.Test;

import com.rtiming.client.ClientSession;

public abstract class AbstractEntityTablePageTest<T, P> extends AbstractTablePageTest<AbstractPageWithTable<?>> {

  protected abstract Class<? extends IMenu> getNewMenu();

  protected abstract Class<? extends IMenu> getDeleteMenu();

  protected abstract Class<? extends IMenu> getEditMenu();

  protected abstract Class<? extends IForm> getForm();

  protected abstract Class<? extends IColumn<P>> getPrimaryKeyColumn();

  protected abstract P getPrimaryKey() throws ProcessingException;

  @Test
  public void testNewMenu() throws ProcessingException {
    if (getNewMenu() != null) {
      Runnable runnableAfterButtonClick = new Runnable() {
        @Override
        public void run() {
          IForm form = FMilaClientTestUtility.findLastAddedForm(getForm());
          try {
            form.doCancel();
          }
          catch (ProcessingException e) {
            e.printStackTrace();
          }
        }
      };

      FMilaClientTestUtility.runBlockingMenu(getTablePage().getTable(), getNewMenu(), runnableAfterButtonClick);
    }
  }

  @Test
  public void testEditMenu() throws ProcessingException {
    if (getEditMenu() != null) {
      Runnable runnableAfterButtonClick = new Runnable() {
        @Override
        public void run() {
          IForm form = FMilaClientTestUtility.findLastAddedForm(getForm());
          try {
            form.doCancel();
          }
          catch (ProcessingException e) {
            e.printStackTrace();
          }
        }
      };
      AbstractPageWithTable<?> tablePage = getTablePage();
      tablePage.loadChildren();
      for (ITableRow row : tablePage.getTable().getRows()) {
        for (IColumn<?> c : tablePage.getTable().getColumns()) {
          if (c.getClass().getSimpleName().equals(getPrimaryKeyColumn().getSimpleName())) {
            if (getPrimaryKey().equals(c.getValue(row))) {
              tablePage.getTable().deselectAllRows();
              tablePage.getTable().selectRow(row);
            }
          }
        }
      }

      FMilaClientTestUtility.runBlockingMenu(tablePage.getTable(), getEditMenu(), runnableAfterButtonClick);
    }
  }

  @Test
  public void testDeleteMenuNo() throws ProcessingException {
    if (getDeleteMenu() != null) {
      DesktopListener listener = new DesktopListener() {
        @Override
        public void desktopChanged(DesktopEvent e) {
          if (e.getType() == DesktopEvent.TYPE_MESSAGE_BOX_ADDED) {
            IMessageBox box = e.getMessageBox();
            box.getUIFacade().setResultFromUI(IMessageBox.NO_OPTION);
          }
        }
      };
      ClientSession.get().getDesktop().addDesktopListener(listener);

      AbstractPageWithTable<?> tablePage = getTablePage();
      tablePage.nodeAddedNotify();
      tablePage.loadChildren();
      selectRow(tablePage);

      boolean run = tablePage.getTable().runMenu(getDeleteMenu());
      Assert.assertTrue(run);

      ClientSession.get().getDesktop().removeDesktopListener(listener);
    }
  }

  @Test
  public void testDeleteMenuYes() throws ProcessingException {
    if (getDeleteMenu() != null) {
      DesktopListener listener = new DesktopListener() {
        @Override
        public void desktopChanged(DesktopEvent e) {
          if (e.getType() == DesktopEvent.TYPE_MESSAGE_BOX_ADDED) {
            IMessageBox box = e.getMessageBox();
            box.getUIFacade().setResultFromUI(IMessageBox.YES_OPTION);
          }
        }
      };
      ClientSession.get().getDesktop().addDesktopListener(listener);

      AbstractPageWithTable<?> tablePage = getTablePage();
      tablePage.nodeAddedNotify();
      tablePage.loadChildren();
      selectRow(tablePage);

      boolean run = tablePage.getTable().runMenu(getDeleteMenu());
      Assert.assertTrue(run);

      ClientSession.get().getDesktop().removeDesktopListener(listener);
    }
  }

  private void selectRow(AbstractPageWithTable<?> tablePage) throws ProcessingException {
    IColumn<?> column = null;
    for (IColumn<?> c : tablePage.getTable().getColumns()) {
      if (c.getClass().getSimpleName().equals(getPrimaryKeyColumn().getSimpleName())) {
        column = c;
      }
    }
    Assert.assertNotNull(column);
    for (ITableRow row : tablePage.getTable().getRows()) {
      if (CompareUtility.equals(tablePage.getTable().getCell(row, column).getValue(), getPrimaryKey())) {
        tablePage.getTable().selectRow(row);
      }
    }
    Assert.assertEquals(1, tablePage.getTable().getSelectedRowCount());
  }

}
