package com.rtiming.client.result;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.test.data.EventTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualClassTestDataProvider;
import com.rtiming.client.test.data.EventWithRelayClassTestDataProvider;
import com.rtiming.shared.event.course.ClassTypeCodeType;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class ResultTypeNodePageTest {

  private EventTestDataProvider event;

  @Test
  public void testIndividualClass() throws Exception {
    EventWithIndividualClassTestDataProvider eventWithClass = new EventWithIndividualClassTestDataProvider();
    event = eventWithClass;
    ResultTypeNodePage page = new ResultTypeNodePage(ClientSession.get().getSessionClientNr(), event.getEventNr(), null, eventWithClass.getClassUid(), ClassTypeCodeType.IndividualEventCode.ID, null, null);
    loadPage(page);

    Assert.assertEquals("2 Childs", 2, page.getChildNodeCount());
  }

  @Test
  public void testRelayClass() throws Exception {
    EventWithRelayClassTestDataProvider eventWithClass = new EventWithRelayClassTestDataProvider(1);
    event = eventWithClass;
    ResultTypeNodePage page = new ResultTypeNodePage(ClientSession.get().getSessionClientNr(), event.getEventNr(), null, eventWithClass.getLegUids()[0], ClassTypeCodeType.RelayCode.ID, null, null);
    loadPage(page);

    Assert.assertEquals("1 Child", 1, page.getChildNodeCount());
  }

  @Test
  public void testCourse() throws Exception {
    EventWithIndividualClassTestDataProvider eventWithClass = new EventWithIndividualClassTestDataProvider();
    event = eventWithClass;
    ResultTypeNodePage page = new ResultTypeNodePage(ClientSession.get().getSessionClientNr(), event.getEventNr(), null, null, null, eventWithClass.getCourseNr(), null);
    loadPage(page);

    Assert.assertEquals("2 Childs", 2, page.getChildNodeCount());
  }

  private void loadPage(ResultTypeNodePage page) throws ProcessingException {
    page.nodeAddedNotify();
    page.setTreeInternal(ClientSession.get().getDesktop().getOutline(), false);
    page.loadChildren();
  }

  @After
  public void after() throws ProcessingException {
    if (event != null) {
      event.remove();
    }
  }

}
