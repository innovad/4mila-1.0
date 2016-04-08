package com.rtiming.server.entry;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.eclipse.scout.commons.CompareUtility;
import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.AbstractJPALookupService;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.common.database.jpa.JPACriteriaUtility;
import com.rtiming.shared.Icons;
import com.rtiming.shared.dao.RtRegistration;
import com.rtiming.shared.dao.RtRegistrationKey_;
import com.rtiming.shared.dao.RtRegistration_;
import com.rtiming.shared.dataexchange.RegistrationSharedUtility;
import com.rtiming.shared.entry.IRegistrationLookupService;

public class RegistrationLookupService extends AbstractJPALookupService implements IRegistrationLookupService {

  CriteriaBuilder b = JPA.getCriteriaBuilder();
  CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
  Root<RtRegistration> registration = selectQuery.from(RtRegistration.class);

  Expression<String> displayString = registration.get(RtRegistration_.registrationNo);

  @Override
  protected Predicate getKeyWhere(Object key) throws ProcessingException {
    return b.equal(registration.get(RtRegistration_.id).get(RtRegistrationKey_.registrationNr), key);
  }

  @Override
  protected Predicate getTextWhere(String text, ILookupCall call) throws ProcessingException {
    text = JPACriteriaUtility.removeDefaultTokens(text);
    return b.like(b.upper(JPACriteriaUtility.removeDefaultTokens(displayString)), text);
  }

  @Override
  protected String getConfiguredIconId() {
    return Icons.REGISTRATION;
  }

  @Override
  protected List<Object[]> fetchLookupRows(Predicate additionalPredicate, ILookupCall call) {

    selectQuery.select(b.array(registration.get(RtRegistration_.id).get(RtRegistrationKey_.registrationNr), displayString)).where(b.and(b.equal(registration.get(RtRegistration_.id).get(RtRegistrationKey_.clientNr), ServerSession.get().getSessionClientNr()), additionalPredicate == null ? b.conjunction() : additionalPredicate));
    List<Object[]> resultList = JPA.createQuery(selectQuery).getResultList();

    if (CompareUtility.equals(call.getKey(), 0L) && StringUtility.isNullOrEmpty(call.getText())) {
      Object[] newRegistration = new Object[2];
      newRegistration[0] = 0;
      newRegistration[1] = RegistrationSharedUtility.getNewRegistrationNoText();
      resultList.add(newRegistration);
    }

    return resultList;
  }

}
