/*******************************************************************************
 * Copyright (c) 2010 BSI Business Systems Integration AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the BSI CRM Software License v1.0
 * which accompanies this distribution as bsi-v10.html
 *
 * Contributors:
 *     BSI Business Systems Integration AG - initial API and implementation
 ******************************************************************************/
package com.rtiming.shared.test.completeness;

import java.io.Serializable;

/**
 * A result of {@link TestCompletenessUtility#testCompleteness()} containing error message and some statistics.
 */
public class CompletenessResult implements Serializable {

  private static final long serialVersionUID = 1L;
  private String message;
  private int missingTests;
  private int missingClazzes;

  /**
   * @param message
   * @param missingTests
   * @param missingClazzes
   */
  public CompletenessResult(String message, int missingTests, int missingClazzes) {
    super();
    this.message = message;
    this.missingTests = missingTests;
    this.missingClazzes = missingClazzes;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public int getMissingTests() {
    return missingTests;
  }

  public void setMissingTests(int missingTests) {
    this.missingTests = missingTests;
  }

  public int getMissingClazzes() {
    return missingClazzes;
  }

  public void setMissingClazzes(int missingClazzes) {
    this.missingClazzes = missingClazzes;
  }

}
