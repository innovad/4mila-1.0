package com.rtiming.client.entry;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.common.code.CODES;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.TestClientSession;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.EntityField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.MandatoryField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.MaximumField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.MinimumField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.TypeField;
import com.rtiming.client.settings.addinfo.EventAdditionalInformationForm;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.client.test.data.AdditionalInformationAdministrationTestDataProvider;
import com.rtiming.client.test.data.CurrencyTestDataProvider;
import com.rtiming.client.test.data.EntryTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualClassTestDataProvider;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.common.EntityCodeType;
import com.rtiming.shared.entry.SharedCache;
import com.rtiming.shared.settings.addinfo.AdditionalInformationCodeType;
import com.rtiming.shared.settings.addinfo.AdditionalInformationTypeCodeType;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class EntriesSearchFormTest {

  private AdditionalInformationAdministrationTestDataProvider admin;
  private EventWithIndividualClassTestDataProvider event;
  private EntryTestDataProvider entry;
  private static CurrencyTestDataProvider currency;

  @BeforeClass
  public static void beforeClass() throws ProcessingException {
    currency = new CurrencyTestDataProvider();
  }

  @AfterClass
  public static void afterClass() throws ProcessingException {
    currency.remove();
  }

  @Test
  public void testAdditionalInformationTextSearch() throws ProcessingException {
    // create additional info
    List<FieldValue> fieldValue = new ArrayList<FieldValue>();
    fieldValue.add(new FieldValue(EntityField.class, EntityCodeType.EntryCode.ID));
    fieldValue.add(new FieldValue(TypeField.class, AdditionalInformationTypeCodeType.TextCode.ID));
    fieldValue.add(new FieldValue(MandatoryField.class, true));
    fieldValue.add(new FieldValue(MinimumField.class, null));
    fieldValue.add(new FieldValue(MaximumField.class, null));
    admin = new AdditionalInformationAdministrationTestDataProvider(fieldValue);

    createEventWithAddInfoAndEntry();

    String value = null;
    for (ITableRow row : entry.getForm().getAdditionalInformationEntryField().getTable().getRows()) {
      if (entry.getForm().getAdditionalInformationEntryField().getTable().getAdditionalInformationUidColumn().getValue(row).equals(admin.getAdditionalInformationUid())) {
        value = entry.getForm().getAdditionalInformationEntryField().getTable().getTextColumn().getValue(row);
      }
    }

    AbstractEntriesTablePage tablePage = getEntriesTablePage();
    EntriesSearchForm searchForm = (EntriesSearchForm) tablePage.getSearchFormInternal();
    searchForm.getFieldByClass(EntriesSearchForm.MainBox.ResetButton.class).doClick();
    searchForm.getEventField().setValue(event.getEventNr());
    searchForm.getAdditionalInformationBox().getAdditionalInformationField().setValue(admin.getAdditionalInformationUid());
    searchForm.getAdditionalInformationBox().getTextField().setValue(value);
    searchForm.getStartTimeFrom().setValue(null);
    searchForm.getStartTimeTo().setValue(null);
    tablePage.loadChildren();

    Assert.assertEquals(1, tablePage.getTable().getRowCount());
    Assert.assertEquals(entry.getForm().getEntryNr(), tablePage.getTable().getEntryNrColumn().getValue(0));
  }

  private void createEventWithAddInfoAndEntry() throws ProcessingException {
    event = new EventWithIndividualClassTestDataProvider();

    EventAdditionalInformationForm ea = new EventAdditionalInformationForm();
    ea.getEventField().setValue(event.getEventNr());
    ea.startNew();
    ea.getAdditionalInformationField().setValue(admin.getAdditionalInformationUid());
    ea.doOk();

    entry = new EntryTestDataProvider(event.getEventNr(), event.getClassUid());
  }

  @Test
  public void testAdditionalInformationIntegerSearch() throws ProcessingException {
    // create additional info
    List<FieldValue> fieldValue = new ArrayList<FieldValue>();
    fieldValue.add(new FieldValue(EntityField.class, EntityCodeType.EntryCode.ID));
    fieldValue.add(new FieldValue(TypeField.class, AdditionalInformationTypeCodeType.IntegerCode.ID));
    fieldValue.add(new FieldValue(MandatoryField.class, true));
    fieldValue.add(new FieldValue(MinimumField.class, null));
    fieldValue.add(new FieldValue(MaximumField.class, null));
    admin = new AdditionalInformationAdministrationTestDataProvider(fieldValue);

    createEventWithAddInfoAndEntry();

    Long value = null;
    for (ITableRow row : entry.getForm().getAdditionalInformationEntryField().getTable().getRows()) {
      if (entry.getForm().getAdditionalInformationEntryField().getTable().getAdditionalInformationUidColumn().getValue(row).equals(admin.getAdditionalInformationUid())) {
        value = entry.getForm().getAdditionalInformationEntryField().getTable().getIntegerColumn().getValue(row);
      }
    }

    AbstractEntriesTablePage tablePage = getEntriesTablePage();
    EntriesSearchForm searchForm = (EntriesSearchForm) tablePage.getSearchFormInternal();
    searchForm.getFieldByClass(EntriesSearchForm.MainBox.ResetButton.class).doClick();
    searchForm.getEventField().setValue(event.getEventNr());
    searchForm.getAdditionalInformationBox().getAdditionalInformationField().setValue(admin.getAdditionalInformationUid());
    searchForm.getAdditionalInformationBox().getIntegerFrom().setValue(value);
    searchForm.getAdditionalInformationBox().getIntegerTo().setValue(value);
    searchForm.getStartTimeFrom().setValue(null);
    searchForm.getStartTimeTo().setValue(null);
    tablePage.loadChildren();

    Assert.assertEquals(1, tablePage.getTable().getRowCount());
    Assert.assertEquals(entry.getForm().getEntryNr(), tablePage.getTable().getEntryNrColumn().getValue(0));
  }

  @Test
  public void testAdditionalInformationDoubleSearch() throws ProcessingException {
    // create additional info
    List<FieldValue> fieldValue = new ArrayList<FieldValue>();
    fieldValue.add(new FieldValue(EntityField.class, EntityCodeType.EntryCode.ID));
    fieldValue.add(new FieldValue(TypeField.class, AdditionalInformationTypeCodeType.DoubleCode.ID));
    fieldValue.add(new FieldValue(MandatoryField.class, true));
    fieldValue.add(new FieldValue(MinimumField.class, null));
    fieldValue.add(new FieldValue(MaximumField.class, null));
    admin = new AdditionalInformationAdministrationTestDataProvider(fieldValue);

    createEventWithAddInfoAndEntry();

    Double value = null;
    for (ITableRow row : entry.getForm().getAdditionalInformationEntryField().getTable().getRows()) {
      if (entry.getForm().getAdditionalInformationEntryField().getTable().getAdditionalInformationUidColumn().getValue(row).equals(admin.getAdditionalInformationUid())) {
        value = entry.getForm().getAdditionalInformationEntryField().getTable().getDecimalColumn().getValue(row);
      }
    }

    AbstractEntriesTablePage tablePage = getEntriesTablePage();
    EntriesSearchForm searchForm = (EntriesSearchForm) tablePage.getSearchFormInternal();
    searchForm.getFieldByClass(EntriesSearchForm.MainBox.ResetButton.class).doClick();
    searchForm.getEventField().setValue(event.getEventNr());
    searchForm.getAdditionalInformationBox().getAdditionalInformationField().setValue(admin.getAdditionalInformationUid());
    searchForm.getAdditionalInformationBox().getDecimalFrom().setValue(value);
    searchForm.getAdditionalInformationBox().getDecimalTo().setValue(value);
    searchForm.getStartTimeFrom().setValue(null);
    searchForm.getStartTimeTo().setValue(null);
    tablePage.loadChildren();

    Assert.assertEquals(1, tablePage.getTable().getRowCount());
    Assert.assertEquals(entry.getForm().getEntryNr(), tablePage.getTable().getEntryNrColumn().getValue(0));
  }

  @Test
  public void testAdditionalInformationBooleanSearch() throws ProcessingException {
    // create additional info
    List<FieldValue> fieldValue = new ArrayList<FieldValue>();
    fieldValue.add(new FieldValue(EntityField.class, EntityCodeType.EntryCode.ID));
    fieldValue.add(new FieldValue(TypeField.class, AdditionalInformationTypeCodeType.BooleanCode.ID));
    fieldValue.add(new FieldValue(MandatoryField.class, true));
    fieldValue.add(new FieldValue(MinimumField.class, null));
    fieldValue.add(new FieldValue(MaximumField.class, null));
    admin = new AdditionalInformationAdministrationTestDataProvider(fieldValue);

    createEventWithAddInfoAndEntry();

    Long value = null;
    for (ITableRow row : entry.getForm().getAdditionalInformationEntryField().getTable().getRows()) {
      if (entry.getForm().getAdditionalInformationEntryField().getTable().getAdditionalInformationUidColumn().getValue(row).equals(admin.getAdditionalInformationUid())) {
        value = entry.getForm().getAdditionalInformationEntryField().getTable().getIntegerColumn().getValue(row);
      }
    }

    AbstractEntriesTablePage tablePage = getEntriesTablePage();
    EntriesSearchForm searchForm = (EntriesSearchForm) tablePage.getSearchFormInternal();
    searchForm.getFieldByClass(EntriesSearchForm.MainBox.ResetButton.class).doClick();
    searchForm.getEventField().setValue(event.getEventNr());
    searchForm.getAdditionalInformationBox().getAdditionalInformationField().setValue(admin.getAdditionalInformationUid());
    searchForm.getAdditionalInformationBox().getSmartfieldField().setValue(value);
    searchForm.getStartTimeFrom().setValue(null);
    searchForm.getStartTimeTo().setValue(null);
    tablePage.loadChildren();

    Assert.assertEquals(1, tablePage.getTable().getRowCount());
    Assert.assertEquals(entry.getForm().getEntryNr(), tablePage.getTable().getEntryNrColumn().getValue(0));
  }

  @Test
  public void testAdditionalInformationSmartfieldSearch() throws ProcessingException {
    // create additional info
    List<FieldValue> fieldValue = new ArrayList<FieldValue>();
    fieldValue.add(new FieldValue(EntityField.class, EntityCodeType.EntryCode.ID));
    fieldValue.add(new FieldValue(TypeField.class, AdditionalInformationTypeCodeType.SmartfieldCode.ID));
    fieldValue.add(new FieldValue(MandatoryField.class, true));
    fieldValue.add(new FieldValue(MinimumField.class, null));
    fieldValue.add(new FieldValue(MaximumField.class, null));
    admin = new AdditionalInformationAdministrationTestDataProvider(fieldValue);

    AdditionalInformationAdministrationForm choice = new AdditionalInformationAdministrationForm();
    choice.getSmartfieldField().setValue(admin.getAdditionalInformationUid());
    choice.startNew();
    FormTestUtility.fillFormFields(choice);
    choice.doOk();

    AdditionalInformationAdministrationForm master = new AdditionalInformationAdministrationForm();
    master.setAdditionalInformationUid(admin.getAdditionalInformationUid());
    master.startModify();
    master.getDefaultValueSmartfieldField().setValue(choice.getAdditionalInformationUid());
    master.doOk();

    createEventWithAddInfoAndEntry();

    Long value = null;
    for (ITableRow row : entry.getForm().getAdditionalInformationEntryField().getTable().getRows()) {
      if (entry.getForm().getAdditionalInformationEntryField().getTable().getAdditionalInformationUidColumn().getValue(row).equals(admin.getAdditionalInformationUid())) {
        value = entry.getForm().getAdditionalInformationEntryField().getTable().getIntegerColumn().getValue(row);
      }
    }

    AbstractEntriesTablePage tablePage = getEntriesTablePage();
    EntriesSearchForm searchForm = (EntriesSearchForm) tablePage.getSearchFormInternal();
    searchForm.getFieldByClass(EntriesSearchForm.MainBox.ResetButton.class).doClick();
    searchForm.getEventField().setValue(event.getEventNr());
    searchForm.getAdditionalInformationBox().getAdditionalInformationField().setValue(admin.getAdditionalInformationUid());
    searchForm.getAdditionalInformationBox().getSmartfieldField().setValue(value);
    searchForm.getStartTimeFrom().setValue(null);
    searchForm.getStartTimeTo().setValue(null);
    tablePage.loadChildren();

    Assert.assertEquals(1, tablePage.getTable().getRowCount());
    Assert.assertEquals(entry.getForm().getEntryNr(), tablePage.getTable().getEntryNrColumn().getValue(0));
  }

  private AbstractEntriesTablePage getEntriesTablePage() throws ProcessingException {
    CODES.reloadCodeType(AdditionalInformationCodeType.class);
    SharedCache.resetCache();
    EntriesTablePage page = new EntriesTablePage(event.getEventNr(), ClientSession.get().getSessionClientNr(), null, event.getClassUid(), null, null);
    page.nodeAddedNotify();
    return page;
  }

  @After
  public void delete() throws ProcessingException {
    if (event != null) {
      event.remove();
    }
    if (entry != null) {
      entry.remove();
    }
    if (admin != null) {
      admin.remove();
    }
  }

}
