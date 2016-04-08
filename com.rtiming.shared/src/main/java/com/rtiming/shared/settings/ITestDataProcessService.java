package com.rtiming.shared.settings;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

@TunnelToServer
public interface ITestDataProcessService extends IService {

  TestDataFormData prepareCreate(TestDataFormData formData) throws ProcessingException;

  TestDataFormData create(TestDataFormData formData) throws ProcessingException;

  TestDataFormData load(TestDataFormData formData) throws ProcessingException;

  TestDataFormData store(TestDataFormData formData) throws ProcessingException;
}
