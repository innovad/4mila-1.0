package com.rtiming.client.test.data;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.client.ecard.ECardForm;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.ecard.IECardProcessService;
import com.rtiming.shared.test.helper.ITestingJPAService;

public class ECardTestDataProvider extends AbstractTestDataProvider<ECardForm> {

  private String eCardNo;

  public ECardTestDataProvider() throws ProcessingException {
    this.eCardNo = "012345";
    callInitializer();
  }

  public ECardTestDataProvider(String eCardNo) throws ProcessingException {
    this.eCardNo = eCardNo;
    callInitializer();
  }

  private ECardForm eCard;

  @Override
  protected ECardForm createForm() throws ProcessingException {

    // Control
    eCard = new ECardForm();
    eCard.startNew();
    FormTestUtility.fillFormFields(eCard, new FieldValue(ECardForm.MainBox.NumberField.class, eCardNo));
    eCard.doOk();

    return eCard;
  }

  @Override
  public void remove() throws ProcessingException {
    BEANS.get(ITestingJPAService.class).cleanupRunner(getForm().getECardKey().getId());
    BEANS.get(IECardProcessService.class).delete(getForm().getECardKey());
  }

  public Long getECardNr() throws ProcessingException {
    return getForm().getECardKey().getId();
  }

}
