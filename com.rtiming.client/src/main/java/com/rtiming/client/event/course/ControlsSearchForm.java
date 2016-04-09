package com.rtiming.client.event.course;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractSearchForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractRadioButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractResetButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractSearchButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.radiobuttongroup.AbstractRadioButtonGroup;
import org.eclipse.scout.rt.client.ui.form.fields.tabbox.AbstractTabBox;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.event.course.ControlsSearchForm.MainBox.ResetButton;
import com.rtiming.client.event.course.ControlsSearchForm.MainBox.SearchButton;
import com.rtiming.client.event.course.ControlsSearchForm.MainBox.TabBox;
import com.rtiming.client.event.course.ControlsSearchForm.MainBox.TabBox.GroupBox;
import com.rtiming.client.event.course.ControlsSearchForm.MainBox.TabBox.GroupBox.ActiveGroup;
import com.rtiming.client.event.course.ControlsSearchForm.MainBox.TabBox.GroupBox.ActiveGroup.AllButton;
import com.rtiming.client.event.course.ControlsSearchForm.MainBox.TabBox.GroupBox.ActiveGroup.NoButton;
import com.rtiming.client.event.course.ControlsSearchForm.MainBox.TabBox.GroupBox.ActiveGroup.YesButton;
import com.rtiming.shared.Texts;
import com.rtiming.shared.event.course.ControlsSearchFormData;

@FormData(value = ControlsSearchFormData.class, sdkCommand = SdkCommand.CREATE)
public class ControlsSearchForm extends AbstractSearchForm {

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Controls");
  }

  public ControlsSearchForm() throws ProcessingException {
    super();
  }

  @Override
  protected void execInitForm() throws ProcessingException {
    getActiveGroup().setValue(true);
  }

  @Override
  protected void execResetSearchFilter(SearchFilter searchFilter) throws ProcessingException {
    super.execResetSearchFilter(searchFilter);
    ControlsSearchFormData formData = new ControlsSearchFormData();
    exportFormData(formData);
    searchFilter.setFormData(formData);
  }

  @Override
  public void start() throws ProcessingException {
    startInternal(new SearchHandler());
  }

  public ActiveGroup getActiveGroup() {
    return getFieldByClass(ActiveGroup.class);
  }

  public AllButton getAllButton() {
    return getFieldByClass(AllButton.class);
  }

  public GroupBox getGroupBox() {
    return getFieldByClass(GroupBox.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public NoButton getNoButton() {
    return getFieldByClass(NoButton.class);
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

  public YesButton getYesButton() {
    return getFieldByClass(YesButton.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(20.0)
    public class TabBox extends AbstractTabBox {

      @Order(10.0)
      public class GroupBox extends AbstractGroupBox {

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Controls");
        }

        @Order(10.0)
        public class ActiveGroup extends AbstractRadioButtonGroup<Boolean> {

          @Override
          protected String getConfiguredLabel() {
            return Texts.get("Active");
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
      }
    }

    @Order(30.0)
    public class ResetButton extends AbstractResetButton {

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("ResetButton");
      }
    }

    @Order(40.0)
    public class SearchButton extends AbstractSearchButton {

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("SearchButton");
      }
    }
  }

  public class SearchHandler extends AbstractFormHandler {
  }
}
