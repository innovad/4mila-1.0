package com.rtiming.shared.settings.fee;

import org.eclipse.scout.rt.shared.TunnelToServer;
import org.eclipse.scout.rt.shared.services.lookup.ILookupService;

@TunnelToServer
public interface IFeeGroupLookupService extends ILookupService<Long> {
}
