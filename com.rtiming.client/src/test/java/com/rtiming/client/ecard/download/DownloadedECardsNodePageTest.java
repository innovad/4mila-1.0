package com.rtiming.client.ecard.download;

import java.util.ArrayList;

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

import com.rtiming.client.event.EventForm.MainBox.PunchingSystemField;
import com.rtiming.client.result.ResultsOutline;
import com.rtiming.client.test.data.EventTestDataProvider;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.ecard.download.PunchingSystemCodeType;
import com.rtiming.shared.ecard.download.PunchingSystemCodeType.PunchingSystemNoneCode;
import com.rtiming.shared.settings.IDefaultProcessService;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestEnvironmentClientSession.class)
public class DownloadedECardsNodePageTest {

  private EventTestDataProvider event;
  private Long defaultEventNr;

  @Before
  public void before() throws ProcessingException {
    defaultEventNr = BEANS.get(IDefaultProcessService.class).getDefaultEventNr();
  }

  @Test
  public void testElectronicPunchingSystem() throws Exception {
    ArrayList<FieldValue> list = new ArrayList<FieldValue>();
    list.add(new FieldValue(PunchingSystemField.class, PunchingSystemCodeType.SportIdentCode.ID));
    event = new EventTestDataProvider(list.toArray(new FieldValue[1]));
    BEANS.get(IDefaultProcessService.class).setDefaultEventNr(event.getEventNr());

    DownloadedECardsNodePage page = new DownloadedECardsNodePage();
    page.setTreeInternal(new ResultsOutline(), true);
    page.nodeAddedNotify();
    page.loadChildren();

    Assert.assertEquals("Punching System", event.getForm().getPunchingSystemField().getValue().longValue(), PunchingSystemCodeType.SportIdentCode.ID);
    Assert.assertEquals("6 child pages", 6, page.getChildNodeCount());
  }

  @Test
  public void testPunchingSystemNone() throws Exception {
    ArrayList<FieldValue> list = new ArrayList<FieldValue>();
    list.add(new FieldValue(PunchingSystemField.class, PunchingSystemNoneCode.ID));
    event = new EventTestDataProvider(list.toArray(new FieldValue[1]));
    BEANS.get(IDefaultProcessService.class).setDefaultEventNr(event.getEventNr());

    DownloadedECardsNodePage page = new DownloadedECardsNodePage();
    page.setTreeInternal(new ResultsOutline(), true);
    page.nodeAddedNotify();
    page.loadChildren();

    Assert.assertEquals("Punching System", event.getForm().getPunchingSystemField().getValue().longValue(), PunchingSystemCodeType.PunchingSystemNoneCode.ID);
    Assert.assertEquals("2 child pages", 2, page.getChildNodeCount());
  }

  @After
  public void after() throws ProcessingException {
    if (defaultEventNr != null) {
      BEANS.get(IDefaultProcessService.class).setDefaultEventNr(defaultEventNr);
    }
    event.remove();
  }

}
