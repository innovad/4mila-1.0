package com.rtiming.shared.settings.clazz;

import java.util.List;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;

import com.rtiming.shared.dao.RtClassAge;
import com.rtiming.shared.dao.RtClassAgeKey;

public interface IAgeProcessService extends IService {

  RtClassAge prepareCreate(RtClassAge bean) throws ProcessingException;

  RtClassAge create(RtClassAge bean) throws ProcessingException;

  RtClassAge load(RtClassAgeKey key) throws ProcessingException;

  RtClassAge store(RtClassAge bean) throws ProcessingException;

  RtClassAge delete(RtClassAgeKey bean) throws ProcessingException;

  List<RtClassAge> loadAgeConfiguration() throws ProcessingException;
}
