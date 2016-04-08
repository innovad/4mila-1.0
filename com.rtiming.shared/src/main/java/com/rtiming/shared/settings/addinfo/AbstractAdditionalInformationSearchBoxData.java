package com.rtiming.shared.settings.addinfo;

import org.eclipse.scout.rt.shared.data.form.fields.AbstractFormFieldData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;

public abstract class AbstractAdditionalInformationSearchBoxData extends AbstractFormFieldData {
  private static final long serialVersionUID = 1L;

  public AbstractAdditionalInformationSearchBoxData() {
  }

  public AdditionalInformation getAdditionalInformation() {
    return getFieldByClass(AdditionalInformation.class);
  }

  public Boolean getBoolean() {
    return getFieldByClass(Boolean.class);
  }

  public DecimalFrom getDecimalFrom() {
    return getFieldByClass(DecimalFrom.class);
  }

  public DecimalTo getDecimalTo() {
    return getFieldByClass(DecimalTo.class);
  }

  public Empty getEmpty() {
    return getFieldByClass(Empty.class);
  }

  public IntegerFrom getIntegerFrom() {
    return getFieldByClass(IntegerFrom.class);
  }

  public IntegerTo getIntegerTo() {
    return getFieldByClass(IntegerTo.class);
  }

  public Smartfield getSmartfield() {
    return getFieldByClass(Smartfield.class);
  }

  public Text getText() {
    return getFieldByClass(Text.class);
  }

  public static class AdditionalInformation extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public AdditionalInformation() {
    }

  }

  public static class Boolean extends AbstractValueFieldData<java.lang.Boolean> {
    private static final long serialVersionUID = 1L;

    public Boolean() {
    }

  }

  public static class DecimalFrom extends AbstractValueFieldData<Double> {
    private static final long serialVersionUID = 1L;

    public DecimalFrom() {
    }
  }

  public static class DecimalTo extends AbstractValueFieldData<Double> {
    private static final long serialVersionUID = 1L;

    public DecimalTo() {
    }
  }

  public static class Empty extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public Empty() {
    }
  }

  public static class IntegerFrom extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public IntegerFrom() {
    }
  }

  public static class IntegerTo extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public IntegerTo() {
    }
  }

  public static class Smartfield extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Smartfield() {
    }

  }

  public static class Text extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public Text() {
    }

  }
}
