package com.rtiming.shared.entry.startlist;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;

public interface IStartlistService extends IService {

  void createStartlists(Long[] startlistSettingNr) throws ProcessingException;

  boolean existsRaceWithStartTime(Long[] startlistSettingNrs) throws ProcessingException;

}
