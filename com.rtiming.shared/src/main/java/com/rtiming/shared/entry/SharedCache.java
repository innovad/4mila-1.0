package com.rtiming.shared.entry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.BEANS;

import com.rtiming.shared.common.database.sql.AdditionalInformationValueBean;
import com.rtiming.shared.dao.RtClassAge;
import com.rtiming.shared.dao.RtReportTemplate;
import com.rtiming.shared.entry.startlist.StartlistSettingRowData;
import com.rtiming.shared.settings.ISettingsOutlineService;
import com.rtiming.shared.settings.addinfo.IAdditionalInformationProcessService;
import com.rtiming.shared.settings.addinfo.IEventAdditionalInformationProcessService;
import com.rtiming.shared.settings.clazz.IAgeProcessService;
import com.rtiming.shared.settings.fee.FeeFormData;
import com.rtiming.shared.settings.fee.IFeeProcessService;

/**
 * 
 */
public class SharedCache {

  private static List<RtClassAge> ageConfig;
  private static EventConfiguration eventConfig;
  private static HashMap<Long, long[]> eventAdditionalInformationConfig;
  private static List<FeeFormData> feeConfig;
  private static List<RtReportTemplate> templateConfig;
  private static List<StartlistSettingRowData> startlistConfig;
  private static Map<Long, Map<Long, List<AdditionalInformationValueBean>>> addInfoDefaults = new HashMap<>();

  private static Object CACHE_LOCK = new Object();

  public static List<RtClassAge> getAgeConfiguration() throws ProcessingException {
    synchronized (CACHE_LOCK) {
      if (ageConfig == null) {
        ageConfig = loadAgeConfigurationInternal();
      }
      return ageConfig;
    }
  }

  public static EventConfiguration getEventConfiguration() throws ProcessingException {
    synchronized (CACHE_LOCK) {
      if (eventConfig == null) {
        eventConfig = loadEventConfigurationInternal();
      }
      return eventConfig;
    }
  }

  public static Map<Long, long[]> getEventAdditionalInformationConfiguration() throws ProcessingException {
    synchronized (CACHE_LOCK) {
      if (eventAdditionalInformationConfig == null) {
        eventAdditionalInformationConfig = loadEventAdditionalInformationConfigurationInternal();
      }
      return eventAdditionalInformationConfig;
    }
  }

  public static List<FeeFormData> getFeeConfiguration() throws ProcessingException {
    synchronized (CACHE_LOCK) {
      if (feeConfig == null) {
        feeConfig = loadFeeConfigurationInternal();
      }
      return feeConfig;
    }
  }

  public static List<RtReportTemplate> getTemplateConfiguration() throws ProcessingException {
    synchronized (CACHE_LOCK) {
      if (templateConfig == null) {
        templateConfig = loadReportTemplateConfigurationInternal();
      }
      return templateConfig;
    }
  }

  public static List<StartlistSettingRowData> getStartlistConfiguration() throws ProcessingException {
    synchronized (CACHE_LOCK) {
      if (startlistConfig == null) {
        startlistConfig = loadStarlistConfigurationInternal();
      }
      return startlistConfig;
    }
  }

  public static List<AdditionalInformationValueBean> getAddInfoForEntity(Long entityUid, Long clientNr) throws ProcessingException {
    if (entityUid == null) {
      return new ArrayList<>();
    }
    synchronized (CACHE_LOCK) {
      if (addInfoDefaults.get(clientNr) == null) {
        addInfoDefaults.put(clientNr, loadAddInfoForEntityInternal(clientNr));
      }
      List<AdditionalInformationValueBean> list = addInfoDefaults.get(clientNr).get(entityUid);
      if (list != null) {
        list = new ArrayList<>(list); // return a copy, so that users may modify it safely
      }
      return list;
    }
  }

  public static void resetCache() throws ProcessingException {
    synchronized (CACHE_LOCK) {
      ageConfig = null;
      eventConfig = null;
      eventAdditionalInformationConfig = null;
      feeConfig = null;
      templateConfig = null;
      startlistConfig = null;
      addInfoDefaults.clear();
    }
  }

  private static List<RtClassAge> loadAgeConfigurationInternal() throws ProcessingException {
    return BEANS.get(IAgeProcessService.class).loadAgeConfiguration();
  }

  private static EventConfiguration loadEventConfigurationInternal() throws ProcessingException {
    return BEANS.get(IEntryService.class).loadEventConfiguration();
  }

  private static HashMap<Long, long[]> loadEventAdditionalInformationConfigurationInternal() throws ProcessingException {
    return BEANS.get(IEventAdditionalInformationProcessService.class).loadEventAdditionalInformationConfiguration();
  }

  private static List<FeeFormData> loadFeeConfigurationInternal() throws ProcessingException {
    return BEANS.get(IFeeProcessService.class).loadFeeConfiguration();
  }

  private static List<RtReportTemplate> loadReportTemplateConfigurationInternal() throws ProcessingException {
    return BEANS.get(ISettingsOutlineService.class).getReportTemplateTableData();
  }

  private static List<StartlistSettingRowData> loadStarlistConfigurationInternal() throws ProcessingException {
    return BEANS.get(IRegistrationsOutlineService.class).getStartlistSettingTableData(null);
  }

  private static Map<Long, List<AdditionalInformationValueBean>> loadAddInfoForEntityInternal(Long clientNr) {
    return BEANS.get(IAdditionalInformationProcessService.class).loadAddInfoForEntity(clientNr);
  }

}
