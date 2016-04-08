package com.rtiming.shared.ranking;

import org.eclipse.scout.rt.shared.data.form.fields.AbstractFormFieldData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;

public abstract class AbstractRankingBoxData extends AbstractFormFieldData {
  private static final long serialVersionUID = 1L;

  public AbstractRankingBoxData() {
  }

  public DecimalPlaces getDecimalPlaces() {
    return getFieldByClass(DecimalPlaces.class);
  }

  public Format getFormat() {
    return getFieldByClass(Format.class);
  }

  public Formula getFormula() {
    return getFieldByClass(Formula.class);
  }

  public FormulaType getFormulaType() {
    return getFieldByClass(FormulaType.class);
  }

  public Sorting getSorting() {
    return getFieldByClass(Sorting.class);
  }

  public TimePrecision getTimePrecision() {
    return getFieldByClass(TimePrecision.class);
  }

  public static class DecimalPlaces extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public DecimalPlaces() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class Format extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Format() {
    }

    /**
     * list of derived validation rules.
     */
 
  }

  public static class Formula extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public Formula() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class FormulaType extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public FormulaType() {
    }

    /**
     * list of derived validation rules.
     */
 
  }

  public static class Sorting extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Sorting() {
    }

    /**
     * list of derived validation rules.
     */
 
  }

  public static class TimePrecision extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public TimePrecision() {
    }

    /**
     * list of derived validation rules.
     */
     
  }
}
