package com.rtiming.client.dataexchange;

import org.eclipse.scout.rt.client.ui.wizard.AbstractWizardStep;

public abstract class AbstractFMilaWizardStep extends AbstractWizardStep {

  @Override
  public final String getDescriptionHtml() {
    String text = getDescriptionText();
    text = text.replace("\r\n", "<br/>");
    text = text.replace("\n", "<br/>");
    text = text.replace("\r", "<br/>");
    return "<p style=\"padding:10px;\">" + text + "</p>";
  }

  @Override
  protected final String getConfiguredDescriptionHtml() {
    return super.getConfiguredDescriptionHtml();
  }

  protected abstract String getDescriptionText();

}
