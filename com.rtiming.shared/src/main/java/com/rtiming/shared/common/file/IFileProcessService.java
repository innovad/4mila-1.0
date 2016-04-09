package com.rtiming.shared.common.file;

import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

@TunnelToServer
public interface IFileProcessService extends IService {

  String writeDataToFile(Long pkNr, Long clientNr, String format, byte[] imageData, String path) throws ProcessingException;

  byte[] loadFile(Long pkNr, Long clientNr, String format, String path) throws ProcessingException;

  void cleanUpFiles(List<Long> existingPkNrs, Long clientNr, String path) throws ProcessingException;

  void deleteFile(Long pkNr, Long clientNr, String format, String[] suffixes) throws ProcessingException;

}
