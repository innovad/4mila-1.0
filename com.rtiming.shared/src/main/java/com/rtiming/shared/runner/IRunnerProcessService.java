package com.rtiming.shared.runner;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

import com.rtiming.shared.common.database.sql.RunnerBean;

@TunnelToServer
public interface IRunnerProcessService extends IService {

  RunnerBean prepareCreate(RunnerBean bean) throws ProcessingException;

  RunnerBean create(RunnerBean bean) throws ProcessingException;

  RunnerBean load(RunnerBean bean) throws ProcessingException;

  RunnerBean store(RunnerBean bean) throws ProcessingException;

  Long findRunner(String extKey) throws ProcessingException;

  Long findRunnerByECard(String eCardNo) throws ProcessingException;

  RunnerBean delete(RunnerBean bean) throws ProcessingException;

  /**
   * Updates RACE_NR on RT_RUNNER based on E-Mail-Address matching between Account and Runner
   * (E-Mail of RT_ACCOUNT and E-Mail of RT_RUNNER)
   * 
   * @param accountNr
   * @param raceNr
   * @throws ProcessingException
   */
  void updateOnlineAccountRunnerLinks(Long accountNr, Long clientNr, Long runnerNr) throws ProcessingException;

}
