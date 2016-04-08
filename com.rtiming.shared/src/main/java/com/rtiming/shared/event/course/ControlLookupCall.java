package com.rtiming.shared.event.course;

import org.eclipse.scout.commons.annotations.FormData;
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

  @FormData
  public Long getEventNr() {
    return m_eventNr;
  }

  @FormData
  public void setEventNr(Long eventNr) {
    m_eventNr = eventNr;
  }

  @FormData
  public Long getTypeUid() {
    return m_typeUid;
  }

  @FormData
  public void setTypeUid(Long typeUid) {
    m_typeUid = typeUid;
  }

  @FormData
  public boolean isIsDisplayEvent() {
    return m_isDisplayEvent;
  }

  @FormData
  public void setIsDisplayEvent(boolean isDisplayEvent) {
    m_isDisplayEvent = isDisplayEvent;
  }
}
