/*******************************************************************************
 * Copyright (c) 2010 BSI Business Systems Integration AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the BSI CRM Software License v1.0
 * which accompanies this distribution as bsi-v10.html
 *
 * Contributors:
 *     BSI Business Systems Integration AG - initial API and implementation
 ******************************************************************************/
package com.rtiming.client.test.field;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn;
import org.eclipse.scout.rt.client.ui.basic.tree.ITreeNode;
import org.eclipse.scout.rt.client.ui.basic.tree.ITreeVisitor;
import org.eclipse.scout.rt.client.ui.form.fields.IValueField;
import org.eclipse.scout.rt.client.ui.form.fields.bigdecimalfield.IBigDecimalField;
import org.eclipse.scout.rt.client.ui.form.fields.bigintegerfield.IBigIntegerField;
import org.eclipse.scout.rt.client.ui.form.fields.booleanfield.IBooleanField;
import org.eclipse.scout.rt.client.ui.form.fields.button.IRadioButton;
import org.eclipse.scout.rt.client.ui.form.fields.datefield.IDateField;
import org.eclipse.scout.rt.client.ui.form.fields.integerfield.IIntegerField;
import org.eclipse.scout.rt.client.ui.form.fields.listbox.IListBox;
import org.eclipse.scout.rt.client.ui.form.fields.longfield.ILongField;
import org.eclipse.scout.rt.client.ui.form.fields.numberfield.INumberField;
import org.eclipse.scout.rt.client.ui.form.fields.radiobuttongroup.IRadioButtonGroup;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.ISmartField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.IStringField;
import org.eclipse.scout.rt.client.ui.form.fields.treebox.ITreeBox;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.BooleanUtility;
import org.eclipse.scout.rt.platform.util.TypeCastUtility;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;

import com.rtiming.client.common.ui.fields.AbstractEmailField;
import com.rtiming.client.test.ClientTestingUtility;

public abstract class AbstractFormFieldValueProvider implements IFormFieldValueProvider {

  public AbstractFormFieldValueProvider() {
    super();
  }

  @Override
  public void fillValueField(IValueField<?> field, Object value) {
    if (field != null && field.isEnabled() && ClientTestingUtility.isFieldVisible(field)) {
      if (field instanceof IBooleanField) {
        fillBooleanField((IBooleanField) field, value);
      }
      else if (field instanceof IDateField) {
        fillDateField((IDateField) field, value);
      }
      else if (field instanceof IBigDecimalField) {
        fillBigDecimalField((IBigDecimalField) field, value);
      }
      else if (field instanceof INumberField) {
        fillDoubleField((INumberField) field, value);
      }
      else if (field instanceof IIntegerField) {
        fillIntegerField((IIntegerField) field, value);
      }
      else if (field instanceof ILongField) {
        fillLongField((ILongField) field, value);
      }
      else if (field instanceof IBigIntegerField) {
        fillBigIntegerField((IBigIntegerField) field, value);
      }
      else if (field instanceof AbstractEmailField) {
        fillEmailField((AbstractEmailField) field, value);
      }
      else if (field instanceof IStringField) {
        fillStringField((IStringField) field, value);
      }
      else if (field instanceof IRadioButtonGroup) {
        fillRadioButtonGroup((IRadioButtonGroup<?>) field, value);
      }
      else if (field instanceof ISmartField) {
        fillSmartField((ISmartField<?>) field, value);
      }
      else if (field instanceof IListBox) {
        fillListBox((IListBox<?>) field, value);
      }
      else if (field instanceof ITreeBox) {
        fillTreeBox((ITreeBox<?>) field, value);
      }
    }
  }

  private void fillBooleanField(IBooleanField field, Object value) {
    Boolean booleanValue = null;
    try {
      booleanValue = TypeCastUtility.castValue(value, Boolean.class);
    }
    catch (RuntimeException e) {
      // nop
    }
    field.setValue(BooleanUtility.nvl(booleanValue, getDefaultBoolean(field)));
  }

  private void fillDateField(IDateField field, Object value) {
    Date dateValue = null;
    try {
      dateValue = TypeCastUtility.castValue(value, Date.class);
    }
    catch (RuntimeException e) {
      // nop
    }
    if (dateValue == null) {
      dateValue = getDefaultDate(field);
    }
    if (field.isHasTime()) {
      field.setValue(dateValue);
    }
    else {
      field.setValue(DateUtility.truncDate(dateValue));
    }
  }

  private void fillBigDecimalField(IBigDecimalField field, Object value) {
    BigDecimal bigDecimalValue = null;
    try {
      bigDecimalValue = TypeCastUtility.castValue(value, BigDecimal.class);
    }
    catch (RuntimeException e) {
      // nop
    }
    if (bigDecimalValue == null) {
      bigDecimalValue = getDefaultBigDecimalValue(field);
    }
    field.setValue(bigDecimalValue);
  }

  private void fillDoubleField(INumberField field, Object value) {
    Number doubleValue = null;
    try {
      doubleValue = TypeCastUtility.castValue(value, Double.class);
    }
    catch (RuntimeException e) {
      // nop
    }
    if (doubleValue == null) {
      doubleValue = getDefaultDoubleValue(field);
    }
    field.setValue(doubleValue);
  }

  private void fillIntegerField(IIntegerField field, Object value) {
    Integer integerValue = null;
    try {
      integerValue = TypeCastUtility.castValue(value, Integer.class);
    }
    catch (RuntimeException e) {
      // nop
    }
    if (integerValue == null) {
      integerValue = getDefaultIntegerValue(field);
    }
    field.setValue(integerValue);
  }

  private void fillLongField(ILongField field, Object value) {
    Long longValue = null;
    try {
      longValue = TypeCastUtility.castValue(value, Long.class);
    }
    catch (RuntimeException e) {
      // nop
    }
    if (longValue == null) {
      longValue = getDefaultLongValue(field);
    }
    field.setValue(longValue);
  }

  private void fillBigIntegerField(IBigIntegerField field, Object value) {
    BigInteger bigIntegerValue = null;
    try {
      bigIntegerValue = TypeCastUtility.castValue(value, BigInteger.class);
    }
    catch (RuntimeException e) {
      // nop
    }
    if (bigIntegerValue == null) {
      bigIntegerValue = getDefaultBigIntegerValue(field);
    }
    field.setValue(bigIntegerValue);
  }

  private void fillStringField(IStringField field, Object value) {
    String stringValue = null;
    try {
      stringValue = TypeCastUtility.castValue(value, String.class);
    }
    catch (RuntimeException e) {
      // nop
    }
    if (stringValue == null) {
      stringValue = getDefaultStringValue(field);
    }
    field.setValue(stringValue);
  }

  private void fillEmailField(AbstractEmailField field, Object value) {
    String stringValue = null;
    try {
      stringValue = TypeCastUtility.castValue(value, String.class);
    }
    catch (RuntimeException e) {
      // nop
    }
    if (stringValue == null) {
      stringValue = getDefaultEmailValue(field);
    }
    field.setValue(stringValue);
  }

  private <T> void fillRadioButtonGroup(IRadioButtonGroup<T> field, Object value) {
    Object radioButtonValue = value;
    if (radioButtonValue == null) {
      radioButtonValue = getDefaultRadioButtonValue(field);
    }
    @SuppressWarnings("unchecked")
    T typedRadioButtonValue = (T) radioButtonValue;
    field.setValue(typedRadioButtonValue);
  }

  private <T> void fillSmartField(ISmartField<T> field, Object value) {
    Object smartFieldValue = value;
    if (value == null) {
      smartFieldValue = getDefaultSmartFieldValue(field);
    }
// TODO MIG    
//    if (field.isAllowCustomText()) {
//      smartFieldValue = TypeCastUtility.castValue(smartFieldValue, field.getHolderType());
//    }
    @SuppressWarnings("unchecked")
    T typesSmartField = (T) smartFieldValue;
    field.setValue(typesSmartField);
  }

  private <T> void fillListBox(IListBox<T> field, Object value) {
    Object[] listBoxValue = null;
    if (value != null) {
      if (value.getClass().isArray()) {
        if (field.getHolderType().getComponentType().isAssignableFrom(value.getClass().getComponentType())) {
          listBoxValue = (Object[]) value;
        }
      }
      else if (field.getHolderType().getComponentType().isAssignableFrom(value.getClass())) {
        listBoxValue = (Object[]) Array.newInstance(field.getHolderType().getComponentType(), 1);
        listBoxValue[0] = value;
      }
    }
    if (listBoxValue == null) {
      listBoxValue = getDefaultListBoxValue(field);
    }
    @SuppressWarnings("unchecked")
    T[] typedListBoxValue = (T[]) listBoxValue;
    field.setValue(new HashSet<>(Arrays.asList(typedListBoxValue)));
  }

  private <T> void fillTreeBox(ITreeBox<T> field, Object value) {
    Object[] treeBoxValue = null;
    if (value != null) {
      if (value.getClass().isArray()) {
        if (field.getHolderType().getComponentType().isAssignableFrom(value.getClass().getComponentType())) {
          treeBoxValue = (Object[]) value;
        }
      }
      else if (field.getHolderType().getComponentType().isAssignableFrom(value.getClass())) {
        treeBoxValue = (Object[]) Array.newInstance(field.getHolderType().getComponentType(), 1);
        treeBoxValue[0] = value;
      }
    }
    if (treeBoxValue == null) {
      treeBoxValue = getDefaultTreeBoxValue(field);
    }
    @SuppressWarnings("unchecked")
    T[] typedTreeBoxValue = (T[]) treeBoxValue;
    field.setValue(new HashSet<>(Arrays.asList(typedTreeBoxValue)));
  }

  protected abstract boolean getDefaultBoolean(IBooleanField field);

  protected abstract Date getDefaultDate(IDateField field);

  protected abstract BigDecimal getDefaultBigDecimalValue(IBigDecimalField field);

  protected abstract Number getDefaultDoubleValue(INumberField field);

  protected abstract Integer getDefaultIntegerValue(IIntegerField field);

  protected abstract Long getDefaultLongValue(ILongField field);

  protected abstract BigInteger getDefaultBigIntegerValue(IBigIntegerField field);

  protected abstract String getDefaultStringValue(IStringField field);

  protected abstract String getDefaultEmailValue(AbstractEmailField field);

  protected Object getDefaultRadioButtonValue(IRadioButtonGroup<?> field) {
    List<? extends IRadioButton<?>> buttons = field.getButtons();
    if (buttons != null) {
      for (IRadioButton button : buttons) {
        if (button.isVisible() && button.isEnabled()) {
          return button.getRadioValue();
        }
      }
    }
    return null;
  }

  protected Object getDefaultSmartFieldValue(ISmartField<?> field) {
    try {
      List<? extends ILookupRow<?>> lookupRows = field.callBrowseLookup(null, 10);
      if (lookupRows != null) {
        for (ILookupRow row : lookupRows) {
          if (row.isActive() && row.isEnabled()) {
            return row.getKey();
          }
        }
      }
    }
    catch (ProcessingException e) {
      throw new RuntimeException("Unexpected exception while filling smartfield [" + field.getClass().getName() + "]", e);
    }
    return null;
  }

  protected Object[] getDefaultListBoxValue(IListBox<?> field) {
    IColumn<?> keyColumn = field.getTable().getColumnSet().getColumn(0);
    @SuppressWarnings("unchecked")
    IColumn<Boolean> activeColumn = field.getTable().getColumnSet().getColumn(2);
    List<ITableRow> rows = activeColumn.findRows(true);
    List<ITableRow> enabledRows = new ArrayList<ITableRow>();
    for (ITableRow row : rows) {
      if (row.isEnabled()) {
        enabledRows.add(row);
      }
    }
    return keyColumn.getValues(enabledRows).toArray(new Long[0]);
  }

  protected Object[] getDefaultTreeBoxValue(ITreeBox<?> field) {
    final Set<Object> keySet = new HashSet<Object>();
    ITreeVisitor v = new ITreeVisitor() {
      @Override
      public boolean visit(ITreeNode node) {
        if (node.getPrimaryKey() != null) {
          keySet.add(node.getPrimaryKey());
        }
        return true;
      }
    };
    field.getTree().visitNode(field.getTree().getRootNode(), v);
    Object[] array = (Object[]) Array.newInstance(field.getHolderType().getComponentType(), keySet.size());
    return keySet.toArray(array);
  }

}
