package com.rtiming.client.settings.clazz;

import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.test.AbstractTablePageTest;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class ClassesTablePageTest extends AbstractTablePageTest<ClassesTablePage> {

  @Override
  protected ClassesTablePage getTablePage() {
    return new ClassesTablePage();
  }

}
