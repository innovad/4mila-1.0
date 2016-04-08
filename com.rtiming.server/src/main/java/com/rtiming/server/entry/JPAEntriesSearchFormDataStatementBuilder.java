package com.rtiming.server.entry;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.BEANS;

import com.rtiming.server.club.JPAClubBoxSearchFormDataStatementBuilder;
import com.rtiming.server.common.AbstractJPASearchFormDataStatementBuilder;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.ecard.JPAECardBoxSearchFormDataStatementBuilder;
import com.rtiming.server.runner.JPARunnerBoxDetailSearchFormDataStatementBuilder;
import com.rtiming.server.runner.JPARunnerBoxSearchFormDataStatementBuilder;
import com.rtiming.server.settings.addinfo.JPAAdditionalInformationBoxSearchFormDataStatementBuilder;
import com.rtiming.server.settings.city.JPACityBoxSearchFormDataStatementBuilder;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.common.EntityCodeType;
import com.rtiming.shared.dao.RtCity;
import com.rtiming.shared.dao.RtClub;
import com.rtiming.shared.dao.RtEcard;
import com.rtiming.shared.dao.RtEntry;
import com.rtiming.shared.dao.RtEntryKey_;
import com.rtiming.shared.dao.RtEntry_;
import com.rtiming.shared.dao.RtParticipation;
import com.rtiming.shared.dao.RtParticipation_;
import com.rtiming.shared.dao.RtRace;
import com.rtiming.shared.dao.RtRaceControl;
import com.rtiming.shared.dao.RtRaceControl_;
import com.rtiming.shared.dao.RtRaceKey_;
import com.rtiming.shared.dao.RtRace_;
import com.rtiming.shared.dao.RtRunner;
import com.rtiming.shared.entry.EntriesSearchFormData;
import com.rtiming.shared.event.IEventProcessService;

public class JPAEntriesSearchFormDataStatementBuilder extends AbstractJPASearchFormDataStatementBuilder<EntriesSearchFormData> {

  private final Path<RtRace> race;
  private final Path<RtEntry> entry;
  private final Path<RtParticipation> participation;
  private final Path<RtRunner> runner;
  private final Path<RtEcard> ecard;
  private final Path<RtClub> club;
  private final Path<RtCity> city;
  private final CriteriaQuery<Object[]> query;

  public JPAEntriesSearchFormDataStatementBuilder(CriteriaQuery<Object[]> query, Path<RtRace> race, Path<RtEntry> entry,
      Path<RtParticipation> participation, Path<RtRunner> runner, Path<RtEcard> ecard, Path<RtClub> club, Path<RtCity> city) {
    super();
    this.query = query;
    this.race = race;
    this.entry = entry;
    this.participation = participation;
    this.runner = runner;
    this.ecard = ecard;
    this.club = club;
    this.city = city;
  }

  @Override
  public void build(EntriesSearchFormData searchFormData) throws ProcessingException {
    super.build(searchFormData);

    // Startlist
    // Startlist > Start Control
    if (searchFormData.getStart().getValue() != null) {
      Predicate exists = createStartControlWhere(searchFormData.getStart().getValue());
      addPredicate(exists);
    }
    // Startlist > Event: Is handled directly in query
    // Startlist > Starttime From/To
    if (searchFormData.getStartTimeFrom().getValue() != null || searchFormData.getStartTimeTo().getValue() != null || searchFormData.getEvent().getValue() != null) {
      Date startTimeFromDate = searchFormData.getStartTimeFrom().getValue();
      Date startTimeToDate = searchFormData.getStartTimeTo().getValue();
      Date evtZero = BEANS.get(IEventProcessService.class).getZeroTime(searchFormData.getEvent().getValue());
      Long startTimeFrom = FMilaUtility.getDateDifferenceInMilliSeconds(evtZero, startTimeFromDate);
      Long startTimeTo = FMilaUtility.getDateDifferenceInMilliSeconds(evtZero, startTimeToDate);
      if (startTimeFrom != null) {
        addLongGreaterThanOrEqualsWherePart(participation.get(RtParticipation_.startTime), startTimeFrom, true);
      }
      if (startTimeTo != null) {
        addLongLessThanOrEqualsWherePart(participation.get(RtParticipation_.startTime), startTimeTo, true);
      }
    }
    // Startlist > Starttime Yes No All
    addBooleanAsNullWherePart(participation.get(RtParticipation_.startTime), searchFormData.getStartTimeGroup().getValue());
    // Startlist > Startblock
    addLongWherePart(participation.get(RtParticipation_.startblockUid), searchFormData.getStartblocks().getValue());

    // Runner
    JPARunnerBoxSearchFormDataStatementBuilder runnerBuilder = new JPARunnerBoxSearchFormDataStatementBuilder(runner);
    runnerBuilder.build(searchFormData.getRunnerBox());
    addPredicate(runnerBuilder.getPredicate());

    // Details Box
    JPARunnerBoxDetailSearchFormDataStatementBuilder detailBuilder = new JPARunnerBoxDetailSearchFormDataStatementBuilder(runner);
    detailBuilder.build(searchFormData.getDetailsBox());
    addPredicate(detailBuilder.getPredicate());

    // ECard Box
    JPAECardBoxSearchFormDataStatementBuilder ecardBuilder = new JPAECardBoxSearchFormDataStatementBuilder(ecard);
    ecardBuilder.build(searchFormData.getECardBox());
    addPredicate(ecardBuilder.getPredicate());

    // Club Box
    JPAClubBoxSearchFormDataStatementBuilder clubBuilder = new JPAClubBoxSearchFormDataStatementBuilder(club);
    clubBuilder.build(searchFormData.getClubBox());
    addPredicate(clubBuilder.getPredicate());

    // Address Box
    JPACityBoxSearchFormDataStatementBuilder cityBuilder = new JPACityBoxSearchFormDataStatementBuilder(city);
    cityBuilder.build(searchFormData.getCityBox());
    addPredicate(cityBuilder.getPredicate());

    // Additional Info
    JPAAdditionalInformationBoxSearchFormDataStatementBuilder addInfoBuilder = new JPAAdditionalInformationBoxSearchFormDataStatementBuilder(entry.get(RtEntry_.id).get(RtEntryKey_.entryNr), EntityCodeType.EntryCode.ID);
    addInfoBuilder.build(searchFormData.getAdditionalInformationBox());
    addPredicate(addInfoBuilder.getPredicate());
  }

  private Predicate createStartControlWhere(Long startcontrolUid) {
    CriteriaBuilder b = JPA.getCriteriaBuilder();
    Subquery<Long> subquery = query.subquery(Long.class);
    Root<RtRaceControl> subroot = subquery.from(RtRaceControl.class);
    subquery.select(subroot.get(RtRaceControl_.controlNr)).
        where(b.and(
            b.equal(race.get(RtRace_.id).get(RtRaceKey_.raceNr), subroot.get(RtRaceControl_.raceNr)),
            b.equal(subroot.get(RtRaceControl_.controlNr), startcontrolUid)
            ));
    Predicate exists = b.exists(subquery);
    return exists;
  }

}
