package com.rtiming.server.entry;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.shared.entry.EventConfiguration;
import com.rtiming.shared.entry.IEntryService;
import com.rtiming.shared.event.EventRowData;
import com.rtiming.shared.event.EventsSearchFormData;
import com.rtiming.shared.event.IEventsOutlineService;

/**
 * @author amo
 */
@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class EntryServiceTest {

  @Test
  public void testLoadEventConfiguration1() throws Exception {
    BEANS.get(IEntryService.class).loadEventConfiguration();
  }

  @Test
  public void testLoadEventConfiguration2() throws Exception {
    List<EventRowData> list = BEANS.get(IEventsOutlineService.class).getEventTableData(ServerSession.get().getSessionClientNr(), new EventsSearchFormData());

    EventConfiguration config = BEANS.get(IEntryService.class).loadEventConfiguration();
    assertEquals("same number of events", list.size(), config.getEvents().size());
  }

}
