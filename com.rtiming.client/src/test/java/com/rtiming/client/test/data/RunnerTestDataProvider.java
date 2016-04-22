package com.rtiming.client.test.data;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.client.runner.RunnerForm;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.common.database.sql.RunnerBean;
import com.rtiming.shared.runner.IRunnerProcessService;

public class RunnerTestDataProvider extends AbstractTestDataProvider<RunnerForm> {

  private List<FieldValue> fieldValue;

  public RunnerTestDataProvider() throws ProcessingException {
    this.fieldValue = new ArrayList<FieldValue>();
    callInitializer();
  }

  public RunnerTestDataProvider(List<FieldValue> fieldValue) throws ProcessingException {
    this.fieldValue = fieldValue;
    callInitializer();
  }

  @Override
  protected RunnerForm createForm() throws ProcessingException {
    RunnerForm runner = new RunnerForm();
    runner.startNew();
    FormTestUtility.fillFormFields(runner, fieldValue.toArray(new FieldValue[fieldValue.size()]));
    runner.doOk();
    return runner;
  }

  @Override
  public void remove() throws ProcessingException {
    RunnerBean runner = new RunnerBean();
    runner.setRunnerNr(getForm().getRunnerNr());
    BEANS.get(IRunnerProcessService.class).delete(runner);
  }

  public Long getRunnerNr() throws ProcessingException {
    return getForm().getRunnerNr();
  }

  public Long getClubNr() throws ProcessingException {
    return getForm().getClubField().getValue();
  }

}
