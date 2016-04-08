package com.rtiming.shared.settings.user;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;

import com.rtiming.shared.dao.RtUserKey;

public interface IUserProcessService extends IService {

  UserFormData prepareCreate(UserFormData formData) throws ProcessingException;

  UserFormData create(UserFormData formData) throws ProcessingException;

  UserFormData load(UserFormData formData) throws ProcessingException;

  UserFormData store(UserFormData formData) throws ProcessingException;

  UserFormData find(String username) throws ProcessingException;

  int delete(RtUserKey... keys) throws ProcessingException;
}
