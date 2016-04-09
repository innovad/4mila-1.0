package com.rtiming.client.result;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractSearchForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractResetButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractSearchButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.tabbox.AbstractTabBox;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.event.AbstractEventField;
import com.rtiming.client.result.SingleEventSearchForm.MainBox.ResetButton;
import com.rtiming.client.result.SingleEventSearchForm.MainBox.SearchButton;
import com.rtiming.client.result.SingleEventSearchForm.MainBox.TabBox;
import com.rtiming.client.result.SingleEventSearchForm.MainBox.TabBox.FieldBox;
import com.rtiming.client.result.SingleEventSearchForm.MainBox.TabBox.FieldBox.EventField;
import com.rtiming.shared.Texts;
import com.rtiming.shared.results.SingleEventSearchFormData;
import com.rtiming.shared.settings.IDefaultProcessService;

@FormData(value = SingleEventSearchFormData.class, sdkCommand = SdkCommand.CREATE)
public class SingleEventSearchForm extends AbstractSearchForm {

  public SingleEventSearchForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Events");
  }

  @Override
  protected void execInitForm() throws ProcessingException {
    getEventField().setValue(BEANS.get(IDefaultProcessService.class).getDefaultEventNr());
  }

  @Override
  protected void execResetSearchFilter(SearchFilter searchFilter) throws ProcessingException {
    super.execResetSearchFilter(searchFilter);
    SingleEventSearchFormData formData = new SingleEventSearchFormData();
    exportFormData(formData);
    searchFilter.setFormData(formData);
  }

  @Override
  public void start() throws ProcessingException {
    startInternal(new SearchHandler());
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

  public ResetButton getResetButton() {
    return getFieldByClass(ResetButton.class);
  }

  public SearchButton getSearchButton() {
    return getFieldByClass(SearchButton.class);
  }

  public TabBox getTabBox() {
    return getFieldByClass(TabBox.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class TabBox extends AbstractTabBox {

      @Order(10.0)
      public class FieldBox extends AbstractGroupBox {

        @Override
        public String getConfiguredLabel() {
          return Texts.get("SearchCriteria");
        }

        @Order(10.0)
        public class EventField extends AbstractEventField {
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
