package com.rtiming.client.race;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.TestClientSession;
import com.rtiming.client.ecard.download.RaceControlsTableForm;
import com.rtiming.client.test.FMilaClientTestUtility;
import com.rtiming.client.test.data.EventWithIndividualValidatedRaceTestDataProvider;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class RaceControlsTableFormTest {

  private static EventWithIndividualValidatedRaceTestDataProvider event;

  @BeforeClass
  public static void beforeClass() throws ProcessingException {
    event = new EventWithIndividualValidatedRaceTestDataProvider(new String[]{"31"}, new String[]{"31"});
  }

  @Test
  public void testLoad() throws Exception {
    RaceControlsTableForm form = new RaceControlsTableForm();
    form.startNew();
    Assert.assertEquals("0 Rows", 0, form.getRaceControlField().getTable().getRowCount());

    form.setRaceNrs(new Long[]{event.getRaceNr()});
    form.getRaceControlField().getTable().reloadTable();

    Assert.assertEquals("3 Rows", 3, form.getRaceControlField().getTable().getRowCount());
    Assert.assertEquals("Start", "S", form.getRaceControlField().getTable().getControlColumn().getValue(0));
    Assert.assertEquals("31", "31", form.getRaceControlField().getTable().getControlColumn().getValue(1));
    Assert.assertEquals("Finish", "Z", form.getRaceControlField().getTable().getControlColumn().getValue(2));

    form.doClose();
  }

  @Test
  public void testReload() throws Exception {
    RaceControlsTableForm form = new RaceControlsTableForm();
    form.startNew();
    Assert.assertEquals("0 Rows", 0, form.getRaceControlField().getTable().getRowCount());

    form.setRaceNrs(new Long[]{event.getRaceNr()});
    form.getRaceControlField().getTable().reloadTable();
    form.getRaceControlField().getTable().reloadTable();

    Assert.assertEquals("3 Rows", 3, form.getRaceControlField().getTable().getRowCount());
    Assert.assertEquals("Start", "S", form.getRaceControlField().getTable().getControlColumn().getValue(0));
    Assert.assertEquals("31", "31", form.getRaceControlField().getTable().getControlColumn().getValue(1));
    Assert.assertEquals("Finish", "Z", form.getRaceControlField().getTable().getControlColumn().getValue(2));

    form.doClose();
  }

  @Test
  public void testClear() throws Exception {
    RaceControlsTableForm form = new RaceControlsTableForm();
    form.startNew();
    Assert.assertEquals("0 Rows", 0, form.getRaceControlField().getTable().getRowCount());

    form.setRaceNrs(new Long[]{event.getRaceNr()});
    form.getRaceControlField().getTable().reloadTable();
    form.getRaceControlField().getTable().clearTable();

    Assert.assertEquals("0 Rows", 0, form.getRaceControlField().getTable().getRowCount());
    form.doClose();
  }

  @Test
  public void testForm() throws Exception {
    RaceControlsTableForm form = new RaceControlsTableForm();
    form.startNew();
    FMilaClientTestUtility.testFormFields(form);
    form.doClose();
  }

  @AfterClass
  public static void afterClass() throws ProcessingException {
    if (event != null) {
      event.remove();
    }
  }

}
