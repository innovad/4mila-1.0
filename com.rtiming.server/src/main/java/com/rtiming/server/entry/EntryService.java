package com.rtiming.server.entry;

import java.util.List;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.BEANS;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaTypedQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.common.database.sql.EventBean;
import com.rtiming.shared.entry.EventConfiguration;
import com.rtiming.shared.entry.IEntryService;
import com.rtiming.shared.event.EventClassFormData;
import com.rtiming.shared.event.EventRowData;
import com.rtiming.shared.event.EventsSearchFormData;
import com.rtiming.shared.event.IEventClassProcessService;
import com.rtiming.shared.event.IEventProcessService;
import com.rtiming.shared.event.IEventsOutlineService;

public class EntryService  implements IEntryService {

  @Override
  public EventConfiguration loadEventConfiguration() throws ProcessingException {
    EventConfiguration cache = new EventConfiguration();
    List<EventRowData> list = BEANS.get(IEventsOutlineService.class).getEventTableData(ServerSession.get().getSessionClientNr(), new EventsSearchFormData());

    for (EventRowData event : list) {
      EventBean eventBean = new EventBean();
      eventBean.setEventNr(event.getEventNr());
      eventBean = BEANS.get(IEventProcessService.class).load(eventBean);
      cache.addEvents(eventBean);

      String queryString = "SELECT id.classUid " +
          "FROM RtEventClass " +
          "WHERE id.eventNr = :eventNr " +
          "AND id.clientNr = :sessionClientNr ";
      FMilaTypedQuery<Long> query = JPA.createQuery(queryString, Long.class);
      query.setParameter("eventNr", event.getEventNr());
      query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
      List<Long> classUids = query.getResultList();

      for (Long classUid : classUids) {
        EventClassFormData eventClass = new EventClassFormData();
        eventClass.getEvent().setValue(event.getEventNr());
        eventClass.getClazz().setValue(classUid);
        eventClass.setClientNr(ServerSession.get().getSessionClientNr());
        eventClass = BEANS.get(IEventClassProcessService.class).load(eventClass);
        cache.addClass(eventClass);
      }

    }
    return cache;
  }

}
