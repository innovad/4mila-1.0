package com.rtiming.server.entry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.shared.entry.EntriesSearchFormData;
import com.rtiming.shared.entry.EntryRowData;
import com.rtiming.shared.entry.EntryTableDataOptions;
import com.rtiming.shared.entry.RegistrationsSearchFormData;
import com.rtiming.shared.results.SingleEventSearchFormData;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class RegistrationsOutlineServiceTest {

  @Test
  public void testGetEventStartblockTableData() throws ProcessingException {
    RegistrationsOutlineService svc = new RegistrationsOutlineService();
    Object[][] result = svc.getEventStartblockTableData(null);
    assertEquals("always 1 row", 1, result.length);
  }

  @Test
  public void testGetPaymentTableData1() throws ProcessingException {
    RegistrationsOutlineService svc = new RegistrationsOutlineService();
    svc.getPaymentTableData(null);
  }

  @Test
  public void testGetPaymentTableData2() throws ProcessingException {
    RegistrationsOutlineService svc = new RegistrationsOutlineService();
    Object[][] result = svc.getPaymentTableData(-123L);
    assertEquals("0 rows", 0, result.length);
  }

  @Test
  public void testGetClubTableData1() throws ProcessingException {
    RegistrationsOutlineService svc = new RegistrationsOutlineService();
    SearchFilter filter = new SearchFilter();
    SingleEventSearchFormData formData = new SingleEventSearchFormData();
    formData.getEvent().setValue(-1L); // not existing event
    filter.setFormData(formData);
    Object[][] result = svc.getEntryClubTableData(filter);
    assertEquals("0 rows", 0, result.length);
  }

  @Test
  public void testGetClubTableData2() throws ProcessingException {
    RegistrationsOutlineService svc = new RegistrationsOutlineService();
    SearchFilter filter = new SearchFilter();
    filter.setFormData(new SingleEventSearchFormData());
    Object[][] result = svc.getEntryClubTableData(filter);
    assertNotNull(result);
  }

  @Test
  public void testGetEntryClassTableData() throws Exception {
    RegistrationsOutlineService svc = new RegistrationsOutlineService();
    SearchFilter filter = new SearchFilter();
    filter.setFormData(new SingleEventSearchFormData());
    Object[][] result = svc.getEntryClassTableData(filter);
    assertNotNull(result);
  }

  @Test
  public void testGetEntryCourseTableData() throws Exception {
    RegistrationsOutlineService svc = new RegistrationsOutlineService();
    SearchFilter filter = new SearchFilter();
    filter.setFormData(new SingleEventSearchFormData());
    Object[][] result = svc.getEntryCourseTableData(filter);
    assertNotNull(result);
  }

  @Test
  public void testGetEntryTableData() throws Exception {
    RegistrationsOutlineService svc = new RegistrationsOutlineService();
    EntriesSearchFormData formData = new EntriesSearchFormData();
    formData.getEvent().setValue(0L);
    List<EntryRowData> list = svc.getEntryTableData(new EntryTableDataOptions(), formData);
    assertEquals("0 Rows", 0, list.size());
  }

  @Test
  public void testGetRegistrationTableData() throws Exception {
    RegistrationsOutlineService svc = new RegistrationsOutlineService();
    svc.getRegistrationTableData(new RegistrationsSearchFormData());
  }

}
