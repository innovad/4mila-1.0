package com.rtiming.shared.map;

import java.util.List;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;

import com.rtiming.shared.dao.RtMap;
import com.rtiming.shared.dao.RtMapKey;

public interface IMapProcessService extends IService {

  String SERVER_MAP_DIR = "maps";

  RtMap prepareCreate(RtMap formData) throws ProcessingException;

  RtMap create(RtMap formData) throws ProcessingException;

  RtMap load(RtMapKey key) throws ProcessingException;

  RtMap store(RtMap formData) throws ProcessingException;

  RtMap findMap(long eventNr, long clientNr) throws ProcessingException;

  List<Long> getAllMaps(Long clientNr) throws ProcessingException;

  void delete(RtMapKey key) throws ProcessingException;
}
