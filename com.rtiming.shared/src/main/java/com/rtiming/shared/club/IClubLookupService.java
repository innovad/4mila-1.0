package com.rtiming.shared.club;

import org.eclipse.scout.rt.shared.TunnelToServer;
import org.eclipse.scout.rt.shared.services.lookup.ILookupService;

@TunnelToServer
public interface IClubLookupService extends ILookupService<Long> {
}
