package com.rtiming.shared.runner;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

import com.rtiming.shared.common.database.sql.AddressBean;

@TunnelToServer
public interface IAddressProcessService extends IService {

  AddressBean load(AddressBean formData) throws ProcessingException;

  AddressBean store(AddressBean formData) throws ProcessingException;
}
