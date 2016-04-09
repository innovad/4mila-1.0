package com.rtiming.shared.entry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.util.CompareUtility;

import com.rtiming.shared.common.database.sql.EventBean;
import com.rtiming.shared.event.EventClassFormData;

/**
 * 
 */
public class EventConfiguration implements Serializable {

  private static final long serialVersionUID = 1L;
  private final List<EventBean> events;
  private final List<EventClassFormData> classes;

  public EventConfiguration() {
    events = new ArrayList<>();
    classes = new ArrayList<>();
  }

  public List<EventBean> getEvents() {
    return events;
  }

  public List<EventClassFormData> getClasses() {
    return classes;
  }

  public void addEvents(EventBean event) {
    events.add(event);
  }

  public void addClass(EventClassFormData clazz) {
    classes.add(clazz);
  }

  public EventClassFormData getEventClassInfo(Long eventNr, Long classUid) {
    // TODO caching with hash map
    for (EventClassFormData formData : getClasses()) {
      if (CompareUtility.equals(formData.getEvent().getValue(), eventNr) && CompareUtility.equals(formData.getClazz().getValue(), classUid)) {
        return formData;
      }
    }
    return null;
  }

  public EventBean getEvent(Long eventNr) {
    for (EventBean event : getEvents()) {
      if (CompareUtility.equals(event.getEventNr(), eventNr)) {
        return event;
      }
    }
    return null;
  }

}
