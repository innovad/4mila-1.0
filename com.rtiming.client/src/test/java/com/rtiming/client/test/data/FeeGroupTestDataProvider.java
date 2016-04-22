package com.rtiming.client.test.data;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.client.settings.fee.FeeGroupForm;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.settings.fee.FeeGroupFormData;
import com.rtiming.shared.settings.fee.IFeeGroupProcessService;

public class FeeGroupTestDataProvider extends AbstractTestDataProvider<FeeGroupForm> {

  private List<FieldValue> fieldValue;

  public FeeGroupTestDataProvider() throws ProcessingException {
    this.fieldValue = new ArrayList<FieldValue>();
    callInitializer();
  }

  public FeeGroupTestDataProvider(List<FieldValue> fieldValue) throws ProcessingException {
    this.fieldValue = fieldValue;
    callInitializer();
  }

  @Override
  protected FeeGroupForm createForm() throws ProcessingException {
    FeeGroupForm feeGroup = new FeeGroupForm();
    feeGroup.startNew();
    FormTestUtility.fillFormFields(feeGroup, fieldValue.toArray(new FieldValue[fieldValue.size()]));
    feeGroup.doOk();
    return feeGroup;
  }

  @Override
  public void remove() throws ProcessingException {
    FeeGroupFormData formData = new FeeGroupFormData();
    formData.setFeeGroupNr(getFeeGroupNr());
    BEANS.get(IFeeGroupProcessService.class).delete(formData);
  }

  public Long getFeeGroupNr() throws ProcessingException {
    return getForm().getFeeGroupNr();
  }

}
