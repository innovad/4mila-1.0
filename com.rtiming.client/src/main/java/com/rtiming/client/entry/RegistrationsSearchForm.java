package com.rtiming.client.entry;

import org.eclipse.scout.commons.annotations.FormData;
import org.eclipse.scout.commons.annotations.FormData.SdkCommand;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractSearchForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractResetButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractSearchButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.tabbox.AbstractTabBox;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.club.AbstractClubSearchBox;
import com.rtiming.client.ecard.AbstractECardSearchBox;
import com.rtiming.client.entry.RegistrationsSearchForm.MainBox.ResetButton;
import com.rtiming.client.entry.RegistrationsSearchForm.MainBox.SearchButton;
import com.rtiming.client.entry.RegistrationsSearchForm.MainBox.TabBox;
import com.rtiming.client.entry.RegistrationsSearchForm.MainBox.TabBox.AdditionalInformationBox;
import com.rtiming.client.runner.AbstractRunnerDetailsSearchBox;
import com.rtiming.client.runner.AbstractRunnerSearchBox;
import com.rtiming.client.settings.addinfo.AbstractAdditionalInformationSearchBox;
import com.rtiming.client.settings.city.AbstractCitySearchBox;
import com.rtiming.shared.common.EntityCodeType;
import com.rtiming.shared.entry.RegistrationsSearchFormData;

@FormData(value = RegistrationsSearchFormData.class, sdkCommand = SdkCommand.CREATE)
public class RegistrationsSearchForm extends AbstractSearchForm {

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Registrations");
  }

  @Override
  protected void execResetSearchFilter(SearchFilter searchFilter) throws ProcessingException {
    super.execResetSearchFilter(searchFilter);
    RegistrationsSearchFormData formData = new RegistrationsSearchFormData();
    exportFormData(formData);
    searchFilter.setFormData(formData);
  }

  public RegistrationsSearchForm() throws ProcessingException {
    super();
  }

  @Override
  public void start() throws ProcessingException {
    startInternal(new SearchHandler());
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
      public class RegistrationBox extends AbstractRegistrationSearchBox {
      }

      @Order(20.0)
      public class RunnerBox extends AbstractRunnerSearchBox {
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

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("ResetButton");
      }
    }

    @Order(30.0)
    public class SearchButton extends AbstractSearchButton {

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("SearchButton");
      }
    }
  }

  public AdditionalInformationBox getAdditionalInformationBox() {
    return getFieldByClass(AdditionalInformationBox.class);
  }

  public class SearchHandler extends AbstractFormHandler {
  }
}
