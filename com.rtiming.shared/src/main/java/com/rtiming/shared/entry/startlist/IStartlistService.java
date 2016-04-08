package com.rtiming.shared.entry.startlist;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

@TunnelToServer
public interface IStartlistService extends IService {

  void createStartlists(Long[] startlistSettingNr) throws ProcessingException;

  boolean existsRaceWithStartTime(Long[] startlistSettingNrs) throws ProcessingException;

}
