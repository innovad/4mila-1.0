package com.rtiming.server.common.database.jpa;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.shared.dao.RtClub;
import com.rtiming.shared.dao.RtClub_;
import com.rtiming.shared.dao.RtRunner;
import com.rtiming.shared.dao.RtRunnerKey_;
import com.rtiming.shared.dao.RtRunner_;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class JPACriteriaUtilityTest {

  @Test
  public void testReplace1() throws Exception {
    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
    Root<RtClub> club = selectQuery.from(RtClub.class);

    testQuery(selectQuery, JPACriteriaUtility.replace(club.get(RtClub_.name), "", " "));
  }

  @Test
  public void testReplace2() throws Exception {
    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
    Root<RtClub> club = selectQuery.from(RtClub.class);
    Expression<String> test = JPACriteriaUtility.replace(club.get(RtClub_.name), "", " ");
    for (int i = 0; i < 100; i++) {
      test = JPACriteriaUtility.replace(test, "", " ");
    }

    testQuery(selectQuery, test);
  }

  @Test
  public void testRemoveDefaultTokens1() throws Exception {
    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
    Root<RtClub> club = selectQuery.from(RtClub.class);

    testQuery(selectQuery, JPACriteriaUtility.removeDefaultTokens(club.get(RtClub_.name)));
  }

  @Test
  public void testRemoveDefaultTokens2() throws Exception {
    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
    Root<RtClub> club = selectQuery.from(RtClub.class);
    Expression<String> test = JPACriteriaUtility.removeDefaultTokens(club.get(RtClub_.name));
    for (int i = 0; i < 100; i++) {
      test = JPACriteriaUtility.replace(test, "", " ");
    }

    testQuery(selectQuery, test);
  }

  @Test
  public void testRemoveDefaultTokens3() throws Exception {
    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
    Root<RtRunner> runner = selectQuery.from(RtRunner.class);
    Expression<String> runnerName = b.coalesce(JPACriteriaUtility.runnerNameJPA(runner), JPACriteriaUtility.runnerNameTokenDefaultsRemoved(runner));
    Expression<String> test = JPACriteriaUtility.removeDefaultTokens(runnerName);
    for (int i = 0; i < 100; i++) {
      test = JPACriteriaUtility.replace(test, "", " ");
    }

    testQuery(selectQuery, test);
  }

  @Test
  public void testRemoveDefaultTokens4() throws Exception {
    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
    Root<RtRunner> runner = selectQuery.from(RtRunner.class);

    Expression<String> test = b.selectCase().
        when(b.isNull(runner.get(RtRunner_.id).get(RtRunnerKey_.runnerNr)), "a")
        .otherwise("b").as(String.class);

    for (int k = 0; k < 20; k++) {
      test = JPACriteriaUtility.removeDefaultTokens(test);
    }

    testQuery(selectQuery, test);
  }

  @Test
  public void testRemoveDefaultTokens5() throws Exception {
    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
    Root<RtRunner> runner = selectQuery.from(RtRunner.class);

    Expression<String> test = b.concat(b.coalesce(runner.get(RtRunner_.lastName), ""), "");

    for (int k = 0; k < 20; k++) {
      test = JPACriteriaUtility.removeDefaultTokens(test);
    }

    testQuery(selectQuery, test);
  }

  private void testQuery(CriteriaQuery<Object[]> selectQuery, Expression<String> expression) {
    CriteriaBuilder b = JPA.getCriteriaBuilder();
    selectQuery.select(b.array(
        b.literal("a")
        )
        ).where(b.equal(expression, ""));

    JPA.createQuery(selectQuery).getResultList();
  }

}
