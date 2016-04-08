package com.rtiming.shared.runner;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;

import com.rtiming.shared.common.database.sql.AddressBean;

public interface IAddressProcessService extends IService {

  AddressBean load(AddressBean formData) throws ProcessingException;

  AddressBean store(AddressBean formData) throws ProcessingException;
}
