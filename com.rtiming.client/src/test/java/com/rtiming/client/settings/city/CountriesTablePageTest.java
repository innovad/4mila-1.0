package com.rtiming.client.settings.city;

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
import com.rtiming.client.settings.city.CountriesTablePage.Table.CountryUidColumn;
import com.rtiming.client.settings.city.CountriesTablePage.Table.DeleteMenu;
import com.rtiming.client.settings.city.CountriesTablePage.Table.EditMenu;
import com.rtiming.client.settings.city.CountriesTablePage.Table.NewMenu;
import com.rtiming.client.test.AbstractEntityTablePageTest;
import com.rtiming.client.test.data.CountryTestDataProvider;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class CountriesTablePageTest extends AbstractEntityTablePageTest<CountriesTablePage, Long> {

  private CountryTestDataProvider country;

  @Before
  public void before() throws ProcessingException {
    country = new CountryTestDataProvider();
  }

  @Override
  protected CountriesTablePage getTablePage() {
    return new CountriesTablePage();
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
    return CountryForm.class;
  }

  @Override
  protected Class<? extends IColumn<Long>> getPrimaryKeyColumn() {
    return CountryUidColumn.class;
  }

  @Override
  protected Long getPrimaryKey() throws ProcessingException {
    return country.getCountryUid();
  }

  @After
  public void after() throws ProcessingException {
    country.remove();
  }

}
