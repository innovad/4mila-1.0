package com.rtiming.client.services.lookup;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.services.lookup.LocalLookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;

import com.rtiming.shared.event.IEventsOutlineService;
import com.rtiming.shared.event.course.CourseControlRowData;

public class CourseMasterCourseControlLookupCall extends LocalLookupCall<Long> {

  private static final long serialVersionUID = 1L;
  private Long courseNr;

  @Override
  protected List<LookupRow<Long>> execCreateLookupRows() throws ProcessingException {
    List<CourseControlRowData> list = BEANS.get(IEventsOutlineService.class).getCourseControlTableData(courseNr);
    ArrayList<LookupRow<Long>> rows = new ArrayList<LookupRow<Long>>();
    for (CourseControlRowData row : list) {
      if (row.getLoopTypeUid() != null) {
        LookupRow r = new LookupRow(row.getCourseControlNr(), row.getControlNo() + " - " + row.getSortCode());
        rows.add(r);
      }
    }
    return rows;
  }

  public void setCourseNr(Long courseNr) {
    this.courseNr = courseNr;
  }

}
