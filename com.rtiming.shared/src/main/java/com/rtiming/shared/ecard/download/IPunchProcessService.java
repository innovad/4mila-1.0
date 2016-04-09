package com.rtiming.shared.ecard.download;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

@TunnelToServer
public interface IPunchProcessService extends IService {

  PunchFormData prepareCreate(PunchFormData formData) throws ProcessingException;

  PunchFormData create(PunchFormData formData) throws ProcessingException;

  PunchFormData load(PunchFormData formData) throws ProcessingException;

  PunchFormData store(PunchFormData formData) throws ProcessingException;

  void delete(long punchSessionNr, Long[] sortCodes) throws ProcessingException;

  void createTestData(long punchSessionNr, Long raceNr, Long eventNr, Long classUid) throws ProcessingException;
}
