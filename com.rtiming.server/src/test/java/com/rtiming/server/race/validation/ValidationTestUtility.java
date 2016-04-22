package com.rtiming.server.race.validation;

import java.util.ArrayList;
import java.util.List;

import com.rtiming.server.race.RaceControlBean;
import com.rtiming.shared.event.course.ControlTypeCodeType;
import com.rtiming.shared.event.course.CourseControlRowData;
import com.rtiming.shared.services.code.CourseForkTypeCodeType;

/**
 * @author amo
 */
public class ValidationTestUtility {

  public static List<RaceControlBean> buildControlList(String... controls) {
    return buildControlList(true, controls);
  }

  public static List<RaceControlBean> buildControlList(boolean generateTime, String... controls) {
    List<RaceControlBean> result = new ArrayList<RaceControlBean>();
    long sort = 0;
    for (String control : controls) {
      RaceControlBean bean = new RaceControlBean();
      bean.setSortcode(sort);
      bean.setControlNo(control);
      bean.setCountLeg(true);
      bean.setMandatory(true);
      if (generateTime) {
        bean.setPunchTime(sort * 10);
      }
      bean.setTypeUid(ControlTypeCodeType.ControlCode.ID);
      result.add(bean);
      sort++;
    }
    return result;
  }

  public static List<RaceControlBean> buildFreeOrderControlList(String... controls) {
    List<RaceControlBean> result = new ArrayList<RaceControlBean>();
    for (String control : controls) {
      RaceControlBean bean = new RaceControlBean();
      bean.setSortcode(10L);
      bean.setControlNo(control);
      bean.setCountLeg(true);
      bean.setMandatory(true);
      bean.setTypeUid(ControlTypeCodeType.ControlCode.ID);
      result.add(bean);
    }
    return result;
  }

  public static List<RaceControlBean> setSortCodes(List<RaceControlBean> planned, Long... sortCodes) {
    int k = 0;
    for (RaceControlBean control : planned) {
      control.setSortcode(sortCodes[k]);
      k++;
    }
    return planned;
  }

  public static CourseControlRowData createControlDef(Long controlNr, Long sortCode, Long masterControlNr, String variant) {
    return createControlDef(controlNr, controlNr, sortCode, masterControlNr, variant);
  }

  public static CourseControlRowData createControlDef(Long courseControlNr, Long controlNr, Long sortCode, Long masterControlNr, String variant) {
    CourseControlRowData control = new CourseControlRowData();
    control.setControlNo(String.valueOf(controlNr));
    control.setCourseControlNr(courseControlNr);
    control.setControlNr(controlNr);
    control.setSortCode(sortCode);
    control.setTypeUid(ControlTypeCodeType.ControlCode.ID);
    control.setForkMasterCourseControlNr(masterControlNr);
    control.setForkTypeUid(CourseForkTypeCodeType.ButterflyCode.ID);
    control.setForkVariantCode(variant);
    control.setMandatory(true);
    control.setCountLeg(true);
    return control;
  }

  public static List<CourseControlRowData> createControlDefList(CourseControlRowData... controls) {
    List<CourseControlRowData> definition = new ArrayList<CourseControlRowData>();
    for (CourseControlRowData control : controls) {
      definition.add(control);
    }
    return definition;
  }

}
