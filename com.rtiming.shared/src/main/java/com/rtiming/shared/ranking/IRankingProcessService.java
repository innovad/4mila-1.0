package com.rtiming.shared.ranking;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

import com.rtiming.shared.dao.RtRanking;
import com.rtiming.shared.dao.RtRankingKey;

@TunnelToServer
public interface IRankingProcessService extends IService {

  RtRanking prepareCreate(RtRanking bean) throws ProcessingException;

  RtRanking create(RtRanking bean) throws ProcessingException;

  RtRanking load(RtRankingKey key) throws ProcessingException;

  RtRanking store(RtRanking bean) throws ProcessingException;

  void delete(RtRankingKey key) throws ProcessingException;
}
