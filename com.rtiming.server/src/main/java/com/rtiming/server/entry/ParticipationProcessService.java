package com.rtiming.server.entry;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.CreateParticipationPermission;
import com.rtiming.shared.common.security.permission.ReadParticipationPermission;
import com.rtiming.shared.common.security.permission.UpdateParticipationPermission;
import com.rtiming.shared.entry.IParticipationProcessService;
import com.rtiming.shared.entry.ParticipationFormData;

public class ParticipationProcessService  implements IParticipationProcessService {

  @Override
  public ParticipationFormData prepareCreate(ParticipationFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateParticipationPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }
    return formData;
  }

  @Override
  public ParticipationFormData create(ParticipationFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateParticipationPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }
    return formData;
  }

  @Override
  public ParticipationFormData load(ParticipationFormData formData) throws ProcessingException {
    if (!ACCESS.check(new ReadParticipationPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }
    return formData;
  }

  @Override
  public ParticipationFormData store(ParticipationFormData formData) throws ProcessingException {
    if (!ACCESS.check(new UpdateParticipationPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }
    return formData;
  }
}
