package com.rtiming.client.club;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.TestClientSession;
import com.rtiming.client.event.EventsOutline;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.EntityField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.MandatoryField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.MaximumField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.MinimumField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.TypeField;
import com.rtiming.client.test.ClientTestingUtility;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.client.test.data.AdditionalInformationAdministrationTestDataProvider;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.common.EntityCodeType;
import com.rtiming.shared.settings.addinfo.AdditionalInformationAdministrationFormData;
import com.rtiming.shared.settings.addinfo.AdditionalInformationTypeCodeType;
import com.rtiming.shared.settings.addinfo.IAdditionalInformationAdministrationProcessService;

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(TestClientSession.class)
public class ClubsSearchFormTest {

  private AdditionalInformationAdministrationTestDataProvider admin;

  @Test
  public void testAdditionalInformationTextSearch() throws ProcessingException {
    // create additional info
    List<FieldValue> fieldValue = new ArrayList<FieldValue>();
    fieldValue.add(new FieldValue(EntityField.class, EntityCodeType.ClubCode.ID));
    fieldValue.add(new FieldValue(TypeField.class, AdditionalInformationTypeCodeType.TextCode.ID));
    fieldValue.add(new FieldValue(MandatoryField.class, true));
    fieldValue.add(new FieldValue(MinimumField.class, null));
    fieldValue.add(new FieldValue(MaximumField.class, null));
    admin = new AdditionalInformationAdministrationTestDataProvider(fieldValue);

    ClubForm club = new ClubForm();
    club.startNew();
    FormTestUtility.fillFormFields(club);
    club.doOk();

    String value = null;
    for (ITableRow row : club.getAdditionalInformationField().getTable().getRows()) {
      if (club.getAdditionalInformationField().getTable().getAdditionalInformationUidColumn().getValue(row).equals(admin.getAdditionalInformationUid())) {
        value = club.getAdditionalInformationField().getTable().getTextColumn().getValue(row);
      }
    }

    IPage root = ClientTestingUtility.gotoOutline(EventsOutline.class);
    ClubsTablePage tablePage = ClientTestingUtility.gotoChildPage(root, ClubsTablePage.class);
    ClubsSearchForm searchForm = (ClubsSearchForm) tablePage.getSearchFormInternal();
    searchForm.doReset();
    searchForm.getAdditionalInformationBox().getAdditionalInformationField().setValue(admin.getAdditionalInformationUid());
    searchForm.getAdditionalInformationBox().getTextField().setValue(value);
    searchForm.doSaveWithoutMarkerChange();

    Assert.assertEquals(1, tablePage.getTable().getRowCount());
    Assert.assertEquals(club.getClubNr(), tablePage.getTable().getClubNrColumn().getValue(0));
  }

  @Test
  public void testAdditionalInformationIntegerSearch() throws ProcessingException {
    // create additional info
    List<FieldValue> fieldValue = new ArrayList<FieldValue>();
    fieldValue.add(new FieldValue(EntityField.class, EntityCodeType.ClubCode.ID));
    fieldValue.add(new FieldValue(TypeField.class, AdditionalInformationTypeCodeType.IntegerCode.ID));
    fieldValue.add(new FieldValue(MandatoryField.class, true));
    fieldValue.add(new FieldValue(MinimumField.class, null));
    fieldValue.add(new FieldValue(MaximumField.class, null));
    admin = new AdditionalInformationAdministrationTestDataProvider(fieldValue);

    ClubForm club = new ClubForm();
    club.startNew();
    FormTestUtility.fillFormFields(club);
    club.doOk();

    Long value = null;
    for (ITableRow row : club.getAdditionalInformationField().getTable().getRows()) {
      if (club.getAdditionalInformationField().getTable().getAdditionalInformationUidColumn().getValue(row).equals(admin.getAdditionalInformationUid())) {
        value = club.getAdditionalInformationField().getTable().getIntegerColumn().getValue(row);
      }
    }

    IPage root = ClientTestingUtility.gotoOutline(EventsOutline.class);
    ClubsTablePage tablePage = ClientTestingUtility.gotoChildPage(root, ClubsTablePage.class);
    ClubsSearchForm searchForm = (ClubsSearchForm) tablePage.getSearchFormInternal();
    searchForm.doReset();
    searchForm.getAdditionalInformationBox().getAdditionalInformationField().setValue(admin.getAdditionalInformationUid());
    searchForm.getAdditionalInformationBox().getIntegerFrom().setValue(value);
    searchForm.getAdditionalInformationBox().getIntegerTo().setValue(value);
    searchForm.doSaveWithoutMarkerChange();

    Assert.assertEquals(1, tablePage.getTable().getRowCount());
    Assert.assertEquals(club.getClubNr(), tablePage.getTable().getClubNrColumn().getValue(0));
  }

  @Test
  public void testAdditionalInformationDoubleSearch() throws ProcessingException {
    // create additional info
    List<FieldValue> fieldValue = new ArrayList<FieldValue>();
    fieldValue.add(new FieldValue(EntityField.class, EntityCodeType.ClubCode.ID));
    fieldValue.add(new FieldValue(TypeField.class, AdditionalInformationTypeCodeType.DoubleCode.ID));
    fieldValue.add(new FieldValue(MandatoryField.class, true));
    fieldValue.add(new FieldValue(MinimumField.class, null));
    fieldValue.add(new FieldValue(MaximumField.class, null));
    admin = new AdditionalInformationAdministrationTestDataProvider(fieldValue);

    ClubForm club = new ClubForm();
    club.startNew();
    FormTestUtility.fillFormFields(club);
    club.doOk();

    Double value = null;
    for (ITableRow row : club.getAdditionalInformationField().getTable().getRows()) {
      if (club.getAdditionalInformationField().getTable().getAdditionalInformationUidColumn().getValue(row).equals(admin.getAdditionalInformationUid())) {
        value = club.getAdditionalInformationField().getTable().getDecimalColumn().getValue(row);
      }
    }

    IPage root = ClientTestingUtility.gotoOutline(EventsOutline.class);
    ClubsTablePage tablePage = ClientTestingUtility.gotoChildPage(root, ClubsTablePage.class);
    ClubsSearchForm searchForm = (ClubsSearchForm) tablePage.getSearchFormInternal();
    searchForm.doReset();
    searchForm.getAdditionalInformationBox().getAdditionalInformationField().setValue(admin.getAdditionalInformationUid());
    searchForm.getAdditionalInformationBox().getDecimalFrom().setValue(value);
    searchForm.getAdditionalInformationBox().getDecimalTo().setValue(value);
    searchForm.doSaveWithoutMarkerChange();

    Assert.assertEquals(1, tablePage.getTable().getRowCount());
    Assert.assertEquals(club.getClubNr(), tablePage.getTable().getClubNrColumn().getValue(0));
  }

  @Test
  public void testAdditionalInformationBooleanSearch() throws ProcessingException {
    // create additional info
    List<FieldValue> fieldValue = new ArrayList<FieldValue>();
    fieldValue.add(new FieldValue(EntityField.class, EntityCodeType.ClubCode.ID));
    fieldValue.add(new FieldValue(TypeField.class, AdditionalInformationTypeCodeType.BooleanCode.ID));
    fieldValue.add(new FieldValue(MandatoryField.class, true));
    fieldValue.add(new FieldValue(MinimumField.class, null));
    fieldValue.add(new FieldValue(MaximumField.class, null));
    admin = new AdditionalInformationAdministrationTestDataProvider(fieldValue);

    ClubForm club = new ClubForm();
    club.startNew();
    FormTestUtility.fillFormFields(club);
    club.doOk();

    Long value = null;
    for (ITableRow row : club.getAdditionalInformationField().getTable().getRows()) {
      if (club.getAdditionalInformationField().getTable().getAdditionalInformationUidColumn().getValue(row).equals(admin.getAdditionalInformationUid())) {
        value = club.getAdditionalInformationField().getTable().getIntegerColumn().getValue(row);
      }
    }

    IPage root = ClientTestingUtility.gotoOutline(EventsOutline.class);
    ClubsTablePage tablePage = ClientTestingUtility.gotoChildPage(root, ClubsTablePage.class);
    ClubsSearchForm searchForm = (ClubsSearchForm) tablePage.getSearchFormInternal();
    searchForm.doReset();
    searchForm.getAdditionalInformationBox().getAdditionalInformationField().setValue(admin.getAdditionalInformationUid());
    searchForm.getAdditionalInformationBox().getSmartfieldField().setValue(value);
    searchForm.doSaveWithoutMarkerChange();

    Assert.assertEquals(1, tablePage.getTable().getRowCount());
    Assert.assertEquals(club.getClubNr(), tablePage.getTable().getClubNrColumn().getValue(0));
  }

  @Test
  public void testAdditionalInformationSmartfieldSearch() throws ProcessingException {
    // create additional info
    List<FieldValue> fieldValue = new ArrayList<FieldValue>();
    fieldValue.add(new FieldValue(EntityField.class, EntityCodeType.ClubCode.ID));
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

    ClubForm club = new ClubForm();
    club.startNew();
    FormTestUtility.fillFormFields(club);
    club.doOk();

    Long value = null;
    for (ITableRow row : club.getAdditionalInformationField().getTable().getRows()) {
      if (club.getAdditionalInformationField().getTable().getAdditionalInformationUidColumn().getValue(row).equals(admin.getAdditionalInformationUid())) {
        value = club.getAdditionalInformationField().getTable().getIntegerColumn().getValue(row);
      }
    }

    IPage root = ClientTestingUtility.gotoOutline(EventsOutline.class);
    ClubsTablePage tablePage = ClientTestingUtility.gotoChildPage(root, ClubsTablePage.class);
    ClubsSearchForm searchForm = (ClubsSearchForm) tablePage.getSearchFormInternal();
    searchForm.doReset();
    searchForm.getAdditionalInformationBox().getAdditionalInformationField().setValue(admin.getAdditionalInformationUid());
    searchForm.getAdditionalInformationBox().getSmartfieldField().setValue(value);
    searchForm.doSaveWithoutMarkerChange();

    Assert.assertEquals(1, tablePage.getTable().getRowCount());
    Assert.assertEquals(club.getClubNr(), tablePage.getTable().getClubNrColumn().getValue(0));
  }

  @After
  public void delete() throws ProcessingException {
    if (admin != null) {
      AdditionalInformationAdministrationFormData formData = new AdditionalInformationAdministrationFormData();
      formData.setAdditionalInformationUid(admin.getAdditionalInformationUid());
      BEANS.get(IAdditionalInformationAdministrationProcessService.class).delete(formData);
    }
  }

}
