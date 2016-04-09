package com.rtiming.shared.dataexchange.cache;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.shared.entry.IRegistrationProcessService;
import com.rtiming.shared.entry.RegistrationFormData;

public class RegistrationDataCacher extends AbstractDataCacher<RegistrationFormData, String> {

  @Override
  protected RegistrationFormData find(String string) throws ProcessingException {
    return BEANS.get(IRegistrationProcessService.class).find(string);
  }

  @Override
  protected RegistrationFormData store(RegistrationFormData formData) throws ProcessingException {
    return BEANS.get(IRegistrationProcessService.class).store(formData);
  }

  @Override
  protected RegistrationFormData create(RegistrationFormData formData) throws ProcessingException {
    return BEANS.get(IRegistrationProcessService.class).create(formData);
  }

  @Override
  protected Long getPrimaryKey(RegistrationFormData formData) {
    return formData.getRegistrationNr();
  }

}
