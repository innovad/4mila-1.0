package com.rtiming.shared.settings.addinfo;

import org.eclipse.scout.rt.shared.TunnelToServer;
import org.eclipse.scout.rt.shared.services.lookup.ILookupService;

@TunnelToServer
public interface IAdditionalInformationLookupService extends ILookupService<Long> {
}
