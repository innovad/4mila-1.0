package com.rtiming.client.ecard.download.processor;

import java.io.IOException;
import java.util.GregorianCalendar;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.ecard.download.FMilaSerialTestPort;
import com.rtiming.client.ecard.download.processor.SICardV6Processor.MODEL;
import com.rtiming.client.test.data.ECardStationTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualClassTestDataProvider;
import com.rtiming.serial.FMilaSerialPort;
import com.rtiming.shared.common.database.sql.EventBean;
import com.rtiming.shared.event.IEventProcessService;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestEnvironmentClientSession.class)
public class SICardV6ProcessorTest {

  private EventWithIndividualClassTestDataProvider event;
  private ECardStationTestDataProvider station;

  @Before
  public void before() throws ProcessingException {
    event = new EventWithIndividualClassTestDataProvider();
    station = new ECardStationTestDataProvider();
  }

  @Test
  public void test() throws ProcessingException, IOException {
    SICardV6Processor processor = createProcessor();

    byte[] insertedData = new byte[]{2, -26, 6, 0, 26, 0, 11, -56, 7, -48, 34, 3};
    processor.handleCardInserted(insertedData);

    byte[] configData = new byte[]{2, -125, 4, 0, 26, 51, -63, 32, 38, 3};
    processor.handleProcessData(configData);
  }

  @Test
  public void testGetNumberOfPunches0() throws Exception {
    SICardV6Processor processor = createProcessor();
    processor.setModel(MODEL.SIX);
    long num = processor.getNumberOfPunches(new byte[]{1, 1, 1, 1, -19, -19, -19, -19, 85, -86, 0, 11, -56, 7, -104, -30, 0, 32, /* num of controls */0, 2, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, 50, 1, 52, 16, -1, -1, -1, -1, 0, 0, 0, 1, 32, 32, 32, 32, 76, 97, 116, 115, 99, 104, 97, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 73, 114, 105, 115, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 83, 85, 73, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, -6, 48, 3});
    Assert.assertEquals("Number of controls", 0, num);
  }

  @Test
  public void testGetNumberOfPunches32() throws Exception {
    SICardV6Processor processor = createProcessor();
    processor.setModel(MODEL.SIX);
    long num = processor.getNumberOfPunches(new byte[]{1, 1, 1, 1, -19, -19, -19, -19, 85, -86, 0, 11, -56, 7, -104, -30, 0, 32, /* num of controls */32, 2, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, 50, 1, 52, 16, -1, -1, -1, -1, 0, 0, 0, 1, 32, 32, 32, 32, 76, 97, 116, 115, 99, 104, 97, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 73, 114, 105, 115, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 83, 85, 73, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, -6, 48, 3});
    Assert.assertEquals("Number of controls", 32, num);
  }

  @Test
  public void testGetNumberOfPunches64() throws Exception {
    SICardV6Processor processor = createProcessor();
    processor.setModel(MODEL.SIX);
    long num = processor.getNumberOfPunches(new byte[]{1, 1, 1, 1, -19, -19, -19, -19, 85, -86, 0, 11, -56, 7, -104, -30, 0, 32, /* num of controls */64, 2, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, 50, 1, 52, 16, -1, -1, -1, -1, 0, 0, 0, 1, 32, 32, 32, 32, 76, 97, 116, 115, 99, 104, 97, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 73, 114, 105, 115, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 83, 85, 73, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, -6, 48, 3});
    Assert.assertEquals("Number of controls", 64, num);
  }

  @Test(expected = ProcessingException.class)
  public void testGetNumberOfPunches65() throws Exception {
    SICardV6Processor processor = createProcessor();
    processor.setModel(MODEL.SIX);
    processor.getNumberOfPunches(new byte[]{1, 1, 1, 1, -19, -19, -19, -19, 85, -86, 0, 11, -56, 7, -104, -30, 0, 32, /* num of controls */65, 2, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, 50, 1, 52, 16, -1, -1, -1, -1, 0, 0, 0, 1, 32, 32, 32, 32, 76, 97, 116, 115, 99, 104, 97, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 73, 114, 105, 115, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 83, 85, 73, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, -6, 48, 3});
  }

  @Test(expected = ProcessingException.class)
  public void testGetNumberOfPunchesMinus1() throws Exception {
    SICardV6Processor processor = createProcessor();
    processor.setModel(MODEL.SIX);
    processor.getNumberOfPunches(new byte[]{1, 1, 1, 1, -19, -19, -19, -19, 85, -86, 0, 11, -56, 7, -104, -30, 0, 32, /* num of controls */-1, 2, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, 50, 1, 52, 16, -1, -1, -1, -1, 0, 0, 0, 1, 32, 32, 32, 32, 76, 97, 116, 115, 99, 104, 97, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 73, 114, 105, 115, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 83, 85, 73, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, -6, 48, 3});
  }

  @Test
  public void testGetNumberOfPunches192() throws Exception {
    SICardV6Processor processor = createProcessor();
    processor.setModel(MODEL.SIXSTAR);
    long num = processor.getNumberOfPunches(new byte[]{1, 1, 1, 1, -19, -19, -19, -19, 85, -86, 0, 11, -56, 7, -104, -30, 0, 32, /* num of controls */-64, 2, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, 50, 1, 52, 16, -1, -1, -1, -1, 0, 0, 0, 1, 32, 32, 32, 32, 76, 97, 116, 115, 99, 104, 97, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 73, 114, 105, 115, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 83, 85, 73, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, -6, 48, 3});
    Assert.assertEquals("Number of controls", 192, num);
  }

  @Test(expected = ProcessingException.class)
  public void testGetNumberOfPunches193() throws Exception {
    SICardV6Processor processor = createProcessor();
    processor.setModel(MODEL.SIXSTAR);
    processor.getNumberOfPunches(new byte[]{1, 1, 1, 1, -19, -19, -19, -19, 85, -86, 0, 11, -56, 7, -104, -30, 0, 32, /* num of controls */-63, 2, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, 50, 1, 52, 16, -1, -1, -1, -1, 0, 0, 0, 1, 32, 32, 32, 32, 76, 97, 116, 115, 99, 104, 97, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 73, 114, 105, 115, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 83, 85, 73, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, -6, 48, 3});
  }

  @Test(expected = ProcessingException.class)
  public void testGetNumberOfPunchesModelRequired() throws Exception {
    SICardV6Processor processor = createProcessor();
    processor.getNumberOfPunches(new byte[]{1, 1, 1, 1, -19, -19, -19, -19, 85, -86, 0, 11, -56, 7, -104, -30, 0, 32, /* num of controls */-63, 2, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, 50, 1, 52, 16, -1, -1, -1, -1, 0, 0, 0, 1, 32, 32, 32, 32, 76, 97, 116, 115, 99, 104, 97, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 73, 114, 105, 115, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 83, 85, 73, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, -6, 48, 3});
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetNumberOfPunchesDataRequired() throws Exception {
    SICardV6Processor processor = createProcessor();
    processor.getNumberOfPunches(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetNumberOfPunchesDataTooShort() throws Exception {
    SICardV6Processor processor = createProcessor();
    processor.getNumberOfPunches(new byte[]{1});
  }

  @Test
  public void testCard() throws Exception {

    SICardV6Processor processor = createProcessor();

    byte[] insertedData = new byte[]{2, -26, 6, 0, 26, 0, 11, -56, 7, -48, 34, 3};
    processor.handleCardInserted(insertedData);

    // byte[] configData = new byte[]{2, -125, 4, 0, 26, 51, -63, 32, 38, 3};
    processor.setModel(MODEL.SIX);
    processor.setNumberOfDataMessages(3 + 1);
    processor.handleProcessData(null);

    byte[] processData0 = new byte[]{2, -31, -125, 0, 26, 0, 1, 1, 1, 1, -19, -19, -19, -19, 85, -86, 0, 11, -56, 7, -104, -30, 0, 32, 1, 2, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, 50, 1, 52, 16, -1, -1, -1, -1, 0, 0, 0, 1, 32, 32, 32, 32, 76, 97, 116, 115, 99, 104, 97, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 73, 114, 105, 115, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 83, 85, 73, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, -6, 48, 3};
    byte[] carddataByte0 = new byte[128];
    System.arraycopy(processData0, 6, carddataByte0, 0, 128);
    processor.setNumberOfDataMessages(1 + 1 + 1);
    processor.handleProcessData(carddataByte0);

    byte[] processData1 = new byte[]{2, -31, -125, 0, 26, 6, 18, 32, 126, 84, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, 121, -5, 3};

    byte[] carddataByte1 = new byte[128];
    System.arraycopy(processData1, 6, carddataByte1, 0, 128);
    processor.handleProcessData(carddataByte1);

    byte[] processRemoved = new byte[]{2, -25, 6, 0, 26, 0, 11, -56, 7, -64, 36, 3};
    processor.handleCardRemoved(processRemoved);

    Assert.assertEquals("Verify E-Card Number", "772103", processor.getECardNo());

    Assert.assertNull(processor.getCheckTime());
    Assert.assertEquals(-31672000, processor.getClearTime().longValue());
    Assert.assertNull(processor.getStartTime());
    Assert.assertNull(processor.getFinishTime());

    Assert.assertEquals("Verify number of controls", 1, processor.getControlData().size());

    Assert.assertEquals("Verify control number", "32", processor.getControlData().get(0).getControlNo().getValue());
  }

  private SICardV6Processor createProcessor() throws ProcessingException {
    FMilaSerialPort testPort = new FMilaSerialTestPort();

    EventBean eventFormData = new EventBean();
    eventFormData.setEventNr(event.getEventNr());
    eventFormData = BEANS.get(IEventProcessService.class).load(eventFormData);
    GregorianCalendar greg = new GregorianCalendar(2012, 1, 1, 12, 30, 00);

    SICardV6Processor processor = new SICardV6Processor(station.getFormData(), testPort, greg.getTime(), event.getEventNr(), ClientSession.get());
    return processor;
  }

  @After
  public void after() throws ProcessingException {
    station.remove();
    event.remove();
  }

}
