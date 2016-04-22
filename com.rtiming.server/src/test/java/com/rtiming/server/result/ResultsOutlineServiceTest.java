package com.rtiming.server.result;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.test.data.EventClassTestDataProvider;
import com.rtiming.shared.dao.RtEvent;
import com.rtiming.shared.dao.RtEventKey;
import com.rtiming.shared.dao.RtPunch;
import com.rtiming.shared.dao.RtPunchKey;
import com.rtiming.shared.dao.RtPunchSession;
import com.rtiming.shared.dao.RtPunchSessionKey;
import com.rtiming.shared.ecard.download.DownloadedECards;
import com.rtiming.shared.ecard.download.DownloadedECardsSearchFormData;
import com.rtiming.shared.results.ResultClazzRowData;
import com.rtiming.shared.results.SingleEventSearchFormData;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class ResultsOutlineServiceTest {

  private EventClassTestDataProvider testData;

  @Test
  public void testGetResultClubTableData1() throws Exception {
    ResultsOutlineService svc = new ResultsOutlineService();
    SearchFilter filter = new SearchFilter();
    filter.setFormData(new SingleEventSearchFormData());
    svc.getResultClubTableData(filter);
  }

  @Test
  public void testGetResultClubTableData2() throws Exception {
    ResultsOutlineService svc = new ResultsOutlineService();
    SearchFilter filter = new SearchFilter();
    SingleEventSearchFormData formData = new SingleEventSearchFormData();
    formData.getEvent().setValue(0L);
    filter.setFormData(formData);
    Object[][] result = svc.getResultClubTableData(filter);
    assertEquals("0 Rows", 0, result.length);
  }

  @Test
  public void testGetECardStationTableData() throws Exception {
    ResultsOutlineService svc = new ResultsOutlineService();
    svc.getECardStationTableData();
  }

  @Test
  public void testGetPunchTableData1() throws Exception {
    ResultsOutlineService svc = new ResultsOutlineService();
    Object[][] result = svc.getPunchTableData(-1L);
    assertEquals("0 Rows", 0, result.length);
  }

  @Test
  public void testGetPunchTableData2() throws Exception {
    ResultsOutlineService svc = new ResultsOutlineService();
    svc.getPunchTableData(null);
  }

  @Test
  public void testGetPunchTableData3() throws Exception {
    RtEvent event = new RtEvent();
    event.setId(RtEventKey.create((Long) null));
    event.setEvtZero(DateUtility.parse("02072012 0830", "ddMyyyy HHmm"));
    JPA.persist(event);

    RtPunchSession punchSession = new RtPunchSession();
    punchSession.setId(RtPunchSessionKey.create((Long) null));
    punchSession.setEventNr(event.getId().getId());
    JPA.persist(punchSession);

    RtPunch punch = new RtPunch();
    RtPunchKey key = new RtPunchKey();
    key.setId(punchSession.getId().getId());
    key.setSortcode(1L);
    key.setClientNr(ServerSession.get().getSessionClientNr());
    punch.setId(key);
    punch.setTime(3700L * 1000);
    JPA.persist(punch);

    ResultsOutlineService svc = new ResultsOutlineService();
    Object[][] result = svc.getPunchTableData(punchSession.getId().getId());
    assertEquals("1 Row", 1, result.length);
    assertEquals("Punch Date", DateUtility.parse("02072012 093140", "ddMyyyy HHmmss"), result[0][2]);

    JPA.remove(punch);
    JPA.remove(punchSession);
  }

  @Test
  public void testGetResultCourseTableData() throws Exception {
    ResultsOutlineService svc = new ResultsOutlineService();
    SearchFilter filter = new SearchFilter();
    SingleEventSearchFormData formData = new SingleEventSearchFormData();
    formData.getEvent().setValue(0L);
    filter.setFormData(formData);
    Object[][] result = svc.getResultCourseTableData(filter);
    assertEquals("0 Rows", 0, result.length);
  }

  @Test
  public void testGetResultClassTableData1() throws Exception {
    ResultsOutlineService svc = new ResultsOutlineService();
    SearchFilter filter = new SearchFilter();
    SingleEventSearchFormData formData = new SingleEventSearchFormData();
    formData.getEvent().setValue(0L);
    filter.setFormData(formData);
    List<ResultClazzRowData> result = svc.getResultClassTableData(ServerSession.get().getSessionClientNr(), filter);
    assertEquals("0 Rows", 0, result.size());
  }

  @Test
  public void testGetResultClassTableData2() throws Exception {
    ResultsOutlineService svc = new ResultsOutlineService();
    SearchFilter filter = new SearchFilter();
    SingleEventSearchFormData formData = new SingleEventSearchFormData();
    formData.getEvent().setValue(null);
    filter.setFormData(formData);
    List<ResultClazzRowData> result = svc.getResultClassTableData(null, filter);
    assertEquals("0 Rows", 0, result.size());
  }

  @Test
  public void testGetResultClassTableData3() throws Exception {
    testData = new EventClassTestDataProvider();
    ResultsOutlineService svc = new ResultsOutlineService();
    SearchFilter filter = new SearchFilter();
    SingleEventSearchFormData formData = new SingleEventSearchFormData();
    formData.getEvent().setValue(testData.getEvent().getId().getId());
    filter.setFormData(formData);
    List<ResultClazzRowData> result = svc.getResultClassTableData(ServerSession.get().getSessionClientNr(), filter);
    assertEquals("1 Row", 1, result.size());
    assertEquals("Sortcode", 1, result.get(0).getSortCode().longValue());
  }

  @Test
  public void testGetDownloadedECardTableData1() throws Exception {
    ResultsOutlineService svc = new ResultsOutlineService();
    svc.getDownloadedECardTableData(DownloadedECards.ALL, new DownloadedECardsSearchFormData());
  }

  @Test
  public void testGetDownloadedECardTableData2() throws Exception {
    ResultsOutlineService svc = new ResultsOutlineService();
    svc.getDownloadedECardTableData(DownloadedECards.DUPLICATE, new DownloadedECardsSearchFormData());
  }

  @Test
  public void testGetResultsTableDataInternal1() throws Exception {
    ResultsOutlineService svc = new ResultsOutlineService();
    svc.getResultsTableDataInternal(ServerSession.get().getSessionClientNr(), null, null, null, null);
  }

  @Test
  public void testGetResultsTableDataInternal2() throws Exception {
    ResultsOutlineService svc = new ResultsOutlineService();
    svc.getResultsTableDataInternal(ServerSession.get().getSessionClientNr(), null, null, -1L, null);
  }

  @Test
  public void testGetSplitTimesReportData() throws Exception {
    ResultsOutlineService svc = new ResultsOutlineService();
    svc.getSplitTimesReportData(0L);
  }

  @After
  public void after() throws ProcessingException {
    if (testData != null) {
      testData.remove();
    }
  }

}
