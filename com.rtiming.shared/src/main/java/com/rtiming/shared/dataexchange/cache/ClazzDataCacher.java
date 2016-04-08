package com.rtiming.shared.dataexchange.cache;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.BEANS;

import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.settings.CodeFormData;
import com.rtiming.shared.settings.ICodeProcessService;

public class ClazzDataCacher extends AbstractImportDataCacher<CodeFormData, String> {

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
    return BEANS.get(ICodeProcessService.class).find(string, ClassCodeType.ID);
  }

  @Override
  protected void createNewData(CodeFormData formData, String value) {
    formData.setCodeType(ClassCodeType.ID);
    formData.getMainBox().getShortcut().setValue(value);
    for (int k = 0; k < formData.getMainBox().getLanguage().getRowCount(); k++) {
      formData.getMainBox().getLanguage().setTranslation(k, value);
    }
  }

}
