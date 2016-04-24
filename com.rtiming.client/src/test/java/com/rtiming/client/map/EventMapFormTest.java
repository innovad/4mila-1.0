package com.rtiming.client.map;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.runner.RunWith;

import com.rtiming.client.TestClientSession;
import com.rtiming.client.map.EventMapForm.MainBox.EventField;
import com.rtiming.client.map.EventMapForm.MainBox.MapField;
import com.rtiming.client.test.AbstractFormTest;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.client.test.data.EventTestDataProvider;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.client.test.field.MaxFormFieldValueProvider;
import com.rtiming.shared.event.EventMapFormData;
import com.rtiming.shared.map.IEventMapProcessService;
import com.rtiming.shared.map.IMapProcessService;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class EventMapFormTest extends AbstractFormTest<EventMapForm> {

  private MapForm map;
  private EventTestDataProvider event;

  @Override
  public void setUpForm() throws ProcessingException {
    event = new EventTestDataProvider();

    map = new MapForm();
    FormTestUtility.fillFormFields(map, new MaxFormFieldValueProvider());
    map.startNew();
    map.touch();
    map.doOk();

    super.setUpForm();
  }

  @Override
  protected EventMapForm getStartedForm() throws ProcessingException {
    EventMapForm form = new EventMapForm();
    form.getEventField().setValue(event.getEventNr());
    form.startNew();
    return form;
  }

  @Override
  protected EventMapForm getModifyForm() throws ProcessingException {
    EventMapForm form = new EventMapForm();
    form.getEventField().setValue(event.getEventNr());
    form.getMapField().setValue(map.getMapKey().getId());
    form.startModify();
    return form;
  }

  @Override
  public void cleanup() throws ProcessingException {
    EventMapFormData formData = new EventMapFormData();
    formData.getEvent().setValue(event.getEventNr());
    formData.getMap().setValue(map.getMapKey().getId());
    BEANS.get(IEventMapProcessService.class).delete(formData);
    // Map
    BEANS.get(IMapProcessService.class).delete(map.getMapKey());
    // Event
    event.remove();
  }

  @Override
  protected List<FieldValue> getFixedValues() throws ProcessingException {
    List<FieldValue> result = new ArrayList<FieldValue>();
    FieldValue eventField = new FieldValue(EventField.class, event.getEventNr());
    result.add(eventField);
    FieldValue mapField = new FieldValue(MapField.class, map.getMapKey().getId());
    result.add(mapField);
    return result;
  }

}
