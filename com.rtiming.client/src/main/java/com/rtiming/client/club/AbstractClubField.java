package com.rtiming.client.club;

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
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;

import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.club.ClubLookupCall;

public abstract class AbstractClubField extends AbstractSmartField<Long> {

  @Override
  protected String getConfiguredBrowseNewText() {
    return TEXTS.get("NewButton");
  }

// TODO MIG  
//  @Override
//  protected ISmartFieldProposalForm createProposalForm() throws ProcessingException {
//    KeyStrokeSmartTableForm form = new KeyStrokeSmartTableForm(this);
//    form.setAutoAddRemoveOnDesktop(false);
//    return form;
//  }

  @Override
  protected LookupRow execBrowseNew(String searchText) throws ProcessingException {
    ClubForm form = new ClubForm();
    form.startNew();
    form.getNameField().setValue(searchText);
    form.waitFor();
    if (form.isFormStored()) {
      return new LookupRow(form.getClubNr(), null);
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
      doModifyClubMenu();
    }

  }

  @Override
  protected String getConfiguredLabel() {
    return Texts.get("Club");
  }

  @Override
  protected Class<? extends LookupCall<Long>> getConfiguredLookupCall() {
    return ClubLookupCall.class;
  }

  @Order(10.0)
  public class EditMenu extends AbstractMenu {

    @Override
    protected String getConfiguredText() {
      return ScoutTexts.get("EditMenu");
    }

    @Override
    protected void execAction() throws ProcessingException {
      doModifyClubMenu();
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
      doNewClubMenu();
    }

    @Override
    protected String getConfiguredKeyStroke() {
      return FMilaUtility.NEW_KEY_STROKE;
    }
  }

  private void doModifyClubMenu() throws ProcessingException {
    if (getValue() != null) {
      ClubForm form = new ClubForm();
      form.setClubNr(getValue());
      form.startModify();
      form.waitFor();
      if (form.isFormStored()) {
        refreshDisplayText();
      }
    }
  }

  private void doNewClubMenu() throws ProcessingException {
    ClubForm form = new ClubForm();
    form.startNew();
    form.waitFor();
    if (form.isFormStored()) {
      setValue(form.getClubNr());
    }
  }

}
