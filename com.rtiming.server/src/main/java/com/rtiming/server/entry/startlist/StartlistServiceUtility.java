package com.rtiming.server.entry.startlist;

import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.util.NumberUtility;

import com.rtiming.shared.entry.startlist.BibNoOrderCodeType;
import com.rtiming.shared.entry.startlist.StartlistSettingFormData;
import com.rtiming.shared.entry.startlist.StartlistTypeCodeType;

public final class StartlistServiceUtility {

  private StartlistServiceUtility() {
  }

  public static Long setStartTimesAndBibNo(StartlistSettingBean bean, Long nextStart, Long nextBibNo) {
    StartlistSettingFormData startlistSetting = bean.getSettings();

    long bibInterval = 1;
    if (CompareUtility.equals(startlistSetting.getBibNoOrderUid().getValue(), BibNoOrderCodeType.DescendingCode.ID)) {
      bibInterval = -1;
    }
    for (StartlistParticipationBean participation : bean.getList()) {
      participation.setStartTime(nextStart);
      bean.setLastStart(nextStart);
      participation.setBibNo(nextBibNo);

      if (CompareUtility.equals(startlistSetting.getTypeUid().getValue(), StartlistTypeCodeType.DrawingCode.ID)) {
        nextStart += (NumberUtility.nvl(startlistSetting.getStartInterval().getValue(), 0) * 1000);
      }
      nextBibNo += bibInterval;
    }

    return nextBibNo;
  }

}
