package com.rtiming.client.club;

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
import com.rtiming.client.club.ClubsTablePage.Table.ClubNrColumn;
import com.rtiming.client.club.ClubsTablePage.Table.EditMenu;
import com.rtiming.client.club.ClubsTablePage.Table.NewMenu;
import com.rtiming.client.test.AbstractEntityTablePageTest;
import com.rtiming.client.test.data.ClubTestDataProvider;

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(ClientSession.class)
public class ClubsTablePageTest extends AbstractEntityTablePageTest<ClubsTablePage, Long> {

  private ClubTestDataProvider club;

  @Before
  public void before() throws ProcessingException {
    club = new ClubTestDataProvider();
  }

  @Override
  protected ClubsTablePage getTablePage() {
    return new ClubsTablePage();
  }

  @Override
  protected Class<? extends IMenu> getNewMenu() {
    return NewMenu.class;
  }

  @Override
  protected Class<? extends IForm> getForm() {
    return ClubForm.class;
  }

  @Override
  protected Class<? extends IMenu> getDeleteMenu() {
    return null;
  }

  @Override
  protected Class<? extends IColumn<Long>> getPrimaryKeyColumn() {
    return ClubNrColumn.class;
  }

  @Override
  protected Long getPrimaryKey() throws ProcessingException {
    return club.getClubNr();
  }

  @Override
  protected Class<? extends IMenu> getEditMenu() {
    return EditMenu.class;
  }

  @After
  public void after() throws Exception {
    club.remove();
  }

}
