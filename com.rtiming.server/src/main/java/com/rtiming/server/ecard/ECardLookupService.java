package com.rtiming.server.ecard;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.AbstractJPALookupService;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.Icons;
import com.rtiming.shared.dao.RtEcard;
import com.rtiming.shared.dao.RtEcardKey_;
import com.rtiming.shared.dao.RtEcard_;
import com.rtiming.shared.ecard.IECardLookupService;

public class ECardLookupService extends AbstractJPALookupService implements IECardLookupService {

  CriteriaBuilder b = JPA.getCriteriaBuilder();
  CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
  Root<RtEcard> ecard = selectQuery.from(RtEcard.class);

  @Override
  protected Predicate getKeyWhere(Object key) throws ProcessingException {
    return b.equal(ecard.get(RtEcard_.id).get(RtEcardKey_.eCardNr), key);
  }

  @Override
  protected Predicate getTextWhere(String text, ILookupCall call) throws ProcessingException {
    return b.like(b.upper(ecard.get(RtEcard_.ecardNo)), text);
  }

  @Override
  protected List<Object[]> fetchLookupRows(Predicate additionalPredicate, ILookupCall call) {
    selectQuery.select(b.array(ecard.get(RtEcard_.id).get(RtEcardKey_.eCardNr), ecard.get(RtEcard_.ecardNo), b.concat(b.nullLiteral(String.class), ""), ecard.get(RtEcard_.rentalCard), ecard.get(RtEcard_.typeUid))).where(b.and(b.equal(ecard.get(RtEcard_.id).get(RtEcardKey_.clientNr), ServerSession.get().getSessionClientNr()), additionalPredicate == null ? b.conjunction() : additionalPredicate));
    List<Object[]> resultList = JPA.createQuery(selectQuery).getResultList();
    return resultList;
  }

  @Override
  protected String getConfiguredIconId() {
    return Icons.ECARD;
  }

}
