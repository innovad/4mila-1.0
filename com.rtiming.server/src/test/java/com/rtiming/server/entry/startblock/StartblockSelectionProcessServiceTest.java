package com.rtiming.server.entry.startblock;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.shared.entry.startblock.StartblockSelectionFormData;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class StartblockSelectionProcessServiceTest {

  @Test
  public void testCreate1() throws ProcessingException {
    StartblockSelectionProcessService svc = new StartblockSelectionProcessService();
    svc.create(new StartblockSelectionFormData());
  }

  @Test
  public void testCreate2() throws ProcessingException {
    StartblockSelectionProcessService svc = new StartblockSelectionProcessService();
    StartblockSelectionFormData formData = new StartblockSelectionFormData();
    formData.getOverwrite().setValue(true);
    svc.create(formData);
  }

  @Test
  public void testCreate3() throws ProcessingException {
    StartblockSelectionProcessService svc = new StartblockSelectionProcessService();
    StartblockSelectionFormData formData = new StartblockSelectionFormData();
    formData.getOverwrite().setValue(true);
    formData.setEntryNrs(new Long[]{-1L, -2L, -3L});
    svc.create(formData);
  }

}
