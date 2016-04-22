package com.rtiming.client.event.course.loop;

import java.util.ArrayList;
import java.util.List;

import com.rtiming.client.test.data.ControlTestData;
import com.rtiming.shared.event.course.ControlTypeCodeType;

/**
 * @author amo
 */
public class CourseLoopCalculatorTestUtility {

  public static List<ControlTestData> buildCourse(ControlTestData... controls) {
    List<ControlTestData> list = new ArrayList<>();
    for (ControlTestData control : controls) {
      list.add(control);
    }
    return list;
  }

  public static ControlTestData buildPunch(String controlNo) {
    return buildControl(controlNo, null, null, null, null, null);
  }

  public static ControlTestData buildControl(String controlNo, Long sortCode, Long forkTypeUid, String loopMasterControlNo, Long loopMasterSortCode, String loopVariantCode) {
    return new ControlTestData(controlNo, sortCode, ControlTypeCodeType.ControlCode.ID, forkTypeUid, loopMasterControlNo, loopMasterSortCode, loopVariantCode);
  }

}
