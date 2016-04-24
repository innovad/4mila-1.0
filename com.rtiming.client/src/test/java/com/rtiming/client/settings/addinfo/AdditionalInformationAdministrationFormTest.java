package com.rtiming.client.settings.addinfo;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.form.fields.longfield.AbstractLongField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.testing.client.ScoutClientAssert;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.TestClientSession;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.EntityField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.MandatoryField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.MaximumField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.MinimumField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.TypeField;
import com.rtiming.client.test.AbstractFormTest;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.client.test.data.AdditionalInformationAdministrationTestDataProvider;
import com.rtiming.client.test.data.RunnerTestDataProvider;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.common.EntityCodeType;
import com.rtiming.shared.settings.addinfo.AdditionalInformationAdministrationFormData;
import com.rtiming.shared.settings.addinfo.AdditionalInformationTypeCodeType;
import com.rtiming.shared.settings.addinfo.IAdditionalInformationAdministrationProcessService;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class AdditionalInformationAdministrationFormTest extends AbstractFormTest<AdditionalInformationAdministrationForm> {

  private RunnerTestDataProvider runner;

  @Override
  protected AdditionalInformationAdministrationForm getStartedForm() throws ProcessingException {
    AdditionalInformationAdministrationForm form = new AdditionalInformationAdministrationForm();
    form.startNew();
    return form;
  }

  @Override
  protected AdditionalInformationAdministrationForm getModifyForm() throws ProcessingException {
    AdditionalInformationAdministrationForm form = new AdditionalInformationAdministrationForm();
    form.setAdditionalInformationUid(getForm().getAdditionalInformationUid());
    form.getSmartfieldField().setValue(null);
    form.startModify();
    return form;
  }

  @Override
  public void cleanup() throws ProcessingException {
    if (runner != null) {
      runner.remove();
    }
    AdditionalInformationAdministrationFormData formData = new AdditionalInformationAdministrationFormData();
    formData.setAdditionalInformationUid(getForm().getAdditionalInformationUid());
    BEANS.get(IAdditionalInformationAdministrationProcessService.class).delete(formData);
  }

  @Test
  public void testTypeSmartfieldValidation() throws ProcessingException {
    AdditionalInformationAdministrationForm form = new AdditionalInformationAdministrationForm();
    form.startNew();
    form.getTypeField().setValue(AdditionalInformationTypeCodeType.SmartfieldCode.ID);
    Assert.assertFalse(form.getMaximumField().isEnabled());
    Assert.assertFalse(form.getMinimumField().isEnabled());
    Assert.assertFalse(form.getDefaultValueBooleanField().isVisible());
    Assert.assertFalse(form.getDefaultValueDecimalField().isVisible());
    Assert.assertFalse(form.getDefaultValueIntegerField().isVisible());
    Assert.assertFalse(form.getDefaultValueTextField().isVisible());
    Assert.assertTrue(form.getDefaultValueSmartfieldField().isVisible());
    ScoutClientAssert.assertDisabled(form.getFeeGroupField());
    form.getTypeField().setValue(AdditionalInformationTypeCodeType.IntegerCode.ID);
    Assert.assertTrue(form.getMaximumField().isEnabled());
    Assert.assertTrue(form.getMinimumField().isEnabled());
    Assert.assertFalse(form.getDefaultValueBooleanField().isVisible());
    Assert.assertFalse(form.getDefaultValueDecimalField().isVisible());
    Assert.assertTrue(form.getDefaultValueIntegerField().isVisible());
    Assert.assertFalse(form.getDefaultValueTextField().isVisible());
    Assert.assertFalse(form.getDefaultValueSmartfieldField().isVisible());
    ScoutClientAssert.assertEnabled(form.getFeeGroupField());
    ScoutClientAssert.assertVisible(form.getFeeGroupField());
    form.getTypeField().setValue(AdditionalInformationTypeCodeType.BooleanCode.ID);
    Assert.assertFalse(form.getMaximumField().isEnabled());
    Assert.assertFalse(form.getMinimumField().isEnabled());
    Assert.assertTrue(form.getDefaultValueBooleanField().isVisible());
    Assert.assertFalse(form.getDefaultValueDecimalField().isVisible());
    Assert.assertFalse(form.getDefaultValueIntegerField().isVisible());
    Assert.assertFalse(form.getDefaultValueTextField().isVisible());
    Assert.assertFalse(form.getDefaultValueSmartfieldField().isVisible());
    ScoutClientAssert.assertEnabled(form.getFeeGroupField());
    form.getTypeField().setValue(AdditionalInformationTypeCodeType.DoubleCode.ID);
    Assert.assertTrue(form.getMaximumField().isEnabled());
    Assert.assertTrue(form.getMinimumField().isEnabled());
    Assert.assertFalse(form.getDefaultValueBooleanField().isVisible());
    Assert.assertTrue(form.getDefaultValueDecimalField().isVisible());
    Assert.assertFalse(form.getDefaultValueIntegerField().isVisible());
    Assert.assertFalse(form.getDefaultValueTextField().isVisible());
    Assert.assertFalse(form.getDefaultValueSmartfieldField().isVisible());
    ScoutClientAssert.assertEnabled(form.getFeeGroupField());
    form.getTypeField().setValue(AdditionalInformationTypeCodeType.TextCode.ID);
    Assert.assertTrue(form.getMaximumField().isEnabled());
    Assert.assertTrue(form.getMinimumField().isEnabled());
    Assert.assertFalse(form.getDefaultValueBooleanField().isVisible());
    Assert.assertFalse(form.getDefaultValueDecimalField().isVisible());
    Assert.assertFalse(form.getDefaultValueIntegerField().isVisible());
    Assert.assertTrue(form.getDefaultValueTextField().isVisible());
    Assert.assertFalse(form.getDefaultValueSmartfieldField().isVisible());
    ScoutClientAssert.assertEnabled(form.getFeeGroupField());
    form.doClose();
  }

  @Test
  public void testSmartfieldChild() throws ProcessingException {
    // create additional info
    List<FieldValue> fieldValue = new ArrayList<FieldValue>();
    fieldValue.add(new FieldValue(EntityField.class, EntityCodeType.ClubCode.ID));
    fieldValue.add(new FieldValue(TypeField.class, AdditionalInformationTypeCodeType.SmartfieldCode.ID));
    fieldValue.add(new FieldValue(MandatoryField.class, true));
    fieldValue.add(new FieldValue(MinimumField.class, null));
    fieldValue.add(new FieldValue(MaximumField.class, null));
    AdditionalInformationAdministrationTestDataProvider admin = new AdditionalInformationAdministrationTestDataProvider(fieldValue);

    AdditionalInformationAdministrationForm form = new AdditionalInformationAdministrationForm();
    form.getSmartfieldField().setValue(admin.getAdditionalInformationUid());
    form.startNew();

    Assert.assertFalse(form.getMinimumField().isVisible());
    Assert.assertFalse(form.getMaximumField().isVisible());
    Assert.assertFalse(form.getMandatoryField().isVisible());
    Assert.assertFalse(form.getDefaultValueBooleanField().isVisible());
    Assert.assertFalse(form.getDefaultValueDecimalField().isVisible());
    Assert.assertFalse(form.getDefaultValueIntegerField().isVisible());
    Assert.assertFalse(form.getDefaultValueTextField().isVisible());
    Assert.assertFalse(form.getDefaultValueSmartfieldField().isVisible());
    Assert.assertFalse(form.getSmartfieldField().isEnabled());
    ScoutClientAssert.assertVisible(form.getFeeGroupField());
    ScoutClientAssert.assertEnabled(form.getFeeGroupField());
    Assert.assertEquals(AdditionalInformationTypeCodeType.SmartfieldCode.ID, form.getTypeField().getValue().longValue());
    Assert.assertEquals(admin.getAdditionalInformationUid().longValue(), form.getSmartfieldField().getValue().longValue());

    form.doClose();
    admin.remove();
  }

  @Test(expected = VetoException.class)
  public void testMandatoryVetoException() throws ProcessingException {
    runner = new RunnerTestDataProvider();

    AdditionalInformationAdministrationForm form = getForm();
    FormTestUtility.fillFormFields(form);
    form.getTypeField().setValue(AdditionalInformationTypeCodeType.IntegerCode.ID);
    form.getEntityField().setValue(EntityCodeType.RunnerCode.ID);
    form.getMandatoryField().setValue(true);
    form.getMinimumField().setValue(0d);
    form.getMaximumField().setValue(5d);
    form.doOk();

    runner.getForm().startModify();
    runner.getForm().doOk();
  }

  @Test
  public void testMandatoryOk() throws ProcessingException {
    runner = new RunnerTestDataProvider();

    AdditionalInformationAdministrationForm form = getForm();
    FormTestUtility.fillFormFields(form);
    form.getTypeField().setValue(AdditionalInformationTypeCodeType.IntegerCode.ID);
    form.getEntityField().setValue(EntityCodeType.RunnerCode.ID);
    form.getMinimumField().setValue(0d);
    form.getMaximumField().setValue(5d);
    form.getMandatoryField().setValue(true);
    form.doOk();

    runner.getForm().startModify();

    ITableRow row = findAddtionalInformationRow(form.getAdditionalInformationUid().longValue());
    Assert.assertNotNull("Add Info must exist", row);
    AbstractLongField field = (AbstractLongField) runner.getForm().getAdditionalInformationField().getTable().getValueColumn().prepareEdit(row);
    field.setValue(4L);
    runner.getForm().getAdditionalInformationField().getTable().getValueColumn().completeEdit(row, field);

    runner.getForm().doOk();
  }

  private ITableRow findAddtionalInformationRow(long longValue) throws ProcessingException {
    ITableRow row = null;
    for (int k = 0; k < runner.getForm().getAdditionalInformationField().getTable().getRowCount(); k++) {
      if (CompareUtility.equals(runner.getForm().getAdditionalInformationField().getTable().getAdditionalInformationUidColumn().getValue(k), longValue)) {
        return runner.getForm().getAdditionalInformationField().getTable().getRow(k);
      }
    }
    return row;
  }

  @Test
  public void testMinimum() throws ProcessingException {
    runner = new RunnerTestDataProvider();

    AdditionalInformationAdministrationForm form = getForm();
    FormTestUtility.fillFormFields(form);
    form.getTypeField().setValue(AdditionalInformationTypeCodeType.IntegerCode.ID);
    form.getEntityField().setValue(EntityCodeType.RunnerCode.ID);
    form.getMinimumField().setValue(0d);
    form.getMaximumField().setValue(5d);
    form.getMandatoryField().setValue(false);
    form.doOk();

    runner.getForm().startModify();

    ITableRow row = findAddtionalInformationRow(form.getAdditionalInformationUid().longValue());
    Assert.assertNotNull("Add Info must exist", row);
    AbstractLongField field = (AbstractLongField) runner.getForm().getAdditionalInformationField().getTable().getValueColumn().prepareEdit(row);
    field.setValue(-1L);
    Assert.assertNull(field.getValue());
    field.setValue(0L);
    Assert.assertEquals(0L, field.getValue().longValue());
    runner.getForm().getAdditionalInformationField().getTable().getValueColumn().completeEdit(row, field);

    runner.getForm().doOk();
  }

  @Test
  public void testMaximum() throws ProcessingException {
    runner = new RunnerTestDataProvider();

    AdditionalInformationAdministrationForm form = getForm();
    FormTestUtility.fillFormFields(form);
    form.getTypeField().setValue(AdditionalInformationTypeCodeType.IntegerCode.ID);
    form.getEntityField().setValue(EntityCodeType.RunnerCode.ID);
    form.getMinimumField().setValue(0d);
    form.getMaximumField().setValue(5d);
    form.getMandatoryField().setValue(false);
    form.doOk();

    runner.getForm().startModify();

    ITableRow row = findAddtionalInformationRow(form.getAdditionalInformationUid().longValue());
    Assert.assertNotNull("Add Info must exist", row);
    AbstractLongField field = (AbstractLongField) runner.getForm().getAdditionalInformationField().getTable().getValueColumn().prepareEdit(row);
    field.setValue(6L);
    Assert.assertNull(field.getValue());
    field.setValue(5L);
    Assert.assertEquals(5L, field.getValue().longValue());
    runner.getForm().getAdditionalInformationField().getTable().getValueColumn().completeEdit(row, field);

    runner.getForm().doOk();
  }

}
