package com.rtiming.server.common.database.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.MatrixUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.server.services.lookup.AbstractLookupService;
import org.eclipse.scout.rt.shared.data.basic.FontSpec;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;

public abstract class AbstractJPALookupService extends AbstractLookupService<Long> {

  protected abstract Predicate getKeyWhere(Object key) throws ProcessingException;

  protected abstract Predicate getTextWhere(String text, ILookupCall call) throws ProcessingException;

  protected Predicate getRecWhere(ILookupCall call) throws ProcessingException {
    return JPA.getCriteriaBuilder().conjunction();
  }

  protected abstract List<Object[]> fetchLookupRows(Predicate additionalPredicate, ILookupCall call);

  protected String getConfiguredIconId() {
    return null;
  }

  protected int getConfiguredSortColumn() {
    return 1;
  }

  @Override
  public final List getDataByKey(ILookupCall call) throws ProcessingException {
    List<Object[]> resultList = fetchLookupRows(getKeyWhere(call.getKey()), call);
    return processLookupRows(resultList);
  }

  @Override
  public final List getDataByText(ILookupCall call) throws ProcessingException {
    String text = call.getText();
    text = StringUtility.uppercase(text);
    text = StringUtility.box("%", text, "%");
    text = StringUtility.replace(text, "*", "%");

    Predicate textWhere = getTextWhere(text, call);
    List<Object[]> resultList = fetchLookupRows(textWhere, call);
    return processLookupRows(resultList);
  }

  @Override
  public final List getDataByAll(ILookupCall call) throws ProcessingException {
    List<Object[]> resultList = fetchLookupRows(null, call);
    return processLookupRows(resultList);
  }

  @Override
  public final List getDataByRec(ILookupCall call) throws ProcessingException {
    List<Object[]> resultList = fetchLookupRows(getRecWhere(call), call);
    return processLookupRows(resultList);
  }

  private List processLookupRows(List<Object[]> resultList) {
    Object[][] array = JPAUtility.convertList2Array(resultList);
    if (getConfiguredSortColumn() >= 0) {
      MatrixUtility.sort(array, getConfiguredSortColumn());
    }

    List<LookupRow> result = new ArrayList<>();
    for (Object[] row : array) {
      LookupRow lookupRow = new LookupRow<>(row[0], StringUtility.emptyIfNull(row[1]));
      if (getConfiguredIconId() != null) {
        lookupRow.setIconId(getConfiguredIconId());
      }
      else if (row.length > 2) {
        lookupRow.setIconId(filterEmptyStrings(row[2]));
      }
      if (row.length > 3) {
        lookupRow.setTooltipText(filterEmptyStrings(row[3]));
      }
      if (row.length > 4) {
        lookupRow.setBackgroundColor(filterEmptyStrings(row[4]));
      }
      if (row.length > 5) {
        lookupRow.setForegroundColor(filterEmptyStrings(row[5]));
      }
      if (row.length > 6) {
        lookupRow.setFont(FontSpec.parse(filterEmptyStrings(row[6])));
      }
      if (row.length > 8) {
        lookupRow.setEnabled(true);
        lookupRow.setParentKey(row[8]);
      }
      result.add(lookupRow);
    }

    return result;
  }

  public String filterEmptyStrings(Object object) {
    if (object == null) {
      return null;
    }
    else if (object != null && object instanceof String && StringUtility.isNullOrEmpty((String) object)) {
      return null;
    }
    return StringUtility.emptyIfNull(object);
  }

}
