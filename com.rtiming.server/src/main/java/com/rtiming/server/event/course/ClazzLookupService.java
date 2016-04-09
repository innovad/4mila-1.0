package com.rtiming.server.event.course;

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
import com.rtiming.shared.dao.RtEventClass;
import com.rtiming.shared.dao.RtEventClassKey_;
import com.rtiming.shared.dao.RtEventClass_;
import com.rtiming.shared.dao.RtUc;
import com.rtiming.shared.dao.RtUc_;
import com.rtiming.shared.dao.RtUcl;
import com.rtiming.shared.dao.RtUclKey_;
import com.rtiming.shared.dao.RtUcl_;
import com.rtiming.shared.settings.clazz.ClazzLookupCall;
import com.rtiming.shared.settings.clazz.IClazzLookupService;

public class ClazzLookupService extends AbstractJPALookupService implements IClazzLookupService {

  CriteriaBuilder b = JPA.getCriteriaBuilder();
  CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
  Root<RtEventClass> eventClass = selectQuery.from(RtEventClass.class);
  Join<RtEventClass, RtUc> joinUc = eventClass.join(RtEventClass_.rtUc1, JoinType.LEFT);
  Join<RtUc, RtUcl> joinUcl = joinUc.join(RtUc_.rtUcls, JoinType.LEFT);

  Expression<String> displayString = joinUc.get(RtUc_.shortcut);
  Expression<String> displayStringLong = b.concat(joinUcl.get(RtUcl_.codeName), b.concat(" (", b.concat(joinUc.get(RtUc_.shortcut), ")")));

  @Override
  protected Predicate getKeyWhere(Object key) throws ProcessingException {
    return b.equal(eventClass.get(RtEventClass_.id).get(RtEventClassKey_.classUid), key);
  }

  @Override
  protected Predicate getTextWhere(String text, ILookupCall call) throws ProcessingException {
    boolean showShortcutOnly = ((ClazzLookupCall) call).isShowShortcutOnly();
    if (showShortcutOnly) {
      return b.like(b.upper(displayString), text);
    }
    return b.like(b.upper(displayStringLong), text);
  }

  @Override
  protected Predicate getRecWhere(ILookupCall call) throws ProcessingException {
    return b.equal(eventClass.get(RtEventClass_.parentUid), call.getRec());
  }

  @Override
  protected String getConfiguredIconId() {
    return Icons.CLAZZ;
  }

  @Override
  protected List<Object[]> fetchLookupRows(Predicate additionalPredicate, ILookupCall call) {
    Long eventNr = ((ClazzLookupCall) call).getEventNr();
    boolean showLegsOnly = ((ClazzLookupCall) call).isShowLegsOnly();
    boolean showClassesOnly = ((ClazzLookupCall) call).isShowClassesOnly();
    boolean showShortcutOnly = ((ClazzLookupCall) call).isShowShortcutOnly();

    selectQuery.select(b.array(eventClass.get(RtEventClass_.id).get(RtEventClassKey_.classUid), showShortcutOnly ? displayString : displayStringLong, b.literal(""), showShortcutOnly ? joinUcl.get(RtUcl_.codeName) : b.literal(""), b.literal(""), b.literal(""), b.literal(""), b.literal(""), eventClass.get(RtEventClass_.parentUid))).where(b.and(b.equal(eventClass.get(RtEventClass_.id).get(RtEventClassKey_.clientNr), ServerSession.get().getSessionClientNr()), b.equal(joinUcl.get(RtUcl_.id).get(RtUclKey_.languageUid), ServerSession.get().getLanguageUid()), b.equal(eventClass.get(RtEventClass_.id).get(RtEventClassKey_.eventNr), eventNr), showLegsOnly ? b.notEqual(eventClass.get(RtEventClass_.id).get(RtEventClassKey_.classUid), b.coalesce(eventClass.get(RtEventClass_.parentUid), 0)) : b.conjunction(), showClassesOnly ? b.isNull(eventClass.get(RtEventClass_.parentUid)) : b.conjunction(), additionalPredicate == null ? b.conjunction() : additionalPredicate));
    List<Object[]> resultList = JPA.createQuery(selectQuery).getResultList();
    return resultList;
  }

}
