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

import org.eclipse.scout.rt.client.ui.form.fields.IValueField;

/**
 * Class used to associate a value with an IValueField
 * 
 * @author Dominic Plangger
 */
@SuppressWarnings("rawtypes")
public class FieldValue {

  private final Class<? extends IValueField> m_fieldClass;
  private final Object m_value;

  public FieldValue(Class<? extends IValueField<?>> fieldClass, Object value) {
    m_fieldClass = fieldClass;
    m_value = value;
  }

  public Class<? extends IValueField> getFieldClass() {
    return m_fieldClass;
  }

  public Object getValue() {
    return m_value;
  }
}
