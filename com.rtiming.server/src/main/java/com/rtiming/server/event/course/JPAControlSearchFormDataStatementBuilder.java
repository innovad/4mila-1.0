package com.rtiming.server.event.course;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.eclipse.scout.commons.exception.ProcessingException;

import com.rtiming.server.common.AbstractJPASearchFormDataStatementBuilder;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.dao.RtControl;
import com.rtiming.shared.dao.RtControl_;
import com.rtiming.shared.event.course.ControlsSearchFormData;

/**
 * 
 */
public class JPAControlSearchFormDataStatementBuilder extends AbstractJPASearchFormDataStatementBuilder<ControlsSearchFormData> {

  private final Root<RtControl> root;

  public JPAControlSearchFormDataStatementBuilder(Root<RtControl> root) {
    super();
    this.root = root;
  }

  @Override
  public void build(ControlsSearchFormData searchFormData) throws ProcessingException {
    super.build(searchFormData);
    CriteriaBuilder b = JPA.getCriteriaBuilder();

    // active
    if (searchFormData.getActiveGroup().getValue() != null) {
      if (searchFormData.getActiveGroup().getValue()) {
        Predicate p = b.isTrue(root.get(RtControl_.active));
        addPredicate(p);
      }
      else {
        Predicate p = b.isFalse(b.coalesce(root.get(RtControl_.active), false));
        addPredicate(p);
      }
    }

  }
}
