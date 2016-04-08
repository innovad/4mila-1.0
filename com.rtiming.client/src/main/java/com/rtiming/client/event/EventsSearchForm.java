package com.rtiming.client.event;

import org.eclipse.scout.commons.annotations.FormData;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractSearchForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractResetButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractSearchButton;
import org.eclipse.scout.rt.client.ui.form.fields.datefield.AbstractDateField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.listbox.AbstractListBox;
import org.eclipse.scout.rt.client.ui.form.fields.sequencebox.AbstractSequenceBox;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.client.ui.form.fields.tabbox.AbstractTabBox;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.event.EventsSearchForm.MainBox.ResetButton;
import com.rtiming.client.event.EventsSearchForm.MainBox.SearchButton;
import com.rtiming.client.event.EventsSearchForm.MainBox.TabBox;
import com.rtiming.client.event.EventsSearchForm.MainBox.TabBox.FieldBox;
import com.rtiming.client.event.EventsSearchForm.MainBox.TabBox.FieldBox.LocationField;
import com.rtiming.client.event.EventsSearchForm.MainBox.TabBox.FieldBox.MappField;
import com.rtiming.client.event.EventsSearchForm.MainBox.TabBox.FieldBox.NameField;
import com.rtiming.client.event.EventsSearchForm.MainBox.TabBox.FieldBox.TypeField;
import com.rtiming.client.event.EventsSearchForm.MainBox.TabBox.FieldBox.ZeroTimeBox;
import com.rtiming.client.event.EventsSearchForm.MainBox.TabBox.FieldBox.ZeroTimeBox.ZeroTimeFrom;
import com.rtiming.client.event.EventsSearchForm.MainBox.TabBox.FieldBox.ZeroTimeBox.ZeroTimeTo;
import com.rtiming.shared.Texts;
import com.rtiming.shared.event.EventTypeCodeType;
import com.rtiming.shared.event.EventsSearchFormData;

@FormData(value = EventsSearchFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class EventsSearchForm extends AbstractSearchForm {

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Events");
  }

  public EventsSearchForm() throws ProcessingException {
    super();
  }

  @Override
  protected void execResetSearchFilter(SearchFilter searchFilter) throws ProcessingException {
    super.execResetSearchFilter(searchFilter);
    EventsSearchFormData formData = new EventsSearchFormData();
    exportFormData(formData);
    searchFilter.setFormData(formData);
  }

  @Override
  public void start() throws ProcessingException {
    startInternal(new EventsSearchForm.SearchHandler());
  }

  public FieldBox getFieldBox() {
    return getFieldByClass(FieldBox.class);
  }

  public LocationField getLocationField() {
    return getFieldByClass(LocationField.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public MappField getMappField() {
    return getFieldByClass(MappField.class);
  }

  public NameField getNameField() {
    return getFieldByClass(NameField.class);
  }

  public ResetButton getResetButton() {
    return getFieldByClass(ResetButton.class);
  }

  public SearchButton getSearchButton() {
    return getFieldByClass(SearchButton.class);
  }

  public TabBox getTabBox() {
    return getFieldByClass(TabBox.class);
  }

  public TypeField getTypeField() {
    return getFieldByClass(TypeField.class);
  }

  public ZeroTimeBox getZeroTimeBox() {
    return getFieldByClass(ZeroTimeBox.class);
  }

  public ZeroTimeFrom getZeroTimeFrom() {
    return getFieldByClass(ZeroTimeFrom.class);
  }

  public ZeroTimeTo getZeroTimeTo() {
    return getFieldByClass(ZeroTimeTo.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class TabBox extends AbstractTabBox {

      @Order(10.0)
      public class FieldBox extends AbstractGroupBox {

        @Override
        public String getConfiguredLabel() {
          return Texts.get("Events");
        }

        @Order(10.0)
        public class NameField extends AbstractStringField {

          @Override
          protected String getConfiguredLabel() {
            return ScoutTexts.get("Name");
          }
        }

        @Order(20.0)
        public class LocationField extends AbstractStringField {

          @Override
          protected String getConfiguredLabel() {
            return Texts.get("Location");
          }
        }

        @Order(30.0)
        public class MappField extends AbstractStringField {

          @Override
          protected String getConfiguredLabel() {
            return Texts.get("Map");
          }
        }

        @Order(40.0)
        public class ZeroTimeBox extends AbstractSequenceBox {

          @Override
          protected String getConfiguredLabel() {
            return Texts.get("ZeroTime");
          }

          @Order(10.0)
          public class ZeroTimeFrom extends AbstractDateField {

            @Override
            protected String getConfiguredLabel() {
              return ScoutTexts.get("from");
            }
          }

          @Order(20.0)
          public class ZeroTimeTo extends AbstractDateField {

            @Override
            protected String getConfiguredLabel() {
              return ScoutTexts.get("to");
            }
          }
        }

        @Order(50.0)
        public class TypeField extends AbstractListBox<Long> {

          @Override
          protected int getConfiguredGridH() {
            return 4;
          }

          @Override
          protected String getConfiguredLabel() {
            return Texts.get("Type");
          }

          @Override
          protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
            return EventTypeCodeType.class;
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
    public void execLoad() {
    }
  }
}
