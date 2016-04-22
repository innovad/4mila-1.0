package com.rtiming.server.common.security;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.FMilaTypedQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.common.security.ILocalAccountService;

import org.junit.Assert;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class LocalAccountServiceTest {

  @Test
  public void testCreateLocalClient() throws Exception {
    BEANS.get(ILocalAccountService.class).createLocalClient(7777L, false);
    Long clientNr = 7777L;
    deleteClient(clientNr);
  }

  @Test
  public void testCreateLocalClientAlreadyExists() throws Exception {
    BEANS.get(ILocalAccountService.class).createLocalClient(7777L, true);
    String queryString = "SELECT MAX(id.clientNr) FROM RtClient WHERE id.clientNr = 7777";
    FMilaTypedQuery<Long> query = JPA.createQuery(queryString, Long.class);
    Long clientNr = query.getSingleResult();
    Assert.assertNull("Client Nr NOT created", clientNr);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateLocalClientNull() throws Exception {
    BEANS.get(ILocalAccountService.class).createLocalClient(null, false);
  }

  private void deleteClient(Long clientNr) throws ProcessingException {
    String queryString = "DELETE FROM RtAccountClient WHERE id.clientNr = :clientNr";
    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("clientNr", clientNr);
    query.executeUpdate();

    queryString = "DELETE FROM RtClient WHERE id.clientNr = :clientNr";
    query = JPA.createQuery(queryString);
    query.setParameter("clientNr", clientNr);
    query.executeUpdate();
  }

}
