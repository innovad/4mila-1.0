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

import java.util.List;

import org.eclipse.scout.rt.client.ui.desktop.IDesktop;
import org.eclipse.scout.rt.client.ui.desktop.outline.AbstractOutline;
import org.eclipse.scout.rt.client.ui.desktop.outline.IOutline;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.client.ui.form.fields.IFormField;
import org.eclipse.scout.rt.client.ui.form.fields.IValueField;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.ISmartField;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.FMilaUtility.OperatingSystem;

/**
 * @author Dominic Plangger
 */
public final class ClientTestingUtility {

  private static final Logger LOG = LoggerFactory.getLogger(ClientTestingUtility.class);

  private ClientTestingUtility() {
    // Utility class, no public constructor
  }

  /**
   * Creates a new FieldValue object
   * 
   * @see {@link FieldValue}
   */
  public static FieldValue fieldValue(Class<? extends IValueField<?>> fieldClass, Object value) {
    return new FieldValue(fieldClass, value);
  }

  /**
   * checks recursively if a field is visible (perhaps it inherited invisibility from parent)
   */
  public static boolean isFieldVisible(IFormField field) {
    if (field == null) {
      return true;
    }
    if (!field.isVisible()) {
      return false;
    }
    return isFieldVisible(field.getParentField());
  }

  /**
   * gets the first entry of a smartfield that is enabled and active
   */
  public static <T> boolean selectFirstEntry(ISmartField<T> smartField) {
    if (smartField == null || smartField.getLookupCall() == null) {
      return false;
    }
    try {
      List<? extends ILookupRow> lookupRows = smartField.callBrowseLookup(null, 10);
      if (lookupRows != null) {
        for (ILookupRow row : lookupRows) {
          if (row.isActive() && row.isEnabled()) {
            @SuppressWarnings("unchecked")
            T value = (T) row.getKey();
            smartField.setValue(value);
            if (smartField.getErrorStatus() == null) {
              return true;
            }
          }
        }
      }
    }
    catch (ProcessingException e) {
      LOG.warn("enexpected exception while selecting first valid value in smart field [" + smartField + "]", e);
    }
    return false;
  }

  public static String getSerialTestingPort() {
    if (OperatingSystem.WINDOWS.equals(FMilaUtility.getPlatform())) {
      return "COM3"; // Windows
    }
    else if (OperatingSystem.MACOSX.equals(FMilaUtility.getPlatform())) {
      return "tty.Bluetooth-Incoming-Port"; // Mac OS X 10.9
    }
    return "UNKNOWN_OS";
  }

  public static IPage gotoOutline(Class<? extends AbstractOutline> outlineClass) {
    IPage rootPage = null;
    List<IOutline> availableOutlines = IDesktop.CURRENT.get().getAvailableOutlines();
    for (IOutline outline : availableOutlines) {

      if (outline.getClass().isAssignableFrom(outlineClass)) {
        IDesktop.CURRENT.get().setOutline(outline);
        System.out.println("Selected Outline: " + outline.getTitle());
        outline.releaseUnusedPages();
        rootPage = outline.getRootPage();
      }

    }
    return rootPage;
  }

  @SuppressWarnings("unchecked")
  public static <T extends IPage> T gotoChildPage(IPage parentPage, Class<T> childPageClass) {
    T childPage = null;
    List<IPage> childPages = parentPage.getChildPages();
    for (IPage page : childPages) {
      if (childPageClass.isAssignableFrom(page.getClass())) {
        IOutline outline = IDesktop.CURRENT.get().getOutline();
        outline.selectNode(page);
        System.out.println("Selected Page: " + page);
        outline.releaseUnusedPages();
        childPage = (T) page;
      }

    }
    return childPage;
  }

}
