package com.rtiming.shared.settings.city;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;


public class CityFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public CityFormData() {
  }

  public CityNrProperty getCityNrProperty() {
    return getPropertyByClass(CityNrProperty.class);
  }

  /**
   * access method for property CityNr.
   */
  public Long getCityNr() {
    return getCityNrProperty().getValue();
  }

  /**
   * access method for property CityNr.
   */
  public void setCityNr(Long cityNr) {
    getCityNrProperty().setValue(cityNr);
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

  public class CityNrProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public CityNrProperty() {
    }
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
