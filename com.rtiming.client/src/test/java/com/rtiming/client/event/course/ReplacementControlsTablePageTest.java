package com.rtiming.client.event.course;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.test.AbstractTablePageTest;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.client.test.data.EventTestDataProvider;
import com.rtiming.shared.event.IEventProcessService;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class ReplacementControlsTablePageTest extends AbstractTablePageTest<ReplacementControlsTablePage> {

  private EventTestDataProvider event;
  private ControlForm control;

  @Before
  public void before() throws ProcessingException {
    event = new EventTestDataProvider();

    control = new ControlForm();
    control.getEventField().setValue(event.getEventNr());
    control.startNew();
    FormTestUtility.fillFormFields(control);
    control.doOk();
  }

  @Override
  protected ReplacementControlsTablePage getTablePage() throws ProcessingException {
    return new ReplacementControlsTablePage(control.getControlNr(), event.getEventNr());
  }

  @After
  public void after() throws ProcessingException {
    BEANS.get(IEventProcessService.class).delete(event.getEventNr(), true);
  }

}
