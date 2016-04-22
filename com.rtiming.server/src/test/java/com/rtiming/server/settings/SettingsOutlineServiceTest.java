package com.rtiming.server.settings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.dao.RtAccountClient;
import com.rtiming.shared.dao.RtAccountClientKey;
import com.rtiming.shared.dao.RtUc;
import com.rtiming.shared.dao.RtUcKey;
import com.rtiming.shared.dao.RtUser;
import com.rtiming.shared.dao.RtUserKey;
import com.rtiming.shared.entry.startblock.StartblockCodeType;
import com.rtiming.shared.settings.IDefaultProcessService;
import com.rtiming.shared.settings.city.AreaCodeType;
import com.rtiming.shared.settings.city.CountryCodeType;
import com.rtiming.shared.settings.currency.CurrencyCodeType;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class SettingsOutlineServiceTest {

  @Test
  public void testGetFeeTableData() throws Exception {
    SettingsOutlineService svc = new SettingsOutlineService();
    svc.getFeeTableData(null);
  }

  @Test
  public void create() throws Exception {
    RtUc uc = new RtUc();
    uc.setId(RtUcKey.create((Long) null));
    uc.setActive(true);
    uc.setCodeType(StartblockCodeType.ID);
    JPA.merge(uc);

    SettingsOutlineService svc = new SettingsOutlineService();
    Object[][] result = svc.getStartblockTableData();
    Assert.assertTrue("Min 1 row", result.length > 0);

    JPA.remove(uc);
  }

  @Test
  public void testGetUserTableData() throws Exception {
    RtUser user = new RtUser();
    user.setId(RtUserKey.create(user.getId()));
    JPA.merge(user);

    SettingsOutlineService svc = new SettingsOutlineService();
    Object[][] result = svc.getUserTableData();
    Assert.assertTrue("Min 1 row", result.length > 0);

    JPA.remove(user);
  }

  @Test
  public void testGetCountryTableData() throws Exception {
    Long defaultCountryUid = BEANS.get(IDefaultProcessService.class).getDefaultCountryUid();

    SettingsOutlineService svc = new SettingsOutlineService();
    Object[][] data = svc.getCountryTableData();

    for (Object[] row : data) {
      Assert.assertEquals("Default Country", row[1], defaultCountryUid);
    }
  }

  @Test
  public void testGetFeeGroupTableData() throws Exception {
    SettingsOutlineService svc = new SettingsOutlineService();
    svc.getFeeGroupTableData();
  }

  @Test
  public void testGetAreaTableData() throws Exception {
    SettingsOutlineService svc = new SettingsOutlineService();
    svc.getAreaTableData();
  }

  @Test
  public void testCurrencyTableData() throws Exception {
    Long defaultCountryUid = BEANS.get(IDefaultProcessService.class).getDefaultCurrencyUid();

    SettingsOutlineService svc = new SettingsOutlineService();
    Object[][] data = svc.getCurrencyTableData();

    for (Object[] row : data) {
      Assert.assertEquals("Default Currency", row[1], defaultCountryUid);
    }

  }

  @Test
  public void testGetAccountClientTableData1() throws Exception {
    SettingsOutlineService svc = new SettingsOutlineService();
    Object[][] data = svc.getAccountClientTableData(1L);
    assertTrue(data.length > 0);
    boolean found = false;
    for (Object[] row : data) {
      if (CompareUtility.equals(row[0], 1L) && StringUtility.equalsIgnoreCase(StringUtility.emptyIfNull(row[1]), "Global")) {
        found = true;
      }
    }
    assertTrue("found", found);
  }

  @Test
  public void testLoadCodes1() throws Exception {
    SettingsOutlineService svc = new SettingsOutlineService();
    svc.loadCodes(new AreaCodeType());
  }

  @Test
  public void testLoadCodes2() throws Exception {
    SettingsOutlineService svc = new SettingsOutlineService();
    svc.loadCodes(new CountryCodeType());
  }

  @Test
  public void testLoadCodes3() throws Exception {
    SettingsOutlineService svc = new SettingsOutlineService();
    svc.loadCodes(new CurrencyCodeType());
  }

  @Test
  public void testGetClassTableData1() throws Exception {
    SettingsOutlineService svc = new SettingsOutlineService();
    svc.getClassTableData();
  }

  @Test
  public void testGetAdditionalInformationAdministrationTableData1() throws Exception {
    SettingsOutlineService svc = new SettingsOutlineService();
    svc.getAccountClientTableData(null);
  }

  @Test
  public void testGetAdditionalInformationAdministrationTableData2() throws Exception {
    SettingsOutlineService svc = new SettingsOutlineService();
    svc.getAccountClientTableData(-1L);
  }

  @Test
  public void testGetAccountTableData() throws Exception {
    RtAccountClient ac = new RtAccountClient();
    RtAccountClientKey id = new RtAccountClientKey();
    id.setAccountNr(1L);
    id.setClientNr(1L);
    ac.setId(id);
    JPA.persist(ac);

    SettingsOutlineService svc = new SettingsOutlineService();
    Object[][] result = svc.getAccountTableData();
    assertEquals("1 row on Test", 1, result.length);
    assertEquals("Client Nr", "1", result[0][6]);

    JPA.remove(ac);
  }

}
