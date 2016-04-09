package com.rtiming.client.common.ui.fields;

import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.StringUtility;

import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;

public abstract class AbstractEmailField extends AbstractStringField {

  @Override
  protected String getConfiguredLabel() {
    return Texts.get("EMail");
  }

// TODO MIG  
//  @Override
//  protected void execLinkAction(URL url) throws ProcessingException {
//    if (url != null) {
//      ClientSessionProvider.currentSession().getDesktop().openUri(url.toExternalForm(), OpenUriAction.DOWNLOAD);
//    }
//  }

  @Override
  protected int getConfiguredMaxLength() {
    return 250;
  }

  @Override
  protected String validateValueInternal(String rawValue) throws ProcessingException {
    rawValue = super.validateValueInternal(rawValue);
    FMilaUtility.validateEmailAddresses(rawValue);
    String validValue = StringUtility.substring(rawValue, 0, 250);
    return validValue;
  }

}
