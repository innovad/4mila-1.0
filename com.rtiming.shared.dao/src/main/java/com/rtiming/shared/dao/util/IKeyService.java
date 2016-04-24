package com.rtiming.shared.dao.util;

import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

@TunnelToServer
public interface IKeyService extends IService {

  public Long getClientNr();

  public Long verifyId(Long id);

}
