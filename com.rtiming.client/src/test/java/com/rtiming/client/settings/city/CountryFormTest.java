package com.rtiming.client.settings.city;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.common.ui.fields.AbstractCodeBox.LanguageField;
import com.rtiming.client.test.AbstractFormTest;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.shared.common.AbstractCodeBoxData.Language;
import com.rtiming.shared.settings.city.CountryFormData;
import com.rtiming.shared.settings.city.ICountryProcessService;

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(TestEnvironmentClientSession.class)
public class CountryFormTest extends AbstractFormTest<CountryForm> {

  @Override
  protected CountryForm getStartedForm() throws ProcessingException {
    CountryForm form = new CountryForm();
    form.startNew();
    return form;
  }

  @Override
  protected CountryForm getModifyForm() throws ProcessingException {
    CountryForm form = new CountryForm();
    form.setCountryUid(getForm().getCountryUid());
    form.startModify();
    return form;
  }

  @Override
  public void cleanup() throws ProcessingException {
    CountryFormData formData = new CountryFormData();
    formData.setCountryUid(getForm().getCountryUid());
    BEANS.get(ICountryProcessService.class).delete(formData);
  }

  @Test
  public void testFind() throws ProcessingException {
    createCountry();

    CountryFormData formData = BEANS.get(ICountryProcessService.class).find("LAND1", "XY", "NAT");
    Assert.assertEquals(getForm().getCountryUid(), formData.getCountryUid());
  }

  @Test
  public void testFindCodeOnly() throws ProcessingException {
    createCountry();

    CountryFormData formData = BEANS.get(ICountryProcessService.class).find(null, "XY", null);
    Assert.assertEquals(getForm().getCountryUid(), formData.getCountryUid());
  }

  @Test
  public void testFindNationOnly() throws ProcessingException {
    createCountry();

    CountryFormData formData = BEANS.get(ICountryProcessService.class).find(null, null, "NAT");
    Assert.assertEquals(getForm().getCountryUid(), formData.getCountryUid());
  }

  @Test
  public void testFindNameOnly() throws ProcessingException {
    createCountry();

    CountryFormData formData = BEANS.get(ICountryProcessService.class).find("LAND1", null, null);
    Assert.assertEquals(getForm().getCountryUid(), formData.getCountryUid());
  }

  @Test
  public void testFindNoResult() throws ProcessingException {
    CountryFormData formData = BEANS.get(ICountryProcessService.class).find("LAND1", "XY", "NAT");
    Assert.assertNull(formData.getCountryUid());
    Assert.assertEquals("LAND1", formData.getCodeBox().getFieldByClass(Language.class).getRows()[0].getTranslation());
    Assert.assertEquals("XY", formData.getCountryCode().getValue());
    Assert.assertEquals("NAT", formData.getNation().getValue());
  }

  private void createCountry() throws ProcessingException {
    FormTestUtility.fillFormFields(getForm());
    getForm().getCountryCodeField().setValue("XY");
    getForm().getNationField().setValue("NAT");
    for (int k = 0; k < getForm().getMainBox().getFieldByClass(LanguageField.class).getTable().getRowCount(); k++) {
      getForm().getMainBox().getFieldByClass(LanguageField.class).getTable().getTranslationColumn().setValue(k, "LAND1");
    }
    getForm().doOk();
  }

}
