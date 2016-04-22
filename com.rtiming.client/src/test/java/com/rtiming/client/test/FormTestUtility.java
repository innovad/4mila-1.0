/*******************************************************************************
 * Copyright (c) 2010 BSI Business Systems Integration AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the BSI CRM Software License v1.0
 * which accompanies this distribution as bsi-v10.html
 *
 * Contributors:
 *     BSI Business Systems Integration AG - initial API and implementation
 ******************************************************************************/
package com.rtiming.client.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.scout.rt.client.IClientSession;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn;
import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.client.ui.form.fields.IFormField;
import org.eclipse.scout.rt.client.ui.form.fields.IValueField;
import org.eclipse.scout.rt.client.ui.form.fields.bigdecimalfield.IBigDecimalField;
import org.eclipse.scout.rt.client.ui.form.fields.bigintegerfield.IBigIntegerField;
import org.eclipse.scout.rt.client.ui.form.fields.datefield.IDateField;
import org.eclipse.scout.rt.client.ui.form.fields.integerfield.IIntegerField;
import org.eclipse.scout.rt.client.ui.form.fields.listbox.AbstractListBoxFilterBox;
import org.eclipse.scout.rt.client.ui.form.fields.longfield.ILongField;
import org.eclipse.scout.rt.client.ui.form.fields.numberfield.INumberField;
import org.eclipse.scout.rt.client.ui.form.fields.tablefield.AbstractTableField;
import org.eclipse.scout.rt.client.ui.form.fields.tablefield.ITableField;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rtiming.client.test.field.FieldValue;
import com.rtiming.client.test.field.IFormFieldValueProvider;
import com.rtiming.client.test.field.MaxFormFieldValueProvider;

/**
 * Utility class for testing forms.
 */
public final class FormTestUtility {

  private static final Logger LOG = LoggerFactory.getLogger(FormTestUtility.class);

  private FormTestUtility() {
    // no public constructor for utility classes
  }

  /**
   * See description of {@link #fillFormFields(IForm, IFormFieldValueProvider, ArrayList<OrderedPair>, LinkedHashMap)}
   */
  public static Map<String, Object> fillFormFields(IForm form, IFormFieldValueProvider filler, FieldValue... valueMappings) throws ProcessingException {
    return fillFormFields(form, filler, null, valueMappings);
  }

  /**
   * See description of {@link #fillFormFields(IForm, IFormFieldValueProvider, ArrayList<OrderedPair>, LinkedHashMap)}
   */
  public static Map<String, Object> fillFormFields(IForm form, IFormFieldValueProvider filler, List<OrderedFieldPair> orderedFieldPairs, FieldValue... valueMappings) throws ProcessingException {
    LinkedHashMap<Class<? extends IValueField<?>>, Object> fixValues = new LinkedHashMap<Class<? extends IValueField<?>>, Object>();
    if (valueMappings != null) {
      for (FieldValue fieldValue : valueMappings) {
        if (fieldValue != null) {
          @SuppressWarnings("unchecked")
          Class<? extends IValueField<?>> fieldClass = (Class<? extends IValueField<?>>) fieldValue.getFieldClass();
          fixValues.put(fieldClass, fieldValue.getValue());
        }
      }
    }
    return fillFormFields(form, filler, orderedFieldPairs, fixValues);
  }

  /**
   * The {@link MaxFormFieldValueProvider} is used.
   * 
   * @See {@link #fillFormFields(IForm, IFormFieldValueProvider, ArrayList<OrderedPair>, LinkedHashMap)}
   */
  public static Map<String, Object> fillFormFields(IForm form, FieldValue... valueMappings) throws ProcessingException {
    return fillFormFields(form, new MaxFormFieldValueProvider(), null, valueMappings);
  }

  /**
   * @See {@link #fillFormFields(IForm, IFormFieldValueProvider, ArrayList<OrderedPair>, LinkedHashMap)}
   */
  public static Map<String, Object> fillFormFields(IForm form, IFormFieldValueProvider filler, List<OrderedFieldPair> orderedFieldPairs) throws ProcessingException {
    return fillFormFields(form, filler, orderedFieldPairs, (LinkedHashMap<Class<? extends IValueField<?>>, Object>) null);
  }

  /**
   * Inserts a value in every ValueField of <code> form </code>. If a ValueField contains an entry
   * in the map <code> fixValues </code>, then the value from the map is inserted into the field. If there is no entry
   * in the map, the corresponding fill method from the <code> fieldFiller </code> is called. On Default,
   * <code>fieldFiller</code> is an instance of {@link MaxFormFieldValueProvider}
   * <p>
   * Exception: if the parent of the ValueField is a TableField, the ValueField is ignored
   * <p>
   * 
   * @param fixValues
   *          The values from the <code> fixValues </code> argument are inserted at the beginning.
   *          A {@link LinkedHashMap} as map argument as the order of the entries matters (master fields must precede
   *          the slave fields). For convenience use {@link fillFormFields}, it creates
   *          automatically a {@link LinkedHashMap} out of the FieldValues
   *          <p>
   *          fails if not all values from the <code>fixValues</code> list could be set.
   *          </p>
   * @param orderedFieldPairs
   *          make sure all fields that should be are correctly ordered, if if they have been assigned randomly
   * @return map of field ids with assigned values, to be used with {@link #assertValueFields(Map, IForm)}
   * @throws ProcessingException
   */
  private static Map<String, Object> fillFormFields(IForm form, IFormFieldValueProvider valueProvider, List<OrderedFieldPair> orderedFieldPairs, LinkedHashMap<Class<? extends IValueField<?>>, Object> fixValues) throws ProcessingException {
    if (form == null) {
      return null;
    }

    assertNotNull("value provider must be set", valueProvider);

    if (fixValues == null) {
      fixValues = new LinkedHashMap<Class<? extends IValueField<?>>, Object>();
    }

    // Step 1: insert all values from the map of fixed values
    for (Entry<Class<? extends IValueField<?>>, Object> entry : fixValues.entrySet()) {
      Object value = entry.getValue();
      IValueField<?> formField = form.getFieldByClass(entry.getKey());
      valueProvider.fillValueField(formField, value);
    }

    // step 2: sort fields, first non-slave fields, then slave fields in transitive order
    // e.g. f1 has no master and is master of f2, f2 is master of f3 and f3 is master of f4
    // then the master list is: {f1}, and slave list: {f2->f3->f4}
    List<IValueField<?>> masterList = new LinkedList<IValueField<?>>();
    List<IValueField<?>> slaveList = new LinkedList<IValueField<?>>();
    for (IFormField formField : form.getAllFields()) {
      if (formField.getParentField() instanceof AbstractTableField || !(formField instanceof IValueField) || fixValues.containsKey(formField.getClass())) {
        continue;
      }
      IValueField<?> valueField = (IValueField<?>) formField;
      if (valueField.getMasterField() != null) {
        int index = slaveList.indexOf(valueField.getMasterField());
        if (index > 0) {
          slaveList.add(index + 1, valueField);
        }
        else {
          slaveList.add(0, valueField);
        }
      }
      else {
        masterList.add(valueField);
      }
    }
    // Step 3: insert default values into the master fields
    for (IValueField<?> valueField : masterList) {
      valueProvider.fillValueField(valueField, null);
    }

    // Step 4: insert default values into the slave fields
    for (IValueField<?> valueField : slaveList) {
      valueProvider.fillValueField(valueField, null);
    }

    // Step 5: make sure all fields that should be are correctly ordered, if if they have been
    //         assigned randomly
    if (orderedFieldPairs != null) {
      for (OrderedFieldPair orderedFieldPair : orderedFieldPairs) {
        ensureAscending(form, orderedFieldPair);
      }
    }

    // Step 6: collect all values. Collecting is done at last because subsequent value changes
    //         might affect previously changed value fields
    Map<String, Object> setFieldValues = collectFieldValues(form);

    // Step 7: check whether all fixed values are really set as desired
    for (Entry<Class<? extends IValueField<?>>, Object> fixValue : fixValues.entrySet()) {
      IFormField field = form.getFieldByClass(fixValue.getKey());
      if (!(field instanceof IValueField<?>)) {
        fail("Field [" + field.getClass() + "] could not be set as it is not a value field.");
      }
      if (!Long.valueOf(0).equals(fixValue.getValue()) && !Integer.valueOf(0).equals(fixValue.getValue())) {
        IValueField<?> valueField = (IValueField<?>) field;
        if (fixValue.getValue() != null && !fixValue.getValue().equals(valueField.getValue())) {
          // don't check for NULL / 0;
          assertEquals("Value has been set for field: " + field.getClass(), fixValue.getValue(), valueField.getValue());
        }

      }
    }

    // Step 8: Fill editable Table Fields (AMO) - written especially for code box
    for (IFormField formField : form.getAllFields()) {
      if (formField instanceof ITableField<?>) {
        ITableField<?> tableField = (ITableField<?>) formField;
        for (ITableRow row : tableField.getTable().getRows()) {
          for (IColumn<?> c : tableField.getTable().getColumns()) {
            IFormField editField = c.prepareEdit(row);
            if (editField instanceof IValueField<?>) {
              valueProvider.fillValueField((IValueField<?>) editField, null);
              row.setStatusUpdated();
            }
            c.completeEdit(row, editField);
          }
        }
      }
    }

    return setFieldValues;
  }

  /**
   * Goes through every value of <code> fieldValues </code> and checks whether the corresponding field in
   * <code> form </code> has the same value
   * 
   * @param expectedFieldValues
   *          the values expected, map of field ids with assigned values, created by {@link fillFormFields}
   * @param form
   *          the form to be checked
   */
  public static void assertValueFields(Map<String, Object> expectedFieldValues, IForm form) {
    if (form == null || expectedFieldValues == null) {
      fail("form or fieldValues argument is null");
      return;
    }
    for (Map.Entry<String, Object> fieldValue : expectedFieldValues.entrySet()) {
      IFormField formField = form.getFieldById(fieldValue.getKey());
      if (!(formField instanceof IValueField<?>)) {
        continue;
      }
      IValueField<?> field = (IValueField<?>) formField;
      Object actualValue = field.getValue();
      if (field.getHolderType().isArray() && actualValue != null && fieldValue.getValue() != null) {
        Set<Object> actual = CollectionUtility.hashSet((Object[]) actualValue);
        Set<Object> expected = CollectionUtility.hashSet((Object[]) fieldValue.getValue());
        assertEquals("Unexpected values in field '" + field.getLabel() + "' [" + field.getClass().getName() + "]", expected, actual);
      }
      //DB may allow less fraction digits then a Double has, but still enough to return a number which gets displayed correctly
      else if (field instanceof INumberField && actualValue != null && fieldValue.getValue() != null) {
        int p = ((INumberField) field).getMaxIntegerDigits();
        assertEquals("Unexpected value in field '" + field.getLabel() + "' [" + field.getClass().getName() + "]", (Double) fieldValue.getValue(), (Double) actualValue, Math.pow(0.1, p + 1));
      }
      else {
        assertEquals("Unexpected value in field '" + field.getLabel() + "' [" + field.getClass().getName() + "]", fieldValue.getValue(), actualValue);
      }
    }
  }

  private static Map<String, Object> collectFieldValues(IForm form) {
    Map<String, Object> result = new HashMap<String, Object>();
    for (IFormField formField : form.getAllFields()) {
      if (formField.getParentField() instanceof AbstractTableField) {
        continue;
      }
      if (formField.getParentField() instanceof AbstractListBoxFilterBox) {
        continue;
      }
      if (formField instanceof IValueField && formField.isEnabled() && ClientTestingUtility.isFieldVisible(formField)) {
        IValueField<?> valueField = (IValueField<?>) formField;
        result.put(valueField.getFieldId(), valueField.getValue());
      }
    }
    return result;
  }

  /**
   * class for pairs of fields, of which the first must be smaller than the second by definition
   */
  public static class OrderedFieldPair {
    private final Class<? extends IValueField<?>> m_firstField;
    private final Class<? extends IValueField<?>> m_secondField;

    Class<? extends IValueField<?>> getFirstField() {
      return m_firstField;
    }

    Class<? extends IValueField<?>> getSecondField() {
      return m_secondField;
    }

    /**
     * @param firstField
     *          class of the first field, is supposed to be <code>null</code> or smaller than the first field
     * @param secondField
     *          class of the second field; is supposed to be <code>null</code> or larger than the first field
     */
    public OrderedFieldPair(Class<? extends IValueField<?>> firstField, Class<? extends IValueField<?>> secondField) {
      super();
      m_firstField = firstField;
      m_secondField = secondField;
    }
  }

  /**
   * Ascertains that the second field has a higher value than the first. <code>null</code> as value is always allowed.
   * 
   * @param form
   *          the form in which the fields are read or edited, so the condition is met.
   * @return map of field ids with assigned values, to be used with {@link #assertValueFields(Map, IForm)}; must be read
   *         from the last call;
   */
  private static void ensureAscending(IForm form, OrderedFieldPair orderedPair) {
    IValueField<?> field1 = form.getFieldByClass(orderedPair.getFirstField());
    IValueField<?> field2 = form.getFieldByClass(orderedPair.getSecondField());
    if (field1.getValue() == null || field2.getValue() == null) {
      return;
    }
    assertTrue("both field values must be of the same type: " + field1.getClass() + " and " + field2.getClass(), field1.getValue().getClass().equals(field2.getValue().getClass()));

    if (field1 instanceof IDateField) {
      IDateField dateField1 = (IDateField) field1;
      IDateField dateField2 = (IDateField) field2;
      dateField2.setValue(DateUtility.addDays(dateField1.getValue(), 1d));
    }
    else if (field1 instanceof IBigDecimalField) {
      IBigDecimalField bigDecimalField1 = (IBigDecimalField) field1;
      IBigDecimalField bigDecimalField2 = (IBigDecimalField) field2;
      bigDecimalField2.setValue(bigDecimalField1.getValue().add(BigDecimal.valueOf(0.5d)));
    }
    else if (field1 instanceof INumberField) {
      INumberField doubleField1 = (INumberField) field1;
      INumberField doubleField2 = (INumberField) field2;
      doubleField2.setValue(((Double) doubleField1.getValue()) + 0.5d); // TODO MIG
    }
    else if (field1 instanceof IIntegerField) {
      IIntegerField integerField1 = (IIntegerField) field1;
      IIntegerField integerField2 = (IIntegerField) field2;
      integerField2.setValue(integerField1.getValue() + 1);
    }
    else if (field1 instanceof ILongField) {
      ILongField longField1 = (ILongField) field1;
      ILongField longField2 = (ILongField) field2;
      longField2.setValue(Long.valueOf(longField1.getValue() + 1L));

    }
    else if (field1 instanceof IBigIntegerField) {
      IBigIntegerField bigIntegerField1 = (IBigIntegerField) field1;
      IBigIntegerField bigIntegerField2 = (IBigIntegerField) field2;
      bigIntegerField2.setValue(bigIntegerField1.getValue().add(BigInteger.valueOf(1L)));
    }
    else {
      fail("type not supported: " + field1.getClass());
    }
    return;
  }

  /**
   * Closes all open forms on the current session
   * 
   * @throws ProcessingException
   */
  public static void closeAllBlockingForms() throws ProcessingException {
    IClientSession session = (IClientSession) IClientSession.CURRENT.get();
    if (session != null) {
      List<IForm> forms = session.getDesktop().getDialogStack();
      for (IForm form : forms) {
        form.doClose();
      }
    }
    else {
      LOG.warn("Trying to close all forms, but ClientSession is null");
    }
  }

}
