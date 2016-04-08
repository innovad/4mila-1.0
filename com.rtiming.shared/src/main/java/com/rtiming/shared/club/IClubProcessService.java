package com.rtiming.shared.club;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;

import com.rtiming.shared.common.database.sql.ClubBean;
import com.rtiming.shared.dao.RtClubKey;

public interface IClubProcessService extends IService {

  ClubBean prepareCreate(ClubBean bean) throws ProcessingException;

  ClubBean create(ClubBean bean) throws ProcessingException;

  ClubBean load(ClubBean bean) throws ProcessingException;

  ClubBean store(ClubBean bean) throws ProcessingException;

  ClubBean findClub(String name) throws ProcessingException;

  int delete(RtClubKey... keys) throws ProcessingException;
}
