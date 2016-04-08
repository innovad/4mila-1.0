package com.rtiming.shared.settings.clazz;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

import com.rtiming.shared.dao.RtClassAgeKey;

public class AgeFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public AgeFormData() {
  }

  public KeyProperty getKeyProperty() {
    return getPropertyByClass(KeyProperty.class);
  }

  /**
   * access method for property Key.
   */
  public RtClassAgeKey getKey() {
    return getKeyProperty().getValue();
  }

  /**
   * access method for property Key.
   */
  public void setKey(RtClassAgeKey key) {
    getKeyProperty().setValue(key);
  }

  public Clazz getClazz() {
    return getFieldByClass(Clazz.class);
  }

  public From getFrom() {
    return getFieldByClass(From.class);
  }

  public Sex getSex() {
    return getFieldByClass(Sex.class);
  }

  public To getTo() {
    return getFieldByClass(To.class);
  }

  public class KeyProperty extends AbstractPropertyData<RtClassAgeKey> {
    private static final long serialVersionUID = 1L;

    public KeyProperty() {
    }
  }

  public static class Clazz extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Clazz() {
    }

    /**
     * list of derived validation rules.
     */
 
  }

  public static class From extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public From() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class Sex extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Sex() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class To extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public To() {
    }

    /**
     * list of derived validation rules.
     */
     
  }
}
