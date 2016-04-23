package com.rtiming.client.result;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.test.data.EventWithIndividualClassTestDataProvider;
import com.rtiming.client.test.data.EventWithRelayClassTestDataProvider;
import com.rtiming.shared.settings.IDefaultProcessService;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class ResultsClassesTablePageRelayTest {

  private EventWithRelayClassTestDataProvider relay;
  private EventWithIndividualClassTestDataProvider individual;
  private Long defaultEventNr;

  @Test
  public void testPage() throws Exception {
    defaultEventNr = BEANS.get(IDefaultProcessService.class).getDefaultEventNr();
    relay = new EventWithRelayClassTestDataProvider();
    individual = new EventWithIndividualClassTestDataProvider();

    BEANS.get(IDefaultProcessService.class).setDefaultEventNr(relay.getEventNr());
    ResultsClassesTablePage page = new ResultsClassesTablePage(ClientSession.get().getSessionClientNr());
    page.nodeAddedNotify();
    SingleEventSearchForm search = (SingleEventSearchForm) page.getSearchFormInternal();
    search.doReset();
    search.getEventField().setValue(relay.getEventNr());
    search.resetSearchFilter();
    page.loadChildren();

    Assert.assertEquals("3 Leg Rows + 1 Total Row", 4, page.getTable().getRowCount());
    Assert.assertTrue("Relay Leg Parent visible", page.getTable().getParentColumn().isVisible());
    Assert.assertTrue("Relay Leg visible", page.getTable().getClazzColumn().isVisible());
    Assert.assertFalse("Leg/Class name not null", StringUtility.isNullOrEmpty(page.getTable().getParentColumn().getValue(0)));
    Assert.assertFalse("Leg/Class name not null", StringUtility.isNullOrEmpty(page.getTable().getParentColumn().getValue(1)));
    Assert.assertFalse("Leg/Class name not null", StringUtility.isNullOrEmpty(page.getTable().getParentColumn().getValue(2)));

    BEANS.get(IDefaultProcessService.class).setDefaultEventNr(individual.getEventNr());
    page = new ResultsClassesTablePage(ClientSession.get().getSessionClientNr());
    page.nodeAddedNotify();
    search = (SingleEventSearchForm) page.getSearchFormInternal();
    search.getEventField().setValue(individual.getEventNr());
    page.loadChildren();

    Assert.assertEquals("2 Rows", 2, page.getTable().getRowCount());
    Assert.assertTrue("Individual class visible", page.getTable().getClazzColumn().isVisible());
    Assert.assertFalse("Individual Parent invisible", page.getTable().getParentColumn().isVisible());
    Assert.assertFalse("Leg/Class name not null", StringUtility.isNullOrEmpty(page.getTable().getClazzColumn().getValue(0)));
  }

  @After
  public void after() throws Exception {
    relay.remove();
    individual.remove();
    if (defaultEventNr != null) {
      BEANS.get(IDefaultProcessService.class).setDefaultEventNr(defaultEventNr);
    }
  }

}
