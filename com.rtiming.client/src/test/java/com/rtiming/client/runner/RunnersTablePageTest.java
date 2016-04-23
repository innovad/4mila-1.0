package com.rtiming.client.runner;

import org.eclipse.scout.rt.client.ui.action.menu.IMenu;
import org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn;
import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.runner.RunnersTablePage.Table.DeleteMenu;
import com.rtiming.client.runner.RunnersTablePage.Table.EditMenu;
import com.rtiming.client.runner.RunnersTablePage.Table.NewMenu;
import com.rtiming.client.runner.RunnersTablePage.Table.RunnerNrColumn;
import com.rtiming.client.test.AbstractEntityTablePageTest;
import com.rtiming.client.test.data.RunnerTestDataProvider;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class RunnersTablePageTest extends AbstractEntityTablePageTest<RunnersTablePage, Long> {

  private RunnerTestDataProvider runner;

  @Before
  public void before() throws ProcessingException {
    runner = new RunnerTestDataProvider();
  }

  @Override
  protected RunnersTablePage getTablePage() {
    return new RunnersTablePage();
  }

  @Override
  protected Class<? extends IMenu> getNewMenu() {
    return NewMenu.class;
  }

  @Override
  protected Class<? extends IForm> getForm() {
    return RunnerForm.class;
  }

  @Override
  protected Class<? extends IMenu> getDeleteMenu() {
    return DeleteMenu.class;
  }

  @Override
  protected Class<? extends IColumn<Long>> getPrimaryKeyColumn() {
    return RunnerNrColumn.class;
  }

  @Override
  protected Long getPrimaryKey() throws ProcessingException {
    return runner.getRunnerNr();
  }

  @Override
  protected Class<? extends IMenu> getEditMenu() {
    return EditMenu.class;
  }

  @After
  public void after() throws ProcessingException {
    if (runner != null) {
      runner.remove();
    }
  }

}
