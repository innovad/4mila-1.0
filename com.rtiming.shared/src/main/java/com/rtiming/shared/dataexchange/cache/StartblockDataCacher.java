package com.rtiming.shared.dataexchange.cache;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.shared.entry.startblock.StartblockCodeType;
import com.rtiming.shared.settings.CodeFormData;
import com.rtiming.shared.settings.ICodeProcessService;

public class StartblockDataCacher extends AbstractDataCacher<CodeFormData, String> {

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
    return BEANS.get(ICodeProcessService.class).find(string, StartblockCodeType.ID);
  }

  public CodeFormData getDefault() throws ProcessingException {
    return get("default");
  }

}
