package com.rtiming.client.dataexchange.iof3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.client.ClientSession;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.common.database.sql.BeanUtility;
import com.rtiming.shared.common.database.sql.EventBean;
import com.rtiming.shared.dataexchange.AbstractXMLDataBean;
import com.rtiming.shared.dataexchange.IProgressMonitor;
import com.rtiming.shared.dataexchange.iof3.IOF300Utility;
import com.rtiming.shared.dataexchange.iof3.xml.ClassCourseAssignment;
import com.rtiming.shared.dataexchange.iof3.xml.Control;
import com.rtiming.shared.dataexchange.iof3.xml.ControlType;
import com.rtiming.shared.dataexchange.iof3.xml.Course;
import com.rtiming.shared.dataexchange.iof3.xml.CourseControl;
import com.rtiming.shared.dataexchange.iof3.xml.CourseData;
import com.rtiming.shared.dataexchange.iof3.xml.GeoPosition;
import com.rtiming.shared.dataexchange.iof3.xml.Map;
import com.rtiming.shared.dataexchange.iof3.xml.MapPosition;
import com.rtiming.shared.dataexchange.iof3.xml.RaceCourseData;
import com.rtiming.shared.event.EventClassFormData;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.event.course.ControlFormData;
import com.rtiming.shared.event.course.CourseControlFormData;
import com.rtiming.shared.event.course.CourseFormData;
import com.rtiming.shared.event.course.IControlProcessService;
import com.rtiming.shared.event.course.ICourseControlProcessService;
import com.rtiming.shared.map.IMapProcessService;
import com.rtiming.shared.map.MapFormData;
import com.rtiming.shared.services.code.CourseForkTypeCodeType;
import com.rtiming.shared.settings.CodeFormData;

public class IOF300CourseDataBean extends AbstractXMLDataBean {

  private static final long serialVersionUID = 1L;
  private final java.util.Map<String, ControlFormData> controls = new LinkedHashMap<String, ControlFormData>();
  private final Set<CourseControlFormData> courseControls = new LinkedHashSet<CourseControlFormData>();

  private final CourseDataAccess access;
  private CourseData courseData;

  private Long controlCounter;
  private Long courseCounter;
  private Long classCounter;
  private Long mapCounter;

  public IOF300CourseDataBean(Long primaryKeyNr, CourseDataAccess access) {
    super(primaryKeyNr);
    this.access = access;
  }

  public void setCourseData(CourseData courseData) {
    this.courseData = courseData;
    if (courseData != null) {
      for (RaceCourseData raceCourseData : courseData.getRaceCourseData()) {
        controlCounter = NumberUtility.toLong(raceCourseData.getControl().size());
        courseCounter = NumberUtility.toLong(raceCourseData.getCourse().size());
        classCounter = NumberUtility.toLong(raceCourseData.getClassCourseAssignment().size());
        mapCounter = NumberUtility.toLong(raceCourseData.getMap().size());
      }
    }
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
    for (RaceCourseData raceCourseData : courseData.getRaceCourseData()) {
      // controls
      for (Control control : raceCourseData.getControl()) {
        importControl(control, monitor);
      }

      // course families
      java.util.Map<String, List<Course>> familyVariations = new HashMap<>();
      for (Course course : raceCourseData.getCourse()) {
        List<Course> courses = familyVariations.get(course.getCourseFamily());
        if (courses == null) {
          courses = new ArrayList<>();
          familyVariations.put(course.getCourseFamily(), courses);
        }
        courses.add(course);
      }
      for (List<Course> entry : familyVariations.values()) {
        CourseFormData course = importCourse(entry, monitor);
        access.storeCourse(course);
      }

      // maps
      for (com.rtiming.shared.dataexchange.iof3.xml.Map map : raceCourseData.getMap()) {
        importMap(map, monitor);
      }

      // classes
      importClasses(raceCourseData.getClassCourseAssignment(), monitor);
    }

    // Save
    java.util.Map<Long, ControlFormData> controlNrLookup = new HashMap<>();
    for (Entry<String, ControlFormData> control : controls.entrySet()) {
      ControlFormData controlFormData = null;
      if (control.getValue().getControlNr() == null) {
        controlFormData = control.setValue(BEANS.get(IControlProcessService.class).create(control.getValue()));
        monitor.addInfoCreated(TEXTS.get("Control"), control.getValue().getNumber().getValue());
      }
      else {
        controlFormData = access.storeControl(control.getValue());
        monitor.addInfoUpdated(TEXTS.get("Control"), control.getValue().getNumber().getValue());
      }
      try {
        access.georeferenceControlFromLocalPosition(new Long[]{control.getValue().getControlNr()});
      }
      catch (VetoException e) {
        // nop
      }
      controlNrLookup.put(controlFormData.getControlNr(), controlFormData);
    }

    for (CourseControlFormData courseControl : courseControls) {
      if (courseControl.getCourseControlNr() == null) {
        BEANS.get(ICourseControlProcessService.class).create(courseControl);
        monitor.addInfoCreated(TEXTS.get("CourseControl"), controlNrLookup.get(courseControl.getControl().getValue()).getNumber().getValue());
      }
      else {
        BEANS.get(ICourseControlProcessService.class).store(courseControl);
        monitor.addInfoUpdated(TEXTS.get("CourseControl"), controlNrLookup.get(courseControl.getControl().getValue()).getNumber().getValue());
      }
    }
  }

  protected void importClasses(List<ClassCourseAssignment> classesList, IProgressMonitor monitor) throws ProcessingException {
    if (classesList == null) {
      return;
    }
    for (ClassCourseAssignment classCourse : classesList) {
      EventClassFormData eventClass = importClass(classCourse);

      try {
        access.createEventClass(eventClass);
        monitor.addInfoCreated(TEXTS.get("Class"), FMilaUtility.getCodeText(ClassCodeType.class, eventClass.getClazz().getValue()));
      }
      catch (Exception e) {
        access.storeEventClass(eventClass);
        monitor.addInfoUpdated(TEXTS.get("Class"), FMilaUtility.getCodeText(ClassCodeType.class, eventClass.getClazz().getValue()));
      }

    }
  }

  protected EventClassFormData importClass(ClassCourseAssignment classCourse) throws ProcessingException {
    String className = classCourse.getClassName();
    CourseFormData course = access.findCourse(classCourse.getCourseFamily());
    CodeFormData code = access.findClass(className);
    if (code.getCodeUid() == null) {
      code = access.createCode(code);
    }

    // Event - Class
    EventClassFormData eventClass = new EventClassFormData();
    eventClass.getEvent().setValue(access.getEventNr());
    eventClass.getClazz().setValue(code.getCodeUid());
    eventClass = access.loadEventClass(eventClass);

    if (eventClass.getType().getValue() == null) {
      eventClass = access.prepareCreateEventClass(eventClass);
    }
    eventClass.getCourse().setValue(course.getCourseNr());
    eventClass.getEvent().setValue(access.getEventNr());
    eventClass.getClazz().setValue(code.getCodeUid());
    return eventClass;
  }

  private void importMap(Map map, IProgressMonitor monitor) throws ProcessingException {
    IMapProcessService mapService = BEANS.get(IMapProcessService.class);
    MapFormData mapFormData = access.findMap(ClientSession.get().getSessionClientNr());
    mapFormData.getScale().setValue(NumberUtility.toLong(map.getScale()));
    if (map.getMapPositionTopLeft() != null) {
      MapPosition pos = map.getMapPositionTopLeft();
      mapFormData.getOriginX().setValue(NumberUtility.toBigDecimal(pos.getX()));
      mapFormData.getOriginY().setValue(NumberUtility.toBigDecimal(pos.getY()));
    }
    if (mapFormData.getMapKey().getId() == null) {
      EventBean event = access.loadEvent();
      if (!StringUtility.isNullOrEmpty(event.getMap())) {
        mapFormData.getName().setValue(event.getMap());
      }
      else {
        mapFormData.getName().setValue(event.getName());
      }
      mapFormData.getName().setValue(StringUtility.substring(mapFormData.getName().getValue() + " (" + "Imported by IOF XML" + ")", 0, 250));
      mapFormData.setNewEventNr(access.getEventNr());
      mapService.create(BeanUtility.mapFormData2bean(mapFormData));
    }
    else {
      mapService.store(BeanUtility.mapFormData2bean(mapFormData));
      monitor.addInfoCreated(TEXTS.get("Map"), mapFormData.getName().getValue());
    }
  }

  protected void importControl(Control control, IProgressMonitor monitor) throws ProcessingException {
    Long typeUid = IOF300Utility.convertFromXmlControlType(control.getType());
    if (typeUid == null) {
      return;
    }

    insertControlCache(control.getId().getValue(), typeUid, control.getPosition(), controls, monitor);
  }

  protected CourseFormData importCourse(List<Course> impCourseList, IProgressMonitor monitor) throws ProcessingException {
    if (impCourseList == null || impCourseList.size() == 0) {
      return null;
    }

    Course firstCourse = impCourseList.get(0);

    // Course
    CourseFormData course = access.findCourse(firstCourse.getCourseFamily());
    if (course.getCourseNr() == null) {
      course = access.createCourse(course);
      monitor.addInfoCreated(TEXTS.get("Course"), course.getShortcut().getValue());
    }
    else {
      // delete previously imported course controls
      access.deleteCourse(course, false);
      monitor.addInfoDeleted(TEXTS.get("CourseControl"), course.getShortcut().getValue());
    }

    long lastControlSortCode = -1;

    if (firstCourse.getLength() != null) {
      course.getLength().setValue(NumberUtility.toLong(firstCourse.getLength()));
    }
    if (firstCourse.getClimb() != null) {
      course.getClimb().setValue(NumberUtility.toLong(firstCourse.getClimb()));
    }

    // Course Control
    Long startMaster = null;
    for (Course impCourse : impCourseList) {
      for (CourseControl courseControl : impCourse.getCourseControl()) {
        lastControlSortCode++;
        Long typeUid = IOF300Utility.convertFromXmlControlType(courseControl.getType());
        if (typeUid != null) {
          for (String controlCode : courseControl.getControl()) {
            if (ControlType.START.equals(courseControl.getType()) && startMaster != null) {
              continue;
            }

            ImportedCourseControl control = new ImportedCourseControl();
            control.setControlNo(controlCode);
            control.setCourseNr(course.getCourseNr());
            boolean isForkedCourse = impCourseList.size() > 1;
            if (isForkedCourse) {
              control.setForkVariantCode(impCourse.getName());
              control.setForkMasterCourseControlNr(startMaster);
            }
            if (startMaster == null && isForkedCourse) {
              control.setForkTypeUid(CourseForkTypeCodeType.ForkCode.ID);
              control.setForkVariantCode(null);
              control.setForkMasterCourseControlNr(null);
            }
            control.setTypeUid(typeUid);
            control.setSortCode(lastControlSortCode);
            CourseControlFormData courseControlFormData = importCourseControl(control, controls, monitor);

            // start master
            if (startMaster == null && impCourseList.size() > 0) {
              if (courseControlFormData.getCourseControlNr() == null) {
                courseControlFormData = access.createCourseControl(courseControlFormData);
                monitor.addInfoCreated(TEXTS.get("CourseControl") + " (" + TEXTS.get("ForkStart") + ")", control.getControlNo());
              }
              startMaster = courseControlFormData.getCourseControlNr();
            }

            courseControls.add(courseControlFormData);
          }
        }
      }
    }

    return course;
  }

  private void insertControlCache(String controlNo, Long controlTypeUid, GeoPosition position, java.util.Map<String, ControlFormData> controlCache, IProgressMonitor monitor) throws ProcessingException {
    if (!(StringUtility.isNullOrEmpty(controlNo))) {
      controlNo = controlNo.trim().toUpperCase();

      // try in cache
      ControlFormData control = controlCache.get(controlNo);
      if (control == null) {
        // otherwise try database
        control = access.findControl(controlNo);
        if (control.getControlNr() != null || CompareUtility.notEquals(control.getType().getValue(), controlTypeUid)) {
          control.getType().setValue(controlTypeUid);
          control.getActive().setValue(true);
          control = access.storeControl(control);
        }
      }

      // write attributes
      control.getType().setValue(controlTypeUid);
      if (position != null) {
        control.getPositionX().setValue(NumberUtility.toBigDecimal(position.getLat()));
        control.getPositionY().setValue(NumberUtility.toBigDecimal(position.getLng()));
      }

      // put/replace in cache
      controlCache.put(controlNo, control);
    }
  }

  protected CourseControlFormData importCourseControl(ImportedCourseControl impControl, java.util.Map<String, ControlFormData> controlCache, IProgressMonitor monitor) throws ProcessingException {
    if (impControl == null || impControl.getControlNo() == null) {
      return null;
    }
    String controlNo = impControl.getControlNo().trim().toUpperCase();

    // try in cache
    ControlFormData control = controlCache.get(controlNo);
    if (control == null || CompareUtility.notEquals(control.getType().getValue(), impControl.getTypeUid())) {
      // otherwise try database
      control = access.findControl(controlNo);
      if (control.getControlNr() != null || CompareUtility.notEquals(control.getType().getValue(), impControl.getTypeUid())) {
        control.getType().setValue(impControl.getTypeUid());
        control.getActive().setValue(true);
        control = access.storeControl(control);
      }
    }

    // failsafe, if control does not exist, create
    if (control.getControlNr() == null) {
      insertControlCache(controlNo, impControl.getTypeUid(), null, controlCache, monitor);
      control = controlCache.get(controlNo); // fetch object with PK
    }
    if (control.getControlNr() == null) {
      control = access.createControl(control);
    }
    controlCache.put(controlNo, control);

    CourseControlFormData courseControl = access.findCourseControl(impControl.getCourseNr(), control.getControlNr(), impControl.getSortCode());
    courseControl.getCountLeg().setValue(true);
    courseControl.getMandatory().setValue(true);
    courseControl.getSortCode().setValue(impControl.getSortCode());
    courseControl.getForkVariantCode().setValue(impControl.getForkVariantCode());
    courseControl.getForkMasterCourseControl().setValue(impControl.getForkMasterCourseControlNr());
    courseControl.getForkType().setValue(impControl.getForkTypeUid());

    return courseControl;
  }

  @Override
  public ArrayList<String> getPreviewRowData() throws ProcessingException {
    ArrayList<String> list = new ArrayList<String>();
    list.add(String.valueOf(NumberUtility.nvl(controlCounter, 0L)));
    list.add(String.valueOf(NumberUtility.nvl(courseCounter, 0L)));
    list.add(String.valueOf(NumberUtility.nvl(classCounter, 0L)));
    list.add(String.valueOf(NumberUtility.nvl(mapCounter, 0L)));
    return list;
  }

  protected class ImportedCourseControl {

    private String controlNo;
    private Long courseNr;
    private Long typeUid;
    private String forkVariantCode;
    private Long sortCode;
    private Long forkTypeUid;
    private Long forkMasterCourseControlNr;

    public String getControlNo() {
      return controlNo;
    }

    public void setControlNo(String controlNo) {
      this.controlNo = controlNo;
    }

    public Long getCourseNr() {
      return courseNr;
    }

    public void setCourseNr(Long courseNr) {
      this.courseNr = courseNr;
    }

    public Long getTypeUid() {
      return typeUid;
    }

    public void setTypeUid(Long typeUid) {
      this.typeUid = typeUid;
    }

    public String getForkVariantCode() {
      return forkVariantCode;
    }

    public void setForkVariantCode(String forkVariantCode) {
      this.forkVariantCode = forkVariantCode;
    }

    public Long getForkTypeUid() {
      return forkTypeUid;
    }

    public void setForkTypeUid(Long forkTypeUid) {
      this.forkTypeUid = forkTypeUid;
    }

    public Long getForkMasterCourseControlNr() {
      return forkMasterCourseControlNr;
    }

    public void setForkMasterCourseControlNr(Long forkMasterCourseControlNr) {
      this.forkMasterCourseControlNr = forkMasterCourseControlNr;
    }

    public Long getSortCode() {
      return sortCode;
    }

    public void setSortCode(Long sortCode) {
      this.sortCode = sortCode;
    }

  }

}
