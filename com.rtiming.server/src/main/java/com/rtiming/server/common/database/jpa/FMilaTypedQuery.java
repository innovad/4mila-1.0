package com.rtiming.server.common.database.jpa;

import java.util.List;

import javax.persistence.TypedQuery;

/**
 * Wrapper class for JPA {@link TypedQuery}
 */
public class FMilaTypedQuery<T> extends FMilaQuery {

  public FMilaTypedQuery(TypedQuery<T> query) {
    super(query);
  }

  @Override
  @SuppressWarnings("unchecked")
  public T getSingleResult() {
    return (T) super.getSingleResult();
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<T> getResultList() {
    return super.getResultList();
  }

}
