package com.rtiming.client.ecard;

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
import com.rtiming.client.ecard.ECardsTablePage.Table.ECardNrColumn;
import com.rtiming.client.ecard.ECardsTablePage.Table.EditMenu;
import com.rtiming.client.ecard.ECardsTablePage.Table.NewMenu;
import com.rtiming.client.test.AbstractEntityTablePageTest;
import com.rtiming.client.test.data.ECardTestDataProvider;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class ECardsTablePageTest extends AbstractEntityTablePageTest<ECardsTablePage, Long> {

  private ECardTestDataProvider ecard;

  @Before
  public void before() throws ProcessingException {
    ecard = new ECardTestDataProvider();
  }

  @Override
  protected ECardsTablePage getTablePage() {
    return new ECardsTablePage();
  }

  @Override
  protected Class<? extends IMenu> getNewMenu() {
    return NewMenu.class;
  }

  @Override
  protected Class<? extends IMenu> getDeleteMenu() {
    return null;
  }

  @Override
  protected Class<? extends IMenu> getEditMenu() {
    return EditMenu.class;
  }

  @Override
  protected Class<? extends IForm> getForm() {
    return ECardForm.class;
  }

  @Override
  protected Class<? extends IColumn<Long>> getPrimaryKeyColumn() {
    return ECardNrColumn.class;
  }

  @Override
  protected Long getPrimaryKey() throws ProcessingException {
    return ecard.getECardNr();
  }

  @After
  public void after() throws ProcessingException {
    ecard.remove();
  }

}
