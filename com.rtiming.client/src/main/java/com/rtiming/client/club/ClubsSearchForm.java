package com.rtiming.client.club;

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

import com.rtiming.client.club.AbstractClubSearchBox.ContactPersonField;
import com.rtiming.client.club.ClubsSearchForm.MainBox.TabBox.AdditionalInformationBox;
import com.rtiming.client.settings.addinfo.AbstractAdditionalInformationSearchBox;
import com.rtiming.shared.Texts;
import com.rtiming.shared.club.ClubsSearchFormData;
import com.rtiming.shared.common.EntityCodeType;

@FormData(value = ClubsSearchFormData.class, sdkCommand = SdkCommand.CREATE)
public class ClubsSearchForm extends AbstractSearchForm {

  public ClubsSearchForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Clubs");
  }

  @Override
  protected void execResetSearchFilter(SearchFilter searchFilter) throws ProcessingException {
    super.execResetSearchFilter(searchFilter);
    ClubsSearchFormData formData = new ClubsSearchFormData();
    exportFormData(formData);
    searchFilter.setFormData(formData);
  }

  @Override
  public void start() throws ProcessingException {
    startInternal(new SearchHandler());
  }

  public ContactPersonField getContactPersonField() {
    return getFieldByClass(ContactPersonField.class);
  }

  public AdditionalInformationBox getAdditionalInformationBox() {
    return getFieldByClass(AdditionalInformationBox.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class TabBox extends AbstractTabBox {

      @Order(10.0)
      public class ClubBox extends AbstractClubSearchBox {

      }

      @Order(20.0)
      public class AdditionalInformationBox extends AbstractAdditionalInformationSearchBox {

        @Override
        protected Long getEntityUid() {
          return EntityCodeType.ClubCode.ID;
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
}
