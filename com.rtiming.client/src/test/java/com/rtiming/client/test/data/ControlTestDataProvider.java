/*******************************************************************************
 * Copyright (c) 2010 BSI Business Systems Integration AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the BSI CRM Software License v1.0
 * which accompanies this distribution as bsi-v10.html
 *
 * Contributors:
 *     BSI Business Systems Integration AG - initial API and implementation
 ******************************************************************************/
package com.rtiming.client.test.data;

import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.client.event.course.ControlForm;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.client.test.field.FieldValue;

public class ControlTestDataProvider extends AbstractTestDataProvider<ControlForm> {

  private Long eventNr;
  private Long typeUid;
  private String controlNo;

  public ControlTestDataProvider(Long eventNr, Long typeUid, String controlNo) throws ProcessingException {
    this.eventNr = eventNr;
    this.typeUid = typeUid;
    this.controlNo = controlNo;
    callInitializer();
  }

  @Override
  protected ControlForm createForm() throws ProcessingException {
    ControlForm control = new ControlForm();
    control.getEventField().setValue(eventNr);
    control.startNew();
    FormTestUtility.fillFormFields(control, new FieldValue(ControlForm.MainBox.TypeField.class, typeUid), new FieldValue(ControlForm.MainBox.NumberField.class, controlNo));
    control.doOk();
    return control;
  }

  @Override
  public void remove() throws ProcessingException {
    // handled by event
  }

  public Long getControlNr() throws ProcessingException {
    return getForm().getControlNr();
  }

}
