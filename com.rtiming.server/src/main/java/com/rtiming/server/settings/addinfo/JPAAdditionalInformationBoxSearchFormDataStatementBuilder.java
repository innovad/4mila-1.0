package com.rtiming.server.settings.addinfo;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;

import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.server.common.AbstractJPASearchFormDataStatementBuilder;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.settings.addinfo.AbstractAdditionalInformationSearchBoxData;

public class JPAAdditionalInformationBoxSearchFormDataStatementBuilder extends AbstractJPASearchFormDataStatementBuilder<AbstractAdditionalInformationSearchBoxData> {

  private final Path<Long> pkColumn;
  private final Long entityUid;

  public JPAAdditionalInformationBoxSearchFormDataStatementBuilder(Path<Long> pkColumn, Long entityUid) {
    super();
    this.pkColumn = pkColumn;
    this.entityUid = entityUid;
  }

  @Override
  public void build(AbstractAdditionalInformationSearchBoxData searchFormData) throws ProcessingException {
    super.build(searchFormData);
    if (searchFormData.getAdditionalInformation().getValue() != null) {
      boolean whereAdded = false;

      // long
      whereAdded |= addWherePart(searchFormData.getAdditionalInformation().getValue(), searchFormData.getIntegerFrom().getValue(), null, searchFormData.getIntegerTo().getValue());

      // double
      whereAdded |= addWherePart(searchFormData.getAdditionalInformation().getValue(), searchFormData.getDecimalFrom().getValue(), null, searchFormData.getDecimalTo().getValue());

      // text
      whereAdded |= addWherePart(searchFormData.getAdditionalInformation().getValue(), null, searchFormData.getText().getValue(), null);

      // boolean
      whereAdded |= addWherePart(searchFormData.getAdditionalInformation().getValue(), null, searchFormData.getBoolean().getValue(), null);

      // smartfield
      whereAdded |= addWherePart(searchFormData.getAdditionalInformation().getValue(), null, searchFormData.getSmartfield().getValue(), null);

      if (!whereAdded) {
        CriteriaBuilder b = JPA.getCriteriaBuilder();
        addPredicate(b.isNotNull(pkColumn));
      }
    }
  }

  private boolean addWherePart(Long addInfoUid, Object from, Object equal, Object to) throws ProcessingException {
    if (from != null || equal != null || to != null) {
      Long[] pks = AdditionalInformationDatabaseUtility.getMatchingPrimaryKeys(addInfoUid, entityUid, from, equal, to);
      addLongWherePart(pkColumn, pks);
      return true;
    }
    return false;
  }

}
