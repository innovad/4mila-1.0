package com.rtiming.client.test.data;

import java.util.Date;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.event.course.ControlTypeCodeType;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class TestDataTest {

  @Test
  public void testCodeTestDataProvider() throws Exception {
    CodeTestDataProvider provider = new CodeTestDataProvider(ClassCodeType.ID);
    Assert.assertNotNull(provider.getCodeUid());
    provider.remove();
  }

  @Test
  public void testStartblockTestDataProvider() throws Exception {
    StartblockTestDataProvider provider = new StartblockTestDataProvider();
    Assert.assertNotNull(provider.getStartblockUid());
    provider.remove();
  }

  @Test
  public void testRegistrationTestDataProvider() throws Exception {
    RegistrationTestDataProvider provider = new RegistrationTestDataProvider();
    Assert.assertNotNull(provider.getRegistrationNr());
    provider.remove();
  }

  @Test
  public void testFeeGroupTestDataProvider() throws Exception {
    FeeGroupTestDataProvider provider = new FeeGroupTestDataProvider();
    Assert.assertNotNull(provider.getFeeGroupNr());
    provider.remove();
  }

  @Test
  public void testCityTestDataProvider() throws Exception {
    CityTestDataProvider city = new CityTestDataProvider();
    Assert.assertNotNull(city.getCityNr());
    city.remove();
  }

  @Test
  public void testEntryTestDataProvider() throws ProcessingException {
    EventWithIndividualClassTestDataProvider event = new EventWithIndividualClassTestDataProvider();
    EntryTestDataProvider provider = new EntryTestDataProvider(event.getEventNr(), event.getClassUid());
    Assert.assertNotNull(provider.getECardNr());
    Assert.assertNotNull(provider.getEntryNr());
    Assert.assertNotNull(provider.getRaceNr());
    Assert.assertNotNull(provider.getRunnerNr());
    event.remove();
    provider.remove();
  }

  @Test
  public void testEventTestDataProvider() throws ProcessingException {
    EventTestDataProvider provider = new EventTestDataProvider();
    Assert.assertNotNull(provider.getEventNr());
    provider.remove();
  }

  @Test
  public void testEventWithIndividualClassTestDataProvider() throws ProcessingException {
    EventWithIndividualClassTestDataProvider provider = new EventWithIndividualClassTestDataProvider();
    Assert.assertNotNull(provider.getClassUid());
    Assert.assertNotNull(provider.getCourseNr());
    Assert.assertNotNull(provider.getEventNr());
    provider.remove();
  }

  @Test
  public void testEventWithRelayValidatedRaceTestDataProvider() throws Exception {
    String[][] controlNos = new String[][]{new String[]{"31"}, new String[]{"32"}};
    String[][] punchNos = new String[][]{new String[]{"31"}, new String[]{"32"}};
    Integer[] start = new Integer[]{1000, 2000};
    Integer[] finish = new Integer[]{2000, 3000};
    Integer[][] legTimes = new Integer[][]{new Integer[]{1}, new Integer[]{2}};
    EventWithRelayValidatedRaceTestDataProvider provider = new EventWithRelayValidatedRaceTestDataProvider(controlNos, punchNos, start, finish, legTimes);
    Assert.assertEquals("2 legs", 2, provider.getLegUids().length);
    provider.remove();
  }

  @Test
  public void testRunnerTestDataProvider() throws ProcessingException {
    RunnerTestDataProvider provider = new RunnerTestDataProvider();
    Assert.assertNotNull(provider.getRunnerNr());
    provider.remove();
  }

  @Test
  public void testECardStationTestDataProvider() throws ProcessingException {
    ECardStationTestDataProvider provider = new ECardStationTestDataProvider();
    Assert.assertNotNull(provider.getECardStationNr());
    provider.remove();
  }

  @Test
  public void testCountryTestDataProvider() throws ProcessingException {
    CountryTestDataProvider provider = new CountryTestDataProvider();
    Assert.assertNotNull(provider.getCountryUid());
    provider.remove();
  }

  @Test
  public void testCourseControlTestDataProvider() throws ProcessingException {
    EventWithIndividualClassTestDataProvider event = new EventWithIndividualClassTestDataProvider();
    CourseControlTestDataProvider provider = new CourseControlTestDataProvider(event.getEventNr(), event.getCourseNr(), ControlTypeCodeType.ControlCode.ID, 88L, "88");
    Assert.assertNotNull(provider.getControlNo());
    provider.remove();
    event.remove();
  }

  @Test
  public void testDownloadedECardTestDataProvider() throws ProcessingException {
    EventTestDataProvider event = new EventTestDataProvider();
    ECardStationTestDataProvider station = new ECardStationTestDataProvider();
    ECardTestDataProvider eCard = new ECardTestDataProvider();
    DownloadedECardTestDataProvider download = new DownloadedECardTestDataProvider(event.getEventNr(), null, eCard.getECardNr(), station.getECardStationNr());
    download.remove();
    eCard.remove();
    station.remove();
    event.remove();
  }

  @Test
  public void testPunchTestDataProvider() throws ProcessingException {
    EventTestDataProvider event = new EventTestDataProvider();
    ECardStationTestDataProvider station = new ECardStationTestDataProvider();
    ECardTestDataProvider eCard = new ECardTestDataProvider();
    DownloadedECardTestDataProvider download = new DownloadedECardTestDataProvider(event.getEventNr(), null, eCard.getECardNr(), station.getECardStationNr());
    PunchTestDataProvider provider = new PunchTestDataProvider(download.getForm().getPunchSessionNr(), event.getEventNr(), new Date(), "44", 42L);
    provider.remove();
    download.remove();
    eCard.remove();
    station.remove();
    event.remove();
  }

  @Test
  public void testECardTestDataProvider() throws ProcessingException {
    ECardTestDataProvider provider = new ECardTestDataProvider();
    Assert.assertNotNull(provider.getECardNr());
    provider.remove();
  }

  @Test
  public void testCurrencyTestDataProvider() throws ProcessingException {
    CurrencyTestDataProvider provider = new CurrencyTestDataProvider();
    Assert.assertNotNull(provider.getCurrencyUid());
    provider.remove();
  }

  @Test
  public void testMapTestDataProvider() throws ProcessingException {
    MapTestDataProvider provider = new MapTestDataProvider();
    Assert.assertNotNull(provider.getMapNr());
    provider.remove();
  }

  @Test
  public void testRankingTestDataProvider() throws Exception {
    RankingTestDataProvider provider = new RankingTestDataProvider();
    Assert.assertNotNull(provider.getRankingNr());
    provider.remove();
  }

}
