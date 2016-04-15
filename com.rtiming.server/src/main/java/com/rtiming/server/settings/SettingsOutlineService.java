package com.rtiming.server.settings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.BooleanUtility;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.platform.util.TypeCastUtility;
import org.eclipse.scout.rt.shared.services.common.code.CODES;
import org.eclipse.scout.rt.shared.services.common.code.ICode;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;

import com.rtiming.server.ServerInfoUtility;
import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.FMilaTypedQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.common.database.jpa.JPAUtility;
import com.rtiming.server.settings.city.JPACityBoxSearchFormDataStatementBuilder;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.common.AbstractSqlCodeType;
import com.rtiming.shared.dao.RtAccount;
import com.rtiming.shared.dao.RtAccountClient;
import com.rtiming.shared.dao.RtAccountClientKey_;
import com.rtiming.shared.dao.RtAccountClient_;
import com.rtiming.shared.dao.RtAccount_;
import com.rtiming.shared.dao.RtAdditionalInformationDef;
import com.rtiming.shared.dao.RtAdditionalInformationDefKey_;
import com.rtiming.shared.dao.RtAdditionalInformationDef_;
import com.rtiming.shared.dao.RtAddress;
import com.rtiming.shared.dao.RtAddressKey_;
import com.rtiming.shared.dao.RtAddress_;
import com.rtiming.shared.dao.RtCity;
import com.rtiming.shared.dao.RtCityKey_;
import com.rtiming.shared.dao.RtCity_;
import com.rtiming.shared.dao.RtClassAge;
import com.rtiming.shared.dao.RtClassAgeKey_;
import com.rtiming.shared.dao.RtClassAge_;
import com.rtiming.shared.dao.RtCountry;
import com.rtiming.shared.dao.RtCountry_;
import com.rtiming.shared.dao.RtFeeGroup;
import com.rtiming.shared.dao.RtFeeGroup_;
import com.rtiming.shared.dao.RtReportTemplate;
import com.rtiming.shared.dao.RtReportTemplateKey_;
import com.rtiming.shared.dao.RtReportTemplate_;
import com.rtiming.shared.dao.RtUc;
import com.rtiming.shared.dao.RtUcKey_;
import com.rtiming.shared.dao.RtUc_;
import com.rtiming.shared.dao.RtUcl;
import com.rtiming.shared.dao.RtUclKey_;
import com.rtiming.shared.dao.RtUcl_;
import com.rtiming.shared.entry.SharedCache;
import com.rtiming.shared.entry.startblock.StartblockCodeType;
import com.rtiming.shared.event.EventRowData;
import com.rtiming.shared.event.EventsSearchFormData;
import com.rtiming.shared.event.IEventsOutlineService;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.settings.IDefaultProcessService;
import com.rtiming.shared.settings.ISettingsOutlineService;
import com.rtiming.shared.settings.addinfo.AdditionalInformationCodeType;
import com.rtiming.shared.settings.city.AreaCodeType;
import com.rtiming.shared.settings.city.CitySearchFormData;
import com.rtiming.shared.settings.city.CountryCodeType;
import com.rtiming.shared.settings.currency.CurrencyCodeType;
import com.rtiming.shared.settings.user.RoleCodeType;

public class SettingsOutlineService implements ISettingsOutlineService {

  @Override
  public Object[][] getCityTableData(CitySearchFormData formData) throws ProcessingException {
    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
    Root<RtCity> city = selectQuery.from(RtCity.class);
    Join<RtCity, RtCountry> joinCountry = city.join(RtCity_.rtCountry);
    Join<RtCity, RtAddress> joinAddress = city.join(RtCity_.rtAddresses, JoinType.LEFT);

    JPACityBoxSearchFormDataStatementBuilder builder = new JPACityBoxSearchFormDataStatementBuilder(city);
    builder.build(formData.getCityBox());

    selectQuery.select(b.array(city.get(RtCity_.id).get(RtCityKey_.cityNr), city.get(RtCity_.zip), city.get(RtCity_.name), city.get(RtCity_.areaUid), city.get(RtCity_.region), city.get(RtCity_.countryUid), joinCountry.get(RtCountry_.countryCode), b.count(joinAddress.get(RtAddress_.id).get(RtAddressKey_.addressNr)))).where(b.and(b.equal(city.get(RtCity_.id).get(RtCityKey_.clientNr), ServerSession.get().getSessionClientNr()), builder.getPredicate())).groupBy(city.get(RtCity_.id).get(RtCityKey_.cityNr), city.get(RtCity_.zip), city.get(RtCity_.name), city.get(RtCity_.areaUid), city.get(RtCity_.region), city.get(RtCity_.countryUid), joinCountry.get(RtCountry_.countryCode));

    List<Object[]> result = JPA.createQuery(selectQuery).getResultList();
    return JPAUtility.convertList2Array(result);
  }

  @Override
  public Object[][] loadCodes(AbstractSqlCodeType codeType) throws ProcessingException {
    StringBuffer buf = new StringBuffer("'");
    buf.append(StringUtility.nvl(codeType.getIconId(), ""));
    buf.append("'");
    String icon = buf.toString();

    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
    Root<RtUc> uc = selectQuery.from(RtUc.class);
    Join<RtUc, RtUcl> joinText = uc.join(RtUc_.rtUcls, JoinType.LEFT);

    List<Selection<?>> list = new ArrayList<>();
    list.add(uc.get(RtUc_.id).get(RtUcKey_.ucUid));
    list.add(joinText.get(RtUcl_.codeName));
    list.add(uc.get(RtUc_.shortcut));
    list.add(uc.get(RtUc_.id).get(RtUcKey_.clientNr));
    if (codeType instanceof CountryCodeType) {
      Join<RtUc, RtCountry> joinCountry = uc.join(RtUc_.rtCountries, JoinType.LEFT);
      list.add(joinCountry.get(RtCountry_.countryCode));
    }

    selectQuery.select(b.array(list.toArray(new Selection<?>[list.size()]))).where(b.and(b.equal(joinText.get(RtUcl_.id).get(RtUclKey_.languageUid), ServerSession.get().getLanguageUid()), b.equal(uc.get(RtUc_.codeType), codeType.getId()), ServerSession.get().getSessionClientNr() == null ? b.conjunction() : b.equal(uc.get(RtUc_.id).get(RtUcKey_.clientNr), ServerSession.get().getSessionClientNr()))).orderBy(b.asc(joinText.get(RtUcl_.codeName)));

    List<Object[]> resultList = JPA.createQuery(selectQuery).getResultList();

    Object[][] result = new Object[resultList.size()][13];
    int k = 0;
    for (Object[] resultRow : resultList) {
      Long ucUid = TypeCastUtility.castValue(resultRow[0], Long.class);
      String name = TypeCastUtility.castValue(resultRow[1], String.class);
      String shortcut = TypeCastUtility.castValue(resultRow[2], String.class);
      Long clientNr = TypeCastUtility.castValue(resultRow[3], Long.class);
      if (codeType instanceof CountryCodeType) {
        // icon = countryCode
        icon = TypeCastUtility.castValue(resultRow[4], String.class);
      }
      if (codeType instanceof CurrencyCodeType) {
        if (!StringUtility.isNullOrEmpty(shortcut)) {
          name = shortcut + " (" + StringUtility.emptyIfNull(name) + ")";
        }
      }

      result[k][0] = ucUid;
      result[k][1] = name;
      result[k][2] = icon;
      result[k][3] = name;
      result[k][9] = shortcut;
      result[k][12] = clientNr;
      k++;
    }
    return result;

//    return SQL.select("SELECT " +
//        "U.UC_UID, " +
//        name +
//        icon + ", " +
//        nameAndShortcut +
//        "NULL, " +
//        "NULL, " +
//        "NULL, " +
//        "NULL, " +
//        "NULL, " +
//        "U.SHORTCUT, " + // ext key
//        "NULL, " +
//        "NULL, " +
//        "U.CLIENT_NR " + // partitionId
//        "FROM RT_UC U, RT_UCL L " +
//        "WHERE U.UC_UID = L.UC_UID " +
//        "AND U.CLIENT_NR = COALESCE(:sessionClientNr, U.CLIENT_NR) " +
//        "AND L.CLIENT_NR = U.CLIENT_NR " +
//        "AND U.CODE_TYPE = :codeType " +
//        "AND L.LANGUAGE_UID = " + ServerSession.get().getLanguageUid() + " " +
//        "ORDER BY 2 "
//        , new NVPair("codeType", codeType.getId())
//        );
  }

  @Override
  public Object[][] getCountryTableData() throws ProcessingException {
    String queryString = "SELECT " + "U.id.ucUid, " + "1, " + // will be set below
    "L.codeName, " + "C.countryCode, " + "C.nation " + "FROM RtCountry C " + "LEFT JOIN C.rtUc U " + "LEFT JOIN U.rtUcls L " + "WHERE U.codeType = :countryCodeTypeId " + "AND U.id.clientNr = :sessionClientNr " + "AND L.id.languageUid = :languageUid ";

    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("countryCodeTypeId", CountryCodeType.ID);
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    query.setParameter("languageUid", ServerSession.get().getLanguageUid());
    Object[][] data = JPAUtility.convertList2Array(query.getResultList());

    Long defaultCountryUid = BEANS.get(IDefaultProcessService.class).getDefaultCountryUid();
    for (Object[] row : data) {
      row[1] = defaultCountryUid;
    }
    return data;
  }

  @Override
  public Object[][] getClassTableData() throws ProcessingException {
    CODES.reloadCodeType(ClassCodeType.class);
    List<? extends ICode<Long>> codes = BEANS.get(ClassCodeType.class).getCodes();

    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<RtClassAge> q = b.createQuery(RtClassAge.class);

    Root<RtClassAge> root = q.from(RtClassAge.class);
    q.where(b.equal(root.get(RtClassAge_.id).get(RtClassAgeKey_.clientNr), ServerSession.get().getSessionClientNr()));
    List<RtClassAge> classAgeList = JPA.createQuery(q).getResultList();

    Map<Long, List<RtClassAge>> map = new HashMap<Long, List<RtClassAge>>();
    for (RtClassAge classAge : classAgeList) {
      if (map.get(classAge.getClassUid()) == null) {
        List<RtClassAge> list = new ArrayList<>();
        map.put(classAge.getClassUid(), list);
      }
      List<RtClassAge> list = map.get(classAge.getClassUid());
      list.add(classAge);
    }

    List<Object[]> resultList = new ArrayList<>();
    for (ICode code : codes) {
      Object[] row = new Object[4];
      Long codeUid = (Long) code.getId();
      row[0] = codeUid;
      row[1] = FMilaUtility.getCodeText(ClassCodeType.class, codeUid);
      row[2] = FMilaUtility.getCodeExtKey(ClassCodeType.class, codeUid);
      List<RtClassAge> list = map.get(codeUid);
      List<String> ageTexts = ClassAgeUtility.calculateAgeText(list);
      row[3] = CollectionUtility.format(ageTexts, ", ");
      resultList.add(row);
    }
    return JPAUtility.convertList2Array(resultList);
  }

  @Override
  public List<RtClassAge> getAgeTableData(Long classUid) throws ProcessingException {

    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<RtClassAge> q = b.createQuery(RtClassAge.class);

    Root<RtClassAge> root = q.from(RtClassAge.class);
    q.where(b.equal(root.get(RtClassAge_.id).get(RtClassAgeKey_.clientNr), ServerSession.get().getSessionClientNr()), b.equal(root.get(RtClassAge_.classUid), classUid));

    return JPA.createQuery(q).getResultList();
  }

  @Override
  public void intialDataLoad() throws ProcessingException {
    // Codes
    InitialLoadUtility.createCodes();

    // Country
    InitialLoadUtility.createCountries();

    // Classes
    InitialLoadUtility.createClasses();

    // Currency
    InitialLoadUtility.createCurrencies();

    // Insert all missing translations with English
    InitialLoadUtility.insertMissingTranslationsWithEnglish();

    // reload all codes since new codes were created
    Collection<ICodeType<?, ?>> types = CODES.getAllCodeTypes(""); // TODO MIG
    List<Class> clazzes = new ArrayList<>();
    for (ICodeType type : types) {
      clazzes.add(type.getClass());
    }
    CODES.reloadCodeTypes(clazzes.toArray(new Class[clazzes.size()]));
  }

  @Override
  public String getInstallationInfo(Map<String, String> plainTextPasswords) throws ProcessingException {
    return ServerInfoUtility.buildInstallationInfo(plainTextPasswords);
  }

  @Override
  public Object[][] getAdditionalInformationAdministrationTableData(Long parentUid) throws ProcessingException {
    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
    Root<RtAdditionalInformationDef> addInfoDef = selectQuery.from(RtAdditionalInformationDef.class);
    Join<RtAdditionalInformationDef, RtFeeGroup> joinFeeGroup = addInfoDef.join(RtAdditionalInformationDef_.rtFeeGroup, JoinType.LEFT);

    selectQuery.select(b.array(addInfoDef.get(RtAdditionalInformationDef_.id).get(RtAdditionalInformationDefKey_.additionalInformationUid), addInfoDef.get(RtAdditionalInformationDef_.entityUid), addInfoDef.get(RtAdditionalInformationDef_.typeUid), addInfoDef.get(RtAdditionalInformationDef_.valueMin), addInfoDef.get(RtAdditionalInformationDef_.valueMax), joinFeeGroup.get(RtFeeGroup_.name), addInfoDef.get(RtAdditionalInformationDef_.mandatory))).where(b.and(b.equal(addInfoDef.get(RtAdditionalInformationDef_.id).get(RtAdditionalInformationDefKey_.clientNr), ServerSession.get().getSessionClientNr()), parentUid == null ? b.isNull(addInfoDef.get(RtAdditionalInformationDef_.parentUid)) : b.equal(addInfoDef.get(RtAdditionalInformationDef_.parentUid), parentUid)));
    List<Object[]> resultList = JPA.createQuery(selectQuery).getResultList();

    Map<Long, long[]> cache = SharedCache.getEventAdditionalInformationConfiguration();
    List<EventRowData> events = BEANS.get(IEventsOutlineService.class).getEventTableData(ServerSession.get().getSessionClientNr(), new EventsSearchFormData());
    Map<Long, String> eventNames = new HashMap<>();
    for (EventRowData event : events) {
      eventNames.put(event.getEventNr(), event.getName());
    }
    Map<Long, String> texts = new HashMap<>();
    for (Long addInfoUid : cache.keySet()) {
      long[] eventsNrs = cache.get(addInfoUid);
      List<String> eventNameStrings = new ArrayList<>();
      for (Long eventNr : eventsNrs) {
        String eventNameString = eventNames.get(eventNr);
        eventNameStrings.add(eventNameString);
      }
      texts.put(addInfoUid, CollectionUtility.format(eventNameStrings, "<br>"));
    }

    List<Object[]> result = new ArrayList<>();
    for (Object[] resultRow : resultList) {
      Object[] row = new Object[9];
      Long addInfoUid = TypeCastUtility.castValue(resultRow[0], Long.class);
      Long entityUid = TypeCastUtility.castValue(resultRow[1], Long.class);
      Long typeUid = TypeCastUtility.castValue(resultRow[2], Long.class);
      Double valueMin = TypeCastUtility.castValue(resultRow[3], Double.class);
      Double valueMax = TypeCastUtility.castValue(resultRow[4], Double.class);
      String name = TypeCastUtility.castValue(resultRow[5], String.class);
      boolean mandatory = BooleanUtility.nvl(TypeCastUtility.castValue(resultRow[6], Boolean.class));

      row[0] = addInfoUid;
      row[1] = FMilaUtility.getCodeText(AdditionalInformationCodeType.class, addInfoUid);
      row[2] = entityUid;
      row[3] = typeUid;
      row[4] = valueMin;
      row[5] = valueMax;
      row[6] = name;
      row[7] = mandatory;
      row[8] = FMilaUtility.boxHtmlBody(texts.get(addInfoUid));

      result.add(row);
    }

    return JPAUtility.convertList2Array(result);
  }

  @Override
  public Object[][] getCurrencyTableData() throws ProcessingException {
    String queryString = "SELECT " + "U.id.ucUid, " + "1, " + // value will be set below
    "L.codeName, " + "U.shortcut, " + "C.exchangeRate " + "FROM RtCurrency C " + "LEFT JOIN C.rtUc U " + "LEFT JOIN U.rtUcls L " + "WHERE U.codeType = :countryCodeTypeId " + "AND U.id.clientNr = :sessionClientNr " + "AND L.id.languageUid = :languageUid ";

    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("countryCodeTypeId", CurrencyCodeType.ID);
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    query.setParameter("languageUid", ServerSession.get().getLanguageUid());
    Object[][] data = JPAUtility.convertList2Array(query.getResultList());

    Long defaultCountryUid = BEANS.get(IDefaultProcessService.class).getDefaultCurrencyUid();
    for (Object[] row : data) {
      row[1] = defaultCountryUid;
    }
    return data;
  }

  @Override
  public Object[][] getFeeGroupTableData() throws ProcessingException {
    String queryString = "SELECT " + "FG.id.feeGroupNr, " + "FG.id.clientNr, " + "FG.name, " + "FG.cashPaymentOnRegistration, " + "COUNT(F.id.feeNr) " + "FROM RtFeeGroup FG " + "LEFT JOIN FG.rtFees F " + "WHERE FG.id.clientNr = :sessionClientNr " + "GROUP BY FG.id.feeGroupNr, FG.id.clientNr, FG.name ";

    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    return JPAUtility.convertList2Array(query.getResultList());
  }

  @Override
  public Object[][] getFeeTableData(Long feeGroupNr) throws ProcessingException {
    String queryString = "SELECT F.id.feeNr, " + "F.id.clientNr, " + "F.fee, " + "F.currencyUid, " + "F.evtFrom, " + "F.evtTo, " + "F.ageFrom, " + "F.ageTo " + "FROM RtFee F " + "WHERE F.rtFeeGroup.id.feeGroupNr = :feeGroupNr " + "AND F.rtFeeGroup.id.clientNr = F.id.clientNr " + "AND F.id.clientNr = :sessionClientNr ";

    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("feeGroupNr", feeGroupNr);
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    return JPAUtility.convertList2Array(query.getResultList());
  }

  @Override
  public Object[][] getStartblockTableData() throws ProcessingException {
    String queryString = "SELECT U.id.ucUid " + "FROM RtUc U " + "WHERE U.id.clientNr = :sessionClientNr " + "AND codeType = :startblockCodeTypeId ";

    FMilaQuery query = JPA.createQuery(queryString, Object.class);
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    query.setParameter("startblockCodeTypeId", StartblockCodeType.ID);
    return JPAUtility.convertList2Array(query.getResultList());
  }

  @Override
  public Object[][] getAreaTableData() throws ProcessingException {
    String queryString = "SELECT U.id.ucUid " + "FROM RtUc U " + "WHERE U.id.clientNr = :sessionClientNr " + "AND codeType = :areaCodeTypeId ";

    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    query.setParameter("areaCodeTypeId", AreaCodeType.ID);
    return JPAUtility.convertList2Array(query.getResultList());
  }

  @Override
  public Object[][] getUserTableData() throws ProcessingException {

    String queryString = "SELECT " + "U.id.userNr, " + // 0
    "U.username, " + // 1
    "'role' " + // 2
    "FROM RtUser U " + "WHERE U.id.clientNr = :sessionClientNr ";

    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    Object[][] data = JPAUtility.convertList2Array(query.getResultList());

    ICodeType roles = BEANS.get(RoleCodeType.class);
    for (Object[] row : data) {
      queryString = "SELECT id.roleUid " + "FROM RtUserRole " + "WHERE id.userNr = :userNr " + "AND id.clientNr = :clientNr";
      FMilaTypedQuery<Long> typedQuery = JPA.createQuery(queryString, Long.class);
      typedQuery.setParameter("userNr", row[0]);
      typedQuery.setParameter("clientNr", ServerSession.get().getSessionClientNr());
      List<Long> roleUids = typedQuery.getResultList();
      StringBuffer roleConcat = new StringBuffer();
      for (Long roleUid : roleUids) {
        if (roleConcat.length() > 0) {
          roleConcat.append(", ");
        }
        roleConcat.append(roles.getCode(roleUid).getText());
      }
      row[2] = roleConcat.toString();
    }

    return data;
  }

  @Override
  public Object[][] getAccountTableData() throws ProcessingException {

    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<RtAccount> selectQuery = b.createQuery(RtAccount.class);
    selectQuery.from(RtAccount.class);
    List<RtAccount> accountList = JPA.createQuery(selectQuery).getResultList();

    CriteriaQuery<RtAccountClient> selectAccountClientQuery = b.createQuery(RtAccountClient.class);
    selectAccountClientQuery.from(RtAccountClient.class);
    List<RtAccountClient> accountClientList = JPA.createQuery(selectAccountClientQuery).getResultList();
    Map<Long /* accountNr */, Set<Long>> accountClientNrs = new HashMap<>();
    for (RtAccountClient accountClient : accountClientList) {
      if (accountClientNrs.get(accountClient.getId().getAccountNr()) == null) {
        accountClientNrs.put(accountClient.getId().getAccountNr(), new HashSet<Long>());
      }
      accountClientNrs.get(accountClient.getId().getAccountNr()).add(accountClient.getId().getClientNr());
    }

    List<Object[]> result = new ArrayList<>();
    for (RtAccount account : accountList) {
      Object[] row = new Object[7];
      row[0] = account.getAccountNr();
      row[1] = account.getUsername();
      row[2] = account.getFirstName();
      row[3] = account.getLastName();
      row[4] = account.getEmail();
      row[5] = account.getGlobalClientNr();
      row[6] = CollectionUtility.format(accountClientNrs.get(account.getAccountNr()));

      result.add(row);
    }
    return JPAUtility.convertList2Array(result);

//    return SQL.select("SELECT " +
//        "A.ACCOUNT_NR, " +
//        "A.USERNAME, " +
//        "A.FIRST_NAME, " +
//        "A.LAST_NAME, " +
//        "A.EMAIL, " +
//        "A.GLOBAL_CLIENT_NR, " +
//        "GROUP_CONCAT('' || AC.CLIENT_NR) " +
//        "FROM RT_ACCOUNT A " +
//        "LEFT JOIN RT_ACCOUNT_CLIENT AC ON AC.ACCOUNT_NR = A.ACCOUNT_NR " +
//        "GROUP BY A.ACCOUNT_NR, A.USERNAME, A.FIRST_NAME, A.LAST_NAME, A.EMAIL, A.GLOBAL_CLIENT_NR "
//        );
  }

  @Override
  public Object[][] getAccountClientTableData(Long accountNr) throws ProcessingException {
    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<RtAccount> selectGlobalQuery = b.createQuery(RtAccount.class);
    Root<RtAccount> global = selectGlobalQuery.from(RtAccount.class);
    selectGlobalQuery.where(b.equal(global.get(RtAccount_.accountNr), accountNr));
    List<RtAccount> globalList = JPA.createQuery(selectGlobalQuery).getResultList();

    CriteriaQuery<RtAccountClient> selectLocalQuery = b.createQuery(RtAccountClient.class);
    Root<RtAccountClient> local = selectLocalQuery.from(RtAccountClient.class);
    selectLocalQuery.where(b.equal(local.get(RtAccountClient_.id).get(RtAccountClientKey_.accountNr), accountNr));
    List<RtAccountClient> localList = JPA.createQuery(selectLocalQuery).getResultList();

    List<Object[]> result = new ArrayList<>();
    for (RtAccount account : globalList) {
      Object[] row = new Object[2];
      row[0] = account.getGlobalClientNr();
      row[1] = "Global";
      result.add(row);
    }
    for (RtAccountClient account : localList) {
      Object[] row = new Object[2];
      row[0] = account.getId().getClientNr();
      row[1] = "Local";
      result.add(row);
    }
    return JPAUtility.convertList2Array(result);
  }

  @Override
  public List<RtReportTemplate> getReportTemplateTableData() throws ProcessingException {

    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<RtReportTemplate> q = b.createQuery(RtReportTemplate.class);

    Root<RtReportTemplate> root = q.from(RtReportTemplate.class);
    q.where(b.equal(root.get(RtReportTemplate_.id).get(RtReportTemplateKey_.clientNr), ServerSession.get().getSessionClientNr()));

    return JPA.createQuery(q).getResultList();
  }

}
