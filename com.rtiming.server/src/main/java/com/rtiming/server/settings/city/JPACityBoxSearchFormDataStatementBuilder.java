package com.rtiming.server.settings.city;

import java.util.Arrays;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.server.common.AbstractJPASearchFormDataStatementBuilder;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.dao.RtCity;
import com.rtiming.shared.dao.RtCity_;
import com.rtiming.shared.settings.city.AbstractCitySearchBoxData;

/**
 * 
 */
public class JPACityBoxSearchFormDataStatementBuilder extends AbstractJPASearchFormDataStatementBuilder<AbstractCitySearchBoxData> {

  private final Path<RtCity> root;

  public JPACityBoxSearchFormDataStatementBuilder(Path<RtCity> root) {
    super();
    this.root = root;
  }

  @Override
  public void build(AbstractCitySearchBoxData searchFormData) throws ProcessingException {
    super.build(searchFormData);
    CriteriaBuilder b = JPA.getCriteriaBuilder();

    // zip
    addStringWherePart(root.get(RtCity_.zip), searchFormData.getZip().getValue());

    // area
    if (searchFormData.getArea().getValue() != null) {
      Predicate p = b.and(root.get(RtCity_.areaUid).in(Arrays.asList(searchFormData.getArea().getValue())));
      addPredicate(p);
    }

    // region
    addStringWherePart(root.get(RtCity_.region), searchFormData.getRegion().getValue());

    // city
    addStringWherePart(root.get(RtCity_.name), searchFormData.getCity().getValue());

    // country
    if (searchFormData.getCountry().getValue() != null) {
      Predicate p = b.and(root.get(RtCity_.countryUid).in(Arrays.asList(searchFormData.getCountry().getValue())));
      addPredicate(p);
    }

  }
}
