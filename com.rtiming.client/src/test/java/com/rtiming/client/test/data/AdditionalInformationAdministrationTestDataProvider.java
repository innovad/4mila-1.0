package com.rtiming.client.test.data;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.common.code.CODES;
import org.junit.Assert;

import com.rtiming.client.common.ui.fields.AbstractCodeBox.ShortcutField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.settings.addinfo.AdditionalInformationAdministrationFormData;
import com.rtiming.shared.settings.addinfo.AdditionalInformationCodeType;
import com.rtiming.shared.settings.addinfo.IAdditionalInformationAdministrationProcessService;

public class AdditionalInformationAdministrationTestDataProvider extends AbstractTestDataProvider<AdditionalInformationAdministrationForm> {

  private List<FieldValue> fieldValue;

  public AdditionalInformationAdministrationTestDataProvider() throws ProcessingException {
    this.fieldValue = new ArrayList<FieldValue>();
    fieldValue.add(new FieldValue(ShortcutField.class, "ai" + System.currentTimeMillis()));
    callInitializer();
  }

  public AdditionalInformationAdministrationTestDataProvider(List<FieldValue> fieldValue) throws ProcessingException {
    this.fieldValue = fieldValue;
    boolean found = false;
    for (FieldValue value : fieldValue) {
      if (value.getFieldClass().isAssignableFrom(ShortcutField.class)) {
        found = true;
        break;
      }
    }
    if (!found) {
      fieldValue.add(new FieldValue(ShortcutField.class, "ai" + System.currentTimeMillis()));
    }
    callInitializer();
  }

  @Override
  protected AdditionalInformationAdministrationForm createForm() throws ProcessingException {
    AdditionalInformationAdministrationForm admin = new AdditionalInformationAdministrationForm();
    admin.startNew();
    FormTestUtility.fillFormFields(admin, fieldValue.toArray(new FieldValue[fieldValue.size()]));
    admin.doOk();
    Assert.assertNotNull(admin.getAdditionalInformationUid());
    CODES.reloadCodeType(AdditionalInformationCodeType.class);
    return admin;
  }

  @Override
  public void remove() throws ProcessingException {
    if (getForm() != null) {
      AdditionalInformationAdministrationFormData formData = new AdditionalInformationAdministrationFormData();
      formData.setAdditionalInformationUid(getForm().getAdditionalInformationUid());
      BEANS.get(IAdditionalInformationAdministrationProcessService.class).delete(formData);
    }
  }

  public Long getAdditionalInformationUid() throws ProcessingException {
    return getForm().getAdditionalInformationUid();
  }

}
