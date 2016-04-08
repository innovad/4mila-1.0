package com.rtiming.shared.settings.fee;

import java.util.List;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;

public interface IFeeProcessService extends IService {

  FeeFormData prepareCreate(FeeFormData formData) throws ProcessingException;

  FeeFormData create(FeeFormData formData) throws ProcessingException;

  FeeFormData load(FeeFormData formData) throws ProcessingException;

  FeeFormData store(FeeFormData formData) throws ProcessingException;

  FeeFormData delete(FeeFormData formData) throws ProcessingException;

  List<FeeFormData> loadFeeConfiguration() throws ProcessingException;
}
