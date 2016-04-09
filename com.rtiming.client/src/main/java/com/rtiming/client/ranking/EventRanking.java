package com.rtiming.client.ranking;

import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;

import com.rtiming.shared.event.course.ControlTypeCodeType;
import com.rtiming.shared.ranking.AbstractRankingBoxData;

public class EventRanking extends AbstractRanking {

  private boolean bestRanking = true;
  private List<Control> controls;

  /**
   * @param entryNr
   * @param runnerNr
   * @param time
   * @param statusUid
   * @param formulaSettings
   */
  public EventRanking(Long entryNr, Long runnerNr, Long time, Long statusUid, AbstractRankingBoxData formulaSettings) {
    super(entryNr, runnerNr, time, statusUid, formulaSettings);
  }

  @Override
  protected int getAdditionalColumnCount() {
    return 1;
  }

  public boolean isBestRanking() {
    return bestRanking;
  }

  public void setBestRanking(boolean bestRanking) {
    this.bestRanking = bestRanking;
  }

  public List<Control> getControls() {
    return controls;
  }

  public void setControls(List<Control> controls) {
    this.controls = controls;
  }

  public int getControlCount(String status) throws ProcessingException {
    Long id = null;
    if (!StringUtility.isNullOrEmpty(status)) {
      id = RankingUtility.extKey2ControlStatus(status);
    }

    int c = 0;
    if (controls != null) {
      for (Control control : controls) {
        if (CompareUtility.equals(ControlTypeCodeType.ControlCode.ID, control.getTypeUid()) &&
            (id == null || CompareUtility.equals(control.getStatusUid(), id))) {
          c++;
        }
      }
    }
    return c;
  }
}
