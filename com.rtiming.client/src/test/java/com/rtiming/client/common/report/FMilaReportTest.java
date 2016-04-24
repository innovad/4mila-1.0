package com.rtiming.client.common.report;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.TestClientSession;
import com.rtiming.shared.services.code.ReportTypeCodeType;

import net.sf.jasperreports.engine.JasperPrint;

/**
 * @author amo
 */
@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class FMilaReportTest {

  @Test(expected = IllegalArgumentException.class)
  public void testTemplate1() throws Exception {
    FMilaReport report = new TestReport();
    report.setTemplate(null, null);
  }

  @Test
  public void testTemplate2() throws Exception {
    FMilaReport report = new TestReport();
    report.setTemplate(ReportTypeCodeType.DefaultCode.ID, null);
  }

  class TestReport extends FMilaReport {

    @Override
    protected JasperPrint createJasperPrint() throws ProcessingException {
      return null;
    }

  }

}
