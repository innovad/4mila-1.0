package com.rtiming.client.setup;

import java.util.Currency;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.client.ui.form.fields.tablefield.AbstractTableField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.services.common.code.ICode;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;

import com.rtiming.client.settings.user.UsersTablePage;
import com.rtiming.client.setup.SetupForm.MainBox.SettingsBox;
import com.rtiming.client.setup.SetupForm.MainBox.SettingsBox.CountryField;
import com.rtiming.client.setup.SetupForm.MainBox.SettingsBox.CurrencyField;
import com.rtiming.client.setup.SetupForm.MainBox.SettingsBox.LanguageField;
import com.rtiming.client.setup.SetupForm.MainBox.UserBox;
import com.rtiming.client.setup.SetupForm.MainBox.UserBox.UserField;
import com.rtiming.client.setup.SetupForm.MainBox.UserBox.UserField.Table;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.AbstractRoleCode;
import com.rtiming.shared.settings.CodeFormData;
import com.rtiming.shared.settings.ICodeProcessService;
import com.rtiming.shared.settings.IDefaultProcessService;
import com.rtiming.shared.settings.city.CountryCodeType;
import com.rtiming.shared.settings.city.CountryFormData;
import com.rtiming.shared.settings.city.ICountryProcessService;
import com.rtiming.shared.settings.currency.CurrencyCodeType;
import com.rtiming.shared.settings.user.IUserProcessService;
import com.rtiming.shared.settings.user.LanguageCodeType;
import com.rtiming.shared.settings.user.RoleCodeType;
import com.rtiming.shared.settings.user.UserFormData;

public class SetupForm extends AbstractForm {

  public SetupForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Setup");
  }

  public void startNew() throws ProcessingException {
    startInternal(new SetupForm.NewHandler());
  }

  public CountryField getCountryField() {
    return getFieldByClass(CountryField.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public SettingsBox getSettingsBox() {
    return getFieldByClass(SettingsBox.class);
  }

  public UserBox getUserBox() {
    return getFieldByClass(UserBox.class);
  }

  public UserField getUserField() {
    return getFieldByClass(UserField.class);
  }

  public LanguageField getLanguageField() {
    return getFieldByClass(LanguageField.class);
  }

  public CurrencyField getCurrencyField() {
    return getFieldByClass(CurrencyField.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class SettingsBox extends AbstractGroupBox {

      @Override
      protected int getConfiguredGridW() {
        return 2;
      }

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Settings");
      }

      @Order(10.0)
      public class LanguageField extends AbstractSmartField<Long> {

        @Override
        protected String getConfiguredLabel() {
          return ScoutTexts.get("Language");
        }

        @Override
        protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
          return LanguageCodeType.class;
        }

        @Override
        protected boolean getConfiguredMandatory() {
          return true;
        }
      }

      @Order(20.0)
      public class CurrencyField extends AbstractSmartField<Long> {

        @Override
        protected String getConfiguredLabel() {
          return Texts.get("Currency");
        }

        @Override
        protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
          return CurrencyCodeType.class;
        }

        @Override
        protected boolean getConfiguredMandatory() {
          return true;
        }
      }

      @Order(30.0)
      public class CountryField extends AbstractSmartField<Long> {

        @Override
        protected String getConfiguredLabel() {
          return Texts.get("Country");
        }

        @Override
        protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
          return CountryCodeType.class;
        }

        @Override
        protected boolean getConfiguredMandatory() {
          return true;
        }
      }
    }

    @Order(30.0)
    public class UserBox extends AbstractGroupBox {

      @Override
      protected int getConfiguredGridW() {
        return 2;
      }

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("User");
      }

      @Order(10.0)
      public class UserField extends AbstractTableField<UserField.Table> {

        @Override
        protected boolean getConfiguredGridUseUiHeight() {
          return true;
        }

        @Override
        protected int getConfiguredGridW() {
          return 2;
        }

        @Override
        protected String getConfiguredLabel() {
          return Texts.get("User");
        }

        @Order(10.0)
        public class Table extends AbstractTable {

          @Override
          protected boolean getConfiguredAutoResizeColumns() {
            return true;
          }

          public PasswordColumn getPasswordColumn() {
            return getColumnSet().getColumnByClass(PasswordColumn.class);
          }

          public RoleColumn getRoleColumn() {
            return getColumnSet().getColumnByClass(RoleColumn.class);
          }

          public UsernameColumn getUsernameColumn() {
            return getColumnSet().getColumnByClass(UsernameColumn.class);
          }

          @Order(10.0)
          public class UsernameColumn extends AbstractStringColumn {

            @Override
            protected boolean getConfiguredEditable() {
              return true;
            }

            @Override
            protected String getConfiguredFont() {
              return "bold";
            }

            @Override
            protected String getConfiguredHeaderText() {
              return ScoutTexts.get("Username");
            }
          }

          @Order(20.0)
          public class PasswordColumn extends AbstractStringColumn {

            @Override
            protected boolean getConfiguredEditable() {
              return true;
            }

            @Override
            protected String getConfiguredHeaderText() {
              return ScoutTexts.get("Password");
            }
          }

          @Order(30.0)
          public class RoleColumn extends AbstractSmartColumn<Long> {

            @Override
            protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
              return RoleCodeType.class;
            }

            @Override
            protected String getConfiguredHeaderText() {
              return Texts.get("Role");
            }
          }
        }
      }
    }

  }

  @Override
  public void validateForm() throws ProcessingException {
    HashSet<String> distinctUsernames = new HashSet<String>();
    for (String username : getUserField().getTable().getUsernameColumn().getValues()) {
      distinctUsernames.add(username);
    }
    if (distinctUsernames.size() < getUserField().getTable().getRowCount()) {
      throw new VetoException(Texts.get("UserNameUniqueErrorMessage"));
    }
    super.validateForm();
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    protected void execLoad() throws ProcessingException {

      Locale locale = Locale.getDefault();
      String countryCode = locale.getCountry();

      CountryFormData country = BEANS.get(ICountryProcessService.class).find(null, countryCode, null);
      if (country.getCountryUid() != null) {
        getCountryField().setValue(country.getCountryUid());
      }

      ICode language = BEANS.get(LanguageCodeType.class).getCodeByExtKey(locale.getLanguage());
      if (language != null) {
        getLanguageField().setValue((Long) language.getId());
      }

      Currency currencyLocale = Currency.getInstance(locale);
      CodeFormData currency = BEANS.get(ICodeProcessService.class).find(currencyLocale.getCurrencyCode(), CurrencyCodeType.ID);
      if (currency.getCodeUid() != null) {
        getCurrencyField().setValue(currency.getCodeUid());
      }

      for (ICode<?> code : BEANS.get(RoleCodeType.class).getCodes()) {
        AbstractRoleCode roleCode = (AbstractRoleCode) code;
        ITableRow row = getUserField().getTable().createRow();
        row = getUserField().getTable().addRow(row);
        getUserField().getTable().getUsernameColumn().setValue(row, roleCode.getDefaultSetupUsername());
        getUserField().getTable().getPasswordColumn().setValue(row, roleCode.getDefaultSetupUsername());
        getUserField().getTable().getRoleColumn().setValue(row, roleCode.getId());
      }

    }

    @Override
    protected void execPostLoad() throws ProcessingException {
      touch();
    }

    @Override
    protected void execStore() throws ProcessingException {
      // defaults
      IDefaultProcessService defaultService = BEANS.get(IDefaultProcessService.class);
      defaultService.setDefaultCountryUid(getCountryField().getValue());
      defaultService.setDefaultCurrencyUid(getCurrencyField().getValue());

      // existing users
      UsersTablePage users = new UsersTablePage();
      users.loadChildren();
      List<String> existingUsers = users.getTable().getUsernameColumn().getValues();

      // users
      Table userTable = getUserField().getTable();
      for (int i = 0; i < userTable.getRowCount(); i++) {
        if (!existingUsers.contains(userTable.getUsernameColumn().getValue(i).toLowerCase())) {
          UserFormData user = new UserFormData();
          user.setUpdatePassword(true);
          user.getUsername().setValue(userTable.getUsernameColumn().getValue(i));
          user.getPassword().setValue(userTable.getPasswordColumn().getValue(i));
          user.getRepeatPassword().setValue(userTable.getPasswordColumn().getValue(i));
          user.getLanguage().setValue(getLanguageField().getValue());
          Set<Long> set = new HashSet<>();
          set.add(userTable.getRoleColumn().getValue(i));
          user.getRoles().setValue(set);
          BEANS.get(IUserProcessService.class).create(user);
        }
      }

    }

  }
}
