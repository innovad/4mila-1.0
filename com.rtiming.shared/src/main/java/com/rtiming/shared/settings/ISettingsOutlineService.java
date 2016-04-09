package com.rtiming.shared.settings;

import java.util.List;
import java.util.Map;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

import com.rtiming.shared.common.AbstractSqlCodeType;
import com.rtiming.shared.dao.RtClassAge;
import com.rtiming.shared.dao.RtReportTemplate;
import com.rtiming.shared.settings.city.CitySearchFormData;

@TunnelToServer
public interface ISettingsOutlineService extends IService {

  Object[][] getCityTableData(CitySearchFormData formData) throws ProcessingException;

  Object[][] loadCodes(AbstractSqlCodeType codeType) throws ProcessingException;

  Object[][] getCountryTableData() throws ProcessingException;

  Object[][] getClassTableData() throws ProcessingException;

  List<RtClassAge> getAgeTableData(Long classUid) throws ProcessingException;

  void intialDataLoad() throws ProcessingException;

  String getInstallationInfo(Map<String, String> plainTextPasswords) throws ProcessingException;

  Object[][] getAdditionalInformationAdministrationTableData(Long parentUid) throws ProcessingException;

  Object[][] getCurrencyTableData() throws ProcessingException;

  Object[][] getFeeGroupTableData() throws ProcessingException;

  Object[][] getFeeTableData(Long feeGroupNr) throws ProcessingException;

  Object[][] getStartblockTableData() throws ProcessingException;

  Object[][] getAreaTableData() throws ProcessingException;

  Object[][] getUserTableData() throws ProcessingException;

  Object[][] getAccountTableData() throws ProcessingException;

  Object[][] getAccountClientTableData(Long accountNr) throws ProcessingException;

  List<RtReportTemplate> getReportTemplateTableData() throws ProcessingException;

}
