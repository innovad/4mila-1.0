package com.rtiming.server.entry.startblock;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.AbstractJPALookupService;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.dao.RtEventStartblock;
import com.rtiming.shared.dao.RtEventStartblockKey_;
import com.rtiming.shared.dao.RtEventStartblock_;
import com.rtiming.shared.dao.RtUc;
import com.rtiming.shared.dao.RtUc_;
import com.rtiming.shared.dao.RtUcl;
import com.rtiming.shared.dao.RtUclKey_;
import com.rtiming.shared.dao.RtUcl_;
import com.rtiming.shared.entry.startblock.IStartblockLookupService;
import com.rtiming.shared.entry.startblock.StartblockLookupCall;

public class StartblockLookupService extends AbstractJPALookupService implements IStartblockLookupService {

  CriteriaBuilder b = JPA.getCriteriaBuilder();
  CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
  Root<RtEventStartblock> eventStartblock = selectQuery.from(RtEventStartblock.class);
  Join<RtEventStartblock, RtUc> uc = eventStartblock.join(RtEventStartblock_.rtUc, JoinType.INNER);
  Join<RtUc, RtUcl> ucl = uc.join(RtUc_.rtUcls, JoinType.INNER);

  Expression<String> displayString = ucl.get(RtUcl_.codeName);

  @Override
  protected Predicate getKeyWhere(Object key) throws ProcessingException {
    return b.equal(eventStartblock.get(RtEventStartblock_.id).get(RtEventStartblockKey_.startblockUid), key);
  }

  @Override
  protected Predicate getTextWhere(String text, ILookupCall call) throws ProcessingException {
    return b.like(b.upper(displayString), text);
  }

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.File; // TODO MIG
  }

  @Override
  protected List<Object[]> fetchLookupRows(Predicate additionalPredicate, ILookupCall call) {
    Long eventNr = ((StartblockLookupCall) call).getEventNr();

    selectQuery.select(b.array(eventStartblock.get(RtEventStartblock_.id).get(RtEventStartblockKey_.startblockUid), displayString)).where(b.and(b.equal(eventStartblock.get(RtEventStartblock_.id).get(RtEventStartblockKey_.clientNr), ServerSession.get().getSessionClientNr()), eventNr != null ? b.equal(eventStartblock.get(RtEventStartblock_.id).get(RtEventStartblockKey_.eventNr), eventNr) : b.conjunction(), b.equal(ucl.get(RtUcl_.id).get(RtUclKey_.languageUid), ServerSession.get().getLanguageUid()), additionalPredicate == null ? b.conjunction() : additionalPredicate));
    List<Object[]> resultList = JPA.createQuery(selectQuery).getResultList();
    return resultList;
  }

}
