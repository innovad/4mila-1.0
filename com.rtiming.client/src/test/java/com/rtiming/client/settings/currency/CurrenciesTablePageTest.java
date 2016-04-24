package com.rtiming.client.settings.currency;

import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.runner.RunWith;

import com.rtiming.client.TestClientSession;
import com.rtiming.client.test.AbstractTablePageTest;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class CurrenciesTablePageTest extends AbstractTablePageTest<CurrenciesTablePage> {

  @Override
  protected CurrenciesTablePage getTablePage() {
    return new CurrenciesTablePage();
  }

}
