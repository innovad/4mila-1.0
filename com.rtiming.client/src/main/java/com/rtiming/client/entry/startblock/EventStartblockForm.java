package com.rtiming.client.entry.startblock;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.client.common.help.AbstractHelpLinkButton;
import com.rtiming.client.common.ui.fields.AbstractSortCodeField;
import com.rtiming.client.entry.startblock.EventStartblockForm.MainBox.CancelButton;
import com.rtiming.client.entry.startblock.EventStartblockForm.MainBox.OkButton;
import com.rtiming.client.entry.startblock.EventStartblockForm.MainBox.SortCodeField;
import com.rtiming.client.entry.startblock.EventStartblockForm.MainBox.StartblockUidField;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.UpdateEventStartblockPermission;
import com.rtiming.shared.entry.startblock.EventStartblockFormData;
import com.rtiming.shared.entry.startblock.IEventStartblockProcessService;

@FormData(value = EventStartblockFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class EventStartblockForm extends AbstractForm {

  private Long m_eventNr;

  public EventStartblockForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Startblocks");
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  public void startModify() throws ProcessingException {
    startInternal(new EventStartblockForm.ModifyHandler());
  }

  public void startNew() throws ProcessingException {
    startInternal(new EventStartblockForm.NewHandler());
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  public SortCodeField getSortCodeField() {
    return getFieldByClass(SortCodeField.class);
  }

  public StartblockUidField getStartblockUidField() {
    return getFieldByClass(StartblockUidField.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class StartblockUidField extends AbstractStartblockField {

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }
    }

    @Order(20.0)
    public class SortCodeField extends AbstractSortCodeField {

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
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
      IEventStartblockProcessService service = BEANS.get(IEventStartblockProcessService.class);
      EventStartblockFormData formData = new EventStartblockFormData();
      exportFormData(formData);
      formData = service.load(formData);
      importFormData(formData);
      setEnabledPermission(new UpdateEventStartblockPermission());
    }

    @Override
    public void execStore() throws ProcessingException {
      IEventStartblockProcessService service = BEANS.get(IEventStartblockProcessService.class);
      EventStartblockFormData formData = new EventStartblockFormData();
      exportFormData(formData);
      formData = service.store(formData);
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IEventStartblockProcessService service = BEANS.get(IEventStartblockProcessService.class);
      EventStartblockFormData formData = new EventStartblockFormData();
      exportFormData(formData);
      formData = service.prepareCreate(formData);
      importFormData(formData);
    }

    @Override
    public void execStore() throws ProcessingException {
      IEventStartblockProcessService service = BEANS.get(IEventStartblockProcessService.class);
      EventStartblockFormData formData = new EventStartblockFormData();
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
}
