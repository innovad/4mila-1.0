package com.rtiming.server.ecard.download;

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
import com.rtiming.shared.dao.RtEcardStation;
import com.rtiming.shared.dao.RtEcardStationKey_;
import com.rtiming.shared.dao.RtEcardStation_;
import com.rtiming.shared.ecard.download.IECardStationLookupService;

public class ECardStationLookupService extends AbstractJPALookupService implements IECardStationLookupService {

  CriteriaBuilder b = JPA.getCriteriaBuilder();
  CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
  Root<RtEcardStation> station = selectQuery.from(RtEcardStation.class);

  Expression<String> displayString = b.concat(station.get(RtEcardStation_.clientAddress), b.concat(" - ", station.get(RtEcardStation_.identifier)));

  @Override
  protected Predicate getKeyWhere(Object key) throws ProcessingException {
    return b.equal(station.get(RtEcardStation_.id).get(RtEcardStationKey_.stationNr), key);
  }

  @Override
  protected Predicate getTextWhere(String text, ILookupCall call) throws ProcessingException {
    return b.like(b.upper(displayString), text);
  }

  @Override
  protected String getConfiguredIconId() {
    return Icons.ECARD_STATION;
  }

  @Override
  protected List<Object[]> fetchLookupRows(Predicate additionalPredicate, ILookupCall call) {

    selectQuery.select(b.array(station.get(RtEcardStation_.id).get(RtEcardStationKey_.stationNr), displayString)).where(b.and(b.equal(station.get(RtEcardStation_.id).get(RtEcardStationKey_.clientNr), ServerSession.get().getSessionClientNr()), additionalPredicate == null ? b.conjunction() : additionalPredicate));
    List<Object[]> resultList = JPA.createQuery(selectQuery).getResultList();
    return resultList;
  }

}
