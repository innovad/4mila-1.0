package com.rtiming.server.entry.startlist;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import org.junit.Assert;

/**
 * @author amo
 */
public class StartlistServiceTest {

  @Test
  public void testGetMaxBibNoDefault() throws Exception {
    StartlistService svc = new StartlistService();
    Long nextBibNo = svc.getMaxBibNoFromCurrentStartlists(null, null);
    Assert.assertEquals("Default Bib No", 1, nextBibNo.longValue());
  }

  @Test
  public void testGetMaxBibNoUnchanged() throws Exception {
    StartlistService svc = new StartlistService();
    Long nextBibNo = svc.getMaxBibNoFromCurrentStartlists(5L, null);
    Assert.assertEquals("Unchanged+1", 6, nextBibNo.longValue());
  }

  @Test
  public void testGetMaxBibNo1() throws Exception {
    List<StartlistSettingBean> list = new ArrayList<>();
    StartlistService svc = new StartlistService();
    Long nextBibNo = svc.getMaxBibNoFromCurrentStartlists(5L, list);
    Assert.assertEquals("Unchanged+1", 6, nextBibNo.longValue());
  }

  @Test
  public void testGetMaxBibNo2() throws Exception {
    List<StartlistSettingBean> list = new ArrayList<>();
    LinkedList<StartlistParticipationBean> startlist = new LinkedList<>();
    StartlistSettingBean bean = new StartlistSettingBean(1L, 1L, null, startlist);
    list.add(bean);
    StartlistService svc = new StartlistService();
    Long nextBibNo = svc.getMaxBibNoFromCurrentStartlists(5L, list);
    Assert.assertEquals("Unchanged+1", 6, nextBibNo.longValue());
  }

  @Test
  public void testGetMaxBibNo3() throws Exception {
    StartlistService svc = new StartlistService();
    List<StartlistSettingBean> list = createStartlistSettingList(createStartlist(createRace(77L)));
    Long nextBibNo = svc.getMaxBibNoFromCurrentStartlists(5L, list);
    Assert.assertEquals("New Bib No", 78, nextBibNo.longValue());
  }

  @Test
  public void testGetMaxBibNo4() throws Exception {
    StartlistService svc = new StartlistService();
    List<StartlistSettingBean> list = createStartlistSettingList(createStartlist(
        createRace(78L),
        createRace(77L)
        ));
    Long nextBibNo = svc.getMaxBibNoFromCurrentStartlists(5L, list);
    Assert.assertEquals("New Bib No", 79, nextBibNo.longValue());
  }

  @Test
  public void testGetMaxBibNo5() throws Exception {
    StartlistService svc = new StartlistService();
    List<StartlistSettingBean> list = createStartlistSettingList(createStartlist(
        createRace(78L),
        createRace(77L)
        ),
        createStartlist(
            createRace(80L),
            createRace(81L)
        ));
    Long nextBibNo = svc.getMaxBibNoFromCurrentStartlists(5L, list);
    Assert.assertEquals("New Bib No", 82, nextBibNo.longValue());
  }

  @Test
  public void testGetMaxBibNo6() throws Exception {
    StartlistService svc = new StartlistService();
    List<StartlistSettingBean> list = createStartlistSettingList(createStartlist(
        createRace(78L),
        createRace(77L)
        ),
        createStartlist(
            createRace(80L),
            createRace(81L)
        ));
    Long nextBibNo = svc.getMaxBibNoFromCurrentStartlists(122L, list);
    Assert.assertEquals("New Bib No", 123, nextBibNo.longValue());
  }

  @SafeVarargs
  private final List<StartlistSettingBean> createStartlistSettingList(LinkedList<StartlistParticipationBean>... startlists) {
    List<StartlistSettingBean> list = new ArrayList<>();
    long k = 0;
    for (LinkedList<StartlistParticipationBean> startlist : startlists) {
      StartlistSettingBean bean = new StartlistSettingBean(k, 1L, null, startlist);
      list.add(bean);
      k++;
    }
    return list;
  }

  private LinkedList<StartlistParticipationBean> createStartlist(StartlistParticipationBean... beans) {
    LinkedList<StartlistParticipationBean> startlist = new LinkedList<>();
    for (StartlistParticipationBean bean : beans) {
      startlist.add(bean);
    }
    return startlist;
  }

  private StartlistParticipationBean createRace(Long bibNo) {
    StartlistParticipationBean race = new StartlistParticipationBean();
    race.setBibNo(bibNo);
    return race;
  }

}
