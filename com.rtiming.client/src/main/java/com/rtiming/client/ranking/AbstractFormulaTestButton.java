package com.rtiming.client.ranking;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractButton;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.client.FMilaClientUtility;

public abstract class AbstractFormulaTestButton extends AbstractButton {

  protected abstract String getForumula();

  @Override
  protected String getConfiguredLabel() {
    return TEXTS.get("TestFormula");
  }

  @Override
  protected void execClickAction() throws ProcessingException {
    new FormulaScript(getForumula());
    FMilaClientUtility.showOkMessage(TEXTS.get("ApplicationName"), null, "4mila compiled script successfully.");
  }

}
