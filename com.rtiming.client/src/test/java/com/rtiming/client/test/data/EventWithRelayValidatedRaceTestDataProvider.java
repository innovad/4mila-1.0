package com.rtiming.client.test.data;

import java.util.Date;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.junit.Assert;

import com.rtiming.client.ecard.download.PunchForm;
import com.rtiming.client.event.EventForm;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.event.course.ControlTypeCodeType;
import com.rtiming.shared.race.IRaceService;

public class EventWithRelayValidatedRaceTestDataProvider extends EventWithRelayClassTestDataProvider {

  private ECardStationTestDataProvider eCardStation;
  private EntryTestDataProvider entry;

  private int numberOfLegs;
  private DownloadedECardTestDataProvider[] download;
  private final String[][] controlNos;
  private final String[][] punchNos;
  private final Integer[][] legTime;
  private final Integer[] start;
  private final Integer[] finish;

  public EventWithRelayValidatedRaceTestDataProvider(String[][] controlNos, String[][] punchNos, Integer[] start, Integer[] finish, Integer[][] legTime) throws ProcessingException {
    super(start.length);
    numberOfLegs = start.length;
    Assert.assertEquals("Correct number of legs", numberOfLegs, controlNos.length);
    Assert.assertEquals("Correct number of legs", numberOfLegs, punchNos.length);
    Assert.assertEquals("Correct number of legs", numberOfLegs, finish.length);
    Assert.assertEquals("Correct number of legs", numberOfLegs, legTime.length);
    this.controlNos = controlNos;
    this.punchNos = punchNos;
    this.start = start;
    this.finish = finish;
    this.legTime = legTime;
    this.download = new DownloadedECardTestDataProvider[start.length];
    callInitializer();
  }

  @Override
  protected EventForm createForm() throws ProcessingException {
    EventForm form = super.createForm();

    // E-Card Station
    eCardStation = new ECardStationTestDataProvider();

    // Start
    if (start != null) {
      for (int i = 0; i < numberOfLegs; i++) {
        CourseControlTestDataProvider startControl = new CourseControlTestDataProvider(form.getEventNr(), getCourseNr(i), ControlTypeCodeType.StartCode.ID, 1L, "S");
        Assert.assertNotNull(startControl.getControlNo());
      }
    }

    // Controls
    for (int i = 0; i < numberOfLegs; i++) {
      int count = 1;
      for (String controlNo : controlNos[i]) {
        CourseControlTestDataProvider control = new CourseControlTestDataProvider(form.getEventNr(), getCourseNr(i), ControlTypeCodeType.ControlCode.ID, 1L + count, controlNo);
        Assert.assertNotNull(control.getControlNo());
        count++;
      }
    }

    // Finish
    if (finish != null) {
      for (int i = 0; i < numberOfLegs; i++) {
        CourseControlTestDataProvider finishControl = new CourseControlTestDataProvider(form.getEventNr(), getCourseNr(i), ControlTypeCodeType.FinishCode.ID, 2L + controlNos[i].length, "Z");
        Assert.assertNotNull(finishControl.getControlNo());
      }
    }

    // Entry
    entry = new EntryTestDataProvider(form.getEventNr(), getLegUids());

    // Punch Session including Times
    for (int i = 0; i < numberOfLegs; i++) {

      download[i] = new DownloadedECardTestDataProvider(form.getEventNr(), entry.getRaceNrs()[i], entry.getECardNrs()[i], eCardStation.getECardStationNr(), start[i], finish[i]);
      Assert.assertNotNull(download[i].getForm().getPunchSessionNr());
      Date startDate = DateUtility.nvl(download[i].getForm().getStartField().getValue(), form.getZeroTimeField().getValue());

      // Punch Form
      int sortCode = 0;
      for (String punchNo : punchNos[i]) {
        PunchForm punch = new PunchForm();
        punch.startNew();
        punch.getPunchSessionField().setValue(download[i].getForm().getPunchSessionNr());
        punch.getControlNoField().setValue(punchNo);
        punch.getEventField().setValue(form.getEventNr());
        punch.getTimeField().setValue(FMilaUtility.addSeconds(startDate, legTime[i][sortCode]));
        punch.getSortCodeField().setValue(1L + sortCode);
        punch.doOk();
        sortCode++;
      }

      // Validate (OK)
      BEANS.get(IRaceService.class).validateAndPersistRace(entry.getRaceNrs()[i]);

    }

    return form;
  }

  public EntryTestDataProvider getEntry() {
    return entry;
  }

}
