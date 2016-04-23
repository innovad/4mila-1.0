package com.rtiming.client.event;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.testing.client.ScoutClientAssert;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.test.data.EventTestDataProvider;
import com.rtiming.shared.event.course.ClassTypeCodeType;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class EventClassFormTeamSizeTest {

  private static EventTestDataProvider event;

  @BeforeClass
  public static void beforeClass() throws ProcessingException {
    event = new EventTestDataProvider();
  }

  @Test
  public void testTeamSizeIndividual() throws Exception {
    EventClassForm form = new EventClassForm();
    form.getEventField().setValue(event.getEventNr());
    form.getParentField().setValue(null);
    form.startNew();

    Assert.assertEquals("Default Individual Type", ClassTypeCodeType.IndividualEventCode.ID, form.getTypeField().getValue().longValue());
    ScoutClientAssert.assertInvisible(form.getTeamSizeBox());
    Assert.assertEquals("Team Min 1", 1L, form.getTeamSizeMinField().getValue().longValue());
    Assert.assertEquals("Team Max 1", 1L, form.getTeamSizeMaxField().getValue().longValue());

    form.doClose();
  }

  @Test
  public void testTeamSizeTeam() throws Exception {
    EventClassForm form = new EventClassForm();
    form.getEventField().setValue(event.getEventNr());
    form.getParentField().setValue(null);
    form.startNew();

    form.getTypeField().setValue(ClassTypeCodeType.TeamCombinedCourseCode.ID);

    ScoutClientAssert.assertVisible(form.getTeamSizeBox());
    Assert.assertEquals("Team Min 1", 1L, form.getTeamSizeMinField().getValue().longValue());
    Assert.assertEquals("Team Max 1", 1L, form.getTeamSizeMaxField().getValue().longValue());

    form.getTypeField().setValue(ClassTypeCodeType.IndividualScoreEventCode.ID);

    ScoutClientAssert.assertInvisible(form.getTeamSizeBox());
    Assert.assertEquals("Team Min 1", 1L, form.getTeamSizeMinField().getValue().longValue());
    Assert.assertEquals("Team Max 1", 1L, form.getTeamSizeMaxField().getValue().longValue());

    form.doClose();
  }

  @AfterClass
  public static void afterClass() throws ProcessingException {
    event.remove();
  }

}
