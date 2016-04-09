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
import com.rtiming.shared.dao.RtControl;
import com.rtiming.shared.dao.RtControlKey_;
import com.rtiming.shared.dao.RtControl_;
import com.rtiming.shared.dao.RtEvent;
import com.rtiming.shared.dao.RtEvent_;
import com.rtiming.shared.event.course.ControlLookupCall;
import com.rtiming.shared.event.course.IControlLookupService;

public class ControlLookupService extends AbstractJPALookupService implements IControlLookupService {

  CriteriaBuilder b = JPA.getCriteriaBuilder();
  CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
  Root<RtControl> control = selectQuery.from(RtControl.class);
  Join<RtControl, RtEvent> joinEvent = control.join(RtControl_.rtEvent, JoinType.LEFT);

  Expression<String> displayString = control.get(RtControl_.controlNo);
  Expression<String> displayStringWithEvent = b.concat(control.get(RtControl_.controlNo), b.concat(" (", b.concat(joinEvent.get(RtEvent_.name), ")")));

  @Override
  protected Predicate getKeyWhere(Object key) throws ProcessingException {
    return b.equal(control.get(RtControl_.id).get(RtControlKey_.controlNr), key);
  }

  @Override
  protected Predicate getTextWhere(String text, ILookupCall call) throws ProcessingException {
    boolean isDisplayControl = ((ControlLookupCall) call).isIsDisplayEvent();
    if (isDisplayControl) {
      return b.like(b.upper(displayStringWithEvent), text);
    }
    return b.like(b.upper(displayString), text);
  }

  @Override
  protected String getConfiguredIconId() {
    return Icons.EVENT;
  }

  @Override
  protected List<Object[]> fetchLookupRows(Predicate additionalPredicate, ILookupCall call) {
    boolean isDisplayControl = ((ControlLookupCall) call).isIsDisplayEvent();
    Long eventNr = ((ControlLookupCall) call).getEventNr();
    Long typeUid = ((ControlLookupCall) call).getTypeUid();

    selectQuery.select(b.array(control.get(RtControl_.id).get(RtControlKey_.controlNr), isDisplayControl ? displayStringWithEvent : displayString)).where(b.and(b.equal(control.get(RtControl_.id).get(RtControlKey_.clientNr), ServerSession.get().getSessionClientNr()), additionalPredicate == null ? b.conjunction() : additionalPredicate, eventNr != null ? b.equal(control.get(RtControl_.eventNr), eventNr) : b.conjunction(), typeUid != null ? b.equal(control.get(RtControl_.typeUid), typeUid) : b.conjunction()));
    List<Object[]> resultList = JPA.createQuery(selectQuery).getResultList();
    return resultList;
  }

//  @Override
//  public String getConfiguredSqlSelect() {
//    return "SELECT C.CONTROL_NR, C.CONTROL_NO || CASE WHEN :isDisplayControl=TRUE THEN ' (' || EV.NAME || ') ' ELSE '' END " +
//        "FROM RT_CONTROL C " +
//        "LEFT JOIN RT_EVENT EV ON EV.EVENT_NR = C.EVENT_NR AND EV.CLIENT_NR = C.CLIENT_NR " +
//        "WHERE C.EVENT_NR = COALESCE(:eventNr, C.EVENT_NR) " +
//        "AND C.TYPE_UID = COALESCE(:typeUid, C.TYPE_UID) " +
//        "<key> AND CAST(:key AS INT) = C.CONTROL_NR </key>" +
//        "<text> AND UPPER(C.CONTROL_NO) LIKE UPPER(:text)</text>";
//  }
}
