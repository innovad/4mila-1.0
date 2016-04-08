package com.rtiming.server.common.database.jpa;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.eclipse.scout.commons.exception.ProcessingException;

/**
 * Wrapper class for JPA {@link Query}
 */
public class FMilaQuery {

  private final Query query;

  public FMilaQuery(Query query) {
    super();
    this.query = query;
  }

  public int executeUpdate() throws ProcessingException {
    int result = 0;
    try {
      result = query.executeUpdate();
    }
    catch (Exception e) {
      if (e instanceof PersistenceException) {
        JPAUtility.handlePersistenceException((PersistenceException) e);
      }
      else {
        throw e;
      }
    }
    return result;
  }

  public Object getSingleResult() {
    Object singleResult = null;
    try {
      singleResult = query.getSingleResult();
    }
    catch (NoResultException e) {
      return null;
    }
    return singleResult;
  }

  public List getResultList() {
    return query.getResultList();
  }

  public Query setParameter(String string, Object value) {
    return query.setParameter(string, value);
  }

}
