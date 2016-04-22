package com.rtiming.server.ranking;

import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.dao.RtRanking;
import com.rtiming.shared.dao.RtRankingKey;
import com.rtiming.shared.ranking.RankingFormulaTypeCodeType;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class RankingProcessServiceTest {

  @Test
  public void testPrepareCreate1() throws Exception {
    RankingProcessService svc = new RankingProcessService();
    svc.prepareCreate(null);
  }

  @Test
  public void testPrepareCreate2() throws Exception {
    RankingProcessService svc = new RankingProcessService();
    RtRanking result = svc.prepareCreate(new RtRanking());

    Assert.assertNotNull(result);
    Assert.assertEquals("Default", RankingFormulaTypeCodeType.SumTimeCode.ID, result.getFormulaTypeUid());
  }

  @Test
  public void testLoad1() throws Exception {
    RankingProcessService svc = new RankingProcessService();
    svc.load(null);
  }

  @Test
  public void testLoad2() throws Exception {
    RankingProcessService svc = new RankingProcessService();

    RtRanking ranking = new RtRanking();
    ranking.setId(RtRankingKey.create(ranking.getId()));
    JPA.merge(ranking);
    RtRanking ranking2 = svc.load(ranking.getId());
    Assert.assertNotNull("map loaded", ranking2);
    Assert.assertEquals("Key", ranking.getId(), ranking2.getId());

    svc.delete(ranking.getId());
  }

  @Test
  public void testDelete1() throws Exception {
    RankingProcessService svc = new RankingProcessService();
    svc.delete(null);
  }

  @Test
  public void testDelete2() throws Exception {
    RankingProcessService svc = new RankingProcessService();

    RtRanking ranking = new RtRanking();
    ranking.setId(RtRankingKey.create(ranking.getId()));
    JPA.merge(ranking);

    svc.delete(ranking.getId());

    RtRanking ranking2 = JPA.find(RtRanking.class, ranking.getId());
    Assert.assertNull("map should be deleted", ranking2);
  }

}
