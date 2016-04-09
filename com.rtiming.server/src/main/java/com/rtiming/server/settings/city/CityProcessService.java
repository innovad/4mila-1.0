package com.rtiming.server.settings.city;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.FMilaTypedQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.common.database.jpa.JPAUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.CreateCityPermission;
import com.rtiming.shared.common.security.permission.DeleteCityPermission;
import com.rtiming.shared.common.security.permission.ReadCityPermission;
import com.rtiming.shared.common.security.permission.UpdateCityPermission;
import com.rtiming.shared.dao.RtCity;
import com.rtiming.shared.dao.RtCityKey;
import com.rtiming.shared.settings.IDefaultProcessService;
import com.rtiming.shared.settings.city.CityFormData;
import com.rtiming.shared.settings.city.CountryCodeType;
import com.rtiming.shared.settings.city.CountryFormData;
import com.rtiming.shared.settings.city.ICityProcessService;
import com.rtiming.shared.settings.city.ICountryProcessService;

public class CityProcessService  implements ICityProcessService {

  @Override
  public CityFormData prepareCreate(CityFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateCityPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }
    formData.getCountry().setValue(BEANS.get(IDefaultProcessService.class).getDefaultCountryUid());
    return formData;
  }

  @Override
  public CityFormData create(CityFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateCityPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    RtCityKey key = RtCityKey.create((Long) null);
    RtCity city = new RtCity();
    city.setId(key);
    JPA.persist(city);

    formData.setCityNr(city.getId().getId());
    formData = store(formData);

    return formData;
  }

  @Override
  public CityFormData load(CityFormData formData) throws ProcessingException {
    if (!ACCESS.check(new ReadCityPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    if (formData != null && formData.getCityNr() != null) {
      RtCity city = JPA.find(RtCity.class, RtCityKey.create(formData.getCityNr()));
      if (city != null) {
        formData.getZip().setValue(city.getZip());
        formData.getCity().setValue(city.getName());
        formData.getArea().setValue(city.getAreaUid());
        formData.getRegion().setValue(city.getRegion());
        formData.getCountry().setValue(city.getRtCountry() != null ? city.getRtCountry().getId().getId() : null);
      }
    }

    return formData;
  }

  @Override
  public CityFormData store(CityFormData formData) throws ProcessingException {
    if (!ACCESS.check(new UpdateCityPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    String queryString = "UPDATE RtCity C " +
        "SET C.zip = :zip, C.name = :city, C.areaUid = :area, C.region = :region, C.rtCountry.id.countryUid = :country " +
        "WHERE C.id.cityNr = :cityNr " +
        "AND C.id.clientNr = :sessionClientNr ";

    FMilaQuery query = JPA.createQuery(queryString);
    JPAUtility.setAutoParameters(query, queryString, formData);
    query.executeUpdate();

    return formData;
  }

  @Override
  public CityFormData findCity(String name, String zip, String region, String countryName, Long countryLanguageUid, String code) throws ProcessingException {
    if (countryName == null && code == null) {
      throw new IllegalArgumentException("Country not set for city: " + name);
    }

    String countryCode = null;
    String nationCode = null;
    if (code != null && code.length() == 2) {
      countryCode = code;
    }
    if (code != null && code.length() == 3) {
      nationCode = code;
    }
    else if (code != null && code.length() > 3) {
      nationCode = code.substring(0, 3);
    }
    countryName = StringUtility.trim(countryName);

    String queryString = "SELECT MAX(id.cityNr) FROM RtCity C " +
        "WHERE UPPER(COALESCE(name,'NU#LL')) = UPPER(:name) " +
        "AND UPPER(COALESCE(zip,'NU#LL')) = UPPER(:zip) " +
        "AND UPPER(COALESCE(region,'NU#LL')) = UPPER(:region) " +
        // search with country full name
        (countryName == null ? "" :
            "AND (C.countryUid IN (SELECT U.id.ucUid FROM RtUcl L " +
                "                   INNER JOIN L.rtUc U " +
                "                   WHERE UPPER(L.codeName) = UPPER(:countryName) " +
                (countryLanguageUid == null ? "" : "AND L.id.languageUid = :countryLanguageUid ") +
                "                   AND L.id.clientNr = C.id.clientNr " +
                "                   AND U.codeType = " + CountryCodeType.ID + ")) ") +
        // search with country code
        (countryCode == null ? "" :
            "AND C.countryUid IN (SELECT X.id.countryUid " +
                "                   FROM RtCountry X " +
                "                   WHERE X.id.clientNr = C.id.clientNr " +
                "                   AND X.countryCode = :countryCode) ") +
        // search with nation code
        (nationCode == null ? "" :
            "AND C.countryUid IN (SELECT X.id.countryUid " +
                "                   FROM RtCountry X " +
                "                   WHERE X.id.clientNr = C.id.clientNr " +
                "                   AND X.nation = :nationCode) ") +
        "AND C.id.clientNr = :sessionClientNr ";
    FMilaTypedQuery<Long> query = JPA.createQuery(queryString, Long.class);
    query.setParameter("name", StringUtility.nvl(name, "NU#LL"));
    query.setParameter("zip", StringUtility.nvl(zip, "NU#LL"));
    query.setParameter("region", StringUtility.nvl(region, "NU#LL"));
    if (countryName != null) {
      query.setParameter("countryName", countryName);
      if (countryLanguageUid != null) {
        query.setParameter("countryLanguageUid", countryLanguageUid);
      }
    }
    if (countryCode != null) {
      query.setParameter("countryCode", countryCode);
    }
    if (nationCode != null) {
      query.setParameter("nationCode", nationCode);
    }
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    Long cityNr = query.getSingleResult();

    CityFormData city = new CityFormData();
    if (cityNr != null) {
      city.setCityNr(cityNr);
      city = load(city);
    }
    else {
      ICountryProcessService countryService = BEANS.get(ICountryProcessService.class);

      city.getCity().setValue(name);
      city.getZip().setValue(zip);
      city.getRegion().setValue(region);

      CountryFormData country = null;
      if (countryCode != null || nationCode != null) {
        // first try to find with code only
        country = countryService.find(null, countryCode, nationCode);
      }
      if (country == null || country.getCountryUid() == null) {
        // if not found, or no code, try to find/create with name/code combination
        country = countryService.find(countryName, countryCode, nationCode);
      }

      if (country.getCountryUid() == null) {
        country = countryService.create(country);
      }
      city.getCountry().setValue(country.getCountryUid());
    }

    return city;
  }

  @Override
  public CityFormData delete(CityFormData formData) throws ProcessingException {
    if (!ACCESS.check(new DeleteCityPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    if (formData != null) {
      RtCity city = new RtCity();
      city.setId(RtCityKey.create(formData.getCityNr()));
      JPA.remove(city);
    }

    return formData;
  }
}
