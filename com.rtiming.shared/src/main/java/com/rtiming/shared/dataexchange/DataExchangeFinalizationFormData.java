package com.rtiming.shared.dataexchange;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

public class DataExchangeFinalizationFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public DataExchangeFinalizationFormData() {
  }

  public FullPathNameProperty getFullPathNameProperty() {
    return getPropertyByClass(FullPathNameProperty.class);
  }

  /**
   * access method for property FullPathName.
   */
  public String getFullPathName() {
    return getFullPathNameProperty().getValue();
  }

  /**
   * access method for property FullPathName.
   */
  public void setFullPathName(String fullPathName) {
    getFullPathNameProperty().setValue(fullPathName);
  }

  public Info getInfo() {
    return getFieldByClass(Info.class);
  }

  public class FullPathNameProperty extends AbstractPropertyData<String> {
    private static final long serialVersionUID = 1L;

    public FullPathNameProperty() {
    }
  }

  public static class Info extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public Info() {
    }

    /**
     * list of derived validation rules.
     */
     
  }
}
