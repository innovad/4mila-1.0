package com.rtiming.client.event.course;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.longfield.AbstractLongField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.client.common.help.AbstractHelpLinkButton;
import com.rtiming.client.event.AbstractEventField;
import com.rtiming.client.event.course.CourseForm.MainBox.CancelButton;
import com.rtiming.client.event.course.CourseForm.MainBox.ClimbField;
import com.rtiming.client.event.course.CourseForm.MainBox.EventField;
import com.rtiming.client.event.course.CourseForm.MainBox.LengthField;
import com.rtiming.client.event.course.CourseForm.MainBox.OkButton;
import com.rtiming.client.event.course.CourseForm.MainBox.ShortcutField;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.UpdateCoursePermission;
import com.rtiming.shared.event.course.CourseFormData;
import com.rtiming.shared.event.course.ICourseProcessService;

@FormData(value = CourseFormData.class, sdkCommand = SdkCommand.CREATE)
public class CourseForm extends AbstractForm {

  private Long courseNr;

  public CourseForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Course");
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  @FormData
  public Long getCourseNr() {
    return courseNr;
  }

  @FormData
  public void setCourseNr(Long courseNr) {
    this.courseNr = courseNr;
  }

  public void startModify() throws ProcessingException {
    startInternal(new ModifyHandler());
  }

  public void startNew() throws ProcessingException {
    startInternal(new NewHandler());
  }

  public void startNewCourseWithVariants(Long... courseNrs) throws ProcessingException {
    startInternal(new NewCourseWithVariantsHandler(courseNrs));
  }

  public ClimbField getClimbField() {
    return getFieldByClass(ClimbField.class);
  }

  public EventField getEventField() {
    return getFieldByClass(EventField.class);
  }

  public LengthField getLengthField() {
    return getFieldByClass(LengthField.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  public ShortcutField getShortcutField() {
    return getFieldByClass(ShortcutField.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class EventField extends AbstractEventField {

      @Override
      protected boolean getConfiguredEnabled() {
        return false;
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }
    }

    @Order(20.0)
    public class ShortcutField extends AbstractStringField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Shortcut");
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
    public class LengthField extends AbstractLongField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Length");
      }

      @Override
      protected Long getConfiguredMaxValue() {
        return 999999L;
      }

      @Override
      protected Long getConfiguredMinValue() {
        return 0L;
      }
    }

    @Order(40.0)
    public class ClimbField extends AbstractLongField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Climb");
      }

      @Override
      protected Long getConfiguredMaxValue() {
        return 99999L;
      }

      @Override
      protected Long getConfiguredMinValue() {
        return 0L;
      }
    }

    @Order(48.0)
    public class HelpLink extends AbstractHelpLinkButton {
    }

    @Order(50.0)
    public class OkButton extends AbstractOkButton {
    }

    @Order(60.0)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  public class ModifyHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      ICourseProcessService service = BEANS.get(ICourseProcessService.class);
      CourseFormData formData = new CourseFormData();
      exportFormData(formData);
      formData = service.load(formData);
      importFormData(formData);
      setEnabledPermission(new UpdateCoursePermission());
    }

    @Override
    public void execStore() throws ProcessingException {
      ICourseProcessService service = BEANS.get(ICourseProcessService.class);
      CourseFormData formData = new CourseFormData();
      exportFormData(formData);
      formData = service.store(formData);
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      ICourseProcessService service = BEANS.get(ICourseProcessService.class);
      CourseFormData formData = new CourseFormData();
      exportFormData(formData);
      formData = service.prepareCreate(formData);
      importFormData(formData);
    }

    @Override
    public void execStore() throws ProcessingException {
      ICourseProcessService service = BEANS.get(ICourseProcessService.class);
      CourseFormData formData = new CourseFormData();
      exportFormData(formData);
      formData = service.create(formData);
      importFormData(formData);
    }
  }

  public class NewCourseWithVariantsHandler extends NewHandler {

    private final Long[] courseNrs;

    public NewCourseWithVariantsHandler(Long... courseNrs) {
      this.courseNrs = courseNrs;
    }

    @Override
    public void execLoad() throws ProcessingException {
      super.execLoad();
      if (courseNrs != null && courseNrs.length > 0) {
        CourseFormData course = new CourseFormData();
        course.setCourseNr(courseNrs[0]);
        course = BEANS.get(ICourseProcessService.class).load(course);
        getLengthField().setValue(course.getLength().getValue());
        getClimbField().setValue(course.getClimb().getValue());
      }
    }

    @Override
    public void execStore() throws ProcessingException {
      ICourseProcessService service = BEANS.get(ICourseProcessService.class);
      CourseFormData formData = new CourseFormData();
      exportFormData(formData);
      formData = service.createNewCourseWithVariants(formData, courseNrs);
      importFormData(formData);
    }

  }

}
