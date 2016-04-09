package com.rtiming.client.common.infodisplay;

import org.eclipse.scout.rt.client.IClientSession;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.shared.common.database.sql.EventBean;
import com.rtiming.shared.event.IEventProcessService;
import com.rtiming.shared.settings.IDefaultProcessService;

/**
 * 
 */
public class InfoDisplayIdleJob extends AbstractInfoDisplayUpdateJob {

  private final String message;

  public InfoDisplayIdleJob(IClientSession session) {
    super(session);
    message = "";
  }

  public InfoDisplayIdleJob(String message, IClientSession session) {
    super(session);
    this.message = message;
  }

  @Override
  protected String prepareURL() throws ProcessingException {
    Long eventNr = BEANS.get(IDefaultProcessService.class).getDefaultEventNr();
    EventBean event = new EventBean();
    if (eventNr != null) {
      event.setEventNr(eventNr);
      event = BEANS.get(IEventProcessService.class).load(event);
    }

    String eventName = event.getName();
    String eventMap = event.getMap();

    String location = InfoDisplayUtility.getInfoDisplayHtmlUrl("infoDisplayIdle.html");

    location = InfoDisplayUtility.addParameter(location, "event", eventName);
    location = InfoDisplayUtility.addParameter(location, "map", eventMap);
    location = InfoDisplayUtility.addParameter(location, "message", message);

    return location;
  }

}
