package com.rtiming.shared.common.security;

import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

@TunnelToServer
public interface IUpdateService extends IService {

  UpdateInfo checkForUpdate() throws ProcessingException;

  List<Download> loadDownloads() throws ProcessingException;
}
