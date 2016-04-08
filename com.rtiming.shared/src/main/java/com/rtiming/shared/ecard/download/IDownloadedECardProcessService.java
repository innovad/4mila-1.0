package com.rtiming.shared.ecard.download;

import java.util.Date;
import java.util.List;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

@TunnelToServer
public interface IDownloadedECardProcessService extends IService {

  DownloadedECardFormData prepareCreate(DownloadedECardFormData formData) throws ProcessingException;

  DownloadedECardFormData create(DownloadedECardFormData formData) throws ProcessingException;

  DownloadedECardFormData load(DownloadedECardFormData formData) throws ProcessingException;

  DownloadedECardFormData store(DownloadedECardFormData formData) throws ProcessingException;

  DownloadedECardFormData delete(DownloadedECardFormData formData) throws ProcessingException;

  Long[] matchClass(long punchSessionNr, List<PunchFormData> punches, long eventNr, Date evtZero, Long sexUid, Long year) throws ProcessingException;
}
