package com.rtiming.server.common.web;

import com._4mila._4mila.jaxws.online.Account;
import com.rtiming.shared.settings.account.AccountFormData;

public final class AccountUtility {

  private AccountUtility() {
  }

  public static Account formDataToWsdl(AccountFormData formData) {

    if (formData == null) {
      return null;
    }
    Account account = new Account();
    account.setAccountNr(formData.getAccountNr());
    account.setClientNr(formData.getClientNr());
    account.setEmail(formData.getEMail().getValue());
    account.setPassword(formData.getPassword().getValue());
    account.setUsername(formData.getUsername().getValue());
    account.setFirstName(formData.getFirstName().getValue());
    account.setLastName(formData.getLastName().getValue());

    return account;
  }

  public static AccountFormData wsdlToFormData(Account wsdl) {

    if (wsdl == null) {
      return null;
    }
    AccountFormData account = new AccountFormData();
    account.setAccountNr(wsdl.getAccountNr());
    account.setClientNr(wsdl.getClientNr());
    account.getEMail().setValue(wsdl.getEmail());
    account.getPassword().setValue(wsdl.getPassword());
    account.getUsername().setValue(wsdl.getUsername());
    account.getFirstName().setValue(wsdl.getFirstName());
    account.getLastName().setValue(wsdl.getLastName());

    return account;
  }

}
