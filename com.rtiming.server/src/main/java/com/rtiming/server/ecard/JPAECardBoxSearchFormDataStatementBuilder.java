package com.rtiming.server.ecard;

import java.util.Arrays;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.server.common.AbstractJPASearchFormDataStatementBuilder;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.dao.RtEcard;
import com.rtiming.shared.dao.RtEcard_;
import com.rtiming.shared.ecard.AbstractECardSearchBoxData;

/**
 * 
 */
public class JPAECardBoxSearchFormDataStatementBuilder extends AbstractJPASearchFormDataStatementBuilder<AbstractECardSearchBoxData> {

  private final Path<RtEcard> root;

  public JPAECardBoxSearchFormDataStatementBuilder(Path<RtEcard> root) {
    super();
    this.root = root;
  }

  @Override
  public void build(AbstractECardSearchBoxData searchFormData) throws ProcessingException {
    super.build(searchFormData);
    CriteriaBuilder b = JPA.getCriteriaBuilder();

    // ecard no
    addStringWherePart(root.get(RtEcard_.ecardNo), searchFormData.getNumber().getValue());

    // rental card
    if (searchFormData.getRentalCardGroup().getValue() != null) {
      if (searchFormData.getRentalCardGroup().getValue()) {
        Predicate p = b.isTrue(root.get(RtEcard_.rentalCard));
        addPredicate(p);
      }
      else {
        Predicate p = b.isFalse(b.coalesce(root.get(RtEcard_.rentalCard), false));
        addPredicate(p);
      }
    }

    // ecard type
    if (searchFormData.getECardType().getValue() != null) {
      Predicate p = b.and(root.get(RtEcard_.typeUid).in(Arrays.asList(searchFormData.getECardType().getValue())));
      addPredicate(p);
    }

  }
}
