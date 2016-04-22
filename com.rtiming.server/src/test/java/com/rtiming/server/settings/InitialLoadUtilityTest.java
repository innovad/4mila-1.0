package com.rtiming.server.settings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.BeanMetaData;
import org.eclipse.scout.rt.platform.IBean;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.common.code.ICodeService;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.eclipse.scout.rt.testing.shared.TestingUtility;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.dao.RtUc;
import com.rtiming.shared.dao.RtUcKey;
import com.rtiming.shared.dao.RtUcl;
import com.rtiming.shared.dao.RtUclKey;
import com.rtiming.shared.entry.startblock.StartblockCodeType;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.runner.SexCodeType;
import com.rtiming.shared.settings.CodeFormData;
import com.rtiming.shared.settings.ICodeProcessService;
import com.rtiming.shared.settings.currency.CurrencyFormData;
import com.rtiming.shared.settings.currency.ICurrencyProcessService;
import com.rtiming.shared.settings.user.LanguageCodeType;

/**
 * @author amo
 */
@RunWith(ServerTestRunner.class)
@RunWithSubject("admin")
@RunWithServerSession(ServerSession.class)
public class InitialLoadUtilityTest {

  @SuppressWarnings("rawtypes")
  private List<IBean<?>> registration = null;
  private final int numberOfCurrencies = 159;

  @Test
  public void testCurrenciesCreate() throws Exception {
    ICurrencyProcessService currencyService = Mockito.mock(ICurrencyProcessService.class);
    ICodeProcessService codeService = Mockito.mock(ICodeProcessService.class);
    registration = TestingUtility.registerBeans(new BeanMetaData(ICurrencyProcessService.class, currencyService), new BeanMetaData(ICodeService.class, codeService));

    CodeFormData codeFormData = new CodeFormData();
    CurrencyFormData currencyFormData = new CurrencyFormData();
    Mockito.when(codeService.find(Mockito.anyString(), Mockito.anyLong())).thenReturn(codeFormData);
    Mockito.when(currencyService.prepareCreate(Mockito.any(CurrencyFormData.class))).thenReturn(currencyFormData);

    InitialLoadUtility.createCurrencies();

    Mockito.verify(currencyService, Mockito.atLeast(numberOfCurrencies)).prepareCreate(Mockito.any(CurrencyFormData.class));
    Mockito.verify(currencyService, Mockito.atLeast(numberOfCurrencies)).create(currencyFormData);
    Mockito.verify(currencyService, Mockito.atLeast(0)).store(currencyFormData);
  }

  @Test
  public void testCurrenciesUpdate() throws Exception {
    ICurrencyProcessService currencyService = Mockito.mock(ICurrencyProcessService.class);
    ICodeProcessService codeService = Mockito.mock(ICodeProcessService.class);
    registration = TestingUtility.registerBeans(new BeanMetaData(ICurrencyProcessService.class, currencyService), new BeanMetaData(ICodeService.class, codeService));

    CodeFormData codeFormData = new CodeFormData();
    codeFormData.setCodeUid(1L);
    CurrencyFormData currencyFormData = new CurrencyFormData();
    Mockito.when(codeService.find(Mockito.anyString(), Mockito.anyLong())).thenReturn(codeFormData);
    Mockito.when(currencyService.load(Mockito.any(CurrencyFormData.class))).thenReturn(currencyFormData);

    InitialLoadUtility.createCurrencies();

    Mockito.verify(currencyService, Mockito.atLeast(0)).prepareCreate(currencyFormData);
    Mockito.verify(currencyService, Mockito.atLeast(0)).create(currencyFormData);
    Mockito.verify(currencyService, Mockito.atLeast(numberOfCurrencies)).store(currencyFormData);
  }

  @Test
  public void testInsertMissingTranslations1() throws Exception {
    long insertCount = InitialLoadUtility.insertMissingTranslationsWithEnglish();
    assertEquals("0 codes", 0, insertCount);
  }

  @Test
  public void testInsertMissingTranslations2() throws Exception {
    RtUc uc = new RtUc();
    uc.setCodeType(StartblockCodeType.ID);
    uc.setActive(true);
    uc.setId(RtUcKey.create((Long) null));
    JPA.merge(uc);

    RtUclKey keyUcl = new RtUclKey();
    keyUcl.setClientNr(ServerSession.get().getSessionClientNr());
    keyUcl.setLanguageUid(LanguageCodeType.English.ID);
    keyUcl.setUcUid(uc.getId().getId());
    RtUcl ucl = new RtUcl();
    ucl.setId(keyUcl);
    ucl.setCodeName("ENGLISH");
    JPA.persist(ucl);

    long insertCount = InitialLoadUtility.insertMissingTranslationsWithEnglish();
    assertEquals("1 code", BEANS.get(LanguageCodeType.class).getCodes().size() - 1, insertCount);

    insertCount = InitialLoadUtility.insertMissingTranslationsWithEnglish();
    assertEquals("0 codes", 0, insertCount);
  }

  @Test
  public void testCreateClass() throws Exception {
    String clazz = "CLASS" + System.currentTimeMillis();
    InitialLoadUtility.createClass(clazz, clazz, clazz, SexCodeType.ManCode.ID, 0L, 98L);

    CodeProcessService svc = new CodeProcessService();
    CodeFormData find = svc.find(clazz, ClassCodeType.ID);
    assertNotNull(find);
    assertEquals(clazz, find.getMainBox().getShortcut().getValue());
  }

  @After
  public void after() throws ProcessingException {
    if (registration != null) {
      TestingUtility.unregisterBeans(registration);
    }
  }

}
