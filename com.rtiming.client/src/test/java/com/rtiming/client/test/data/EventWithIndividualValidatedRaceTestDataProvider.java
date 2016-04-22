package com.rtiming.client.test.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.junit.Assert;

import com.rtiming.client.ecard.download.PunchForm;
import com.rtiming.client.event.EventForm;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.event.course.ControlTypeCodeType;
import com.rtiming.shared.race.IRaceService;
import com.rtiming.shared.services.code.CourseGenerationCodeType;

public class EventWithIndividualValidatedRaceTestDataProvider extends EventWithIndividualClassTestDataProvider {

  private ECardStationTestDataProvider eCardStation;
  private EntryTestDataProvider entry;
  private DownloadedECardTestDataProvider download;
  private final List<ControlTestData> controlNos;
  private final List<ControlTestData> punchNos;
  private final Integer[] legTime;
  private final Integer start;
  private final Integer finish;

  /**
   * @param controlNos
   *          the expected controls
   * @param punchNos
   *          the really punched controls
   * @throws ProcessingException
   */
  public EventWithIndividualValidatedRaceTestDataProvider(String[] controlNos, String[] punchNos) throws ProcessingException {
    super(false, CourseGenerationCodeType.CourseGenerationIndividualControlsCode.ID);
    this.controlNos = convertToControlTestData(controlNos, true, true);
    this.punchNos = convertToPunchTestData(punchNos);
    List<Integer> legTimes = new ArrayList<Integer>();
    for (int k = 0; k < punchNos.length; k++) {
      legTimes.add(k);
    }
    this.legTime = legTimes.toArray(new Integer[legTimes.size()]);
    this.start = 1;
    this.finish = 1 + legTimes.size();
    callInitializer();
  }

  /**
   * @param controlNos
   *          the expected controls
   * @param punchNos
   *          the really punched controls
   * @param legTime
   *          the leg time to the control in seconds
   * @throws ProcessingException
   */
  public EventWithIndividualValidatedRaceTestDataProvider(String[] controlNos, String[] punchNos, Integer start, Integer finish, Integer[] legTime) throws ProcessingException {
    super(false, CourseGenerationCodeType.CourseGenerationIndividualControlsCode.ID);
    this.controlNos = convertToControlTestData(controlNos, start != null, finish != null);
    this.punchNos = convertToPunchTestData(punchNos);
    this.legTime = legTime;
    this.start = start;
    this.finish = finish;
    callInitializer();
  }

  public EventWithIndividualValidatedRaceTestDataProvider(List<ControlTestData> controlNos, List<ControlTestData> punchNos, Integer start, Integer finish, Integer[] legTime, Long courseGenerationTypeUid) throws ProcessingException {
    super(false, courseGenerationTypeUid);
    this.controlNos = controlNos;
    this.punchNos = punchNos;
    this.legTime = legTime;
    this.start = start;
    this.finish = finish;
    callInitializer();
  }

  private class Key {

    private final String controlNo;
    private final Long sortCode;

    public Key(String controlNo, Long sortCode) {
      super();
      this.controlNo = controlNo;
      this.sortCode = sortCode;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + getOuterType().hashCode();
      result = prime * result + ((controlNo == null) ? 0 : controlNo.hashCode());
      result = prime * result + ((sortCode == null) ? 0 : sortCode.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (!(obj instanceof Key)) {
        return false;
      }
      Key other = (Key) obj;
      if (!getOuterType().equals(other.getOuterType())) {
        return false;
      }
      if (controlNo == null) {
        if (other.controlNo != null) {
          return false;
        }
      }
      else if (!controlNo.equals(other.controlNo)) {
        return false;
      }
      if (sortCode == null) {
        if (other.sortCode != null) {
          return false;
        }
      }
      else if (!sortCode.equals(other.sortCode)) {
        return false;
      }
      return true;
    }

    private EventWithIndividualValidatedRaceTestDataProvider getOuterType() {
      return EventWithIndividualValidatedRaceTestDataProvider.this;
    }

  }

  @Override
  protected EventForm createForm() throws ProcessingException {
    EventForm form = super.createForm();

    Map<Key, Long> masterCourseControlNrLookup = new HashMap<>();
    for (ControlTestData controlTestData : controlNos) {
      Long loopMasterCourseControlNr = masterCourseControlNrLookup.get(new Key(controlTestData.getLoopMasterCourseControlNo(), controlTestData.getLoopMasterSortCode()));
      if (loopMasterCourseControlNr == null && !StringUtility.isNullOrEmpty(controlTestData.getLoopMasterCourseControlNo())) {
        Assert.fail("Illegal Test Data, no master course control nr");
      }
      if (!StringUtility.isNullOrEmpty(controlTestData.getLoopVariantCode()) && loopMasterCourseControlNr == null) {
        Assert.fail("Illegal Test Data, no master");
      }
      CourseControlTestDataProvider control = new CourseControlTestDataProvider(form.getEventNr(),
          getCourseNr(),
          controlTestData.getControlTypeUid(),
          controlTestData.getSortCode(),
          controlTestData.getControlNo(),
          controlTestData.getLoopTypeUid(),
          loopMasterCourseControlNr,
          controlTestData.getLoopVariantCode());
      Assert.assertNotNull(control.getControlNo());

      Key key = new Key(control.getForm().getControlField().getDisplayText(), control.getForm().getSortCodeField().getValue());
      masterCourseControlNrLookup.put(key, control.getForm().getCourseControlNr());
    }

    // Entry
    entry = new EntryTestDataProvider(form.getEventNr(), getClassUid());

    // E-Card Station
    eCardStation = new ECardStationTestDataProvider();

    // Punch Session including Times
    download = new DownloadedECardTestDataProvider(form.getEventNr(), entry.getRaceNr(), entry.getECardNr(), eCardStation.getECardStationNr(), start, finish);
    Assert.assertNotNull(download.getForm().getPunchSessionNr());
    Date startDate = DateUtility.nvl(download.getForm().getStartField().getValue(), form.getZeroTimeField().getValue());

    // Punch Form
    int sortCode = 0;
    for (ControlTestData punchNo : punchNos) {
      PunchForm punch = new PunchForm();
      punch.startNew();
      punch.getPunchSessionField().setValue(download.getForm().getPunchSessionNr());
      punch.getControlNoField().setValue(punchNo.getControlNo());
      punch.getEventField().setValue(form.getEventNr());
      punch.getTimeField().setValue(FMilaUtility.addSeconds(startDate, legTime[sortCode]));
      punch.getSortCodeField().setValue(1L + sortCode);
      punch.doOk();
      sortCode++;
    }

    // Validate (OK)
    BEANS.get(IRaceService.class).validateAndPersistRace(entry.getRaceNr());

    return form;
  }

  @Override
  public void remove() throws ProcessingException {
    super.remove();
    eCardStation.remove();
  }

  public Long getRaceNr() throws ProcessingException {
    return entry.getRaceNr();
  }

  public Long getPunchSessionNr() throws ProcessingException {
    return download.getPunchSessionNr();
  }

  public Long getRunnerNr() throws ProcessingException {
    return entry.getRunnerNr();
  }

  public Long getClubNr() throws ProcessingException {
    return entry.getClubNr();
  }

  public Long getEntryNr() throws ProcessingException {
    return entry.getEntryNr();
  }

  private List<ControlTestData> convertToControlTestData(String[] controlNos, boolean addStart, boolean addFinish) {
    List<ControlTestData> result = new ArrayList<>();
    if (addStart) {
      result.add(new ControlTestData("S", 1L, ControlTypeCodeType.StartCode.ID, null, null, null, null));
    }
    long count = 1;
    for (String controlNo : controlNos) {
      result.add(new ControlTestData(controlNo, count + 1, ControlTypeCodeType.ControlCode.ID, null, null, null, null));
      count++;
    }
    if (addFinish) {
      result.add(new ControlTestData("Z", count + 1, ControlTypeCodeType.FinishCode.ID, null, null, null, null));
    }
    return result;
  }

  private List<ControlTestData> convertToPunchTestData(String[] controlNos) {
    List<ControlTestData> result = new ArrayList<>();
    long count = 0;
    for (String controlNo : controlNos) {
      result.add(new ControlTestData(controlNo, count, ControlTypeCodeType.ControlCode.ID, null, null, null, null));
      count++;
    }
    return result;
  }

}
