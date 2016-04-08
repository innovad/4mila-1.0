package com.rtiming.shared.event.course;

import org.eclipse.scout.rt.shared.TunnelToServer;
import org.eclipse.scout.rt.shared.services.lookup.ILookupService;

@TunnelToServer
public interface IControlLookupService extends ILookupService<Long> {
}
