package com.rtiming.client.event;

import org.eclipse.scout.rt.client.ui.action.menu.IMenu;
import org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn;
import org.eclipse.scout.rt.client.ui.form.IForm;
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
import com.rtiming.client.event.EventsTablePage.Table.DeleteMenu;
import com.rtiming.client.event.EventsTablePage.Table.EditMenu;
import com.rtiming.client.event.EventsTablePage.Table.EventNrColumn;
import com.rtiming.client.event.EventsTablePage.Table.NewMenu;
import com.rtiming.client.test.AbstractEntityTablePageTest;
import com.rtiming.client.test.data.EventTestDataProvider;
import com.rtiming.shared.common.database.sql.EventBean;
import com.rtiming.shared.event.IEventProcessService;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class EventsTablePageTest extends AbstractEntityTablePageTest<EventsTablePage, Long> {

  private EventTestDataProvider event;

  @Before
  public void before() throws ProcessingException {
    event = new EventTestDataProvider();
  }

  @Override
  protected EventsTablePage getTablePage() {
    return new EventsTablePage(ClientSession.get().getSessionClientNr());
  }

  @After
  public void after() throws ProcessingException {
    if (event != null) {
      event.remove();
    }
  }

  @Override
  protected Class<? extends IMenu> getNewMenu() {
    return NewMenu.class;
  }

  @Override
  protected Class<? extends IForm> getForm() {
    return EventForm.class;
  }

  @Override
  protected Class<? extends IMenu> getDeleteMenu() {
    return DeleteMenu.class;
  }

  @Override
  protected Class<? extends IColumn<Long>> getPrimaryKeyColumn() {
    return EventNrColumn.class;
  }

  @Override
  protected Long getPrimaryKey() throws ProcessingException {
    return event.getEventNr();
  }

  @Override
  protected Class<? extends IMenu> getEditMenu() {
    return EditMenu.class;
  }

  @Test
  public void testPublishOnline() throws Exception {
    BEANS.get(IEventProcessService.class).syncWithOnline(event.getEventNr());
    EventBean eventBean = new EventBean();
    eventBean.setEventNr(event.getEventNr());
    eventBean = BEANS.get(IEventProcessService.class).load(eventBean);

    Assert.assertEquals("Same value after transfer", event.getForm().getNameField().getValue(), eventBean.getName());
  }

}
