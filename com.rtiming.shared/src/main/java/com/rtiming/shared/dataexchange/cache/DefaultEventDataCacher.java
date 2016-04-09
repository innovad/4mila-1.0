package com.rtiming.shared.dataexchange.cache;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.shared.common.database.sql.BeanUtility;
import com.rtiming.shared.common.database.sql.EventBean;
import com.rtiming.shared.event.EventFormData;
import com.rtiming.shared.event.IEventProcessService;

public class DefaultEventDataCacher extends AbstractDefaultDataCacher<EventFormData> {

  private final Long eventNr;

  public DefaultEventDataCacher(Long eventNr) {
    this.eventNr = eventNr;
  }

  @Override
  protected Long getPrimaryKey(EventFormData formData) {
    return formData.getEventNr();
  }

  @Override
  protected EventFormData createDefaultValue() throws ProcessingException {
    // simply return the selected event
    EventBean event = new EventBean();
    event.setEventNr(eventNr);
    return BeanUtility.eventBean2formData(BEANS.get(IEventProcessService.class).load(event));
  }

}
