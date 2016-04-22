package com.rtiming.client.test.data;

import java.util.Date;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.junit.Assert;

import com.rtiming.client.ecard.download.PunchForm;

public class PunchTestDataProvider extends AbstractTestDataProvider<PunchForm> {

  private PunchForm punch;

  private final Long punchSessionNr;
  private final Long eventNr;
  private final Date time;
  private final String controlNo;
  private final Long sortCode;

  /**
   * @param punchSessionNr
   * @param eventNr
   * @param time
   * @param controlNo
   * @param sortCode
   * @throws ProcessingException
   */
  public PunchTestDataProvider(Long punchSessionNr, Long eventNr, Date time, String controlNo, Long sortCode) throws ProcessingException {
    super();
    this.punchSessionNr = punchSessionNr;
    this.eventNr = eventNr;
    this.time = time;
    this.controlNo = controlNo;
    this.sortCode = sortCode;
    callInitializer();
  }

  @Override
  protected PunchForm createForm() throws ProcessingException {

    Assert.assertNotNull(eventNr);
    Assert.assertNotNull(punchSessionNr);
    Assert.assertNotNull(time);
    Assert.assertNotNull(controlNo);
    Assert.assertNotNull(sortCode);

    // Control
    punch = new PunchForm();
    punch.getPunchSessionField().setValue(punchSessionNr);
    punch.startNew();
    punch.getEventField().setValue(eventNr);
    punch.getTimeField().setValue(time);
    punch.getControlNoField().setValue(controlNo);
    punch.getSortCodeField().setValue(sortCode);
    punch.doOk();

    return punch;
  }

  @Override
  public void remove() throws ProcessingException {

  }

  public String getControlNo() throws ProcessingException {
    return getForm().getControlNoField().getValue();
  }

}
