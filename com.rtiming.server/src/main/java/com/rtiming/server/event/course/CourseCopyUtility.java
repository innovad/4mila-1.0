package com.rtiming.server.event.course;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.commons.CompareUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.exception.VetoException;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.shared.event.course.CourseControlFormData;
import com.rtiming.shared.event.course.CourseControlRowData;

/**
 * @since 1.0.7
 */
public class CourseCopyUtility {

  private CourseCopyUtility() {
  }

  public static CourseControlRowData getFirstControlOfFirstCourse(List<List<CourseControlRowData>> list) throws ProcessingException {
    if (list == null || list.isEmpty() || list.get(0).isEmpty()) {
      throw new VetoException(TEXTS.get("NewCourseWithSelectedVariantsErrorMessage"));
    }
    return list.get(0).get(0);
  }

  public static List<CourseControlFormData> createVariantsFromAllCourses(Long eventNr, Long courseNr, Long startCourseControlNr, Long startControlNr, List<List<CourseControlRowData>> allCourseControlVariants) throws VetoException {
    if (allCourseControlVariants == null) {
      throw new IllegalArgumentException("Mandatory arguments missing");
    }
    long sortCode = 2; // start is already 1
    long variantCode = 1;
    List<CourseControlFormData> newControls = new ArrayList<>();

    for (List<CourseControlRowData> variant : allCourseControlVariants) {
      // make sure first control is start
      if (variant.size() <= 0) {
        throw new VetoException(TEXTS.get("NewCourseWithSelectedVariantsErrorMessage"));
      }
      CourseControlRowData currentStart = variant.get(0);
      if (CompareUtility.notEquals(startControlNr, currentStart.getControlNr())) {
        throw new VetoException(TEXTS.get("NewCourseWithSelectedVariantsErrorMessage"));
      }
      variant.remove(0);
      // insert all controls (with master and variant code)
      // ignore existing variants
      for (CourseControlRowData control : variant) {
        CourseControlFormData controlFormData = row2formData(control, eventNr, courseNr, null, startCourseControlNr, "" + variantCode, sortCode);
        newControls.add(controlFormData);
        sortCode++;
      }
      variantCode++;
    }
    return newControls;
  }

  public static CourseControlFormData row2formData(CourseControlRowData row, Long eventNr, Long courseNr, Long forkTypeUid, Long masterCourseControlNr, String variant, Long sortCode) {
    CourseControlFormData control = new CourseControlFormData();
    control.setEventNr(eventNr);
    control.getCourse().setValue(courseNr);
    control.getControl().setValue(row.getControlNr());
    control.getCountLeg().setValue(row.isCountLeg());
    control.getMandatory().setValue(row.isMandatory());
    control.getForkType().setValue(forkTypeUid);
    control.getForkMasterCourseControl().setValue(masterCourseControlNr);
    control.getForkVariantCode().setValue(variant);
    control.getSortCode().setValue(sortCode);
    return control;
  }

}
