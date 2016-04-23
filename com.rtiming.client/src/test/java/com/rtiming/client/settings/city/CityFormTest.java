package com.rtiming.client.settings.city;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.settings.city.CityForm.MainBox.CountryField;
import com.rtiming.client.test.AbstractFormTest;
import com.rtiming.client.test.data.CountryTestDataProvider;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.settings.IDefaultProcessService;
import com.rtiming.shared.settings.city.CityFormData;
import com.rtiming.shared.settings.city.CountryFormData;
import com.rtiming.shared.settings.city.ICityProcessService;
import com.rtiming.shared.settings.city.ICountryProcessService;
import com.rtiming.shared.settings.user.LanguageCodeType;

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(ClientSession.class)
public class CityFormTest extends AbstractFormTest<CityForm> {

  private static CountryTestDataProvider defaultCountry;

  @BeforeClass
  public static void beforeClass() throws ProcessingException {
    defaultCountry = new CountryTestDataProvider();
  }

  @AfterClass
  public static void afterClass() throws ProcessingException {
    defaultCountry.remove();
  }

  @Override
  protected CityForm getStartedForm() throws ProcessingException {
    CityForm form = new CityForm();
    form.startNew();
    return form;
  }

  @Override
  protected CityForm getModifyForm() throws ProcessingException {
    CityForm form = new CityForm();
    form.setCityNr(getForm().getCityNr());
    form.startModify();
    return form;
  }

  @Override
  protected List<FieldValue> getFixedValues() throws ProcessingException {
    List<FieldValue> list = new ArrayList<FieldValue>();
    list.add(new FieldValue(CountryField.class, defaultCountry.getCountryUid()));
    return list;
  }

  @Override
  public void cleanup() throws ProcessingException {
    CityFormData city = new CityFormData();
    city.setCityNr(getForm().getCityNr());
    BEANS.get(ICityProcessService.class).delete(city);
  }

  @Test
  public void testDefaultValue() throws ProcessingException {
    CityForm city = new CityForm();
    city.startNew();

    Long defaultCountryUid = BEANS.get(IDefaultProcessService.class).getDefaultCountryUid();
    Assert.assertEquals(city.getCountryField().getValue(), defaultCountryUid);
  }

  @Test(expected = ProcessingException.class)
  public void testFindCountryCodeCountryName() throws ProcessingException {
    BEANS.get(ICityProcessService.class).findCity("Bulunkul", null, null, null, null, null);
  }

  @Test
  public void testFindCountryNameCountryLanguage1() throws ProcessingException {
    BEANS.get(ICityProcessService.class).findCity("Bulunkul", null, null, "Name", null, null);
  }

  @Test(expected = ProcessingException.class)
  public void testFindCountryNameCountryLanguage2() throws ProcessingException {
    BEANS.get(ICityProcessService.class).findCity("Bulunkul", null, null, null, LanguageCodeType.German.ID, null);
  }

  @Test
  public void testFindNoResult() throws ProcessingException {
    CountryFormData country = new CountryFormData();
    country.setCountryUid(defaultCountry.getCountryUid());
    country = BEANS.get(ICountryProcessService.class).load(country);
    Assert.assertNotNull(country.getCountryCode().getValue());

    CityFormData formData = BEANS.get(ICityProcessService.class).findCity("Bulunkul", "1234567890", "XYZ", null, null, country.getCountryCode().getValue());
    Assert.assertNull(formData.getCityNr());
    Assert.assertEquals("Bulunkul", formData.getCity().getValue());
    Assert.assertEquals("1234567890", formData.getZip().getValue());
    Assert.assertEquals("XYZ", formData.getRegion().getValue());
    Assert.assertEquals(country.getCountryUid(), formData.getCountry().getValue());
  }

  @Test
  public void testFindNoResultWithNewCountryByCountryCode() throws ProcessingException {
    CountryFormData country = BEANS.get(ICountryProcessService.class).find(null, "ZZ", null);
    Assert.assertNull(country.getCountryUid());

    CityFormData formData = BEANS.get(ICityProcessService.class).findCity("Bulunkul", "1234567890", "XYZ", null, null, "ZZ");
    Assert.assertNull(formData.getCityNr());
    Assert.assertEquals("Bulunkul", formData.getCity().getValue());
    Assert.assertEquals("1234567890", formData.getZip().getValue());
    Assert.assertEquals("XYZ", formData.getRegion().getValue());
    Assert.assertNotNull(formData.getCountry().getValue());

    country = BEANS.get(ICountryProcessService.class).find(null, "ZZ", null);
    Assert.assertNotNull(country.getCountryUid());
    Assert.assertEquals("ZZ", country.getCountryCode().getValue());
    BEANS.get(ICountryProcessService.class).delete(country);
  }

  @Test
  public void testFindNoResultWithNewCountryByCountryName() throws ProcessingException {
    CountryFormData country = BEANS.get(ICountryProcessService.class).find("NEW_COUNTRY", null, null);
    Assert.assertNull(country.getCountryUid());

    CityFormData formData = BEANS.get(ICityProcessService.class).findCity("Bulunkul", "1234567890", "XYZ", "NEW_COUNTRY", LanguageCodeType.German.ID, null);
    Assert.assertNull(formData.getCityNr());
    Assert.assertEquals("Bulunkul", formData.getCity().getValue());
    Assert.assertEquals("1234567890", formData.getZip().getValue());
    Assert.assertEquals("XYZ", formData.getRegion().getValue());
    Assert.assertNotNull(formData.getCountry().getValue());

    country = BEANS.get(ICountryProcessService.class).find("NEW_COUNTRY", null, null);
    Assert.assertNotNull(country.getCountryUid());
    Assert.assertEquals("NEW_COUNTRY", country.getCodeBox().getLanguage().getRows()[0].getTranslation());
    BEANS.get(ICountryProcessService.class).delete(country);
  }

  @Test
  public void testFindNoResultWithNewCountryByCountryNameAndCountryCode() throws ProcessingException {
    CountryFormData country = BEANS.get(ICountryProcessService.class).find("NEW_COUNTRY", "NN", null);
    Assert.assertNull(country.getCountryUid());

    CityFormData formData = BEANS.get(ICityProcessService.class).findCity("Bulunkul", "1234567890", "XYZ", "NEW_COUNTRY", LanguageCodeType.German.ID, "NN");
    Assert.assertNull(formData.getCityNr());
    Assert.assertEquals("Bulunkul", formData.getCity().getValue());
    Assert.assertEquals("1234567890", formData.getZip().getValue());
    Assert.assertEquals("XYZ", formData.getRegion().getValue());
    Assert.assertNotNull(formData.getCountry().getValue());

    country = BEANS.get(ICountryProcessService.class).find("NEW_COUNTRY", "NN", null);
    Assert.assertNotNull(country.getCountryUid());
    Assert.assertEquals("NEW_COUNTRY", country.getCodeBox().getLanguage().getRows()[0].getTranslation());
    Assert.assertEquals("NN", country.getCountryCode().getValue());
    BEANS.get(ICountryProcessService.class).delete(country);
  }

  @Test
  public void testFindWithZipRegion() throws ProcessingException {
    getForm().getCityField().setValue("Bulunkul");
    getForm().getRegionField().setValue("XYZ");
    getForm().getZipField().setValue("1234567890");
    getForm().getCountryField().setValue(defaultCountry.getCountryUid());
    getForm().doOk();

    CountryFormData country = new CountryFormData();
    country.setCountryUid(getForm().getCountryField().getValue());
    country = BEANS.get(ICountryProcessService.class).load(country);
    Assert.assertNotNull(country.getCountryCode().getValue());

    CityFormData formData = BEANS.get(ICityProcessService.class).findCity("Bulunkul", "1234567890", "XYZ", null, null, country.getCountryCode().getValue());
    Assert.assertEquals(getForm().getCityNr(), formData.getCityNr());
    Assert.assertEquals(getForm().getCityField().getValue(), formData.getCity().getValue());

    formData = BEANS.get(ICityProcessService.class).findCity("Bulunkul", "1", "XYZ", null, null, country.getCountryCode().getValue());
    Assert.assertNull(formData.getCityNr());

    formData = BEANS.get(ICityProcessService.class).findCity("Bulunkul", "1234567890", "ABC", null, null, country.getCountryCode().getValue());
    Assert.assertNull(formData.getCityNr());
  }

  @Test
  public void testFindWithCountryCode() throws ProcessingException {
    getForm().getCityField().setValue("Bulunkul");
    getForm().getCountryField().setValue(defaultCountry.getCountryUid());
    getForm().doOk();

    CountryFormData country = new CountryFormData();
    country.setCountryUid(getForm().getCountryField().getValue());
    country = BEANS.get(ICountryProcessService.class).load(country);
    Assert.assertNotNull(country.getCountryCode().getValue());

    CityFormData formData = BEANS.get(ICityProcessService.class).findCity("Bulunkul", null, null, null, null, country.getCountryCode().getValue());
    Assert.assertEquals(getForm().getCityNr(), formData.getCityNr());
    Assert.assertEquals(getForm().getCityField().getValue(), formData.getCity().getValue());
  }

  @Test
  public void testFindWithCountryName() throws ProcessingException {
    getForm().getCityField().setValue("Bulunkul");
    getForm().getCountryField().setValue(defaultCountry.getCountryUid());
    getForm().doOk();

    CountryFormData country = new CountryFormData();
    country.setCountryUid(getForm().getCountryField().getValue());
    country = BEANS.get(ICountryProcessService.class).load(country);
    Assert.assertNotNull(country.getCountryCode().getValue());

    Assert.assertTrue(country.getCodeBox().getLanguage().getRowCount() > 0);
    String countryName = country.getCodeBox().getLanguage().getRows()[0].getTranslation();
    Long languageUid = country.getCodeBox().getLanguage().getRows()[0].getLanguage();
    Assert.assertNotNull(countryName);
    Assert.assertNotNull(languageUid);

    CityFormData formData = BEANS.get(ICityProcessService.class).findCity("Bulunkul", null, null, countryName, languageUid, null);
    Assert.assertEquals(getForm().getCityNr(), formData.getCityNr());
    Assert.assertEquals(getForm().getCityField().getValue(), formData.getCity().getValue());

    BEANS.get(ICityProcessService.class).delete(formData);
  }

}
