package com.rtiming.server.entry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
import javax.persistence.criteria.Subquery;

import org.eclipse.scout.commons.BooleanUtility;
import org.eclipse.scout.commons.CompareUtility;
import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.TypeCastUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaTypedQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.common.database.jpa.JPACriteriaUtility;
import com.rtiming.server.common.database.jpa.JPAUtility;
import com.rtiming.server.settings.addinfo.AdditionalInformationDatabaseUtility;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.EntityCodeType;
import com.rtiming.shared.common.database.sql.AdditionalInformationValueBean;
import com.rtiming.shared.dao.RtAddress;
import com.rtiming.shared.dao.RtAddress_;
import com.rtiming.shared.dao.RtCity;
import com.rtiming.shared.dao.RtCity_;
import com.rtiming.shared.dao.RtClub;
import com.rtiming.shared.dao.RtClubKey_;
import com.rtiming.shared.dao.RtClub_;
import com.rtiming.shared.dao.RtCountry;
import com.rtiming.shared.dao.RtCountry_;
import com.rtiming.shared.dao.RtCourse;
import com.rtiming.shared.dao.RtCourseKey_;
import com.rtiming.shared.dao.RtCourse_;
import com.rtiming.shared.dao.RtCurrency;
import com.rtiming.shared.dao.RtCurrency_;
import com.rtiming.shared.dao.RtEcard;
import com.rtiming.shared.dao.RtEcard_;
import com.rtiming.shared.dao.RtEntry;
import com.rtiming.shared.dao.RtEntryKey_;
import com.rtiming.shared.dao.RtEntry_;
import com.rtiming.shared.dao.RtEvent;
import com.rtiming.shared.dao.RtEventClass;
import com.rtiming.shared.dao.RtEventClassKey_;
import com.rtiming.shared.dao.RtEventClass_;
import com.rtiming.shared.dao.RtEvent_;
import com.rtiming.shared.dao.RtParticipation;
import com.rtiming.shared.dao.RtParticipationKey_;
import com.rtiming.shared.dao.RtParticipation_;
import com.rtiming.shared.dao.RtPayment;
import com.rtiming.shared.dao.RtPaymentKey_;
import com.rtiming.shared.dao.RtPayment_;
import com.rtiming.shared.dao.RtRace;
import com.rtiming.shared.dao.RtRaceKey_;
import com.rtiming.shared.dao.RtRace_;
import com.rtiming.shared.dao.RtRegistration;
import com.rtiming.shared.dao.RtRegistrationKey_;
import com.rtiming.shared.dao.RtRegistration_;
import com.rtiming.shared.dao.RtRunner;
import com.rtiming.shared.dao.RtRunnerKey_;
import com.rtiming.shared.dao.RtRunner_;
import com.rtiming.shared.dao.RtStartlistSetting;
import com.rtiming.shared.dao.RtStartlistSettingKey_;
import com.rtiming.shared.dao.RtStartlistSettingVacant;
import com.rtiming.shared.dao.RtStartlistSettingVacantKey_;
import com.rtiming.shared.dao.RtStartlistSettingVacant_;
import com.rtiming.shared.dao.RtStartlistSetting_;
import com.rtiming.shared.entry.EntriesSearchFormData;
import com.rtiming.shared.entry.EntryList;
import com.rtiming.shared.entry.EntryRowData;
import com.rtiming.shared.entry.EntryTableDataOptions;
import com.rtiming.shared.entry.EventConfiguration;
import com.rtiming.shared.entry.IRegistrationsOutlineService;
import com.rtiming.shared.entry.RegistrationRowData;
import com.rtiming.shared.entry.RegistrationRowData.Entry;
import com.rtiming.shared.entry.RegistrationsSearchFormData;
import com.rtiming.shared.entry.SharedCache;
import com.rtiming.shared.entry.startblock.StartblockCodeType;
import com.rtiming.shared.entry.startlist.StartlistSettingRowData;
import com.rtiming.shared.entry.startlist.StartlistSettingUtility;
import com.rtiming.shared.entry.startlist.StartlistTypeCodeType;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.event.course.ClassTypeCodeType;
import com.rtiming.shared.results.SingleEventSearchFormData;
import com.rtiming.shared.settings.addinfo.AdditionalInformationTypeCodeType;
import com.rtiming.shared.settings.fee.FeeCalculator;
import com.rtiming.shared.settings.fee.FeeCalculator.AddInfoInput;
import com.rtiming.shared.settings.fee.FeeCalculator.FeeResult;
import com.rtiming.shared.settings.fee.FeeFormData;

public class RegistrationsOutlineService implements IRegistrationsOutlineService {

  @Override
  public List<EntryRowData> getEntryTableData(EntryTableDataOptions options, EntriesSearchFormData formData) throws ProcessingException {

    EntryList presentationType = options.getPresentationType();
    Long eventNr = formData.getEvent().getValue();
    Long clientNr = options.getClientNr();
    Long registrationNr = options.getRegistrationNr();
    Long classUid = options.getClassUid();
    Long courseNr = options.getCourseNr();
    Long clubNr = options.getClubNr();

    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
    Root<RtRace> race = selectQuery.from(RtRace.class);
    Join<RtRace, RtParticipation> joinParticipation = race.join(RtRace_.rtParticipation, JoinType.LEFT);
    Join<RtRace, RtEvent> joinEvent = race.join(RtRace_.rtEvent, JoinType.LEFT);
    Join<RtRace, RtEntry> joinEntry = race.join(RtRace_.rtEntry, JoinType.LEFT);
    Join<RtEntry, RtRegistration> joinRegistration = joinEntry.join(RtEntry_.rtRegistration, JoinType.LEFT);
    Join<RtRace, RtRunner> joinRunner = race.join(RtRace_.rtRunner, JoinType.LEFT);
    Join<RtRace, RtClub> joinClub = race.join(RtRace_.rtClub, JoinType.LEFT);
    Join<RtRace, RtEcard> joinECard = race.join(RtRace_.rtEcard, JoinType.LEFT);
    Join<RtRace, RtAddress> joinAddress = race.join(RtRace_.rtAddress, JoinType.LEFT);
    Join<RtAddress, RtCity> joinCity = joinAddress.join(RtAddress_.rtCity, JoinType.LEFT);
    Join<RtRace, RtCountry> joinNation = race.join(RtRace_.rtCountry, JoinType.LEFT);
    Join<RtParticipation, RtEventClass> joinEventClass = joinParticipation.join(RtParticipation_.rtEventClass, JoinType.LEFT);
    Join<RtEventClass, RtCourse> joinCourse = joinEventClass.join(RtEventClass_.rtCourse, JoinType.LEFT);

    JPAEntriesSearchFormDataStatementBuilder builder = new JPAEntriesSearchFormDataStatementBuilder(selectQuery, race, joinEntry, joinParticipation, joinRunner, joinECard, joinClub, joinCity);
    builder.build(formData);

    Subquery<Long> exists = selectQuery.subquery(Long.class);
    Root<RtParticipation> subroot = exists.from(RtParticipation.class);
    exists.select(subroot.get(RtParticipation_.id).get(RtParticipationKey_.entryNr)).where(b.equal(subroot.get(RtParticipation_.id).get(RtParticipationKey_.entryNr), race.get(RtRace_.entryNr)), b.equal(subroot.get(RtParticipation_.id).get(RtParticipationKey_.eventNr), eventNr));

    selectQuery.select(b.array(joinEntry.get(RtEntry_.id).get(RtEntryKey_.entryNr), joinRunner.get(RtRunner_.id).get(RtRunnerKey_.runnerNr), race.get(RtRace_.id).get(RtRaceKey_.raceNr), joinEvent.get(RtEvent_.punchingSystemUid), race.get(RtRace_.bibNo), joinRegistration.get(RtRegistration_.registrationNo), joinEntry.get(RtEntry_.evtEntry), joinRunner.get(RtRunner_.extKey), JPACriteriaUtility.runnerNameJPA(joinRunner), joinRunner.get(RtRunner_.lastName), joinRunner.get(RtRunner_.firstName), joinECard.get(RtEcard_.ecardNo), joinECard.get(RtEcard_.rentalCard), joinClub.get(RtClub_.shortcut), joinClub.get(RtClub_.extKey), joinClub.get(RtClub_.name), joinNation.get(RtCountry_.nation), joinNation.get(RtCountry_.countryCode), joinRunner.get(RtRunner_.sexUid), joinRunner.get(RtRunner_.evtBirth), joinRunner.get(RtRunner_.year), race.get(RtRace_.legClassUid), joinCourse.get(RtCourse_.shortcut), joinAddress.get(RtAddress_.street), joinCity.get(RtCity_.zip), joinCity.get(RtCity_.name), joinCity.get(RtCity_.areaUid), joinCity.get(RtCity_.region), joinCity.get(RtCity_.countryUid), joinAddress.get(RtAddress_.phone), joinAddress.get(RtAddress_.fax), joinAddress.get(RtAddress_.mobile), joinAddress.get(RtAddress_.email), joinAddress.get(RtAddress_.www), joinEvent.get(RtEvent_.name), joinEventClass.get(RtEventClass_.startlistSettingNr), joinCourse.get(RtCourse_.startlistSettingNr), joinParticipation.get(RtParticipation_.startblockUid), joinRegistration.get(RtRegistration_.startlistSettingOptionUid), joinEvent.get(RtEvent_.evtZero), joinParticipation.get(RtParticipation_.startTime))).where(b.and(b.equal(race.get(RtRace_.id).get(RtRaceKey_.clientNr), clientNr), eventNr == null ? b.conjunction() : b.exists(exists), registrationNr == null ? b.conjunction() : b.equal(joinEntry.get(RtEntry_.registrationNr), registrationNr), eventNr == null ? b.conjunction() : b.equal(race.get(RtRace_.eventNr), eventNr), classUid == null ? b.conjunction() : b.equal(race.get(RtRace_.legClassUid), classUid), courseNr == null ? b.conjunction() : b.equal(joinCourse.get(RtCourse_.id).get(RtCourseKey_.courseNr), courseNr), clubNr == null ? b.conjunction() : b.equal(joinClub.get(RtClub_.id).get(RtClubKey_.clubNr), clubNr), builder.getPredicate()));

    if (EntryList.MISSING.equals(presentationType)) {
      selectQuery.getRestriction().getExpressions().add(b.isNull(race.get(RtRace_.statusUid)));
    }

    if (EntryList.MANUAL_RACE_STATUS.equals(presentationType)) {
      selectQuery.getRestriction().getExpressions().add(b.isTrue(race.get(RtRace_.manualStatus)));
    }

    if (EntryList.FINISH_TIMES_STORED.equals(presentationType)) {
      selectQuery.getRestriction().getExpressions().add(b.notEqual(b.coalesce(race.get(RtRace_.statusUid), 0), 0));
    }

    // order is important to calculate team sort code
    selectQuery.orderBy(b.asc(race.get(RtRace_.entryNr)), b.asc(joinECard.get(RtEcard_.ecardNo)), b.asc(race.get(RtRace_.id).get(RtRaceKey_.raceNr)));
    List<Object[]> resultList = JPA.createQuery(selectQuery).getResultList();

    // load additional values
    Set<Long> entryPkNrs = AdditionalInformationDatabaseUtility.convertToPrimaryKeyList(resultList, 0);
    Map<Long, Object[]> entryAdditionalValues = AdditionalInformationDatabaseUtility.selectTableData(EntityCodeType.EntryCode.ID, entryPkNrs);

    Set<Long> runnerPkNrs = AdditionalInformationDatabaseUtility.convertToPrimaryKeyList(resultList, 1);
    Map<Long, Object[]> runnerAdditionalValues = AdditionalInformationDatabaseUtility.selectTableData(EntityCodeType.RunnerCode.ID, runnerPkNrs);

    List<EntryRowData> result = new ArrayList<>();
    Long lastEntryNr = null;
    for (Object[] row : resultList) {
      int k = 0;
      EntryRowData r = new EntryRowData();
      r.setEntryNr(TypeCastUtility.castValue(row[k++], Long.class));
      r.setRunnerNr(TypeCastUtility.castValue(row[k++], Long.class));
      r.setRaceNr(TypeCastUtility.castValue(row[k++], Long.class));
      r.setRaceNrOrder(0L);
      // team sort code: 0 for lowest race nr with e-card
      // if no ecards, lowest race, 1 for other runners
      if (CompareUtility.equals(lastEntryNr, r.getEntryNr())) {
        r.setRaceNrOrder(1L);
      }
      else {
        r.setRaceNrOrder(0L);
      }
      r.setPunchingSystemUid(TypeCastUtility.castValue(row[k++], Long.class));
      r.setBibNumber(TypeCastUtility.castValue(row[k++], String.class));
      r.setRegistrationNo(TypeCastUtility.castValue(row[k++], String.class));
      r.setEntryTime(TypeCastUtility.castValue(row[k++], Date.class));
      r.setExtKey(TypeCastUtility.castValue(row[k++], String.class));
      r.setRunner(TypeCastUtility.castValue(row[k++], String.class));
      r.setLastName(TypeCastUtility.castValue(row[k++], String.class));
      r.setFirstName(TypeCastUtility.castValue(row[k++], String.class));
      r.setECard(TypeCastUtility.castValue(row[k++], String.class));
      r.setRentalCard(BooleanUtility.nvl(TypeCastUtility.castValue(row[k++], Boolean.class)));
      r.setClubShortcut(TypeCastUtility.castValue(row[k++], String.class));
      r.setClubExtKey(TypeCastUtility.castValue(row[k++], String.class));
      r.setClub(TypeCastUtility.castValue(row[k++], String.class));
      r.setNation(TypeCastUtility.castValue(row[k++], String.class));
      r.setNationCountryCode(TypeCastUtility.castValue(row[k++], String.class));
      r.setSex(TypeCastUtility.castValue(row[k++], Long.class));
      r.setBirthdate(TypeCastUtility.castValue(row[k++], Date.class));
      r.setYear(TypeCastUtility.castValue(row[k++], Long.class));
      Long legClassUid = TypeCastUtility.castValue(row[k++], Long.class);
      r.setLegClassShortcut(FMilaUtility.getCodeExtKey(ClassTypeCodeType.class, legClassUid));
      r.setLegClassName(FMilaUtility.getCodeText(ClassTypeCodeType.class, legClassUid));
      r.setLegClassUid(legClassUid);
      r.setCourseShortcut(TypeCastUtility.castValue(row[k++], String.class));
      r.setStreet(TypeCastUtility.castValue(row[k++], String.class));
      r.setZip(TypeCastUtility.castValue(row[k++], String.class));
      r.setCity(TypeCastUtility.castValue(row[k++], String.class));
      r.setArea(TypeCastUtility.castValue(row[k++], Long.class));
      r.setRegion(TypeCastUtility.castValue(row[k++], String.class));
      r.setCountry(TypeCastUtility.castValue(row[k++], Long.class));
      r.setPhone(TypeCastUtility.castValue(row[k++], String.class));
      r.setFax(TypeCastUtility.castValue(row[k++], String.class));
      r.setMobilePhone(TypeCastUtility.castValue(row[k++], String.class));
      r.setEMail(TypeCastUtility.castValue(row[k++], String.class));
      r.setURL(TypeCastUtility.castValue(row[k++], String.class));
      r.setEvent(TypeCastUtility.castValue(row[k++], String.class));
      Long eventClassStartlistSettingNr = TypeCastUtility.castValue(row[k++], Long.class);
      Long courseStartlistSettingNr = TypeCastUtility.castValue(row[k++], Long.class);
      boolean hasStartlist = NumberUtility.nvl(eventClassStartlistSettingNr, 0L) != 0 || NumberUtility.nvl(courseStartlistSettingNr, 0L) != 0;
      r.setStartList(hasStartlist);
      Long startblockUid = TypeCastUtility.castValue(row[k++], Long.class);
      r.setStartblock(startblockUid != null ? FMilaUtility.getCodeText(StartblockCodeType.class, startblockUid) : "<" + Texts.get("none") + " >");
      r.setStartlistSettingOption(TypeCastUtility.castValue(row[k++], Long.class));
      Date evtZero = TypeCastUtility.castValue(row[k++], Date.class);
      Long startTime = TypeCastUtility.castValue(row[k++], Long.class);
      r.setRelativeStartTime(startTime);
      r.setStartTime(FMilaUtility.addMilliSeconds(evtZero, startTime));
      r.setEntryAdditionalValues(entryAdditionalValues.get(r.getEntryNr()));
      r.setRunnerAdditionalValues(runnerAdditionalValues.get(r.getRunnerNr()));
      result.add(r);

      lastEntryNr = r.getEntryNr();
    }

    return result;
  }

  /**
   * returns registration overview including fee sum per registration
   */
  @Override
  public Object[][] getRegistrationTableData(RegistrationsSearchFormData formData) throws ProcessingException {

    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
    Root<RtRegistration> registration = selectQuery.from(RtRegistration.class);
    Join<RtRegistration, RtEntry> joinEntry = registration.join(RtRegistration_.rtEntries, JoinType.LEFT);
    Join<RtEntry, RtRace> joinRace = joinEntry.join(RtEntry_.rtRaces, JoinType.LEFT);
    Join<RtEntry, RtParticipation> joinParticipation = joinEntry.join(RtEntry_.rtParticipations, JoinType.LEFT);
    Join<RtParticipation, RtEvent> joinEvent = joinParticipation.join(RtParticipation_.rtEvent, JoinType.LEFT);
    Join<RtRace, RtRunner> joinRunner = joinRace.join(RtRace_.rtRunner, JoinType.LEFT);
    Join<RtRace, RtClub> joinClub = joinRace.join(RtRace_.rtClub, JoinType.LEFT);

    JPARegistrationsSearchFormDataStatementBuilder builder = new JPARegistrationsSearchFormDataStatementBuilder(selectQuery, registration, joinEntry);
    builder.build(formData);

    selectQuery.select(b.array(registration.get(RtRegistration_.id).get(RtRegistrationKey_.registrationNr), registration.get(RtRegistration_.registrationNo), JPACriteriaUtility.runnerNameJPA(joinRunner), joinClub.get(RtClub_.name), joinEvent.get(RtEvent_.name), joinEntry.get(RtEntry_.id).get(RtEntryKey_.entryNr), registration.get(RtRegistration_.evtRegistration), joinEntry.get(RtEntry_.evtEntry), joinEntry.get(RtEntry_.currencyUid), joinRace.get(RtRace_.id).get(RtRaceKey_.raceNr), joinRace.get(RtRace_.legClassUid), joinRace.get(RtRace_.eventNr), joinRunner.get(RtRunner_.year))).where(b.and(b.equal(registration.get(RtRegistration_.id).get(RtRegistrationKey_.clientNr), ServerSession.get().getSessionClientNr()), builder.getPredicate()));
    List<Object[]> resultList = JPA.createQuery(selectQuery).getResultList();

    EventConfiguration eventConfiguration = SharedCache.getEventConfiguration();
    List<FeeFormData> feeConfiguration = SharedCache.getFeeConfiguration();
    FeeCalculator calculator = new FeeCalculator(feeConfiguration, eventConfiguration);

    Map<Long, RegistrationRowData> map = new HashMap<>();
    Set<Long> entryNrs = new HashSet<>(Math.max(100, resultList.size()));
    Set<Long> registrationNrs = new HashSet<>(Math.max(100, resultList.size()));
    for (Object[] resultRow : resultList) {
      Long registrationNr = TypeCastUtility.castValue(resultRow[0], Long.class);
      String registrationNo = TypeCastUtility.castValue(resultRow[1], String.class);
      String runnerName = TypeCastUtility.castValue(resultRow[2], String.class);
      String club = TypeCastUtility.castValue(resultRow[3], String.class);
      String event = TypeCastUtility.castValue(resultRow[4], String.class);
      Long entryNr = TypeCastUtility.castValue(resultRow[5], Long.class);
      Date evtRegistration = TypeCastUtility.castValue(resultRow[6], Date.class);
      Date evtEntry = TypeCastUtility.castValue(resultRow[7], Date.class);
      Long currencyUid = TypeCastUtility.castValue(resultRow[8], Long.class);
      Long raceNr = TypeCastUtility.castValue(resultRow[9], Long.class);
      Long legClassUid = TypeCastUtility.castValue(resultRow[10], Long.class);
      Long eventNr = TypeCastUtility.castValue(resultRow[11], Long.class);
      Long runnerYear = TypeCastUtility.castValue(resultRow[12], Long.class);

      RegistrationRowData rowData = null;
      if (map.get(registrationNr) == null) {
        RegistrationRowData newRowData = new RegistrationRowData();
        newRowData.setRegistrationNr(registrationNr);
        newRowData.setRegistrationNo(registrationNo);
        newRowData.setEvtRegistration(evtRegistration);
        rowData = map.put(registrationNr, newRowData);
      }

      rowData = map.get(registrationNr);
      rowData.setRunnerName(StringUtility.concatenateTokens(rowData.getRunnerName(), "<br>", runnerName));
      rowData.setClubs(StringUtility.concatenateTokens(rowData.getClubs(), "<br>", club));
      rowData.setEvents(StringUtility.concatenateTokens(rowData.getEvents(), "<br>", event));
      Entry entry = rowData.new Entry(entryNr, evtEntry, currencyUid);
      if (rowData.getEntries().contains(entry)) {
        // if entry already exists, copy existing races
        int index = rowData.getEntries().indexOf(entry);
        Entry existingEntry = rowData.getEntries().get(index);
        entry.getRaces().addAll(existingEntry.getRaces());
      }
      entry.getRaces().add(calculator.new RaceInput(raceNr, legClassUid, eventNr, runnerYear));
      rowData.getEntries().add(entry);
      entryNrs.add(entryNr);
      registrationNrs.add(registrationNr);
    }

    // select add info for selected entries
    Map<Long /* entryNr */, List<AddInfoInput>> addInfos = getRegistrationTableAdditionalInformation(entryNrs, calculator);

    // select payment sum for selected registrations
    Map<Long /* paymentNr */, Double> payments = getRegistrationTablePayments(registrationNrs);

    resultList.clear();
    for (RegistrationRowData rowData : map.values()) {
      Object[] row = new Object[10];
      row[0] = rowData.getRegistrationNr();
      row[1] = rowData.getRegistrationNo();
      row[2] = FMilaUtility.boxHtmlBody(rowData.getRunnerName());
      row[3] = FMilaUtility.boxHtmlBody(rowData.getClubs());
      row[4] = FMilaUtility.boxHtmlBody(rowData.getEvents());
      row[5] = rowData.getEntries().size();

      int raceCount = 0;
      double totalPrice = 0;
      for (RegistrationRowData.Entry entry : rowData.getEntries()) {
        FeeResult feeResult = calculator.calculateFees(entry.getRaces(), addInfos.get(entry.getEntryNr()), entry.getEvtEntry(), entry.getCurrencyUid());
        totalPrice += feeResult.getSum();
        raceCount += entry.getRaces().size();
      }

      row[6] = raceCount;
      row[7] = totalPrice; // Total Price
      row[8] = payments.get(rowData.getRegistrationNr()); // Total Payments
      row[9] = rowData.getEvtRegistration();
      resultList.add(row);
    }

    return JPAUtility.convertList2Array(resultList);
  }

  private Map<Long /* paymentNr */, Double /* sum */> getRegistrationTablePayments(Set<Long> registrationNrs) {
    Map<Long, Double> result = new HashMap<>();
    if (registrationNrs.size() == 0) {
      return result;
    }
    CriteriaBuilder b = JPA.getCriteriaBuilder();

    CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
    Root<RtPayment> payment = selectQuery.from(RtPayment.class);
    Join<RtPayment, RtCurrency> joinCurrency = payment.join(RtPayment_.rtCurrency, JoinType.INNER);

    selectQuery.select(b.array(payment.get(RtPayment_.amount), joinCurrency.get(RtCurrency_.exchangeRate), payment.get(RtPayment_.registrationNr))).where(b.and(b.equal(payment.get(RtPayment_.id).get(RtPaymentKey_.clientNr), ServerSession.get().getSessionClientNr()), payment.get(RtPayment_.registrationNr).in(registrationNrs)));

    List<Object[]> resultList = JPA.createQuery(selectQuery).getResultList();
    for (Object[] row : resultList) {
      Double amount = TypeCastUtility.castValue(row[0], Double.class);
      Double exchangeRate = TypeCastUtility.castValue(row[1], Double.class);
      Long registrationNr = TypeCastUtility.castValue(row[2], Long.class);
      Double existingSum = NumberUtility.nvl(result.get(registrationNr), 0d);
      result.put(registrationNr, existingSum + (amount * exchangeRate));
    }
    return result;
  }

  private Map<Long, List<AddInfoInput>> getRegistrationTableAdditionalInformation(Set<Long> entryNrs, FeeCalculator calculator) throws ProcessingException {
    Map<Long, List<AddInfoInput>> addInfos = new HashMap<>();
    if (entryNrs.size() == 0) {
      return addInfos;
    }

    List<AdditionalInformationValueBean> addInfoDefs = SharedCache.getAddInfoForEntity(EntityCodeType.EntryCode.ID, ServerSession.get().getSessionClientNr());
    List<?> addInfoResultList = AdditionalInformationDatabaseUtility.selectValues(EntityCodeType.EntryCode.ID, true, null, ServerSession.get().getSessionClientNr(), addInfoDefs);

    for (Object result : addInfoResultList) {
      Object[] resultRow = (Object[]) result;
      List<AddInfoInput> list = new ArrayList<>();
      Long entryNr = TypeCastUtility.castValue(resultRow[0], Long.class);
      AddInfoInput addInfoInput = calculator.new AddInfoInput();
      for (int k = 1; k < addInfoDefs.size(); k++) {
        Object value = resultRow[k];
        addInfoInput.setTypeUid(addInfoDefs.get(k).getTypeUid());
        if (CompareUtility.equals(addInfoInput.getTypeUid(), AdditionalInformationTypeCodeType.TextCode.ID)) {
          addInfoInput.setText(TypeCastUtility.castValue(value, String.class));
        }
        else if (CompareUtility.equals(addInfoInput.getTypeUid(), AdditionalInformationTypeCodeType.DoubleCode.ID)) {
          addInfoInput.setDecimal(TypeCastUtility.castValue(value, Double.class));
        }
        else {
          addInfoInput.setInteger(TypeCastUtility.castValue(value, Long.class));
        }
        addInfoInput.setAdditionalInformationUid(addInfoDefs.get(k).getAdditionalInformationUid());
      }
      list.add(addInfoInput);
      addInfos.put(entryNr, list);
    }
    return addInfos;
  }

  @Override
  public Object[][] getPaymentTableData(Long registrationNr) throws ProcessingException {
    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
    Root<RtPayment> payment = selectQuery.from(RtPayment.class);
    Join<RtPayment, RtRegistration> joinRegistration = payment.join(RtPayment_.rtRegistration);

    selectQuery.select(b.array(payment.get(RtPayment_.id).get(RtPaymentKey_.clientNr), // placeholder for 0
    payment.get(RtPayment_.id).get(RtPaymentKey_.paymentNr), payment.get(RtPayment_.paymentNo), payment.get(RtPayment_.amount), payment.get(RtPayment_.currencyUid), joinRegistration.get(RtRegistration_.registrationNo), payment.get(RtPayment_.evtPayment), payment.get(RtPayment_.typeUid))).where(b.and(b.equal(payment.get(RtPayment_.id).get(RtPaymentKey_.clientNr), ServerSession.get().getSessionClientNr()), registrationNr == null ? b.conjunction() : b.equal(payment.get(RtPayment_.registrationNr), (registrationNr == null ? payment.get(RtPayment_.registrationNr) : registrationNr))));

    Object[][] array = JPAUtility.convertList2Array(JPA.createQuery(selectQuery).getResultList());
    for (Object[] row : array) {
      row[0] = 0L;
    }
    return array;
  }

  @Override
  public List<StartlistSettingRowData> getStartlistSettingTableData(Long eventNr) throws ProcessingException {
    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
    Root<RtEventClass> eventClass = selectQuery.from(RtEventClass.class);
    Join<RtEventClass, RtStartlistSetting> joinStartlistSetting = eventClass.join(RtEventClass_.rtStartlistSetting, JoinType.LEFT);
    Join<RtStartlistSetting, RtEvent> joinEvent = joinStartlistSetting.join(RtStartlistSetting_.rtEvent, JoinType.LEFT);
    Join<RtEventClass, RtCourse> joinCourse = eventClass.join(RtEventClass_.rtCourse, JoinType.LEFT);

    selectQuery.select(b.array(joinStartlistSetting.get(RtStartlistSetting_.id).get(RtStartlistSettingKey_.startlistSettingNr), eventClass.get(RtEventClass_.id).get(RtEventClassKey_.eventNr), joinStartlistSetting.get(RtStartlistSetting_.bibNoOrderUid), eventClass.get(RtEventClass_.id).get(RtEventClassKey_.classUid), eventClass.get(RtEventClass_.typeUid), joinCourse.get(RtCourse_.shortcut), joinCourse.get(RtCourse_.id).get(RtCourseKey_.courseNr), joinStartlistSetting.get(RtStartlistSetting_.typeUid), joinStartlistSetting.get(RtStartlistSetting_.firstStarttime), joinEvent.get(RtEvent_.evtZero), joinStartlistSetting.get(RtStartlistSetting_.startInterval), joinStartlistSetting.get(RtStartlistSetting_.bibNoFrom), joinStartlistSetting.get(RtStartlistSetting_.vacantAbsolute), joinStartlistSetting.get(RtStartlistSetting_.vacantPercent))).where(b.and(eventNr == null ? b.conjunction() : b.equal(eventClass.get(RtEventClass_.id).get(RtEventClassKey_.eventNr), eventNr), b.isNull(eventClass.get(RtEventClass_.parentUid))));
    List<Object[]> resultList = JPA.createQuery(selectQuery).getResultList();

    ParticipationStatistics statistics = getStartlistSettingTablePageParticipationCount();

    List<StartlistSettingRowData> result = new ArrayList<>();
    Map<Long, List<String>> sameSettingLookup = new HashMap<>();
    for (Object[] row : resultList) {
      StartlistSettingRowData r = new StartlistSettingRowData();

      Long startlistSettingNr = TypeCastUtility.castValue(row[0], Long.class);
      Long currentEventNr = TypeCastUtility.castValue(row[1], Long.class);
      Long bibNoOrderUid = TypeCastUtility.castValue(row[2], Long.class);
      Long classUid = TypeCastUtility.castValue(row[3], Long.class);
      Long classTypeUid = TypeCastUtility.castValue(row[4], Long.class);
      String course = TypeCastUtility.castValue(row[5], String.class);
      Long courseNr = TypeCastUtility.castValue(row[6], Long.class);
      Long typeUid = TypeCastUtility.castValue(row[7], Long.class);
      Long firstStart = TypeCastUtility.castValue(row[8], Long.class);
      Date evtZero = TypeCastUtility.castValue(row[9], Date.class);
      Long startInterval = TypeCastUtility.castValue(row[10], Long.class);
      Long bibNoFrom = TypeCastUtility.castValue(row[11], Long.class);
      Long percent = TypeCastUtility.castValue(row[12], Long.class);
      Long absolute = TypeCastUtility.castValue(row[13], Long.class);
      Long participationCount = 0L;
      if (startlistSettingNr == null) {
        Map<Long, Long> map = statistics.getClassUidCount().get(currentEventNr);
        if (map != null) {
          participationCount = NumberUtility.nvl(map.get(classUid), 0);
        }
      }
      else {
        participationCount = statistics.getStartlistSettingNrCount().get(startlistSettingNr);
      }

      Date firstStartDate = FMilaUtility.addMilliSeconds(evtZero, firstStart);
      Date lastStart = StartlistSettingUtility.calculateLastStart(firstStartDate, startInterval, participationCount, percent, absolute);
      Long vacantCount = StartlistSettingUtility.calculateVacantCount(participationCount, percent, absolute);

      r.setStartlistSettingNr(startlistSettingNr);
      r.setEventNr(currentEventNr);
      r.setBibNoOrderUid(bibNoOrderUid);
      r.setClazzUid(classUid);
      r.setClazzTypeUid(classTypeUid);
      r.setCourse(course);
      r.setCourseNr(courseNr);
      r.setTypeUid(NumberUtility.nvl(typeUid, StartlistTypeCodeType.StartlistNoneCode.ID));
      r.setFirstStart(firstStartDate);
      r.setLastStart(lastStart);
      r.setParticipationCount(participationCount);
      r.setInterval(startInterval);
      r.setBibNoFrom(bibNoFrom);
      r.setVacant(vacantCount);
      result.add(r);

      if (r.getStartlistSettingNr() != null) {
        if (sameSettingLookup.get(r.getStartlistSettingNr()) == null) {
          sameSettingLookup.put(r.getStartlistSettingNr(), new ArrayList<String>());
        }
        sameSettingLookup.get(r.getStartlistSettingNr()).add(FMilaUtility.getCodeText(ClassCodeType.class, r.getClazzUid()));
      }
    }

    // create columns with all classes sharing the same startlist setting
    for (StartlistSettingRowData row : result) {
      if (row.getStartlistSettingNr() != null) {
        List<String> sameClassesList = sameSettingLookup.get(row.getStartlistSettingNr());
        Collections.sort(sameClassesList);
        row.setSameClasses(CollectionUtility.format(sameClassesList));
      }
    }

    return result;
  }

  private ParticipationStatistics getStartlistSettingTablePageParticipationCount() {
    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<Object[]> selectCountQuery = b.createQuery(Object[].class);
    Root<RtParticipation> participation = selectCountQuery.from(RtParticipation.class);
    Join<RtParticipation, RtEventClass> joinEventClass = participation.join(RtParticipation_.rtEventClass, JoinType.INNER);
    Join<RtEventClass, RtCourse> joinCourse2 = joinEventClass.join(RtEventClass_.rtCourse, JoinType.LEFT);

    selectCountQuery.select(b.array(participation.get(RtParticipation_.id).get(RtParticipationKey_.eventNr), joinEventClass.get(RtEventClass_.id).get(RtEventClassKey_.classUid), joinEventClass.get(RtEventClass_.startlistSettingNr), joinCourse2.get(RtCourse_.startlistSettingNr))).where(b.and(b.equal(participation.get(RtParticipation_.id).get(RtParticipationKey_.clientNr), ServerSession.get().getSessionClientNr())));

    List<Object[]> particpationList = JPA.createQuery(selectCountQuery).getResultList();
    Map<Long /* startlistSettingNr */, Long /* count */> countCacheStartlistSettingNr = new HashMap<>();
    Map<Long /* eventNr */, Map<Long /* classUid */, Long /* count */>> countCacheClassUid = new HashMap<>();
    for (Object[] resultRow : particpationList) {
      Long participationEventNr = TypeCastUtility.castValue(resultRow[0], Long.class);
      Long classUid = TypeCastUtility.castValue(resultRow[1], Long.class);
      Long eventClassStartlistSettingNr = TypeCastUtility.castValue(resultRow[2], Long.class);
      Long courseStartlistSettingNr = TypeCastUtility.castValue(resultRow[3], Long.class);

      Long startlistSettingNr = eventClassStartlistSettingNr;
      if (startlistSettingNr == null) {
        startlistSettingNr = courseStartlistSettingNr;
      }
      // startlist setting exists
      if (startlistSettingNr != null) {
        if (!countCacheStartlistSettingNr.containsKey(startlistSettingNr)) {
          countCacheStartlistSettingNr.put(startlistSettingNr, 1L);
        }
        else {
          countCacheStartlistSettingNr.put(startlistSettingNr, countCacheStartlistSettingNr.get(startlistSettingNr) + 1L);
        }
      }
      // no startlist setting, calculate for classUid
      else {
        if (!countCacheClassUid.containsKey(participationEventNr)) {
          countCacheClassUid.put(participationEventNr, new HashMap<Long, Long>());
        }
        Map<Long, Long> countCacheForEvent = countCacheClassUid.get(participationEventNr);
        if (!countCacheForEvent.containsKey(classUid)) {
          countCacheForEvent.put(classUid, 1L);
        }
        else {
          countCacheForEvent.put(classUid, countCacheForEvent.get(classUid) + 1L);
        }
      }
    }
    ParticipationStatistics result = new ParticipationStatistics(countCacheStartlistSettingNr, countCacheClassUid);
    return result;
  }

  private class ParticipationStatistics {
    Map<Long /* startlistSettingNr */, Long /* count */> startlistSettingNrCount;
    Map<Long /* eventNr */, Map<Long /* classUid */, Long /* count */>> classUidCount;

    public ParticipationStatistics(Map<Long, Long> startlistSettingNrCount, Map<Long, Map<Long, Long>> classUidCount) {
      super();
      this.startlistSettingNrCount = startlistSettingNrCount;
      this.classUidCount = classUidCount;
    }

    public Map<Long, Map<Long, Long>> getClassUidCount() {
      return classUidCount;
    }

    public Map<Long, Long> getStartlistSettingNrCount() {
      return startlistSettingNrCount;
    }

  }

  @Override
  public Object[][] getEventStartblockTableData(Long eventNr) throws ProcessingException {
    String queryString = "SELECT SB.id.startblockUid, L.codeName, SB.sortcode, COUNT(P.id.entryNr) " + "FROM RtEventStartblock SB " + "LEFT OUTER JOIN SB.rtParticipations P " + "INNER JOIN SB.rtUc U " + "INNER JOIN U.rtUcls L " + "WHERE L.id.languageUid = :languageUid " + "AND SB.id.eventNr = :eventNr " + "AND SB.id.clientNr = :sessionClientNr " + "GROUP BY SB.id.startblockUid, SB.sortcode, L.codeName ";
    FMilaTypedQuery<Object[]> query = JPA.createQuery(queryString, Object[].class);
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    query.setParameter("eventNr", eventNr);
    query.setParameter("languageUid", ServerSession.get().getLanguageUid());
    List<Object[]> result = query.getResultList();

    queryString = "SELECT COUNT(P.id.entryNr) " + "FROM RtParticipation P " + "WHERE P.startblockUid IS NULL " + "AND P.id.clientNr = :sessionClientNr " + "AND P.id.eventNr = :eventNr ";
    FMilaTypedQuery<Long> typedQuery = JPA.createQuery(queryString, Long.class);
    typedQuery.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    typedQuery.setParameter("eventNr", eventNr);
    Long count = typedQuery.getSingleResult();
    Object[] row = new Object[]{null, "<" + Texts.get("none") + ">", null, count};
    result.add(row);

    return JPAUtility.convertList2Array(result);
  }

  @Override
  public Object[][] getEntryClassTableData(SearchFilter filter) throws ProcessingException {
    Long eventNr = ((SingleEventSearchFormData) filter.getFormData()).getEvent().getValue();

    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
    Root<RtEventClass> eventClass = selectQuery.from(RtEventClass.class);
    Join<RtEventClass, RtRace> joinRace = eventClass.join(RtEventClass_.rtRaces, JoinType.LEFT);

    selectQuery.select(b.array(eventClass.get(RtEventClass_.id).get(RtEventClassKey_.classUid), eventClass.get(RtEventClass_.sortcode), b.countDistinct(joinRace.get(RtRace_.entryNr)), b.countDistinct(joinRace.get(RtRace_.id).get(RtRaceKey_.raceNr)))).where(eventNr != null ? b.equal(joinRace.get(RtRace_.eventNr), eventNr) : b.conjunction()).groupBy(eventClass.get(RtEventClass_.id).get(RtEventClassKey_.classUid), eventClass.get(RtEventClass_.sortcode));

    List<Object[]> list = JPA.createQuery(selectQuery).getResultList();

    List<Object[]> result = new ArrayList<>();
    for (Object[] row : list) {
      Object[] resultRow = new Object[6];
      Long classUid = TypeCastUtility.castValue(row[0], Long.class);
      resultRow[0] = 0L; // Force Summary Order to bottom
      resultRow[1] = classUid;
      resultRow[2] = FMilaUtility.getCodeText(ClassCodeType.class, classUid);
      resultRow[3] = row[1];
      resultRow[4] = row[2];
      resultRow[5] = row[3];
      result.add(resultRow);
    }
    return JPAUtility.convertList2Array(result);
  }

  @Override
  public Object[][] getEntryClubTableData(SearchFilter filter) throws ProcessingException {
    Long eventNr = ((SingleEventSearchFormData) filter.getFormData()).getEvent().getValue();

    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
    Root<RtClub> club = selectQuery.from(RtClub.class);
    Join<RtClub, RtRace> joinRace = club.join(RtClub_.rtRaces, JoinType.LEFT);

    selectQuery.select(b.array(club.get(RtClub_.id).get(RtClubKey_.clubNr), club.get(RtClub_.name), b.countDistinct(joinRace.get(RtRace_.entryNr)), b.countDistinct(joinRace.get(RtRace_.id).get(RtRaceKey_.raceNr)))).where(eventNr != null ? b.equal(joinRace.get(RtRace_.eventNr), eventNr) : b.conjunction()).groupBy(club.get(RtClub_.id).get(RtClubKey_.clubNr), club.get(RtClub_.name));
    return JPAUtility.convertList2Array(JPA.createQuery(selectQuery).getResultList());
  }

  @Override
  public Object[][] getEntryCourseTableData(SearchFilter filter) throws ProcessingException {
    Long eventNr = ((SingleEventSearchFormData) filter.getFormData()).getEvent().getValue();

    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
    Root<RtCourse> course = selectQuery.from(RtCourse.class);
    Join<RtCourse, RtEventClass> joinEventClasses = course.join(RtCourse_.rtEventClasses, JoinType.LEFT);
    Join<RtEventClass, RtRace> joinRaces = joinEventClasses.join(RtEventClass_.rtRaces, JoinType.LEFT);

    selectQuery.select(b.array(course.get(RtCourse_.id).get(RtCourseKey_.courseNr), course.get(RtCourse_.shortcut), b.countDistinct(joinRaces.get(RtRace_.entryNr)), b.countDistinct(joinRaces.get(RtRace_.id).get(RtRaceKey_.raceNr)))).where(eventNr != null ? b.equal(joinRaces.get(RtRace_.eventNr), eventNr) : b.conjunction()).groupBy(course.get(RtCourse_.id).get(RtCourseKey_.courseNr), course.get(RtCourse_.shortcut));

    List<Object[]> list = JPA.createQuery(selectQuery).getResultList();

    List<Object[]> result = new ArrayList<>();
    for (Object[] row : list) {
      Object[] resultRow = new Object[5];
      resultRow[0] = 0L; // Force Summary Order to bottom
      resultRow[1] = row[0];
      resultRow[2] = row[1];
      resultRow[3] = row[2];
      resultRow[4] = row[3];
      result.add(resultRow);
    }
    return JPAUtility.convertList2Array(result);
  }

  @Override
  public Object[][] getVacantsTableData(Long eventNr) throws ProcessingException {

    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
    Root<RtStartlistSettingVacant> startlistSettingVacant = selectQuery.from(RtStartlistSettingVacant.class);
    Join<RtStartlistSettingVacant, RtStartlistSetting> joinStartlistSetting = startlistSettingVacant.join(RtStartlistSettingVacant_.rtStartlistSetting, JoinType.INNER);
    Join<RtStartlistSetting, RtEventClass> joinEventClass = joinStartlistSetting.join(RtStartlistSetting_.rtEventClasses, JoinType.INNER);
    Join<RtEventClass, RtEvent> joinEvent = joinEventClass.join(RtEventClass_.rtEvent, JoinType.INNER);
    Join<RtEventClass, RtCourse> joinCourse = joinEventClass.join(RtEventClass_.rtCourse, JoinType.LEFT);

    selectQuery.select(b.array(startlistSettingVacant.get(RtStartlistSettingVacant_.id).get(RtStartlistSettingVacantKey_.startlistSettingNr), joinEventClass.get(RtEventClass_.id).get(RtEventClassKey_.classUid), joinCourse.get(RtCourse_.shortcut), startlistSettingVacant.get(RtStartlistSettingVacant_.id).get(RtStartlistSettingVacantKey_.startTime), joinEvent.get(RtEvent_.evtZero), startlistSettingVacant.get(RtStartlistSettingVacant_.id).get(RtStartlistSettingVacantKey_.bibNo))).where(b.and(b.equal(startlistSettingVacant.get(RtStartlistSettingVacant_.id).get(RtStartlistSettingVacantKey_.clientNr), ServerSession.get().getSessionClientNr()), b.equal(joinEventClass.get(RtEventClass_.id).get(RtEventClassKey_.eventNr), eventNr)));
    List<Object[]> resultList = JPA.createQuery(selectQuery).getResultList();

    EntryTableDataOptions options = new EntryTableDataOptions();
    options.setPresentationType(EntryList.ALL);
    options.setClientNr(ServerSession.get().getSessionClientNr());
    EntriesSearchFormData formData = new EntriesSearchFormData();
    formData.getEvent().setValue(eventNr);
    List<EntryRowData> entries = getEntryTableData(options, formData);
    Map<Long /* classUid */, Map<Long /* starttime */, List<String>>> classStarttimeRunnerNames = new HashMap<>();
    for (EntryRowData entry : entries) {
      if (!classStarttimeRunnerNames.containsKey(entry.getLegClassUid())) {
        classStarttimeRunnerNames.put(entry.getLegClassUid(), new HashMap<Long, List<String>>());
      }
      Map<Long, List<String>> starttimeRunnerNames = classStarttimeRunnerNames.get(entry.getLegClassUid());
      if (!starttimeRunnerNames.containsKey(entry.getRelativeStartTime())) {
        starttimeRunnerNames.put(entry.getRelativeStartTime(), new ArrayList<String>());
      }
      starttimeRunnerNames.get(entry.getRelativeStartTime()).add(entry.getRunner());
    }

    List<Object[]> result = new ArrayList<>();
    for (Object[] resultRow : resultList) {
      Object[] row = new Object[6];
      Long startlistSettingNr = TypeCastUtility.castValue(resultRow[0], Long.class);
      Long classUid = TypeCastUtility.castValue(resultRow[1], Long.class);
      String shortcut = TypeCastUtility.castValue(resultRow[2], String.class);
      Long starttime = TypeCastUtility.castValue(resultRow[3], Long.class);
      Date evtZero = TypeCastUtility.castValue(resultRow[4], Date.class);
      String bibNo = TypeCastUtility.castValue(resultRow[5], String.class);

      row[0] = startlistSettingNr;
      row[1] = classUid;
      row[2] = shortcut;
      row[3] = FMilaUtility.addMilliSeconds(evtZero, starttime);
      row[4] = bibNo;
      Map<Long, List<String>> cache = classStarttimeRunnerNames.get(classUid);
      if (cache != null) {
        row[5] = FMilaUtility.boxHtmlBody(CollectionUtility.format(cache.get(starttime), "<br>"));
      }

      result.add(row);
    }

    return JPAUtility.convertList2Array(result);

//    return SQL.select("SELECT " +
//        "SSV.STARTLIST_SETTING_NR, " +
//        "EC.CLASS_UID, " +
//        "C.SHORTCUT, " +
//        FMilaSQLUtility.convertMillisecondsToDateSQL("SSV.START_TIME", "E.EVT_ZERO") + ", " +
//        "SSV.BIB_NO, " +
//        "(SELECT GROUP_CONCAT(" + FMilaSQLUtility.runnerNameSQL() + ") " +
//        " FROM RT_PARTICIPATION P " +
//        " INNER JOIN RT_RACE RA ON P.ENTRY_NR = RA.ENTRY_NR AND EC.EVENT_NR = RA.EVENT_NR AND RA.CLIENT_NR = SSV.CLIENT_NR " +
//        " INNER JOIN RT_RUNNER R ON RA.RUNNER_NR = R.RUNNER_NR AND RA.CLIENT_NR = R.CLIENT_NR " +
//        " WHERE P.START_TIME = SSV.START_TIME" +
//        " AND P.CLASS_UID = EC.CLASS_UID " +
//        " AND P.EVENT_NR = :eventNr " +
//        " AND P.CLIENT_NR = SSV.CLIENT_NR) " +
//        "FROM RT_STARTLIST_SETTING_VACANT SSV " +
//        "INNER JOIN RT_STARTLIST_SETTING SS ON SS.STARTLIST_SETTING_NR = SSV.STARTLIST_SETTING_NR AND SS.CLIENT_NR = SSV.CLIENT_NR " +
//        "INNER JOIN RT_EVENT_CLASS EC ON EC.STARTLIST_SETTING_NR = SSV.STARTLIST_SETTING_NR AND EC.CLIENT_NR = SSV.CLIENT_NR " +
//        "INNER JOIN RT_EVENT E ON EC.EVENT_NR = E.EVENT_NR AND E.CLIENT_NR = EC.CLIENT_NR " +
//        "LEFT JOIN RT_COURSE C ON EC.COURSE_NR = C.COURSE_NR AND EC.CLIENT_NR = C.CLIENT_NR " +
//        "WHERE SSV.CLIENT_NR = :sessionClientNr " +
//        "AND EC.EVENT_NR = :eventNr " +
//        "", new NVPair("eventNr", eventNr));
  }
}
