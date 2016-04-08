package com.rtiming.shared.event.course;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;


public interface IReplacementControlProcessService extends IService {

  ReplacementControlFormData prepareCreate(ReplacementControlFormData formData) throws ProcessingException;

  ReplacementControlFormData create(ReplacementControlFormData formData) throws ProcessingException;

  ReplacementControlFormData load(ReplacementControlFormData formData) throws ProcessingException;

  ReplacementControlFormData store(ReplacementControlFormData formData) throws ProcessingException;

  ReplacementControlFormData delete(ReplacementControlFormData formData) throws ProcessingException;
}
