package com.rtiming.shared.entry;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

import com.rtiming.shared.common.database.sql.EntryBean;

@TunnelToServer
public interface IEntryProcessService extends IService {

  EntryBean prepareCreate(EntryBean bean) throws ProcessingException;

  EntryBean create(EntryBean bean) throws ProcessingException;

  EntryBean load(EntryBean bean) throws ProcessingException;

  EntryBean store(EntryBean bean) throws ProcessingException;

  void delete(EntryBean bean) throws ProcessingException;

}
