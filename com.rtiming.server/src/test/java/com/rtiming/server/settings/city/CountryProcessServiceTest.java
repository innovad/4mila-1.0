package com.rtiming.server.settings.city;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.dao.RtCountry;
import com.rtiming.shared.dao.RtCountryKey;
import com.rtiming.shared.dao.RtUc;
import com.rtiming.shared.dao.RtUcKey;
import com.rtiming.shared.dao.RtUcl;
import com.rtiming.shared.dao.RtUclKey;
import com.rtiming.shared.settings.city.CountryCodeType;
import com.rtiming.shared.settings.city.CountryFormData;

/**
 * @author amo
 */
@RunWith(ServerTestRunner.class)
@RunWithSubject("admin")
@RunWithServerSession(ServerSession.class)
public class CountryProcessServiceTest {

  private static String TEST_CODE = "EX";

  @Test
  public void testDelete1() throws Exception {
    CountryProcessService svc = new CountryProcessService();
    CountryFormData formData = new CountryFormData();
    svc.delete(formData);
  }

  @Test
  public void testDelete2() throws Exception {
    CountryProcessService svc = new CountryProcessService();
    svc.delete(null);
  }

  @Test
  public void testDelete3() throws Exception {
    RtCountry country = new RtCountry();
    country.setId(RtCountryKey.create((Long) null));
    JPA.persist(country);

    CountryProcessService svc = new CountryProcessService();
    CountryFormData formData = new CountryFormData();
    formData.setCountryUid(country.getId().getId());
    svc.delete(formData);

    country = JPA.find(RtCountry.class, country.getId());
    assertNull("deleted", country);
  }

  @Test
  public void testStore() throws Exception {
    RtCountry country = new RtCountry();
    country.setId(RtCountryKey.create((Long) null));
    JPA.persist(country);

    RtUc uc = new RtUc();
    uc.setId(RtUcKey.create(country.getId().getId()));
    uc.setActive(true);
    uc.setCodeType(CountryCodeType.ID);
    JPA.persist(uc);

    CountryProcessService svc = new CountryProcessService();
    CountryFormData formData = new CountryFormData();
    formData.setCountryUid(country.getId().getId());
    formData = svc.load(formData);
    svc.store(formData);

    JPA.remove(uc);
    JPA.remove(country);
  }

  @Test
  public void testStoreDuplicate() throws Exception {
// TODO MIG    
//    ServerJob job = new ServerJob("test", ServerSession.get()) {
//      @Override
//      protected IStatus runTransaction(IProgressMonitor monitor) throws Exception {
//        doTestStoreDuplicate();
//        return null;
//      }
//    };
//    IStatus result = job.runNow(null);
//    Assert.assertTrue("error message", result.getMessage().startsWith(TEXTS.get("DuplicateKeyMessage")));
  }

  private void doTestStoreDuplicate() throws ProcessingException {
    cleanupExistingCodes(TEST_CODE, null);

    RtCountry country = new RtCountry();
    country.setId(RtCountryKey.create((Long) null));
    country.setCountryCode(TEST_CODE);
    JPA.persist(country);

    RtUc uc = new RtUc();
    uc.setId(RtUcKey.create(country.getId().getId()));
    uc.setActive(true);
    uc.setCodeType(CountryCodeType.ID);
    JPA.persist(uc);

    CountryProcessService svc = new CountryProcessService();
    CountryFormData formData = new CountryFormData();
    formData = svc.prepareCreate(formData);
    formData.getCountryCode().setValue(TEST_CODE);
    for (int k = 0; k < formData.getCodeBox().getLanguage().getRowCount(); k++) {
      formData.getCodeBox().getLanguage().getRows()[0].setTranslation("blabla");
    }
    formData = svc.create(formData);
  }

  private void cleanupExistingCodes(String existingCode, String nation) throws ProcessingException {
    if (!StringUtility.isNullOrEmpty(existingCode)) {
      String queryString = "UPDATE RtCountry SET countryCode = NULL WHERE UPPER(countryCode) = :existingCode";
      FMilaQuery query = JPA.createQuery(queryString);
      query.setParameter("existingCode", StringUtility.uppercase(existingCode));
      query.executeUpdate();
    }
    if (!StringUtility.isNullOrEmpty(nation)) {
      String queryString = "UPDATE RtCountry SET nation = NULL WHERE UPPER(nation) = :nation";
      FMilaQuery query = JPA.createQuery(queryString);
      query.setParameter("nation", StringUtility.uppercase(nation));
      query.executeUpdate();
    }
  }

  @Test
  public void testFind1() throws Exception {
    CountryProcessService svc = new CountryProcessService();
    CountryFormData result = svc.find(null, null, null);
    assertNotNull("exists", result);
    assertNull("not found", result.getCountryUid());
    assertNull("not found", result.getCodeBox().getCodeUid().getValue());
  }

  @Test
  public void testFind2() throws Exception {
    doFindTest("Test", "BB", "NAT", "Test", "BB", "NAT", true);
  }

  @Test
  public void testFind3() throws Exception {
    doFindTest("Test", "BB", "NAT", "Test", "BC", "NAT", false);
  }

  @Test
  public void testFind4() throws Exception {
    doFindTest("Test", "BB", "NAT", "Test", "bb", "nat", true);
  }

  @Test
  public void testFind5() throws Exception {
    doFindTest("Test", "BB", "NAT", null, null, "nat", true);
  }

  @Test
  public void testFind6() throws Exception {
    doFindTest("not empty", null, "NaT", null, null, "nat", true);
  }

  @Test
  public void testFind7() throws Exception {
    doFindTest("not empty", "bb", null, null, " Bb ", null, true);
  }

  @Test
  public void testFind8() throws Exception {
    doFindTest("not empty", "bb", " ccc ", null, null, null, false);
  }

  private void doFindTest(String name, String code, String nation, String searchName, String searchCode, String searchNation, boolean found) throws ProcessingException {
    Long countryUid = createTestCountry(name, code, nation);
    CountryProcessService svc = new CountryProcessService();
    CountryFormData result = svc.find(searchName, searchCode, searchNation);
    assertNotNull("exists", result);
    if (found) {
      assertEquals("found", countryUid, result.getCountryUid());
      assertEquals("found", countryUid, result.getCodeBox().getCodeUid().getValue());
      assertEquals("name", StringUtility.uppercase(name), StringUtility.uppercase(result.getCodeBox().getLanguage().getRows()[0].getTranslation()));
      assertEquals("code", StringUtility.uppercase(code), StringUtility.uppercase(result.getCountryCode().getValue()));
      assertEquals("nation", StringUtility.uppercase(nation), StringUtility.uppercase(result.getNation().getValue()));
    }
    else {
      assertNull("not found", result.getCountryUid());
      assertNull("not found", result.getCodeBox().getCodeUid().getValue());
      assertEquals("name", searchName, result.getCodeBox().getLanguage().getRows()[0].getTranslation());
      assertEquals("code", StringUtility.uppercase(searchCode), StringUtility.uppercase(result.getCountryCode().getValue()));
      assertEquals("nation", StringUtility.uppercase(searchNation), StringUtility.uppercase(result.getNation().getValue()));
    }
    removeTestCountry(countryUid);
  }

  private Long createTestCountry(String name, String code, String nation) throws ProcessingException {
    cleanupExistingCodes(code, nation);

    RtCountry country = new RtCountry();
    country.setId(RtCountryKey.create((Long) null));
    country.setCountryCode(StringUtility.trim(StringUtility.uppercase(code)));
    country.setNation(StringUtility.trim(StringUtility.uppercase(nation)));
    JPA.persist(country);

    RtUc uc = new RtUc();
    uc.setId(RtUcKey.create(country.getId().getId()));
    uc.setActive(true);
    uc.setCodeType(CountryCodeType.ID);
    JPA.persist(uc);

    RtUcl ucl = new RtUcl();
    RtUclKey key = new RtUclKey();
    key.setClientNr(ServerSession.get().getSessionClientNr());
    key.setLanguageUid(ServerSession.get().getLanguageUid());
    key.setUcUid(uc.getId().getId());
    ucl.setId(key);
    ucl.setCodeName(name);
    JPA.persist(ucl);

    return uc.getId().getId();
  }

  private void removeTestCountry(Long id) throws ProcessingException {
    CountryProcessService svc = new CountryProcessService();
    CountryFormData formData = new CountryFormData();
    formData.setCountryUid(id);
    svc.delete(formData);
  }
}
