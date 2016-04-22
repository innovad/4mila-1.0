package com.rtiming.client.common.report.template;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.client.ui.action.menu.IMenu;
import org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn;
import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.runner.RunWith;

import com.rtiming.client.test.AbstractEntityTablePageTest;

/**
 * @author Adrian Moser
 */
@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestEnvironmentClientSession.class)
public class ReportTemplatesTablePageTest extends AbstractEntityTablePageTest<ReportTemplatesTablePage, Long> {

  @Override
  protected ReportTemplatesTablePage getTablePage() throws ProcessingException {
    return new ReportTemplatesTablePage();
  }

  @Override
  protected Class<? extends IMenu> getNewMenu() {
    return null;
  }

  @Override
  protected Class<? extends IMenu> getDeleteMenu() {
    return null;
  }

  @Override
  protected Class<? extends IMenu> getEditMenu() {
    return null;
  }

  @Override
  protected Class<? extends IForm> getForm() {
    return null;
  }

  @Override
  protected Class<? extends IColumn<Long>> getPrimaryKeyColumn() {
    return null;
  }

  @Override
  protected Long getPrimaryKey() throws ProcessingException {
    return null;
  }

}
