package com.rtiming.client.entry.startblock;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.booleanfield.AbstractBooleanField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

import com.rtiming.client.common.help.AbstractHelpLinkButton;
import com.rtiming.client.entry.startblock.StartblockSelectionForm.MainBox.CancelButton;
import com.rtiming.client.entry.startblock.StartblockSelectionForm.MainBox.OkButton;
import com.rtiming.client.entry.startblock.StartblockSelectionForm.MainBox.OverwriteField;
import com.rtiming.client.entry.startblock.StartblockSelectionForm.MainBox.StartblockUidField;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.UpdateStartblockSelectionPermission;
import com.rtiming.shared.entry.startblock.IStartblockSelectionProcessService;
import com.rtiming.shared.entry.startblock.StartblockLookupCall;
import com.rtiming.shared.entry.startblock.StartblockSelectionFormData;

@FormData(value = StartblockSelectionFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class StartblockSelectionForm extends AbstractForm {

  private Long m_eventNr;
  private Long[] m_entryNrs;

  public StartblockSelectionForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Startblock");
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  public void startModify() throws ProcessingException {
    startInternal(new StartblockSelectionForm.ModifyHandler());
  }

  public void startNew() throws ProcessingException {
    startInternal(new StartblockSelectionForm.NewHandler());
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  public OverwriteField getOverwriteField() {
    return getFieldByClass(OverwriteField.class);
  }

  public StartblockUidField getStartblockUidField() {
    return getFieldByClass(StartblockUidField.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class StartblockUidField extends AbstractSmartField<Long> {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Startblock");
      }

      @Override
      protected Class<? extends LookupCall<Long>> getConfiguredLookupCall() {
        return StartblockLookupCall.class;
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

      @Override
      protected void execPrepareLookup(ILookupCall call) throws ProcessingException {
        ((StartblockLookupCall) call).setEventNr(getEventNr());
      }
    }

    @Order(20.0)
    public class OverwriteField extends AbstractBooleanField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Overwrite");
      }
    }

    @Order(28.0)
    public class HelpLink extends AbstractHelpLinkButton {
    }

    @Order(30.0)
    public class OkButton extends AbstractOkButton {
    }

    @Order(40.0)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  public class ModifyHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IStartblockSelectionProcessService service = BEANS.get(IStartblockSelectionProcessService.class);
      StartblockSelectionFormData formData = new StartblockSelectionFormData();
      exportFormData(formData);
      formData = service.load(formData);
      importFormData(formData);
      setEnabledPermission(new UpdateStartblockSelectionPermission());
    }

    @Override
    public void execStore() throws ProcessingException {
      IStartblockSelectionProcessService service = BEANS.get(IStartblockSelectionProcessService.class);
      StartblockSelectionFormData formData = new StartblockSelectionFormData();
      exportFormData(formData);
      formData = service.store(formData);
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IStartblockSelectionProcessService service = BEANS.get(IStartblockSelectionProcessService.class);
      StartblockSelectionFormData formData = new StartblockSelectionFormData();
      exportFormData(formData);
      formData = service.prepareCreate(formData);
      importFormData(formData);
    }

    @Override
    public void execStore() throws ProcessingException {
      IStartblockSelectionProcessService service = BEANS.get(IStartblockSelectionProcessService.class);
      StartblockSelectionFormData formData = new StartblockSelectionFormData();
      exportFormData(formData);
      formData = service.create(formData);
    }
  }

  @FormData
  public Long getEventNr() {
    return m_eventNr;
  }

  @FormData
  public void setEventNr(Long eventNr) {
    m_eventNr = eventNr;
  }

  @FormData
  public Long[] getEntryNrs() {
    return m_entryNrs;
  }

  @FormData
  public void setEntryNrs(Long[] entryNrs) {
    m_entryNrs = entryNrs;
  }
}
