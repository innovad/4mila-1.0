package com.rtiming.shared.common.security;

import java.util.List;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;

public interface IUpdateService extends IService {

  UpdateInfo checkForUpdate() throws ProcessingException;

  List<Download> loadDownloads() throws ProcessingException;
}
