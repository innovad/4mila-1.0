package com.rtiming.server.test.helper;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.shared.test.helper.ITestingJPAService;

@RunWith(ServerTestRunner.class)
@RunWithSubject("admin")
@RunWithServerSession(ServerSession.class)
public class TestingJPAServiceTest {

  @Test
  public void testGetMaxCityNr() throws ProcessingException {
    ITestingJPAService svc = BEANS.get(ITestingJPAService.class);
    svc.getMaxCityNr();
  }

  @Test
  public void testGetCityCount() throws ProcessingException {
    ITestingJPAService svc = BEANS.get(ITestingJPAService.class);
    svc.getCityCount();
  }

  @Test
  public void testDeleteCities() throws ProcessingException {
    ITestingJPAService svc = BEANS.get(ITestingJPAService.class);
    svc.deleteCities(0L);
  }

  @Test
  public void testGetPunchSessionsForStation1() throws Exception {
    ITestingJPAService svc = BEANS.get(ITestingJPAService.class);
    List<Long> result = svc.getPunchSessionsForStation(0L);
    assertEquals(0L, result.size());
  }

  @Test
  public void testGetPunchSessionsForStation2() throws Exception {
    ITestingJPAService svc = BEANS.get(ITestingJPAService.class);
    svc.getPunchSessionsForStation(null);
  }

  @Test
  public void testCleanupAccounts() throws Exception {
    ITestingJPAService svc = BEANS.get(ITestingJPAService.class);
    svc.cleanupAccounts();
  }

  @Test
  public void testCleanupCountries() throws Exception {
    ITestingJPAService svc = BEANS.get(ITestingJPAService.class);
    svc.cleanupCountries();
  }

  @Test
  public void testCleanupRunner() throws Exception {
    ITestingJPAService svc = BEANS.get(ITestingJPAService.class);
    svc.cleanupRunner(0L);
  }

}
