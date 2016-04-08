package com.rtiming.shared.entry.startblock;

import org.eclipse.scout.rt.shared.TunnelToServer;
import org.eclipse.scout.rt.shared.services.lookup.ILookupService;

@TunnelToServer
public interface IStartblockLookupService extends ILookupService<Long> {
}
