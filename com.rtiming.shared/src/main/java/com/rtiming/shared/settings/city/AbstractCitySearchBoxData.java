package com.rtiming.shared.settings.city;

import org.eclipse.scout.rt.shared.data.form.fields.AbstractFormFieldData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;


public abstract class AbstractCitySearchBoxData extends AbstractFormFieldData {
  private static final long serialVersionUID = 1L;

  public AbstractCitySearchBoxData() {
  }

  public Area getArea() {
    return getFieldByClass(Area.class);
  }

  public City getCity() {
    return getFieldByClass(City.class);
  }

  public Country getCountry() {
    return getFieldByClass(Country.class);
  }

  public Region getRegion() {
    return getFieldByClass(Region.class);
  }

  public Zip getZip() {
    return getFieldByClass(Zip.class);
  }

  public static class Area extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Area() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class City extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public City() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class Country extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Country() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class Region extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public Region() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class Zip extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public Zip() {
    }

    /**
     * list of derived validation rules.
     */
     
  }
}