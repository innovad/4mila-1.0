package com.rtiming.shared.dataexchange.iof3;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.club.ClubFormData;
import com.rtiming.shared.common.database.sql.RunnerBean;
import com.rtiming.shared.dao.RtEcard;
import com.rtiming.shared.dataexchange.AbstractDataBean;
import com.rtiming.shared.dataexchange.AbstractXMLDataBean;
import com.rtiming.shared.dataexchange.IDataExchangeService;
import com.rtiming.shared.dataexchange.IProgressMonitor;
import com.rtiming.shared.dataexchange.ImportMessageList;
import com.rtiming.shared.dataexchange.cache.CityDataCacher;
import com.rtiming.shared.dataexchange.cache.ClazzDataCacher;
import com.rtiming.shared.dataexchange.cache.ClubDataCacher;
import com.rtiming.shared.dataexchange.cache.DataCacher;
import com.rtiming.shared.dataexchange.cache.IDataCacher;
import com.rtiming.shared.dataexchange.cache.NationDataCacher;
import com.rtiming.shared.dataexchange.iof3.IOF300Utility.ContactType;
import com.rtiming.shared.dataexchange.iof3.xml.Address;
import com.rtiming.shared.dataexchange.iof3.xml.Class;
import com.rtiming.shared.dataexchange.iof3.xml.Competitor;
import com.rtiming.shared.dataexchange.iof3.xml.CompetitorList;
import com.rtiming.shared.dataexchange.iof3.xml.ControlCard;
import com.rtiming.shared.dataexchange.iof3.xml.Country;
import com.rtiming.shared.dataexchange.iof3.xml.Id;
import com.rtiming.shared.dataexchange.iof3.xml.Organisation;
import com.rtiming.shared.dataexchange.iof3.xml.OrganisationType;
import com.rtiming.shared.dataexchange.iof3.xml.Person;
import com.rtiming.shared.ecard.IECardProcessService;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.runner.IRunnerProcessService;
import com.rtiming.shared.runner.RunnerRowData;
import com.rtiming.shared.settings.CodeFormData;
import com.rtiming.shared.settings.city.CountryFormData;

public class IOF300RunnerDataBean extends AbstractXMLDataBean {

  private static final long serialVersionUID = 1L;
  private Competitor competitor;
  private RunnerBean runnerBean;
  private RtEcard ecardBean;
  private final RunnerRowData runnerRow;
  private final transient DataCacher cache; // do not send to server
  private final String defaultNation;

  public IOF300RunnerDataBean(Long primaryKeyNr, RunnerRowData row, DataCacher cache, String defaultNation) {
    super(primaryKeyNr);
    this.runnerRow = row;
    this.cache = cache;
    this.defaultNation = defaultNation;
  }

  @Override
  public Object createXMLObject(Object main) throws ProcessingException {
    if (main == null || !(main instanceof CompetitorList)) {
      throw new IllegalArgumentException("must be iof3 CompetitorList");
    }
    if (runnerRow != null) {
      CompetitorList list = (CompetitorList) main;
      competitor = new Competitor();

      // Person
      Person person = IOF300Utility.convertToXmlPerson(runnerRow.getExtKey(),
          runnerRow.getLastName(),
          runnerRow.getFirstName(),
          runnerRow.getSexUid(),
          runnerRow.getYear(),
          runnerRow.getEvtBirthdate());

      IOF300Utility.addContact(person, ContactType.PhoneNumber, runnerRow.getPhone());
      IOF300Utility.addContact(person, ContactType.MobilePhoneNumber, runnerRow.getMobile());
      IOF300Utility.addContact(person, ContactType.FaxNumber, runnerRow.getFax());
      IOF300Utility.addContact(person, ContactType.EmailAddress, runnerRow.getEmail());
      IOF300Utility.addContact(person, ContactType.WebAddress, runnerRow.getWww());

      competitor.setPerson(person);

      // Address
      Address address = new Address();
      address.setCity(runnerRow.getCity());
      address.setZipCode(runnerRow.getZip());
      address.setStreet(runnerRow.getStreet());
      person.getAddress().add(address);

      // E-Card
      ControlCard controlCard = IOF300Utility.convertToXmlECard(runnerRow.geteCard());
      if (controlCard != null) {
        competitor.getControlCard().add(controlCard);
      }

      // Nation
      if (!StringUtility.isNullOrEmpty(runnerRow.getNation())) {
        Country country = new Country();
        country.setCode(runnerRow.getNation());
        country.setValue(runnerRow.getNation());
        competitor.getPerson().setNationality(country);
      }

      // Default Class
      if (runnerRow.getDefaultClazzUid() != null) {
        com.rtiming.shared.dataexchange.iof3.xml.Class clazz = new Class();
        clazz.setName(FMilaUtility.getCodeText(ClassCodeType.class, runnerRow.getDefaultClazzUid()));
        clazz.setShortName(FMilaUtility.getCodeExtKey(ClassCodeType.class, runnerRow.getDefaultClazzUid()));
        competitor.getClazz().add(clazz);
      }

      // Club
      if (!StringUtility.isNullOrEmpty(runnerRow.getClub())) {
        Organisation organisation = new Organisation();
        organisation.setName(runnerRow.getClub());
        organisation.setMediaName(runnerRow.getClub());
        organisation.setShortName(runnerRow.getClub());
        organisation.setType(OrganisationType.CLUB.toString());
        competitor.getOrganisation().add(organisation);
      }

      list.getCompetitor().add(competitor);
    }
    return main;
  }

  @Override
  public ArrayList<String> getPreviewRowData() throws ProcessingException {
    IRunnerProcessService runnerSvc = BEANS.get(IRunnerProcessService.class);
    if (competitor != null) {
      ImportResult result = doImportInternal(runnerSvc, competitor, cache, defaultNation);
      runnerBean = result.getRunner();
      ecardBean = result.getEcard();
    }
    ArrayList<String> list = new ArrayList<String>();
    if (runnerBean != null) {
      if (competitor != null) {
        list.add(runnerBean.getRunnerNr() == null ? TEXTS.get("No") : TEXTS.get("Yes"));
      }
      else {
        list.add("");
      }
      list.add(runnerBean.getExtKey());
      list.add(runnerBean.getLastName());
      list.add(runnerBean.getFirstName());
    }
    else if (runnerRow != null) {
      list.add(runnerRow.getExtKey());
      list.add(runnerRow.getLastName());
      list.add(runnerRow.getFirstName());
    }
    else {
      list.add("");
      list.add("");
      list.add("");
    }
    if (ecardBean != null) {
      list.add(ecardBean.getEcardNo());
    }
    else if (runnerRow != null) {
      list.add(runnerRow.geteCard());
    }
    else {
      list.add("");
    }

    // finally clean XML object since it is not serializable
    competitor = null;
    return list;
  }

  public void setCompetitor(Competitor competitor) {
    this.competitor = competitor;
  }

  @Override
  public void loadBeanFromDatabase() throws ProcessingException {
  }

  protected ImportResult doImportInternal(IRunnerProcessService runnerSvc, Competitor currentCompetitor, IDataCacher dataCache, String nation) throws ProcessingException {
    if (runnerSvc == null || currentCompetitor == null || dataCache == null || nation == null) {
      throw new IllegalArgumentException("arguments required");
    }
    RunnerBean runner = new RunnerBean();
    RtEcard ecard = null;

    Person person = currentCompetitor.getPerson();
    if (person != null) {
      runner = matchExistingRunner(person, runnerSvc);

      // Person and Address
      IOF300Utility.convertFromXmlPerson(person, runner, dataCache.getCache(CityDataCacher.class), nation);

      // Nation
      if (person.getNationality() != null) {
        if (!StringUtility.isNullOrEmpty(person.getNationality().getCode())) {
          String code = StringUtility.substring(person.getNationality().getCode(), 0, 3);
          CountryFormData country = dataCache.getCache(NationDataCacher.class).doImport(code);
          runner.setNationUid(country.getCountryUid());
        }
      }
    }

    // Default Class
    if (currentCompetitor.getClazz().size() > 0) {
      Class clazz = currentCompetitor.getClazz().get(0);
      if (!StringUtility.isNullOrEmpty(clazz.getShortName())) {
        CodeFormData clazzFormData = dataCache.getCache(ClazzDataCacher.class).doImport(clazz.getShortName());
        runner.setDefaultClassUid(clazzFormData.getCodeUid());
      }
      else if (!StringUtility.isNullOrEmpty(clazz.getName())) {
        CodeFormData clazzFormData = dataCache.getCache(ClazzDataCacher.class).doImport(clazz.getName());
        runner.setDefaultClassUid(clazzFormData.getCodeUid());
      }
    }

    // Club
    for (Organisation org : currentCompetitor.getOrganisation()) {
      if (StringUtility.equalsIgnoreCase(org.getType(), OrganisationType.CLUB.toString())) {
        if (!StringUtility.isNullOrEmpty(org.getName())) {
          ClubFormData club = dataCache.getCache(ClubDataCacher.class).doImport(org.getName());
          runner.setClubNr(club.getClubNr());
        }
      }
    }
    if (runner.getClubNr() == null) {
      for (Organisation org : currentCompetitor.getOrganisation()) {
        if (StringUtility.isNullOrEmpty(org.getType())) {
          ClubFormData club = dataCache.getCache(ClubDataCacher.class).doImport(org.getName());
          runner.setClubNr(club.getClubNr());
        }
        break;
      }
    }

    // E-Card
    if (currentCompetitor.getControlCard().size() > 0) {
      ControlCard controlCard = currentCompetitor.getControlCard().get(0);
      ecard = BEANS.get(IECardProcessService.class).findECard(controlCard.getValue());
    }

    ImportResult result = new ImportResult(runner, ecard);
    return result;
  }

  protected RunnerBean matchExistingRunner(Person person, IRunnerProcessService runnerSvc) throws ProcessingException {
    if (runnerSvc == null) {
      throw new IllegalArgumentException("Runner Svc must be provided");
    }
    RunnerBean result = new RunnerBean();

    // Match existing runner
    Long runnerNr = null;
    String extKey = null;
    if (person.getId().size() > 0) {
      Id personId = person.getId().get(0);
      runnerNr = runnerSvc.findRunner(String.valueOf(personId.getValue()));
      extKey = personId.getValue();
    }
    if (runnerNr != null) {
      // runner already exists
      result.setRunnerNr(runnerNr);
      result = runnerSvc.load(result);
    }
    else {
      // new runner, but keep identifier (ext key)
      result.setExtKey(StringUtility.uppercase(StringUtility.trim(extKey)));
    }
    return result;
  }

  @Override
  public void storeBeansToDatabase(List<AbstractDataBean> batch, IProgressMonitor monitor) throws ProcessingException {
    ImportMessageList result = BEANS.get(IDataExchangeService.class).storeIOF300Runner(batch);
    monitor.addErrors(result);
  }

  @Override
  public void storeBeanToDatabase(IProgressMonitor monitor) throws ProcessingException {
  }

  public RtEcard getEcard() {
    return ecardBean;
  }

  public RunnerBean getRunner() {
    return runnerBean;
  }

  public class ImportResult {
    RunnerBean runner;
    RtEcard ecard;

    public ImportResult(RunnerBean runner, RtEcard ecard) {
      super();
      this.runner = runner;
      this.ecard = ecard;
    }

    public RunnerBean getRunner() {
      return runner;
    }

    public RtEcard getEcard() {
      return ecard;
    }

  }

}
