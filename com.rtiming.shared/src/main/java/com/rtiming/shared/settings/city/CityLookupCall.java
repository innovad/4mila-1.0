package com.rtiming.shared.settings.city;

import org.eclipse.scout.rt.shared.services.lookup.ILookupService;

import com.rtiming.shared.common.AbstractBasicLookupCall;

public class CityLookupCall extends AbstractBasicLookupCall {

  private static final long serialVersionUID = 1L;

  @Override
  protected Class<? extends ILookupService<Long>> getConfiguredService() {
    return ICityLookupService.class;
  }
}
