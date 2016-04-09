package com.rtiming.client.runner;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractSearchForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractResetButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractSearchButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.tabbox.AbstractTabBox;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.club.AbstractClubSearchBox;
import com.rtiming.client.ecard.AbstractECardSearchBox;
import com.rtiming.client.runner.AbstractRunnerDetailsSearchBox.BirthdateBox;
import com.rtiming.client.runner.AbstractRunnerDetailsSearchBox.YearBox;
import com.rtiming.client.runner.AbstractRunnerSearchBox.ExtKeyField;
import com.rtiming.client.runner.AbstractRunnerSearchBox.NameField;
import com.rtiming.client.runner.RunnersSearchForm.MainBox.ResetButton;
import com.rtiming.client.runner.RunnersSearchForm.MainBox.SearchButton;
import com.rtiming.client.runner.RunnersSearchForm.MainBox.TabBox;
import com.rtiming.client.runner.RunnersSearchForm.MainBox.TabBox.AdditionalInformationBox;
import com.rtiming.client.runner.RunnersSearchForm.MainBox.TabBox.CityBox;
import com.rtiming.client.runner.RunnersSearchForm.MainBox.TabBox.DetailsBox;
import com.rtiming.client.runner.RunnersSearchForm.MainBox.TabBox.ECardBox;
import com.rtiming.client.runner.RunnersSearchForm.MainBox.TabBox.RunnerBox;
import com.rtiming.client.settings.addinfo.AbstractAdditionalInformationSearchBox;
import com.rtiming.client.settings.city.AbstractCitySearchBox;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.EntityCodeType;
import com.rtiming.shared.runner.RunnersSearchFormData;

@FormData(value = RunnersSearchFormData.class, sdkCommand = SdkCommand.CREATE)
public class RunnersSearchForm extends AbstractSearchForm {

  public RunnersSearchForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Runners");
  }

  public AdditionalInformationBox getAdditionalInformationBox() {
    return getFieldByClass(AdditionalInformationBox.class);
  }

  @Override
  protected void execResetSearchFilter(SearchFilter searchFilter) throws ProcessingException {
    super.execResetSearchFilter(searchFilter);
    RunnersSearchFormData formData = new RunnersSearchFormData();
    exportFormData(formData);
    searchFilter.setFormData(formData);
  }

  @Override
  public void start() throws ProcessingException {
    startInternal(new SearchHandler());
  }

  public DetailsBox getDetailsBox() {
    return getFieldByClass(DetailsBox.class);
  }

  public ECardBox getECardBox() {
    return getFieldByClass(ECardBox.class);
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

  public CityBox getAddressBox() {
    return getFieldByClass(CityBox.class);
  }

  public BirthdateBox getBirthdateBox() {
    return getFieldByClass(BirthdateBox.class);
  }

  public ExtKeyField getExtKeyField() {
    return getFieldByClass(ExtKeyField.class);
  }

  public RunnerBox getFieldBox() {
    return getFieldByClass(RunnerBox.class);
  }

  public NameField getNameField() {
    return getFieldByClass(NameField.class);
  }

  public YearBox getYearBox() {
    return getFieldByClass(YearBox.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class TabBox extends AbstractTabBox {

      @Order(10.0)
      public class RunnerBox extends AbstractRunnerSearchBox {

      }

      @Order(20.0)
      public class DetailsBox extends AbstractRunnerDetailsSearchBox {

      }

      @Order(30.0)
      public class ECardBox extends AbstractECardSearchBox {

      }

      @Order(40.0)
      public class ClubBox extends AbstractClubSearchBox {

      }

      @Order(50.0)
      public class CityBox extends AbstractCitySearchBox {

      }

      @Order(60.0)
      public class AdditionalInformationBox extends AbstractAdditionalInformationSearchBox {

        @Override
        protected Long getEntityUid() {
          return EntityCodeType.RunnerCode.ID;
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
