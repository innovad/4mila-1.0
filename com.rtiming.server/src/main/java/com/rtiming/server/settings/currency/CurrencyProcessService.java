package com.rtiming.server.settings.currency;

import java.math.BigDecimal;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.AbstractCodeBoxData;
import com.rtiming.shared.common.security.permission.CreateCurrencyPermission;
import com.rtiming.shared.common.security.permission.DeleteCurrencyPermission;
import com.rtiming.shared.common.security.permission.ReadCurrencyPermission;
import com.rtiming.shared.common.security.permission.UpdateCurrencyPermission;
import com.rtiming.shared.dao.RtCurrency;
import com.rtiming.shared.dao.RtCurrencyKey;
import com.rtiming.shared.settings.ICodeProcessService;
import com.rtiming.shared.settings.currency.CurrencyCodeType;
import com.rtiming.shared.settings.currency.CurrencyFormData;
import com.rtiming.shared.settings.currency.ICurrencyProcessService;

public class CurrencyProcessService implements ICurrencyProcessService {

  @Override
  public CurrencyFormData prepareCreate(CurrencyFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateCurrencyPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    formData.getCodeBox().getCodeTypeUid().setValue(CurrencyCodeType.ID);
    BEANS.get(ICodeProcessService.class).prepareCreateCodeBox(formData.getCodeBox());

    return formData;
  }

  @Override
  public CurrencyFormData create(CurrencyFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateCurrencyPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    formData.getCodeBox().getCodeTypeUid().setValue(CurrencyCodeType.ID);
    AbstractCodeBoxData codeBox = BEANS.get(ICodeProcessService.class).createCodeBox(formData.getCodeBox());
    formData.setCurrencyUid(codeBox.getCodeUid().getValue());
    formData = store(formData);

    return formData;
  }

  @Override
  public CurrencyFormData load(CurrencyFormData formData) throws ProcessingException {
    if (!ACCESS.check(new ReadCurrencyPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }
    if (formData == null) {
      return null;
    }

    RtCurrency currency = JPA.find(RtCurrency.class, RtCurrencyKey.create(formData.getCurrencyUid()));
    if (currency != null) {
      formData.getExchangeRate().setValue(BigDecimal.valueOf(currency.getExchangeRate()));
    }

    formData.getCodeBox().getCodeUid().setValue(formData.getCurrencyUid());
    BEANS.get(ICodeProcessService.class).loadCodeBox(formData.getCodeBox());

    return formData;
  }

  @Override
  public CurrencyFormData store(CurrencyFormData formData) throws ProcessingException {
    if (!ACCESS.check(new UpdateCurrencyPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    RtCurrency currency = new RtCurrency();
    currency.setId(RtCurrencyKey.create(formData.getCurrencyUid()));
    currency.setExchangeRate(NumberUtility.nvl(NumberUtility.toDouble(formData.getExchangeRate().getValue()), 0d));
    JPA.merge(currency);

    BEANS.get(ICodeProcessService.class).storeCodeBox(formData.getCodeBox());

    return formData;
  }

  @Override
  public void delete(CurrencyFormData formData) throws ProcessingException {
    if (!ACCESS.check(new DeleteCurrencyPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }
    if (formData == null) {
      return;
    }
    formData = load(formData);

    String queryString = "DELETE FROM RtCurrency " + "WHERE id.clientNr = :sessionClientNr " + "AND id.currencyUid = :currencyUid";
    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("currencyUid", formData.getCurrencyUid());
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    query.executeUpdate();

    BEANS.get(ICodeProcessService.class).deleteCodeBox(formData.getCodeBox());
  }
}
