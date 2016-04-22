package com.rtiming.server.test.data;

import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.dao.RtEvent;
import com.rtiming.shared.dao.RtEventClass;
import com.rtiming.shared.dao.RtEventClassKey;
import com.rtiming.shared.dao.RtEventKey;
import com.rtiming.shared.dao.RtUc;
import com.rtiming.shared.dao.RtUcKey;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.event.course.ClassTypeCodeType;
import com.rtiming.shared.race.TimePrecisionCodeType;
import com.rtiming.shared.services.code.CourseGenerationCodeType;

public class EventClassTestDataProvider {

  private RtEvent event;
  private RtUc clazz;
  private RtEventClass eventClass;

  public EventClassTestDataProvider() throws ProcessingException {
    create();
  }

  private void create() throws ProcessingException {
    event = new RtEvent();
    event.setId(RtEventKey.create((Long) null));
    JPA.merge(event);

    clazz = new RtUc();
    clazz.setActive(true);
    clazz.setCodeType(ClassCodeType.ID);
    clazz.setId(RtUcKey.create((Long) null));
    JPA.merge(clazz);

    eventClass = new RtEventClass();
    RtEventClassKey id = new RtEventClassKey();
    id.setClassUid(clazz.getId().getId());
    id.setEventNr(event.getId().getId());
    id.setClientNr(ServerSession.get().getSessionClientNr());
    eventClass.setTeamSizeMin(4L);
    eventClass.setSortcode(1L);
    eventClass.setTimePrecisionUid(TimePrecisionCodeType.Precision100sCode.ID);
    eventClass.setCourseGenerationTypeUid(CourseGenerationCodeType.CourseGenerationIndividualControlsCode.ID);
    eventClass.setId(id);
    eventClass.setTypeUid(ClassTypeCodeType.IndividualEventCode.ID);
    JPA.merge(eventClass);
  }

  public void remove() throws ProcessingException {
    if (eventClass != null) {
      JPA.remove(eventClass);
    }
    if (clazz != null) {
      JPA.remove(clazz);
    }
    if (event != null) {
      JPA.remove(event);
    }
  }

  public RtEvent getEvent() {
    return event;
  }

  public RtEventClass getEventClass() {
    return eventClass;
  }

  public RtUc getClazz() {
    return clazz;
  }

  public void setEvent(RtEvent event) {
    this.event = event;
  }

  public void setClazz(RtUc clazz) {
    this.clazz = clazz;
  }

  public void setEventClass(RtEventClass eventClass) {
    this.eventClass = eventClass;
  }

}
