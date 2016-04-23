package com.rtiming.client.entry;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.EntityField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.MandatoryField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.MaximumField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.MinimumField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.TypeField;
import com.rtiming.client.settings.addinfo.EventAdditionalInformationForm;
import com.rtiming.client.test.ClientTestingUtility;
import com.rtiming.client.test.data.AdditionalInformationAdministrationTestDataProvider;
import com.rtiming.client.test.data.CurrencyTestDataProvider;
import com.rtiming.client.test.data.EntryTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualClassTestDataProvider;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.common.EntityCodeType;
import com.rtiming.shared.settings.addinfo.AdditionalInformationTypeCodeType;

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(ClientSession.class)
public class RegistrationsSearchFormTest {

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

    IPage root = ClientTestingUtility.gotoOutline(RegistrationOutline.class);
    RegistrationsTablePage tablePage = ClientTestingUtility.gotoChildPage(root, RegistrationsTablePage.class);
    RegistrationsSearchForm searchForm = (RegistrationsSearchForm) tablePage.getSearchFormInternal();
    searchForm.doReset();
    searchForm.getAdditionalInformationBox().getAdditionalInformationField().setValue(admin.getAdditionalInformationUid());
    searchForm.getAdditionalInformationBox().getSmartfieldField().setValue(value);
    searchForm.doSaveWithoutMarkerChange();

    Assert.assertEquals(1, tablePage.getTable().getRowCount());
    Assert.assertEquals(entry.getRegistrationNr(), tablePage.getTable().getRegistrationNrColumn().getValue(0));
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

}
