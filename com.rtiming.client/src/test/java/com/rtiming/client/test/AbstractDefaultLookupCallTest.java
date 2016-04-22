package com.rtiming.client.test;

import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

public abstract class AbstractDefaultLookupCallTest extends AbstractSimpleDatabaseLookupServiceTest {

  @Override
  protected void init() throws ProcessingException {
    LookupCall call = createLookupCall();
    List<ILookupRow> rows = call.getDataByAll();
    setKey((Long) rows.get(0).getKey());
    setText(rows.get(0).getText());
    setLookupCall(call);
  }

  protected abstract LookupCall createLookupCall() throws ProcessingException;

  @Override
  protected void insertTestRow() throws ProcessingException {
  }

  @Override
  protected void deleteTestRow() throws ProcessingException {
  }

}
