package com.rtiming.client.settings.clazz;

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
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;

import com.rtiming.client.settings.CodeForm;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.event.course.ClassCodeType;

public abstract class AbstractClassField extends AbstractSmartField<Long> {

  @Order(10.0)
  public class EditKeyStroke extends AbstractKeyStroke {

    @Override
    protected String getConfiguredKeyStroke() {
      return FMilaUtility.EDIT_KEY_STROKE;
    }

    @Override
    protected void execAction() throws ProcessingException {
      doModifyClassMenu();
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
      doNewClassMenu();
    }
  }

  @Override
  protected String getConfiguredLabel() {
    return Texts.get("Class");
  }

  @Override
  protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
    return ClassCodeType.class;
  }

  @Order(10.0)
  public class EditMenu extends AbstractMenu {

    @Override
    protected String getConfiguredText() {
      return ScoutTexts.get("EditMenu");
    }

    @Override
    protected void execAction() throws ProcessingException {
      doModifyClassMenu();
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
      doNewClassMenu();
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

// TODO MIG  
//  @Override
//  protected ISmartFieldProposalForm createProposalForm() throws ProcessingException {
//    KeyStrokeSmartTableForm form = new KeyStrokeSmartTableForm(this);
//    form.setAutoAddRemoveOnDesktop(false);
//    return form;
//  }

  @Override
  protected LookupRow execBrowseNew(String searchText) throws ProcessingException {
    CodeForm form = new CodeForm();
    form.setCodeType(ClassCodeType.ID);
    form.startNew();
    form.getMainBox().getShortcutField().setValue(searchText);
    form.waitFor();
    if (form.isFormStored()) {
      return new LookupRow(form.getCodeUid(), null);
    }
    return null;
  }

  private void doModifyClassMenu() throws ProcessingException {
    if (getValue() != null) {
      CodeForm form = new CodeForm();
      form.setCodeType(ClassCodeType.ID);
      form.setCodeUid(getValue());
      form.startModify();
      form.waitFor();
      if (form.isFormStored()) {
        refreshDisplayText();
      }
    }
  }

  private void doNewClassMenu() throws ProcessingException {
    CodeForm form = new CodeForm();
    form.setCodeType(ClassCodeType.ID);
    form.startNew();
    form.waitFor();
    if (form.isFormStored()) {
      setValue(form.getCodeUid());
    }
  }

}
