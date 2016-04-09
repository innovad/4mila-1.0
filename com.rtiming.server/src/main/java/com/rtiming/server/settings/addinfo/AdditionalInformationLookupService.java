package com.rtiming.server.settings.addinfo;

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
import com.rtiming.shared.dao.RtAdditionalInformationDef;
import com.rtiming.shared.dao.RtAdditionalInformationDefKey_;
import com.rtiming.shared.dao.RtAdditionalInformationDef_;
import com.rtiming.shared.dao.RtUc;
import com.rtiming.shared.dao.RtUc_;
import com.rtiming.shared.dao.RtUcl;
import com.rtiming.shared.dao.RtUclKey_;
import com.rtiming.shared.dao.RtUcl_;
import com.rtiming.shared.settings.addinfo.AdditionalInformationLookupCall;
import com.rtiming.shared.settings.addinfo.IAdditionalInformationLookupService;

public class AdditionalInformationLookupService extends AbstractJPALookupService implements IAdditionalInformationLookupService {

  CriteriaBuilder b = JPA.getCriteriaBuilder();
  CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
  Root<RtAdditionalInformationDef> addInfoDef = selectQuery.from(RtAdditionalInformationDef.class);
  Join<RtAdditionalInformationDef, RtUc> joinUc = addInfoDef.join(RtAdditionalInformationDef_.rtUc, JoinType.INNER);
  Join<RtUc, RtUcl> joinUcl = joinUc.join(RtUc_.rtUcls, JoinType.LEFT);

  Expression<String> displayString = joinUcl.get(RtUcl_.codeName);

  @Override
  protected Predicate getKeyWhere(Object key) throws ProcessingException {
    return b.equal(addInfoDef.get(RtAdditionalInformationDef_.id).get(RtAdditionalInformationDefKey_.additionalInformationUid), key);
  }

  @Override
  protected Predicate getTextWhere(String text, ILookupCall call) throws ProcessingException {
    return b.like(b.upper(displayString), text);
  }

  @Override
  protected String getConfiguredIconId() {
    return Icons.ADDITIONAL_INFORMATION;
  }

  @Override
  protected List<Object[]> fetchLookupRows(Predicate additionalPredicate, ILookupCall call) {
    Long parentUid = ((AdditionalInformationLookupCall) call).getParentUid();
    Long entityUid = ((AdditionalInformationLookupCall) call).getEntityUid();

    selectQuery.select(b.array(addInfoDef.get(RtAdditionalInformationDef_.id).get(RtAdditionalInformationDefKey_.additionalInformationUid), displayString)).where(b.and(b.equal(addInfoDef.get(RtAdditionalInformationDef_.id).get(RtAdditionalInformationDefKey_.clientNr), ServerSession.get().getSessionClientNr()), b.equal(joinUcl.get(RtUcl_.id).get(RtUclKey_.languageUid), ServerSession.get().getLanguageUid()), parentUid == null ? b.isNull(addInfoDef.get(RtAdditionalInformationDef_.parentUid)) : b.equal(addInfoDef.get(RtAdditionalInformationDef_.parentUid), parentUid), entityUid == null ? b.conjunction() : b.equal(addInfoDef.get(RtAdditionalInformationDef_.entityUid), entityUid), additionalPredicate == null ? b.conjunction() : additionalPredicate));
    List<Object[]> resultList = JPA.createQuery(selectQuery).getResultList();
    return resultList;
  }

}
