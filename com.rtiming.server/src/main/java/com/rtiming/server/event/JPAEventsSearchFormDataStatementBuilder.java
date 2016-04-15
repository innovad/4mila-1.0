package com.rtiming.server.event;

import javax.persistence.criteria.Root;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CollectionUtility;

import com.rtiming.server.common.AbstractJPASearchFormDataStatementBuilder;
import com.rtiming.shared.dao.RtEvent;
import com.rtiming.shared.dao.RtEvent_;
import com.rtiming.shared.event.EventsSearchFormData;

/**
 *
 */
public class JPAEventsSearchFormDataStatementBuilder extends AbstractJPASearchFormDataStatementBuilder<EventsSearchFormData> {

  private final Root<RtEvent> root;

  public JPAEventsSearchFormDataStatementBuilder(Root<RtEvent> root) {
    super();
    this.root = root;
  }

  @Override
  public void build(EventsSearchFormData searchFormData) throws ProcessingException {
    super.build(searchFormData);

    // name
    addStringWherePart(root.get(RtEvent_.name), searchFormData.getName().getValue());

    // map
    addStringWherePart(root.get(RtEvent_.map), searchFormData.getMapp().getValue());

    // location
    addStringWherePart(root.get(RtEvent_.location), searchFormData.getLocation().getValue());

    // type
    addLongWherePart(root.get(RtEvent_.typeUid), CollectionUtility.toArray(searchFormData.getType().getValue(), Long.class));

    // time from
    addDateGreaterThanOrEqualsWherePart(root.get(RtEvent_.evtZero), searchFormData.getZeroTimeFrom().getValue());

    // time to
    addDateLessThanOrEqualsWherePart(root.get(RtEvent_.evtZero), searchFormData.getZeroTimeTo().getValue());
  }

}
