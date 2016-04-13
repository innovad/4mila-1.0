package com.rtiming.client.entry.startblock;

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
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;

import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.entry.startblock.StartblockCodeType;

public abstract class AbstractStartblockField extends AbstractSmartField<Long> {

  @Override
  protected String getConfiguredBrowseNewText() {
    return TEXTS.get("NewButton");
  }

//TODO MIG  
//@Override
//protected ISmartFieldProposalForm createProposalForm() throws ProcessingException {
//  KeyStrokeSmartTableForm form = new KeyStrokeSmartTableForm(this);
//  form.setAutoAddRemoveOnDesktop(false);
//  return form;
//}

  @Override
  protected LookupRow<Long> execBrowseNew(String searchText) throws ProcessingException {
    StartblockForm form = new StartblockForm();
    form.startNew();
    form.getCodeBox().getShortcutField().setValue(searchText);
    form.waitFor();
    if (form.isFormStored()) {
      return new LookupRow<Long>(form.getStartblockUid(), null);
    }
    return null;
  }

  @Order(10.0)
  public class EditKeyStroke extends AbstractKeyStroke {

    @Override
    protected String getConfiguredKeyStroke() {
      return FMilaUtility.EDIT_KEY_STROKE;
    }

    @Override
    protected void execAction() throws ProcessingException {
      doModifyStartblockMenu();
    }

  }

  @Override
  protected String getConfiguredLabel() {
    return Texts.get("Startblock");
  }

  @Override
  protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
    return StartblockCodeType.class;
  }

  @Order(10.0)
  public class EditMenu extends AbstractMenu {

    @Override
    protected String getConfiguredText() {
      return ScoutTexts.get("EditMenu");
    }

    @Override
    protected void execAction() throws ProcessingException {
      doModifyStartblockMenu();
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
      doNewStartblockMenu();
    }

    @Override
    protected String getConfiguredKeyStroke() {
      return FMilaUtility.NEW_KEY_STROKE;
    }
  }

  private void doModifyStartblockMenu() throws ProcessingException {
    if (getValue() != null) {
      StartblockForm form = new StartblockForm();
      form.setStartblockUid(getValue());
      form.startModify();
    }
  }

  private void doNewStartblockMenu() throws ProcessingException {
    StartblockForm form = new StartblockForm();
    form.startNew();
    form.waitFor();
    if (form.isFormStored()) {
      setValue(form.getStartblockUid());
    }
  }

}
