package com.rtiming.shared.settings.city;

import org.eclipse.scout.rt.shared.TunnelToServer;
import org.eclipse.scout.rt.shared.services.lookup.ILookupService;

@TunnelToServer
public interface ICityLookupService extends ILookupService<Long> {
}
