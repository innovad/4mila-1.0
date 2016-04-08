package com.rtiming.shared.race;

import org.eclipse.scout.rt.shared.TunnelToServer;
import org.eclipse.scout.rt.shared.services.lookup.ILookupService;

@TunnelToServer
public interface IRaceLookupService extends ILookupService<Long> {
}
