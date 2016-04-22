package com.rtiming.client.test.data;

import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.client.settings.fee.FeeForm;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.settings.fee.FeeFormData;
import com.rtiming.shared.settings.fee.IFeeProcessService;

public class FeeTestDataProvider extends AbstractTestDataProvider<FeeForm> {

  private List<FieldValue> fieldValue;
  private Long feeGroupNr;

  public FeeTestDataProvider(Long feeGroupNr, List<FieldValue> fieldValue) throws ProcessingException {
    this.fieldValue = fieldValue;
    this.feeGroupNr = feeGroupNr;
    callInitializer();
  }

  @Override
  protected FeeForm createForm() throws ProcessingException {
    FeeForm fee = new FeeForm();
    fee.setFeeGroupNr(feeGroupNr);
    fee.startNew();
    FormTestUtility.fillFormFields(fee, fieldValue.toArray(new FieldValue[fieldValue.size()]));
    fee.doOk();
    return fee;
  }

  @Override
  public void remove() throws ProcessingException {
    FeeFormData formData = new FeeFormData();
    formData.setFeeNr(getFeeNr());
    BEANS.get(IFeeProcessService.class).delete(formData);
  }

  public Long getFeeNr() throws ProcessingException {
    return getForm().getFeeNr();
  }

}
