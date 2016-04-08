package com.rtiming.shared.event.course;

import java.util.List;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

@TunnelToServer
public interface ICourseControlProcessService extends IService {

  CourseControlFormData prepareCreate(CourseControlFormData formData) throws ProcessingException;

  CourseControlFormData create(CourseControlFormData formData) throws ProcessingException;

  CourseControlFormData load(CourseControlFormData formData) throws ProcessingException;

  CourseControlFormData store(CourseControlFormData formData) throws ProcessingException;

  CourseControlFormData find(Long courseNr, Long controlNr, Long sortCode) throws ProcessingException;

  void delete(CourseControlFormData formData) throws ProcessingException;

  List<List<CourseControlRowData>> getCourses(Long courseNr) throws ProcessingException;
}
