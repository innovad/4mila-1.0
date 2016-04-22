package com.rtiming.client.ecard.download;

import java.util.List;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.testing.client.ScoutClientAssert;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.test.data.EventWithIndividualValidatedRaceTestDataProvider;

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(TestEnvironmentClientSession.class)
public class DownloadedECardFormRaceFieldTest {

  private EventWithIndividualValidatedRaceTestDataProvider event;
  private EventWithIndividualValidatedRaceTestDataProvider event2;

  @Test
  public void testRaceField() throws Exception {
    event = new EventWithIndividualValidatedRaceTestDataProvider(new String[]{"31"}, new String[]{"31"});

    DownloadedECardForm download = new DownloadedECardForm();
    download.setPunchSessionNr(event.getPunchSessionNr());
    download.startModify();

    ScoutClientAssert.assertEnabled(download.getEventField());
    ScoutClientAssert.assertEnabled(download.getRaceField());

    List<? extends ILookupRow<?>> rows = download.getRaceField().callBrowseLookup("", Integer.MAX_VALUE);
    Assert.assertEquals("Race Lookup Rows", 1, rows.size());
    Assert.assertEquals("Race Lookup Value", event.getRaceNr(), rows.get(0).getKey());

    download.doClose();
  }

  @Test
  public void testRaceFieldMultipleEvents() throws Exception {
    event = new EventWithIndividualValidatedRaceTestDataProvider(new String[]{"31"}, new String[]{"31"});
    event2 = new EventWithIndividualValidatedRaceTestDataProvider(new String[]{"31"}, new String[]{"31"});

    DownloadedECardForm download = new DownloadedECardForm();
    download.setPunchSessionNr(event.getPunchSessionNr());
    download.startModify();

    List<? extends ILookupRow<?>> rows = download.getRaceField().callBrowseLookup("", Integer.MAX_VALUE);
    Assert.assertEquals("Race Lookup Rows", 1, rows.size());
    Assert.assertEquals("Race Lookup Value", event.getRaceNr(), rows.get(0).getKey());

    // check race field is empty when no event is set
    download.getEventField().setValue(null);
    ScoutClientAssert.assertDisabled(download.getRaceField());
    ScoutClientAssert.assertEnabled(download.getEventField());
    Assert.assertNull("Race Empty", download.getRaceField().getValue());

    download.getEventField().setValue(event2.getEventNr());
    rows = download.getRaceField().callBrowseLookup("", Integer.MAX_VALUE);
    Assert.assertEquals("Race Lookup Rows", 1, rows.size());
    Assert.assertEquals("Race Lookup Value", event2.getRaceNr(), rows.get(0).getKey());

    download.doClose();
  }

  @After
  public void after() throws ProcessingException {
    if (event != null) {
      event.remove();
    }
    if (event2 != null) {
      event2.remove();
    }
  }

}
