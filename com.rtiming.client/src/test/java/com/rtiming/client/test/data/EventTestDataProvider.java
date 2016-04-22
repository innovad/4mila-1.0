package com.rtiming.client.test.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.junit.Assert;

import com.rtiming.client.event.EventForm;
import com.rtiming.client.event.EventForm.MainBox.FinishTimeField;
import com.rtiming.client.event.EventForm.MainBox.ZeroTimeField;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.client.test.FormTestUtility.OrderedFieldPair;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.client.test.field.MaxFormFieldValueProvider;
import com.rtiming.shared.event.IEventProcessService;

public class EventTestDataProvider extends AbstractTestDataProvider<EventForm> {

  private FieldValue[] values;
  private Date evtZero;
  private Date evtFinish;

  public EventTestDataProvider(boolean callInitializer) throws ProcessingException {
    values = new FieldValue[]{};
    if (callInitializer) {
      callInitializer();
    }
  }

  public EventTestDataProvider(FieldValue... values) throws ProcessingException {
    this.values = values;
    callInitializer();
  }

  public EventTestDataProvider() throws ProcessingException {
    values = new FieldValue[]{};
    callInitializer();
  }

  public EventTestDataProvider(Date evtZero, Date evtFinish) throws ProcessingException {
    this.evtZero = evtZero;
    this.evtFinish = evtFinish;
    values = new FieldValue[]{};
    callInitializer();
  }

  @Override
  protected EventForm createForm() throws ProcessingException {
    EventForm event = new EventForm();
    OrderedFieldPair orderedFieldPairs = new OrderedFieldPair(ZeroTimeField.class, FinishTimeField.class);
    List<OrderedFieldPair> list = new ArrayList<OrderedFieldPair>();
    list.add(orderedFieldPairs);
    event.startNew();
    FormTestUtility.fillFormFields(event, new MaxFormFieldValueProvider(), list, values);
    // only minutes are allowed for zero and finish time
    if (evtZero != null) {
      event.getZeroTimeField().setValue(evtZero);
    }
    if (evtFinish != null) {
      event.getFinishTimeField().setValue(evtFinish);
    }
    event.getZeroTimeField().setValue(DateUtility.truncDateToMinute(event.getZeroTimeField().getValue()));
    event.getFinishTimeField().setValue(DateUtility.truncDateToMinute(event.getFinishTimeField().getValue()));
    event.touch();
    event.doOk();
    Assert.assertNotNull(event.getEventNr());
    return event;
  }

  @Override
  public void remove() throws ProcessingException {
    BEANS.get(IEventProcessService.class).delete(getForm().getEventNr(), true);
  }

  public Long getEventNr() throws ProcessingException {
    return getForm().getEventNr();
  }

}
