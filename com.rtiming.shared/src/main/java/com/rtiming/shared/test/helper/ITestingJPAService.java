package com.rtiming.shared.test.helper;

import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

@TunnelToServer
public interface ITestingJPAService extends IService {

  public Long getMaxCityNr() throws ProcessingException;

  public Long getCityCount() throws ProcessingException;

  public void deleteCities(Long countryUid) throws ProcessingException;

  List<Long> getPunchSessionsForStation(Long stationNr) throws ProcessingException;

  public void cleanupAccounts() throws ProcessingException;

  public void cleanupCountries() throws ProcessingException;

  public void deleteAccount(Long accountNr) throws ProcessingException;

  public void cleanupRunner(Long eCardNr) throws ProcessingException;
}
