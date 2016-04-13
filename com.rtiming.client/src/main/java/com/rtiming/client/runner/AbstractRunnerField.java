package com.rtiming.client.runner;

import java.util.Set;

import org.eclipse.scout.rt.client.ui.action.keystroke.AbstractKeyStroke;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.ValueFieldMenuType;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;

import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.runner.RunnerLookupCall;

public abstract class AbstractRunnerField extends AbstractSmartField<Long> {

  @Override
  protected String getConfiguredLabel() {
    return Texts.get("Runner");
  }

  @Override
  protected Class<? extends LookupCall<Long>> getConfiguredLookupCall() {
    return RunnerLookupCall.class;
  }

  @Order(10.0)
  public class EditKeyStroke extends AbstractKeyStroke {

    @Override
    protected String getConfiguredKeyStroke() {
      return FMilaUtility.EDIT_KEY_STROKE;
    }

    @Override
    protected void execAction() throws ProcessingException {
      doModifyRunnerMenu();
    }

  }

  @Override
  protected String getConfiguredBrowseNewText() {
    return ScoutTexts.get("NewButton");
  }

//TODO MIG  
//@Override
//protected ISmartFieldProposalForm createProposalForm() throws ProcessingException {
//  KeyStrokeSmartTableForm form = new KeyStrokeSmartTableForm(this);
//  form.setAutoAddRemoveOnDesktop(false);
//  return form;
//}

  @Override
  protected LookupRow execBrowseNew(String searchText) throws ProcessingException {
    RunnerForm form = new RunnerForm();
    form.startNew();
    form.getLastNameField().setValue(searchText);
    form.waitFor();
    if (form.isFormStored()) {
      return new LookupRow<Long>(form.getRunnerNr(), null);
    }
    return null;
  }

  @Order(10.0)
  public class NewMenu extends AbstractMenu {

    @Override
    protected String getConfiguredText() {
      return ScoutTexts.get("NewButton");
    }

    @Override
    protected String getConfiguredKeyStroke() {
      return FMilaUtility.NEW_KEY_STROKE;
    }

    @Override
    protected Set<? extends IMenuType> getConfiguredMenuTypes() {
      return CollectionUtility.<IMenuType> hashSet(ValueFieldMenuType.Null);
    }

    @Override
    protected void execAction() throws ProcessingException {
      doNewRunnerMenu();
    }
  }

  @Order(20.0)
  public class EditMenu extends AbstractMenu {

    @Override
    protected String getConfiguredKeyStroke() {
      return FMilaUtility.EDIT_KEY_STROKE;
    }

    @Override
    protected String getConfiguredText() {
      return ScoutTexts.get("EditMenu");
    }

    @Override
    protected void execAction() throws ProcessingException {
      doModifyRunnerMenu();
    }

  }

  private void doModifyRunnerMenu() throws ProcessingException {
    if (getValue() != null) {
      RunnerForm form = new RunnerForm();
      form.setRunnerNr(getValue());
      form.startModify();
      form.waitFor();
      if (form.isFormStored()) {
        refreshDisplayText();
      }
    }
  }

  private void doNewRunnerMenu() throws ProcessingException {
    RunnerForm form = new RunnerForm();
    form.startNew();
    form.waitFor();
    if (form.isFormStored()) {
      setValue(form.getRunnerNr());
      refreshDisplayText();
    }
  }

}
