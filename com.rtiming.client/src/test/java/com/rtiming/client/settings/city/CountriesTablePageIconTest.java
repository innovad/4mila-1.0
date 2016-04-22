package com.rtiming.client.settings.city;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.settings.city.CountriesTablePage.Table.DeleteMenu;
import com.rtiming.client.test.data.CountryTestDataProvider;
import com.rtiming.shared.settings.city.CountryFormData;
import com.rtiming.shared.settings.city.ICountryProcessService;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestEnvironmentClientSession.class)
public class CountriesTablePageIconTest {

  private CountryTestDataProvider country;
  private boolean executed = false;

  @Test
  public void testDeleteManualCountry() throws Exception {
    country = new CountryTestDataProvider();

    CountriesTablePage page = new CountriesTablePage();
    page.nodeAddedNotify();
    page.loadChildren();

    Assert.assertTrue("Country must exist in Table", page.getTable().getRowCount() > 0);
    for (int i = 0; i < page.getTable().getRowCount(); i++) {
      if (CompareUtility.equals(country.getCountryUid().longValue(), page.getTable().getCountryUidColumn().getValue(i))) {
        page.getTable().selectRow(i);
        executed = page.getTable().runMenu(DeleteMenu.class);
      }
    }

    Assert.assertTrue("Manual Country, Delete Menu active", executed);
  }

  @Test
  public void testDeleteCountryWithIcon() throws Exception {
    ICountryProcessService countrySvc = BEANS.get(ICountryProcessService.class);
    CountryFormData countryFormData = countrySvc.find(null, "CH", null);
    if (countryFormData.getCountryUid() == null) {
      countryFormData.getNation().setValue("SUI");
      countryFormData = countrySvc.create(countryFormData);
    }

    CountryForm form = new CountryForm();
    form.setCountryUid(countryFormData.getCountryUid());
    form.startModify();

    form.getCountryCodeField().setValue("CH");
    form.doOk();

    CountriesTablePage page = new CountriesTablePage();
    page.nodeAddedNotify();
    page.loadChildren();

    Assert.assertTrue("Country must exist in Table", page.getTable().getRowCount() > 0);
    for (int i = 0; i < page.getTable().getRowCount(); i++) {
      if (CompareUtility.equals(countryFormData.getCountryUid().longValue(), page.getTable().getCountryUidColumn().getValue(i))) {
        page.getTable().selectRow(i);
        executed = page.getTable().runMenu(DeleteMenu.class);
      }
    }

    Assert.assertFalse("Default Country, Delete Menu inactive", executed);
  }

  @After
  public void after() throws ProcessingException {
    if (!executed && country != null) {
      country.remove();
    }
  }

}
