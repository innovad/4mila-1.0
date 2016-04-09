package com.rtiming.server.ecard.download;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;

import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.server.common.AbstractJPASearchFormDataStatementBuilder;
import com.rtiming.server.common.database.jpa.JPACriteriaUtility;
import com.rtiming.shared.dao.RtCourse;
import com.rtiming.shared.dao.RtCourseKey_;
import com.rtiming.shared.dao.RtCourse_;
import com.rtiming.shared.dao.RtEcard;
import com.rtiming.shared.dao.RtEcard_;
import com.rtiming.shared.dao.RtEvent;
import com.rtiming.shared.dao.RtEventKey_;
import com.rtiming.shared.dao.RtEvent_;
import com.rtiming.shared.dao.RtPunchSession;
import com.rtiming.shared.dao.RtPunchSession_;
import com.rtiming.shared.dao.RtRace;
import com.rtiming.shared.dao.RtRaceKey_;
import com.rtiming.shared.dao.RtRace_;
import com.rtiming.shared.dao.RtRunner;
import com.rtiming.shared.ecard.download.DownloadedECardsSearchFormData;

public class JPADownloadedECardsSearchFormDataStatementBuilder extends AbstractJPASearchFormDataStatementBuilder<DownloadedECardsSearchFormData> {

  private final Path<RtRunner> runner;
  private final Path<RtEcard> ecard;
  private final Path<RtPunchSession> punchsession;
  private final Path<RtRace> race;
  private final Path<RtEvent> event;
  private final Path<RtCourse> course;

  public JPADownloadedECardsSearchFormDataStatementBuilder(Path<RtPunchSession> punchsession, Path<RtRunner> runner, Path<RtEcard> ecard, Path<RtRace> race, Path<RtEvent> event, Path<RtCourse> course) {
    this.runner = runner;
    this.ecard = ecard;
    this.punchsession = punchsession;
    this.race = race;
    this.event = event;
    this.course = course;
  }

  @Override
  public void build(DownloadedECardsSearchFormData searchFormData) throws ProcessingException {
    super.build(searchFormData);

    // E-Card Tab
    Expression<String> runnerNameToken = JPACriteriaUtility.runnerNameTokenDefaultsRemoved(runner);
    String runnerNameSearchString = JPACriteriaUtility.removeDefaultTokens(searchFormData.getRunner().getValue());
    addStringWherePart(runnerNameToken, runnerNameSearchString);
    addStringWherePart(ecard.get(RtEcard_.ecardNo), searchFormData.getECard().getValue());
    addDateGreaterThanOrEqualsWherePart(punchsession.get(RtPunchSession_.evtDownload), searchFormData.getDownloadedOnFrom().getValue());
    addDateLessThanOrEqualsWherePart(punchsession.get(RtPunchSession_.evtDownload), searchFormData.getDownloadedOnTo().getValue());
    addLongWherePart(race.get(RtRace_.statusUid), searchFormData.getRaceStatus().getValue());
    addBooleanAsNullWherePart(race.get(RtRace_.id).get(RtRaceKey_.raceNr), searchFormData.getRunnerAssignedGroup().getValue());

    // Event Tab
    addLongOrIsNullWherePart(event.get(RtEvent_.id).get(RtEventKey_.eventNr), searchFormData.getEvent().getValue());
    addLongWherePart(race.get(RtRace_.legClassUid), searchFormData.getClazz().getValue());
    addLongWherePart(course.get(RtCourse_.id).get(RtCourseKey_.courseNr), searchFormData.getCourse().getValue());
  }

  @Override
  protected void buildForeignBuilders(DownloadedECardsSearchFormData searchFormData) throws ProcessingException {
    super.buildForeignBuilders(searchFormData);
  }

}
