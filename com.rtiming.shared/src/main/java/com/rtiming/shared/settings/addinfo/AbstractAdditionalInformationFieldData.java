package com.rtiming.shared.settings.addinfo;

import org.eclipse.scout.rt.shared.data.form.fields.tablefield.AbstractTableFieldData;

public abstract class AbstractAdditionalInformationFieldData extends AbstractTableFieldData {
  private static final long serialVersionUID = 1L;

  public AbstractAdditionalInformationFieldData() {
  }

  public static final int ADDITIONAL_INFORMATION_UID_COLUMN_ID = 0;
  public static final int TYPE_COLUMN_ID = 1;
  public static final int VALUE_COLUMN_ID = 2;
  public static final int INTEGER_COLUMN_ID = 3;
  public static final int DECIMAL_COLUMN_ID = 4;
  public static final int TEXT_COLUMN_ID = 5;
  public static final int MINIMUM_COLUMN_ID = 6;
  public static final int MAXIMUM_COLUMN_ID = 7;
  public static final int FEE_GROUP_COLUMN_ID = 8;
  public static final int DEFAULT_VALUE_INTEGER_COLUMN_ID = 9;
  public static final int DEFAULT_VALUE_DECIMAL_COLUMN_ID = 10;
  public static final int DEFAULT_VALUE_TEXT_COLUMN_ID = 11;
  public static final int MANDATORY_COLUMN_ID = 12;

  public void setAdditionalInformationUid(int row, Long additionalInformationUid) {
    setValueInternal(row, ADDITIONAL_INFORMATION_UID_COLUMN_ID, additionalInformationUid);
  }

  public Long getAdditionalInformationUid(int row) {
    return (Long) getValueInternal(row, ADDITIONAL_INFORMATION_UID_COLUMN_ID);
  }

  public void setType(int row, Long type) {
    setValueInternal(row, TYPE_COLUMN_ID, type);
  }

  public Long getType(int row) {
    return (Long) getValueInternal(row, TYPE_COLUMN_ID);
  }

  public void setValue(int row, String value) {
    setValueInternal(row, VALUE_COLUMN_ID, value);
  }

  public String getValue(int row) {
    return (String) getValueInternal(row, VALUE_COLUMN_ID);
  }

  public void setInteger(int row, Long integer) {
    setValueInternal(row, INTEGER_COLUMN_ID, integer);
  }

  public Long getInteger(int row) {
    return (Long) getValueInternal(row, INTEGER_COLUMN_ID);
  }

  public void setDecimal(int row, Double decimal) {
    setValueInternal(row, DECIMAL_COLUMN_ID, decimal);
  }

  public Double getDecimal(int row) {
    return (Double) getValueInternal(row, DECIMAL_COLUMN_ID);
  }

  public void setText(int row, String text) {
    setValueInternal(row, TEXT_COLUMN_ID, text);
  }

  public String getText(int row) {
    return (String) getValueInternal(row, TEXT_COLUMN_ID);
  }

  public void setMinimum(int row, Double minimum) {
    setValueInternal(row, MINIMUM_COLUMN_ID, minimum);
  }

  public Double getMinimum(int row) {
    return (Double) getValueInternal(row, MINIMUM_COLUMN_ID);
  }

  public void setMaximum(int row, Double maximum) {
    setValueInternal(row, MAXIMUM_COLUMN_ID, maximum);
  }

  public Double getMaximum(int row) {
    return (Double) getValueInternal(row, MAXIMUM_COLUMN_ID);
  }

  public void setFeeGroup(int row, Long feeGroup) {
    setValueInternal(row, FEE_GROUP_COLUMN_ID, feeGroup);
  }

  public Long getFeeGroup(int row) {
    return (Long) getValueInternal(row, FEE_GROUP_COLUMN_ID);
  }

  public void setDefaultValueInteger(int row, Long defaultValueInteger) {
    setValueInternal(row, DEFAULT_VALUE_INTEGER_COLUMN_ID, defaultValueInteger);
  }

  public Long getDefaultValueInteger(int row) {
    return (Long) getValueInternal(row, DEFAULT_VALUE_INTEGER_COLUMN_ID);
  }

  public void setDefaultValueDecimal(int row, Double defaultValueDecimal) {
    setValueInternal(row, DEFAULT_VALUE_DECIMAL_COLUMN_ID, defaultValueDecimal);
  }

  public Double getDefaultValueDecimal(int row) {
    return (Double) getValueInternal(row, DEFAULT_VALUE_DECIMAL_COLUMN_ID);
  }

  public void setDefaultValueText(int row, String defaultValueText) {
    setValueInternal(row, DEFAULT_VALUE_TEXT_COLUMN_ID, defaultValueText);
  }

  public String getDefaultValueText(int row) {
    return (String) getValueInternal(row, DEFAULT_VALUE_TEXT_COLUMN_ID);
  }

  public void setMandatory(int row, Boolean mandatory) {
    setValueInternal(row, MANDATORY_COLUMN_ID, mandatory);
  }

  public Boolean getMandatory(int row) {
    return (Boolean) getValueInternal(row, MANDATORY_COLUMN_ID);
  }

  @Override
  public int getColumnCount() {
    return 13;
  }

  @Override
  public Object getValueAt(int row, int column) {
    switch (column) {
      case ADDITIONAL_INFORMATION_UID_COLUMN_ID:
        return getAdditionalInformationUid(row);
      case TYPE_COLUMN_ID:
        return getType(row);
      case VALUE_COLUMN_ID:
        return getValue(row);
      case INTEGER_COLUMN_ID:
        return getInteger(row);
      case DECIMAL_COLUMN_ID:
        return getDecimal(row);
      case TEXT_COLUMN_ID:
        return getText(row);
      case MINIMUM_COLUMN_ID:
        return getMinimum(row);
      case MAXIMUM_COLUMN_ID:
        return getMaximum(row);
      case FEE_GROUP_COLUMN_ID:
        return getFeeGroup(row);
      case DEFAULT_VALUE_INTEGER_COLUMN_ID:
        return getDefaultValueInteger(row);
      case DEFAULT_VALUE_DECIMAL_COLUMN_ID:
        return getDefaultValueDecimal(row);
      case DEFAULT_VALUE_TEXT_COLUMN_ID:
        return getDefaultValueText(row);
      case MANDATORY_COLUMN_ID:
        return getMandatory(row);
      default:
        return null;
    }
  }

  @Override
  public void setValueAt(int row, int column, Object value) {
    switch (column) {
      case ADDITIONAL_INFORMATION_UID_COLUMN_ID:
        setAdditionalInformationUid(row, (Long) value);
        break;
      case TYPE_COLUMN_ID:
        setType(row, (Long) value);
        break;
      case VALUE_COLUMN_ID:
        setValue(row, (String) value);
        break;
      case INTEGER_COLUMN_ID:
        setInteger(row, (Long) value);
        break;
      case DECIMAL_COLUMN_ID:
        setDecimal(row, (Double) value);
        break;
      case TEXT_COLUMN_ID:
        setText(row, (String) value);
        break;
      case MINIMUM_COLUMN_ID:
        setMinimum(row, (Double) value);
        break;
      case MAXIMUM_COLUMN_ID:
        setMaximum(row, (Double) value);
        break;
      case FEE_GROUP_COLUMN_ID:
        setFeeGroup(row, (Long) value);
        break;
      case DEFAULT_VALUE_INTEGER_COLUMN_ID:
        setDefaultValueInteger(row, (Long) value);
        break;
      case DEFAULT_VALUE_DECIMAL_COLUMN_ID:
        setDefaultValueDecimal(row, (Double) value);
        break;
      case DEFAULT_VALUE_TEXT_COLUMN_ID:
        setDefaultValueText(row, (String) value);
        break;
      case MANDATORY_COLUMN_ID:
        setMandatory(row, (Boolean) value);
        break;
    }
  }
}
