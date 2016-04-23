package com.rtiming.client.common.report.template;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.test.AbstractTablePageTest;
import com.rtiming.shared.services.code.ReportTypeCodeType;

/**
 * @author Adrian Moser
 */
@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class ReportTemplateColumnsTablePageTest extends AbstractTablePageTest<ReportTemplateColumnsTablePage> {

  @Override
  protected ReportTemplateColumnsTablePage getTablePage() throws ProcessingException {
    return new ReportTemplateColumnsTablePage(ReportTypeCodeType.ResultsCode.ID);
  }

}
