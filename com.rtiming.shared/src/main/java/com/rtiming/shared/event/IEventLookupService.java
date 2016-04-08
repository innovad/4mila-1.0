package com.rtiming.shared.event;

import org.eclipse.scout.rt.shared.TunnelToServer;
import org.eclipse.scout.rt.shared.services.lookup.ILookupService;

@TunnelToServer
public interface IEventLookupService extends ILookupService<Long> {
}
