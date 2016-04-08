package com.rtiming.client.event;

import org.eclipse.scout.commons.annotations.FormData;
import org.eclipse.scout.commons.annotations.FormData.SdkCommand;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.longfield.AbstractLongField;
import org.eclipse.scout.rt.client.ui.form.fields.sequencebox.AbstractSequenceBox;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

import com.rtiming.client.common.help.AbstractHelpLinkButton;
import com.rtiming.client.common.ui.fields.AbstractSortCodeField;
import com.rtiming.client.event.EventClassForm.MainBox.CancelButton;
import com.rtiming.client.event.EventClassForm.MainBox.ClazzField;
import com.rtiming.client.event.EventClassForm.MainBox.CourseField;
import com.rtiming.client.event.EventClassForm.MainBox.EventField;
import com.rtiming.client.event.EventClassForm.MainBox.OkButton;
import com.rtiming.client.event.EventClassForm.MainBox.OptionsBox;
import com.rtiming.client.event.EventClassForm.MainBox.OptionsBox.CourseGenerationTypeField;
import com.rtiming.client.event.EventClassForm.MainBox.OptionsBox.FeeGroupField;
import com.rtiming.client.event.EventClassForm.MainBox.OptionsBox.SortCodeField;
import com.rtiming.client.event.EventClassForm.MainBox.OptionsBox.TimePrecisionField;
import com.rtiming.client.event.EventClassForm.MainBox.ParentField;
import com.rtiming.client.event.EventClassForm.MainBox.TeamSizeBox;
import com.rtiming.client.event.EventClassForm.MainBox.TeamSizeBox.TeamSizeMaxField;
import com.rtiming.client.event.EventClassForm.MainBox.TeamSizeBox.TeamSizeMinField;
import com.rtiming.client.event.EventClassForm.MainBox.TypeField;
import com.rtiming.client.settings.clazz.AbstractClassField;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.UpdateEventClassPermission;
import com.rtiming.shared.event.EventClassFormData;
import com.rtiming.shared.event.IEventClassProcessService;
import com.rtiming.shared.event.course.ClassTypeCodeType;
import com.rtiming.shared.event.course.CourseLookupCall;
import com.rtiming.shared.race.TimePrecisionCodeType;
import com.rtiming.shared.services.code.CourseGenerationCodeType;
import com.rtiming.shared.settings.fee.FeeGroupLookupCall;

@FormData(value = EventClassFormData.class, sdkCommand = SdkCommand.CREATE)
public class EventClassForm extends AbstractForm {

  private Long m_startlistSettingNr;
  private Long m_clientNr;

  public EventClassForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("EventClass");
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  @Override
  protected void execInitForm() throws ProcessingException {
    if (NumberUtility.nvl(getParentField().getValue(), 0) == 0) {
      // Class
      getParentField().setVisible(false);
      getParentField().setMandatory(false);
      getClazzField().setLabel(Texts.get("Class"));
    }
    else {
      // Leg
      getTypeField().setVisible(false);
      getTypeField().setMandatory(false);
      getParentField().setEnabled(false);
      getTimePrecisionField().setEnabled(false);
      getFeeGroupField().setEnabled(false);
      getCourseGenerationTypeField().setEnabled(false);
    }
  }

  public void startModify() throws ProcessingException {
    startInternal(new ModifyHandler());
  }

  public void startNew() throws ProcessingException {
    startInternal(new NewHandler());
  }

  public CourseField getCourseField() {
    return getFieldByClass(CourseField.class);
  }

  public CourseGenerationTypeField getCourseGenerationTypeField() {
    return getFieldByClass(CourseGenerationTypeField.class);
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

  public SortCodeField getSortCodeField() {
    return getFieldByClass(SortCodeField.class);
  }

  public FeeGroupField getFeeGroupField() {
    return getFieldByClass(FeeGroupField.class);
  }

  public TeamSizeBox getTeamSizeBox() {
    return getFieldByClass(TeamSizeBox.class);
  }

  public TeamSizeMaxField getTeamSizeMaxField() {
    return getFieldByClass(TeamSizeMaxField.class);
  }

  public TeamSizeMinField getTeamSizeMinField() {
    return getFieldByClass(TeamSizeMinField.class);
  }

  public TimePrecisionField getTimePrecisionField() {
    return getFieldByClass(TimePrecisionField.class);
  }

  public TypeField getTypeField() {
    return getFieldByClass(TypeField.class);
  }

  public ClazzField getClazzField() {
    return getFieldByClass(ClazzField.class);
  }

  public OptionsBox getOptionsBox() {
    return getFieldByClass(OptionsBox.class);
  }

  public ParentField getParentField() {
    return getFieldByClass(ParentField.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Override
    protected boolean getConfiguredMandatory() {
      return true;
    }

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

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }
    }

    @Order(20.0)
    public class ParentField extends AbstractClassField {

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }
    }

    @Order(30.0)
    public class ClazzField extends AbstractClassField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Leg");
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

    }

    @Order(35.0)
    public class CourseField extends AbstractSmartField<Long> {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Course");
      }

      @Override
      protected Class<? extends LookupCall<Long>> getConfiguredLookupCall() {
        return CourseLookupCall.class;
      }

      @Override
      protected void execPrepareLookup(ILookupCall call) throws ProcessingException {
        ((CourseLookupCall) call).setEventNr(getEventField().getValue());
      }
    }

    @Order(40.0)
    public class TypeField extends AbstractSmartField<Long> {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Type");
      }

      @Override
      protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
        return ClassTypeCodeType.class;
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

      @Override
      protected void execChangedValue() throws ProcessingException {
        validateClassType();
      }
    }

    @Order(50.0)
    public class TeamSizeBox extends AbstractSequenceBox {

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("TeamSize");
      }

      @Order(10.0)
      public class TeamSizeMinField extends AbstractLongField {

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("from");
        }

        @Override
        protected boolean getConfiguredMandatory() {
          return true;
        }

        @Override
        protected Long getConfiguredMaxValue() {
          return 999999L;
        }

        @Override
        protected Long getConfiguredMinValue() {
          return 1L;
        }
      }

      @Order(20.0)
      public class TeamSizeMaxField extends AbstractLongField {

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("to");
        }

        @Override
        protected boolean getConfiguredMandatory() {
          return true;
        }

        @Override
        protected Long getConfiguredMaxValue() {
          return 999999L;
        }

        @Override
        protected Long getConfiguredMinValue() {
          return 1L;
        }
      }
    }

    @Order(110.0)
    public class OptionsBox extends AbstractGroupBox {

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Options");
      }

      @Order(10.0)
      public class TimePrecisionField extends AbstractSmartField<Long> {

        @Override
        protected String getConfiguredLabel() {
          return Texts.get("TimePrecision");
        }

        @Override
        protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
          return TimePrecisionCodeType.class;
        }

        @Override
        protected boolean getConfiguredMandatory() {
          return true;
        }
      }

      @Order(20.0)
      public class FeeGroupField extends AbstractSmartField<Long> {

        @Override
        protected String getConfiguredLabel() {
          return Texts.get("FeeGroup");
        }

        @Override
        protected Class<? extends LookupCall<Long>> getConfiguredLookupCall() {
          return FeeGroupLookupCall.class;
        }
      }

      @Order(30.0)
      public class CourseGenerationTypeField extends AbstractSmartField<Long> {

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("CourseSetting");
        }

        @Override
        protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
          return CourseGenerationCodeType.class;
        }

        @Override
        protected boolean getConfiguredMandatory() {
          return true;
        }
      }

      @Order(40.0)
      public class SortCodeField extends AbstractSortCodeField {
      }

    }

    @Order(120.0)
    public class HelpLink extends AbstractHelpLinkButton {
    }

    @Order(130.0)
    public class OkButton extends AbstractOkButton {
    }

    @Order(140.0)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  public class ModifyHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IEventClassProcessService service = BEANS.get(IEventClassProcessService.class);
      EventClassFormData formData = new EventClassFormData();
      exportFormData(formData);
      formData = service.load(formData);
      importFormData(formData);
      setEnabledPermission(new UpdateEventClassPermission());

      // validate class type
      validateClassType();
    }

    @Override
    protected void execPostLoad() throws ProcessingException {
      getClazzField().setEnabled(false);
      getParentField().setEnabled(false);
    }

    @Override
    public void execStore() throws ProcessingException {
      IEventClassProcessService service = BEANS.get(IEventClassProcessService.class);
      EventClassFormData formData = new EventClassFormData();
      exportFormData(formData);
      formData = service.store(formData);
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IEventClassProcessService service = BEANS.get(IEventClassProcessService.class);
      EventClassFormData formData = new EventClassFormData();
      exportFormData(formData);
      formData = service.prepareCreate(formData);
      importFormData(formData);

      validateClassType();
    }

    @Override
    public void execStore() throws ProcessingException {
      IEventClassProcessService service = BEANS.get(IEventClassProcessService.class);
      EventClassFormData formData = new EventClassFormData();
      exportFormData(formData);
      formData = service.create(formData);
    }
  }

  @FormData
  public Long getStartlistSettingNr() {
    return m_startlistSettingNr;
  }

  @FormData
  public void setStartlistSettingNr(Long startlistSettingNr) {
    m_startlistSettingNr = startlistSettingNr;
  }

  @FormData
  public Long getClientNr() {
    return m_clientNr;
  }

  @FormData
  public void setClientNr(Long clientNr) {
    m_clientNr = clientNr;
  }

  private void validateClassType() {
    if (ClassTypeCodeType.isLegClassType(getTypeField().getValue())) {
      getCourseField().setEnabled(true);
    }
    else {
      getCourseField().setValue(null);
      getCourseField().setEnabled(false);
    }

    if (ClassTypeCodeType.isTeamClassType(getTypeField().getValue())) {
      getTeamSizeBox().setVisible(true);
    }
    else {
      getTeamSizeBox().setVisible(false);
      getTeamSizeMinField().setValue(1L);
      getTeamSizeMaxField().setValue(1L);
    }
  }
}
