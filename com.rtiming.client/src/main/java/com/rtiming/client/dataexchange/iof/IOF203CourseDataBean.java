package com.rtiming.client.dataexchange.iof;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.scout.commons.CompareUtility;
import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.exception.VetoException;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.client.ClientSession;
import com.rtiming.shared.common.database.sql.BeanUtility;
import com.rtiming.shared.common.database.sql.EventBean;
import com.rtiming.shared.dataexchange.AbstractXMLDataBean;
import com.rtiming.shared.dataexchange.DataExchangeUtility;
import com.rtiming.shared.dataexchange.IProgressMonitor;
import com.rtiming.shared.dataexchange.iof203.xml.ClassShortName;
import com.rtiming.shared.dataexchange.iof203.xml.Control;
import com.rtiming.shared.dataexchange.iof203.xml.ControlCode;
import com.rtiming.shared.dataexchange.iof203.xml.Course;
import com.rtiming.shared.dataexchange.iof203.xml.CourseControl;
import com.rtiming.shared.dataexchange.iof203.xml.CourseVariation;
import com.rtiming.shared.dataexchange.iof203.xml.FinishPoint;
import com.rtiming.shared.dataexchange.iof203.xml.FinishPointCode;
import com.rtiming.shared.dataexchange.iof203.xml.Map;
import com.rtiming.shared.dataexchange.iof203.xml.MapPosition;
import com.rtiming.shared.dataexchange.iof203.xml.StartPoint;
import com.rtiming.shared.dataexchange.iof203.xml.StartPointCode;
import com.rtiming.shared.event.EventClassFormData;
import com.rtiming.shared.event.IEventClassProcessService;
import com.rtiming.shared.event.IEventProcessService;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.event.course.ClassTypeCodeType;
import com.rtiming.shared.event.course.ControlFormData;
import com.rtiming.shared.event.course.ControlTypeCodeType;
import com.rtiming.shared.event.course.CourseControlFormData;
import com.rtiming.shared.event.course.CourseFormData;
import com.rtiming.shared.event.course.IControlProcessService;
import com.rtiming.shared.event.course.ICourseControlProcessService;
import com.rtiming.shared.event.course.ICourseProcessService;
import com.rtiming.shared.map.IMapProcessService;
import com.rtiming.shared.map.MapFormData;
import com.rtiming.shared.race.TimePrecisionCodeType;
import com.rtiming.shared.settings.CodeFormData;
import com.rtiming.shared.settings.ICodeProcessService;

public class IOF203CourseDataBean extends AbstractXMLDataBean {

  private static final long serialVersionUID = 1L;
  private final HashMap<String, ControlFormData> controls = new HashMap<String, ControlFormData>();
  private final HashSet<CourseControlFormData> courseControls = new HashSet<CourseControlFormData>();

  private final Long eventNr;

  private List<StartPoint> startPointList;
  private List<Control> controlsList;
  private List<FinishPoint> finishPointList;
  private List<Course> courseList;
  private Map map;

  public IOF203CourseDataBean(Long primaryKeyNr, Long eventNr) {
    super(primaryKeyNr);
    this.eventNr = eventNr;
  }

  public void setStartPointList(List<StartPoint> startPointList) {
    this.startPointList = startPointList;
  }

  public void setControlsList(List<Control> controlsList) {
    this.controlsList = controlsList;
  }

  public void setFinishPointList(List<FinishPoint> finishPointList) {
    this.finishPointList = finishPointList;
  }

  public void setCourseList(List<Course> courseList) {
    this.courseList = courseList;
  }

  public void setMap(Map map) {
    this.map = map;
  }

  @Override
  public Object createXMLObject(Object main) throws ProcessingException {
    return null;
  }

  @Override
  public void loadBeanFromDatabase() throws ProcessingException {
  }

  @Override
  public void storeBeanToDatabase(IProgressMonitor monitor) throws ProcessingException {

    // Map
    if (map != null) {
      IMapProcessService mapService = BEANS.get(IMapProcessService.class);
      MapFormData mapFormData = BeanUtility.mapBean2FormData(mapService.findMap(eventNr, ClientSession.get().getSessionClientNr()));
      mapFormData.getScale().setValue(DataExchangeUtility.parseLong(map.getScale()));
      if (map.getMapPosition() != null) {
        MapPosition pos = map.getMapPosition();
        mapFormData.getOriginX().setValue(DataExchangeUtility.parseDouble(pos.getX()));
        mapFormData.getOriginY().setValue(DataExchangeUtility.parseDouble(pos.getY()));
      }
      if (mapFormData.getMapKey().getId() == null) {
        EventBean event = new EventBean();
        event.setEventNr(eventNr);
        event = BEANS.get(IEventProcessService.class).load(event);
        if (!StringUtility.isNullOrEmpty(event.getMap())) {
          mapFormData.getName().setValue(event.getMap());
        }
        else {
          mapFormData.getName().setValue(event.getName());
        }
        mapFormData.getName().setValue(StringUtility.substring(mapFormData.getName().getValue() + " (" + "Imported by IOF XML" + ")", 0, 250));
        mapFormData.setNewEventNr(eventNr);
        mapService.create(BeanUtility.mapFormData2bean(mapFormData));
      }
      else {
        mapService.store(BeanUtility.mapFormData2bean(mapFormData));
      }
      monitor.addInfo(TEXTS.get("Map"), mapFormData.getName().getValue());
    }

    // Start
    for (StartPoint start : startPointList) {
      insertControlCache(start.getStartPointCode().getvalue(), ControlTypeCodeType.StartCode.ID, start.getMapPosition(), monitor);
    }
    int total = startPointList.size();
    monitor.update(total, total);

    // Controls
    for (Control control : controlsList) {
      insertControlCache(control.getControlCode().getvalue(), ControlTypeCodeType.ControlCode.ID, control.getMapPosition(), monitor);
    }
    total += controlsList.size();
    monitor.update(total, total);

    // Finish
    for (FinishPoint finish : finishPointList) {
      insertControlCache(finish.getFinishPointCode().getvalue(), ControlTypeCodeType.FinishCode.ID, finish.getMapPosition(), monitor);
    }
    total += finishPointList.size();
    monitor.update(total, total);

    // Courses
    for (Course impCourse : courseList) {

      // Course
      ICourseProcessService courseService = BEANS.get(ICourseProcessService.class);
      CourseFormData course = courseService.find(impCourse.getCourseName(), eventNr);
      if (course.getCourseNr() == null) {
        course = courseService.create(course);
      }
      else {
        // delete previously imported course controls
        BEANS.get(ICourseProcessService.class).delete(course, false);
      }

      // Course Variation
      for (CourseVariation variation : impCourse.getCourseVariation()) {
        long lastControlSortCode = -1;

        Long variationNr = 0L; // variation support only in 3.0.0 iof interface
        if (variation.getCourseLength() != null) {
          course.getLength().setValue(DataExchangeUtility.parseLongEx(variation.getCourseLength().getvalue()));
        }
        if (variation.getCourseClimb() != null) {
          course.getClimb().setValue(DataExchangeUtility.parseLongEx(variation.getCourseClimb().getvalue()));
        }

        // Start
        for (Object startPointCodeOrStartPoint : variation.getStartPointCodeOrStartPoint()) {
          if (startPointCodeOrStartPoint instanceof StartPointCode) {
            StartPointCode controlCode = (StartPointCode) startPointCodeOrStartPoint;
            lastControlSortCode++;
            createCourseControl(controlCode.getvalue(), course.getCourseNr(), variationNr, ControlTypeCodeType.StartCode.ID, lastControlSortCode, monitor);
          }
        }

        // Course Control
        for (CourseControl courseControl : variation.getCourseControl()) {
          Long sequenceNr = DataExchangeUtility.parseLongEx(courseControl.getSequence());
          if (sequenceNr != null) {
            lastControlSortCode = sequenceNr;
          }
          else {
            lastControlSortCode++;
          }
          for (Object controlCodeOrControl : courseControl.getControlCodeOrControl()) {
            if (controlCodeOrControl instanceof ControlCode) {
              ControlCode controlCode = (ControlCode) controlCodeOrControl;
              createCourseControl(controlCode.getvalue(), course.getCourseNr(), variationNr, ControlTypeCodeType.ControlCode.ID, sequenceNr, monitor);
            }
          }
        }

        // Finish
        for (Object finishPointCodeOrFinishPoint : variation.getFinishPointCodeOrFinishPoint()) {
          if (finishPointCodeOrFinishPoint instanceof FinishPointCode) {
            FinishPointCode controlCode = (FinishPointCode) finishPointCodeOrFinishPoint;
            lastControlSortCode++;
            createCourseControl(controlCode.getvalue(), course.getCourseNr(), variationNr, ControlTypeCodeType.FinishCode.ID, lastControlSortCode, monitor);
          }
        }

      }

      // Class
      for (Object classIdOrClassShortNameObj : impCourse.getClassIdOrClassShortName()) {
        if (classIdOrClassShortNameObj instanceof ClassShortName) {
          ClassShortName className = (ClassShortName) classIdOrClassShortNameObj;
          ICodeProcessService codeService = BEANS.get(ICodeProcessService.class);
          CodeFormData code = codeService.find(className.getvalue(), ClassCodeType.ID);
          if (code.getCodeUid() == null) {
            code = codeService.create(code);
          }

          // Event - Class
          IEventClassProcessService eventClassService = BEANS.get(IEventClassProcessService.class);

          EventClassFormData eventClass = new EventClassFormData();
          eventClass.getEvent().setValue(eventNr);
          eventClass.getClazz().setValue(code.getCodeUid());

          eventClass = eventClassService.load(eventClass);
          eventClass.getCourse().setValue(course.getCourseNr());
          eventClass.getType().setValue(ClassTypeCodeType.IndividualEventCode.ID); // no relay support in 2.0.3
          eventClass.getTimePrecision().setValue(TimePrecisionCodeType.Precision1sCode.ID);

          try {
            eventClassService.create(eventClass);
          }
          catch (VetoException e) {
            eventClassService.store(eventClass);
          }
        }
      } // class

      BEANS.get(ICourseProcessService.class).store(course);

    } // Course

    // Save
    for (Entry<String, ControlFormData> control : controls.entrySet()) {
      if (control.getValue().getControlNr() == null) {
        control.setValue(BEANS.get(IControlProcessService.class).create(control.getValue()));
      }
      else {
        BEANS.get(IControlProcessService.class).store(control.getValue());
      }
      try {
        BEANS.get(IControlProcessService.class).georeferenceFromLocalPosition(new Long[]{control.getValue().getControlNr()}, eventNr);
      }
      catch (VetoException e) {
        // nop
      }
    }

    for (CourseControlFormData courseControl : courseControls) {
      if (courseControl.getCourseControlNr() == null) {
        BEANS.get(ICourseControlProcessService.class).create(courseControl);
      }
      else {
        BEANS.get(ICourseControlProcessService.class).store(courseControl);
      }
    }
    total += courseList.size();
    monitor.update(total, total);

  }

  private void insertControlCache(String controlNo, long controlTypeUid, MapPosition position, IProgressMonitor monitor) throws ProcessingException {
    if (!(StringUtility.isNullOrEmpty(controlNo))) {
      IControlProcessService controlService = BEANS.get(IControlProcessService.class);
      controlNo = controlNo.trim().toUpperCase();

      // try in cache
      ControlFormData control = controls.get(controlNo);
      if (control == null) {
        // otherwise try database
        control = controlService.find(controlNo, eventNr);
        if (control.getControlNr() != null || CompareUtility.notEquals(control.getType().getValue(), controlTypeUid)) {
          control.getType().setValue(controlTypeUid);
          control.getActive().setValue(true);
          control = controlService.store(control);
        }
      }

      // write attributes
      control.getType().setValue(controlTypeUid);
      if (position != null && !StringUtility.isNullOrEmpty(position.getX())) {
        control.getPositionX().setValue(Double.parseDouble(position.getX()));
      }
      if (position != null && !StringUtility.isNullOrEmpty(position.getY())) {
        control.getPositionY().setValue(Double.parseDouble(position.getY()));
      }

      // put/replace in cache
      controls.put(control.getNumber().getValue(), control);
      monitor.addInfo(TEXTS.get("Control"), control.getNumber().getValue());
    }
  }

  private void createCourseControl(String controlNo, long courseNr, Long variationUid, long controlTypeUid, Long sortcode, IProgressMonitor monitor) throws ProcessingException {
    ICourseControlProcessService courseControlService = BEANS.get(ICourseControlProcessService.class);
    IControlProcessService controlService = BEANS.get(IControlProcessService.class);
    controlNo = controlNo.trim().toUpperCase();

    // try in cache
    ControlFormData control = controls.get(controlNo);
    if (control == null) {
      // otherwise try database
      control = controlService.find(controlNo, eventNr);
      if (control.getControlNr() != null || CompareUtility.notEquals(control.getType().getValue(), controlTypeUid)) {
        control.getType().setValue(controlTypeUid);
        control.getActive().setValue(true);
        control = controlService.store(control);
      }
    }

    // failsafe, if control does not exist, create
    if (control.getControlNr() == null) {
      insertControlCache(controlNo, controlTypeUid, null, monitor);
      control = controls.get(controlNo); // fetch object with PK
    }
    if (control.getControlNr() == null) {
      control = BEANS.get(IControlProcessService.class).create(control);
    }
    controls.put(control.getNumber().getValue(), control);

    CourseControlFormData courseControl = courseControlService.find(courseNr, control.getControlNr(), sortcode);
    courseControl.getCountLeg().setValue(true);
    courseControl.getMandatory().setValue(true);
    courseControl.getSortCode().setValue(sortcode);
    courseControls.add(courseControl);
  }

  @Override
  public ArrayList<String> getPreviewRowData() throws ProcessingException {
    ArrayList<String> list = new ArrayList<String>();
    list.add(String.valueOf(startPointList.size()));
    list.add(String.valueOf(controlsList.size()));
    list.add(String.valueOf(finishPointList.size()));
    list.add(String.valueOf(courseList.size()));
    return list;
  }

}
