package com.rtiming.client.map;

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

import com.rtiming.client.TestClientSession;
import com.rtiming.client.map.MapsTablePage.Table.DeleteMenu;
import com.rtiming.client.map.MapsTablePage.Table.EditMenu;
import com.rtiming.client.map.MapsTablePage.Table.MapNrColumn;
import com.rtiming.client.map.MapsTablePage.Table.NewMenu;
import com.rtiming.client.test.AbstractEntityTablePageTest;
import com.rtiming.client.test.data.MapTestDataProvider;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class MapsTablePageTest extends AbstractEntityTablePageTest<MapsTablePage, Long> {

  private MapTestDataProvider map;

  @Before
  public void before() throws ProcessingException {
    map = new MapTestDataProvider();
  }

  @Override
  protected MapsTablePage getTablePage() {
    return new MapsTablePage();
  }

  @Override
  protected Class<? extends IMenu> getNewMenu() {
    return NewMenu.class;
  }

  @Override
  protected Class<? extends IMenu> getDeleteMenu() {
    return DeleteMenu.class;
  }

  @Override
  protected Class<? extends IMenu> getEditMenu() {
    return EditMenu.class;
  }

  @Override
  protected Class<? extends IForm> getForm() {
    return MapForm.class;
  }

  @Override
  protected Class<? extends IColumn<Long>> getPrimaryKeyColumn() {
    return MapNrColumn.class;
  }

  @Override
  protected Long getPrimaryKey() throws ProcessingException {
    return map.getMapNr();
  }

  @After
  public void after() throws ProcessingException {
    map.remove();
  }

}
