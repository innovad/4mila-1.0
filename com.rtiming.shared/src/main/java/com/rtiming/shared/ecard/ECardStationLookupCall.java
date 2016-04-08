package com.rtiming.shared.ecard;

import org.eclipse.scout.rt.shared.services.lookup.ILookupService;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

import com.rtiming.shared.ecard.download.IECardStationLookupService;

public class ECardStationLookupCall extends LookupCall<Long> {

  private static final long serialVersionUID = 1L;

  @Override
  protected Class<? extends ILookupService<Long>> getConfiguredService() {
    return IECardStationLookupService.class;
  }
}
