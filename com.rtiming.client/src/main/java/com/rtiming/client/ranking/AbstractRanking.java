package com.rtiming.client.ranking;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;

import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.entry.startlist.BibNoOrderCodeType;
import com.rtiming.shared.race.RaceStatusCodeType;
import com.rtiming.shared.ranking.AbstractRankingBoxData;
import com.rtiming.shared.ranking.FormulaUtility;
import com.rtiming.shared.ranking.RankingFormatCodeType;

public abstract class AbstractRanking implements Comparable<AbstractRanking> {

  private final Long sortingUid;
  private final Long formatUid;
  private final Long decimalPlaces;
  private final Long entryNr;
  private final Long runnerNr;
  private final Long time;

  private Double result;
  private Long statusUid;
  private Long rank;
  private Object[] resultRow;

  public AbstractRanking(Long entryNr, Long runnerNr, Long time, Long statusUid, AbstractRankingBoxData formulaSettings) {
    this.entryNr = entryNr;
    this.runnerNr = runnerNr;
    this.time = time;
    this.statusUid = statusUid;
    this.sortingUid = formulaSettings.getSorting().getValue();
    this.formatUid = formulaSettings.getFormat().getValue();
    this.decimalPlaces = formulaSettings.getDecimalPlaces().getValue();
  }

  public void setResult(Object calculation) {
    // try to convert javascript value to java double
    if (calculation == null) {
      result = null;
    }
    else if (calculation instanceof Double) {
      result = (Double) calculation;
    }
    else if (calculation instanceof Long) {
      result = (double) (Long) calculation;
    }
    else if (calculation instanceof Integer) {
      result = (double) (Integer) calculation;
    }
    else {
      String r = StringUtility.emptyIfNull(calculation);
      result = NumberUtility.parseDouble(r);
    }
    result = roundResult(result);
  }

  private Double roundResult(Double value) {
    if (value == null) {
      return null;
    }
    else if (CompareUtility.equals(formatUid, RankingFormatCodeType.TimeCode.ID)) {
      Long timePrecisionUid = FormulaUtility.decimalPlaces2timePrecision(decimalPlaces);
      return NumberUtility.toDouble(FMilaUtility.roundTime(NumberUtility.toLong(value), timePrecisionUid));
    }
    else {
      double floor = Math.pow(10, NumberUtility.nvl(decimalPlaces, 0));
      if (CompareUtility.equals(sortingUid, BibNoOrderCodeType.DescendingCode.ID)) {
        return Math.floor(value * floor) / floor;
      }
      else {
        return Math.ceil(value * floor) / floor;
      }
    }
  }

  public Double getResult() {
    return result;
  }

  public Long getTime() {
    return time;
  }

  public Object getPointsFormatted() {
    Object resultFormatted = null;
    if (CompareUtility.equals(formatUid, RankingFormatCodeType.TimeCode.ID)) {
      Long timePrecisionUid = FormulaUtility.decimalPlaces2timePrecision(decimalPlaces);
      resultFormatted = FMilaUtility.formatTime(NumberUtility.toLong(result), timePrecisionUid);
    }
    else {
      String formatted = String.format("%1$." + NumberUtility.nvl(decimalPlaces, 0) + "f", result);
      resultFormatted = formatted;
    }
    return resultFormatted;
  }

  public void setRank(Long rank) {
    this.rank = rank;
  }

  public Long getRank() {
    return rank;
  }

  public void setResultRow(Object[] resultRow) {
    this.resultRow = resultRow;
  }

  public Long getStatusUid() {
    return statusUid;
  }

  public String getStatus() throws ProcessingException {
    return RankingUtility.raceStatus2ExtKey(statusUid);
  }

  public void setStatus(Object status) throws ProcessingException {
    statusUid = RankingUtility.extKey2RaceStatus(status);
  }

  public Long getRunnerNr() {
    return runnerNr;
  }

  public Long getEntryNr() {
    return entryNr;
  }

  protected abstract int getAdditionalColumnCount();

  public Object[] getResultRow() {
    Object[] f = new Object[resultRow.length + getAdditionalColumnCount()];
    System.arraycopy(resultRow, 0, f, 0, resultRow.length); // expand array
    return f;
  }

  protected Long getFormatUid() {
    return formatUid;
  }

  @Override
  public int compareTo(AbstractRanking other) {
    if (other == null) {
      return -1;
    }
    if (CompareUtility.notEquals(other.getStatusUid(), getStatusUid())) {
      if (CompareUtility.equals(getStatusUid(), RaceStatusCodeType.OkCode.ID)) {
        return -1;
      }
      return 1;
    }
    if (getResult() == null) {
      return 1;
    }
    if (other.getResult() == null) {
      return -1;
    }
    if (sortingUid == BibNoOrderCodeType.AscendingCode.ID) {
      return getResult().compareTo(other.getResult());
    }
    return other.getResult().compareTo(getResult());
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hashCode = 1;
    hashCode = prime * hashCode + ((entryNr == null) ? 0 : entryNr.hashCode());
    hashCode = prime * hashCode + ((runnerNr == null) ? 0 : runnerNr.hashCode());
    return hashCode;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    AbstractRanking other = (AbstractRanking) obj;
    if (entryNr == null) {
      if (other.entryNr != null) {
        return false;
      }
    }
    else if (!entryNr.equals(other.entryNr)) {
      return false;
    }
    if (runnerNr == null) {
      if (other.runnerNr != null) {
        return false;
      }
    }
    else if (!runnerNr.equals(other.runnerNr)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "AbstractRanking [entryNr=" + entryNr + ", runnerNr=" + runnerNr + ", time=" + time + ", result=" + result + ", statusUid=" + statusUid + ", rank=" + rank + "]";
  }

}
