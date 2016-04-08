package com.rtiming.shared.entry.startlist;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;

public interface IStartlistSettingProcessService extends IService {

  StartlistSettingFormData prepareCreate(StartlistSettingFormData formData) throws ProcessingException;

  StartlistSettingFormData create(StartlistSettingFormData formData) throws ProcessingException;

  StartlistSettingFormData load(StartlistSettingFormData formData) throws ProcessingException;

  StartlistSettingFormData store(StartlistSettingFormData formData) throws ProcessingException;

  StartlistSettingFormData delete(StartlistSettingFormData formData) throws ProcessingException;

}
