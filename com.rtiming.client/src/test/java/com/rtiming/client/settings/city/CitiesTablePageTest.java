package com.rtiming.client.settings.city;

import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.test.AbstractTablePageTest;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class CitiesTablePageTest extends AbstractTablePageTest<CitiesTablePage> {

  @Override
  protected CitiesTablePage getTablePage() {
    return new CitiesTablePage();
  }

}
