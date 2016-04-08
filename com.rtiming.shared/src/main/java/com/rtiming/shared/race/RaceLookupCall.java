package com.rtiming.shared.race;

import org.eclipse.scout.commons.annotations.FormData;
import org.eclipse.scout.rt.shared.services.lookup.ILookupService;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

public class RaceLookupCall extends LookupCall<Long> {

  private static final long serialVersionUID = 1L;
  private Long m_eventNr;

  @Override
  protected Class<? extends ILookupService<Long>> getConfiguredService() {
    return IRaceLookupService.class;
  }

  @FormData
  public Long getEventNr() {
    return m_eventNr;
  }

  @FormData
  public void setEventNr(Long eventNr) {
    m_eventNr = eventNr;
  }
}
