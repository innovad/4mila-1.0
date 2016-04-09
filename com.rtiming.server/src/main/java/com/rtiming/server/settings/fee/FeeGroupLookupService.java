package com.rtiming.server.settings.fee;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.AbstractJPALookupService;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.Icons;
import com.rtiming.shared.dao.RtFeeGroup;
import com.rtiming.shared.dao.RtFeeGroupKey_;
import com.rtiming.shared.dao.RtFeeGroup_;
import com.rtiming.shared.settings.fee.IFeeGroupLookupService;

public class FeeGroupLookupService extends AbstractJPALookupService implements IFeeGroupLookupService {

  CriteriaBuilder b = JPA.getCriteriaBuilder();
  CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
  Root<RtFeeGroup> feeGroup = selectQuery.from(RtFeeGroup.class);

  Expression<String> displayString = feeGroup.get(RtFeeGroup_.name);

  @Override
  protected Predicate getKeyWhere(Object key) throws ProcessingException {
    return b.equal(feeGroup.get(RtFeeGroup_.id).get(RtFeeGroupKey_.feeGroupNr), key);
  }

  @Override
  protected Predicate getTextWhere(String text, ILookupCall call) throws ProcessingException {
    return b.like(b.upper(displayString), text);
  }

  @Override
  protected String getConfiguredIconId() {
    return Icons.FEE_GROUP;
  }

  @Override
  protected List<Object[]> fetchLookupRows(Predicate additionalPredicate, ILookupCall call) {

    selectQuery.select(b.array(feeGroup.get(RtFeeGroup_.id).get(RtFeeGroupKey_.feeGroupNr), displayString)).where(b.and(b.equal(feeGroup.get(RtFeeGroup_.id).get(RtFeeGroupKey_.clientNr), ServerSession.get().getSessionClientNr()), additionalPredicate == null ? b.conjunction() : additionalPredicate));
    List<Object[]> resultList = JPA.createQuery(selectQuery).getResultList();
    return resultList;
  }

}
