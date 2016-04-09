package com.rtiming.client.ecard.download;

import java.util.List;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBox;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.shared.services.common.code.CODES;

import com.rtiming.client.ClientSession;
import com.rtiming.client.FMilaClientUtility;
import com.rtiming.client.common.help.AbstractHelpLinkButton;
import com.rtiming.client.ecard.download.CourseAndClassFromECardForm.MainBox.CancelButton;
import com.rtiming.client.ecard.download.CourseAndClassFromECardForm.MainBox.ClazzField;
import com.rtiming.client.ecard.download.CourseAndClassFromECardForm.MainBox.CourseField;
import com.rtiming.client.ecard.download.CourseAndClassFromECardForm.MainBox.EventField;
import com.rtiming.client.ecard.download.CourseAndClassFromECardForm.MainBox.OkButton;
import com.rtiming.client.event.AbstractEventField;
import com.rtiming.client.settings.clazz.AbstractClassField;
import com.rtiming.shared.Texts;
import com.rtiming.shared.ecard.download.CourseAndClassFromECardFormData;
import com.rtiming.shared.ecard.download.ECardStationDownloadModusCodeType;
import com.rtiming.shared.ecard.download.ICourseAndClassFromECardProcessService;
import com.rtiming.shared.ecard.download.PunchFormData;
import com.rtiming.shared.event.EventClassFormData;
import com.rtiming.shared.event.IEventClassProcessService;
import com.rtiming.shared.event.course.CourseFormData;
import com.rtiming.shared.event.course.ICourseProcessService;

@FormData(value = CourseAndClassFromECardFormData.class, sdkCommand = SdkCommand.CREATE)
public class CourseAndClassFromECardForm extends AbstractForm {

  private List<PunchFormData> m_controls;

  public CourseAndClassFromECardForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("CreateCourseAndClassFromECard");
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  public void startNew() throws ProcessingException {
    startInternal(new NewHandler());
  }

  public ClazzField getClazzField() {
    return getFieldByClass(ClazzField.class);
  }

  public CourseField getCourseField() {
    return getFieldByClass(CourseField.class);
  }

  public EventField getEventField() {
    return getFieldByClass(EventField.class);
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
    public class EventField extends AbstractEventField {

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }
    }

    @Order(20.0)
    public class CourseField extends AbstractStringField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Course");
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

      @Override
      protected int getConfiguredMaxLength() {
        return 60;
      }
    }

    @Order(30.0)
    public class ClazzField extends AbstractClassField {

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }
    }

    @Order(38.0)
    public class HelpLink extends AbstractHelpLinkButton {
    }

    @Order(40.0)
    public class OkButton extends AbstractOkButton {
    }

    @Order(50.0)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  @Override
  protected boolean execValidate() throws ProcessingException {

    EventClassFormData ec = new EventClassFormData();
    ec.getEvent().setValue(getEventField().getValue());
    ec.getClazz().setValue(getClazzField().getValue());
    ec = BEANS.get(IEventClassProcessService.class).load(ec);
    if (ec.getType().getValue() != null) {
      throw new VetoException(Texts.get("ClassAlreadyUsedInEventMessage"));
    }

    CourseFormData course = BEANS.get(ICourseProcessService.class).find(getCourseField().getValue(), getEventField().getValue());
    if (course.getCourseNr() != null) {
      throw new VetoException(Texts.get("CourseCodeAlreadyUsedInEventMessage"));
    }

    return true;
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      ICourseAndClassFromECardProcessService service = BEANS.get(ICourseAndClassFromECardProcessService.class);
      CourseAndClassFromECardFormData formData = new CourseAndClassFromECardFormData();
      exportFormData(formData);
      formData = service.prepareCreate(formData);
      importFormData(formData);
    }

    @Override
    public void execStore() throws ProcessingException {
      ICourseAndClassFromECardProcessService service = BEANS.get(ICourseAndClassFromECardProcessService.class);
      CourseAndClassFromECardFormData formData = new CourseAndClassFromECardFormData();
      exportFormData(formData);
      formData = service.create(formData);

      // Reload
      ClientSession.get().getDesktop().getOutline().getActivePage().reloadPage();
      String text = CODES.getCode(ECardStationDownloadModusCodeType.DownloadSplitTimesAndRegisterCode.class).getText();
      int answer = FMilaClientUtility.showYesNoMessage(null, null, Texts.get("CourseFromECardCreatedMessage", text)).getSeverity();
      if (answer == MessageBox.YES_OPTION) {
        ClientSession.get().getDesktop().findForm(ECardStationStatusForm.class).getModusField().setValue(ECardStationDownloadModusCodeType.DownloadSplitTimesAndRegisterCode.ID);
      }
    }
  }

  @FormData
  public List<PunchFormData> getControls() {
    return m_controls;
  }

  @FormData
  public void setControls(List<PunchFormData> controls) {
    m_controls = controls;
  }
}
