package com.rtiming.server.settings.city;

import static org.junit.Assert.assertNull;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.dao.RtCity;
import com.rtiming.shared.dao.RtCityKey;
import com.rtiming.shared.settings.city.CityFormData;
import com.rtiming.shared.settings.city.CountryFormData;
import com.rtiming.shared.settings.user.LanguageCodeType;

/**
 * @author amo
 */
@RunWith(ServerTestRunner.class)
@RunWithSubject("admin")
@RunWithServerSession(ServerSession.class)
public class CityProcessServiceTest {

  private static CountryFormData country;
  private static CityFormData fullCity;
  private static CityFormData withoutZipCity;
  private static CityFormData withoutRegionCity;
  private static CityFormData withoutZipRegionCity;

  @BeforeClass
  public static void beforeClass() throws ProcessingException {
    CountryProcessService svcCountry = new CountryProcessService();
    country = new CountryFormData();
    country = svcCountry.find("Testcountry", "VV", "WWW");
    if (country.getCountryUid() == null) {
      country = svcCountry.create(country);
    }
    Assert.assertNotNull("Country created", country.getCountryUid());

    // create test cities
    long time = System.currentTimeMillis();
    withoutZipCity = new CityFormData();
    createCity(withoutZipCity, "CITYNAME" + time, null, "REGION" + time);

    withoutRegionCity = new CityFormData();
    createCity(withoutRegionCity, "CITYNAME" + time, "ZIP" + time, null);

    withoutZipRegionCity = new CityFormData();
    createCity(withoutZipRegionCity, "CITYNAME" + time, null, null);

    fullCity = new CityFormData();
    createCity(fullCity, "CITYNAME" + time, "ZIP" + time, "REGION" + time);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFind1() throws Exception {
    CityProcessService svc = new CityProcessService();
    svc.findCity(null, null, null, null, null, null);
  }

  @Test
  public void testFind2() throws Exception {
    CityProcessService svc = new CityProcessService();
    CityFormData result = svc.findCity(fullCity.getCity().getValue(), fullCity.getZip().getValue(), fullCity.getRegion().getValue(), null, null, country.getCountryCode().getValue());
    Assert.assertEquals("Key", fullCity.getCityNr(), result.getCityNr());
  }

  @Test
  public void testFind3() throws Exception {
    CityProcessService svc = new CityProcessService();
    CityFormData result = svc.findCity(withoutZipCity.getCity().getValue(), null, withoutZipCity.getRegion().getValue(), null, null, country.getCountryCode().getValue());
    Assert.assertEquals("Key", withoutZipCity.getCityNr(), result.getCityNr());
  }

  @Test
  public void testFind4() throws Exception {
    CityProcessService svc = new CityProcessService();
    CityFormData result = svc.findCity(withoutRegionCity.getCity().getValue(), withoutRegionCity.getZip().getValue(), null, null, null, country.getCountryCode().getValue());
    Assert.assertEquals("Key", withoutRegionCity.getCityNr(), result.getCityNr());
  }

  @Test
  public void testFind5() throws Exception {
    CityProcessService svc = new CityProcessService();
    CityFormData result = svc.findCity(withoutZipRegionCity.getCity().getValue(), null, null, null, null, country.getCountryCode().getValue());
    Assert.assertEquals("Key", withoutZipRegionCity.getCityNr(), result.getCityNr());
  }

  @Test
  public void testFind6() throws Exception {
    CityProcessService svc = new CityProcessService();
    CityFormData result = svc.findCity(fullCity.getCity().getValue(), fullCity.getZip().getValue(), fullCity.getRegion().getValue(), country.getCodeBox().getLanguage().getRows()[0].getTranslation(), null, null);
    Assert.assertEquals("Key", fullCity.getCityNr(), result.getCityNr());
  }

  @Test
  public void testFind7() throws Exception {
    CityProcessService svc = new CityProcessService();
    CityFormData result = svc.findCity(fullCity.getCity().getValue(), fullCity.getZip().getValue(), fullCity.getRegion().getValue(), country.getCodeBox().getLanguage().getRows()[0].getTranslation(), LanguageCodeType.English.ID, null);
    Assert.assertEquals("Key", fullCity.getCityNr(), result.getCityNr());
  }

  @Test
  public void testFind8() throws Exception {
    CityProcessService svc = new CityProcessService();
    CityFormData result = svc.findCity(fullCity.getCity().getValue(), fullCity.getZip().getValue(), fullCity.getRegion().getValue(), country.getCodeBox().getLanguage().getRows()[0].getTranslation(), LanguageCodeType.English.ID, country.getCountryCode().getValue());
    Assert.assertEquals("Key", fullCity.getCityNr(), result.getCityNr());
  }

  @Test
  public void testFind9() throws Exception {
    CityProcessService svc = new CityProcessService();
    CityFormData result = svc.findCity(fullCity.getCity().getValue(), fullCity.getZip().getValue(), fullCity.getRegion().getValue(), country.getCodeBox().getLanguage().getRows()[0].getTranslation(), LanguageCodeType.English.ID, country.getNation().getValue());
    Assert.assertEquals("Key", fullCity.getCityNr(), result.getCityNr());
  }

  @Test
  public void testFindNoResult1() throws Exception {
    CityProcessService svc = new CityProcessService();
    CityFormData result = svc.findCity(fullCity.getCity().getValue(), fullCity.getZip().getValue(), fullCity.getRegion().getValue(), country.getCodeBox().getLanguage().getRows()[0].getTranslation(), LanguageCodeType.English.ID, "XXXXXX");
    Assert.assertNull("Key", result.getCityNr());
  }

  @Test
  public void testFindNoResult2() throws Exception {
    CityProcessService svc = new CityProcessService();
    CityFormData result = svc.findCity(fullCity.getCity().getValue(), "8500", fullCity.getRegion().getValue(), null, LanguageCodeType.English.ID, "SUI");
    Assert.assertNull("Key", result.getCityNr());
  }

  private static void createCity(CityFormData city, String cityname, String zip, String region) throws ProcessingException {
    CityProcessService svc = new CityProcessService();
    city.getCity().setValue(cityname);
    city.getCountry().setValue(country.getCountryUid());
    city.getRegion().setValue(region);
    city.getZip().setValue(zip);
    city = svc.create(city);
    Assert.assertNotNull("City created", city.getCityNr());
  }

  @Test
  public void testDelete1() throws Exception {
    CityProcessService svc = new CityProcessService();
    CityFormData formData = new CityFormData();
    svc.delete(formData);
  }

  @Test
  public void testDelete2() throws Exception {
    CityProcessService svc = new CityProcessService();
    svc.delete(null);
  }

  @Test
  public void testDelete3() throws Exception {
    RtCity city = new RtCity();
    city.setId(RtCityKey.create((Long) null));
    JPA.persist(city);

    CityProcessService svc = new CityProcessService();
    CityFormData formData = new CityFormData();
    formData.setCityNr(city.getId().getId());
    svc.delete(formData);

    city = JPA.find(RtCity.class, city.getId());
    assertNull("deleted", city);
  }

  @Test
  public void testStore() throws Exception {
    RtCity city = new RtCity();
    city.setId(RtCityKey.create((Long) null));
    JPA.persist(city);

    CityProcessService svc = new CityProcessService();
    CityFormData formData = new CityFormData();
    formData.setCityNr(city.getId().getId());
    formData = svc.load(formData);
    formData.getCity().setValue("abcdef");
    svc.store(formData);

    formData = svc.load(formData);
    Assert.assertEquals("updated", "abcdef", formData.getCity().getValue());
    city = JPA.find(RtCity.class, city.getId());
    Assert.assertEquals("updated", "abcdef", city.getName());
    JPA.remove(city);
  }

  @Test
  public void testLoad() throws Exception {
    RtCity city = new RtCity();
    city.setId(RtCityKey.create((Long) null));
    city.setRegion("region1");
    JPA.persist(city);

    CityProcessService svc = new CityProcessService();
    CityFormData formData = new CityFormData();
    formData.setCityNr(city.getId().getId());
    formData = svc.load(formData);

    Assert.assertEquals("loaded", "region1", formData.getRegion().getValue());
    JPA.remove(city);
  }

}
