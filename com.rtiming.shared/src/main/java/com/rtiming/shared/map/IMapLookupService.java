package com.rtiming.shared.map;

import org.eclipse.scout.rt.shared.TunnelToServer;
import org.eclipse.scout.rt.shared.services.lookup.ILookupService;

@TunnelToServer
public interface IMapLookupService extends ILookupService<Long> {
}
