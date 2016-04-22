package com.rtiming.server.settings.currency;

import static org.junit.Assert.assertEquals;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.dao.RtCurrency;
import com.rtiming.shared.dao.RtCurrencyKey;
import com.rtiming.shared.dao.RtUc;
import com.rtiming.shared.dao.RtUcKey;
import com.rtiming.shared.settings.currency.CurrencyCodeType;
import com.rtiming.shared.settings.currency.CurrencyFormData;

@RunWith(ServerTestRunner.class)
@RunWithSubject("admin")
@RunWithServerSession(ServerSession.class)
public class CurrencyProcessServiceTest {

  @Test
  public void testDelete1() throws ProcessingException {
    CurrencyProcessService svc = new CurrencyProcessService();
    svc.delete(null);
  }

  @Test
  public void testDelete2() throws ProcessingException {
    CurrencyProcessService svc = new CurrencyProcessService();
    svc.delete(new CurrencyFormData());
  }

  @Test
  public void testLoad1() throws Exception {
    RtUc ucCurrency = new RtUc();
    ucCurrency.setCodeType(CurrencyCodeType.ID);
    ucCurrency.setActive(true);
    ucCurrency.setId(RtUcKey.create((Long) null));
    JPA.merge(ucCurrency);

    RtCurrency currency = new RtCurrency();
    currency.setId(RtCurrencyKey.create(ucCurrency.getId().getId()));
    currency.setExchangeRate(1d);
    JPA.merge(currency);

    CurrencyProcessService svc = new CurrencyProcessService();
    CurrencyFormData formData = new CurrencyFormData();
    formData.setCurrencyUid(ucCurrency.getId().getId());
    formData = svc.load(formData);
    assertEquals("loaded", 1d, formData.getExchangeRate().getValue(), 0.0001d);
    formData.getExchangeRate().setValue(2d);
    formData = svc.store(formData);

    RtCurrency find = JPA.find(RtCurrency.class, currency.getId());
    assertEquals("updated", 2d, find.getExchangeRate());

    JPA.remove(currency);
    JPA.remove(ucCurrency);
  }

  @Test
  public void testLoad2() throws Exception {
    CurrencyProcessService svc = new CurrencyProcessService();
    svc.load(null);
  }

  @Test
  public void testLoad3() throws Exception {
    CurrencyProcessService svc = new CurrencyProcessService();
    svc.load(new CurrencyFormData());
  }

}
