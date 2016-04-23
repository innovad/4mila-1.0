package com.rtiming.client.settings.city;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.testing.client.ScoutClientAssert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.test.data.CountryTestDataProvider;
import com.rtiming.shared.settings.city.CountryFormData;
import com.rtiming.shared.settings.city.ICountryProcessService;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class CountryFormIconTest {

  @Test
  public void testNewCountry() throws Exception {
    CountryForm country = new CountryForm();
    country.startNew();

    ScoutClientAssert.assertEnabled(country.getCountryCodeField());
    ScoutClientAssert.assertEnabled(country.getNationField());

    country.doClose();
  }

  @Test
  public void testModifyCountryWithIcon() throws Exception {
    CountryFormData formData = BEANS.get(ICountryProcessService.class).find(null, "CH", null);
    if (formData.getCountryUid() != null) {
      formData.getCountryCode().setValue(null);
      BEANS.get(ICountryProcessService.class).store(formData);
    }

    CountryTestDataProvider country = new CountryTestDataProvider();
    CountryForm form = new CountryForm();
    form.setCountryUid(country.getCountryUid());
    form.startModify();

    ScoutClientAssert.assertEnabled(form.getCountryCodeField());
    ScoutClientAssert.assertEnabled(form.getNationField());

    form.getCountryCodeField().setValue("CH");
    form.doOk();

    form = new CountryForm();
    form.setCountryUid(country.getCountryUid());
    form.startModify();

    ScoutClientAssert.assertDisabled(form.getCountryCodeField());
    ScoutClientAssert.assertEnabled(form.getNationField());
    form.doClose();

    country.remove();
  }

}
