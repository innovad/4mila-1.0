package com.rtiming.shared.settings.fee;

import java.util.Date;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

public class FeeFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public FeeFormData() {
  }

  public AdditionalInformationUidProperty getAdditionalInformationUidProperty() {
    return getPropertyByClass(AdditionalInformationUidProperty.class);
  }

  /**
   * access method for property AdditionalInformationUid.
   */
  public Long getAdditionalInformationUid() {
    return getAdditionalInformationUidProperty().getValue();
  }

  /**
   * access method for property AdditionalInformationUid.
   */
  public void setAdditionalInformationUid(Long additionalInformationUid) {
    getAdditionalInformationUidProperty().setValue(additionalInformationUid);
  }

  public CashPaymentOnRegistrationProperty getCashPaymentOnRegistrationProperty() {
    return getPropertyByClass(CashPaymentOnRegistrationProperty.class);
  }

  /**
   * access method for property CashPaymentOnRegistration.
   */
  public boolean isCashPaymentOnRegistration() {
    return (getCashPaymentOnRegistrationProperty().getValue() == null) ? (false) : (getCashPaymentOnRegistrationProperty().getValue());
  }

  /**
   * access method for property CashPaymentOnRegistration.
   */
  public void setCashPaymentOnRegistration(boolean cashPaymentOnRegistration) {
    getCashPaymentOnRegistrationProperty().setValue(cashPaymentOnRegistration);
  }

  public ClassUidProperty getClassUidProperty() {
    return getPropertyByClass(ClassUidProperty.class);
  }

  /**
   * access method for property ClassUid.
   */
  public Long getClassUid() {
    return getClassUidProperty().getValue();
  }

  /**
   * access method for property ClassUid.
   */
  public void setClassUid(Long classUid) {
    getClassUidProperty().setValue(classUid);
  }

  public EventNrProperty getEventNrProperty() {
    return getPropertyByClass(EventNrProperty.class);
  }

  /**
   * access method for property EventNr.
   */
  public Long getEventNr() {
    return getEventNrProperty().getValue();
  }

  /**
   * access method for property EventNr.
   */
  public void setEventNr(Long eventNr) {
    getEventNrProperty().setValue(eventNr);
  }

  public FeeGroupNrProperty getFeeGroupNrProperty() {
    return getPropertyByClass(FeeGroupNrProperty.class);
  }

  /**
   * access method for property FeeGroupNr.
   */
  public Long getFeeGroupNr() {
    return getFeeGroupNrProperty().getValue();
  }

  /**
   * access method for property FeeGroupNr.
   */
  public void setFeeGroupNr(Long feeGroupNr) {
    getFeeGroupNrProperty().setValue(feeGroupNr);
  }

  public FeeNrProperty getFeeNrProperty() {
    return getPropertyByClass(FeeNrProperty.class);
  }

  /**
   * access method for property FeeNr.
   */
  public Long getFeeNr() {
    return getFeeNrProperty().getValue();
  }

  /**
   * access method for property FeeNr.
   */
  public void setFeeNr(Long feeNr) {
    getFeeNrProperty().setValue(feeNr);
  }

  public AgeFrom getAgeFrom() {
    return getFieldByClass(AgeFrom.class);
  }

  public AgeTo getAgeTo() {
    return getFieldByClass(AgeTo.class);
  }

  public Currency getCurrency() {
    return getFieldByClass(Currency.class);
  }

  public DateFrom getDateFrom() {
    return getFieldByClass(DateFrom.class);
  }

  public DateTo getDateTo() {
    return getFieldByClass(DateTo.class);
  }

  public Fee getFee() {
    return getFieldByClass(Fee.class);
  }

  public class AdditionalInformationUidProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public AdditionalInformationUidProperty() {
    }
  }

  public class CashPaymentOnRegistrationProperty extends AbstractPropertyData<Boolean> {
    private static final long serialVersionUID = 1L;

    public CashPaymentOnRegistrationProperty() {
    }
  }

  public class ClassUidProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public ClassUidProperty() {
    }
  }

  public class EventNrProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public EventNrProperty() {
    }
  }

  public class FeeGroupNrProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public FeeGroupNrProperty() {
    }
  }

  public class FeeNrProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public FeeNrProperty() {
    }
  }

  public static class AgeFrom extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public AgeFrom() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class AgeTo extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public AgeTo() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class Currency extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Currency() {
    }

    /**
     * list of derived validation rules.
     */
 
  }

  public static class DateFrom extends AbstractValueFieldData<Date> {
    private static final long serialVersionUID = 1L;

    public DateFrom() {
    }
  }

  public static class DateTo extends AbstractValueFieldData<Date> {
    private static final long serialVersionUID = 1L;

    public DateTo() {
    }
  }

  public static class Fee extends AbstractValueFieldData<Double> {
    private static final long serialVersionUID = 1L;

    public Fee() {
    }

    /**
     * list of derived validation rules.
     */
 
  }
}
