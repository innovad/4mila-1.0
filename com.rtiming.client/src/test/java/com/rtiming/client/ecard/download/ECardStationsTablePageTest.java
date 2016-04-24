package com.rtiming.client.ecard.download;

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
import com.rtiming.client.ecard.download.ECardStationsTablePage.Table.DeleteMenu;
import com.rtiming.client.ecard.download.ECardStationsTablePage.Table.ECardStationNrColumn;
import com.rtiming.client.ecard.download.ECardStationsTablePage.Table.EditMenu;
import com.rtiming.client.test.AbstractEntityTablePageTest;
import com.rtiming.client.test.data.ECardStationTestDataProvider;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class ECardStationsTablePageTest extends AbstractEntityTablePageTest<ECardStationsTablePage, Long> {

  private ECardStationTestDataProvider station;

  @Before
  public void before() throws ProcessingException {
    station = new ECardStationTestDataProvider();
    ECardStationTestDataProvider.assignToCurrentHost(station.getECardStationNr());
  }

  @Override
  protected ECardStationsTablePage getTablePage() {
    return new ECardStationsTablePage();
  }

  @After
  public void after() throws ProcessingException {
    if (station != null) {
      station.remove();
    }
  }

  @Override
  protected Class<? extends IMenu> getNewMenu() {
    return null;
  }

  @Override
  protected Class<? extends IForm> getForm() {
    return ECardStationForm.class;
  }

  @Override
  protected Class<? extends IMenu> getDeleteMenu() {
    return DeleteMenu.class;
  }

  @Override
  protected Class<? extends IColumn<Long>> getPrimaryKeyColumn() {
    return ECardStationNrColumn.class;
  }

  @Override
  protected Long getPrimaryKey() throws ProcessingException {
    return station.getECardStationNr();
  }

  @Override
  protected Class<? extends IMenu> getEditMenu() {
    return EditMenu.class;
  }

}
