package com.rtiming.shared.settings.addinfo;

import java.util.List;
import java.util.Map;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

import com.rtiming.shared.common.database.sql.AdditionalInformationBean;
import com.rtiming.shared.common.database.sql.AdditionalInformationValueBean;

@TunnelToServer
public interface IAdditionalInformationProcessService extends IService {

  AdditionalInformationBean prepareCreate(AdditionalInformationBean formData) throws ProcessingException;

  AdditionalInformationBean create(AdditionalInformationBean formData) throws ProcessingException;

  AdditionalInformationBean load(AdditionalInformationBean formData) throws ProcessingException;

  AdditionalInformationBean store(AdditionalInformationBean formData) throws ProcessingException;

  Map<Long, List<AdditionalInformationValueBean>> loadAddInfoForEntity(Long clientNr);
}
