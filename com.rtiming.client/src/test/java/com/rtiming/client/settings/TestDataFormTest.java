package com.rtiming.client.settings;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.TestClientSession;
import com.rtiming.client.runner.RunnerForm.MainBox.ClubField;
import com.rtiming.client.runner.RunnerForm.MainBox.ECardField;
import com.rtiming.client.runner.RunnerForm.MainBox.SexField;
import com.rtiming.client.test.FMilaClientTestUtility;
import com.rtiming.client.test.data.ClubTestDataProvider;
import com.rtiming.client.test.data.ECardStationTestDataProvider;
import com.rtiming.client.test.data.ECardTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualClassTestDataProvider;
import com.rtiming.client.test.data.RunnerTestDataProvider;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.runner.SexCodeType;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class TestDataFormTest {

  private EventWithIndividualClassTestDataProvider event;
  private RunnerTestDataProvider runner;
  private ClubTestDataProvider club;
  private ECardTestDataProvider ecard;
  private ECardStationTestDataProvider station;

  @Before
  public void before() throws ProcessingException {
    event = new EventWithIndividualClassTestDataProvider();
    club = new ClubTestDataProvider();
    ecard = new ECardTestDataProvider();
    station = new ECardStationTestDataProvider();
    List<FieldValue> value = new ArrayList<FieldValue>();
    value.add(new FieldValue(SexField.class, SexCodeType.ManCode.ID));
    value.add(new FieldValue(ClubField.class, club.getClubNr()));
    value.add(new FieldValue(ECardField.class, ecard.getECardNr()));
    runner = new RunnerTestDataProvider(value);
  }

  @Test
  public void testTestDataCreation() throws Exception {
    TestDataForm form = new TestDataForm();
    form.startNew();
    form.getEventField().setValue(event.getEventNr());
    form.getClazzField().setValue(event.getClassUid());
    form.getCountField().setValue(1L);
    form.doOk();
  }

  @Test
  public void testGetters() throws Exception {
    FMilaClientTestUtility.testFormFields(new TestDataForm());
  }

  @After
  public void after() throws ProcessingException {
    event.remove();
    runner.remove();
    club.remove();
    ecard.remove();
    station.remove();
  }

}
