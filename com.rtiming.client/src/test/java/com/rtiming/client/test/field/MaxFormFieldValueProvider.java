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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import org.eclipse.scout.rt.client.ui.form.fields.bigdecimalfield.IBigDecimalField;
import org.eclipse.scout.rt.client.ui.form.fields.bigintegerfield.IBigIntegerField;
import org.eclipse.scout.rt.client.ui.form.fields.booleanfield.IBooleanField;
import org.eclipse.scout.rt.client.ui.form.fields.composer.internal.ComposerAttributeForm.MainBox.SequenceBox.ValueField;
import org.eclipse.scout.rt.client.ui.form.fields.datefield.IDateField;
import org.eclipse.scout.rt.client.ui.form.fields.integerfield.IIntegerField;
import org.eclipse.scout.rt.client.ui.form.fields.longfield.ILongField;
import org.eclipse.scout.rt.client.ui.form.fields.numberfield.INumberField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.IStringField;
import org.eclipse.scout.rt.platform.util.StringUtility;

/**
 * Inserts a given value into a given {@link ValueField}. If the value is <code>null</code>: inserts a biggest value of
 * the
 * ValueField. If the field does not have a biggest value, a default value is inserted.
 * <p>
 * Special cases (if no map entry is available):
 * <ul>
 * <li>Treeboxes: checks every node (independent of active/inactive)</li>
 * <li>Listboxes: checks every entry (independent of active/inactive)</li>
 * <li>Smartfield: takes the value of the first row from the corresponding lookup call (if row value is invalid, try up
 * to 9 others)</li>
 * <li>Datefield: takes the current date</li>
 * </ul>
 * <p>
 * 
 * @author Dominic Plangger
 */
public class MaxFormFieldValueProvider extends AbstractFormFieldValueProvider {

  @Override
  protected boolean getDefaultBoolean(IBooleanField field) {
    return true;
  }

  @Override
  protected Date getDefaultDate(IDateField field) {
    return new Date();
  }

  @Override
  protected BigDecimal getDefaultBigDecimalValue(IBigDecimalField field) {
    if (field.getMaxValue() != null) {
      return field.getMaxValue();
    }
    return BigDecimal.TEN;
  }

  @Override
  protected Number getDefaultDoubleValue(INumberField field) {
    if (field.getMaxValue() != null) {
      return field.getMaxValue();
    }
    return Double.MAX_VALUE;
  }

  @Override
  protected Integer getDefaultIntegerValue(IIntegerField field) {
    if (field.getMaxValue() != null) {
      return field.getMaxValue();
    }
    return Integer.MAX_VALUE / 100;
  }

  @Override
  protected Long getDefaultLongValue(ILongField field) {
    if (field.getMaxValue() != null) {
      return field.getMaxValue();
    }
    return Long.MAX_VALUE / 100000L;
  }

  @Override
  protected BigInteger getDefaultBigIntegerValue(IBigIntegerField field) {
    if (field.getMaxValue() != null) {
      return field.getMaxValue();
    }
    return BigInteger.TEN;
  }

  @Override
  protected String getDefaultStringValue(IStringField field) {
    int maxLength = field.getMaxLength();
    // limit max length for performance reasons
    if (maxLength > 4000) {
      maxLength = 4000;
    }
    return StringUtility.rpad("TestString", "abcdefghijklmnopqrstuvwxyz0123456789", maxLength);
  }

}
