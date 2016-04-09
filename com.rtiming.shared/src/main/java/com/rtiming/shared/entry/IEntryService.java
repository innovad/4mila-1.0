package com.rtiming.shared.entry;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

@TunnelToServer
public interface IEntryService extends IService {

  EventConfiguration loadEventConfiguration() throws ProcessingException;

}
