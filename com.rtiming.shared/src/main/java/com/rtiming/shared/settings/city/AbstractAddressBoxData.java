package com.rtiming.shared.settings.city;

import org.eclipse.scout.rt.shared.data.form.fields.AbstractFormFieldData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;


public abstract class AbstractAddressBoxData extends AbstractFormFieldData {
  private static final long serialVersionUID = 1L;

  public AbstractAddressBoxData() {
  }

  public City getCity() {
    return getFieldByClass(City.class);
  }

  public EMail getEMail() {
    return getFieldByClass(EMail.class);
  }

  public Fax getFax() {
    return getFieldByClass(Fax.class);
  }

  public Mobile getMobile() {
    return getFieldByClass(Mobile.class);
  }

  public Phone getPhone() {
    return getFieldByClass(Phone.class);
  }

  public Street getStreet() {
    return getFieldByClass(Street.class);
  }

  public Url getUrl() {
    return getFieldByClass(Url.class);
  }

  public static class City extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public City() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class EMail extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public EMail() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class Fax extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public Fax() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class Mobile extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public Mobile() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class Phone extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public Phone() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class Street extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public Street() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class Url extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public Url() {
    }

    /**
     * list of derived validation rules.
     */
     
  }
}
