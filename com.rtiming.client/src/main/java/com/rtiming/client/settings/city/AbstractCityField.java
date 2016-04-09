package com.rtiming.client.settings.city;

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
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;

import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.AbstractBasicLookupCall;
import com.rtiming.shared.settings.city.CityLookupCall;

public abstract class AbstractCityField extends AbstractSmartField<Long> {

  @Override
  protected String getConfiguredLabel() {
    return Texts.get("City");
  }

  @Override
  protected Class<? extends AbstractBasicLookupCall> getConfiguredLookupCall() {
    return CityLookupCall.class;
  }

  @Order(10.0)
  public class EditKeyStroke extends AbstractKeyStroke {

    @Override
    protected String getConfiguredKeyStroke() {
      return FMilaUtility.EDIT_KEY_STROKE;
    }

    @Override
    protected void execAction() throws ProcessingException {
      doModifyCityMenu();
    }

  }

  @Override
  protected String getConfiguredBrowseNewText() {
    return ScoutTexts.get("NewButton");
  }

// TODO MIG  
//  @Override
//  protected IProposalChooser createProposalChooser() throws ProcessingException {
//    KeyStrokeSmartTableForm form = new KeyStrokeSmartTableForm(this);
//    form.setAutoAddRemoveOnDesktop(false);
//    return form;
//  }

  @Override
  protected LookupRow execBrowseNew(String searchText) throws ProcessingException {
    CityForm form = new CityForm();
    form.startNew();
    form.getCityField().setValue(searchText);
    form.waitFor();
    if (form.isFormStored()) {
      return new LookupRow(form.getCityNr(), null);
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
    protected Set<? extends IMenuType> getConfiguredMenuTypes() {
      return CollectionUtility.<IMenuType> hashSet(ValueFieldMenuType.Null);
    }

    @Override
    protected String getConfiguredKeyStroke() {
      return FMilaUtility.NEW_KEY_STROKE;
    }

    @Override
    protected void execAction() throws ProcessingException {
      doNewCityMenu();
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
      doModifyCityMenu();
    }

  }

  private void doModifyCityMenu() throws ProcessingException {
    if (getValue() != null) {
      CityForm form = new CityForm();
      form.setCityNr(getValue());
      form.startModify();
      form.waitFor();
      if (form.isFormStored()) {
        refreshDisplayText();
      }
    }
  }

  private void doNewCityMenu() throws ProcessingException {
    CityForm form = new CityForm();
    form.startNew();
    form.waitFor();
    if (form.isFormStored()) {
      setValue(form.getCityNr());
    }
  }

}
