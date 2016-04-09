package com.rtiming.server.entry;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;

import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.server.common.AbstractJPASearchFormDataStatementBuilder;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.dao.RtRegistration;
import com.rtiming.shared.dao.RtRegistration_;
import com.rtiming.shared.entry.AbstractRegistrationSearchBoxData;

public class JPARegistrationBoxSearchFormDataStatementBuilder extends AbstractJPASearchFormDataStatementBuilder<AbstractRegistrationSearchBoxData> {

  private final Path<RtRegistration> root;

  public JPARegistrationBoxSearchFormDataStatementBuilder(Path<RtRegistration> root) {
    super();
    this.root = root;
  }

  @Override
  public void build(AbstractRegistrationSearchBoxData searchFormData) throws ProcessingException {
    super.build(searchFormData);
    CriteriaBuilder b = JPA.getCriteriaBuilder();

    // registration no
    addStringWherePart(root.get(RtRegistration_.registrationNo), searchFormData.getNumber().getValue());

    // startlist setting option
    if (searchFormData.getStartlistSettingOptionGroup().getValue() == null) {
      addPredicate(b.isNull(root.get(RtRegistration_.startlistSettingOptionUid)));
    }
    else {
      if (searchFormData.getStartlistSettingOptionGroup().getValue() != 0) {
        addLongWherePart(root.get(RtRegistration_.startlistSettingOptionUid), searchFormData.getStartlistSettingOptionGroup().getValue());
      }
    }

    // registration date
    addDateGreaterThanOrEqualsWherePart(root.get(RtRegistration_.evtRegistration), searchFormData.getEvtRegistrationFrom().getValue());
    addDateLessThanOrEqualsWherePart(root.get(RtRegistration_.evtRegistration), searchFormData.getEvtRegistrationTo().getValue());

  }
}
