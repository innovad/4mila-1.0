package com.rtiming.client.entry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.scout.rt.client.ui.form.fields.IFormField;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.TestClientSession;
import com.rtiming.client.entry.EntryForm.MainBox.TabBox.FeesBox.FeesField;
import com.rtiming.client.entry.payment.PaymentsTablePage;
import com.rtiming.client.event.EventClassForm;
import com.rtiming.client.runner.RunnerForm.MainBox.ECardField;
import com.rtiming.client.settings.addinfo.AbstractAdditionalInformationField.Table;
import com.rtiming.client.settings.addinfo.AbstractAdditionalInformationField.Table.ValueColumn.IntegerField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.EntityField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.FeeGroupField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.MaximumField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.MinimumField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.TypeField;
import com.rtiming.client.settings.addinfo.EventAdditionalInformationForm;
import com.rtiming.client.settings.fee.FeeForm.MainBox.AgeBox.AgeFrom;
import com.rtiming.client.settings.fee.FeeForm.MainBox.AgeBox.AgeTo;
import com.rtiming.client.settings.fee.FeeForm.MainBox.CurrencyField;
import com.rtiming.client.settings.fee.FeeForm.MainBox.DateBox.DateFrom;
import com.rtiming.client.settings.fee.FeeForm.MainBox.DateBox.DateTo;
import com.rtiming.client.settings.fee.FeeForm.MainBox.FeeField;
import com.rtiming.client.settings.fee.FeeGroupForm.MainBox.CashPaymentOnRegistrationField;
import com.rtiming.client.test.FMilaClientTestUtility;
import com.rtiming.client.test.data.AdditionalInformationAdministrationTestDataProvider;
import com.rtiming.client.test.data.CurrencyTestDataProvider;
import com.rtiming.client.test.data.ECardTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualClassTestDataProvider;
import com.rtiming.client.test.data.FeeGroupTestDataProvider;
import com.rtiming.client.test.data.FeeTestDataProvider;
import com.rtiming.client.test.data.RunnerTestDataProvider;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.common.EntityCodeType;
import com.rtiming.shared.settings.addinfo.AdditionalInformationTypeCodeType;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class EntryFormCashPaymentTest {

  private AdditionalInformationAdministrationTestDataProvider addInfo;
  private ECardTestDataProvider ecard;
  private RunnerTestDataProvider runner;
  private EventWithIndividualClassTestDataProvider event;
  private FeeGroupTestDataProvider feeGroup;
  private FeeTestDataProvider fee;
  private CurrencyTestDataProvider currency;

  @Test
  public void testCashPayment() throws Exception {
    EntryForm form = createTestData(Boolean.TRUE);

    // check payment
    PaymentsTablePage page = new PaymentsTablePage(form.getRegistrationField().getValue());
    page.nodeAddedNotify();
    page.loadChildren();

    Assert.assertEquals("3 Rows (2 Payments + Total)", 3, page.getTable().getRowCount());
    Assert.assertEquals("Amount 1", page.getTable().getAmountColumn().getValue(0).doubleValue(), 5.55d, 0.1d);
    Assert.assertEquals("Amount 2", page.getTable().getAmountColumn().getValue(1).doubleValue(), 5.55d, 0.1d);
    Assert.assertEquals("Sum Amount", page.getTable().getAmountColumn().getValue(2).doubleValue(), 11.1d, 0.1d);

    form.doClose();
  }

  @Test
  public void testNoCashPayment() throws Exception {
    EntryForm form = createTestData(Boolean.FALSE);

    // check payment
    PaymentsTablePage page = new PaymentsTablePage(form.getRegistrationField().getValue());
    page.nodeAddedNotify();
    page.loadChildren();

    Assert.assertEquals("0 Rows (0 Payments + Total)", 0, page.getTable().getRowCount());

    form.doClose();
  }

  private EntryForm createTestData(Boolean cashPayment) throws ProcessingException {
    ecard = new ECardTestDataProvider();
    event = new EventWithIndividualClassTestDataProvider();

    List<FieldValue> list = new ArrayList<FieldValue>();
    list.add(new FieldValue(ECardField.class, ecard.getECardNr()));
    runner = new RunnerTestDataProvider(list);

    currency = new CurrencyTestDataProvider();

    list = new ArrayList<FieldValue>();
    list.add(new FieldValue(CashPaymentOnRegistrationField.class, cashPayment));
    feeGroup = new FeeGroupTestDataProvider(list);
    Assert.assertEquals("Cash Settings", cashPayment, feeGroup.getForm().getCashPaymentOnRegistrationField().getValue());

    list = new ArrayList<FieldValue>();
    list.add(new FieldValue(FeeField.class, 5.55d));
    list.add(new FieldValue(CurrencyField.class, currency.getCurrencyUid()));
    list.add(new FieldValue(DateFrom.class, DateUtility.truncDateToMinute(DateUtility.addDays(new Date(), -10))));
    list.add(new FieldValue(DateTo.class, DateUtility.truncDateToMinute(DateUtility.addDays(new Date(), +10))));
    list.add(new FieldValue(AgeFrom.class, 0L));
    list.add(new FieldValue(AgeTo.class, 99L));
    fee = new FeeTestDataProvider(feeGroup.getFeeGroupNr(), list);

    list = new ArrayList<FieldValue>();
    list.add(new FieldValue(TypeField.class, AdditionalInformationTypeCodeType.IntegerCode.ID));
    list.add(new FieldValue(EntityField.class, EntityCodeType.EntryCode.ID));
    list.add(new FieldValue(FeeGroupField.class, feeGroup.getFeeGroupNr()));
    list.add(new FieldValue(MinimumField.class, 0d));
    list.add(new FieldValue(MaximumField.class, 99.0d));
    addInfo = new AdditionalInformationAdministrationTestDataProvider(list);

    EventAdditionalInformationForm eventAddInfo = new EventAdditionalInformationForm();
    eventAddInfo.getEventField().setValue(event.getEventNr());
    eventAddInfo.startNew();
    eventAddInfo.getAdditionalInformationField().setValue(addInfo.getAdditionalInformationUid());
    eventAddInfo.doOk();

    EventClassForm eventClass = new EventClassForm();
    eventClass.getEventField().setValue(event.getEventNr());
    eventClass.getClazzField().setValue(event.getClassUid());
    eventClass.startModify();
    eventClass.getFeeGroupField().setValue(feeGroup.getFeeGroupNr());
    eventClass.doOk();

    EntryForm form = new EntryForm();
    form.startNew();
    FMilaClientTestUtility.doColumnEdit(form.getEventsField().getTable().getEventNrColumn(), 0, event.getEventNr());

    form.getCurrencyField().setValue(currency.getCurrencyUid());
    form.getRunnerField().setValue(runner.getRunnerNr());
    form.getClazzField().setValue(event.getClassUid());

    boolean found = false;
    Table addInfoTable = form.getAdditionalInformationEntryField().getTable();
    for (int k = 0; k < addInfoTable.getRowCount(); k++) {
      if (addInfoTable.getAdditionalInformationUidColumn().getValue(k).longValue() == addInfo.getAdditionalInformationUid()) {
        found = true;
        IFormField field = addInfoTable.getValueColumn().prepareEdit(addInfoTable.getRow(k));
        ((IntegerField) field).setValue(1L);
        addInfoTable.getValueColumn().completeEdit(addInfoTable.getRow(k), field);
      }
    }
    Assert.assertTrue("Add Info exists", found);

    Integer feeRow = null;
    FeesField.Table feesTable = form.getFeesField().getTable();
    for (int k = 0; k < feesTable.getRowCount(); k++) {
      feeRow = k;
      Assert.assertEquals("Fee", 5.55d, feesTable.getAmountColumn().getValue(k).doubleValue(), 0.1d);
      System.out.println(feesTable.getNameColumn().getValue(k));
      System.out.println(feesTable.getAmountColumn().getValue(k));
      Assert.assertEquals(cashPayment, feesTable.getCashPaymentOnRegistrationColumn().getValue(k));
    }
    Assert.assertNotNull("Fee Row must exist", feeRow);
    Assert.assertEquals("1 Class Fee, 1 Add Info Fee", 2, feesTable.getRowCount());

    form.doOk();
    return form;
  }

  @After
  public void after() throws ProcessingException {
    event.remove();
    addInfo.remove();
    fee.remove();
    feeGroup.remove();
    runner.remove();
    ecard.remove();
    currency.remove();
  }

}
