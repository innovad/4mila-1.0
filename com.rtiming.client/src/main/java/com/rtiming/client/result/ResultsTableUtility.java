package com.rtiming.client.result;

import java.util.List;

import org.eclipse.scout.commons.exception.ProcessingException;

import com.rtiming.client.ClientSession;
import com.rtiming.shared.common.EntityCodeType;
import com.rtiming.shared.common.database.sql.AdditionalInformationValueBean;
import com.rtiming.shared.entry.SharedCache;
import com.rtiming.shared.results.ResultRowData;

/**
 * 
 */
public class ResultsTableUtility {

  public static Object[][] convertListToObjectArray(List<ResultRowData> list, AbstractResultsTable table) throws ProcessingException {

    Object[][] result = new Object[list.size()][table.getColumnCount()];
    int k = 0;
    for (ResultRowData row : list) {
      result[k] = new Object[table.getColumnCount()];
      result[k][table.getRaceNrColumn().getColumnIndex()] = row.getRaceNr();
      result[k][table.getEntryNrColumn().getColumnIndex()] = row.getEntryNr();
      result[k][table.getRunnerNrColumn().getColumnIndex()] = row.getRunnerNr();
      result[k][table.getRankColumn().getColumnIndex()] = row.getRank();
      result[k][table.getClassShortcutColumn().getColumnIndex()] = row.getClassShortcut();
      result[k][table.getClassNameColumn().getColumnIndex()] = row.getClassName();
      result[k][table.getCourseShortcutColumn().getColumnIndex()] = row.getCourseShortcut();
      result[k][table.getBibNumberColumn().getColumnIndex()] = row.getBibNumber();
      result[k][table.getExtKeyColumn().getColumnIndex()] = row.getExtKey();
      result[k][table.getRunnerColumn().getColumnIndex()] = row.getRunner();
      result[k][table.getLastNameColumn().getColumnIndex()] = row.getLastName();
      result[k][table.getFirstNameColumn().getColumnIndex()] = row.getFirstName();
      result[k][table.getECardColumn().getColumnIndex()] = row.geteCard();
      result[k][table.getRentalCardColumn().getColumnIndex()] = row.isRentalCard();
      result[k][table.getClubShortcutColumn().getColumnIndex()] = row.getClubShortcut();
      result[k][table.getClubExtKeyColumn().getColumnIndex()] = row.getClubExtKey();
      result[k][table.getClubColumn().getColumnIndex()] = row.getClub();
      result[k][table.getNationColumn().getColumnIndex()] = row.getNation();
      result[k][table.getNationCountryCodeColumn().getColumnIndex()] = row.getNationCountryCode();
      result[k][table.getSexColumn().getColumnIndex()] = row.getSex();
      result[k][table.getBirthdateColumn().getColumnIndex()] = row.getBirthdate();
      result[k][table.getYearColumn().getColumnIndex()] = row.getYear();
      result[k][table.getStreetColumn().getColumnIndex()] = row.getStreet();
      result[k][table.getZipColumn().getColumnIndex()] = row.getZip();
      result[k][table.getCityColumn().getColumnIndex()] = row.getCity();
      result[k][table.getAreaColumn().getColumnIndex()] = row.getArea();
      result[k][table.getRegionColumn().getColumnIndex()] = row.getRegion();
      result[k][table.getCountryColumn().getColumnIndex()] = row.getCountry();
      result[k][table.getPhoneColumn().getColumnIndex()] = row.getPhone();
      result[k][table.getFaxColumn().getColumnIndex()] = row.getFax();
      result[k][table.getMobilePhoneColumn().getColumnIndex()] = row.getMobilePhone();
      result[k][table.getEMailColumn().getColumnIndex()] = row.geteMail();
      result[k][table.getURLColumn().getColumnIndex()] = row.getURL();
      result[k][table.getRaceStatusColumn().getColumnIndex()] = row.getRaceStatus();
      result[k][table.getLegStartTimeColumn().getColumnIndex()] = row.getLegStartTime();
      result[k][table.getLegTimeColumn().getColumnIndex()] = row.getLegTime();
      result[k][table.getTimeBehindColumn().getColumnIndex()] = row.getTimeBehind();
      result[k][table.getPercentColumn().getColumnIndex()] = row.getPercent();
      result[k][table.getTimeColumn().getColumnIndex()] = row.getTime();

      // additional values
      List<AdditionalInformationValueBean> entryAis = SharedCache.getAddInfoForEntity(EntityCodeType.EntryCode.ID, ClientSession.get().getSessionClientNr());
      List<AdditionalInformationValueBean> runnerAis = SharedCache.getAddInfoForEntity(EntityCodeType.RunnerCode.ID, ClientSession.get().getSessionClientNr());
      for (int a = 0; a < entryAis.size(); a++) {
        if (row.getEntryAdditionalValues() != null) {
          result[k][table.getColumnCount() - entryAis.size() - runnerAis.size() + a] = row.getEntryAdditionalValues()[a];
        }
      }
      for (int a = 0; a < runnerAis.size(); a++) {
        if (row.getRunnerAdditionalValues() != null) {
          result[k][table.getColumnCount() - runnerAis.size() + a] = row.getRunnerAdditionalValues()[a];
        }
      }

      k++;
    }
    return result;
  }

}
