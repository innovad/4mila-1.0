package com.rtiming.server.entry.startlist;

import java.util.Arrays;
import java.util.LinkedList;

import org.eclipse.scout.commons.CompareUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.NumberUtility;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.entry.startlist.StartlistSettingFormData;
import com.rtiming.shared.entry.startlist.StartlistSettingUtility;
import com.rtiming.shared.entry.startlist.StartlistSettingVacantPositionCodeType;

public final class StartlistVacantUtility {

  private StartlistVacantUtility() {
  }

  public static void addVacants(StartlistSettingFormData startlistSetting, LinkedList<StartlistParticipationBean> completeStartlist) throws ProcessingException {
    long vacantCount = StartlistSettingUtility.calculateVacantCount((long) completeStartlist.size(), startlistSetting.getVacantPercent().getValue(), startlistSetting.getVacantAbsolute().getValue());

    for (long v = 0; v < vacantCount; v++) {
      StartlistParticipationBean bean = new StartlistParticipationBean();
      bean.setVacant(true);
      bean.setStartlistSettingNr(startlistSetting.getStartlistSettingNr());
      bean.setEntryNr(v); // workaround to make equals/hash methods work
      if (CompareUtility.equals(startlistSetting.getVacantPositionGroup().getValue(), StartlistSettingVacantPositionCodeType.EarlyStartCode.ID)) {
        completeStartlist.addFirst(bean);
      }
      else if (CompareUtility.equals(startlistSetting.getVacantPositionGroup().getValue(), StartlistSettingVacantPositionCodeType.LateStartCode.ID)) {
        completeStartlist.addLast(bean);
      }
      else if (CompareUtility.equals(startlistSetting.getVacantPositionGroup().getValue(), StartlistSettingVacantPositionCodeType.DrawingCode.ID)) {
        int position = 0;
        if (completeStartlist.size() > 0) {
          position = NumberUtility.randomInt(completeStartlist.size() + 1);
        }
        completeStartlist.add(position, bean);
      }
    }
  }

  public static void removeVacants(Long... startlistSettingNrs) throws ProcessingException {
    if (startlistSettingNrs == null || startlistSettingNrs.length == 0) {
      return;
    }
    String queryString = "DELETE FROM RtStartlistSettingVacant " + "WHERE id.startlistSettingNr IN (:startlistSettingNrs) " + "AND id.clientNr = :sessionClientNr ";

    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("startlistSettingNrs", Arrays.asList(startlistSettingNrs));
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    query.executeUpdate();
  }

}
