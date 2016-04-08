package com.rtiming.shared.event.course;

import java.util.List;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

@TunnelToServer
public interface ICourseProcessService extends IService {

  CourseFormData prepareCreate(CourseFormData formData) throws ProcessingException;

  CourseFormData create(CourseFormData formData) throws ProcessingException;

  CourseFormData load(CourseFormData formData) throws ProcessingException;

  CourseFormData store(CourseFormData formData) throws ProcessingException;

  Long getControlCount(Long courseNr) throws ProcessingException;

  CourseFormData find(String courseShortcut, long eventNr) throws ProcessingException;

  List<ControlFormData> loadControls(long courseNr, Long clientNr) throws ProcessingException;

  void delete(CourseFormData formData, boolean includingCourse) throws ProcessingException;

  CourseFormData createNewCourseWithVariants(CourseFormData formData, Long... courseNrs) throws ProcessingException;

}
