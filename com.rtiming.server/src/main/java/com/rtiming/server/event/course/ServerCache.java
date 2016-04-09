package com.rtiming.server.event.course;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.TypeCastUtility;

import com.rtiming.server.ServerSession;
import com.rtiming.shared.event.EventRowData;
import com.rtiming.shared.event.EventsSearchFormData;
import com.rtiming.shared.event.IEventsOutlineService;
import com.rtiming.shared.event.course.ControlsSearchFormData;
import com.rtiming.shared.event.course.ReplacementControlRowData;

public class ServerCache {

  private static Object CACHE_LOCK = new Object();

  private static Map<Long, Map<Long, List<String>>> replacementControls;

  public static Map<Long, List<String>> getReplacementControls(Long eventNr) throws ProcessingException {
    synchronized (CACHE_LOCK) {
      if (replacementControls == null) {
        replacementControls = loadReplacementControlsInternal();
      }
      Map<Long, List<String>> map = replacementControls.get(eventNr);
      if (map != null) {
        return map;
      }
      return new HashMap<>();
    }
  }

  private static Map<Long, Map<Long, List<String>>> loadReplacementControlsInternal() throws ProcessingException {
    // Load all events 
    List<EventRowData> events = BEANS.get(IEventsOutlineService.class).getEventTableData(ServerSession.get().getSessionClientNr(), new EventsSearchFormData());
    List<Long> eventNrs = new ArrayList<>();
    for (EventRowData event : events) {
      eventNrs.add(event.getEventNr());
    }

    Map<Long, Map<Long, List<String>>> results = new HashMap<Long, Map<Long, List<String>>>();
    for (Long eventNr : eventNrs) {
      Map<Long, List<String>> replacementControlMap = new HashMap<>();
      results.put(eventNr, replacementControlMap);
      Object[][] controls = BEANS.get(IEventsOutlineService.class).getControlTableData(eventNr, new ControlsSearchFormData());
      List<Long> controlNrs = new ArrayList<>();
      for (Object[] row : controls) {
        controlNrs.add(TypeCastUtility.castValue(row[0], Long.class));
      }

      for (Long controlNr : controlNrs) {
        List<String> replacementList = new ArrayList<>();
        replacementControlMap.put(controlNr, replacementList);
        List<ReplacementControlRowData> replacementControlData = BEANS.get(IEventsOutlineService.class).getReplacementControlTableData(controlNr);
        for (ReplacementControlRowData row : replacementControlData) {
          String replacementNo = row.getReplacementControlNo();
          replacementList.add(replacementNo);
        }
      }
    }

    return results;
  }

  public static void resetCache() throws ProcessingException {
    synchronized (CACHE_LOCK) {
      replacementControls = null;
    }
  }

}
