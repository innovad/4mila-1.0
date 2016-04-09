package com.rtiming.server.event.course;

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
import com.rtiming.shared.dao.RtCourse;
import com.rtiming.shared.dao.RtCourseKey_;
import com.rtiming.shared.dao.RtCourse_;
import com.rtiming.shared.event.course.CourseLookupCall;
import com.rtiming.shared.event.course.ICourseLookupService;

public class CourseLookupService extends AbstractJPALookupService implements ICourseLookupService {

  CriteriaBuilder b = JPA.getCriteriaBuilder();
  CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
  Root<RtCourse> course = selectQuery.from(RtCourse.class);

  Expression<String> displayString = course.get(RtCourse_.shortcut);

  @Override
  protected Predicate getKeyWhere(Object key) throws ProcessingException {
    return b.equal(course.get(RtCourse_.id).get(RtCourseKey_.courseNr), key);
  }

  @Override
  protected Predicate getTextWhere(String text, ILookupCall call) throws ProcessingException {
    return b.like(b.upper(displayString), text);
  }

  @Override
  protected String getConfiguredIconId() {
    return Icons.COURSE;
  }

  @Override
  protected List<Object[]> fetchLookupRows(Predicate additionalPredicate, ILookupCall call) {
    CourseLookupCall courseLookupCall = (CourseLookupCall) call;
    Long eventNr = courseLookupCall.getEventNr();

    selectQuery.select(b.array(course.get(RtCourse_.id).get(RtCourseKey_.courseNr), displayString)).where(b.and(eventNr != null ? b.equal(course.get(RtCourse_.eventNr), eventNr) : b.conjunction(), b.equal(course.get(RtCourse_.id).get(RtCourseKey_.clientNr), ServerSession.get().getSessionClientNr()), additionalPredicate == null ? b.conjunction() : additionalPredicate));
    List<Object[]> resultList = JPA.createQuery(selectQuery).getResultList();
    return resultList;
  }

//  @Override
//  public String getConfiguredSqlSelect() {
//    return "SELECT C.COURSE_NR, C.SHORTCUT, '" + Icons.COURSE + "' " +
//          "FROM RT_COURSE C " +
//          "WHERE C.EVENT_NR = COALESCE(:eventNr,C.EVENT_NR) " +
//          "<key> AND CAST(:key AS INT) = C.COURSE_NR </key>" +
//          "<text> AND UPPER(C.SHORTCUT) LIKE UPPER(:text) </text>";
//  }
}
