package com.rtiming.server.runner;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;

import org.eclipse.scout.commons.exception.ProcessingException;

import com.rtiming.server.club.JPAClubBoxSearchFormDataStatementBuilder;
import com.rtiming.server.common.AbstractJPASearchFormDataStatementBuilder;
import com.rtiming.server.ecard.JPAECardBoxSearchFormDataStatementBuilder;
import com.rtiming.server.settings.addinfo.JPAAdditionalInformationBoxSearchFormDataStatementBuilder;
import com.rtiming.server.settings.city.JPACityBoxSearchFormDataStatementBuilder;
import com.rtiming.shared.common.EntityCodeType;
import com.rtiming.shared.dao.RtCity;
import com.rtiming.shared.dao.RtClub;
import com.rtiming.shared.dao.RtEcard;
import com.rtiming.shared.dao.RtRunner;
import com.rtiming.shared.dao.RtRunnerKey_;
import com.rtiming.shared.dao.RtRunner_;
import com.rtiming.shared.runner.RunnersSearchFormData;

public class JPARunnersSearchFormDataStatementBuilder extends AbstractJPASearchFormDataStatementBuilder<RunnersSearchFormData> {

  private final CriteriaQuery<Object[]> query;
  private final Path<RtRunner> runner;
  private final Path<RtClub> club;
  private final Path<RtCity> city;
  private final Path<RtEcard> ecard;

  public JPARunnersSearchFormDataStatementBuilder(CriteriaQuery<Object[]> query, Path<RtRunner> runner,
      Path<RtClub> club, Path<RtCity> city, Path<RtEcard> ecard) {
    super();
    this.query = query;
    this.runner = runner;
    this.club = club;
    this.city = city;
    this.ecard = ecard;
  }

  @Override
  public void build(RunnersSearchFormData searchFormData) throws ProcessingException {
    super.build(searchFormData);

    // Runner General
    JPARunnerBoxSearchFormDataStatementBuilder runnerBuilder = new JPARunnerBoxSearchFormDataStatementBuilder(runner);
    runnerBuilder.build(searchFormData.getRunnerBox());
    addPredicate(runnerBuilder.getPredicate());

    // Runner Details
    JPARunnerBoxDetailSearchFormDataStatementBuilder detailBuilder = new JPARunnerBoxDetailSearchFormDataStatementBuilder(runner);
    detailBuilder.build(searchFormData.getDetailsBox());
    addPredicate(detailBuilder.getPredicate());

    // Club Search
    JPAClubBoxSearchFormDataStatementBuilder clubBuilder = new JPAClubBoxSearchFormDataStatementBuilder(club);
    clubBuilder.build(searchFormData.getClubBox());
    addPredicate(clubBuilder.getPredicate());

    // City Search
    JPACityBoxSearchFormDataStatementBuilder cityBuilder = new JPACityBoxSearchFormDataStatementBuilder(city);
    cityBuilder.build(searchFormData.getCityBox());
    addPredicate(cityBuilder.getPredicate());

    // ECard Search
    JPAECardBoxSearchFormDataStatementBuilder ecardBuilder = new JPAECardBoxSearchFormDataStatementBuilder(ecard);
    ecardBuilder.build(searchFormData.getECardBox());
    addPredicate(ecardBuilder.getPredicate());

    // Add Info
    JPAAdditionalInformationBoxSearchFormDataStatementBuilder addInfoBuilder = new JPAAdditionalInformationBoxSearchFormDataStatementBuilder(runner.get(RtRunner_.id).get(RtRunnerKey_.runnerNr), EntityCodeType.RunnerCode.ID);
    addInfoBuilder.build(searchFormData.getAdditionalInformationBox());
    addPredicate(addInfoBuilder.getPredicate());
  }

}
