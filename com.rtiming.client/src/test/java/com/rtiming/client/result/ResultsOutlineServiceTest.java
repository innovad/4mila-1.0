package com.rtiming.client.result;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.TestClientSession;
import com.rtiming.client.ecard.download.DownloadedECardForm;
import com.rtiming.client.race.RaceForm;
import com.rtiming.client.test.data.ECardTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualValidatedRaceTestDataProvider;
import com.rtiming.shared.common.report.template.IReportParameters;
import com.rtiming.shared.results.IResultsOutlineService;
import com.rtiming.shared.results.SplitTimeReportData;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class ResultsOutlineServiceTest {

  private EventWithIndividualValidatedRaceTestDataProvider event;
  private ECardTestDataProvider ecard;

  @Test
  public void testGetSplitTimesReportData() throws Exception {
    event = new EventWithIndividualValidatedRaceTestDataProvider(new String[]{"31"}, new String[]{"31"});
    ecard = new ECardTestDataProvider("181818");

    RaceForm race = new RaceForm();
    race.setRaceNr(event.getRaceNr());
    race.startModify();
    race.getECardNrField().setValue(ecard.getECardNr());
    race.doOk();

    SplitTimeReportData data = BEANS.get(IResultsOutlineService.class).getSplitTimesReportData(event.getRaceNr());

    String eCardNo = data.getParameters().get(IReportParameters.RUNNER_ECARD_NO);

    DownloadedECardForm download = new DownloadedECardForm();
    download.setPunchSessionNr(event.getPunchSessionNr());
    download.startModify();
    download.getECardField().setValue(ecard.getECardNr());
    download.doClose();

    Assert.assertEquals("E-Card from Race", eCardNo, ecard.getForm().getNumberField().getValue());
  }

  public void after() throws ProcessingException {
    if (event != null) {
      event.remove();
    }
    if (ecard != null) {
      ecard.remove();
    }
  }

}
