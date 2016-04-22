package com.rtiming.client.event.course;

import java.util.List;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.shared.event.course.ControlStatusCodeType;

/**
 * 
 */
@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(TestEnvironmentClientSession.class)
public class ControlStatusManualLookupCallTest {

  @Test
  public void testOKKey() throws Exception {
    doTestGetKey(ControlStatusCodeType.OkCode.ID);
  }

  @Test
  public void testMissingKey() throws Exception {
    doTestGetKey(ControlStatusCodeType.MissingCode.ID);
  }

  @Test
  public void testInitialKey() throws Exception {
    doTestGetKey(ControlStatusCodeType.InitialStatusCode.ID);
  }

  @Test
  public void testWrongKey() throws Exception {
    doTestGetKey(ControlStatusCodeType.WrongCode.ID);
  }

  @Test
  public void testAdditionalKey() throws Exception {
    doTestGetKey(ControlStatusCodeType.AdditionalCode.ID);
  }

  @Test
  public void testGetByText() throws Exception {
    ControlStatusManualLookupCall call = new ControlStatusManualLookupCall();
    call.setText("*");
    List<? extends ILookupRow> rows = call.getDataByText();
    Assert.assertEquals("2 Rows (OK and MP)", 2, rows.size());
  }

  @Test
  public void testGetByAll() throws Exception {
    ControlStatusManualLookupCall call = new ControlStatusManualLookupCall();
    call.setText("*");
    List<? extends ILookupRow> rows = call.getDataByAll();
    Assert.assertEquals("2 Rows (OK and MP)", 2, rows.size());
  }

  private void doTestGetKey(long key) throws ProcessingException {
    ControlStatusManualLookupCall call = new ControlStatusManualLookupCall();
    call.setKey(key);
    List<? extends ILookupRow> rows = call.getDataByKey();
    Assert.assertEquals("1 Row", 1, rows.size());
    ILookupRow row = rows.get(0);
    Assert.assertEquals("OK", key, row.getKey());
  }

}
