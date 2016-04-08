package com.rtiming.shared.race;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;


public interface IRaceControlProcessService extends IService {

  RaceControlFormData prepareCreate(RaceControlFormData formData) throws ProcessingException;

  RaceControlFormData create(RaceControlFormData formData) throws ProcessingException;

  RaceControlFormData load(RaceControlFormData formData) throws ProcessingException;

  RaceControlFormData store(RaceControlFormData formData) throws ProcessingException;

  void delete(Long... raceControlNrs) throws ProcessingException;
}
