package com.rtiming.client.event.course;

import org.eclipse.scout.commons.annotations.FormData;
import org.eclipse.scout.commons.annotations.FormData.SdkCommand;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.IValueField;
import org.eclipse.scout.rt.client.ui.form.fields.booleanfield.AbstractBooleanField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractLinkButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.placeholder.AbstractPlaceholderField;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

import com.rtiming.client.common.help.AbstractHelpLinkButton;
import com.rtiming.client.common.ui.fields.AbstractSortCodeField;
import com.rtiming.client.event.course.CourseControlForm.MainBox.CancelButton;
import com.rtiming.client.event.course.CourseControlForm.MainBox.ControlField;
import com.rtiming.client.event.course.CourseControlForm.MainBox.CourseField;
import com.rtiming.client.event.course.CourseControlForm.MainBox.OkButton;
import com.rtiming.client.event.course.CourseControlForm.MainBox.OptionsBox;
import com.rtiming.client.event.course.CourseControlForm.MainBox.OptionsBox.CountLegField;
import com.rtiming.client.event.course.CourseControlForm.MainBox.OptionsBox.ForkTypeField;
import com.rtiming.client.event.course.CourseControlForm.MainBox.OptionsBox.MandatoryField;
import com.rtiming.client.event.course.CourseControlForm.MainBox.OptionsBox.PlaceholderField;
import com.rtiming.client.event.course.CourseControlForm.MainBox.SaveButton;
import com.rtiming.client.event.course.CourseControlForm.MainBox.SortCodeField;
import com.rtiming.client.event.course.CourseControlForm.MainBox.VariantBox;
import com.rtiming.client.event.course.CourseControlForm.MainBox.VariantBox.ForkMasterCourseControlField;
import com.rtiming.client.event.course.CourseControlForm.MainBox.VariantBox.ForkVariantCodeField;
import com.rtiming.client.services.lookup.CourseMasterCourseControlLookupCall;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.UpdateCourseControlPermission;
import com.rtiming.shared.event.course.CourseControlFormData;
import com.rtiming.shared.event.course.CourseFormData;
import com.rtiming.shared.event.course.CourseLookupCall;
import com.rtiming.shared.event.course.ICourseControlProcessService;
import com.rtiming.shared.event.course.ICourseProcessService;
import com.rtiming.shared.services.code.CourseForkTypeCodeType;

@FormData(value = CourseControlFormData.class, sdkCommand = SdkCommand.CREATE)
public class CourseControlForm extends AbstractForm {

  private Long m_eventNr;
  private Long m_courseControlNr;

  public CourseControlForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("CourseControl");
  }

  public void startModify() throws ProcessingException {
    startInternal(new ModifyHandler());
  }

  public void startNew() throws ProcessingException {
    startInternal(new NewHandler());
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  public CountLegField getCountLegField() {
    return getFieldByClass(CountLegField.class);
  }

  public CourseField getCourseField() {
    return getFieldByClass(CourseField.class);
  }

  public ControlField getControlField() {
    return getFieldByClass(ControlField.class);
  }

  public ForkVariantCodeField getForkVariantCodeField() {
    return getFieldByClass(ForkVariantCodeField.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public MandatoryField getMandatoryField() {
    return getFieldByClass(MandatoryField.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  public OptionsBox getOptionsBox() {
    return getFieldByClass(OptionsBox.class);
  }

  public PlaceholderField getPlaceholderField() {
    return getFieldByClass(PlaceholderField.class);
  }

  public SaveButton getSaveButton() {
    return getFieldByClass(SaveButton.class);
  }

  public ForkMasterCourseControlField getForkMasterCourseControlField() {
    return getFieldByClass(ForkMasterCourseControlField.class);
  }

  public ForkTypeField getForkTypeField() {
    return getFieldByClass(ForkTypeField.class);
  }

  public SortCodeField getSortCodeField() {
    return getFieldByClass(SortCodeField.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class CourseField extends AbstractSmartField<Long> {

      @Override
      protected boolean getConfiguredEnabled() {
        return false;
      }

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Course");
      }

      @Override
      protected Class<? extends LookupCall<Long>> getConfiguredLookupCall() {
        return CourseLookupCall.class;
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

      @Override
      protected void execChangedValue() throws ProcessingException {
        CourseFormData course = new CourseFormData();
        course.setCourseNr(getCourseField().getValue());
        course = BEANS.get(ICourseProcessService.class).load(course);
        setEventNr(course.getEvent().getValue());
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }
    }

    @Order(20.0)
    public class ControlField extends AbstractControlField {

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

      @Override
      protected Long getControlEventNr() {
        return getEventNr();
      }
    }

    @Order(30.0)
    public class SortCodeField extends AbstractSortCodeField {

    }

    @Order(40.0)
    public class OptionsBox extends AbstractGroupBox {

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Options");
      }

      @Order(10.0)
      public class ForkTypeField extends AbstractSmartField<Long> {

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("ForkStart");
        }

        @Override
        protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
          return CourseForkTypeCodeType.class;
        }

        @Override
        protected void execChangedValue() throws ProcessingException {
          validateForkFields();
        }
      }

      @Order(20.0)
      public class PlaceholderField extends AbstractPlaceholderField {
      }

      @Order(30.0)
      public class CountLegField extends AbstractBooleanField {

        @Override
        protected String getConfiguredLabel() {
          return Texts.get("CountLeg");
        }
      }

      @Order(40.0)
      public class MandatoryField extends AbstractBooleanField {

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("MandatoryControl");
        }
      }
    }

    @Order(50.0)
    public class VariantBox extends AbstractGroupBox {

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Variant");
      }

      @Order(10.0)
      public class ForkMasterCourseControlField extends AbstractSmartField<Long> {

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("LoopMasterControl");
        }

        @Override
        protected Class<? extends LookupCall<Long>> getConfiguredLookupCall() {
          return CourseMasterCourseControlLookupCall.class;
        }

        @Override
        protected void execPrepareLookup(ILookupCall call) throws ProcessingException {
          ((CourseMasterCourseControlLookupCall) call).setCourseNr(getCourseField().getValue());
        }

        @Override
        protected void execChangedValue() throws ProcessingException {
          validateForkFields();
        }
      }

      @Order(20.0)
      public class ForkVariantCodeField extends AbstractStringField {

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Variant");
        }

        @Override
        protected Class<? extends IValueField> getConfiguredMasterField() {
          return ForkMasterCourseControlField.class;
        }

        @Override
        protected boolean getConfiguredFormatUpper() {
          return true;
        }

        @Override
        protected boolean getConfiguredEnabled() {
          return false;
        }

        @Override
        protected int getConfiguredMaxLength() {
          return 60;
        }

        @Override
        protected void execChangedMasterValue(Object newMasterValue) throws ProcessingException {
          setEnabled(newMasterValue != null && (Long) newMasterValue != 0);
          setMandatory(isEnabled());
          if (!isEnabled()) {
            setValue(null);
          }
        }

        @Override
        protected void execChangedValue() throws ProcessingException {
          validateForkFields();
        }

      }
    }

    @Order(60.0)
    public class HelpLink extends AbstractHelpLinkButton {
    }

    @Order(70.0)
    public class SaveButton extends AbstractLinkButton {

      @Override
      protected String getConfiguredLabel() {
        return ScoutTexts.get("SaveButton");
      }

      @Override
      protected void execClickAction() throws ProcessingException {
        doOk();
        if (isFormStored()) {
          getDesktop().getOutline().getActivePage().reloadPage();
          CourseControlForm form = new CourseControlForm();
          form.getCourseField().setValue(getCourseField().getValue());
          form.startNew();
        }
      }
    }

    @Order(80.0)
    public class OkButton extends AbstractOkButton {
    }

    @Order(90.0)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  public class ModifyHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      ICourseControlProcessService service = BEANS.get(ICourseControlProcessService.class);
      CourseControlFormData formData = new CourseControlFormData();
      exportFormData(formData);
      formData = service.load(formData);
      importFormData(formData);
      setEnabledPermission(new UpdateCourseControlPermission());
    }

    @Override
    public void execStore() throws ProcessingException {
      ICourseControlProcessService service = BEANS.get(ICourseControlProcessService.class);
      CourseControlFormData formData = new CourseControlFormData();
      exportFormData(formData);
      formData = service.store(formData);
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      ICourseControlProcessService service = BEANS.get(ICourseControlProcessService.class);
      CourseControlFormData formData = new CourseControlFormData();
      exportFormData(formData);
      formData = service.prepareCreate(formData);
      importFormData(formData);
    }

    @Override
    public void execStore() throws ProcessingException {
      ICourseControlProcessService service = BEANS.get(ICourseControlProcessService.class);
      CourseControlFormData formData = new CourseControlFormData();
      exportFormData(formData);
      formData = service.create(formData);
      importFormData(formData);
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
  public Long getCourseControlNr() {
    return m_courseControlNr;
  }

  @FormData
  public void setCourseControlNr(Long courseControlNr) {
    m_courseControlNr = courseControlNr;
  }

  public VariantBox getVariantBox() {
    return getFieldByClass(VariantBox.class);
  }

  private void validateForkFields() {
    // TODO
  }

}
