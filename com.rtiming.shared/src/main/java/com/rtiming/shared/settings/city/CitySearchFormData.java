package com.rtiming.shared.settings.city;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;

public class CitySearchFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public CitySearchFormData() {
  }

  public CityBox getCityBox() {
    return getFieldByClass(CityBox.class);
  }

  public static class CityBox extends AbstractCitySearchBoxData {
    private static final long serialVersionUID = 1L;

    public CityBox() {
    }
  }
}
