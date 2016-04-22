package com.rtiming.client.event;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.runner.RunWith;

import com.rtiming.client.event.EventClassForm.MainBox.ClazzField;
import com.rtiming.client.event.EventClassForm.MainBox.TeamSizeBox.TeamSizeMaxField;
import com.rtiming.client.event.EventClassForm.MainBox.TeamSizeBox.TeamSizeMinField;
import com.rtiming.client.test.AbstractFormTest;
import com.rtiming.client.test.FormTestUtility.OrderedFieldPair;
import com.rtiming.client.test.data.CodeTestDataProvider;
import com.rtiming.client.test.data.EventTestDataProvider;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.event.course.ClassCodeType;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestEnvironmentClientSession.class)
public class EventClassFormTest extends AbstractFormTest<EventClassForm> {

  private EventTestDataProvider event;
  private CodeTestDataProvider clazz;

  @Override
  public void setUpForm() throws ProcessingException {
    event = new EventTestDataProvider();
    clazz = new CodeTestDataProvider(ClassCodeType.ID);
    super.setUpForm();
  }

  /* (non-Javadoc)
   * @see com.rtiming.client.test.AbstractFormTest#getOrderedFieldPairs()
   */
  @Override
  protected ArrayList<OrderedFieldPair> getOrderedFieldPairs() {
    ArrayList<OrderedFieldPair> list = new ArrayList<OrderedFieldPair>();
    list.add(new OrderedFieldPair(TeamSizeMinField.class, TeamSizeMaxField.class));
    return list;
  }

  @Override
  protected List<FieldValue> getFixedValues() throws ProcessingException {
    List<FieldValue> result = new ArrayList<FieldValue>();
    FieldValue value = new FieldValue(ClazzField.class, clazz.getCodeUid());
    result.add(value);
    return result;
  }

  @Override
  protected EventClassForm getStartedForm() throws ProcessingException {
    EventClassForm form = new EventClassForm();
    form.getEventField().setValue(event.getEventNr());
    form.getParentField().setValue(null);
    form.startNew();
    return form;
  }

  @Override
  protected EventClassForm getModifyForm() throws ProcessingException {
    EventClassForm form = new EventClassForm();
    form.getEventField().setValue(event.getEventNr());
    form.getClazzField().setValue(getForm().getClazzField().getValue());
    form.getParentField().setValue(null);
    form.startModify();
    return form;
  }

  @Override
  public void cleanup() throws ProcessingException {
    event.remove();
  }

}
