package com.rtiming.client.ecard.download.job;

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
import com.rtiming.client.TestClientSession;
import com.rtiming.client.ecard.download.CourseAndClassFromECardForm;
import com.rtiming.client.ecard.download.FMilaSerialTestPort;
import com.rtiming.client.ecard.download.processor.SICardV5Processor;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.client.test.data.CodeTestDataProvider;
import com.rtiming.client.test.data.ECardStationTestDataProvider;
import com.rtiming.client.test.data.EventTestDataProvider;
import com.rtiming.serial.FMilaSerialPort;
import com.rtiming.shared.common.database.sql.EventBean;
import com.rtiming.shared.ecard.download.ECardStationDownloadModusCodeType;
import com.rtiming.shared.event.IEventProcessService;
import com.rtiming.shared.event.course.ClassCodeType;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class CourseAndClassFromECardJobTest {

  private EventTestDataProvider event;
  private ECardStationTestDataProvider station;
  private CodeTestDataProvider clazz;

  @Before
  public void before() throws ProcessingException {
    event = new EventTestDataProvider();
    station = new ECardStationTestDataProvider(ECardStationDownloadModusCodeType.CourseAndClassFromECardCode.ID);
    clazz = new CodeTestDataProvider(ClassCodeType.ID);
  }

  @Test
  public void test() throws Exception {
    FMilaSerialPort testPort = new FMilaSerialTestPort();

    EventBean eventFormData = new EventBean();
    eventFormData.setEventNr(event.getEventNr());
    eventFormData = BEANS.get(IEventProcessService.class).load(eventFormData);

    SICardV5Processor processor = new SICardV5Processor(station.getFormData(), testPort, eventFormData.getEvtZero(), event.getEventNr(), ClientSession.get());

    byte[] insertedData = new byte[]{2, -27, 6, 0, 4, 0, 0, -10, -19, -29, -101, 3};
    processor.handleCardInserted(insertedData);

    byte[] processData = new byte[]{2, -79, -126, 0, 4, -86, 41, 0, 1, -10, -19, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 101, -10, -19, 35, 2, 42, 52, 10, 86, -11, -90, 40, 1, -28, 0, 7, 0, 34, 35, -116, 52, 36, 99, 54, 37, 30, 88, 37, -93, 90, 38, 43, 0, 96, 39, 108, 95, 40, 15, 99, 41, -86, 100, 42, 12, 0, -18, -18, 0, 0, -18, -18, 0, -18, -18, 0, -18, -18, 0, -18, -18, 0, -18, -18, 0, 0, -18, -18, 0, -18, -18, 0, -18, -18, 0, -18, -18, 0, -18, -18, 0, 0, -18, -18, 0, -18, -18, 0, -18, -18, 0, -18, -18, 0, -18, -18, 0, 0, -18, -18, 0, -18, -18, 0, -18, -18, 0, -18, -18, 0, -18, -18, -26, 7, 3};
    byte[] carddataByte = new byte[128];
    System.arraycopy(processData, 5, carddataByte, 0, 128);
    processor.handleProcessData(carddataByte);

    byte[] processRemoved = new byte[]{2, -25, 6, 0, 4, 0, 0, -10, -19, -61, -105, 3};
    processor.handleCardRemoved(processRemoved);

    Assert.assertEquals("63213", processor.getECardNo());

    CourseAndClassFromECardForm form = ClientSession.get().getDesktop().findForm(CourseAndClassFromECardForm.class);
    Assert.assertNotNull(form);
    FormTestUtility.fillFormFields(form);
    form.getEventField().setValue(event.getEventNr());
    form.getClazzField().setValue(clazz.getCodeUid());
    form.doOk();
  }

  @After
  public void after() throws ProcessingException {
    event.remove();
    station.remove();
    clazz.remove();
  }

}
