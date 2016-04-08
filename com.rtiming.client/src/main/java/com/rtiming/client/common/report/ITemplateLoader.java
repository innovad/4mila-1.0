package com.rtiming.client.common.report;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.common.file.RemoteFile;

import com.rtiming.shared.dao.RtReportTemplateFile;

/**
 * 
 */
public interface ITemplateLoader {

  public RemoteFile createRemoteFileFromTemplateFile(RtReportTemplateFile file) throws ProcessingException;

}
