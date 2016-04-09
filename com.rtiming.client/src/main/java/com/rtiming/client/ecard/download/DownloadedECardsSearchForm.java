package com.rtiming.client.ecard.download;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractSearchForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractRadioButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractResetButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractSearchButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.listbox.AbstractListBox;
import org.eclipse.scout.rt.client.ui.form.fields.radiobuttongroup.AbstractRadioButtonGroup;
import org.eclipse.scout.rt.client.ui.form.fields.sequencebox.AbstractSequenceBox;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.client.ui.form.fields.tabbox.AbstractTabBox;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

import com.rtiming.client.common.ui.fields.AbstractDefaultDateTimeField;
import com.rtiming.client.ecard.download.DownloadedECardsSearchForm.MainBox.ResetButton;
import com.rtiming.client.ecard.download.DownloadedECardsSearchForm.MainBox.SearchButton;
import com.rtiming.client.ecard.download.DownloadedECardsSearchForm.MainBox.TabBox;
import com.rtiming.client.ecard.download.DownloadedECardsSearchForm.MainBox.TabBox.EventBox;
import com.rtiming.client.ecard.download.DownloadedECardsSearchForm.MainBox.TabBox.EventBox.ClazzField;
import com.rtiming.client.ecard.download.DownloadedECardsSearchForm.MainBox.TabBox.EventBox.CourseField;
import com.rtiming.client.ecard.download.DownloadedECardsSearchForm.MainBox.TabBox.EventBox.EventField;
import com.rtiming.client.ecard.download.DownloadedECardsSearchForm.MainBox.TabBox.FieldBox;
import com.rtiming.client.ecard.download.DownloadedECardsSearchForm.MainBox.TabBox.FieldBox.DownloadedOnBox;
import com.rtiming.client.ecard.download.DownloadedECardsSearchForm.MainBox.TabBox.FieldBox.DownloadedOnBox.DownloadedOnFrom;
import com.rtiming.client.ecard.download.DownloadedECardsSearchForm.MainBox.TabBox.FieldBox.DownloadedOnBox.DownloadedOnTo;
import com.rtiming.client.ecard.download.DownloadedECardsSearchForm.MainBox.TabBox.FieldBox.ECardField;
import com.rtiming.client.ecard.download.DownloadedECardsSearchForm.MainBox.TabBox.FieldBox.RaceStatusField;
import com.rtiming.client.ecard.download.DownloadedECardsSearchForm.MainBox.TabBox.FieldBox.RunnerAssignedGroup;
import com.rtiming.client.ecard.download.DownloadedECardsSearchForm.MainBox.TabBox.FieldBox.RunnerAssignedGroup.AllButton;
import com.rtiming.client.ecard.download.DownloadedECardsSearchForm.MainBox.TabBox.FieldBox.RunnerAssignedGroup.NoButton;
import com.rtiming.client.ecard.download.DownloadedECardsSearchForm.MainBox.TabBox.FieldBox.RunnerAssignedGroup.YesButton;
import com.rtiming.client.ecard.download.DownloadedECardsSearchForm.MainBox.TabBox.FieldBox.RunnerField;
import com.rtiming.client.event.AbstractEventField;
import com.rtiming.shared.Texts;
import com.rtiming.shared.ecard.download.DownloadedECards;
import com.rtiming.shared.ecard.download.DownloadedECardsSearchFormData;
import com.rtiming.shared.event.course.CourseLookupCall;
import com.rtiming.shared.race.RaceStatusCodeType;
import com.rtiming.shared.settings.IDefaultProcessService;
import com.rtiming.shared.settings.clazz.ClazzLookupCall;

@FormData(value = DownloadedECardsSearchFormData.class, sdkCommand = SdkCommand.CREATE)
public class DownloadedECardsSearchForm extends AbstractSearchForm {

  private DownloadedECards m_presentationType;

  public DownloadedECardsSearchForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("DownloadedECards");
  }

  @Override
  protected void execInitForm() throws ProcessingException {
    getEventField().setValue(BEANS.get(IDefaultProcessService.class).getDefaultEventNr());

    if (getPresentationType() != null) {
      switch (getPresentationType()) {
        case ALL:
          //nop
          break;
        case DUPLICATE:
          // nop
          break;
        case MISSING_PUNCH:
          getRaceStatusField().uncheckAllKeys();
          getRaceStatusField().checkKey(RaceStatusCodeType.MissingPunchCode.ID);
          getRaceStatusField().setEnabled(false);
          break;
        case ENTRY_NOT_FOUND:
          getRunnerAssignedGroup().setValue(false);
          getRunnerAssignedGroup().setEnabled(false);
          break;
        default:
          break;
      }
    }
  }

  @Override
  protected void execResetSearchFilter(SearchFilter searchFilter) throws ProcessingException {
    super.execResetSearchFilter(searchFilter);
    DownloadedECardsSearchFormData formData = new DownloadedECardsSearchFormData();
    exportFormData(formData);
    searchFilter.setFormData(formData);
  }

  @FormData
  public DownloadedECards getPresentationType() {
    return m_presentationType;
  }

  @FormData
  public void setPresentationType(DownloadedECards presentationType) {
    m_presentationType = presentationType;
  }

  @Override
  public void start() throws ProcessingException {
    startInternal(new SearchHandler());
  }

  public AllButton getAllButton() {
    return getFieldByClass(AllButton.class);
  }

  public ClazzField getClazzField() {
    return getFieldByClass(ClazzField.class);
  }

  public CourseField getCourseField() {
    return getFieldByClass(CourseField.class);
  }

  public DownloadedOnBox getDownloadedOnBox() {
    return getFieldByClass(DownloadedOnBox.class);
  }

  public DownloadedOnFrom getDownloadedOnFrom() {
    return getFieldByClass(DownloadedOnFrom.class);
  }

  public DownloadedOnTo getDownloadedOnTo() {
    return getFieldByClass(DownloadedOnTo.class);
  }

  public ECardField getECardField() {
    return getFieldByClass(ECardField.class);
  }

  public EventBox getEventBox() {
    return getFieldByClass(EventBox.class);
  }

  public EventField getEventField() {
    return getFieldByClass(EventField.class);
  }

  public FieldBox getFieldBox() {
    return getFieldByClass(FieldBox.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public NoButton getNoButton() {
    return getFieldByClass(NoButton.class);
  }

  public RaceStatusField getRaceStatusField() {
    return getFieldByClass(RaceStatusField.class);
  }

  public ResetButton getResetButton() {
    return getFieldByClass(ResetButton.class);
  }

  public RunnerAssignedGroup getRunnerAssignedGroup() {
    return getFieldByClass(RunnerAssignedGroup.class);
  }

  public RunnerField getRunnerField() {
    return getFieldByClass(RunnerField.class);
  }

  public SearchButton getSearchButton() {
    return getFieldByClass(SearchButton.class);
  }

  public TabBox getTabBox() {
    return getFieldByClass(TabBox.class);
  }

  public YesButton getYesButton() {
    return getFieldByClass(YesButton.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class TabBox extends AbstractTabBox {

      @Order(10.0)
      public class FieldBox extends AbstractGroupBox {

        @Override
        public String getConfiguredLabel() {
          return Texts.get("DownloadedECards");
        }

        @Order(10.0)
        public class RunnerField extends AbstractStringField {

          @Override
          protected String getConfiguredLabel() {
            return Texts.get("Runner");
          }
        }

        @Order(20.0)
        public class ECardField extends AbstractStringField {

          @Override
          protected String getConfiguredLabel() {
            return Texts.get("ECard");
          }
        }

        @Order(30.0)
        public class DownloadedOnBox extends AbstractSequenceBox {

          @Override
          protected String getConfiguredLabel() {
            return Texts.get("DownloadedOn");
          }

          @Order(10.0)
          public class DownloadedOnFrom extends AbstractDefaultDateTimeField {

            @Override
            protected String getConfiguredLabel() {
              return ScoutTexts.get("from");
            }
          }

          @Order(20.0)
          public class DownloadedOnTo extends AbstractDefaultDateTimeField {

            @Override
            protected String getConfiguredLabel() {
              return ScoutTexts.get("to");
            }
          }
        }

        @Order(40.0)
        public class RunnerAssignedGroup extends AbstractRadioButtonGroup<Boolean> {

          @Override
          protected String getConfiguredLabel() {
            return Texts.get("RunnerAssigned");
          }

          @Order(10.0)
          public class YesButton extends AbstractRadioButton {

            @Override
            protected String getConfiguredLabel() {
              return ScoutTexts.get("Yes");
            }

            @Override
            protected Object getConfiguredRadioValue() {
              return true;
            }
          }

          @Order(20.0)
          public class NoButton extends AbstractRadioButton {

            @Override
            protected String getConfiguredLabel() {
              return ScoutTexts.get("No");
            }

            @Override
            protected Object getConfiguredRadioValue() {
              return false;
            }
          }

          @Order(30.0)
          public class AllButton extends AbstractRadioButton {

            @Override
            protected String getConfiguredLabel() {
              return ScoutTexts.get("All");
            }
          }
        }

        @Order(50.0)
        public class RaceStatusField extends AbstractListBox<Long> {

          @Override
          protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
            return RaceStatusCodeType.class;
          }

          @Override
          protected int getConfiguredGridH() {
            return 4;
          }

          @Override
          protected String getConfiguredLabel() {
            return Texts.get("RaceStatus");
          }
        }
      }

      @Order(20.0)
      public class EventBox extends AbstractGroupBox {

        @Override
        protected String getConfiguredLabel() {
          return Texts.get("Events");
        }

        @Order(10.0)
        public class EventField extends AbstractEventField {

          @Override
          protected boolean getConfiguredMandatory() {
            return true;
          }

          @Override
          protected void execChangedValue() throws ProcessingException {
            getCourseField().loadListBoxData();
            getClazzField().loadListBoxData();
          }
        }

        @Order(20.0)
        public class CourseField extends AbstractListBox<Long> {

          @Override
          protected int getConfiguredGridH() {
            return 3;
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
          protected void execPrepareLookup(ILookupCall call) throws ProcessingException {
            ((CourseLookupCall) call).setEventNr(getEventField().getValue());
          }
        }

        @Order(30.0)
        public class ClazzField extends AbstractListBox<Long> {

          @Override
          protected int getConfiguredGridH() {
            return 4;
          }

          @Override
          protected String getConfiguredLabel() {
            return Texts.get("Class");
          }

          @Override
          protected Class<? extends LookupCall<Long>> getConfiguredLookupCall() {
            return ClazzLookupCall.class;
          }

          @Override
          protected void execPrepareLookup(ILookupCall call) throws ProcessingException {
            ((ClazzLookupCall) call).setEventNr(getEventField().getValue());
          }
        }
      }
    }

    @Order(20.0)
    public class ResetButton extends AbstractResetButton {
    }

    @Order(30.0)
    public class SearchButton extends AbstractSearchButton {
    }
  }

  public class SearchHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
    }
  }
}
