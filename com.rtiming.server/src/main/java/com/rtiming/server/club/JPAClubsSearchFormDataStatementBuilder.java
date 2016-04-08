package com.rtiming.server.club;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.eclipse.scout.commons.exception.ProcessingException;

import com.rtiming.server.common.AbstractJPASearchFormDataStatementBuilder;
import com.rtiming.server.settings.addinfo.JPAAdditionalInformationBoxSearchFormDataStatementBuilder;
import com.rtiming.shared.club.ClubsSearchFormData;
import com.rtiming.shared.common.EntityCodeType;
import com.rtiming.shared.dao.RtClub;
import com.rtiming.shared.dao.RtClubKey_;
import com.rtiming.shared.dao.RtClub_;

public class JPAClubsSearchFormDataStatementBuilder extends AbstractJPASearchFormDataStatementBuilder<ClubsSearchFormData> {

  private final Root<RtClub> root;
  private final CriteriaQuery<Object[]> query;

  public JPAClubsSearchFormDataStatementBuilder(CriteriaQuery<Object[]> query, Root<RtClub> root) {
    super();
    this.root = root;
    this.query = query;
  }

  @Override
  public void build(ClubsSearchFormData searchFormData) throws ProcessingException {
    super.build(searchFormData);

    // Club Search
    JPAClubBoxSearchFormDataStatementBuilder builder = new JPAClubBoxSearchFormDataStatementBuilder(root);
    builder.build(searchFormData.getClubBox());
    addPredicate(builder.getPredicate());

    // Add Info
    JPAAdditionalInformationBoxSearchFormDataStatementBuilder addInfoBuilder = new JPAAdditionalInformationBoxSearchFormDataStatementBuilder(root.get(RtClub_.id).get(RtClubKey_.clubNr), EntityCodeType.ClubCode.ID);
    addInfoBuilder.build(searchFormData.getAdditionalInformationBox());
    addPredicate(addInfoBuilder.getPredicate());
  }

}
