package com.rtiming.shared.ecard;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

import com.rtiming.shared.dao.RtEcard;
import com.rtiming.shared.dao.RtEcardKey;

@TunnelToServer
public interface IECardProcessService extends IService {

  ECardFormData prepareCreate(ECardFormData formData) throws ProcessingException;

  RtEcard create(RtEcard bean) throws ProcessingException;

  RtEcard load(RtEcardKey key) throws ProcessingException;

  RtEcard store(RtEcard bean) throws ProcessingException;

  RtEcard findECard(String eCardNo) throws ProcessingException;

  int delete(RtEcardKey... keys) throws ProcessingException;

}
