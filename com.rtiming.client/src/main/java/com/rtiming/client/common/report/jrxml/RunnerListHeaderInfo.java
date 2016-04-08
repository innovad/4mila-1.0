package com.rtiming.client.common.report.jrxml;

import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.NumberUtility;

import com.rtiming.client.ClientSession;
import com.rtiming.shared.Texts;
import com.rtiming.shared.club.IClubProcessService;
import com.rtiming.shared.common.database.sql.ClubBean;
import com.rtiming.shared.event.EventClassFormData;
import com.rtiming.shared.event.IEventClassProcessService;
import com.rtiming.shared.event.course.CourseFormData;
import com.rtiming.shared.event.course.ICourseProcessService;
import com.rtiming.shared.settings.CodeFormData;
import com.rtiming.shared.settings.ICodeProcessService;

public class RunnerListHeaderInfo {

  private final Long eventNr;
  private final Long classUid;
  private Long courseNr;
  private final Long clubNr;

  private String listName = new String();
  private String listShortcut = new String();

  private String courseInfo = new String();

  public RunnerListHeaderInfo(Long eventNr, Long classUid, Long courseNr, Long clubNr) throws ProcessingException {
    super();
    this.eventNr = eventNr;
    this.classUid = classUid;
    this.courseNr = courseNr;
    this.clubNr = clubNr;

    init();
  }

  private void init() throws ProcessingException {
    // load data

    // class data
    if (classUid != null) {
      listName = BEANS.get(ICodeProcessService.class).getTranslation(classUid, ClientSession.get().getSessionClientNr());

      CodeFormData clazz = new CodeFormData();
      clazz.setCodeUid(classUid);
      clazz = BEANS.get(ICodeProcessService.class).load(clazz);
      listShortcut = clazz.getMainBox().getShortcut().getValue();

      EventClassFormData eventClass = new EventClassFormData();
      eventClass.getEvent().setValue(eventNr);
      eventClass.getClazz().setValue(classUid);
      eventClass = BEANS.get(IEventClassProcessService.class).load(eventClass);
      courseNr = eventClass.getCourse().getValue();
    }

    // course data
    if (courseNr != null) {
      CourseFormData course = new CourseFormData();
      course.setCourseNr(courseNr);
      course = BEANS.get(ICourseProcessService.class).load(course);

      String courseClimb = NumberUtility.format(course.getClimb().getValue());
      String courseLength = NumberUtility.format(course.getLength().getValue());
      Long courseControlCount = BEANS.get(ICourseProcessService.class).getControlCount(courseNr);
      courseInfo = getCourseInfo(courseClimb, courseLength, courseControlCount);

      String courseShortcut = course.getShortcut().getValue();
      if (StringUtility.isNullOrEmpty(listName)) {
        listName = courseShortcut;
      }
      if (StringUtility.isNullOrEmpty(listShortcut)) {
        listShortcut = courseShortcut;
      }
    }

    // club data
    if (clubNr != null) {
      ClubBean club = new ClubBean();
      club.setClubNr(clubNr);
      club = BEANS.get(IClubProcessService.class).load(club);
      listName = club.getName();
      listShortcut = club.getName();
    }

  }

  public String getListName() {
    return listName;
  }

  public String getListShortcut() {
    return listShortcut;
  }

  public String getCourseInfo() {
    return courseInfo;
  }

  public Long getCourseNr() {
    return courseNr;
  }

  /**
   * @param climb
   * @param length
   * @param controlCount
   * @return
   */
  protected String getCourseInfo(String climb, String length, Long controlCount) {
    StringBuffer info = new StringBuffer();
    if (!StringUtility.isNullOrEmpty(length)) {
      info = appendSeparator(info);
      info.append(Texts.get("CourseDistanceInfo", length));
    }
    if (!StringUtility.isNullOrEmpty(climb)) {
      info = appendSeparator(info);
      info.append(Texts.get("CourseClimbInfo", climb));
    }
    if (controlCount != 0) {
      info = appendSeparator(info);
      info.append(Texts.get("CourseControlInfo", Long.toString(controlCount)));
    }
    return info.toString();
  }

  protected static StringBuffer appendSeparator(StringBuffer courseInfo) {
    if (!StringUtility.isNullOrEmpty(courseInfo.toString())) {
      courseInfo.append(", ");
    }
    return courseInfo;
  }

}
