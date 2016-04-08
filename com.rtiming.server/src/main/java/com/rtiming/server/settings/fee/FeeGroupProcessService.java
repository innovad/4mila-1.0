package com.rtiming.server.settings.fee;

import java.util.List;

import org.eclipse.scout.commons.BooleanUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.exception.VetoException;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.FMilaTypedQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.common.database.jpa.JPAUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.CreateFeeGroupPermission;
import com.rtiming.shared.common.security.permission.ReadFeeGroupPermission;
import com.rtiming.shared.common.security.permission.UpdateFeeGroupPermission;
import com.rtiming.shared.dao.RtFeeGroup;
import com.rtiming.shared.dao.RtFeeGroupKey;
import com.rtiming.shared.settings.fee.FeeGroupFormData;
import com.rtiming.shared.settings.fee.IFeeGroupProcessService;

public class FeeGroupProcessService  implements IFeeGroupProcessService {

  @Override
  public FeeGroupFormData prepareCreate(FeeGroupFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateFeeGroupPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    return formData;
  }

  @Override
  public FeeGroupFormData create(FeeGroupFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateFeeGroupPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    RtFeeGroupKey key = RtFeeGroupKey.create((Long) null);
    RtFeeGroup feeGroup = new RtFeeGroup();
    feeGroup.setId(key);
    JPA.persist(feeGroup);

    formData.setFeeGroupNr(feeGroup.getId().getId());
    formData = store(formData);

    return formData;
  }

  @Override
  public FeeGroupFormData load(FeeGroupFormData formData) throws ProcessingException {
    if (!ACCESS.check(new ReadFeeGroupPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    JPAUtility.select("SELECT FG.name, FG.cashPaymentOnRegistration " +
        "FROM RtFeeGroup FG " +
        "WHERE id.feeGroupNr = :feeGroupNr " +
        "AND id.clientNr = :sessionClientNr " +
        "INTO :name, :cashPaymentOnRegistration", formData);

    return formData;
  }

  @Override
  public FeeGroupFormData store(FeeGroupFormData formData) throws ProcessingException {
    if (!ACCESS.check(new UpdateFeeGroupPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    RtFeeGroup group = new RtFeeGroup();
    group.setId(RtFeeGroupKey.create(formData.getFeeGroupNr()));
    group.setName(formData.getName().getValue());
    group.setCashPaymentOnRegistration(BooleanUtility.nvl(formData.getCashPaymentOnRegistration().getValue()));
    JPA.merge(group);

    return formData;
  }

  @Override
  public void delete(FeeGroupFormData formData) throws ProcessingException {
    String queryString = "DELETE FROM RtFee F " +
        "WHERE F.id.clientNr = :sessionClientNr " +
        "AND F.rtFeeGroup.id.feeGroupNr = :feeGroupNr";

    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    query.setParameter("feeGroupNr", formData.getFeeGroupNr());
    query.executeUpdate();

    RtFeeGroup group = new RtFeeGroup();
    group.setId(RtFeeGroupKey.create(formData.getFeeGroupNr()));
    JPA.remove(group);
  }

  @Override
  public Long[] getMissingCurrencies(Long feeGroupNr) throws ProcessingException {
    String queryString = "SELECT C.id.currencyUid FROM RtCurrency C WHERE C.id.clientNr = :clientNr";

    FMilaTypedQuery<Long> query = JPA.createQuery(queryString, Long.class);
    query.setParameter("clientNr", ServerSession.get().getSessionClientNr());
    List<Long> allCurrencyUids = query.getResultList();

    queryString = "SELECT DISTINCT F.currencyUid FROM RtFee F " +
        "WHERE F.rtFeeGroup.id.feeGroupNr = :feeGroupNr " +
        "AND F.id.clientNr = :clientNr";

    query = JPA.createQuery(queryString, Long.class);
    query.setParameter("clientNr", ServerSession.get().getSessionClientNr());
    query.setParameter("feeGroupNr", feeGroupNr);
    List<Long> feeCurrencyUids = query.getResultList();

    for (Long uid : feeCurrencyUids) {
      allCurrencyUids.remove(uid);
    }

    return allCurrencyUids.toArray(new Long[allCurrencyUids.size()]);
  }
}
