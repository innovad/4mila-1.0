package com.rtiming.shared.race;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

import com.rtiming.shared.common.database.sql.RaceBean;

@TunnelToServer
public interface IRaceService extends IService {

  void inflateRaceControls(Long[] raceNrs) throws ProcessingException;

  void validateAndPersistRace(long raceNr) throws ProcessingException;

  RaceBean validateRace(long raceNr) throws ProcessingException;

  void reset(long raceNr) throws ProcessingException;

}
