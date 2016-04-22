package com.rtiming.client.common.infodisplay;

import static org.junit.Assert.assertTrue;

import org.eclipse.scout.rt.client.IClientSession;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.rtiming.client.ClientSession;
import com.rtiming.client.test.data.EventWithIndividualValidatedRaceTestDataProvider;
import com.rtiming.shared.Texts;

@RunWith(ClientTestRunner.class)
public class InfoDisplayDownloadJobTest {

  private EventWithIndividualValidatedRaceTestDataProvider event;

  @Test
  public void testPrepareURL1() throws Exception {
    InfoDisplayDownloadJob job = new InfoDisplayDownloadJob(null, Mockito.mock(IClientSession.class));
    String url = job.prepareURL();
    assertTrue(url.endsWith(InfoDisplayUtility.addParameter("", "name", Texts.get("NoEntryFound"))));
  }

  @Test
  public void testPrepareURL2() throws Exception {
    InfoDisplayDownloadJob job = new InfoDisplayDownloadJob(0L, Mockito.mock(IClientSession.class));
    String url = job.prepareURL();
    assertTrue(url.endsWith(InfoDisplayUtility.addParameter("", "name", Texts.get("NoEntryFound"))));
  }

  @Test
  public void testPrepareURL3() throws Exception {
    event = new EventWithIndividualValidatedRaceTestDataProvider(new String[]{"31"}, new String[]{"31"}, 0, 2000, new Integer[]{500});
    InfoDisplayDownloadJob job = new InfoDisplayDownloadJob(event.getRaceNr(), ClientSession.get());
    String url = job.prepareURL();
    assertTrue(url.contains("rank=" + Texts.get("Rank") + "%201%2F1"));
  }

  @After
  public void after() throws ProcessingException {
    if (event != null) {
      event.remove();
    }
  }

}
