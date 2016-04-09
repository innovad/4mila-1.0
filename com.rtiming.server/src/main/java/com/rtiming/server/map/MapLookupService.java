package com.rtiming.server.map;

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
import com.rtiming.shared.dao.RtMap;
import com.rtiming.shared.dao.RtMapKey_;
import com.rtiming.shared.dao.RtMap_;
import com.rtiming.shared.map.IMapLookupService;

public class MapLookupService extends AbstractJPALookupService implements IMapLookupService {

  CriteriaBuilder b = JPA.getCriteriaBuilder();
  CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
  Root<RtMap> map = selectQuery.from(RtMap.class);

  Expression<String> displayString = map.get(RtMap_.name);

  @Override
  protected Predicate getKeyWhere(Object key) throws ProcessingException {
    return b.equal(map.get(RtMap_.key).get(RtMapKey_.mapNr), key);
  }

  @Override
  protected Predicate getTextWhere(String text, ILookupCall call) throws ProcessingException {
    return b.like(b.upper(displayString), text);
  }

  @Override
  protected String getConfiguredIconId() {
    return Icons.COUNTRY;
  }

  @Override
  protected List<Object[]> fetchLookupRows(Predicate additionalPredicate, ILookupCall call) {

    selectQuery.select(b.array(map.get(RtMap_.key).get(RtMapKey_.mapNr), displayString)).where(b.and(b.equal(map.get(RtMap_.key).get(RtMapKey_.clientNr), ServerSession.get().getSessionClientNr()), additionalPredicate == null ? b.conjunction() : additionalPredicate));
    List<Object[]> resultList = JPA.createQuery(selectQuery).getResultList();
    return resultList;
  }

}
