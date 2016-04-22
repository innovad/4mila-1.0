package com.rtiming.server.settings.fee;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.settings.SettingsOutlineService;
import com.rtiming.shared.dao.RtCurrency;
import com.rtiming.shared.dao.RtCurrencyKey;
import com.rtiming.shared.dao.RtFeeGroup;
import com.rtiming.shared.dao.RtFeeGroupKey;
import com.rtiming.shared.dao.RtUc;
import com.rtiming.shared.dao.RtUcKey;
import com.rtiming.shared.dao.RtUcl;
import com.rtiming.shared.dao.RtUclKey;
import com.rtiming.shared.settings.currency.CurrencyCodeType;
import com.rtiming.shared.settings.fee.FeeFormData;
import com.rtiming.shared.settings.fee.FeeGroupFormData;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class FeeGroupProcessServiceTest {

  private RtUc ucCurrency;
  private RtUcl uclCurrency;
  private RtCurrency currency;

  @Test
  public void testDelete1() throws ProcessingException {
    FeeGroupProcessService svc = new FeeGroupProcessService();
    FeeGroupFormData formData = new FeeGroupFormData();
    svc.delete(formData);
  }

  @Test
  public void testDelete2() throws ProcessingException {
    RtFeeGroup group = new RtFeeGroup();
    group.setId(RtFeeGroupKey.create((Long) null));
    JPA.merge(group);

    FeeGroupProcessService svc = new FeeGroupProcessService();
    FeeGroupFormData formData = new FeeGroupFormData();
    formData.setFeeGroupNr(group.getId().getId());
    svc.delete(formData);

    Assert.assertNull("deleted", JPA.find(RtFeeGroup.class, group.getId()));
  }

  @Test
  public void testStore() throws Exception {
    FeeGroupProcessService svc = new FeeGroupProcessService();

    RtFeeGroup group = new RtFeeGroup();
    group.setId(RtFeeGroupKey.create((Long) null));
    JPA.merge(group);

    FeeGroupFormData formData = new FeeGroupFormData();
    formData.setFeeGroupNr(group.getId().getId());
    formData = svc.load(formData);
    formData.getName().setValue("abcdef");
    formData = svc.store(formData);

    group = JPA.find(RtFeeGroup.class, group.getId());
    Assert.assertEquals("updated", "abcdef", group.getName());

    JPA.remove(group);
  }

  @Test
  public void testGetMissingCurrencies1() throws Exception {
    createNewCurrency();

    FeeGroupProcessService svc = new FeeGroupProcessService();
    Long[] missingCurrencies = svc.getMissingCurrencies(null);

    SettingsOutlineService settingsOutlineSvc = new SettingsOutlineService();
    Object[][] currencyData = settingsOutlineSvc.getCurrencyTableData();

    assertEquals("Number of currencies", currencyData.length, missingCurrencies.length);
    assertTrue(missingCurrencies.length > 0);

    removeCurrency();
  }

  @Test
  public void testGetMissingCurrencies2() throws Exception {
    createNewCurrency();

    RtFeeGroup group = new RtFeeGroup();
    group.setId(RtFeeGroupKey.create((Long) null));
    JPA.merge(group);
    JPA.currentEntityManager().flush();

    FeeProcessService feeSvc = new FeeProcessService();
    FeeFormData fee = new FeeFormData();
    fee = feeSvc.prepareCreate(fee);
    fee.setFeeGroupNr(group.getId().getId());
    fee.getCurrency().setValue(currency.getId().getId());
    fee.getFee().setValue(1d);
    fee = feeSvc.create(fee);

    FeeGroupProcessService svc = new FeeGroupProcessService();
    Long[] missingCurrencies = svc.getMissingCurrencies(group.getId().getId());

    SettingsOutlineService settingsOutlineSvc = new SettingsOutlineService();
    Object[][] currencyData = settingsOutlineSvc.getCurrencyTableData();

    assertEquals("Number of currencies", currencyData.length, missingCurrencies.length + 1);

    feeSvc.delete(fee);
    JPA.remove(group);
    removeCurrency();
  }

  private void removeCurrency() throws ProcessingException {
    JPA.remove(currency);
    JPA.remove(uclCurrency);
    JPA.remove(ucCurrency);
  }

  private void createNewCurrency() throws ProcessingException {
    ucCurrency = new RtUc();
    ucCurrency.setCodeType(CurrencyCodeType.ID);
    ucCurrency.setActive(true);
    ucCurrency.setId(RtUcKey.create((Long) null));
    JPA.merge(ucCurrency);

    uclCurrency = new RtUcl();
    RtUclKey key = new RtUclKey();
    key.setClientNr(ServerSession.get().getSessionClientNr());
    key.setLanguageUid(ServerSession.get().getLanguageUid());
    key.setUcUid(ucCurrency.getId().getId());
    uclCurrency.setId(key);
    JPA.merge(uclCurrency);

    currency = new RtCurrency();
    currency.setId(RtCurrencyKey.create(ucCurrency.getId().getId()));
    currency.setExchangeRate(1d);
    JPA.merge(currency);
  }

}
