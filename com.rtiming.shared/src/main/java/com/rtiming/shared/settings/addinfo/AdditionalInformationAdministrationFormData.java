package com.rtiming.shared.settings.addinfo;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

import com.rtiming.shared.common.AbstractCodeBoxData;

public class AdditionalInformationAdministrationFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public AdditionalInformationAdministrationFormData() {
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

  public CodeBox getCodeBox() {
    return getFieldByClass(CodeBox.class);
  }

  public DefaultValueBoolean getDefaultValueBoolean() {
    return getFieldByClass(DefaultValueBoolean.class);
  }

  public DefaultValueDecimal getDefaultValueDecimal() {
    return getFieldByClass(DefaultValueDecimal.class);
  }

  public DefaultValueInteger getDefaultValueInteger() {
    return getFieldByClass(DefaultValueInteger.class);
  }

  public DefaultValueSmartfield getDefaultValueSmartfield() {
    return getFieldByClass(DefaultValueSmartfield.class);
  }

  public DefaultValueText getDefaultValueText() {
    return getFieldByClass(DefaultValueText.class);
  }

  public Entity getEntity() {
    return getFieldByClass(Entity.class);
  }

  public FeeGroup getFeeGroup() {
    return getFieldByClass(FeeGroup.class);
  }

  public Mandatory getMandatory() {
    return getFieldByClass(Mandatory.class);
  }

  public Maximum getMaximum() {
    return getFieldByClass(Maximum.class);
  }

  public Minimum getMinimum() {
    return getFieldByClass(Minimum.class);
  }

  public Smartfield getSmartfield() {
    return getFieldByClass(Smartfield.class);
  }

  public Type getType() {
    return getFieldByClass(Type.class);
  }

  public class AdditionalInformationUidProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public AdditionalInformationUidProperty() {
    }
  }

  public static class CodeBox extends AbstractCodeBoxData {
    private static final long serialVersionUID = 1L;

    public CodeBox() {
    }
  }

  public static class DefaultValueBoolean extends AbstractValueFieldData<Boolean> {
    private static final long serialVersionUID = 1L;

    public DefaultValueBoolean() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class DefaultValueDecimal extends AbstractValueFieldData<Double> {
    private static final long serialVersionUID = 1L;

    public DefaultValueDecimal() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class DefaultValueInteger extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public DefaultValueInteger() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class DefaultValueSmartfield extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public DefaultValueSmartfield() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class DefaultValueText extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public DefaultValueText() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class Entity extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Entity() {
    }

    /**
     * list of derived validation rules.
     */
 
  }

  public static class FeeGroup extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public FeeGroup() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class Mandatory extends AbstractValueFieldData<Boolean> {
    private static final long serialVersionUID = 1L;

    public Mandatory() {
    }
  }

  public static class Maximum extends AbstractValueFieldData<Double> {
    private static final long serialVersionUID = 1L;

    public Maximum() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class Minimum extends AbstractValueFieldData<Double> {
    private static final long serialVersionUID = 1L;

    public Minimum() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class Smartfield extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Smartfield() {
    }

    /**
     * list of derived validation rules.
     */
 
  }

  public static class Type extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Type() {
    }

    /**
     * list of derived validation rules.
     */
 
  }
}
