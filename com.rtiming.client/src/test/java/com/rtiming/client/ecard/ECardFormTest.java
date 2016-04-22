package com.rtiming.client.ecard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ecard.ECardForm.MainBox.NumberField;
import com.rtiming.client.test.AbstractFormTest;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.dao.RtEcard;
import com.rtiming.shared.ecard.IECardProcessService;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestEnvironmentClientSession.class)
public class ECardFormTest extends AbstractFormTest<ECardForm> {

  @Override
  protected ECardForm getStartedForm() throws ProcessingException {
    ECardForm form = new ECardForm();
    form.startNew();
    return form;
  }

  @Override
  protected ECardForm getModifyForm() throws ProcessingException {
    ECardForm form = new ECardForm();
    form.setECardKey(getForm().getECardKey());
    form.startModify();
    return form;
  }

  @Override
  public void cleanup() throws ProcessingException {
    if (getForm().getECardKey() != null) {
      BEANS.get(IECardProcessService.class).delete(getForm().getECardKey());
    }
  }

  @Override
  protected List<FieldValue> getFixedValues() {
    List<FieldValue> result = new ArrayList<FieldValue>();
    FieldValue value = new FieldValue(NumberField.class, "1");
    result.add(value);
    return result;
  }

  @Test
  public void testFind() throws ProcessingException {
    getForm().getNumberField().setValue("1151999");
    getForm().doOk();

    RtEcard formData = BEANS.get(IECardProcessService.class).findECard("1151999");
    Assert.assertNotNull("Key exists", formData.getKey());
    Assert.assertEquals(getForm().getECardKey().getId(), formData.getKey().getId());
    Assert.assertEquals(getForm().getECardTypeField().getValue(), formData.getTypeUid());
  }

}
