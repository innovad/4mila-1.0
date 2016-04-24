package com.rtiming.client.dataexchange;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.common.code.ICode;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.TestClientSession;
import com.rtiming.client.dataexchange.cache.DataExchangeFormUtility;
import com.rtiming.shared.dataexchange.AbstractImportExportCode;
import com.rtiming.shared.dataexchange.ImportExportFormatCodeType;

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(TestClientSession.class)
public class DataExchangeStartFormTest {

  @Before
  public void before() throws ProcessingException {
    DataExchangeFormUtility.cleanPreferences();
  }

  @Test
  public void testDefault() throws Exception {
    DataExchangeStartForm form = new DataExchangeStartForm();
    form.startNew();

    Assert.assertEquals("Format default", ImportExportFormatCodeType.IOFDataStandard203CourseDataCode.ID, form.getFormatField().getValue().longValue());

    ICode<?> code = BEANS.get(ImportExportFormatCodeType.class).getCode(ImportExportFormatCodeType.IOFDataStandard203CourseDataCode.ID);
    Assert.assertTrue("is AbstractImportExportCode", code instanceof AbstractImportExportCode);

    AbstractImportExportCode interfaceCode = (AbstractImportExportCode) code;
    Assert.assertEquals("Import default", interfaceCode.isImport(), form.getImportExportGroup().getValue());

    form.doClose();
  }

  @Test
  public void testDefaultValueAfterChange() throws Exception {
    DataExchangeStartForm form = new DataExchangeStartForm();
    form.startNew();

    Assert.assertEquals("Format default", ImportExportFormatCodeType.IOFDataStandard203CourseDataCode.ID, form.getFormatField().getValue().longValue());
    form.getFormatField().setValue(ImportExportFormatCodeType.SwissOrienteeringRunnerDatabaseCode.ID);

    ICode<?> code = BEANS.get(ImportExportFormatCodeType.class).getCode(ImportExportFormatCodeType.SwissOrienteeringRunnerDatabaseCode.ID);
    Assert.assertTrue("is AbstractImportExportCode", code instanceof AbstractImportExportCode);

    AbstractImportExportCode interfaceCode = (AbstractImportExportCode) code;
    Assert.assertEquals("Import default", interfaceCode.isImport(), form.getImportExportGroup().getValue());
    Assert.assertEquals("Header Row default", interfaceCode.getConfiguredIgnoreHeaderRow(), form.getIgnoreHeaderRowField().getValue());
    Assert.assertEquals("Text Enclosing default", interfaceCode.getConfiguredDefaultTextEnclosing(), form.getTextEnclosingField().getValue());
    Assert.assertEquals("Field Separator default", interfaceCode.getConfiguredDefaultFieldSeparator(), form.getFieldSeparatorField().getValue());

    form.doClose();
  }

  @Test
  public void testPersistence() throws Exception {
    DataExchangeStartForm form = new DataExchangeStartForm();
    form.startNew();
    String def = form.getCharacterSetField().getValue();

    Assert.assertFalse("Ok Button invisible", form.getOkButton().isVisible());
    Assert.assertFalse("Cancel Button invisible", form.getCancelButton().isVisible());

    form.getFormatField().setValue(ImportExportFormatCodeType.SwissOrienteeringRunnerDatabaseCode.ID);
    form.getCharacterSetField().setValue("UTF77");
    form.getFieldSeparatorField().setValue(';');
    form.getTextEnclosingField().setValue('x');
    form.getIgnoreHeaderRowField().setValue(false);
    form.getImportExportGroup().setValue(false);
    form.doClose();

    DataExchangeStartForm form2 = new DataExchangeStartForm();
    form2.startNew();
    Assert.assertEquals("Format persisted", ImportExportFormatCodeType.SwissOrienteeringRunnerDatabaseCode.ID, form2.getFormatField().getValue().longValue());
    Assert.assertEquals("Character Set persisted", "UTF77", form2.getCharacterSetField().getValue());
    Assert.assertEquals("Field Separator persisted", String.valueOf(';'), String.valueOf(form2.getFieldSeparatorField().getValue()));
    Assert.assertEquals("Text Enclosing persisted", String.valueOf('x'), String.valueOf(form2.getTextEnclosingField().getValue()));
    Assert.assertEquals("Header Row persisted", false, form2.getIgnoreHeaderRowField().getValue());
    Assert.assertEquals("Import/Export persisted", false, form2.getImportExportGroup().getValue());

    DataExchangeStartForm form3 = new DataExchangeStartForm();
    form2.getCharacterSetField().setValue(def);
    form.doClose();
  }
}
