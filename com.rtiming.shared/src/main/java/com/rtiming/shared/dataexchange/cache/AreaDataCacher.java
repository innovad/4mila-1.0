package com.rtiming.shared.dataexchange.cache;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.BEANS;

import com.rtiming.shared.settings.CodeFormData;
import com.rtiming.shared.settings.ICodeProcessService;
import com.rtiming.shared.settings.city.AreaCodeType;

public class AreaDataCacher extends AbstractDataCacher<CodeFormData, String> {

  @Override
  protected Long getPrimaryKey(CodeFormData formData) {
    return formData.getCodeUid();
  }

  @Override
  protected CodeFormData store(CodeFormData formData) throws ProcessingException {
    return BEANS.get(ICodeProcessService.class).store(formData);
  }

  @Override
  protected CodeFormData create(CodeFormData formData) throws ProcessingException {
    return BEANS.get(ICodeProcessService.class).create(formData);
  }

  @Override
  protected CodeFormData find(String string) throws ProcessingException {
    return BEANS.get(ICodeProcessService.class).find(string, AreaCodeType.ID);
  }

}
