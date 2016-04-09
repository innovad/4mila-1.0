package com.rtiming.server.runner;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;

import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.server.common.AbstractJPASearchFormDataStatementBuilder;
import com.rtiming.server.common.database.jpa.JPACriteriaUtility;
import com.rtiming.shared.dao.RtRunner;
import com.rtiming.shared.dao.RtRunner_;
import com.rtiming.shared.runner.AbstractRunnerSearchBoxData;

public class JPARunnerBoxSearchFormDataStatementBuilder extends AbstractJPASearchFormDataStatementBuilder<AbstractRunnerSearchBoxData> {

  private final Path<RtRunner> runner;

  public JPARunnerBoxSearchFormDataStatementBuilder(Path<RtRunner> runner) {
    this.runner = runner;
  }

  @Override
  public void build(AbstractRunnerSearchBoxData searchFormData) throws ProcessingException {
    super.build(searchFormData);

    addStringWherePart(runner.get(RtRunner_.extKey), searchFormData.getExtKey().getValue());
    Expression<String> runnerNameToken = JPACriteriaUtility.runnerNameTokenDefaultsRemoved(runner);
    String runnerNameSearchString = JPACriteriaUtility.removeDefaultTokens(searchFormData.getName().getValue());
    addStringWherePart(runnerNameToken, runnerNameSearchString);
    addBooleanWherePart(runner.get(RtRunner_.active), searchFormData.getActiveGroup().getValue());
  }

}
