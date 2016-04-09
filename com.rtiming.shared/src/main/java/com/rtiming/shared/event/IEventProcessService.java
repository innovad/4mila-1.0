package com.rtiming.shared.event;

import java.util.Date;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

import com.rtiming.shared.common.database.sql.EventBean;

@TunnelToServer
public interface IEventProcessService extends IService {

  EventBean prepareCreate(EventBean formData) throws ProcessingException;

  EventBean create(EventBean formData) throws ProcessingException;

  EventBean load(EventBean formData) throws ProcessingException;

  EventBean store(EventBean formData) throws ProcessingException;

  Date getZeroTime(long eventNr) throws ProcessingException;

  long getRunnerStartedCount(long eventNr, Long classUid, Long courseNr) throws ProcessingException;

  void syncWithOnline(long eventNr) throws ProcessingException;

  void delete(Long eventNr, boolean cleanup) throws ProcessingException;
}
