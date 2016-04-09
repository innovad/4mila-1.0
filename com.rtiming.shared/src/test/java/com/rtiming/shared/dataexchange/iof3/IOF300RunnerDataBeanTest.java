package com.rtiming.shared.dataexchange.iof3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.rtiming.shared.club.ClubFormData;
import com.rtiming.shared.common.database.sql.RunnerBean;
import com.rtiming.shared.dataexchange.cache.ClubDataCacher;
import com.rtiming.shared.dataexchange.cache.DataCacher;
import com.rtiming.shared.dataexchange.cache.IDataCacher;
import com.rtiming.shared.dataexchange.iof3.IOF300RunnerDataBean.ImportResult;
import com.rtiming.shared.dataexchange.iof3.IOF300Utility.ContactType;
import com.rtiming.shared.dataexchange.iof3.xml.Competitor;
import com.rtiming.shared.dataexchange.iof3.xml.CompetitorList;
import com.rtiming.shared.dataexchange.iof3.xml.Id;
import com.rtiming.shared.dataexchange.iof3.xml.Organisation;
import com.rtiming.shared.dataexchange.iof3.xml.OrganisationList;
import com.rtiming.shared.dataexchange.iof3.xml.OrganisationType;
import com.rtiming.shared.dataexchange.iof3.xml.Person;
import com.rtiming.shared.runner.IRunnerProcessService;
import com.rtiming.shared.runner.RunnerRowData;

/**
 * @author amo
 */
public class IOF300RunnerDataBeanTest {

  @Test(expected = IllegalArgumentException.class)
  public void testNull1() throws Exception {
    IOF300RunnerDataBean bean = createRunnerDataBean();
    bean.matchExistingRunner(null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNull2() throws Exception {
    IOF300RunnerDataBean bean = createRunnerDataBean();
    bean.matchExistingRunner(Mockito.mock(Person.class), null);
  }

  @Test
  public void testMatch1() throws Exception {
    IOF300RunnerDataBean bean = createRunnerDataBean();
    Person person = new Person();
    RunnerBean result = bean.matchExistingRunner(person, createMockService());
    Assert.assertNotNull("Never null", result);
  }

  @Test
  public void testMatch2() throws Exception {
    IOF300RunnerDataBean bean = createRunnerDataBean();
    Person person = new Person();
    person.getId().add(new Id());
    RunnerBean result = bean.matchExistingRunner(person, createMockService());
    Assert.assertNotNull("Never null", result);
  }

  @Test
  public void testMatch3() throws Exception {
    IOF300RunnerDataBean bean = createRunnerDataBean();
    Person person = new Person();
    Id id = new Id();
    person.getId().add(id);
    RunnerBean result = bean.matchExistingRunner(person, createMockService());
    Assert.assertNotNull("Never null", result);
  }

  @Test
  public void testMatchExistingRunner() throws Exception {
    IOF300RunnerDataBean bean = createRunnerDataBean();
    Person person = new Person();
    Id id = new Id();
    id.setValue("CP6MOA");
    person.getId().add(id);
    RunnerBean result = bean.matchExistingRunner(person, createMockService());
    Assert.assertNotNull("Result", result);
    Assert.assertEquals("ExtKey", "CP6MOA", result.getExtKey());
    Assert.assertNotNull("Nr", result.getRunnerNr());
    Assert.assertEquals("Nr", 777, result.getRunnerNr().longValue());
  }

  @Test
  public void testMatchNewRunner() throws Exception {
    IOF300RunnerDataBean bean = createRunnerDataBean();
    Person person = new Person();
    Id id = new Id();
    id.setValue("CP6JON");
    person.getId().add(id);
    RunnerBean result = bean.matchExistingRunner(person, createMockService());
    Assert.assertNotNull("Result", result);
    Assert.assertEquals("ExtKey", "CP6JON", result.getExtKey());
    Assert.assertNull("Nr", result.getRunnerNr());
  }

  @Test
  public void testPersonId() throws Exception {
    Person person = new Person();
    Assert.assertNotNull("Never null", person.getId());
    Assert.assertEquals("Empty", 0, person.getId().size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateXmlObject1() throws Exception {
    IOF300RunnerDataBean bean = createRunnerDataBean();
    bean.createXMLObject(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateXmlObject2() throws Exception {
    IOF300RunnerDataBean bean = createRunnerDataBean();
    bean.createXMLObject(new OrganisationList());
  }

  @Test
  public void testCreateXmlObject3() throws Exception {
    IOF300RunnerDataBean bean = createRunnerDataBean();
    CompetitorList competitorList = new CompetitorList();
    Object result = bean.createXMLObject(competitorList);
    assertEquals(competitorList, result);
  }

  @Test
  public void testCreateXmlObject4() throws Exception {
    RunnerRowData row = new RunnerRowData();
    IOF300RunnerDataBean bean = createRunnerDataBean(1L, row, null, null);
    CompetitorList competitorList = new CompetitorList();
    Object result = bean.createXMLObject(competitorList);
    assertTrue(result instanceof CompetitorList);
    competitorList = (CompetitorList) result;
    assertEquals(competitorList.getCompetitor().size(), 1);
    assertNull(competitorList.getCompetitor().get(0).getPerson().getName().getFamily());
    assertNull(competitorList.getCompetitor().get(0).getPerson().getName().getGiven());
    assertTrue(competitorList.getCompetitor().get(0).getPerson().getContact().isEmpty());
  }

  @Test
  public void testCreateXmlObject5() throws Exception {
    RunnerRowData row = new RunnerRowData();
    row.setFirstName("FirstName");
    row.setLastName("LastName");
    row.setEmail("test@4mila.com");
    row.seteCard("123456");
    row.setNation("SUI");
    row.setClub("thurgorienta");
    row.setDefaultClazz(-6L);
    IOF300RunnerDataBean bean = createRunnerDataBean(1L, row, null, null);
    CompetitorList competitorList = new CompetitorList();
    Object result = bean.createXMLObject(competitorList);
    assertTrue(result instanceof CompetitorList);
    competitorList = (CompetitorList) result;
    assertEquals(competitorList.getCompetitor().size(), 1);
    assertEquals("FirstName", competitorList.getCompetitor().get(0).getPerson().getName().getGiven());
    assertEquals("LastName", competitorList.getCompetitor().get(0).getPerson().getName().getFamily());
    assertEquals(1, competitorList.getCompetitor().get(0).getPerson().getContact().size());
    assertEquals(ContactType.EmailAddress.toString(), competitorList.getCompetitor().get(0).getPerson().getContact().get(0).getType());
    assertEquals(1, competitorList.getCompetitor().get(0).getControlCard().size());
    assertEquals("SUI", competitorList.getCompetitor().get(0).getPerson().getNationality().getCode());
    assertEquals(1, competitorList.getCompetitor().get(0).getOrganisation().size());
    assertEquals(1, competitorList.getCompetitor().get(0).getClazz().size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDoImportInternal1() throws Exception {
    IOF300RunnerDataBean bean = createRunnerDataBean();
    bean.doImportInternal(null, null, null, null);
  }

  @Test
  public void testDoImportInternal2() throws Exception {
    IOF300RunnerDataBean bean = createRunnerDataBean();
    ImportResult result = bean.doImportInternal(createMockService(), new Competitor(), DataCacher.get("test"), "SUI");
    assertNotNull(result);
  }

  @Test
  public void testDoImportInternal3() throws Exception {
    IDataCacher cache = Mockito.mock(IDataCacher.class);
    ClubDataCacher clubCache = Mockito.mock(ClubDataCacher.class);
    ClubFormData clubFormData = new ClubFormData();
    clubFormData.setClubNr(888L);
    Mockito.when(clubCache.doImport("thurgorienta")).thenReturn(clubFormData);
    Mockito.when(cache.getCache(ClubDataCacher.class)).thenReturn(clubCache);

    IOF300RunnerDataBean bean = createRunnerDataBean();
    Competitor competitor = new Competitor();
    Person person = new Person();
    Id id = new Id();
    id.setValue("CP6MOA");
    person.getId().add(id);
    competitor.setPerson(person);
    Organisation club = new Organisation();
    club.setType(OrganisationType.CLUB.toString());
    club.setName("thurgorienta");
    competitor.getOrganisation().add(club);
    ImportResult result = bean.doImportInternal(createMockService(), competitor, cache, "SUI");
    assertNotNull(result);
    assertEquals("Club-Nr", 888, result.getRunner().getClubNr().longValue());
    Assert.assertEquals("CP6MOA", result.getRunner().getExtKey());
  }

  private IOF300RunnerDataBean createRunnerDataBean() {
    return createRunnerDataBean(null, null, null, null);
  }

  private IOF300RunnerDataBean createRunnerDataBean(Long primaryKeyNr, RunnerRowData row, DataCacher cache, String defaultNation) {
    return new IOF300RunnerDataBean(primaryKeyNr, row, cache, defaultNation);
  }

  private IRunnerProcessService createMockService() throws ProcessingException {
    return new TestingRunnerProcessService();
  }
}
