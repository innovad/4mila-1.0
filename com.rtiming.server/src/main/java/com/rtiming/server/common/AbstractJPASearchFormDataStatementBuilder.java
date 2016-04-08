package com.rtiming.server.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.date.DateUtility;

import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.FMilaUtility;

/**
 * 
 */
public abstract class AbstractJPASearchFormDataStatementBuilder<T> {

  private Predicate predicate;
  private final CriteriaBuilder builder;

  public AbstractJPASearchFormDataStatementBuilder() {
    builder = JPA.getCriteriaBuilder();
    predicate = builder.conjunction();
  }

  public void build(T searchFormData) throws ProcessingException {
    buildForeignBuilders(searchFormData);
  }

  protected void buildForeignBuilders(T searchFormData) throws ProcessingException {
  }

  public void addPredicate(Predicate p) {
    predicate = builder.and(predicate, p);
  }

  public Predicate getPredicate() {
    return predicate;
  }

  protected void addForeignBuilder(AbstractJPASearchFormDataStatementBuilder foreignBuilder, Predicate entityExists) {
    if (foreignBuilder.getPredicate() != null && foreignBuilder.getPredicate().getExpressions().size() > 0) {
      addPredicate(entityExists);
    }
  }

  protected void addStringWherePart(Expression<String> expression, String item) {
    if (!StringUtility.isNullOrEmpty(item)) {
      Predicate p = builder.like(builder.lower(expression), StringUtility.lowercase("%" + item + "%"));
      addPredicate(p);
    }
  }

  protected void addDateGreaterThanOrEqualsWherePart(Expression<Date> expression, Date date) {
    if (date != null) {
      Predicate p = builder.and(builder.greaterThanOrEqualTo(expression, DateUtility.truncDate(date)));
      addPredicate(p);
    }
  }

  protected void addDateLessThanOrEqualsWherePart(Expression<Date> expression, Date date) {
    if (date != null) {
      Date truncDate = FMilaUtility.addSeconds(DateUtility.truncDate(date), 86399);
      Predicate p = builder.and(builder.lessThanOrEqualTo(expression, truncDate));
      addPredicate(p);
    }
  }

  protected void addLongGreaterThanOrEqualsWherePart(Expression<Long> expression, Long value, boolean onlyIfNotNull) {
    if (value != null) {
      Predicate p = builder.and(builder.greaterThanOrEqualTo(expression, value));
      if (onlyIfNotNull) {
        p = builder.or(p, builder.isNull(expression));
      }
      addPredicate(p);
    }
  }

  protected void addLongLessThanOrEqualsWherePart(Expression<Long> expression, Long value, boolean onlyIfNotNull) {
    if (value != null) {
      Predicate p = builder.and(builder.lessThanOrEqualTo(expression, value));
      if (onlyIfNotNull) {
        p = builder.or(p, builder.isNull(expression));
      }
      addPredicate(p);
    }
  }

  protected void addDoubleGreaterThanOrEqualsWherePart(Expression<Double> expression, Double value) {
    if (value != null) {
      Predicate p = builder.and(builder.ge(expression, value));
      addPredicate(p);
    }
  }

  protected void addDoubleLessThanOrEqualsWherePart(Expression<Double> expression, Double value) {
    if (value != null) {
      Predicate p = builder.and(builder.le(expression, value));
      addPredicate(p);
    }
  }

  protected void addBooleanAsLongWherePart(Expression<Long> expression, Boolean value) {
    if (value != null) {
      Predicate p = builder.and(builder.equal(expression, value == true ? 1 : 0));
      addPredicate(p);
    }
  }

  protected void addBooleanAsNullWherePart(Expression<?> expression, Boolean value) {
    if (value != null) {
      Predicate p = value ? builder.and(builder.isNotNull(expression)) : builder.and(builder.isNull(expression));
      addPredicate(p);
    }
  }

  protected void addBooleanWherePart(Expression<Boolean> expression, Boolean value) {
    if (value != null) {
      Predicate p = builder.and(builder.equal(expression, value));
      addPredicate(p);
    }
  }

  protected void addLongWherePart(Expression<Long> expression, Long... value) {
    if (value == null) {
      return;
    }
    List<Long> list = new ArrayList<Long>(Arrays.asList(value));
    list.remove(null);
    if (list.size() > 0) {
      Predicate p = builder.and(expression.in(Arrays.asList(value)));
      addPredicate(p);
    }
  }

  protected void addLongOrIsNullWherePart(Expression<Long> expression, Long... value) {
    if (value == null) {
      return;
    }
    List<Long> list = new ArrayList<Long>(Arrays.asList(value));
    list.remove(null);
    if (list.size() > 0) {
      Predicate p = builder.and(builder.or(builder.isNull(expression), expression.in(Arrays.asList(value))));
      addPredicate(p);
    }
  }

}
