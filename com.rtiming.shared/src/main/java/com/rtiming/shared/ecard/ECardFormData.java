package com.rtiming.shared.ecard;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

import com.rtiming.shared.dao.RtEcardKey;

public class ECardFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public ECardFormData() {
  }

  public ECardKeyProperty getECardKeyProperty() {
    return getPropertyByClass(ECardKeyProperty.class);
  }

  /**
   * access method for property ECardKey.
   */
  public RtEcardKey getECardKey() {
    return getECardKeyProperty().getValue();
  }

  /**
   * access method for property ECardKey.
   */
  public void setECardKey(RtEcardKey eCardKey) {
    getECardKeyProperty().setValue(eCardKey);
  }

  public ECardType getECardType() {
    return getFieldByClass(ECardType.class);
  }

  public Number getNumber() {
    return getFieldByClass(Number.class);
  }

  public RentalCard getRentalCard() {
    return getFieldByClass(RentalCard.class);
  }

  public class ECardKeyProperty extends AbstractPropertyData<RtEcardKey> {
    private static final long serialVersionUID = 1L;

    public ECardKeyProperty() {
    }
  }

  public static class ECardType extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public ECardType() {
    }

    /**
     * list of derived validation rules.
     */
 
  }

  public static class Number extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public Number() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class RentalCard extends AbstractValueFieldData<Boolean> {
    private static final long serialVersionUID = 1L;

    public RentalCard() {
    }
  }
}
