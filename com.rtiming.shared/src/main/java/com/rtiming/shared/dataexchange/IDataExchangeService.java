package com.rtiming.shared.dataexchange;

import java.util.List;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;

public interface IDataExchangeService extends IService {

  ImportMessageList storeSwissOrienteeringRunner(List<AbstractDataBean> runner) throws ProcessingException;

  ImportMessageList storeOE2003Entry(List<AbstractDataBean> runner, Long eventNr) throws ProcessingException;

  ImportMessageList storeGO2OLEntry(List<AbstractDataBean> entry, Long eventNr) throws ProcessingException;

  ImportMessageList storeGeneralCity(List<AbstractDataBean> city) throws ProcessingException;

  ImportMessageList storeGeonamesCity(List<AbstractDataBean> city) throws ProcessingException;

  ImportMessageList storeSwissPostCity(List<AbstractDataBean> entry) throws ProcessingException;

  ImportMessageList storeIOF300Runner(List<AbstractDataBean> runner) throws ProcessingException;

  void clearCaches();

}
