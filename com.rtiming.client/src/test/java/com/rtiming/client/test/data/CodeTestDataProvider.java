package com.rtiming.client.test.data;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.client.settings.CodeForm;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.shared.settings.CodeFormData;
import com.rtiming.shared.settings.ICodeProcessService;

public class CodeTestDataProvider extends AbstractTestDataProvider<CodeForm> {

  private final Long codeTypeUid;

  public CodeTestDataProvider(Long codeTypeUid) throws ProcessingException {
    this.codeTypeUid = codeTypeUid;
    callInitializer();
  }

  @Override
  protected CodeForm createForm() throws ProcessingException {
    CodeForm code = new CodeForm();
    code.setCodeType(codeTypeUid);
    code.startNew();
    FormTestUtility.fillFormFields(code);
    code.doOk();
    return code;
  }

  @Override
  public void remove() throws ProcessingException {
    CodeFormData codeFormData = new CodeFormData();
    codeFormData.setCodeUid(getCodeUid());
    codeFormData.setCodeType(codeTypeUid);
    codeFormData = BEANS.get(ICodeProcessService.class).load(codeFormData);
    BEANS.get(ICodeProcessService.class).deleteCodeBox(codeFormData.getMainBox());
  }

  public Long getCodeUid() throws ProcessingException {
    return getForm().getCodeUid();
  }

}
