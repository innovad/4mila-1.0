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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.rtiming.client.test.FormTestUtility.OrderedFieldPair;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.client.test.field.IFormFieldValueProvider;
import com.rtiming.client.test.field.MaxFormFieldValueProvider;
import com.rtiming.client.test.field.MinFormFieldValueProvider;
import com.rtiming.client.test.field.RandFormFieldValueProvider;

/**
 * <p>
 * Abstract class to support the test of forms.
 * </p>
 * <p>
 * After implementing some abstract methods (see {@link CommunicationFormTest} for an example), three tests are
 * performed. They are all similar, but use random values, minimum values and maximum values. They do the following
 * </p>
 * <ol>
 * <li>create an empty form, fill it with values
 * <ul>
 * <li><b>You have to </b>implement {@link #getStartedForm()} to create the form.</li>
 * <li>You can implement {@link #getFixedValues()} to define some fixed values (e.g. person nr)</li>
 * <li>You can also implement {@link #getOrderedFieldPairs()}, if some fields must have ordered values, most often this
 * applies to date from an date to</li>
 * </ul>
 * </li>
 * <li>close and save the form</li>
 * <li>reopen the form and check the values loaded with those stored before
 * <ul>
 * <li><b>You have to </b>implement {@link #getModifyForm()} to open the form</li>
 * <li>You can implement {@link #getFieldIdsToIgnore()} to ignore some fields from being checked. This might be
 * necessary if fields are changed when stored or openend.</li>
 * </ul>
 * </li>
 * </ol>
 * Finally you <b>have to</b> implement {@link #cleanup()} to delete the row created.
 * <p>
 * 
 * @param <T>
 *          the form to be tested
 * @author Andreas Ottiger
 */
public abstract class AbstractFormTest<T extends AbstractForm> {
  private T m_form = null;

  /**
   * This is the form to be tested. For each test it is an empty, new form, the one supplied by
   * {@link #getStartedForm()}.
   */
  protected final T getForm() {
    return m_form;
  }

  @Before
  public void setUpForm() throws ProcessingException {
    m_form = getStartedForm();
  }

  @After
  public void tearDownForm() throws ProcessingException {
    m_form.doReset();
    m_form.doClose();
  }

  @After
  public abstract void cleanup() throws ProcessingException;

  /**
   * test case: insert max value in every value field, save, reload, and check if the values are unchanged
   */
  @Test
  public void testAllValueFieldsMax() throws ProcessingException {
    doTest(new MaxFormFieldValueProvider());
  }

  /**
   * test case: insert min value in every value field
   */
  @Test
  public void testAllValueFieldsMin() throws ProcessingException {
    doTest(new MinFormFieldValueProvider());
  }

  /**
   * test case: insert random value in every value field
   */
  @Test
  public void testAllValueFieldsRand() throws ProcessingException {
    doTest(new RandFormFieldValueProvider());
  }

  @Test
  public void testFields() throws Exception {
    FMilaClientTestUtility.testFormFields(getStartedForm());
  }

  private void doTest(IFormFieldValueProvider valueProvider) throws ProcessingException {
    ArrayList<OrderedFieldPair> orderedPairs = getOrderedFieldPairs();

    Map<String, Object> insertedValues = FormTestUtility.fillFormFields(m_form, valueProvider, orderedPairs, getFixedValues().toArray(new FieldValue[0]));
    m_form.doOk();

    T modifyForm = getModifyForm();
    List<String> fieldIdsToIgnore = getFieldIdsToIgnore();
    for (String fieldId : fieldIdsToIgnore) {
      insertedValues.remove(fieldId);
    }
    FormTestUtility.assertValueFields(insertedValues, modifyForm);

    modifyForm.doCancel();
  }

  /**
   * @return a list of of field ids, that should not be checked after loading the form again
   */
  protected List<String> getFieldIdsToIgnore() {
    return Collections.emptyList();
  }

  /**
   * Overwrite this if you want to specify fixed values for the form
   * 
   * @return an array of fixed values
   */
  protected List<FieldValue> getFixedValues() throws ProcessingException {
    return Collections.emptyList();
  }

  /**
   * Overwrite this to return a list of ordered pairs, if this constraint exists on the form to be tested
   * 
   * @return the pairs of fields that should have ordered values
   */
  protected ArrayList<OrderedFieldPair> getOrderedFieldPairs() {
    return null;
  }

  /**
   * @return the form to be tested (a new instance), should already be started with <code>startInternal()</code> and
   *         data should have been loaded. This is typically done by a method named <code>startModify()</code> on the
   *         form.
   */
  protected abstract T getModifyForm() throws ProcessingException;

  /**
   * @return the form to be tested (a new instance), should already be started with <code>startInternal()</code>.
   *         Typically a method <code>startNew()</code> should be called.
   */
  protected abstract T getStartedForm() throws ProcessingException;

}
