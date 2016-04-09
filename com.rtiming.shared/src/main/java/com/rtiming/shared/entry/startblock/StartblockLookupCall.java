package com.rtiming.shared.entry.startblock;

import org.eclipse.scout.rt.shared.services.lookup.ILookupService;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

public class StartblockLookupCall extends LookupCall<Long> {

  private static final long serialVersionUID = 1L;
  private Long m_eventNr;

  @Override
  protected Class<? extends ILookupService<Long>> getConfiguredService() {
    return IStartblockLookupService.class;
  }

  public Long getEventNr() {
    return m_eventNr;
  }

  public void setEventNr(Long eventNr) {
    m_eventNr = eventNr;
  }
}
