package com.rtiming.client.entry;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractSearchForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.IValueField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractRadioButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractResetButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractSearchButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.listbox.AbstractListBox;
import org.eclipse.scout.rt.client.ui.form.fields.radiobuttongroup.AbstractRadioButtonGroup;
import org.eclipse.scout.rt.client.ui.form.fields.sequencebox.AbstractSequenceBox;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.client.ui.form.fields.tabbox.AbstractTabBox;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

import com.rtiming.client.club.AbstractClubSearchBox;
import com.rtiming.client.common.ui.fields.AbstractDefaultDateTimeWithSecondsField;
import com.rtiming.client.ecard.AbstractECardSearchBox;
import com.rtiming.client.entry.EntriesSearchForm.MainBox.ResetButton;
import com.rtiming.client.entry.EntriesSearchForm.MainBox.TabBox.AdditionalInformationBox;
import com.rtiming.client.entry.EntriesSearchForm.MainBox.TabBox.StartListBox;
import com.rtiming.client.entry.EntriesSearchForm.MainBox.TabBox.StartListBox.EventField;
import com.rtiming.client.entry.EntriesSearchForm.MainBox.TabBox.StartListBox.StartField;
import com.rtiming.client.entry.EntriesSearchForm.MainBox.TabBox.StartListBox.StartTimeBox;
import com.rtiming.client.entry.EntriesSearchForm.MainBox.TabBox.StartListBox.StartTimeBox.StartTimeFrom;
import com.rtiming.client.entry.EntriesSearchForm.MainBox.TabBox.StartListBox.StartTimeBox.StartTimeTo;
import com.rtiming.client.entry.EntriesSearchForm.MainBox.TabBox.StartListBox.StartTimeGroup;
import com.rtiming.client.entry.EntriesSearchForm.MainBox.TabBox.StartListBox.StartTimeGroup.AllButton;
import com.rtiming.client.entry.EntriesSearchForm.MainBox.TabBox.StartListBox.StartTimeGroup.NoButton;
import com.rtiming.client.entry.EntriesSearchForm.MainBox.TabBox.StartListBox.StartTimeGroup.YesButton;
import com.rtiming.client.entry.EntriesSearchForm.MainBox.TabBox.StartListBox.StartblocksField;
import com.rtiming.client.event.AbstractEventField;
import com.rtiming.client.runner.AbstractRunnerDetailsSearchBox;
import com.rtiming.client.runner.AbstractRunnerSearchBox;
import com.rtiming.client.settings.addinfo.AbstractAdditionalInformationSearchBox;
import com.rtiming.client.settings.city.AbstractCitySearchBox;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.EntityCodeType;
import com.rtiming.shared.common.database.sql.EventBean;
import com.rtiming.shared.entry.EntriesSearchFormData;
import com.rtiming.shared.entry.startblock.StartblockCodeType;
import com.rtiming.shared.event.IEventProcessService;
import com.rtiming.shared.event.course.ControlLookupCall;
import com.rtiming.shared.event.course.ControlTypeCodeType;

@FormData(value = EntriesSearchFormData.class, sdkCommand = SdkCommand.CREATE)
public class EntriesSearchForm extends AbstractSearchForm {

  public EntriesSearchForm() throws ProcessingException {
    super();
  }

  public AdditionalInformationBox getAdditionalInformationBox() {
    return getFieldByClass(AdditionalInformationBox.class);
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Entries");
  }

  @Override
  protected void execResetSearchFilter(SearchFilter searchFilter) throws ProcessingException {
    super.execResetSearchFilter(searchFilter);
    EntriesSearchFormData formData = new EntriesSearchFormData();
    exportFormData(formData);
    searchFilter.setFormData(formData);
  }

  @Override
  public void start() throws ProcessingException {
    startInternal(new SearchHandler());
  }

  public AllButton getAllButton() {
    return getFieldByClass(AllButton.class);
  }

  public EventField getEventField() {
    return getFieldByClass(EventField.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public NoButton getNoButton() {
    return getFieldByClass(NoButton.class);
  }

  public StartField getStartField() {
    return getFieldByClass(StartField.class);
  }

  public StartListBox getStartListBox() {
    return getFieldByClass(StartListBox.class);
  }

  public StartTimeBox getStartTimeBox() {
    return getFieldByClass(StartTimeBox.class);
  }

  public StartTimeFrom getStartTimeFrom() {
    return getFieldByClass(StartTimeFrom.class);
  }

  public StartTimeGroup getStartTimeGroup() {
    return getFieldByClass(StartTimeGroup.class);
  }

  public StartTimeTo getStartTimeTo() {
    return getFieldByClass(StartTimeTo.class);
  }

  public StartblocksField getStartblocksField() {
    return getFieldByClass(StartblocksField.class);
  }

  public YesButton getYesButton() {
    return getFieldByClass(YesButton.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class TabBox extends AbstractTabBox {

      @Order(10.0)
      public class RunnerBox extends AbstractRunnerSearchBox {
      }

      @Order(20.0)
      public class StartListBox extends AbstractGroupBox {

        @Override
        protected String getConfiguredLabel() {
          return Texts.get("StartList");
        }

        @Order(10.0)
        public class StartTimeBox extends AbstractSequenceBox {

          @Override
          protected String getConfiguredLabel() {
            return Texts.get("StartTime");
          }

          @Order(10.0)
          public class StartTimeFrom extends AbstractDefaultDateTimeWithSecondsField {

            @Override
            protected String getConfiguredLabel() {
              return ScoutTexts.get("from");
            }

            @Override
            protected boolean getConfiguredMasterRequired() {
              return true;
            }

            @Override
            protected Class<? extends IValueField> getConfiguredMasterField() {
              return EventField.class;
            }

          }

          @Order(20.0)
          public class StartTimeTo extends AbstractDefaultDateTimeWithSecondsField {

            @Override
            protected String getConfiguredLabel() {
              return ScoutTexts.get("to");
            }

            @Override
            protected boolean getConfiguredMasterRequired() {
              return true;
            }

            @Override
            protected Class<? extends IValueField> getConfiguredMasterField() {
              return EventField.class;
            }

          }
        }

        @Order(20.0)
        public class StartField extends AbstractSmartField<Long> {

          @Override
          protected String getConfiguredLabel() {
            return Texts.get("Start");
          }

          @Override
          protected Class<? extends LookupCall<Long>> getConfiguredLookupCall() {
            return ControlLookupCall.class;
          }

          @Override
          protected void execPrepareLookup(ILookupCall call) throws ProcessingException {
            ((ControlLookupCall) call).setEventNr(getEventField().getValue());
            ((ControlLookupCall) call).setTypeUid(ControlTypeCodeType.StartCode.ID);
            ((ControlLookupCall) call).setIsDisplayEvent(true);
          }

        }

        @Order(30.0)
        public class EventField extends AbstractEventField {

          @Override
          protected void execChangedValue() throws ProcessingException {
            Long eventNr = getValue();
            if (eventNr != null) {
              EventBean event = new EventBean();
              event.setEventNr(eventNr);
              event = BEANS.get(IEventProcessService.class).load(event);
              // set from time to evt zero time
              getStartTimeFrom().setValue(event.getEvtZero());
              // set to time to finish time
              getStartTimeTo().setValue(event.getEvtFinish());
            }
            else {
              getStartTimeFrom().setValue(null);
              getStartTimeTo().setValue(null);
            }
          }

        }

        @Order(40.0)
        public class StartTimeGroup extends AbstractRadioButtonGroup<Boolean> {

          @Override
          protected String getConfiguredLabel() {
            return Texts.get("StartTime");
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
        public class StartblocksField extends AbstractListBox<Long> {

          @Override
          protected int getConfiguredGridH() {
            return 4;
          }

          @Override
          protected String getConfiguredLabel() {
            return Texts.get("Startblocks");
          }

          @Override
          protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
            return StartblockCodeType.class;
          }
        }
      }

      @Order(30.0)
      public class DetailsBox extends AbstractRunnerDetailsSearchBox {

      }

      @Order(40.0)
      public class ECardBox extends AbstractECardSearchBox {

      }

      @Order(50.0)
      public class ClubBox extends AbstractClubSearchBox {

      }

      @Order(60.0)
      public class CityBox extends AbstractCitySearchBox {

      }

      @Order(70.0)
      public class AdditionalInformationBox extends AbstractAdditionalInformationSearchBox {

        @Override
        protected Long getEntityUid() {
          return EntityCodeType.EntryCode.ID;
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
  }

  public ResetButton getResetButton() {
    return getFieldByClass(ResetButton.class);
  }

}
