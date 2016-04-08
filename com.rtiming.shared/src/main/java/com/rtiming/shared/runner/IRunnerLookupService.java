package com.rtiming.shared.runner;

import org.eclipse.scout.rt.shared.TunnelToServer;
import org.eclipse.scout.rt.shared.services.lookup.ILookupService;

@TunnelToServer
public interface IRunnerLookupService extends ILookupService<Long> {
}
