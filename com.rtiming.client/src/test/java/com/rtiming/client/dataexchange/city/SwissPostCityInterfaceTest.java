package com.rtiming.client.dataexchange.city;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URL;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.client.ui.wizard.DefaultWizardContainerForm.MainBox.WizardFinishButton;
import org.eclipse.scout.rt.client.ui.wizard.DefaultWizardContainerForm.MainBox.WizardNextStepButton;
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

import com.rtiming.client.dataexchange.DataExchangeFinalizationForm;
import com.rtiming.client.dataexchange.DataExchangePreviewForm;
import com.rtiming.client.dataexchange.DataExchangeStartForm;
import com.rtiming.client.dataexchange.DataExchangeWizard;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.dataexchange.ImportExportFormatCodeType;
import com.rtiming.shared.settings.city.CityFormData;
import com.rtiming.shared.settings.city.CountryFormData;
import com.rtiming.shared.settings.city.ICityProcessService;
import com.rtiming.shared.settings.city.ICountryProcessService;
import com.rtiming.shared.test.helper.ITestingJPAService;

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(TestEnvironmentClientSession.class)
public class SwissPostCityInterfaceTest {

  @Before
  public void before() throws ProcessingException {
  }

  @Test
  public void testImport() throws ProcessingException {

    // do first import
    doImport();

    // Check Data
    Long cityNr = BEANS.get(ITestingJPAService.class).getMaxCityNr();

    Assert.assertNotNull(cityNr);

    CityFormData city = new CityFormData();
    city.setCityNr(cityNr);
    city = BEANS.get(ICityProcessService.class).load(city);

    Assert.assertEquals("City (Trimmed)", "Kreuzlingen 3", city.getCity().getValue());
    Assert.assertEquals("Region", "TG", city.getRegion().getValue());

    CountryFormData country = new CountryFormData();
    country.setCountryUid(city.getCountry().getValue());
    country = BEANS.get(ICountryProcessService.class).load(country);
    assertEquals("Country Code", "CH", country.getCountryCode().getValue());

    // do second import - data should be replaced, not added
    Long count = BEANS.get(ITestingJPAService.class).getCityCount();

    doImport();

    Long count2 = BEANS.get(ITestingJPAService.class).getCityCount();

    // Check Data
    assertNotNull("Countries must exist", count);
    assertNotNull("Countries must exist", count2);
    assertEquals("Data should not duplicate", count, count2);
  }

  private void doImport() throws ProcessingException {
    DataExchangeWizard wizard = new DataExchangeWizard();
    wizard.start();
    Assert.assertTrue(wizard.getWizardForm() instanceof DataExchangeStartForm);

    // Start Form
    DataExchangeStartForm start = (DataExchangeStartForm) wizard.getWizardForm();
    start.getFormatField().setValue(ImportExportFormatCodeType.SwissPostPostalCode.ID);
    URL file = FMilaUtility.findFileLocation("resources//dataexchange//swisspost.txt", "");
    start.getFileField().setValue(FMilaUtility.createBinaryResource(file.getPath()));
    ((WizardNextStepButton) wizard.getContainerForm().getWizardNextStepButton()).doClick();

    // Preview Form
    Assert.assertTrue(wizard.getWizardForm() instanceof DataExchangePreviewForm);
    ((WizardNextStepButton) wizard.getContainerForm().getWizardNextStepButton()).doClick();

    // Preview Form
    Assert.assertTrue(wizard.getWizardForm() instanceof DataExchangeFinalizationForm);
    DataExchangeFinalizationForm finish = (DataExchangeFinalizationForm) wizard.getWizardForm();
    ((WizardFinishButton) wizard.getContainerForm().getWizardFinishButton()).doClick();

    Assert.assertFalse(wizard.isOpen());
    String result = finish.getInfoField().getValue();
    Assert.assertTrue(result.contains(Texts.get("Error") + ": 0"));
  }

  @After
  public void after() throws ProcessingException {

  }

}
