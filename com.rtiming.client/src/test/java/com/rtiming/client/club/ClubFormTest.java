package com.rtiming.client.club;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.settings.addinfo.AbstractAdditionalInformationField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.DefaultValueTextField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.EntityField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.MandatoryField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.MaximumField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.MinimumField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.TypeField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationTablePage;
import com.rtiming.client.test.AbstractFormTest;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.client.test.data.AdditionalInformationAdministrationTestDataProvider;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.club.ClubFormData;
import com.rtiming.shared.club.IClubProcessService;
import com.rtiming.shared.common.EntityCodeType;
import com.rtiming.shared.common.database.sql.BeanUtility;
import com.rtiming.shared.dao.RtClubKey;
import com.rtiming.shared.settings.addinfo.AdditionalInformationAdministrationFormData;
import com.rtiming.shared.settings.addinfo.AdditionalInformationTypeCodeType;
import com.rtiming.shared.settings.addinfo.IAdditionalInformationAdministrationProcessService;

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(TestEnvironmentClientSession.class)
public class ClubFormTest extends AbstractFormTest<ClubForm> {

  private AdditionalInformationAdministrationTestDataProvider admin;

  @Override
  protected ClubForm getStartedForm() throws ProcessingException {
    ClubForm form = new ClubForm();
    form.startNew();
    return form;
  }

  @Override
  protected ClubForm getModifyForm() throws ProcessingException {
    ClubForm form = new ClubForm();
    form.setClubNr(getForm().getClubNr());
    form.startModify();
    return form;
  }

  @Override
  public void cleanup() throws ProcessingException {
    BEANS.get(IClubProcessService.class).delete(RtClubKey.create(getForm().getClubNr()));
  }

  @Test
  public void testFind() throws ProcessingException {
    getForm().getNameField().setValue("FOG United Switzerland");
    getForm().doOk();

    ClubFormData formData = BeanUtility.clubBean2formData(BEANS.get(IClubProcessService.class).findClub("FOG United Switzerland"));
    Assert.assertEquals(getForm().getClubNr(), formData.getClubNr());
    Assert.assertEquals(getForm().getNameField().getValue(), formData.getName().getValue());
    Assert.assertEquals(getForm().getExtKeyField().getValue(), formData.getExtKey().getValue());
    Assert.assertEquals(getForm().getShortcutField().getValue(), formData.getShortcut().getValue());
  }

  @Test
  public void testAdditionalInformationStandard() throws ProcessingException {
    // create additional info
    List<FieldValue> fieldValue = new ArrayList<FieldValue>();
    fieldValue.add(new FieldValue(EntityField.class, EntityCodeType.ClubCode.ID));
    fieldValue.add(new FieldValue(TypeField.class, AdditionalInformationTypeCodeType.TextCode.ID));
    fieldValue.add(new FieldValue(MandatoryField.class, false));
    fieldValue.add(new FieldValue(MinimumField.class, null));
    fieldValue.add(new FieldValue(MaximumField.class, null));
    admin = new AdditionalInformationAdministrationTestDataProvider(fieldValue);

    // club
    ClubForm club = new ClubForm();
    club.startNew();
    FormTestUtility.fillFormFields(club);
    boolean exists = false;
    AbstractAdditionalInformationField.Table addInfoTable = club.getAdditionalInformationField().getTable();
    for (ITableRow row : addInfoTable.getRows()) {
      if (addInfoTable.getAdditionalInformationUidColumn().getValue(row).equals(admin.getAdditionalInformationUid())) {
        // this is the add info row
        exists = true;
        Assert.assertEquals(AdditionalInformationTypeCodeType.TextCode.ID, addInfoTable.getTypeColumn().getValue(row).longValue());
      }
    }
    Assert.assertTrue(exists);
    club.doClose();
  }

  @Test
  public void testAdditionalInformationDefaultValue() throws ProcessingException {
    // create additional info
    List<FieldValue> fieldValue = new ArrayList<FieldValue>();
    fieldValue.add(new FieldValue(EntityField.class, EntityCodeType.ClubCode.ID));
    fieldValue.add(new FieldValue(TypeField.class, AdditionalInformationTypeCodeType.TextCode.ID));
    fieldValue.add(new FieldValue(MandatoryField.class, false));
    fieldValue.add(new FieldValue(MinimumField.class, null));
    fieldValue.add(new FieldValue(MaximumField.class, null));
    fieldValue.add(new FieldValue(DefaultValueTextField.class, "Default"));
    admin = new AdditionalInformationAdministrationTestDataProvider(fieldValue);

    // club
    ClubForm club = new ClubForm();
    club.startNew();
    boolean exists = false;
    AbstractAdditionalInformationField.Table addInfoTable = club.getAdditionalInformationField().getTable();
    for (ITableRow row : addInfoTable.getRows()) {
      if (addInfoTable.getAdditionalInformationUidColumn().getValue(row).equals(admin.getAdditionalInformationUid())) {
        // this is the add info row
        exists = true;
        Assert.assertEquals("Default", addInfoTable.getValueColumn().getValue(row));
      }
    }
    Assert.assertTrue(exists);
    club.doClose();
  }

  @Test(expected = VetoException.class)
  public void testAdditionalInformationMandatory() throws ProcessingException {
    // create additional info
    List<FieldValue> fieldValue = new ArrayList<FieldValue>();
    fieldValue.add(new FieldValue(EntityField.class, EntityCodeType.ClubCode.ID));
    fieldValue.add(new FieldValue(TypeField.class, AdditionalInformationTypeCodeType.TextCode.ID));
    fieldValue.add(new FieldValue(MandatoryField.class, true));
    fieldValue.add(new FieldValue(MinimumField.class, null));
    fieldValue.add(new FieldValue(MaximumField.class, null));
    admin = new AdditionalInformationAdministrationTestDataProvider(fieldValue);

    // create new club
    ClubForm club = new ClubForm();
    club.startNew();
    FormTestUtility.fillFormFields(club);
    // force add value = null
    for (ITableRow row : club.getAdditionalInformationField().getTable().getRows()) {
      if (club.getAdditionalInformationField().getTable().getAdditionalInformationUidColumn().getValue(row).equals(admin.getAdditionalInformationUid())) {
        club.getAdditionalInformationField().getTable().getTextColumn().setValue(row, null);
        club.getAdditionalInformationField().getTable().getValueColumn().setValue(row, null);
      }
    }
    club.doOk();
  }

  @After
  public void delete() throws ProcessingException {
    if (admin != null) {
      deleteAdditionalInformation(admin.getAdditionalInformationUid());
    }
  }

  private void deleteAdditionalInformation(Long addInfoUid) throws ProcessingException {
    AdditionalInformationAdministrationTablePage tablePage = new AdditionalInformationAdministrationTablePage(null);
    tablePage.loadChildren();
    boolean exists = false;
    for (ITableRow row : tablePage.getTable().getRows()) {
      if (tablePage.getTable().getAdditionalInformationUidColumn().getValue(row).equals(addInfoUid)) {
        exists = true;
      }
    }
    Assert.assertTrue(exists);
    AdditionalInformationAdministrationFormData formData = new AdditionalInformationAdministrationFormData();
    formData.setAdditionalInformationUid(addInfoUid);
    BEANS.get(IAdditionalInformationAdministrationProcessService.class).delete(formData);
  }

}
