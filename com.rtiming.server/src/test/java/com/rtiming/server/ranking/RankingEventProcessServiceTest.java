package com.rtiming.server.ranking;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.dao.RtEvent;
import com.rtiming.shared.dao.RtEventKey;
import com.rtiming.shared.dao.RtRanking;
import com.rtiming.shared.dao.RtRankingEvent;
import com.rtiming.shared.dao.RtRankingEventKey;
import com.rtiming.shared.dao.RtRankingKey;
import com.rtiming.shared.ranking.RankingFormulaTypeCodeType;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class RankingEventProcessServiceTest {

  private static RtEvent event;
  private static RtRanking ranking;

  @BeforeClass
  public static void beforeClass() throws ProcessingException {
    ranking = new RtRanking();
    ranking.setId(RtRankingKey.create(ranking.getId()));
    JPA.merge(ranking);

    event = new RtEvent();
    event.setId(RtEventKey.create(event.getId()));
    JPA.merge(event);
  }

  @AfterClass
  public static void afterClass() throws ProcessingException {
    JPA.remove(event);
    JPA.remove(ranking);
  }

  @Test
  public void testPrepareCreate1() throws Exception {
    RankingEventProcessService svc = new RankingEventProcessService();
    RtRankingEvent result = svc.prepareCreate(null);
    Assert.assertNull(result);
  }

  @Test
  public void testPrepareCreate2() throws Exception {
    RankingEventProcessService svc = new RankingEventProcessService();

    RtRankingEvent rankingEvent = new RtRankingEvent();
    RtRankingEventKey key = new RtRankingEventKey();
    key.setClientNr(ServerSession.get().getSessionClientNr());
    key.setRankingNr(ranking.getId().getId());
    rankingEvent.setId(key);
    RtRankingEvent result = svc.prepareCreate(rankingEvent);

    Assert.assertNotNull(result);
    Assert.assertEquals("Default", RankingFormulaTypeCodeType.TimeCode.ID, result.getFormulaTypeUid());
    Assert.assertEquals("Sortcode", 1, result.getSortcode().longValue());
  }

  @Test
  public void testPrepareCreate3() throws Exception {
    RankingEventProcessService svc = new RankingEventProcessService();

    RtRankingEvent rankingEvent = new RtRankingEvent();
    RtRankingEvent result = svc.prepareCreate(rankingEvent);

    Assert.assertNotNull(result);
    Assert.assertEquals("Default", RankingFormulaTypeCodeType.TimeCode.ID, result.getFormulaTypeUid());
    Assert.assertEquals("Sortcode", 1L, result.getSortcode().longValue());
  }

  @Test
  public void testLoad1() throws Exception {
    RankingProcessService svc = new RankingProcessService();
    svc.load(null);
  }

  @Test
  public void testLoad2() throws Exception {
    RankingEventProcessService svc = new RankingEventProcessService();

    RtRankingEvent rankingEvent = fillRankingEvent();
    JPA.merge(rankingEvent);

    RtRankingEvent rankingEvent2 = svc.load(rankingEvent.getId());
    Assert.assertNotNull("map loaded", rankingEvent2);
    Assert.assertEquals("Key", rankingEvent.getId(), rankingEvent2.getId());

    svc.delete(rankingEvent.getId());
  }

  @Test
  public void testDelete1() throws Exception {
    RankingEventProcessService svc = new RankingEventProcessService();
    svc.delete(null);
  }

  @Test
  public void testDelete2() throws Exception {
    RankingEventProcessService svc = new RankingEventProcessService();

    RtRankingEvent rankingEvent = fillRankingEvent();
    JPA.merge(rankingEvent);
    svc.delete(rankingEvent.getId());

    RtRankingEvent rankingEvent2 = JPA.find(RtRankingEvent.class, rankingEvent.getId());
    Assert.assertNull("map should be deleted", rankingEvent2);
  }

  private RtRankingEvent fillRankingEvent() {
    RtRankingEvent rankingEvent = new RtRankingEvent();
    RtRankingEventKey rankingEventKey = new RtRankingEventKey();
    rankingEventKey.setClientNr(ServerSession.get().getSessionClientNr());
    rankingEventKey.setEventNr(event.getId().getId());
    rankingEventKey.setRankingNr(ranking.getId().getId());
    rankingEvent.setId(rankingEventKey);
    return rankingEvent;
  }

}
