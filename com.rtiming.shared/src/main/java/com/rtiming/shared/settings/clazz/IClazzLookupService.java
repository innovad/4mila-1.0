package com.rtiming.shared.settings.clazz;

import org.eclipse.scout.rt.shared.TunnelToServer;
import org.eclipse.scout.rt.shared.services.lookup.ILookupService;

@TunnelToServer
public interface IClazzLookupService extends ILookupService<Long> {
}
