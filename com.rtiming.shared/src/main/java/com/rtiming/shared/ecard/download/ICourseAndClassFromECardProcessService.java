package com.rtiming.shared.ecard.download;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

@TunnelToServer
public interface ICourseAndClassFromECardProcessService extends IService {

  CourseAndClassFromECardFormData prepareCreate(CourseAndClassFromECardFormData formData) throws ProcessingException;

  CourseAndClassFromECardFormData create(CourseAndClassFromECardFormData formData) throws ProcessingException;

  CourseAndClassFromECardFormData load(CourseAndClassFromECardFormData formData) throws ProcessingException;

  CourseAndClassFromECardFormData store(CourseAndClassFromECardFormData formData) throws ProcessingException;
}
