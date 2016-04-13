package com.rtiming.client.event.course;

import java.util.Set;

import org.eclipse.scout.rt.client.ui.action.keystroke.AbstractKeyStroke;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;

import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.event.course.ControlLookupCall;

public abstract class AbstractControlField extends AbstractSmartField<Long> {

  @Order(10.0)
  public class EditKeyStroke extends AbstractKeyStroke {

    @Override
    protected String getConfiguredKeyStroke() {
      return FMilaUtility.EDIT_KEY_STROKE;
    }

    @Override
    protected void execAction() throws ProcessingException {
      doModifyControlMenu();
    }

  }

  @Order(20.0)
  public class NewKeyStroke extends AbstractKeyStroke {

    @Override
    protected String getConfiguredKeyStroke() {
      return FMilaUtility.NEW_KEY_STROKE;
    }

    @Override
    protected void execAction() throws ProcessingException {
      doNewControlMenu();
    }
  }

  @Override
  protected String getConfiguredLabel() {
    return Texts.get("Control");
  }

  @Override
  protected Class<? extends LookupCall<Long>> getConfiguredLookupCall() {
    return ControlLookupCall.class;
  }

  @Override
  protected void execPrepareLookup(ILookupCall call) throws ProcessingException {
    ((ControlLookupCall) call).setEventNr(getControlEventNr());
  }

  @Order(10.0)
  public class EditMenu extends AbstractMenu {

    @Override
    protected boolean getConfiguredInheritAccessibility() {
      return false;
    }

    @Override
    protected String getConfiguredText() {
      return ScoutTexts.get("EditMenu");
    }

    @Override
    protected void execAction() throws ProcessingException {
      doModifyControlMenu();
    }

    @Override
    protected String getConfiguredKeyStroke() {
      return FMilaUtility.EDIT_KEY_STROKE;
    }
  }

  @Order(20.0)
  public class NewMenu extends AbstractMenu {

    @Override
    protected String getConfiguredText() {
      return ScoutTexts.get("NewButton");
    }

    @Override
    protected Set<? extends IMenuType> getConfiguredMenuTypes() {
      return CollectionUtility.<IMenuType> hashSet(TableMenuType.EmptySpace);
    }

    @Override
    protected void execAction() throws ProcessingException {
      doNewControlMenu();
    }

    @Override
    protected String getConfiguredKeyStroke() {
      return FMilaUtility.NEW_KEY_STROKE;
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
    ControlForm form = new ControlForm();
    form.getEventField().setValue(getControlEventNr());
    form.startNew();
    form.getNumberField().setValue(searchText);
    form.waitFor();
    if (form.isFormStored()) {
      return new LookupRow<Long>(form.getControlNr(), null);
    }
    return null;
  }

  private void doModifyControlMenu() throws ProcessingException {
    if (getValue() != null) {
      ControlForm form = new ControlForm();
      form.setControlNr(getValue());
      form.startModify();
      form.waitFor();
      if (form.isFormStored()) {
        refreshDisplayText();
      }
    }
  }

  private void doNewControlMenu() throws ProcessingException {
    ControlForm form = new ControlForm();
    form.getEventField().setValue(getControlEventNr());
    form.startNew();
    form.waitFor();
    if (form.isFormStored()) {
      setValue(form.getControlNr());
    }
  }

  protected abstract Long getControlEventNr();

}
