package com.rtiming.client.settings.city;

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

import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.settings.city.CountryCodeType;

public abstract class AbstractCountryField extends AbstractSmartField<Long> {

  @Override
  protected String getConfiguredLabel() {
    return Texts.get("Country");
  }

  @Override
  protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
    return CountryCodeType.class;
  }

  @Order(10.0)
  public class EditKeyStroke extends AbstractKeyStroke {

    @Override
    protected String getConfiguredKeyStroke() {
      return FMilaUtility.EDIT_KEY_STROKE;
    }

    @Override
    protected void execAction() throws ProcessingException {
      doModifyCountryMenu();
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
      doNewCountryMenu();
    }
  }

  @Order(10.0)
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
    protected String getConfiguredKeyStroke() {
      return FMilaUtility.NEW_KEY_STROKE;
    }

    @Override
    protected void execAction() throws ProcessingException {
      doNewCountryMenu();
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
      doModifyCountryMenu();
    }

  }

  private void doModifyCountryMenu() throws ProcessingException {
    if (getValue() != null) {
      CountryForm form = new CountryForm();
      form.setCountryUid(getValue());
      form.startModify();
    }
  }

  private void doNewCountryMenu() throws ProcessingException {
    CountryForm form = new CountryForm();
    form.startNew();
    form.waitFor();
    if (form.isFormStored()) {
      setValue(form.getCountryUid());
    }
  }

}
