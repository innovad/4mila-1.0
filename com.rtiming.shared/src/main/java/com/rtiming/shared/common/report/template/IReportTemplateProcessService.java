package com.rtiming.shared.common.report.template;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

import com.rtiming.shared.dao.RtReportTemplate;
import com.rtiming.shared.dao.RtReportTemplateKey;

@TunnelToServer
public interface IReportTemplateProcessService extends IService {

  String SERVER_MAP_DIR = "reports";

  String TEMPLATE_SUFFIX_JRXML = "jrxml";

  String TEMPLATE_SUFFIX_JRTX = "jrtx";

  RtReportTemplate prepareCreate(RtReportTemplate bean) throws ProcessingException;

  RtReportTemplate create(RtReportTemplate bean) throws ProcessingException;

  RtReportTemplate load(RtReportTemplateKey key) throws ProcessingException;

  RtReportTemplate store(RtReportTemplate bean) throws ProcessingException;

  void delete(RtReportTemplateKey key) throws ProcessingException;
}
