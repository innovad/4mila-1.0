package com.rtiming.server.event.course;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.exception.VetoException;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.FMilaTypedQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.common.database.jpa.JPAUtility;
import com.rtiming.server.event.course.loop.CourseCalculator;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.CreateCoursePermission;
import com.rtiming.shared.common.security.permission.DeleteCoursePermission;
import com.rtiming.shared.common.security.permission.ReadCoursePermission;
import com.rtiming.shared.common.security.permission.UpdateCoursePermission;
import com.rtiming.shared.dao.RtCourse;
import com.rtiming.shared.dao.RtCourseKey;
import com.rtiming.shared.event.IEventsOutlineService;
import com.rtiming.shared.event.course.ControlFormData;
import com.rtiming.shared.event.course.CourseControlFormData;
import com.rtiming.shared.event.course.CourseControlRowData;
import com.rtiming.shared.event.course.CourseFormData;
import com.rtiming.shared.event.course.IControlProcessService;
import com.rtiming.shared.event.course.ICourseControlProcessService;
import com.rtiming.shared.event.course.ICourseProcessService;
import com.rtiming.shared.services.code.CourseForkTypeCodeType;

public class CourseProcessService  implements ICourseProcessService {

  @Override
  public CourseFormData prepareCreate(CourseFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateCoursePermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    return formData;
  }

  @Override
  public CourseFormData create(CourseFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateCoursePermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    RtCourseKey key = RtCourseKey.create((Long) null);
    RtCourse course = new RtCourse();
    course.setId(key);
    JPA.persist(course);

    formData.setCourseNr(course.getId().getId());
    formData = store(formData);

    return formData;
  }

  @Override
  public CourseFormData load(CourseFormData formData) throws ProcessingException {
    if (!ACCESS.check(new ReadCoursePermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    JPAUtility.select("SELECT eventNr, shortcut, length, climb " +
        "FROM RtCourse C " +
        "WHERE C.id.courseNr = :courseNr " +
        "AND C.id.clientNr = :sessionClientNr " +
        "INTO :event, :shortcut, :length, :climb", formData);

    return formData;
  }

  @Override
  public CourseFormData store(CourseFormData formData) throws ProcessingException {
    if (!ACCESS.check(new UpdateCoursePermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    String queryString = "UPDATE RtCourse " +
        "SET eventNr = :event, " +
        "shortcut = :shortcut, " +
        "length = :length, " +
        "climb = :climb " +
        "WHERE id.courseNr = :courseNr " +
        "AND id.clientNr = :sessionClientNr ";
    FMilaQuery query = JPA.createQuery(queryString);
    JPAUtility.setAutoParameters(query, queryString, formData);
    query.executeUpdate();

    return formData;
  }

  @Override
  public Long getControlCount(Long courseNr) throws ProcessingException {
    List<CourseControlRowData> definitions = BEANS.get(IEventsOutlineService.class).getCourseControlTableData(courseNr);
    List<List<CourseControlRowData>> variants = CourseCalculator.calculateCourse(definitions);
    long maxControlCount = 0;
    for (List<CourseControlRowData> variant : variants) {
      maxControlCount = Math.max(maxControlCount, variant.size());
    }
    return maxControlCount;
  }

  @Override
  public CourseFormData find(String courseShortcut, long eventNr) throws ProcessingException {
    courseShortcut = StringUtility.nvl(courseShortcut, new String()).trim();

    String queryString = "SELECT MAX(id.courseNr) FROM RtCourse " +
        "WHERE UPPER(shortcut) = UPPER(:courseShortcut) " +
        "AND eventNr = :eventNr " +
        "AND id.clientNr = :sessionClientNr ";
    FMilaTypedQuery<Long> query = JPA.createQuery(queryString, Long.class);
    query.setParameter("courseShortcut", courseShortcut);
    query.setParameter("eventNr", eventNr);
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    Long courseNr = query.getSingleResult();

    CourseFormData course = new CourseFormData();
    if (courseNr != null) {
      course.setCourseNr(courseNr);
      course = load(course);
    }
    else {
      course.getEvent().setValue(eventNr);
      course.getShortcut().setValue(courseShortcut);
    }

    return course;
  }

  @Override
  public List<ControlFormData> loadControls(long courseNr, Long clientNr) throws ProcessingException {
    ArrayList<ControlFormData> result = new ArrayList<ControlFormData>();

    String queryString = "SELECT controlNr " +
        "FROM RtCourseControl CC " +
        "WHERE courseNr = :courseNr " +
        "AND id.clientNr = :clientNr " +
        "ORDER BY sortcode ASC ";
    FMilaTypedQuery<Long> query = JPA.createQuery(queryString, Long.class);
    query.setParameter("clientNr", NumberUtility.nvl(clientNr, ServerSession.get().getSessionClientNr()));
    query.setParameter("courseNr", courseNr);
    List<Long> controlNrs = query.getResultList();

    for (Long controlNr : controlNrs) {
      ControlFormData formData = new ControlFormData();
      formData.setControlNr(controlNr);
      formData.setClientNr(clientNr);
      formData = BEANS.get(IControlProcessService.class).load(formData);
      result.add(formData);
    }

    return result;
  }

  @Override
  public void delete(CourseFormData formData, boolean includingCourse) throws ProcessingException {
    if (!ACCESS.check(new DeleteCoursePermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }
    if (formData == null || formData.getCourseNr() == null) {
      return;
    }

    // controls
    String queryString = "DELETE FROM RtCourseControl " +
        "WHERE courseNr = :courseNr " +
        "AND id.clientNr = :sessionClientNr";
    FMilaQuery query = JPA.createQuery(queryString);
    JPAUtility.setAutoParameters(query, queryString, formData);
    query.executeUpdate();

    // course
    if (includingCourse) {
      queryString = "DELETE FROM RtCourse " +
          "WHERE id.courseNr = :courseNr " +
          "AND id.clientNr = :sessionClientNr";
      query = JPA.createQuery(queryString);
      JPAUtility.setAutoParameters(query, queryString, formData);
      query.executeUpdate();
    }
  }

  @Override
  public CourseFormData createNewCourseWithVariants(CourseFormData courseFormData, Long... courseNrs) throws ProcessingException {
    courseFormData = create(courseFormData);
    List<List<CourseControlRowData>> allCourseControlVariants = new ArrayList<>();
    for (Long courseNr : courseNrs) {
      List<CourseControlRowData> definitions = BEANS.get(IEventsOutlineService.class).getCourseControlTableData(courseNr);
      List<List<CourseControlRowData>> courseControlVariants = CourseCalculator.calculateCourse(definitions);
      allCourseControlVariants.addAll(courseControlVariants);
    }

    // start = master control
    // insert start as loop variant center
    Long eventNr = courseFormData.getEvent().getValue();
    Long courseNr = courseFormData.getCourseNr();
    CourseControlFormData start = CourseCopyUtility.row2formData(CourseCopyUtility.getFirstControlOfFirstCourse(allCourseControlVariants), eventNr, courseNr, CourseForkTypeCodeType.ForkCode.ID, null, null, 1L);
    start = BEANS.get(ICourseControlProcessService.class).create(start);
    Long startCourseControlNr = start.getCourseControlNr();
    Long startControlNr = start.getControl().getValue();

    List<CourseControlFormData> newControls = CourseCopyUtility.createVariantsFromAllCourses(eventNr, courseNr, startCourseControlNr, startControlNr, allCourseControlVariants);

    // store
    for (CourseControlFormData controlFormData : newControls) {
      BEANS.get(ICourseControlProcessService.class).create(controlFormData);
    }

    return courseFormData;
  }

}
