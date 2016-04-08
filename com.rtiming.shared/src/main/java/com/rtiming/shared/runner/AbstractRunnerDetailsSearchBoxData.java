package com.rtiming.shared.runner;

import java.util.Date;

import org.eclipse.scout.rt.shared.data.form.fields.AbstractFormFieldData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;

public abstract class AbstractRunnerDetailsSearchBoxData extends AbstractFormFieldData {
  private static final long serialVersionUID = 1L;

  public AbstractRunnerDetailsSearchBoxData() {
  }

  public BirthdateFrom getBirthdateFrom() {
    return getFieldByClass(BirthdateFrom.class);
  }

  public BirthdateTo getBirthdateTo() {
    return getFieldByClass(BirthdateTo.class);
  }

  public DefaultClazz getDefaultClazz() {
    return getFieldByClass(DefaultClazz.class);
  }

  public NationUid getNationUid() {
    return getFieldByClass(NationUid.class);
  }

  public Sex getSex() {
    return getFieldByClass(Sex.class);
  }

  public YearFrom getYearFrom() {
    return getFieldByClass(YearFrom.class);
  }

  public YearTo getYearTo() {
    return getFieldByClass(YearTo.class);
  }

  public static class BirthdateFrom extends AbstractValueFieldData<Date> {
    private static final long serialVersionUID = 1L;

    public BirthdateFrom() {
    }
  }

  public static class BirthdateTo extends AbstractValueFieldData<Date> {
    private static final long serialVersionUID = 1L;

    public BirthdateTo() {
    }
  }

  public static class DefaultClazz extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public DefaultClazz() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class NationUid extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public NationUid() {
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

  public static class YearFrom extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public YearFrom() {
    }
  }

  public static class YearTo extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public YearTo() {
    }
  }
}
