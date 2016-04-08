package com.rtiming.server.common.database.jpa;

import java.util.List;

/**
 * 
 */
public class DetachedQuery<T> {

  private final javax.persistence.TypedQuery<T> typedQuery;

  public DetachedQuery(javax.persistence.TypedQuery<T> typedQuery) {
    super();
    this.typedQuery = typedQuery;
  }

  public List<T> getResultList() {
    List<T> resultList = typedQuery.getResultList();
    JPA.currentEntityManager().flush();
    JPA.currentEntityManager().clear();
    return resultList;
  }

}
