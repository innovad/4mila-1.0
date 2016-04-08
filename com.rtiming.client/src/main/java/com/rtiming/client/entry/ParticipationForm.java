package com.rtiming.client.entry;

import org.eclipse.scout.commons.annotations.FormData;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.platform.BEANS;

import com.rtiming.client.entry.ParticipationForm.MainBox.CancelButton;
import com.rtiming.client.entry.ParticipationForm.MainBox.OkButton;
import com.rtiming.shared.common.security.permission.UpdateParticipationPermission;
import com.rtiming.shared.entry.IParticipationProcessService;
import com.rtiming.shared.entry.ParticipationFormData;

@FormData(value = ParticipationFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class ParticipationForm extends AbstractForm {

  private Long m_entryNr;
  private Long m_eventNr;
  private Long m_rawStartTime;
  private String m_baseBibNumber;

  public ParticipationForm() throws ProcessingException {
    super();
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  public void startModify() throws ProcessingException {
    startInternal(new ParticipationForm.ModifyHandler());
  }

  public void startNew() throws ProcessingException {
    startInternal(new ParticipationForm.NewHandler());
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class OkButton extends AbstractOkButton {
    }

    @Order(20.0)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  public class ModifyHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IParticipationProcessService service = BEANS.get(IParticipationProcessService.class);
      ParticipationFormData formData = new ParticipationFormData();
      exportFormData(formData);
      formData = service.load(formData);
      importFormData(formData);
      setEnabledPermission(new UpdateParticipationPermission());
    }

    @Override
    public void execStore() throws ProcessingException{
      IParticipationProcessService service = BEANS.get(IParticipationProcessService.class);
      ParticipationFormData formData = new ParticipationFormData();
      exportFormData(formData);
      formData = service.store(formData);
    }
  }

  public class NewHandler extends AbstractFormHandler{

    @Override
    public void execLoad() throws ProcessingException{
      IParticipationProcessService service = BEANS.get(IParticipationProcessService.class);
      ParticipationFormData formData = new ParticipationFormData();
      exportFormData(formData);
      formData = service.prepareCreate(formData);
      importFormData(formData);
    }

    @Override
    public void execStore() throws ProcessingException{
      IParticipationProcessService service = BEANS.get(IParticipationProcessService.class);
      ParticipationFormData formData = new ParticipationFormData();
      exportFormData(formData);
      formData = service.create(formData);
    }
  }

  @FormData
  public Long getEntryNr() {
    return m_entryNr;
  }

  @FormData
  public void setEntryNr(Long entryNr) {
    m_entryNr = entryNr;
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
  public Long getRawStartTime() {
    return m_rawStartTime;
  }

  @FormData
  public void setRawStartTime(Long startTime) {
    m_rawStartTime = startTime;
  }

  @FormData
  public String getBaseBibNumber() {
    return m_baseBibNumber;
  }

  @FormData
  public void setBaseBibNumber(String baseBibNumber) {
    m_baseBibNumber = baseBibNumber;
  }
}
