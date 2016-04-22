package com.rtiming.server.settings.account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.dao.RtAccount;
import com.rtiming.shared.dao.RtAccountClient;
import com.rtiming.shared.dao.RtAccountClientKey;
import com.rtiming.shared.dao.RtClient;
import com.rtiming.shared.settings.account.AccountFormData;
import com.rtiming.shared.settings.account.IAccountProcessService;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class AccountProcessServiceTest {

  @Test
  public void testCreate() throws Exception {
    AccountFormData formData = new AccountFormData();
    formData.getEMail().setValue("TEST");
    formData.getUsername().setValue("TEST");
    BEANS.get(IAccountProcessService.class).create(formData, false);
    deleteAccount(formData.getAccountNr());
    Assert.assertNull("No clientNr should be created", formData.getClientNr());
  }

  @Test
  public void testCreateWithClient() throws Exception {
    AccountFormData formData = new AccountFormData();
    formData.getEMail().setValue("TEST");
    formData.getUsername().setValue("TEST");
    BEANS.get(IAccountProcessService.class).create(formData, true);
    deleteAccount(formData.getAccountNr());
    deleteClient(formData.getClientNr());
    Assert.assertNotNull("ClientNr should be created", formData.getClientNr());
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

  private void deleteAccount(Long accountNr) throws ProcessingException {
    String queryString = "DELETE FROM RtAccount WHERE id.accountNr = :accountNr";
    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("accountNr", accountNr);
    query.executeUpdate();
  }

  @Test
  public void testLoadClientNr() throws Exception {
    AccountProcessService svc = new AccountProcessService();
    Long clientNr = svc.loadClientNr();
    assertEquals("same", ServerSession.get().getSessionClientNr(), clientNr);
  }

  @Test
  public void testLogin() throws Exception {
    AccountProcessService svc = new AccountProcessService();
    AccountFormData formData = new AccountFormData();
    svc.login(formData, false);
  }

  @Test
  public void testFind() throws Exception {
    AccountProcessService svc = new AccountProcessService();
    svc.find("abc");
  }

  @Test
  public void testStore1() throws Exception {
    AccountProcessService svc = new AccountProcessService();
    AccountFormData formData = new AccountFormData();
    formData.setAccountNr(1L);
    formData = svc.load(formData);
    formData.getLastName().setValue("Latscha");
    svc.store(formData);

    RtAccount account = JPA.find(RtAccount.class, 1L);
    assertEquals("updated", "Latscha", account.getLastName());
    account.setLastName("Moser");
    JPA.persist(account);
  }

  @Test
  public void testCheckUniqueEmailUsername1() throws Exception {
    AccountProcessService svc = new AccountProcessService();
    boolean result = svc.checkUniqueEmailUsername(null, null);
    assertFalse("not exists", result);
  }

  @Test
  public void testCheckUniqueEmailUsername2() throws Exception {
    AccountProcessService svc = new AccountProcessService();
    boolean result = svc.checkUniqueEmailUsername("ADMIN", null);
    assertTrue("exists", result);
  }

  @Test
  public void testCheckUniqueEmailUsername3() throws Exception {
    AccountProcessService svc = new AccountProcessService();
    boolean result = svc.checkUniqueEmailUsername(null, "adi.moser@gmail.COM");
    assertTrue("exists", result);
  }

  @Test
  public void testCheckUniqueEmailUsername4() throws Exception {
    AccountProcessService svc = new AccountProcessService();
    boolean result = svc.checkUniqueEmailUsername("admiN", "adi.moser@gmail.COM");
    assertTrue("exists", result);
  }

  @Test
  public void testCheckUniqueEmailUsername5() throws Exception {
    AccountProcessService svc = new AccountProcessService();
    boolean result = svc.checkUniqueEmailUsername("admin2", "adi2.moser@gmail.com");
    assertFalse("not exists", result);
  }

  @Test
  public void testCreateNewClient1() throws Exception {
    AccountProcessService svc = new AccountProcessService();
    Long newClientNr = svc.createNewClient(1L);

    RtAccountClientKey accountClientKey = new RtAccountClientKey();
    accountClientKey.setAccountNr(1L);
    accountClientKey.setClientNr(newClientNr);
    RtAccountClient findAccountClient = JPA.find(RtAccountClient.class, accountClientKey);
    assertNotNull("created", findAccountClient);
    JPA.remove(findAccountClient);

    RtClient client = JPA.find(RtClient.class, newClientNr);
    assertNotNull("created", client);
    JPA.remove(client);
  }

}
