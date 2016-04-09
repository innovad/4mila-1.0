package com.rtiming.server.settings.city;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.holders.ITableHolder;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.services.common.code.CODES;
import org.eclipse.scout.rt.shared.services.common.code.ICode;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaTypedQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.common.database.jpa.JPAUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.AbstractCodeBoxData;
import com.rtiming.shared.common.AbstractCodeBoxData.Language.LanguageRowData;
import com.rtiming.shared.common.security.permission.CreateCountryPermission;
import com.rtiming.shared.common.security.permission.DeleteCountryPermission;
import com.rtiming.shared.common.security.permission.ReadCountryPermission;
import com.rtiming.shared.common.security.permission.UpdateCountryPermission;
import com.rtiming.shared.dao.RtCountry;
import com.rtiming.shared.dao.RtCountryKey;
import com.rtiming.shared.settings.ICodeProcessService;
import com.rtiming.shared.settings.city.CountryCodeType;
import com.rtiming.shared.settings.city.CountryFormData;
import com.rtiming.shared.settings.city.ICountryProcessService;
import com.rtiming.shared.settings.user.LanguageCodeType;

public class CountryProcessService implements ICountryProcessService {

  @Override
  public CountryFormData prepareCreate(CountryFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateCountryPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    formData.getCodeBox().getCodeTypeUid().setValue(CountryCodeType.ID);
    BEANS.get(ICodeProcessService.class).prepareCreateCodeBox(formData.getCodeBox());

    return formData;
  }

  @Override
  public CountryFormData create(CountryFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateCountryPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    formData.getCodeBox().getCodeTypeUid().setValue(CountryCodeType.ID);
    AbstractCodeBoxData codeBox = BEANS.get(ICodeProcessService.class).createCodeBox(formData.getCodeBox());
    formData.setCountryUid(codeBox.getCodeUid().getValue());
    formData = store(formData);

    return formData;
  }

  @Override
  public CountryFormData load(CountryFormData formData) throws ProcessingException {
    if (!ACCESS.check(new ReadCountryPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    JPAUtility.select("SELECT C.countryCode, C.nation " + "FROM RtCountry C " + "WHERE C.id.countryUid = :countryUid " + "INTO :countryCode, :nation", formData);

    formData.getCodeBox().getCodeUid().setValue(formData.getCountryUid());
    BEANS.get(ICodeProcessService.class).loadCodeBox(formData.getCodeBox());

    return formData;
  }

  @Override
  public CountryFormData store(CountryFormData formData) throws ProcessingException {
    if (!ACCESS.check(new UpdateCountryPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    // check for unique values is done by sql service and db
    RtCountry country = new RtCountry();
    country.setId(RtCountryKey.create(formData.getCountryUid()));
    country.setCountryCode(formData.getCountryCode().getValue());
    country.setNation(formData.getNation().getValue());
    JPA.merge(country);

    BEANS.get(ICodeProcessService.class).storeCodeBox(formData.getCodeBox());

    return formData;
  }

  @Override
  public CountryFormData find(String name, String code, String nation) throws ProcessingException {
    String nameUppercase = null;

    // check input values
    if (name != null) {
      nameUppercase = StringUtility.uppercase(name).trim();
      name = name.trim();
    }
    if (code != null) {
      code = StringUtility.uppercase(code).trim();
    }
    if (nation != null) {
      nation = StringUtility.uppercase(nation).trim();
    }

    Long countryUid = null;
    if (!StringUtility.isNullOrEmpty(nameUppercase) || !StringUtility.isNullOrEmpty(code) || !StringUtility.isNullOrEmpty(nation)) {

      String queryString = "SELECT MAX(C.id.countryUid) " + "FROM RtCountry C " + "INNER JOIN C.rtUc U " + "INNER JOIN U.rtUcls L " + (nameUppercase == null ? "WHERE 1=1 " : "WHERE UPPER(L.codeName) = COALESCE(:nameUppercase,UPPER(L.codeName)) ") + (code == null ? "" : "AND UPPER(C.countryCode) = COALESCE(:code,UPPER(C.countryCode)) ") + (nation == null ? "" : "AND UPPER(C.nation) = COALESCE(:nation,UPPER(C.nation)) ") + "AND C.id.clientNr = :sessionClientNr ";
      FMilaTypedQuery<Long> query = JPA.createQuery(queryString, Long.class);
      if (nameUppercase != null) {
        query.setParameter("nameUppercase", nameUppercase);
      }
      if (code != null) {
        query.setParameter("code", code);
      }
      if (nation != null) {
        query.setParameter("nation", nation);
      }
      query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
      countryUid = query.getSingleResult();
    }

    CountryFormData country = new CountryFormData();
    if (countryUid != null) {
      country.setCountryUid(countryUid);
      country = load(country);

      boolean update = false;
      if (!StringUtility.isNullOrEmpty(nation)) {
        country.getNation().setValue(nation);
        update = true;
      }
      if (!StringUtility.isNullOrEmpty(code)) {
        country.getCountryCode().setValue(code);
        update = true;
      }
      if (!country.getCodeBox().getActive().getValue()) {
        country.getCodeBox().getActive().setValue(true);
        update = true;
      }
      if (update) {
        country = store(country);
      }
    }
    else {
      // new country
      if (StringUtility.isNullOrEmpty(name) && !StringUtility.isNullOrEmpty(code)) {
        name = code;
      }
      if (StringUtility.isNullOrEmpty(nation) && !StringUtility.isNullOrEmpty(code)) {
        nation = code;
      }
      if (StringUtility.isNullOrEmpty(name) && !StringUtility.isNullOrEmpty(nation)) {
        name = nation;
      }

      // loop through available languages
      for (ICode<?> c : CODES.getCodeType(LanguageCodeType.class).getCodes()) {
        LanguageRowData newRow = country.getCodeBox().getLanguage().addRow(ITableHolder.STATUS_INSERTED);
        newRow.setTranslation(name);
        newRow.setLanguage((Long) c.getId());
      }
      country.getNation().setValue(nation);
      country.getCountryCode().setValue(code);
      country.getCodeBox().getActive().setValue(true);
    }

    return country;
  }

  @Override
  public void delete(CountryFormData formData) throws ProcessingException {
    if (!ACCESS.check(new DeleteCountryPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }
    if (formData == null || formData.getCountryUid() == null) {
      return;
    }

    formData = load(formData);

    RtCountry country = new RtCountry();
    country.setId(RtCountryKey.create(formData.getCountryUid()));
    JPA.remove(country);

    BEANS.get(ICodeProcessService.class).deleteCodeBox(formData.getCodeBox());
  }

}
