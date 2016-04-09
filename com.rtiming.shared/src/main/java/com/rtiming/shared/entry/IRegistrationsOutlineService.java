package com.rtiming.shared.entry;

import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.shared.entry.startlist.StartlistSettingRowData;

@TunnelToServer
public interface IRegistrationsOutlineService extends IService {

  List<EntryRowData> getEntryTableData(EntryTableDataOptions options, EntriesSearchFormData formData) throws ProcessingException;

  Object[][] getRegistrationTableData(RegistrationsSearchFormData formData) throws ProcessingException;

  Object[][] getPaymentTableData(Long registrationNr) throws ProcessingException;

  List<StartlistSettingRowData> getStartlistSettingTableData(Long eventNr) throws ProcessingException;

  Object[][] getEventStartblockTableData(Long eventNr) throws ProcessingException;

  Object[][] getEntryClassTableData(SearchFilter filter) throws ProcessingException;

  Object[][] getEntryClubTableData(SearchFilter filter) throws ProcessingException;

  Object[][] getEntryCourseTableData(SearchFilter filter) throws ProcessingException;

  Object[][] getVacantsTableData(Long eventNr) throws ProcessingException;
}
