package com.rtiming.client.test.data;

import java.util.Date;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.client.ecard.download.DownloadedECardForm;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.ecard.download.DownloadedECardFormData;
import com.rtiming.shared.ecard.download.IDownloadedECardProcessService;
import com.rtiming.shared.event.IEventProcessService;

public class DownloadedECardTestDataProvider extends AbstractTestDataProvider<DownloadedECardForm> {

  private Long eventNr;
  private Long eCardNr;
  private Long eCardStationNr;
  private Long raceNr;
  private Integer start;
  private Integer finish;

  /**
   * @param eventNr
   * @param raceNr
   * @param eCardNr
   * @param eCardStationNr
   * @throws ProcessingException
   */
  public DownloadedECardTestDataProvider(Long eventNr, Long raceNr, Long eCardNr, Long eCardStationNr) throws ProcessingException {
    this.eventNr = eventNr;
    this.raceNr = raceNr;
    this.eCardNr = eCardNr;
    this.eCardStationNr = eCardStationNr;
    this.start = 1;
    this.finish = 400;
    callInitializer();
  }

  public DownloadedECardTestDataProvider(Long eventNr, Long raceNr, Long eCardNr, Long eCardStationNr, Integer start, Integer finish) throws ProcessingException {
    this.eventNr = eventNr;
    this.raceNr = raceNr;
    this.eCardNr = eCardNr;
    this.eCardStationNr = eCardStationNr;
    this.start = start;
    this.finish = finish;
    callInitializer();
  }

  @Override
  protected DownloadedECardForm createForm() throws ProcessingException {
    DownloadedECardForm punchSession = new DownloadedECardForm();
    punchSession.startNew();
    punchSession.getEventField().setValue(eventNr);
    punchSession.getECardField().setValue(eCardNr);
    punchSession.getECardStationField().setValue(eCardStationNr);
    punchSession.getRaceField().setValue(raceNr);
    Date evtZero = BEANS.get(IEventProcessService.class).getZeroTime(eventNr);
    if (start != null) {
      punchSession.getClearField().setValue(FMilaUtility.addSeconds(evtZero, start - 2));
      punchSession.getCheckField().setValue(FMilaUtility.addSeconds(evtZero, start - 1));
      punchSession.getStartField().setValue(FMilaUtility.addSeconds(evtZero, start));
    }
    if (finish != null) {
      punchSession.getFinishField().setValue(FMilaUtility.addSeconds(evtZero, finish));
    }
    punchSession.doOk();
    return punchSession;
  }

  @Override
  public void remove() throws ProcessingException {
    DownloadedECardFormData formData = new DownloadedECardFormData();
    formData.setPunchSessionNr(getForm().getPunchSessionNr());
    BEANS.get(IDownloadedECardProcessService.class).delete(formData);
  }

  public Long getPunchSessionNr() throws ProcessingException {
    return getForm().getPunchSessionNr();
  }

}
