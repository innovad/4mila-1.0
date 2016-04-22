package com.rtiming.client.test.data;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.client.settings.city.CountryForm;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.settings.city.CountryFormData;
import com.rtiming.shared.settings.city.ICountryProcessService;

public class CountryTestDataProvider extends AbstractTestDataProvider<CountryForm> {

  private List<FieldValue> fieldValue;

  public CountryTestDataProvider() throws ProcessingException {
    this.fieldValue = new ArrayList<FieldValue>();
    fieldValue.add(new FieldValue(CountryForm.MainBox.CountryCodeField.class, "YY"));
    callInitializer();
  }

  public CountryTestDataProvider(List<FieldValue> fieldValue) throws ProcessingException {
    this.fieldValue = fieldValue;
    callInitializer();
  }

  private CountryForm country;

  @Override
  protected CountryForm createForm() throws ProcessingException {
    ICountryProcessService countrySvc = BEANS.get(ICountryProcessService.class);

    boolean countryDataFound = true;
    while (countryDataFound) {
      CountryFormData formData = countrySvc.find(null, "YY", null);
      if (formData.getCountryUid() != null) {
        formData.getCountryCode().setValue(null);
        countrySvc.store(formData);
      }
      else {
        countryDataFound = false;
      }
    }
    countryDataFound = true;
    while (countryDataFound) {
      CountryFormData formData = countrySvc.find(null, null, "Tes");
      if (formData.getCountryUid() != null) {
        formData.getNation().setValue(null);
        countrySvc.store(formData);
      }
      else {
        countryDataFound = false;
      }
    }

    // Country
    country = new CountryForm();
    country.startNew();
    FormTestUtility.fillFormFields(country, fieldValue.toArray(new FieldValue[fieldValue.size()]));
    country.doOk();

    return country;
  }

  @Override
  public void remove() throws ProcessingException {
    CountryFormData formData = new CountryFormData();
    formData.setCountryUid(country.getCountryUid());
    BEANS.get(ICountryProcessService.class).delete(formData);
  }

  public Long getCountryUid() throws ProcessingException {
    return getForm().getCountryUid();
  }

}
