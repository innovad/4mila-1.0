package com.rtiming.shared.common.database.sql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Test;

import com.rtiming.shared.dao.RtRankingEvent;
import com.rtiming.shared.dao.RtRankingEventKey;
import com.rtiming.shared.entry.EntryFormData;
import com.rtiming.shared.entry.EntryFormData.Events.EventsRowData;
import com.rtiming.shared.entry.EventConfiguration;
import com.rtiming.shared.ranking.RankingEventFormData;

/**
 * @author amo
 */
public class BeanUtilityTest {

  @Test
  public void testRankingEventFormData2bean1() throws Exception {
    RankingEventFormData formData = new RankingEventFormData();
    RtRankingEvent bean = BeanUtility.rankingEventFormData2bean(formData);
    assertNotNull("not null", bean);
  }

  @Test
  public void testRankingEventFormData2bean2() throws Exception {
    RankingEventFormData formData = new RankingEventFormData();
    formData.getEvent().setValue(777L);
    formData.setKey(new RtRankingEventKey());
    RtRankingEvent bean = BeanUtility.rankingEventFormData2bean(formData);
    assertNotNull("not null", bean);
    assertEquals("event set", 777, bean.getId().getEventNr().longValue());
  }

  @Test
  public void testRankingEventFormData2bean3() throws Exception {
    RankingEventFormData formData = new RankingEventFormData();
    RtRankingEventKey key = new RtRankingEventKey();
    key.setEventNr(333L);
    formData.setKey(key);
    RtRankingEvent bean = BeanUtility.rankingEventFormData2bean(formData);
    assertNotNull("not null", bean);
    assertEquals("event set", 333, bean.getId().getEventNr().longValue());
  }

  @Test
  public void testRankingEventBean2FormData1() throws Exception {
    RtRankingEvent bean = new RtRankingEvent();
    RankingEventFormData formData = BeanUtility.rankingEventBean2FormData(bean);
    assertNotNull("not null", formData);
  }

  @Test
  public void testRankingEventBean2FormData2() throws Exception {
    RtRankingEvent bean = new RtRankingEvent();
    RtRankingEventKey key = new RtRankingEventKey();
    bean.setId(key);
    RankingEventFormData formData = BeanUtility.rankingEventBean2FormData(bean);
    assertNotNull("not null", formData);
  }

  @Test
  public void testRankingEventBean2FormData3() throws Exception {
    RtRankingEvent bean = new RtRankingEvent();
    RtRankingEventKey key = new RtRankingEventKey();
    key.setEventNr(333L);
    bean.setId(key);
    RankingEventFormData formData = BeanUtility.rankingEventBean2FormData(bean);
    assertNotNull("not null", formData);
    assertEquals("event set", 333, formData.getEvent().getValue().longValue());
  }

  @Test
  public void testEntryBean2formData1() throws Exception {
    EntryBean bean = new EntryBean();
    bean.addParticipation(new ParticipationBean());
    bean.addRace(new RaceBean());
    EntryFormData formData = BeanUtility.entryBean2formData(bean, null);

    assertEquals("1 Row", 1, formData.getRaces().getRowCount());
    assertEquals("1 Row", 1, formData.getEvents().getRowCount());
  }

  @Test
  public void testEntryFormData2bean1() throws Exception {
    EntryFormData formData = new EntryFormData();
    EntryBean bean = BeanUtility.entryFormData2bean(formData, null);

    assertNotNull(bean);
  }

  @Test
  public void testEntryFormData2bean2() throws Exception {
    EntryFormData formData = new EntryFormData();
    EventsRowData newRow = formData.getEvents().createRow();
    ParticipationBean participationBean = new ParticipationBean();
    participationBean.setEventNr(3L);
    participationBean.setStartTime(0L);
    newRow.setParticipationBean(participationBean);
    newRow.setEventNr(3L);
    EventConfiguration configuration = new EventConfiguration();
    EventBean event = new EventBean();
    event.setEventNr(3L);
    event.setEvtZero(new Date());
    configuration.addEvents(event);
    EntryBean bean = BeanUtility.entryFormData2bean(formData, configuration);

    assertNotNull(bean);
    assertEquals(bean.getParticipations().get(0), participationBean);
  }

  @Test
  public void testEntryFormData2bean3() throws Exception {
    EntryFormData formData = new EntryFormData();
    EventsRowData newRow = formData.getEvents().createRow();
    newRow.setEventNr(3L);
    EventConfiguration configuration = new EventConfiguration();
    EventBean event = new EventBean();
    event.setEventNr(3L);
    event.setEvtZero(new Date());
    configuration.addEvents(event);
    EntryBean bean = BeanUtility.entryFormData2bean(formData, configuration);

    assertNotNull(bean);
    assertNotNull(bean.getParticipations().get(0));
  }

}
