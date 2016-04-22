package com.rtiming.server.event;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.dao.RtControl;
import com.rtiming.shared.dao.RtControlKey;
import com.rtiming.shared.dao.RtControlReplacement;
import com.rtiming.shared.dao.RtControlReplacementKey;
import com.rtiming.shared.dao.RtEvent;
import com.rtiming.shared.dao.RtEventKey;
import com.rtiming.shared.dao.RtRanking;
import com.rtiming.shared.dao.RtRankingKey;
import com.rtiming.shared.event.course.ControlsSearchFormData;
import com.rtiming.shared.event.course.ReplacementControlRowData;

import org.junit.Assert;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class EventsOutlineServiceTest {

  @Test
  public void testGetRankingTableData() throws ProcessingException {
    RtRanking ranking = createNewRanking();

    EventsOutlineService svc = new EventsOutlineService();
    Object[][] data = svc.getRankingTableData();

    Assert.assertTrue("More than 1 row", data.length >= 1);
    boolean found = false;
    for (Object[] row : data) {
      if (CompareUtility.equals(row[0], ranking.getId().getId())) {
        found = true;
      }
    }
    Assert.assertTrue("Data found", found);
    JPA.remove(ranking);
  }

  @Test
  public void testGetRankingEventTableData() throws ProcessingException {
    RtRanking ranking = createNewRanking();

    EventsOutlineService svc = new EventsOutlineService();
    Object[][] data = svc.getRankingEventTableData(ranking.getId().getId());

    Assert.assertTrue("More than 1 row", data.length == 0);
    JPA.remove(ranking);
  }

  @Test
  public void testGetCourseTableData1() throws Exception {
    EventsOutlineService svc = new EventsOutlineService();
    svc.getCourseTableData(null);
  }

  @Test
  public void testGetCourseTableData2() throws Exception {
    RtEvent event = createNewEvent();

    EventsOutlineService svc = new EventsOutlineService();
    svc.getCourseTableData(event.getId().getId());

    JPA.remove(event);
  }

  @Test
  public void testGetEventAdditionalInformationTableData1() throws Exception {
    EventsOutlineService svc = new EventsOutlineService();
    Object[][] result = svc.getEventAdditionalInformationTableData(0);
    assertEquals("0 rows", 0, result.length);
  }

  @Test
  public void testGetEventAdditionalInformationTableData2() throws Exception {
    RtEvent event = createNewEvent();
    EventsOutlineService svc = new EventsOutlineService();
    Object[][] result = svc.getEventAdditionalInformationTableData(event.getId().getId());
    assertEquals("0 rows", 0, result.length);
    JPA.remove(event);
  }

  @Test
  public void testGetReplacementControlTableData1() throws Exception {
    EventsOutlineService svc = new EventsOutlineService();
    List<ReplacementControlRowData> result = svc.getReplacementControlTableData(0);
    assertEquals("0 rows", 0, result.size());
  }

  @Test
  public void testGetReplacementControlTableData2() throws Exception {
    RtControl c1 = new RtControl();
    c1.setId(RtControlKey.create((Long) null));
    c1.setActive(true);
    c1.setControlNo("31");
    JPA.merge(c1);

    RtControl c2 = new RtControl();
    c2.setId(RtControlKey.create((Long) null));
    c2.setActive(true);
    c2.setControlNo("32");
    JPA.merge(c2);

    RtControlReplacement replacement = new RtControlReplacement();
    RtControlReplacementKey key = new RtControlReplacementKey();
    key.setClientNr(ServerSession.get().getSessionClientNr());
    key.setControlNr(c1.getId().getId());
    key.setReplacementControlNr(c2.getId().getId());
    replacement.setId(key);
    JPA.merge(replacement);

    EventsOutlineService svc = new EventsOutlineService();
    List<ReplacementControlRowData> result = svc.getReplacementControlTableData(c1.getId().getId());
    assertEquals("1 row", 1, result.size());
    assertEquals("control", c1.getId().getId(), result.get(0).getControlNr());
    assertEquals("replacement control", c2.getId().getId(), result.get(0).getReplacementControlNr());
    assertEquals("control", "31", result.get(0).getControlNo());
    assertEquals("replacement control", "32", result.get(0).getReplacementControlNo());

    JPA.remove(replacement);
    JPA.remove(c1);
    JPA.remove(c2);
  }

  @Test
  public void testGetControlTableData1() throws Exception {
    EventsOutlineService svc = new EventsOutlineService();
    Object[][] data = svc.getControlTableData(null, new ControlsSearchFormData());
    assertEquals("0 Rows", 0, data.length);
  }

  @Test
  public void testGetRaceControlTableData1() throws Exception {
    EventsOutlineService svc = new EventsOutlineService();
    svc.getRaceControlTableData(ServerSession.get().getSessionClientNr(), 0L);
  }

  @Test
  public void testGetRaceControlTableData2() throws Exception {
    EventsOutlineService svc = new EventsOutlineService();
    svc.getRaceControlTableData(ServerSession.get().getSessionClientNr(), 0L, 1L);
  }

  @Test
  public void testGetRaceControlTableData3() throws Exception {
    EventsOutlineService svc = new EventsOutlineService();
    svc.getRaceControlTableData(null, 0L, 1L);
  }

  @Test
  public void testGetRaceControlTableData4() throws Exception {
    EventsOutlineService svc = new EventsOutlineService();
    svc.getRaceControlTableData(null);
  }

  @Test
  public void testGetEventClassTableData() throws Exception {
    EventsOutlineService svc = new EventsOutlineService();
    svc.getEventClassTableData(0L, 0L);
  }

  @Test
  public void testGetRankingClassesTableData() throws Exception {
    EventsOutlineService svc = new EventsOutlineService();
    svc.getRankingClassesTableData(0L);
  }

  private RtEvent createNewEvent() throws ProcessingException {
    RtEvent event = new RtEvent();
    event.setId(RtEventKey.create(event.getId()));
    JPA.merge(event);
    return event;
  }

  private RtRanking createNewRanking() throws ProcessingException {
    RtRanking ranking = new RtRanking();
    ranking.setId(RtRankingKey.create(ranking.getId()));
    JPA.merge(ranking);
    return ranking;
  }

}
