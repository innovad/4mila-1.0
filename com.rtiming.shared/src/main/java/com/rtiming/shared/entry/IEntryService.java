package com.rtiming.shared.entry;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;

public interface IEntryService extends IService {

  EventConfiguration loadEventConfiguration() throws ProcessingException;

}
