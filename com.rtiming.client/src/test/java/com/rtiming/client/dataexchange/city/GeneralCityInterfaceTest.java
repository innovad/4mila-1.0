package com.rtiming.client.dataexchange.city;

import java.net.URL;

import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.client.ui.wizard.DefaultWizardContainerForm.MainBox.WizardFinishButton;
import org.eclipse.scout.rt.client.ui.wizard.DefaultWizardContainerForm.MainBox.WizardNextStepButton;
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
import com.rtiming.client.dataexchange.DataExchangeFinalizationForm;
import com.rtiming.client.dataexchange.DataExchangePreviewForm;
import com.rtiming.client.dataexchange.DataExchangeStartForm;
import com.rtiming.client.dataexchange.DataExchangeWizard;
import com.rtiming.client.settings.SettingsOutline;
import com.rtiming.client.settings.city.AbstractCitySearchBox.RegionField;
import com.rtiming.client.settings.city.AbstractCitySearchBox.ZipField;
import com.rtiming.client.settings.city.CitiesSearchForm;
import com.rtiming.client.settings.city.CitiesTablePage;
import com.rtiming.client.settings.city.CitiesTablePage.Table.DeleteMenu;
import com.rtiming.client.test.ClientTestingUtility;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.dataexchange.ImportExportFormatCodeType;

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(ClientSession.class)
public class GeneralCityInterfaceTest {

  @Before
  public void before() throws ProcessingException {
  }

  @Test
  public void testImport() throws ProcessingException {

    // do first import
    doImport();

    // Check Data
    IPage root = ClientTestingUtility.gotoOutline(SettingsOutline.class);
    CitiesTablePage cities = ClientTestingUtility.gotoChildPage(root, CitiesTablePage.class);
    CitiesSearchForm search = (CitiesSearchForm) cities.getSearchFormInternal();
    search.doReset();
    search.getFieldByClass(RegionField.class).setValue("Rhône Alpes");
    search.resetSearchFilter();
    cities.loadChildren();
    Assert.assertEquals(15, cities.getTable().getRowCount());

    // do second import - data should be replaced, not added
    doImport();

    // Check Row
    search.doReset();
    search.getFieldByClass(RegionField.class).setValue("Rhône Alpes");
    search.getFieldByClass(ZipField.class).setValue("01330");
    search.resetSearchFilter();
    cities.loadChildren();
    Assert.assertEquals(1, cities.getTable().getRowCount());
    ITableRow row = cities.getTable().getRow(0);
    Assert.assertEquals("Zip", "01330", cities.getTable().getZipColumn().getValue(row));
    Assert.assertEquals("Region", "Rhône Alpes", cities.getTable().getRegionColumn().getValue(row));
    Assert.assertEquals("Area", "Ain (01)", cities.getTable().getAreaColumn().getDisplayText(row));
    Assert.assertEquals("City", "Ambérieux-en-Dombes", cities.getTable().getCityColumn().getValue(row));
    Assert.assertNotNull("Country", cities.getTable().getCountryColumn().getValue(row));

    // Check Data
    search = (CitiesSearchForm) cities.getSearchFormInternal();
    search.doReset();
    search.getFieldByClass(RegionField.class).setValue("Rhône Alpes");
    search.resetSearchFilter();
    cities.loadChildren();
    Assert.assertEquals(15, cities.getTable().getRowCount());

    // remove
    cities.getTable().selectAllRows();
    Assert.assertEquals(15, cities.getTable().getSelectedRowCount());
    cities.getTable().getMenuByClass(DeleteMenu.class).doAction();
  }

  private void doImport() throws ProcessingException {
    DataExchangeWizard wizard = new DataExchangeWizard();
    wizard.start();
    Assert.assertTrue(wizard.getWizardForm() instanceof DataExchangeStartForm);

    // Start Form
    DataExchangeStartForm start = (DataExchangeStartForm) wizard.getWizardForm();
    start.getFormatField().setValue(ImportExportFormatCodeType.GeneralPostalCode.ID);
    URL file = FMilaUtility.findFileLocation("resources//dataexchange//generalcity.txt", "");
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
