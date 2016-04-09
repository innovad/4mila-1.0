package com.rtiming.shared.club;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

import com.rtiming.shared.common.database.sql.ClubBean;
import com.rtiming.shared.dao.RtClubKey;

@TunnelToServer
public interface IClubProcessService extends IService {

  ClubBean prepareCreate(ClubBean bean) throws ProcessingException;

  ClubBean create(ClubBean bean) throws ProcessingException;

  ClubBean load(ClubBean bean) throws ProcessingException;

  ClubBean store(ClubBean bean) throws ProcessingException;

  ClubBean findClub(String name) throws ProcessingException;

  int delete(RtClubKey... keys) throws ProcessingException;
}
