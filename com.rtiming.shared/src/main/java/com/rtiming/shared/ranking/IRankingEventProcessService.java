package com.rtiming.shared.ranking;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

import com.rtiming.shared.dao.RtRankingEvent;
import com.rtiming.shared.dao.RtRankingEventKey;

@TunnelToServer
public interface IRankingEventProcessService extends IService {

  RtRankingEvent prepareCreate(RtRankingEvent bean) throws ProcessingException;

  RtRankingEvent create(RtRankingEvent bean) throws ProcessingException;

  RtRankingEvent load(RtRankingEventKey key) throws ProcessingException;

  RtRankingEvent store(RtRankingEvent bean) throws ProcessingException;

  void delete(RtRankingEventKey key) throws ProcessingException;
}
