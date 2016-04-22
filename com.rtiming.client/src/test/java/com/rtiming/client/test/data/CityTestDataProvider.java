package com.rtiming.client.test.data;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.client.settings.city.CityForm;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.settings.city.CityFormData;
import com.rtiming.shared.settings.city.ICityProcessService;

public class CityTestDataProvider extends AbstractTestDataProvider<CityForm> {

  public CityTestDataProvider() throws ProcessingException {
    callInitializer();
  }

  private CountryTestDataProvider country;

  @Override
  protected CityForm createForm() throws ProcessingException {
    // Country
    country = new CountryTestDataProvider();

    // City
    CityForm city = new CityForm();
    city.startNew();
    FormTestUtility.fillFormFields(city, new FieldValue(CityForm.MainBox.CountryField.class, country.getCountryUid()));
    city.doOk();

    return city;
  }

  @Override
  public void remove() throws ProcessingException {
    CityFormData formData = new CityFormData();
    formData.setCityNr(getForm().getCityNr());
    BEANS.get(ICityProcessService.class).delete(formData);
    country.remove();
  }

  public Long getCityNr() throws ProcessingException {
    return getForm().getCityNr();
  }

}
