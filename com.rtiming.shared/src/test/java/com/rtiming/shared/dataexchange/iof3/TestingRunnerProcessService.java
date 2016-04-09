package com.rtiming.shared.dataexchange.iof3;

import org.eclipse.scout.rt.platform.IgnoreBean;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.StringUtility;

import com.rtiming.shared.common.database.sql.RunnerBean;
import com.rtiming.shared.runner.IRunnerProcessService;

@IgnoreBean
public class TestingRunnerProcessService implements IRunnerProcessService {

  @Override
  public RunnerBean prepareCreate(RunnerBean bean) throws ProcessingException {
    return bean;
  }

  @Override
  public RunnerBean create(RunnerBean bean) throws ProcessingException {
    return bean;
  }

  @Override
  public RunnerBean load(RunnerBean bean) throws ProcessingException {
    if (bean != null && bean.getRunnerNr() != null && bean.getRunnerNr() == 777) {
      bean.setExtKey("CP6MOA");
      return bean;
    }
    return bean;
  }

  @Override
  public RunnerBean store(RunnerBean bean) throws ProcessingException {
    return bean;
  }

  @Override
  public Long findRunner(String extKey) throws ProcessingException {
    if (StringUtility.equalsIgnoreCase(extKey, "CP6MOA")) {
      return 777L;
    }
    return null;
  }

  @Override
  public Long findRunnerByECard(String eCardNo) throws ProcessingException {
    return null;
  }

  @Override
  public RunnerBean delete(RunnerBean bean) throws ProcessingException {
    return bean;
  }

  @Override
  public void updateOnlineAccountRunnerLinks(Long accountNr, Long clientNr, Long runnerNr) throws ProcessingException {
  }

}
