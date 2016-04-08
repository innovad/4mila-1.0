package com.rtiming.client.ecard;

import java.util.Set;

import org.eclipse.scout.commons.CollectionUtility;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.action.keystroke.AbstractKeyStroke;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;

import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.dao.RtEcardKey;
import com.rtiming.shared.ecard.ECardLookupCall;

public abstract class AbstractECardField extends AbstractSmartField<Long> {

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
    ECardForm form = new ECardForm();
    form.startNew();
    form.getNumberField().setValue(searchText);
    form.waitFor();
    if (form.isFormStored()) {
      return new LookupRow(form.getECardKey().getId(), null);
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
      doModifyECardMenu();
    }

  }

  @Override
  protected String getConfiguredLabel() {
    return Texts.get("ECard");
  }

  @Override
  protected Class<? extends LookupCall<Long>> getConfiguredLookupCall() {
    return ECardLookupCall.class;
  }

  @Order(10.0)
  public class EditMenu extends AbstractMenu {

    @Override
    protected String getConfiguredText() {
      return ScoutTexts.get("EditMenu");
    }

    @Override
    protected void execAction() throws ProcessingException {
      doModifyECardMenu();
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
      doNewECardMenu();
    }
  }

  private void doModifyECardMenu() throws ProcessingException {
    if (getValue() != null) {
      ECardForm form = new ECardForm();
      form.setECardKey(RtEcardKey.create(getValue()));
      form.startModify();
      form.waitFor();
      if (form.isFormStored()) {
        refreshDisplayText();
      }
    }
  }

  private void doNewECardMenu() throws ProcessingException {
    ECardForm form = new ECardForm();
    form.startNew();
    form.waitFor();
    if (form.isFormStored()) {
      setValue(form.getECardKey().getId());
    }
  }

}
