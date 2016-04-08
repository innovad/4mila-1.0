package com.rtiming.server.club;

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
import com.rtiming.shared.club.IClubLookupService;
import com.rtiming.shared.dao.RtClub;
import com.rtiming.shared.dao.RtClubKey_;
import com.rtiming.shared.dao.RtClub_;

public class ClubLookupService extends AbstractJPALookupService implements IClubLookupService {

  CriteriaBuilder b = JPA.getCriteriaBuilder();
  CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
  Root<RtClub> club = selectQuery.from(RtClub.class);

  Expression<String> displayString = b.coalesce(b.concat(club.get(RtClub_.name), b.concat(" (", b.concat(club.get(RtClub_.shortcut), ")"))), club.get(RtClub_.name));

  @Override
  protected Predicate getKeyWhere(Object key) throws ProcessingException {
    return b.equal(club.get(RtClub_.id).get(RtClubKey_.clubNr), key);
  }

  @Override
  protected Predicate getTextWhere(String text, ILookupCall call) throws ProcessingException {
    return b.like(b.upper(displayString), text);
  }

  @Override
  protected String getConfiguredIconId() {
    return Icons.CLUB;
  }

  @Override
  protected List<Object[]> fetchLookupRows(Predicate additionalPredicate, ILookupCall call) {

    selectQuery.select(b.array(club.get(RtClub_.id).get(RtClubKey_.clubNr), displayString)).where(b.and(b.equal(club.get(RtClub_.id).get(RtClubKey_.clientNr), ServerSession.get().getSessionClientNr()), additionalPredicate == null ? b.conjunction() : additionalPredicate));
    List<Object[]> resultList = JPA.createQuery(selectQuery).getResultList();
    return resultList;
  }

}
