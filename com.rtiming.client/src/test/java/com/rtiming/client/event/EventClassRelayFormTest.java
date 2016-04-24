package com.rtiming.client.event;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.testing.client.ScoutClientAssert;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.TestClientSession;
import com.rtiming.client.test.data.EventWithRelayClassTestDataProvider;
import com.rtiming.shared.event.course.ClassTypeCodeType;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class EventClassRelayFormTest {

  private EventWithRelayClassTestDataProvider relay;

  @Test
  public void testRelayParentSettings() throws Exception {
    relay = new EventWithRelayClassTestDataProvider();

    Long parentUid = relay.getParentUid();
    EventClassForm eventClass = new EventClassForm();
    eventClass.getEventField().setValue(relay.getEventNr());
    eventClass.getClazzField().setValue(parentUid);
    eventClass.getParentField().setValue(null);
    eventClass.startModify();

    ScoutClientAssert.assertDisabled(eventClass.getCourseField());
    ScoutClientAssert.assertInvisible(eventClass.getParentField());
    Assert.assertNull("Parent does not have a course", eventClass.getCourseField().getValue());
    Assert.assertEquals("Relay Type", ClassTypeCodeType.RelayCode.ID, eventClass.getTypeField().getValue().longValue());

    eventClass.getTypeField().setValue(ClassTypeCodeType.IndividualEventCode.ID);
    ScoutClientAssert.assertEnabled(eventClass.getCourseField());
    ScoutClientAssert.assertInvisible(eventClass.getParentField());

    eventClass.getCourseField().setValue(relay.getCourseNr(0));
    eventClass.getTypeField().setValue(ClassTypeCodeType.RelayCode.ID);
    ScoutClientAssert.assertDisabled(eventClass.getCourseField());
    ScoutClientAssert.assertInvisible(eventClass.getParentField());
    Assert.assertNull("Parent does not have a course", eventClass.getCourseField().getValue());

    eventClass.doClose();
  }

  @Test
  public void testRelayLegSettings() throws Exception {
    relay = new EventWithRelayClassTestDataProvider();
    Long parentUid = relay.getParentUid();

    EventClassForm eventClass = new EventClassForm();
    eventClass.getEventField().setValue(relay.getEventNr());
    eventClass.getClazzField().setValue(relay.getLegUid(0));
    eventClass.getParentField().setValue(parentUid);
    eventClass.startModify();

    ScoutClientAssert.assertDisabled(eventClass.getParentField());
    ScoutClientAssert.assertVisible(eventClass.getParentField());
    ScoutClientAssert.assertDisabled(eventClass.getClazzField());
    ScoutClientAssert.assertVisible(eventClass.getClazzField());

    ScoutClientAssert.assertEnabled(eventClass.getCourseField());
    ScoutClientAssert.assertDisabled(eventClass.getTimePrecisionField());
    ScoutClientAssert.assertDisabled(eventClass.getFeeGroupField());

    Assert.assertEquals("Course", relay.getCourseNr(0), eventClass.getCourseField().getValue());
  }

  @After
  public void after() throws ProcessingException {
    relay.remove();
  }

}
