package com.rtiming.server.common.database.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.MatrixUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.platform.util.TypeCastUtility;
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
  public final List<LookupRow<Long>> getDataByKey(ILookupCall call) throws ProcessingException {
    List<Object[]> resultList = fetchLookupRows(getKeyWhere(call.getKey()), call);
    return processLookupRows(resultList);
  }

  @Override
  public final List<LookupRow<Long>> getDataByText(ILookupCall call) throws ProcessingException {
    String text = call.getText();
    text = StringUtility.uppercase(text);
    text = StringUtility.box("%", text, "%");
    text = StringUtility.replace(text, "*", "%");

    Predicate textWhere = getTextWhere(text, call);
    List<Object[]> resultList = fetchLookupRows(textWhere, call);
    return processLookupRows(resultList);
  }

  @Override
  public final List<LookupRow<Long>> getDataByAll(ILookupCall call) throws ProcessingException {
    List<Object[]> resultList = fetchLookupRows(null, call);
    return processLookupRows(resultList);
  }

  @Override
  public final List<LookupRow<Long>> getDataByRec(ILookupCall call) throws ProcessingException {
    List<Object[]> resultList = fetchLookupRows(getRecWhere(call), call);
    return processLookupRows(resultList);
  }

  private List<LookupRow<Long>> processLookupRows(List<Object[]> resultList) {
    Object[][] array = JPAUtility.convertList2Array(resultList);
    if (getConfiguredSortColumn() >= 0) {
      MatrixUtility.sort(array, getConfiguredSortColumn());
    }

    List<LookupRow<Long>> result = new ArrayList<>();
    for (Object[] row : array) {
      LookupRow<Long> lookupRow = new LookupRow<Long>(TypeCastUtility.castValue(row[0], Long.class), StringUtility.emptyIfNull(row[1]));
      if (getConfiguredIconId() != null) {
        lookupRow.withIconId(getConfiguredIconId());
      }
      else if (row.length > 2) {
        lookupRow.withIconId(filterEmptyStrings(row[2]));
      }
      if (row.length > 3) {
        lookupRow.withTooltipText(filterEmptyStrings(row[3]));
      }
      if (row.length > 4) {
        lookupRow.withBackgroundColor(filterEmptyStrings(row[4]));
      }
      if (row.length > 5) {
        lookupRow.withForegroundColor(filterEmptyStrings(row[5]));
      }
      if (row.length > 6) {
        lookupRow.withFont(FontSpec.parse(filterEmptyStrings(row[6])));
      }
      if (row.length > 8) {
        lookupRow.withEnabled(true);
        lookupRow.withParentKey(TypeCastUtility.castValue(row[8], Long.class));
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
