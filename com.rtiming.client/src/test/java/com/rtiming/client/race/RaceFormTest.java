package com.rtiming.client.race;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.race.RaceForm.MainBox.TabBox.RaceStatusBox.ManualStatusField;
import com.rtiming.client.race.RaceForm.MainBox.TabBox.RaceStatusBox.RaceStatusField;
import com.rtiming.client.test.AbstractFormTest;
import com.rtiming.client.test.data.EntryTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualClassTestDataProvider;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.race.RaceStatusCodeType;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class RaceFormTest extends AbstractFormTest<RaceForm> {

  private static EventWithIndividualClassTestDataProvider event;
  private static EntryTestDataProvider entry;

  @BeforeClass
  public static void beforeClass() throws ProcessingException {
    event = new EventWithIndividualClassTestDataProvider();
    entry = new EntryTestDataProvider(event.getEventNr(), event.getClassUid());
  }

  @AfterClass
  public static void afterClass() throws ProcessingException {
    event.remove();
  }

  @Override
  protected RaceForm getStartedForm() throws ProcessingException {
    RaceForm race = new RaceForm();
    race.getEventNrField().setValue(event.getEventNr());
    race.getRunnerNrField().setValue(entry.getRunnerNr());
    race.getLegClassUidField().setValue(event.getClassUid());
    race.startNew();
    race.touch();
    return race;
  }

  @Override
  protected RaceForm getModifyForm() throws ProcessingException {
    RaceForm race = new RaceForm();
    race.setRaceNr(getForm().getRaceNr());
    race.startModify();
    return race;
  }

  @Override
  protected List<FieldValue> getFixedValues() throws ProcessingException {
    List<FieldValue> list = new ArrayList<FieldValue>();
    list.add(new FieldValue(ManualStatusField.class, Boolean.TRUE));
    list.add(new FieldValue(RaceStatusField.class, RaceStatusCodeType.DidNotFinishCode.ID));
    return list;
  }

  @Override
  @After
  public void cleanup() throws ProcessingException {
  }

}
