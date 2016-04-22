package com.rtiming.client.ecard.download;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.runner.RunWith;

import com.rtiming.client.test.AbstractTablePageTest;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestEnvironmentClientSession.class)
public class DownloadedECardsDuplicateTablePageTest extends AbstractTablePageTest<DownloadedECardsDuplicateTablePage> {

  @Override
  protected DownloadedECardsDuplicateTablePage getTablePage() {
    return new DownloadedECardsDuplicateTablePage();
  }

}
