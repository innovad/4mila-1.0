package com.rtiming.client.ecard.download;

import java.util.Date;
import java.util.List;

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
import com.rtiming.client.ecard.download.processor.SICardV5Processor;
import com.rtiming.client.ecard.download.processor.SICardV6Processor;
import com.rtiming.client.test.data.ECardStationTestDataProvider;
import com.rtiming.client.test.data.EventTestDataProvider;
import com.rtiming.serial.FMilaSerialPort;
import com.rtiming.shared.ecard.download.DownloadedECardFormData;
import com.rtiming.shared.ecard.download.IDownloadedECardProcessService;
import com.rtiming.shared.event.IEventProcessService;
import com.rtiming.shared.test.helper.ITestingJPAService;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class SICardSerialPortHandlerTest {

  private EventTestDataProvider event;
  private ECardStationTestDataProvider station;

  @Before
  public void before() throws ProcessingException {
    event = new EventTestDataProvider();
    station = new ECardStationTestDataProvider();
  }

  @Test(expected = ProcessingException.class)
  public void testUnsupportedOperation() throws Exception {
    SICardSerialPortHandler handler = prepareSerialPortHandler();
    handler.handleData(new byte[]{0});
  }

  @Test
  public void testV5inserted() throws Exception {
    SICardSerialPortHandler handler = prepareSerialPortHandler();
    handler.handleData(new byte[]{2, -27, 6, 0, 26, 0, 0, -10, -19, -26, -53, 3});
    Assert.assertTrue("Must be SICard V5 Processor", handler.getProcessor() instanceof SICardV5Processor);
  }

  @Test
  public void testV6inserted() throws Exception {
    SICardSerialPortHandler handler = prepareSerialPortHandler();
    handler.handleData(new byte[]{2, -26, 6, 0, 26, 0, 11, -56, 7, -48, 34, 3});
    Assert.assertTrue("Must be SICard V6 Processor", handler.getProcessor() instanceof SICardV6Processor);
  }

  @Test
  public void testV8inserted() throws Exception {
    SICardSerialPortHandler handler = prepareSerialPortHandler();
    handler.handleData(new byte[]{2, -24, 6, 0, 26, 1, 17, -109, -25, 125, -117, 3});
  }

  @Test
  public void testV6DataConfig() throws Exception {
    SICardSerialPortHandler handler = prepareSerialPortHandler();
    handler.handleData(new byte[]{2, -26, 6, 0, 26, 0, 11, -56, 7, -48, 34, 3});
    handler.handleData(new byte[]{2, -125, 4, 0, 26, 51, -63, 32, 38, 3}); // -63
    Assert.assertTrue("Must be SICard V6 Processor", handler.getProcessor() instanceof SICardV6Processor);
    Assert.assertEquals("Must be SICard V6, not V6 Star", SICardV6Processor.MODEL.SIX, ((SICardV6Processor) handler.getProcessor()).getModel());
  }

  @Test
  public void testV6StarDataConfig() throws Exception {
    SICardSerialPortHandler handler = prepareSerialPortHandler();
    handler.handleData(new byte[]{2, -26, 6, 0, 26, 0, 11, -56, 7, -48, 34, 3});
    handler.handleData(new byte[]{2, -125, 4, 0, 26, 51, -1, 32, 38, 3}); // -1
    Assert.assertTrue("Must be SICard V6 Processor", handler.getProcessor() instanceof SICardV6Processor);
    Assert.assertEquals("Must be SICard V6, not V6 Star", SICardV6Processor.MODEL.SIXSTAR, ((SICardV6Processor) handler.getProcessor()).getModel());
  }

  @Test(expected = ProcessingException.class)
  public void testV6DataConfigUnknown() throws Exception {
    SICardSerialPortHandler handler = prepareSerialPortHandler();
    handler.handleData(new byte[]{2, -26, 6, 0, 26, 0, 11, -56, 7, -48, 34, 3});
    handler.handleData(new byte[]{2, -125, 4, 0, 26, 51, 0, 32, 38, 3}); // 0 is not known
  }

  @Test
  public void testV5Data() throws Exception {
    SICardSerialPortHandler handler = prepareSerialPortHandler();
    handler.handleData(new byte[]{2, -27, 6, 0, 26, 0, 0, -10, -19, -26, -53, 3});
    handler.handleData(new byte[]{2, -79, -126, 0, 26, -86, 41, 0, 1, -10, -19, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 101, -10, -19, 35, 2, 42, 52, 10, 86, -11, -90, 40, 1, -28, 0, 7, 0, 34, 35, -116, 52, 36, 99, 54, 37, 30, 88, 37, -93, 90, 38, 43, 0, 96, 39, 108, 95, 40, 15, 99, 41, -86, 100, 42, 12, 0, -18, -18, 0, 0, -18, -18, 0, -18, -18, 0, -18, -18, 0, -18, -18, 0, -18, -18, 0, 0, -18, -18, 0, -18, -18, 0, -18, -18, 0, -18, -18, 0, -18, -18, 0, 0, -18, -18, 0, -18, -18, 0, -18, -18, 0, -18, -18, 0, -18, -18, 0, 0, -18, -18, 0, -18, -18, 0, -18, -18, 0, -18, -18, 0, -18, -18, -29, -33, 3});
  }

  @Test
  public void testV6DataWith1Control() throws Exception {
    SICardSerialPortHandler handler = prepareSerialPortHandler();
    handler.handleData(new byte[]{2, -26, 6, 0, 26, 0, 11, -56, 7, -48, 34, 3});
    handler.handleData(new byte[]{2, -125, 4, 0, 26, 51, -63, 32, 38, 3}); // -63
    Assert.assertEquals("Data messages 3+1: Initial setting for SI Card 6", 4, handler.getProcessor().getNumberOfDataMessages());
    handler.handleData(new byte[]{2, -31, -125, 0, 26, 0, 1, 1, 1, 1, -19, -19, -19, -19, 85, -86, 0, 11, -56, 7, -104, -30, 0, 32, 1, 2, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, 50, 1, 52, 16, -1, -1, -1, -1, 0, 0, 0, 1, 32, 32, 32, 32, 76, 97, 116, 115, 99, 104, 97, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 73, 114, 105, 115, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 83, 85, 73, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, -6, 48, 3});
    Assert.assertEquals("Data messages 2+1: After analysis of punches", 3, handler.getProcessor().getNumberOfDataMessages());
  }

  @Test
  public void testV6DataWith32Control() throws Exception {
    SICardSerialPortHandler handler = prepareSerialPortHandler();
    handler.handleData(new byte[]{2, -26, 6, 0, 26, 0, 11, -56, 7, -48, 34, 3});
    handler.handleData(new byte[]{2, -125, 4, 0, 26, 51, -63, 32, 38, 3}); // -63
    Assert.assertEquals("Data messages 3+1: Initial setting for SI Card 6", 4, handler.getProcessor().getNumberOfDataMessages());
    handler.handleData(new byte[]{2, -31, -125, 0, 26, 0, 1, 1, 1, 1, -19, -19, -19, -19, 85, -86, 0, 11, -56, 7, -104, -30, 0, 32, /* num of controls */32, 2, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, 50, 1, 52, 16, -1, -1, -1, -1, 0, 0, 0, 1, 32, 32, 32, 32, 76, 97, 116, 115, 99, 104, 97, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 73, 114, 105, 115, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 83, 85, 73, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, -6, 48, 3});
    Assert.assertEquals("Data messages 2+1: After analysis of punches", 3, handler.getProcessor().getNumberOfDataMessages());
  }

  @Test
  public void testV6DataWith33Control() throws Exception {
    SICardSerialPortHandler handler = prepareSerialPortHandler();
    handler.handleData(new byte[]{2, -26, 6, 0, 26, 0, 11, -56, 7, -48, 34, 3});
    handler.handleData(new byte[]{2, -125, 4, 0, 26, 51, -63, 32, 38, 3}); // -63
    Assert.assertEquals("Data messages 3+1: Initial setting for SI Card 6", 4, handler.getProcessor().getNumberOfDataMessages());
    handler.handleData(new byte[]{2, -31, -125, 0, 26, 0, 1, 1, 1, 1, -19, -19, -19, -19, 85, -86, 0, 11, -56, 7, -104, -30, 0, 32, /* num of controls */33, 2, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, 50, 1, 52, 16, -1, -1, -1, -1, 0, 0, 0, 1, 32, 32, 32, 32, 76, 97, 116, 115, 99, 104, 97, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 73, 114, 105, 115, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 83, 85, 73, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, -6, 48, 3});
    Assert.assertEquals("Data messages 3+1: After analysis of punches, more than 32 punches", 4, handler.getProcessor().getNumberOfDataMessages());
  }

  @Test
  public void testV6StarDataWith0Controls() throws Exception {
    SICardSerialPortHandler handler = prepareSerialPortHandler();
    handler.handleData(new byte[]{2, -26, 6, 0, 26, 0, 11, -56, 7, -48, 34, 3});
    handler.handleData(new byte[]{2, -125, 4, 0, 26, 51, -1, 32, 38, 3}); // -1
    Assert.assertEquals("Data messages 7+1: Initial setting for SI Card 6*", 8, handler.getProcessor().getNumberOfDataMessages());
    handler.handleData(new byte[]{2, -31, -125, 0, 26, 0, 1, 1, 1, 1, -19, -19, -19, -19, 85, -86, 0, 11, -56, 7, -104, -30, 0, 32, /* num of controls */0, 2, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, 50, 1, 52, 16, -1, -1, -1, -1, 0, 0, 0, 1, 32, 32, 32, 32, 76, 97, 116, 115, 99, 104, 97, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 73, 114, 105, 115, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 83, 85, 73, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, -6, 48, 3});
    Assert.assertEquals("Data messages 1+1: After analysis of punches, 0 punches", 2, handler.getProcessor().getNumberOfDataMessages());
  }

  @Test
  public void testV6StarDataWith33Control() throws Exception {
    SICardSerialPortHandler handler = prepareSerialPortHandler();
    handler.handleData(new byte[]{2, -26, 6, 0, 26, 0, 11, -56, 7, -48, 34, 3});
    handler.handleData(new byte[]{2, -125, 4, 0, 26, 51, -1, 32, 38, 3}); // -1
    Assert.assertEquals("Data messages 7+1: Initial setting for SI Card 6*", 8, handler.getProcessor().getNumberOfDataMessages());
    handler.handleData(new byte[]{2, -31, -125, 0, 26, 0, 1, 1, 1, 1, -19, -19, -19, -19, 85, -86, 0, 11, -56, 7, -104, -30, 0, 32, /* num of controls */33, 2, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, 50, 1, 52, 16, -1, -1, -1, -1, 0, 0, 0, 1, 32, 32, 32, 32, 76, 97, 116, 115, 99, 104, 97, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 73, 114, 105, 115, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 83, 85, 73, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, -6, 48, 3});
    Assert.assertEquals("Data messages 3+1: After analysis of punches, more than 32 punches", 4, handler.getProcessor().getNumberOfDataMessages());
  }

  @Test
  public void testV6StarDataWith192Control() throws Exception {
    SICardSerialPortHandler handler = prepareSerialPortHandler();
    handler.handleData(new byte[]{2, -26, 6, 0, 26, 0, 11, -56, 7, -48, 34, 3});
    handler.handleData(new byte[]{2, -125, 4, 0, 26, 51, -1, 32, 38, 3}); // -1
    Assert.assertEquals("Data messages 7+1: Initial setting for SI Card 6*", 8, handler.getProcessor().getNumberOfDataMessages());
    handler.handleData(new byte[]{2, -31, -125, 0, 26, 0, 1, 1, 1, 1, -19, -19, -19, -19, 85, -86, 0, 11, -56, 7, -104, -30, 0, 32, /* num of controls */-64, 2, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, 50, 1, 52, 16, -1, -1, -1, -1, 0, 0, 0, 1, 32, 32, 32, 32, 76, 97, 116, 115, 99, 104, 97, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 73, 114, 105, 115, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 83, 85, 73, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, -6, 48, 3});
    Assert.assertEquals("Data messages 7+1: After analysis of punches, 192 punches => 8 blocks", 8, handler.getProcessor().getNumberOfDataMessages());
  }

  @Test
  public void testV8Data0() throws Exception {
    SICardSerialPortHandler handler = prepareSerialPortHandler();
    handler.handleData(new byte[]{2, -24, 6, 0, 26, 1, 17, -109, -25, 125, -117, 3});
    handler.handleData(new byte[]{2, -17, -125, 0, 26, 0, 26, -67, 18, 127, -22, -22, -22, -22, 0, 1, -106, 106, -128, -60, -106, -55, -128, -30, -94, 0, 0, 99, 21, 124, 1, 17, -109, -25, 12, -1, 85, -33, 65, 100, 114, 105, 97, 110, 59, 77, 111, 115, 101, 114, 59, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 57, -106, -13, 0, 48, -105, -102, 0, 47, -105, -27, 0, 43, -104, 44, 0, 46, -104, 110, 0, 58, -104, -59, 0, 47, -104, -3, 0, 44, -103, -122, 0, 60, -102, 41, 0, 42, -101, 80, 0, 34, -100, 66, 0, 74, -100, -121, 0, 55, -98, 56, 0, 53, -98, -58, 0, 54, -98, -1, 0, 40, -97, -112, 0, 85, -96, 43, 0, 53, -96, -116, -126, -34, 3});
  }

  @Test
  public void testV8Data1() throws Exception {
    SICardSerialPortHandler handler = prepareSerialPortHandler();
    handler.handleData(new byte[]{2, -24, 6, 0, 26, 1, 17, -109, -25, 125, -117, 3});
    handler.handleData(new byte[]{2, -17, -125, 0, 26, 0, 26, -67, 18, 127, -22, -22, -22, -22, 0, 1, -106, 106, -128, -60, -106, -55, -128, -30, -94, 0, 0, 99, 21, 124, 1, 17, -109, -25, 12, -1, 85, -33, 65, 100, 114, 105, 97, 110, 59, 77, 111, 115, 101, 114, 59, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 57, -106, -13, 0, 48, -105, -102, 0, 47, -105, -27, 0, 43, -104, 44, 0, 46, -104, 110, 0, 58, -104, -59, 0, 47, -104, -3, 0, 44, -103, -122, 0, 60, -102, 41, 0, 42, -101, 80, 0, 34, -100, 66, 0, 74, -100, -121, 0, 55, -98, 56, 0, 53, -98, -58, 0, 54, -98, -1, 0, 40, -97, -112, 0, 85, -96, 43, 0, 53, -96, -116, -126, -34, 3});
    handler.handleData(new byte[]{2, -17, -125, 0, 26, 1, 0, 67, -95, 39, 0, 69, -95, -98, 0, 99, -95, -28, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, 67, -113, 3});
  }

  @Test
  public void testRemove() throws Exception {
    SICardSerialPortHandler handler = prepareSerialPortHandler();
    handler.handleData(new byte[]{2, -25, 6, 0, 26, 0, 17, -109, -25, 25, -86, 3});
  }

  private SICardSerialPortHandler prepareSerialPortHandler() throws ProcessingException {
    FMilaSerialPort port = new FMilaSerialTestPort();
    Date currentEvtZero = BEANS.get(IEventProcessService.class).getZeroTime(event.getEventNr());
    SICardSerialPortHandler handler = new SICardSerialPortHandler(station.getFormData(), currentEvtZero, event.getEventNr(), ClientSession.get(), port);
    return handler;
  }

  @After
  public void after() throws ProcessingException {
    List<Long> punchSessionNrs = BEANS.get(ITestingJPAService.class).getPunchSessionsForStation(station.getECardStationNr());

    for (Long punchSessionNr : punchSessionNrs) {
      DownloadedECardFormData formData = new DownloadedECardFormData();
      formData.setPunchSessionNr(punchSessionNr);
      BEANS.get(IDownloadedECardProcessService.class).delete(formData);
    }

    event.remove();
    station.remove();
  }

}
