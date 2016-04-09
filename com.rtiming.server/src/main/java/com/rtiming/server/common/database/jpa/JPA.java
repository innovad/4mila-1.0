package com.rtiming.server.common.database.jpa;

import java.sql.Connection;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.server.transaction.ITransaction;
import org.hibernate.internal.SessionImpl;

public class JPA {

  // private static IHibernateWithOsgiFactory<EntityManagerFactory> sessionFactoryBuilder;

//  private static IHibernateWithOsgiFactory<EntityManagerFactory> getSessionFactoryBuilder() {
//    if (sessionFactoryBuilder == null) {
//      JPASetup setup = JPASetup.get();
//      setup.initDatabase();
//      sessionFactoryBuilder = setup.createSessionFactoryBuilder();
//    }
//    return sessionFactoryBuilder;
//  }

  /**
   * @return the JPA {@link EntityManager} session
   */
  public static EntityManager currentEntityManager() {
    ITransaction t = ITransaction.CURRENT.get();
    if (t == null) {
      throw new IllegalStateException("not inside a scout transaction (ServerJob.schedule)");
    }
    JPAScoutTransactionMember m = (JPAScoutTransactionMember) t.getMember(JPAScoutTransactionMember.TRANSACTION_MEMBER_ID);
    if (m == null) {
      try {

        EntityManager session = Persistence.createEntityManagerFactory("4mila").createEntityManager();
        m = new JPAScoutTransactionMember(session);
        t.registerMember(m);
        session.getTransaction().begin();
      }
      catch (ProcessingException e) {
        throw new IllegalStateException("scout transaction is not accepting new members", e);
      }
    }
    return m.getSession();
  }

  public static Connection getConnection() throws ProcessingException {
    SessionImpl hibernateSession = JPA.currentEntityManager().unwrap(SessionImpl.class);
    Connection connection = hibernateSession.connection();
    return connection;
  }

  public static <T> T find(Class<T> entityClass, Object primaryKey) {
    currentEntityManager().flush();
    currentEntityManager().clear();
    T result = currentEntityManager().find(entityClass, primaryKey);
    return result;
  }

  public static void merge(Object entity) throws ProcessingException {
    try {
      entity = currentEntityManager().merge(entity);
      currentEntityManager().flush();
      currentEntityManager().clear();
    }
    catch (Exception e) {
      if (e instanceof PersistenceException) {
        JPAUtility.handlePersistenceException((PersistenceException) e);
      }
      else {
        throw e;
      }
    }
  }

  public static void persist(Object entity) throws ProcessingException {
    try {
      currentEntityManager().persist(entity);
      currentEntityManager().flush();
      currentEntityManager().clear();
    }
    catch (Exception e) {
      if (e instanceof PersistenceException) {
        JPAUtility.handlePersistenceException((PersistenceException) e);
      }
      else {
        throw e;
      }
    }

  }

  public static void remove(Object entity) throws ProcessingException {
    if (entity == null) {
      return;
    }
    try {
      if (!currentEntityManager().contains(entity)) {
        entity = currentEntityManager().merge(entity);
      }
      currentEntityManager().remove(entity);
      currentEntityManager().flush();
      currentEntityManager().clear();
    }
    catch (Exception e) {
      if (e instanceof PersistenceException) {
        JPAUtility.handlePersistenceException((PersistenceException) e);
      }
      else {
        throw e;
      }
    }
  }

  public static CriteriaBuilder getCriteriaBuilder() {
    return currentEntityManager().getCriteriaBuilder();
  }

  public static <T> DetachedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
    javax.persistence.TypedQuery<T> typedQuery = currentEntityManager().createQuery(criteriaQuery);
    DetachedQuery<T> query = new DetachedQuery<T>(typedQuery);
    return query;
  }

  public static FMilaQuery createQuery(String queryString) {
    return new FMilaQuery(currentEntityManager().createQuery(queryString));
  }

  @SuppressWarnings("unchecked")
  public static <T> FMilaTypedQuery<T> createQuery(String queryString, Class<T> type) {
    return new FMilaTypedQuery(currentEntityManager().createQuery(queryString, type));
  }

  public static void commit() {
    JPA.currentEntityManager().getTransaction().commit();
    JPA.currentEntityManager().getTransaction().begin();
  }

}
