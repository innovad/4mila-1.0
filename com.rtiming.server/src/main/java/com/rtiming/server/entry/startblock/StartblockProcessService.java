package com.rtiming.server.entry.startblock;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.shared.Texts;
import com.rtiming.shared.common.AbstractCodeBoxData;
import com.rtiming.shared.common.security.permission.CreateStartblockPermission;
import com.rtiming.shared.common.security.permission.ReadStartblockPermission;
import com.rtiming.shared.common.security.permission.UpdateStartblockPermission;
import com.rtiming.shared.entry.startblock.IStartblockProcessService;
import com.rtiming.shared.entry.startblock.StartblockCodeType;
import com.rtiming.shared.entry.startblock.StartblockFormData;
import com.rtiming.shared.settings.ICodeProcessService;

public class StartblockProcessService  implements IStartblockProcessService {

  @Override
  public StartblockFormData prepareCreate(StartblockFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateStartblockPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    formData.getCodeBox().getCodeTypeUid().setValue(StartblockCodeType.ID);
    BEANS.get(ICodeProcessService.class).prepareCreateCodeBox(formData.getCodeBox());

    return formData;
  }

  @Override
  public StartblockFormData create(StartblockFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateStartblockPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    formData.getCodeBox().getCodeTypeUid().setValue(StartblockCodeType.ID);
    AbstractCodeBoxData codeBox = BEANS.get(ICodeProcessService.class).createCodeBox(formData.getCodeBox());
    formData.setStartblockUid(codeBox.getCodeUid().getValue());
    formData = store(formData);

    return formData;
  }

  @Override
  public StartblockFormData load(StartblockFormData formData) throws ProcessingException {
    if (!ACCESS.check(new ReadStartblockPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    formData.getCodeBox().getCodeUid().setValue(formData.getStartblockUid());
    BEANS.get(ICodeProcessService.class).loadCodeBox(formData.getCodeBox());

    return formData;
  }

  @Override
  public StartblockFormData store(StartblockFormData formData) throws ProcessingException {
    if (!ACCESS.check(new UpdateStartblockPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    BEANS.get(ICodeProcessService.class).storeCodeBox(formData.getCodeBox());

    return formData;
  }
}
