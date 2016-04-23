package com.rtiming.server.common.database.jpa;

import javax.persistence.EntityManager;

import org.eclipse.scout.rt.server.transaction.AbstractTransactionMember;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The scout framework handles transactions using a {@link ServerJob}.
 * A server job transaction sets {@link ThreadContext#getTransaction()}. This transaction has a greater scope than just
 * jdbc or JTA.
 * It may include any kind of resource, also files, custom object etc. Therefore we register the hibernate session (JTA)
 * as a member of that meta transaction.
 */
public class JPAScoutTransactionMember extends AbstractTransactionMember {
  public static final String TRANSACTION_MEMBER_ID = JPAScoutTransactionMember.class.getName();
  private static final Logger LOG = LoggerFactory.getLogger(JPAScoutTransactionMember.class);

  private final EntityManager entityManager;

  public JPAScoutTransactionMember(EntityManager entityManager) {
    super(TRANSACTION_MEMBER_ID);
    this.entityManager = entityManager;
  }

  public EntityManager getEntityManager() {
    return entityManager;
  }

  @Override
  public boolean needsCommit() {
    return true;
  }

  @Override
  public boolean commitPhase1() {
    try {
      entityManager.flush();
      entityManager.getTransaction().commit();
      return true;
    }
    catch (HibernateException e) {
      LOG.error("hibernate commit", e);
      return false;
    }
  }

  @Override
  public void commitPhase2() {
  }

  @Override
  public void rollback() {
    entityManager.getTransaction().rollback();
  }

  @Override
  public void release() {
    entityManager.close();
  }

  @Override
  public void cancel() {
    Session delegate = entityManager.unwrap(Session.class);
    delegate.cancelQuery();
  }
}
