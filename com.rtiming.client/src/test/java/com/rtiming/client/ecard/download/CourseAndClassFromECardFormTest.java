package com.rtiming.client.ecard.download;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.client.test.field.MaxFormFieldValueProvider;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class CourseAndClassFromECardFormTest {

  @Test
  public void testNew() throws ProcessingException {
    CourseAndClassFromECardForm form = new CourseAndClassFromECardForm();
    form.startNew();
    FormTestUtility.fillFormFields(form, new MaxFormFieldValueProvider());
    form.doClose();
    // TODO do some validation test
  }

}
