package com.rtiming.client.dataexchange.iof;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.client.runner.RunnersTablePage;
import com.rtiming.shared.common.database.sql.RunnerBean;
import com.rtiming.shared.dao.RtEcard;
import com.rtiming.shared.dataexchange.AbstractXMLDataBean;
import com.rtiming.shared.dataexchange.IProgressMonitor;
import com.rtiming.shared.dataexchange.iof203.xml.Address;
import com.rtiming.shared.dataexchange.iof203.xml.AddressType;
import com.rtiming.shared.dataexchange.iof203.xml.CCard;
import com.rtiming.shared.dataexchange.iof203.xml.Competitor;
import com.rtiming.shared.dataexchange.iof203.xml.CompetitorList;
import com.rtiming.shared.dataexchange.iof203.xml.Person;
import com.rtiming.shared.dataexchange.iof203.xml.PersonId;
import com.rtiming.shared.ecard.IECardProcessService;
import com.rtiming.shared.runner.IRunnerProcessService;

public class IOF203RunnerDataBean extends AbstractXMLDataBean {

  private static final long serialVersionUID = 1L;
  private Competitor competitor;
  private RunnerBean runner;
  private RtEcard ecard;
  private final ITableRow row;

  public IOF203RunnerDataBean(Long primaryKeyNr, ITableRow row) {
    super(primaryKeyNr);
    this.row = row;
  }

  @Override
  public Object createXMLObject(Object main) throws ProcessingException {
    if (row != null) {
      RunnersTablePage.Table table = (RunnersTablePage.Table) row.getTable();

      CompetitorList list = (CompetitorList) main;
      competitor = new Competitor();

      // Person
      Person person = IOF203Utility.convertToXmlPerson(table.getLastNameColumn().getValue(row), table.getFirstNameColumn().getValue(row));

      // Address
      Address address = new Address();
      address.setCity(table.getCityColumn().getValue(row));
      address.setZipCode(table.getZipColumn().getValue(row));
      address.setStreet(table.getStreetColumn().getValue(row));
      AddressType type = new AddressType();
      type.setValue("official");
      address.setAddressType(type);
      person.getAddress().add(address);

      competitor.getPersonIdOrPerson().add(person);

      // Person Id
      if (!StringUtility.isNullOrEmpty(table.getExtKeyColumn().getValue(row))) {
        PersonId personId = new PersonId();
        personId.setvalue(table.getExtKeyColumn().getValue(row));
        personId.setType("nat");
        competitor.getPersonIdOrPerson().add(personId);
      }

      if (!StringUtility.isNullOrEmpty(table.getECardColumn().getValue(row))) {
        CCard ccard = IOF203Utility.convertToXmlECard(table.getECardColumn().getValue(row));
        competitor.getCCard().add(ccard);
      }

      list.getCompetitor().add(competitor);
    }
    return main;
  }

  @Override
  public ArrayList<String> getPreviewRowData() throws ProcessingException {
    if (competitor != null) {
      doImportInternal();
    }
    ArrayList<String> list = new ArrayList<String>();
    if (runner != null) {
      if (competitor != null) {
        list.add(runner.getRunnerNr() == null ? TEXTS.get("No") : TEXTS.get("Yes"));
      }
      else {
        list.add("");
      }
      list.add(runner.getExtKey());
      list.add(runner.getLastName());
      list.add(runner.getFirstName());
    }
    else if (row != null) {
      RunnersTablePage.Table table = (RunnersTablePage.Table) row.getTable();
      list.add(table.getExtKeyColumn().getValue(row));
      list.add(table.getLastNameColumn().getValue(row));
      list.add(table.getFirstNameColumn().getValue(row));
    }
    else {
      list.add("");
      list.add("");
      list.add("");
    }
    if (ecard != null) {
      list.add(ecard.getEcardNo());
    }
    else if (row != null) {
      RunnersTablePage.Table table = (RunnersTablePage.Table) row.getTable();
      list.add(table.getECardColumn().getValue(row));
    }
    else {
      list.add("");
    }
    return list;
  }

  public void setCompetitor(Competitor competitor) {
    this.competitor = competitor;
  }

  @Override
  public void loadBeanFromDatabase() throws ProcessingException {
  }

  private void doImportInternal() throws ProcessingException {
    IRunnerProcessService runnerSvc = BEANS.get(IRunnerProcessService.class);

    List<Object> personIdOrPersons = competitor.getPersonIdOrPerson();
    // personId
    for (Object personIdOrPerson : personIdOrPersons) {
      if (personIdOrPerson instanceof PersonId) {
        if (runner == null) {
          runner = new RunnerBean();
        }
        PersonId personId = (PersonId) personIdOrPerson;
        Long runnerNr = runnerSvc.findRunner(String.valueOf(personId.getvalue()));
        if (runnerNr != null) {
          // runner already exists
          runner.setRunnerNr(runnerNr);
          runner = runnerSvc.load(runner);
        }
        else {
          // new runner, but keep identifier (ext key)
          runner.setExtKey(StringUtility.uppercase(StringUtility.trim(personId.getvalue())));
        }
      }
    }
    // Person
    for (Object personIdOrPerson : personIdOrPersons) {
      if (personIdOrPerson instanceof Person) {
        Person person = (Person) personIdOrPerson;
        if (runner == null) {
          runner = new RunnerBean();
        }
        runner = IOF203Utility.convertFromXmlPerson(person, runner);
      }
    }
    // E-Card
    for (CCard ccard : competitor.getCCard()) {
      if (ecard == null) {
        ecard = IOF203Utility.convertFromXmlECard(ccard);
      }
    }
  }

  @Override
  public void storeBeanToDatabase(IProgressMonitor monitor) throws ProcessingException {
    if (ecard != null) {
      if (ecard.getKey() == null) {
        ecard = BEANS.get(IECardProcessService.class).create(ecard);
      }
      else {
        ecard = BEANS.get(IECardProcessService.class).store(ecard);
      }
    }
    if (runner != null) {
      if (ecard != null) {
        runner.setECardNr(ecard.getKey().getId());
      }
      if (runner.getRunnerNr() == null) {
        runner = BEANS.get(IRunnerProcessService.class).create(runner);
      }
      else {
        runner = BEANS.get(IRunnerProcessService.class).store(runner);
      }
    }
  }

}
