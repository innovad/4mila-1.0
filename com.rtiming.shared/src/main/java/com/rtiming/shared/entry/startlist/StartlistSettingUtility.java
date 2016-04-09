package com.rtiming.shared.entry.startlist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.NumberUtility;

import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.entry.startlist.StartlistSettingFormData.Options;

public final class StartlistSettingUtility {

  private StartlistSettingUtility() {
  }

  public static List<Long> getStartlistOptions(Options startlistOptions) {
    List<Long> options = new ArrayList<Long>();
    if (startlistOptions != null && startlistOptions.getValue() != null) {
      options = new ArrayList<>(startlistOptions.getValue());
    }
    return options;
  }

  public static boolean checkIfAllExistingStartlistSettingNrsAreSelected(Long[] selectedNrs, Long[] allNrs) {
    if (selectedNrs == null || allNrs == null) {
      throw new IllegalArgumentException("Arguments should not be NULL");
    }
    List<Long> list = new ArrayList<Long>();
    for (Long nr : allNrs) {
      if (nr != null) {
        list.add(nr);
      }
    }
    boolean allSelected = list.size() == selectedNrs.length;
    return allSelected;
  }

  public static boolean checkIfStartlistSettingsContainRegistrationOption(Long[] selectedNrs) throws ProcessingException {
    if (selectedNrs == null) {
      throw new IllegalArgumentException("Argument should not be NULL");
    }

    IStartlistSettingProcessService settingService = BEANS.get(IStartlistSettingProcessService.class);

    boolean registrationOptionSelected = false;
    for (Long startlistSettingNr : selectedNrs) {
      StartlistSettingFormData formData = new StartlistSettingFormData();
      formData.setStartlistSettingNr(startlistSettingNr);
      formData = settingService.load(formData);
      List<Long> options = StartlistSettingUtility.getStartlistOptions(formData.getOptions());
      if (options.contains(StartlistSettingOptionCodeType.GroupRegistrationsCode.ID) || options.contains(StartlistSettingOptionCodeType.SplitRegistrationsCode.ID)) {
        registrationOptionSelected = true;
        break;
      }
    }
    return registrationOptionSelected;
  }

  public static long calculateVacantCount(Long entriesCount, Long vacantPercentIn, Long vacantAbsoluteIn) {
    double entriesCountDouble = NumberUtility.nvl(entriesCount, 0);
    double vacantPercent = NumberUtility.nvl(vacantPercentIn, 0);
    long vacantAbsolute = NumberUtility.nvl(vacantAbsoluteIn, 0);

    long vacantPercentCount = Math.round(vacantPercent / 100 * entriesCountDouble);
    long vacantCount = Math.max(vacantAbsolute, vacantPercentCount);

    return vacantCount;
  }

  public static Date calculateLastStart(Date firstStart, Long interval, Long entriesCount, Long vacantPercent, Long vacantAbsolute) {
    if (firstStart != null && interval != null && entriesCount != null) {
      long totalCount = entriesCount + StartlistSettingUtility.calculateVacantCount(entriesCount, vacantPercent, vacantAbsolute);
      return FMilaUtility.addSeconds(firstStart, ((int) Math.max(totalCount - 1, 0)) * interval.intValue());
    }
    return null;
  }

  public static Date calculateFirstStart(Date lastStart, Long interval, Long entriesCount, Long vacantPercent, Long vacantAbsolute) {
    if (lastStart != null && interval != null && entriesCount != null) {
      long totalCount = entriesCount + StartlistSettingUtility.calculateVacantCount(entriesCount, vacantPercent, vacantAbsolute);
      return FMilaUtility.addSeconds(lastStart, (int) (-1 * (totalCount - 1) * interval.intValue()));
    }
    return null;
  }

}
