package com.rtiming.shared.event.course;

import org.eclipse.scout.rt.shared.services.lookup.ILookupService;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

public class ControlLookupCall extends LookupCall<Long> {

  private static final long serialVersionUID = 1L;
  private Long m_eventNr;
  private Long m_typeUid;
  private boolean m_isDisplayEvent = false;

  @Override
  protected Class<? extends ILookupService<Long>> getConfiguredService() {
    return IControlLookupService.class;
  }

  public Long getEventNr() {
    return m_eventNr;
  }

  public void setEventNr(Long eventNr) {
    m_eventNr = eventNr;
  }

  public Long getTypeUid() {
    return m_typeUid;
  }

  public void setTypeUid(Long typeUid) {
    m_typeUid = typeUid;
  }

  public boolean isIsDisplayEvent() {
    return m_isDisplayEvent;
  }

  public void setIsDisplayEvent(boolean isDisplayEvent) {
    m_isDisplayEvent = isDisplayEvent;
  }
}
