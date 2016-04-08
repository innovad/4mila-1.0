package com.rtiming.server.club;

import javax.persistence.criteria.Path;

import org.eclipse.scout.commons.exception.ProcessingException;

import com.rtiming.server.common.AbstractJPASearchFormDataStatementBuilder;
import com.rtiming.shared.club.AbstractClubSearchBoxData;
import com.rtiming.shared.dao.RtClub;
import com.rtiming.shared.dao.RtClubKey_;
import com.rtiming.shared.dao.RtClub_;

public class JPAClubBoxSearchFormDataStatementBuilder extends AbstractJPASearchFormDataStatementBuilder<AbstractClubSearchBoxData> {

  private final Path<RtClub> club;

  public JPAClubBoxSearchFormDataStatementBuilder(Path<RtClub> club) {
    super();
    this.club = club;
  }

  @Override
  public void build(AbstractClubSearchBoxData searchFormData) throws ProcessingException {
    super.build(searchFormData);
    addLongWherePart(club.get(RtClub_.id).get(RtClubKey_.clubNr), searchFormData.getClub().getValue());
    addLongWherePart(club.get(RtClub_.contactRunnerNr), searchFormData.getContactPerson().getValue());
    addStringWherePart(club.get(RtClub_.name), searchFormData.getName().getValue());
    addStringWherePart(club.get(RtClub_.shortcut), searchFormData.getShortcut().getValue());
  }

}
