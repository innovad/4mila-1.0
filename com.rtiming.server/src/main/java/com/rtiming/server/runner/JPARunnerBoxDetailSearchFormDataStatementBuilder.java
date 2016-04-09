package com.rtiming.server.runner;

import javax.persistence.criteria.Path;

import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.server.common.AbstractJPASearchFormDataStatementBuilder;
import com.rtiming.shared.dao.RtRunner;
import com.rtiming.shared.dao.RtRunner_;
import com.rtiming.shared.runner.AbstractRunnerDetailsSearchBoxData;

public class JPARunnerBoxDetailSearchFormDataStatementBuilder extends AbstractJPASearchFormDataStatementBuilder<AbstractRunnerDetailsSearchBoxData> {

  private final Path<RtRunner> runner;

  public JPARunnerBoxDetailSearchFormDataStatementBuilder(Path<RtRunner> runner) {
    this.runner = runner;
  }

  @Override
  public void build(AbstractRunnerDetailsSearchBoxData searchFormData) throws ProcessingException {
    super.build(searchFormData);
    addLongWherePart(runner.get(RtRunner_.nationUid), searchFormData.getNationUid().getValue());
    addLongWherePart(runner.get(RtRunner_.sexUid), searchFormData.getSex().getValue());
    addDateGreaterThanOrEqualsWherePart(runner.get(RtRunner_.evtBirth), searchFormData.getBirthdateFrom().getValue());
    addDateLessThanOrEqualsWherePart(runner.get(RtRunner_.evtBirth), searchFormData.getBirthdateTo().getValue());
    addLongGreaterThanOrEqualsWherePart(runner.get(RtRunner_.year), searchFormData.getYearFrom().getValue(), false);
    addLongLessThanOrEqualsWherePart(runner.get(RtRunner_.year), searchFormData.getYearTo().getValue(), false);
    addLongWherePart(runner.get(RtRunner_.defaultClassUid), searchFormData.getDefaultClazz().getValue());
  }

}
