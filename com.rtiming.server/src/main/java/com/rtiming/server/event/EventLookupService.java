package com.rtiming.server.event;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.AbstractJPALookupService;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.Icons;
import com.rtiming.shared.dao.RtEvent;
import com.rtiming.shared.dao.RtEventKey_;
import com.rtiming.shared.dao.RtEvent_;
import com.rtiming.shared.event.IEventLookupService;

public class EventLookupService extends AbstractJPALookupService implements IEventLookupService {

  CriteriaBuilder b = JPA.getCriteriaBuilder();
  CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
  Root<RtEvent> event = selectQuery.from(RtEvent.class);

  Expression<String> displayString = event.get(RtEvent_.name);

  @Override
  protected Predicate getKeyWhere(Object key) throws ProcessingException {
    return b.equal(event.get(RtEvent_.id).get(RtEventKey_.eventNr), key);
  }

  @Override
  protected Predicate getTextWhere(String text, ILookupCall call) throws ProcessingException {
    return b.like(b.upper(displayString), text);
  }

  @Override
  protected String getConfiguredIconId() {
    return Icons.EVENT;
  }

  @Override
  protected List<Object[]> fetchLookupRows(Predicate additionalPredicate, ILookupCall call) {

    selectQuery.select(b.array(event.get(RtEvent_.id).get(RtEventKey_.eventNr), displayString)).where(b.and(b.equal(event.get(RtEvent_.id).get(RtEventKey_.clientNr), ServerSession.get().getSessionClientNr()), additionalPredicate == null ? b.conjunction() : additionalPredicate));
    List<Object[]> resultList = JPA.createQuery(selectQuery).getResultList();
    return resultList;
  }

}
