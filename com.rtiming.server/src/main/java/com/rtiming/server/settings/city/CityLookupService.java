package com.rtiming.server.settings.city;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.AbstractJPALookupService;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.Icons;
import com.rtiming.shared.dao.RtCity;
import com.rtiming.shared.dao.RtCityKey_;
import com.rtiming.shared.dao.RtCity_;
import com.rtiming.shared.dao.RtCountry;
import com.rtiming.shared.dao.RtCountry_;
import com.rtiming.shared.settings.city.ICityLookupService;

public class CityLookupService extends AbstractJPALookupService implements ICityLookupService {

  CriteriaBuilder b = JPA.getCriteriaBuilder();
  CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
  Root<RtCity> city = selectQuery.from(RtCity.class);
  Join<RtCity, RtCountry> joinCountry = city.join(RtCity_.rtCountry, JoinType.LEFT);

  Expression<String> displayString = b.concat(b.concat(b.concat(b.coalesce(b.concat(city.get(RtCity_.zip), " "), ""), city.get(RtCity_.name)), b.coalesce(b.concat(b.concat(" (", city.get(RtCity_.region)), ")"), "")), b.coalesce(b.concat(", ", joinCountry.get(RtCountry_.countryCode)), ""));

  @Override
  protected Predicate getKeyWhere(Object key) throws ProcessingException {
    return b.equal(city.get(RtCity_.id).get(RtCityKey_.cityNr), key);
  }

  @Override
  protected Predicate getTextWhere(String text, ILookupCall call) throws ProcessingException {
    return b.like(b.upper(displayString), text);
  }

  @Override
  protected String getConfiguredIconId() {
    return Icons.CITY;
  }

  @Override
  protected List<Object[]> fetchLookupRows(Predicate additionalPredicate, ILookupCall call) {

    selectQuery.select(b.array(city.get(RtCity_.id).get(RtCityKey_.cityNr), displayString)).where(b.and(b.equal(city.get(RtCity_.id).get(RtCityKey_.clientNr), ServerSession.get().getSessionClientNr()), additionalPredicate == null ? b.conjunction() : additionalPredicate));
    List<Object[]> resultList = JPA.createQuery(selectQuery).getResultList();
    return resultList;
  }

}
