package com.rtiming.shared.race;

import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

import com.rtiming.shared.common.database.sql.RaceBean;

@TunnelToServer
public interface IRaceProcessService extends IService {

  RaceBean prepareCreate(RaceBean bean) throws ProcessingException;

  RaceBean create(RaceBean bean) throws ProcessingException;

  RaceBean load(RaceBean bean) throws ProcessingException;

  RaceBean store(RaceBean bean) throws ProcessingException;

  Long findRaceNr(long eventNr, String eCardNo) throws ProcessingException;

  List<RaceBean> findByAccountNr(Long accountNr) throws ProcessingException;

  void delete(Long... raceNrs) throws ProcessingException;

}
