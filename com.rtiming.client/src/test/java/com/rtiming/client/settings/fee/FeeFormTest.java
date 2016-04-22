package com.rtiming.client.settings.fee;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.form.fields.IFormField;
import org.eclipse.scout.rt.client.ui.form.fields.longfield.AbstractLongField;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.AbstractDoubleField;
import com.rtiming.client.entry.EntryForm;
import com.rtiming.client.event.EventClassForm;
import com.rtiming.client.runner.RunnerForm;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.DefaultValueBooleanField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.DefaultValueDecimalField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.DefaultValueIntegerField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.DefaultValueTextField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.EntityField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.FeeGroupField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.MandatoryField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.MaximumField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.MinimumField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.TypeField;
import com.rtiming.client.settings.addinfo.EventAdditionalInformationForm;
import com.rtiming.client.settings.fee.FeeForm.MainBox.CurrencyField;
import com.rtiming.client.test.AbstractFormTest;
import com.rtiming.client.test.FMilaClientTestUtility;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.client.test.FormTestUtility.OrderedFieldPair;
import com.rtiming.client.test.data.AdditionalInformationAdministrationTestDataProvider;
import com.rtiming.client.test.data.CurrencyTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualClassTestDataProvider;
import com.rtiming.client.test.data.RunnerTestDataProvider;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.common.EntityCodeType;
import com.rtiming.shared.settings.addinfo.AdditionalInformationTypeCodeType;
import com.rtiming.shared.settings.fee.FeeGroupFormData;
import com.rtiming.shared.settings.fee.IFeeGroupProcessService;

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(TestEnvironmentClientSession.class)
public class FeeFormTest extends AbstractFormTest<FeeForm> {

  private FeeGroupForm feeGroup;
  private EventWithIndividualClassTestDataProvider event;
  private CurrencyTestDataProvider currency;
  private RunnerTestDataProvider runner;
  private AdditionalInformationAdministrationTestDataProvider addInfo;

  @Override
  public void setUpForm() throws ProcessingException {
    currency = new CurrencyTestDataProvider();
    feeGroup = new FeeGroupForm();
    feeGroup.startNew();
    FormTestUtility.fillFormFields(feeGroup);
    feeGroup.doOk();
    super.setUpForm();
  }

  @Override
  protected ArrayList<OrderedFieldPair> getOrderedFieldPairs() {
    ArrayList<OrderedFieldPair> result = new ArrayList<OrderedFieldPair>();
    OrderedFieldPair pair = new OrderedFieldPair(FeeForm.MainBox.DateBox.DateFrom.class, FeeForm.MainBox.DateBox.DateTo.class);
    result.add(pair);
    OrderedFieldPair age = new OrderedFieldPair(FeeForm.MainBox.AgeBox.AgeFrom.class, FeeForm.MainBox.AgeBox.AgeTo.class);
    result.add(age);
    return result;
  }

  @Override
  protected List<FieldValue> getFixedValues() throws ProcessingException {
    List<FieldValue> list = new ArrayList<FieldValue>();
    list.add(new FieldValue(CurrencyField.class, currency.getCurrencyUid()));
    return list;
  }

  @Override
  protected FeeForm getStartedForm() throws ProcessingException {
    FeeForm form = new FeeForm();
    form.setFeeGroupNr(feeGroup.getFeeGroupNr());
    form.startNew();
    return form;
  }

  @Override
  protected FeeForm getModifyForm() throws ProcessingException {
    FeeForm form = new FeeForm();
    form.setFeeNr(getForm().getFeeNr());
    form.startModify();
    return form;
  }

  @Override
  public void cleanup() throws ProcessingException {
    if (event != null) {
      event.remove();
    }
    if (currency != null) {
      currency.remove();
    }
    if (runner != null) {
      runner.remove();
    }
    if (addInfo != null) {
      addInfo.remove();
    }

    FeeGroupFormData formData = new FeeGroupFormData();
    formData.setFeeGroupNr(feeGroup.getFeeGroupNr());
    BEANS.get(IFeeGroupProcessService.class).delete(formData);
  }

  @Test
  public void testAgeFixedFee() throws Exception {
    event = new EventWithIndividualClassTestDataProvider();
    currency = new CurrencyTestDataProvider();
    runner = new RunnerTestDataProvider();

    // set fee values
    createEventClassFee(null, null, null, null, 123.456);

    EntryForm entry = createEntry();

    Assert.assertEquals(123.456, entry.getFeesField().getTable().getAmountColumn().getValue(0), 0.0001d);

    entry.doClose();
  }

  @Test
  public void testAgeFixedFeeNoYearMatch() throws Exception {
    event = new EventWithIndividualClassTestDataProvider();
    currency = new CurrencyTestDataProvider();
    runner = new RunnerTestDataProvider();

    // set fee values
    createEventClassFee(null, null, null, null, 123.456);

    EntryForm entry = createEntry();
    entry.getYearField().setValue(null);

    Assert.assertEquals(123.456, entry.getFeesField().getTable().getAmountColumn().getValue(0), 0.0001d);

    entry.doClose();
  }

  @Test
  public void testAgeFee() throws Exception {
    event = new EventWithIndividualClassTestDataProvider();
    currency = new CurrencyTestDataProvider();
    List<FieldValue> fieldValue = new ArrayList<FieldValue>();
    fieldValue.add(new FieldValue(RunnerForm.MainBox.BirthdateField.class, getBirthdateForAgeOnEventZero(20)));
    runner = new RunnerTestDataProvider(fieldValue);

    // set fee values
    createEventClassFee(null, null, 20L, 30L, 99.88);

    EntryForm entry = createEntry();

    // age fee matches
    Assert.assertEquals(99.88, entry.getFeesField().getTable().getAmountColumn().getValue(0), 0.0001d);

    // age fee does not match - lower border
    entry.getYearField().setValue(getYearForAgeOnEventZero(19));
    Assert.assertNull(entry.getFeesField().getTable().getAmountColumn().getValue(0));

    // age fee does match - upper border
    entry.getYearField().setValue(getYearForAgeOnEventZero(30));
    Assert.assertEquals(99.88, entry.getFeesField().getTable().getAmountColumn().getValue(0), 0.0001d);

    // age fee does not match - upper border
    entry.getYearField().setValue(getYearForAgeOnEventZero(31));
    Assert.assertNull(entry.getFeesField().getTable().getAmountColumn().getValue(0));

    // age fee does match - between
    entry.getYearField().setValue(getYearForAgeOnEventZero(25));
    Assert.assertEquals(99.88, entry.getFeesField().getTable().getAmountColumn().getValue(0), 0.0001d);

    // age fee does not match - null
    entry.getYearField().setValue(null);
    Assert.assertNull(entry.getFeesField().getTable().getAmountColumn().getValue(0));

    entry.doClose();
  }

  @Test
  public void testDateFee() throws Exception {
    event = new EventWithIndividualClassTestDataProvider();
    currency = new CurrencyTestDataProvider();
    runner = new RunnerTestDataProvider();

    createEventClassFee(getDateRelativeToEventZero(-10), getDateRelativeToEventZero(-1), null, null, 88.88);

    EntryForm entry = createEntry();

    // entry date = from date => match
    entry.getEvtEntryField().setValue(getDateRelativeToEventZero(-10));
    Assert.assertEquals(88.88, entry.getFeesField().getTable().getAmountColumn().getValue(0), 0.0001d);

    // entry date = before from date => NO match
    entry.getEvtEntryField().setValue(getDateRelativeToEventZero(-11));
    Assert.assertNull(entry.getFeesField().getTable().getAmountColumn().getValue(0));
    Assert.assertEquals(0, entry.getFeesField().getTable().getRowCount());

    // entry date = to date => match
    entry.getEvtEntryField().setValue(getDateRelativeToEventZero(-1));
    Assert.assertEquals(88.88, entry.getFeesField().getTable().getAmountColumn().getValue(0), 0.0001d);

    // entry date = after to date => NO match
    entry.getEvtEntryField().setValue(getDateRelativeToEventZero(0));
    Assert.assertNull(entry.getFeesField().getTable().getAmountColumn().getValue(0));
    Assert.assertEquals(0, entry.getFeesField().getTable().getRowCount());

    // entry date = between dates => match
    entry.getEvtEntryField().setValue(getDateRelativeToEventZero(-5));
    Assert.assertEquals(88.88, entry.getFeesField().getTable().getAmountColumn().getValue(0), 0.0001d);

    entry.doClose();
  }

  @Test
  public void testAdditionalInformationFeeInteger() throws ProcessingException {
    event = new EventWithIndividualClassTestDataProvider();
    currency = new CurrencyTestDataProvider();
    runner = new RunnerTestDataProvider();

    createFee(null, null, null, null, 77.77);

    createAdditionalInformation(AdditionalInformationTypeCodeType.IntegerCode.ID, 2L);

    // create entry
    EntryForm entry = createEntry();

    // match: 2 * addInfo
    Assert.assertEquals(2 * 77.77, entry.getFeesField().getTable().getAmountColumn().getValue(0), 0.01);

    // no match: 0 * addInfo
    ITableRow row = entry.getAdditionalInformationEntryField().getTable().getRow(0);
    IFormField edit = entry.getAdditionalInformationEntryField().getTable().getValueColumn().prepareEdit(row);
    ((AbstractLongField) edit).setValue(0L);
    entry.getAdditionalInformationEntryField().getTable().getValueColumn().completeEdit(row, edit);
    Assert.assertEquals(0, entry.getFeesField().getTable().getRowCount());
    Assert.assertTrue(entry.getFeesBox().getLabel().contains("0"));

    // match: 4 * addInfo
    Assert.assertEquals(1, entry.getAdditionalInformationEntryField().getTable().getRowCount());
    Assert.assertEquals(addInfo.getAdditionalInformationUid(), entry.getAdditionalInformationEntryField().getTable().getAdditionalInformationUidColumn().getValue(0));
    row = entry.getAdditionalInformationEntryField().getTable().getRow(0);
    edit = entry.getAdditionalInformationEntryField().getTable().getValueColumn().prepareEdit(row);
    ((AbstractLongField) edit).setValue(4L);
    entry.getAdditionalInformationEntryField().getTable().getValueColumn().completeEdit(row, edit);
    Assert.assertEquals(4 * 77.77, entry.getFeesField().getTable().getAmountColumn().getValue(0), 0.01);

    entry.doClose();
  }

  @Test
  public void testAdditionalInformationFeeDouble() throws ProcessingException {
    event = new EventWithIndividualClassTestDataProvider();
    currency = new CurrencyTestDataProvider();
    runner = new RunnerTestDataProvider();

    createFee(null, null, null, null, 66.66);

    createAdditionalInformation(AdditionalInformationTypeCodeType.DoubleCode.ID, 3.33d);

    // create entry
    EntryForm entry = createEntry();

    // match: 2 * addInfo
    Assert.assertEquals(3.33 * 66.66, entry.getFeesField().getTable().getAmountColumn().getValue(0), 0.01);

    // match: 0 * addInfo
    ITableRow row = entry.getAdditionalInformationEntryField().getTable().getRow(0);
    IFormField edit = entry.getAdditionalInformationEntryField().getTable().getValueColumn().prepareEdit(row);
    ((AbstractDoubleField) edit).setValue(0d);
    entry.getAdditionalInformationEntryField().getTable().getValueColumn().completeEdit(row, edit);
    Assert.assertEquals(0, entry.getFeesField().getTable().getRowCount());
    Assert.assertTrue(entry.getFeesBox().getLabel().contains("0"));

    entry.doClose();
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testAdditionalInformationFeeBoolean() throws ProcessingException {
    event = new EventWithIndividualClassTestDataProvider();
    currency = new CurrencyTestDataProvider();
    runner = new RunnerTestDataProvider();

    createFee(null, null, null, null, 19.77);

    createAdditionalInformation(AdditionalInformationTypeCodeType.BooleanCode.ID, Boolean.TRUE);

    // create entry
    EntryForm entry = createEntry();

    // match: true
    Assert.assertEquals(19.77, entry.getFeesField().getTable().getAmountColumn().getValue(0), 0.01);

    // no match: false
    ITableRow row = entry.getAdditionalInformationEntryField().getTable().getRow(0);
    AbstractSmartField<?> edit = (AbstractSmartField<?>) entry.getAdditionalInformationEntryField().getTable().getValueColumn().prepareEdit(row);
    ((AbstractSmartField<Boolean>) edit).setValue(false);
    entry.getAdditionalInformationEntryField().getTable().getValueColumn().completeEdit(row, edit);
    Assert.assertEquals(0, entry.getFeesField().getTable().getRowCount());
    Assert.assertTrue(entry.getFeesBox().getLabel().contains("0"));

    entry.doClose();
  }

  @Test
  public void testAdditionalInformationFeeText() throws ProcessingException {
    event = new EventWithIndividualClassTestDataProvider();
    currency = new CurrencyTestDataProvider();
    runner = new RunnerTestDataProvider();

    createFee(null, null, null, null, 19.75);

    createAdditionalInformation(AdditionalInformationTypeCodeType.TextCode.ID, "Hallo");

    // create entry
    EntryForm entry = createEntry();

    // match: text is set
    Assert.assertEquals(19.75, entry.getFeesField().getTable().getAmountColumn().getValue(0), 0.01);

    // no match: empty text
    ITableRow row = entry.getAdditionalInformationEntryField().getTable().getRow(0);
    IFormField edit = entry.getAdditionalInformationEntryField().getTable().getValueColumn().prepareEdit(row);
    ((AbstractStringField) edit).setValue("");
    entry.getAdditionalInformationEntryField().getTable().getValueColumn().completeEdit(row, edit);
    Assert.assertEquals(0, entry.getFeesField().getTable().getRowCount());
    Assert.assertTrue(entry.getFeesBox().getLabel().contains("0"));

    // match: text is set
    row = entry.getAdditionalInformationEntryField().getTable().getRow(0);
    edit = entry.getAdditionalInformationEntryField().getTable().getValueColumn().prepareEdit(row);
    ((AbstractStringField) edit).setValue("x");
    entry.getAdditionalInformationEntryField().getTable().getValueColumn().completeEdit(row, edit);
    Assert.assertEquals(1, entry.getFeesField().getTable().getRowCount());
    Assert.assertTrue(entry.getFeesBox().getLabel().contains("19"));
    Assert.assertTrue(entry.getFeesBox().getLabel().contains("75"));

    entry.doClose();
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testAdditionalInformationFeeSmartfield() throws ProcessingException {
    event = new EventWithIndividualClassTestDataProvider();
    currency = new CurrencyTestDataProvider();
    runner = new RunnerTestDataProvider();

    createFee(null, null, null, null, 12.34);

    createAdditionalInformation(AdditionalInformationTypeCodeType.SmartfieldCode.ID, null);

    AdditionalInformationAdministrationForm child = new AdditionalInformationAdministrationForm();
    child.getSmartfieldField().setValue(addInfo.getAdditionalInformationUid());
    child.startNew();
    FormTestUtility.fillFormFields(child);
    child.getFeeGroupField().setValue(feeGroup.getFeeGroupNr());
    child.doOk();

    Assert.assertEquals(addInfo.getAdditionalInformationUid(), child.getSmartfieldField().getValue());

    // create entry
    EntryForm entry = createEntry();

    Assert.assertEquals(0, entry.getFeesField().getTable().getRowCount());
    Assert.assertTrue(entry.getFeesBox().getLabel().contains("0"));

    // match child
    ITableRow row = entry.getAdditionalInformationEntryField().getTable().getRow(0);
    IFormField edit = entry.getAdditionalInformationEntryField().getTable().getValueColumn().prepareEdit(row);
    ((AbstractSmartField<Long>) edit).setValue(child.getAdditionalInformationUid());
    entry.getAdditionalInformationEntryField().getTable().getValueColumn().completeEdit(row, edit);
    Assert.assertEquals(12.34, entry.getFeesField().getTable().getAmountColumn().getValue(0), 0.01);

    entry.doClose();
  }

  @Test
  public void testAdditionalInformationFeeDoubleDate() throws ProcessingException {
    event = new EventWithIndividualClassTestDataProvider();
    currency = new CurrencyTestDataProvider();
    runner = new RunnerTestDataProvider();

    createFee(getDateRelativeToEventZero(-15), getDateRelativeToEventZero(-5), null, null, 66.66);

    createAdditionalInformation(AdditionalInformationTypeCodeType.DoubleCode.ID, 3.33d);

    // create entry
    EntryForm entry = createEntry();

    // match
    entry.getEvtEntryField().setValue(getDateRelativeToEventZero(-5));
    Assert.assertEquals(3.33 * 66.66, entry.getFeesField().getTable().getAmountColumn().getValue(0), 0.01);

    // no match
    entry.getEvtEntryField().setValue(getDateRelativeToEventZero(-16));
    Assert.assertNull(entry.getFeesField().getTable().getAmountColumn().getValue(0));
    Assert.assertEquals(0, entry.getFeesField().getTable().getRowCount());

    // match
    entry.getEvtEntryField().setValue(getDateRelativeToEventZero(-15));
    Assert.assertEquals(3.33 * 66.66, entry.getFeesField().getTable().getAmountColumn().getValue(0), 0.01);

    entry.doClose();
  }

  @Test
  public void testEntryDate() throws ProcessingException {
    event = new EventWithIndividualClassTestDataProvider();
    currency = new CurrencyTestDataProvider();
    runner = new RunnerTestDataProvider();

    EntryForm entry = createEntry();
    Assert.assertNotNull(entry.getEvtEntryField().getValue());

    try {
      entry.getEvtEntryField().setValueChangeTriggerEnabled(false);
      entry.getEvtEntryField().setValue(null);
    }
    finally {
      entry.getEvtEntryField().setValueChangeTriggerEnabled(true);
    }
    entry.getYearField().setValue(entry.getYearField().getValue() - 1);

    Assert.assertNotNull(entry.getEvtEntryField().getValue());
  }

  private EntryForm createEntry() throws ProcessingException {
    EntryForm entry = new EntryForm();
    entry.startNew();
    FMilaClientTestUtility.doColumnEdit(entry.getEventsField().getTable().getEventNrColumn(), 0, event.getEventNr());

    entry.getRunnerField().setValue(runner.getRunnerNr());
    entry.getClazzField().setValue(event.getClassUid());
    entry.getCurrencyField().setValue(currency.getCurrencyUid());
    return entry;
  }

  private void createAdditionalInformation(long addInfoUid, Object value) throws ProcessingException {
    List<FieldValue> fieldValue = new ArrayList<FieldValue>();
    fieldValue.add(new FieldValue(EntityField.class, EntityCodeType.EntryCode.ID));
    fieldValue.add(new FieldValue(TypeField.class, addInfoUid));
    // mandatory
    if (CompareUtility.notEquals(AdditionalInformationTypeCodeType.TextCode.ID, addInfoUid)) {
      fieldValue.add(new FieldValue(MandatoryField.class, true));
    }
    else {
      fieldValue.add(new FieldValue(MandatoryField.class, false));
    }
    if (CompareUtility.notEquals(AdditionalInformationTypeCodeType.BooleanCode.ID, addInfoUid) && CompareUtility.notEquals(AdditionalInformationTypeCodeType.SmartfieldCode.ID, addInfoUid)) {
      fieldValue.add(new FieldValue(MinimumField.class, 0d));
      fieldValue.add(new FieldValue(MaximumField.class, 50d));
    }
    // default value
    if (CompareUtility.equals(AdditionalInformationTypeCodeType.IntegerCode.ID, addInfoUid)) {
      fieldValue.add(new FieldValue(DefaultValueIntegerField.class, value));
    }
    else if (CompareUtility.equals(AdditionalInformationTypeCodeType.DoubleCode.ID, addInfoUid)) {
      fieldValue.add(new FieldValue(DefaultValueDecimalField.class, value));
    }
    else if (CompareUtility.equals(AdditionalInformationTypeCodeType.BooleanCode.ID, addInfoUid)) {
      fieldValue.add(new FieldValue(DefaultValueBooleanField.class, value));
    }
    else if (CompareUtility.equals(AdditionalInformationTypeCodeType.TextCode.ID, addInfoUid)) {
      fieldValue.add(new FieldValue(DefaultValueTextField.class, value));
    }
    if (CompareUtility.notEquals(AdditionalInformationTypeCodeType.SmartfieldCode.ID, addInfoUid)) {
      fieldValue.add(new FieldValue(FeeGroupField.class, feeGroup.getFeeGroupNr()));
    }

    addInfo = new AdditionalInformationAdministrationTestDataProvider(fieldValue);

    EventAdditionalInformationForm ea = new EventAdditionalInformationForm();
    ea.getEventField().setValue(event.getEventNr());
    ea.startNew();
    ea.getAdditionalInformationField().setValue(addInfo.getAdditionalInformationUid());
    ea.doOk();
  }

  private Date getDateRelativeToEventZero(int days) throws ProcessingException {
    return DateUtility.truncDate(DateUtility.addDays(event.getForm().getZeroTimeField().getValue(), days));
  }

  private Date getBirthdateForAgeOnEventZero(int ageOnEventDay) throws ProcessingException {
    return DateUtility.truncDate(DateUtility.addYears(event.getForm().getZeroTimeField().getValue(), ageOnEventDay * -1));
  }

  private Long getYearForAgeOnEventZero(int ageOnEventDay) throws ProcessingException {
    Date truncDate = DateUtility.truncDate(DateUtility.addYears(event.getForm().getZeroTimeField().getValue(), ageOnEventDay * -1));
    GregorianCalendar greg = new GregorianCalendar();
    greg.setTime(truncDate);
    return Long.valueOf(greg.get(Calendar.YEAR));
  }

  private void createEventClassFee(Date dateFrom, Date dateTo, Long ageFrom, Long ageTo, Double fee) throws ProcessingException {
    createFee(dateFrom, dateTo, ageFrom, ageTo, fee);

    // add fee group to event
    EventClassForm eventClass = new EventClassForm();
    eventClass.getEventField().setValue(event.getEventNr());
    eventClass.getClazzField().setValue(event.getClassUid());
    eventClass.startModify();
    eventClass.getFeeGroupField().setValue(feeGroup.getFeeGroupNr());
    eventClass.doOk();
  }

  private void createFee(Date dateFrom, Date dateTo, Long ageFrom, Long ageTo, Double fee) throws ProcessingException {
    FeeForm form = getStartedForm();
    form.getDateFrom().setValue(dateFrom);
    form.getDateTo().setValue(dateTo);
    form.getAgeFrom().setValue(ageFrom);
    form.getAgeTo().setValue(ageTo);
    form.getFeeField().setValue(fee);
    form.getCurrencyField().setValue(currency.getCurrencyUid());
    form.doOk();
  }

}
