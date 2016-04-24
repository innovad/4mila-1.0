package com.rtiming.client.ecard.download.processor;

import java.io.IOException;
import java.util.Date;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.TestClientSession;
import com.rtiming.client.ecard.download.FMilaSerialTestPort;
import com.rtiming.client.ecard.download.util.ByteUtility;
import com.rtiming.client.test.data.ECardStationTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualClassTestDataProvider;
import com.rtiming.serial.FMilaSerialPort;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.common.database.sql.EventBean;
import com.rtiming.shared.event.IEventProcessService;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class SICardV8FamilyProcessorTest {

  private EventWithIndividualClassTestDataProvider event;
  private ECardStationTestDataProvider station;
  private Date evtZero;
  private Date evtFinish;

  @Before
  public void before() throws ProcessingException {
    evtZero = DateUtility.parse("10-11-2012 08:00:00.000", "dd-MM-yyyy HH:mm:ss.SSS");
    evtFinish = DateUtility.parse("10-11-2012 12:00:00.000", "dd-MM-yyyy HH:mm:ss.SSS");
    event = new EventWithIndividualClassTestDataProvider(evtZero, evtFinish);
    station = new ECardStationTestDataProvider();
  }

  @Test
  public void testV8() throws ProcessingException, IOException {
    SICardV8FamilyProcessor processor = createSICardProcessor();

    byte[] insertedData = new byte[]{2, -24, 6, 0, 4, 1, 17, -109, -25, 120, -37, 3};
    processor.handleCardInserted(insertedData);

    byte[] processData0 = new byte[]{2, -17, -125, 0, 4, 0, 26, -67, 18, 127, -22, -22, -22, -22, 29, 3, 58, -112, -99, 93, 60, 8, 29, 2, 66, -65, 0, 50, 26, 15, 1, 17, -109, -25, 12, -1, 85, -33, 65, 100, 114, 105, 97, 110, 59, 77, 111, 115, 101, 114, 59, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 29, 36, 60, 122, 29, 31, 60, -86, 29, 32, 60, -45, 29, 38, 61, 27, 29, 35, 61, 103, 29, 34, 61, 127, 29, 33, 61, -89, 29, 37, 61, -51, 29, 58, 62, 20, 29, 54, 62, -124, 29, 56, 62, -61, 29, 49, 62, -16, 29, 40, 63, 80, 29, 41, 63, -115, 29, 39, 63, -16, 29, 51, 64, 15, 29, 59, 64, 101, 29, 47, 64, -121, -90, -34, 3};
    byte[] carddataByte0 = new byte[128];
    System.arraycopy(processData0, 6, carddataByte0, 0, 128);
    processor.handleProcessData(carddataByte0);

    byte[] processData1 = new byte[]{2, -17, -125, 0, 4, 1, 29, 48, 64, -77, 29, 46, 65, 90, 29, 44, 65, -100, 29, 43, 65, -87, 29, 53, 65, -52, 29, 45, 65, -16, 29, 42, 66, 86, 29, 50, 66, -78, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -70, 17, 3};
    byte[] carddataByte1 = new byte[128];
    System.arraycopy(processData1, 6, carddataByte1, 0, 128);
    processor.handleProcessData(carddataByte1);

    byte[] processRemoved = new byte[]{2, -25, 6, 0, 4, 0, 17, -109, -25, 28, -6, 3};
    processor.handleCardRemoved(processRemoved);

    Assert.assertEquals("1151975", processor.getECardNo());

    Assert.assertEquals(26, processor.getControlData().size());

    Assert.assertEquals("36", processor.getControlData().get(0).getControlNo().getValue());
    Assert.assertEquals("31", processor.getControlData().get(1).getControlNo().getValue());
    Assert.assertEquals("32", processor.getControlData().get(2).getControlNo().getValue());
    Assert.assertEquals("38", processor.getControlData().get(3).getControlNo().getValue());
    Assert.assertEquals("35", processor.getControlData().get(4).getControlNo().getValue());
    Assert.assertEquals("34", processor.getControlData().get(5).getControlNo().getValue());
    Assert.assertEquals("33", processor.getControlData().get(6).getControlNo().getValue());
    Assert.assertEquals("37", processor.getControlData().get(7).getControlNo().getValue());
    Assert.assertEquals("58", processor.getControlData().get(8).getControlNo().getValue());
    Assert.assertEquals("54", processor.getControlData().get(9).getControlNo().getValue());

    Assert.assertEquals("56", processor.getControlData().get(10).getControlNo().getValue());
    Assert.assertEquals("49", processor.getControlData().get(11).getControlNo().getValue());
    Assert.assertEquals("40", processor.getControlData().get(12).getControlNo().getValue());
    Assert.assertEquals("41", processor.getControlData().get(13).getControlNo().getValue());
    Assert.assertEquals("39", processor.getControlData().get(14).getControlNo().getValue());
    Assert.assertEquals("51", processor.getControlData().get(15).getControlNo().getValue());
    Assert.assertEquals("59", processor.getControlData().get(16).getControlNo().getValue());
    Assert.assertEquals("47", processor.getControlData().get(17).getControlNo().getValue());
    Assert.assertEquals("48", processor.getControlData().get(18).getControlNo().getValue());
    Assert.assertEquals("46", processor.getControlData().get(19).getControlNo().getValue());

    Assert.assertEquals("44", processor.getControlData().get(20).getControlNo().getValue());
    Assert.assertEquals("43", processor.getControlData().get(21).getControlNo().getValue());
    Assert.assertEquals("53", processor.getControlData().get(22).getControlNo().getValue());
    Assert.assertEquals("45", processor.getControlData().get(23).getControlNo().getValue());
    Assert.assertEquals("42", processor.getControlData().get(24).getControlNo().getValue());
    Assert.assertEquals("50", processor.getControlData().get(25).getControlNo().getValue());

    assertTime("10-11-2012 16:18:02.000", processor.getControlData().get(0).getRawTime());
    assertTime("10-11-2012 16:18:50.000", processor.getControlData().get(1).getRawTime());
    assertTime("10-11-2012 16:19:31.000", processor.getControlData().get(2).getRawTime());
    assertTime("10-11-2012 16:20:43.000", processor.getControlData().get(3).getRawTime());

    assertTime("Check", "10-11-2012 16:09:52.000", processor.getCheckTime().longValue());
    assertTime("Clear", "10-11-2012 16:09:52.000", processor.getClearTime().longValue());
    assertTime("Start", "10-11-2012 16:16:08.000", processor.getStartTime().longValue());
    assertTime("Finish", "10-11-2012 16:44:47.000", processor.getFinishTime().longValue());
  }

  @Test
  public void testV10() throws Exception {
    SICardV8FamilyProcessor processor = createSICardProcessor();

    byte[] insertedData = new byte[]{2, -24, 6, 0, 1, 15, -119, 84, 68, 59, 101, 3};
    processor.handleCardInserted(insertedData);

    byte[] processData0 = new byte[]{2, -17, -125, 0, 1, 0, 86, 86, -31, -104, -22, -22, -22, -22, 13, 35, 125, -127, 13, 36, 125, -114, 13, 44, 126, 39, 7, -105, 8, 44, 15, -119, 84, 68, 11, 12, 119, 37, 65, 100, 114, 105, 97, 110, 59, 77, 111, 115, 101, 114, 59, 109, 59, 49, 49, 46, 48, 53, 46, 49, 57, 55, 53, 59, 52, 109, 105, 108, 97, 59, 105, 110, 102, 111, 64, 52, 109, 105, 108, 97, 46, 99, 111, 109, 59, 59, 76, 117, 122, 101, 114, 110, 59, 83, 116, 101, 114, 110, 109, 97, 116, 116, 115, 116, 114, 97, 115, 115, 101, 32, 50, 49, 59, 54, 48, 48, 53, 59, 83, 85, 73, 59, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -28, -80, 3};
    byte[] carddataByte0 = new byte[128];
    System.arraycopy(processData0, 6, carddataByte0, 0, 128);
    processor.handleProcessData(carddataByte0);

    byte[] processData1 = new byte[]{2, -17, -125, 0, 1, 4, 13, 37, 125, -94, 13, 38, 125, -53, 13, 39, 125, -39, 13, 40, 125, -29, 13, 41, 125, -14, 13, 42, 125, -4, 13, 43, 126, 10, 13, 43, 126, 27, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, 23, 14, 3, 2, -17, -125, 0, 1, 5, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -5, -115, 3, 2, -17, -125, 0, 1, 6, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -63, -115, 3, 2, -17, -125, 0, 1, 7, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -41, -115, 3};
    for (int k = 0; k < processData1.length; k = k + 137) {
      processor.handleProcessData(ByteUtility.getSubarray(processData1, k + 6, 128));
    }

    byte[] processRemoved = new byte[]{2, -25, 6, 0, 1, 0, -119, 84, 68, 7, 71, 3};
    processor.handleCardRemoved(processRemoved);

    Assert.assertEquals("9000004", processor.getECardNo());

    Assert.assertEquals(8, processor.getControlData().size());

    Assert.assertEquals("37", processor.getControlData().get(0).getControlNo().getValue());
    Assert.assertEquals("38", processor.getControlData().get(1).getControlNo().getValue());
    Assert.assertEquals("39", processor.getControlData().get(2).getControlNo().getValue());
    Assert.assertEquals("40", processor.getControlData().get(3).getControlNo().getValue());
    Assert.assertEquals("41", processor.getControlData().get(4).getControlNo().getValue());
    Assert.assertEquals("42", processor.getControlData().get(5).getControlNo().getValue());
    Assert.assertEquals("43", processor.getControlData().get(6).getControlNo().getValue());
    Assert.assertEquals("43", processor.getControlData().get(7).getControlNo().getValue());

    assertTime("10-11-2012 20:56:02.000", processor.getControlData().get(0).getRawTime());
    assertTime("10-11-2012 20:56:43.000", processor.getControlData().get(1).getRawTime());
    assertTime("10-11-2012 20:56:57.000", processor.getControlData().get(2).getRawTime());
    assertTime("10-11-2012 20:57:07.000", processor.getControlData().get(3).getRawTime());
    assertTime("10-11-2012 20:57:22.000", processor.getControlData().get(4).getRawTime());
    assertTime("10-11-2012 20:57:32.000", processor.getControlData().get(5).getRawTime());
    assertTime("10-11-2012 20:57:46.000", processor.getControlData().get(6).getRawTime());
    assertTime("10-11-2012 20:58:03.000", processor.getControlData().get(7).getRawTime());

    assertTime("Check", "10-11-2012 20:55:29.000", processor.getCheckTime().longValue());
    assertTime("Clear", "10-11-2012 20:55:29.000", processor.getClearTime().longValue());
    assertTime("Start", "10-11-2012 20:55:42.000", processor.getStartTime().longValue());
    assertTime("Finish", "10-11-2012 20:58:15.000", processor.getFinishTime().longValue());
  }

  private void assertTime(String message, String expected, Long actual) {
    String actualStr = DateUtility.format(FMilaUtility.addMilliSeconds(evtZero, actual), "dd-MM-yyyy HH:mm:ss.SSS");
    Assert.assertEquals(message, expected, actualStr);
  }

  private void assertTime(String expected, Long actual) {
    assertTime("Time", expected, actual);
  }

  private SICardV8FamilyProcessor createSICardProcessor() throws ProcessingException {
    FMilaSerialPort testPort = new FMilaSerialTestPort();

    EventBean eventFormData = new EventBean();
    eventFormData.setEventNr(event.getEventNr());
    eventFormData = BEANS.get(IEventProcessService.class).load(eventFormData);

    SICardV8FamilyProcessor processor = new SICardV8FamilyProcessor(station.getFormData(), testPort, eventFormData.getEvtZero(), event.getEventNr(), ClientSession.get());
    Assert.assertEquals("Zero Time", evtZero.getTime(), processor.getCurrentEvtZero().getTime());
    return processor;
  }

  @After
  public void after() throws ProcessingException {
    station.remove();
    event.remove();
  }

}
