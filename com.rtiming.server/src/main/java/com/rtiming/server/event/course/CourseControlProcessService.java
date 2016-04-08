package com.rtiming.server.event.course;

import java.util.List;

import org.eclipse.scout.commons.BooleanUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.exception.VetoException;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.FMilaTypedQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.common.database.jpa.JPAUtility;
import com.rtiming.server.event.course.loop.CourseCalculator;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.CreateCourseControlPermission;
import com.rtiming.shared.common.security.permission.DeleteCourseControlPermission;
import com.rtiming.shared.common.security.permission.ReadCourseControlPermission;
import com.rtiming.shared.common.security.permission.UpdateCourseControlPermission;
import com.rtiming.shared.dao.RtCourseControl;
import com.rtiming.shared.dao.RtCourseControlKey;
import com.rtiming.shared.event.IEventsOutlineService;
import com.rtiming.shared.event.course.CourseControlFormData;
import com.rtiming.shared.event.course.CourseControlRowData;
import com.rtiming.shared.event.course.ICourseControlProcessService;

public class CourseControlProcessService  implements ICourseControlProcessService {

  @Override
  public CourseControlFormData prepareCreate(CourseControlFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateCourseControlPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    formData.getCountLeg().setValue(true);
    formData.getMandatory().setValue(true);

    // generate continuing sortcode
    String queryString = "SELECT MAX(sortcode) " +
        "FROM RtCourseControl CC " +
        "WHERE CC.courseNr = :courseNr " +
        "AND CC.id.clientNr = :sessionClientNr ";
    FMilaTypedQuery<Long> query = JPA.createQuery(queryString, Long.class);
    query.setParameter("courseNr", formData.getCourse().getValue());
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    Long nextSortCode = query.getSingleResult();

    formData.getSortCode().setValue(NumberUtility.nvl(nextSortCode, 0) + 1);

    return formData;
  }

  @Override
  public CourseControlFormData create(CourseControlFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateCourseControlPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    RtCourseControlKey key = RtCourseControlKey.create((Long) null);
    RtCourseControl courseControl = new RtCourseControl();
    courseControl.setId(key);
    courseControl.setSortcode(formData.getSortCode().getValue());
    courseControl.setCountLeg(formData.getCountLeg().getValue());
    courseControl.setMandatory(formData.getMandatory().getValue());
    JPA.persist(courseControl);

    formData.setCourseControlNr(courseControl.getId().getId());
    formData = store(formData);

    return formData;
  }

  @Override
  public CourseControlFormData load(CourseControlFormData formData) throws ProcessingException {
    if (!ACCESS.check(new ReadCourseControlPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    JPAUtility.select("SELECT " +
        "CC.courseNr, " +
        "CC.controlNr, " +
        "CC.sortcode, " +
        "CC.forkTypeUid, " +
        "CC.forkMasterCourseControlNr, " +
        "CC.forkVariantCode, " +
        "CC.countLeg, " +
        "CC.mandatory, " +
        "C.eventNr " +
        "FROM RtCourseControl CC " +
        "INNER JOIN CC.rtCourse C " +
        "WHERE CC.id.courseControlNr = :courseControlNr " +
        "AND CC.id.clientNr = :sessionClientNr " +
        "INTO " +
        ":course, " +
        ":control, " +
        ":sortCode, " +
        ":forkType, " +
        ":forkMasterCourseControl, " +
        ":forkVariantCode, " +
        ":countLeg, " +
        ":mandatory, " +
        ":eventNr ",
        formData);

    return formData;
  }

  @Override
  public CourseControlFormData store(CourseControlFormData formData) throws ProcessingException {
    if (!ACCESS.check(new UpdateCourseControlPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    formData.getCountLeg().setValue(BooleanUtility.nvl(formData.getCountLeg().getValue()));
    formData.getMandatory().setValue(BooleanUtility.nvl(formData.getMandatory().getValue()));
    formData.getSortCode().setValue(NumberUtility.nvl(formData.getSortCode().getValue(), 0));
    String queryString = "UPDATE RtCourseControl SET " +
        "sortcode = :sortCode," +
        "controlNr = :control," +
        "courseNr = :course," +
        "forkTypeUid = :forkType," +
        "forkMasterCourseControlNr = :forkMasterCourseControl," +
        "forkVariantCode = :forkVariantCode," +
        "countLeg = :countLeg, " +
        "mandatory = :mandatory " +
        "WHERE id.courseControlNr = :courseControlNr " +
        "AND id.clientNr = :sessionClientNr ";
    FMilaQuery query = JPA.createQuery(queryString);
    JPAUtility.setAutoParameters(query, queryString, formData);
    query.executeUpdate();

    try {
      List<CourseControlRowData> definitions = BEANS.get(IEventsOutlineService.class).getCourseControlTableData(formData.getCourse().getValue());
      CourseCalculator.calculateCourse(definitions);
    }
    catch (Exception e) {
      throw new VetoException(TEXTS.get("CourseControlInvalidMessage") +
          FMilaUtility.LINE_SEPARATOR +
          FMilaUtility.LINE_SEPARATOR +
          " (" + e.getLocalizedMessage() + ")", e);
    }

    return formData;
  }

  @Override
  public CourseControlFormData find(Long courseNr, Long controlNr, Long sortCode) throws ProcessingException {
    CourseControlFormData formData = new CourseControlFormData();
    formData.getControl().setValue(controlNr);
    formData.getSortCode().setValue(sortCode);
    formData.getCourse().setValue(courseNr);
    String queryString = "SELECT MAX(id.courseControlNr) " +
        "FROM RtCourseControl " +
        "WHERE controlNr = :control " +
        "AND sortcode = :sortCode " +
        "AND courseNr = :course ";
    FMilaTypedQuery<Long> query = JPA.createQuery(queryString, Long.class);
    query.setParameter("control", formData.getControl().getValue());
    query.setParameter("sortCode", formData.getSortCode().getValue());
    query.setParameter("course", formData.getCourse().getValue());
    Long courseControlNr = query.getSingleResult();
    formData.setCourseControlNr(courseControlNr);

    if (formData.getCourseControlNr() != null) {
      formData = load(formData);
    }
    return formData;
  }

  @Override
  public void delete(CourseControlFormData formData) throws ProcessingException {
    if (!ACCESS.check(new DeleteCourseControlPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }
    if (formData == null || formData.getCourseControlNr() == null) {
      return;
    }

    String queryString = "DELETE FROM RtCourseControl " +
        "WHERE id.courseControlNr = :courseControlNr " +
        "AND id.clientNr = :sessionClientNr ";
    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("courseControlNr", formData.getCourseControlNr());
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    query.executeUpdate();
  }

  @Override
  public List<List<CourseControlRowData>> getCourses(Long courseNr) throws ProcessingException {
    List<CourseControlRowData> definitions = BEANS.get(IEventsOutlineService.class).getCourseControlTableData(courseNr);
    return CourseCalculator.calculateCourse(definitions);
  }

}
