package com.rtiming.server.common.database.jpa;

import javax.persistence.EntityManager;

import org.eclipse.scout.rt.server.transaction.AbstractTransactionMember;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The scout framework handles transactions osgi based using a {@link ServerJob}.
 * A server job transaction sets {@link ThreadContext#getTransaction()}. This transaction has a greater scope than just
 * jdbc or JTA.
 * It may include any kind of resource, also files, custom object etc. Therefore we register the hibernate session (JTA)
 * as a member of that meta transaction.
 */
public class JPAScoutTransactionMember extends AbstractTransactionMember {
  public static final String TRANSACTION_MEMBER_ID = JPAScoutTransactionMember.class.getName();
  private static final Logger LOG = LoggerFactory.getLogger(JPAScoutTransactionMember.class);

  private final EntityManager m_session;

  public JPAScoutTransactionMember(EntityManager entityManager) {
    super(TRANSACTION_MEMBER_ID);
    m_session = entityManager;
  }

  public EntityManager getSession() {
    return m_session;
  }

  @Override
  public boolean needsCommit() {
    return true;
  }

  @Override
  public boolean commitPhase1() {
    try {
      m_session.flush();
      m_session.getTransaction().commit();
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
    m_session.getTransaction().rollback();
  }

  @Override
  public void release() {
    m_session.close();
  }

  @Override
  public void cancel() {
    Session delegate = m_session.unwrap(Session.class);
    delegate.cancelQuery();
  }
}
