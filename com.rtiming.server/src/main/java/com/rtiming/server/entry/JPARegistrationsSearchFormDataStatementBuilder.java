package com.rtiming.server.entry;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.server.club.JPAClubBoxSearchFormDataStatementBuilder;
import com.rtiming.server.common.AbstractJPASearchFormDataStatementBuilder;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.ecard.JPAECardBoxSearchFormDataStatementBuilder;
import com.rtiming.server.runner.JPARunnerBoxDetailSearchFormDataStatementBuilder;
import com.rtiming.server.runner.JPARunnerBoxSearchFormDataStatementBuilder;
import com.rtiming.server.settings.addinfo.JPAAdditionalInformationBoxSearchFormDataStatementBuilder;
import com.rtiming.server.settings.city.JPACityBoxSearchFormDataStatementBuilder;
import com.rtiming.shared.club.AbstractClubSearchBoxData;
import com.rtiming.shared.common.EntityCodeType;
import com.rtiming.shared.dao.RtAddress;
import com.rtiming.shared.dao.RtAddress_;
import com.rtiming.shared.dao.RtCity;
import com.rtiming.shared.dao.RtClub;
import com.rtiming.shared.dao.RtEcard;
import com.rtiming.shared.dao.RtEntry;
import com.rtiming.shared.dao.RtEntryKey_;
import com.rtiming.shared.dao.RtEntry_;
import com.rtiming.shared.dao.RtRace;
import com.rtiming.shared.dao.RtRace_;
import com.rtiming.shared.dao.RtRegistration;
import com.rtiming.shared.dao.RtRegistrationKey_;
import com.rtiming.shared.dao.RtRegistration_;
import com.rtiming.shared.dao.RtRunner;
import com.rtiming.shared.ecard.AbstractECardSearchBoxData;
import com.rtiming.shared.entry.RegistrationsSearchFormData;
import com.rtiming.shared.runner.AbstractRunnerDetailsSearchBoxData;
import com.rtiming.shared.runner.AbstractRunnerSearchBoxData;
import com.rtiming.shared.settings.city.AbstractCitySearchBoxData;

public class JPARegistrationsSearchFormDataStatementBuilder extends AbstractJPASearchFormDataStatementBuilder<RegistrationsSearchFormData> {

  private final CriteriaQuery<Object[]> query;
  private final Path<RtRegistration> registration;
  private final Path<RtEntry> entry;

  public JPARegistrationsSearchFormDataStatementBuilder(CriteriaQuery<Object[]> query, Path<RtRegistration> registration, Path<RtEntry> entry) {
    super();
    this.query = query;
    this.registration = registration;
    this.entry = entry;
  }

  @Override
  public void build(RegistrationsSearchFormData searchFormData) throws ProcessingException {
    super.build(searchFormData);

    // Registration
    JPARegistrationBoxSearchFormDataStatementBuilder registrationBuilder = new JPARegistrationBoxSearchFormDataStatementBuilder(registration);
    registrationBuilder.build(searchFormData.getRegistrationBox());
    addPredicate(registrationBuilder.getPredicate());

    // Add Info
    JPAAdditionalInformationBoxSearchFormDataStatementBuilder entryBuilder = new JPAAdditionalInformationBoxSearchFormDataStatementBuilder(entry.get(RtEntry_.id).get(RtEntryKey_.entryNr), EntityCodeType.EntryCode.ID);
    entryBuilder.build(searchFormData.getAdditionalInformationBox());
    addPredicate(entryBuilder.getPredicate());
  }

  @Override
  protected void buildForeignBuilders(RegistrationsSearchFormData searchFormData) throws ProcessingException {
    super.buildForeignBuilders(searchFormData);

    // Runner
    buildForeignRunnerBuilder(searchFormData.getRunnerBox());

    // Details
    buildForeignRunnerDetailBuilder(searchFormData.getDetailsBox());

    // E-Card
    buildForeignECardBuilder(searchFormData.getECardBox());

    // Club
    buildForeignClubBuilder(searchFormData.getClubBox());

    // City
    buildForeignCityBuilder(searchFormData.getCityBox());
  }

  private void buildForeignRunnerBuilder(AbstractRunnerSearchBoxData searchFormData) throws ProcessingException {
    Subquery<Long> subquery = query.subquery(Long.class);
    Root<RtRegistration> subroot = subquery.from(RtRegistration.class);
    Join<RtRegistration, RtEntry> joinEntry = subroot.join(RtRegistration_.rtEntries, JoinType.LEFT);
    Join<RtEntry, RtRace> joinRace = joinEntry.join(RtEntry_.rtRaces, JoinType.LEFT);
    Join<RtRace, RtRunner> joinRunner = joinRace.join(RtRace_.rtRunner, JoinType.LEFT);

    JPARunnerBoxSearchFormDataStatementBuilder foreignBuilder = new JPARunnerBoxSearchFormDataStatementBuilder(joinRunner);
    foreignBuilder.build(searchFormData);

    existsInForeignBuilder(subquery, subroot, foreignBuilder);
  }

  private void buildForeignRunnerDetailBuilder(AbstractRunnerDetailsSearchBoxData searchFormData) throws ProcessingException {
    Subquery<Long> subquery = query.subquery(Long.class);
    Root<RtRegistration> subroot = subquery.from(RtRegistration.class);
    Join<RtRegistration, RtEntry> joinEntry = subroot.join(RtRegistration_.rtEntries, JoinType.LEFT);
    Join<RtEntry, RtRace> joinRace = joinEntry.join(RtEntry_.rtRaces, JoinType.LEFT);
    Join<RtRace, RtRunner> joinRunner = joinRace.join(RtRace_.rtRunner, JoinType.LEFT);

    JPARunnerBoxDetailSearchFormDataStatementBuilder foreignBuilder = new JPARunnerBoxDetailSearchFormDataStatementBuilder(joinRunner);
    foreignBuilder.build(searchFormData);

    existsInForeignBuilder(subquery, subroot, foreignBuilder);
  }

  private void buildForeignECardBuilder(AbstractECardSearchBoxData searchFormData) throws ProcessingException {
    Subquery<Long> subquery = query.subquery(Long.class);
    Root<RtRegistration> subroot = subquery.from(RtRegistration.class);
    Join<RtRegistration, RtEntry> joinEntry = subroot.join(RtRegistration_.rtEntries, JoinType.LEFT);
    Join<RtEntry, RtRace> joinRace = joinEntry.join(RtEntry_.rtRaces, JoinType.LEFT);
    Join<RtRace, RtEcard> joinECard = joinRace.join(RtRace_.rtEcard, JoinType.LEFT);

    JPAECardBoxSearchFormDataStatementBuilder foreignBuilder = new JPAECardBoxSearchFormDataStatementBuilder(joinECard);
    foreignBuilder.build(searchFormData);

    existsInForeignBuilder(subquery, subroot, foreignBuilder);
  }

  private void buildForeignClubBuilder(AbstractClubSearchBoxData searchFormData) throws ProcessingException {
    Subquery<Long> subquery = query.subquery(Long.class);
    Root<RtRegistration> subroot = subquery.from(RtRegistration.class);
    Join<RtRegistration, RtEntry> joinEntry = subroot.join(RtRegistration_.rtEntries, JoinType.LEFT);
    Join<RtEntry, RtRace> joinRace = joinEntry.join(RtEntry_.rtRaces, JoinType.LEFT);
    Join<RtRace, RtClub> joinClub = joinRace.join(RtRace_.rtClub, JoinType.LEFT);

    JPAClubBoxSearchFormDataStatementBuilder foreignBuilder = new JPAClubBoxSearchFormDataStatementBuilder(joinClub);
    foreignBuilder.build(searchFormData);

    existsInForeignBuilder(subquery, subroot, foreignBuilder);
  }

  private void buildForeignCityBuilder(AbstractCitySearchBoxData searchFormData) throws ProcessingException {
    Subquery<Long> subquery = query.subquery(Long.class);
    Root<RtRegistration> subroot = subquery.from(RtRegistration.class);
    Join<RtRegistration, RtEntry> joinEntry = subroot.join(RtRegistration_.rtEntries, JoinType.LEFT);
    Join<RtEntry, RtRace> joinRace = joinEntry.join(RtEntry_.rtRaces, JoinType.LEFT);
    Join<RtRace, RtAddress> joinAddress = joinRace.join(RtRace_.rtAddress, JoinType.LEFT);
    Join<RtAddress, RtCity> joinCity = joinAddress.join(RtAddress_.rtCity, JoinType.LEFT);

    JPACityBoxSearchFormDataStatementBuilder foreignBuilder = new JPACityBoxSearchFormDataStatementBuilder(joinCity);
    foreignBuilder.build(searchFormData);

    existsInForeignBuilder(subquery, subroot, foreignBuilder);
  }

  private void existsInForeignBuilder(Subquery<Long> subquery, Root<RtRegistration> subroot, AbstractJPASearchFormDataStatementBuilder<?> foreignBuilder) {
    CriteriaBuilder b = JPA.getCriteriaBuilder();

    subquery.select(subroot.get(RtRegistration_.id).get(RtRegistrationKey_.registrationNr));
    Predicate entityExists = b.exists(
        subquery.where(
            b.and(
                b.equal(
                    subroot.get(RtRegistration_.id).get(RtRegistrationKey_.registrationNr),
                    registration.get(RtRegistration_.id).get(RtRegistrationKey_.registrationNr)
                    ),
                b.equal(
                    subroot.get(RtRegistration_.id).get(RtRegistrationKey_.clientNr),
                    registration.get(RtRegistration_.id).get(RtRegistrationKey_.clientNr)
                    ),
                foreignBuilder.getPredicate())
            )
        );
    addForeignBuilder(foreignBuilder, entityExists);
  }

}
