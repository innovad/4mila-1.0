package com.rtiming.shared.entry;

import org.eclipse.scout.rt.shared.TunnelToServer;
import org.eclipse.scout.rt.shared.services.lookup.ILookupService;

@TunnelToServer
public interface IRegistrationLookupService extends ILookupService<Long> {
}
