package com.rtiming.shared.ranking;

import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;

import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.entry.startlist.BibNoOrderCodeType;

public class RankingFormulaTypeCodeType extends AbstractCodeType<Long, Long> {

  private static final long serialVersionUID = 1L;
  public static final Long ID = 2300L;

  public RankingFormulaTypeCodeType() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredText() {
    return TEXTS.get("Formula");
  }

  @Override
  public Long getId() {
    return ID;
  }

  @Order(10.0)
  public static class TimeCode extends AbstractFormulaCode {

    private static final long serialVersionUID = 1L;
    public static final Long ID = 2301L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("Time");
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public String getFormula() {
      return "var count = races.size();" + FMilaUtility.LINE_SEPARATOR +
          "for (k = 0; k < count; k++) {" + FMilaUtility.LINE_SEPARATOR +
          "   var race = races.get(k);" + FMilaUtility.LINE_SEPARATOR +
          "   if (race.status == 'OK') {" + FMilaUtility.LINE_SEPARATOR +
          "      race.result = race.time;" + FMilaUtility.LINE_SEPARATOR +
          "   } else {" + FMilaUtility.LINE_SEPARATOR +
          "      race.result = null;" + FMilaUtility.LINE_SEPARATOR +
          "   }" + FMilaUtility.LINE_SEPARATOR +
          "}";
    }

    @Override
    public Long getFormatUid() {
      return RankingFormatCodeType.TimeCode.ID;
    }

    @Override
    public Long getDecimalPlaces() {
      return 0L;
    }

    @Override
    public Long getSortingUid() {
      return BibNoOrderCodeType.AscendingCode.ID;
    }

    @Override
    public RankingType getRankingType() {
      return RankingType.EVENT;
    }

  }

  @Order(20.0)
  public static class PercentFormulaCode extends AbstractFormulaCode {

    private static final long serialVersionUID = 1L;
    public static final Long ID = 2307L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("PercentRankingFormula");
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public String getFormula() {
      return "var count = races.size();" + FMilaUtility.LINE_SEPARATOR +
          "for (k = 0; k < count; k++) {" + FMilaUtility.LINE_SEPARATOR +
          "   var race = races.get(k);" + FMilaUtility.LINE_SEPARATOR +
          "   var time = parseInt(race.getTime());" + FMilaUtility.LINE_SEPARATOR +
          "   if (race.status == 'OK' && !isNaN(winningTime) && !isNaN(time)) {" + FMilaUtility.LINE_SEPARATOR +
          "      race.result = Math.max(0,100 - (time-winningTime) / winningTime*100);" + FMilaUtility.LINE_SEPARATOR +
          "   } else {" + FMilaUtility.LINE_SEPARATOR +
          "      race.result = 0;" + FMilaUtility.LINE_SEPARATOR +
          "   }" + FMilaUtility.LINE_SEPARATOR +
          "}";
    }

    @Override
    public Long getFormatUid() {
      return RankingFormatCodeType.PointsCode.ID;
    }

    @Override
    public Long getDecimalPlaces() {
      return 2L;
    }

    @Override
    public Long getSortingUid() {
      return BibNoOrderCodeType.DescendingCode.ID;
    }

    @Override
    public RankingType getRankingType() {
      return RankingType.EVENT;
    }

  }

  @Order(30.0)
  public static class ScottishRankingFormulaCode extends AbstractFormulaCode {

    private static final long serialVersionUID = 1L;
    public static final Long ID = 2302L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("ScottishRankingFormula");
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public String getFormula() {
      return "var count = races.size();" + FMilaUtility.LINE_SEPARATOR +
          "for (k = 0; k < count; k++) {" + FMilaUtility.LINE_SEPARATOR +
          "   var race = races.get(k);" + FMilaUtility.LINE_SEPARATOR +
          "   if (race.status == 'OK') {" + FMilaUtility.LINE_SEPARATOR +
          "      race.result = 1000/race.time*winningTime;" + FMilaUtility.LINE_SEPARATOR +
          "   } else {" + FMilaUtility.LINE_SEPARATOR +
          "      race.result = 0;" + FMilaUtility.LINE_SEPARATOR +
          "   }" + FMilaUtility.LINE_SEPARATOR +
          "}";
    }

    @Override
    public Long getFormatUid() {
      return RankingFormatCodeType.PointsCode.ID;
    }

    @Override
    public Long getDecimalPlaces() {
      return 0L;
    }

    @Override
    public Long getSortingUid() {
      return BibNoOrderCodeType.DescendingCode.ID;
    }

    @Override
    public RankingType getRankingType() {
      return RankingType.EVENT;
    }

  }

  /**
   * Score Orienteering with time limit and penalty for overdue runners
   */
  @Order(40.0)
  public static class ScoreTimeLimitFormulaCode extends AbstractFormulaCode {

    private static final long serialVersionUID = 1L;
    public static final Long ID = 2303L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("ScoreTimeLimitFormula");
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public String getFormula() {
      return "var timeLimit = 20*60*1000; // change to desired time limit (currently 20 minutes)" + FMilaUtility.LINE_SEPARATOR +
          "var penalty = -1; // change to desired penalty (currently -1 point per minute)" + FMilaUtility.LINE_SEPARATOR +
          "" + FMilaUtility.LINE_SEPARATOR +
          "var count = races.size();" + FMilaUtility.LINE_SEPARATOR +
          "for (k = 0; k < count; k++) {" + FMilaUtility.LINE_SEPARATOR +
          "   var race = races.get(k);" + FMilaUtility.LINE_SEPARATOR +
          "   var time = race.getTime();" + FMilaUtility.LINE_SEPARATOR +
          "   var result = race.getControlCount('OK');" + FMilaUtility.LINE_SEPARATOR +
          "" + FMilaUtility.LINE_SEPARATOR +
          "   race.status = 'OK' // race is always ok" + FMilaUtility.LINE_SEPARATOR +
          "   if (time > timeLimit) {" + FMilaUtility.LINE_SEPARATOR +
          "      var minutes = Math.ceil(Math.max(0,time - timeLimit)/60/1000);" + FMilaUtility.LINE_SEPARATOR +
          "      result = Math.max(0,result + (minutes*penalty));" + FMilaUtility.LINE_SEPARATOR +
          "   }" + FMilaUtility.LINE_SEPARATOR +
          "   race.result = result;" + FMilaUtility.LINE_SEPARATOR +
          "}";
    }

    @Override
    public Long getFormatUid() {
      return RankingFormatCodeType.PointsCode.ID;
    }

    @Override
    public Long getDecimalPlaces() {
      return 0L;
    }

    @Override
    public Long getSortingUid() {
      return BibNoOrderCodeType.DescendingCode.ID;
    }

    @Override
    public RankingType getRankingType() {
      return RankingType.EVENT;
    }

  }

  /**
   * Score event with mandatory amount of controls, ranking by time
   */
  @Order(50.0)
  public static class ScatterFormulaCode extends AbstractFormulaCode {

    private static final long serialVersionUID = 1L;
    public static final Long ID = 2304L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("ScatterFormula");
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public String getFormula() {
      return "var requiredControls = 10; // change to minimum required controls" + FMilaUtility.LINE_SEPARATOR +
          "" + FMilaUtility.LINE_SEPARATOR +
          "var count = races.size();" + FMilaUtility.LINE_SEPARATOR +
          "for (k = 0; k < count; k++) {" + FMilaUtility.LINE_SEPARATOR +
          "   var race = races.get(k);" + FMilaUtility.LINE_SEPARATOR +
          "   var controlCount = race.getControlCount('OK'); // all punched controls" + FMilaUtility.LINE_SEPARATOR +
          "   if (controlCount >= requiredControls) { " + FMilaUtility.LINE_SEPARATOR +
          "      if (race.status == 'MP') {" + FMilaUtility.LINE_SEPARATOR +
          "         race.status = 'OK';" + FMilaUtility.LINE_SEPARATOR +
          "      }" + FMilaUtility.LINE_SEPARATOR +
          "   } else {" + FMilaUtility.LINE_SEPARATOR +
          "      race.status = 'MP';" + FMilaUtility.LINE_SEPARATOR +
          "   }" + FMilaUtility.LINE_SEPARATOR +
          "   race.result = race.getTime();" + FMilaUtility.LINE_SEPARATOR +
          "}";
    }

    @Override
    public Long getFormatUid() {
      return RankingFormatCodeType.TimeCode.ID;
    }

    @Override
    public Long getDecimalPlaces() {
      return 0L;
    }

    @Override
    public Long getSortingUid() {
      return BibNoOrderCodeType.AscendingCode.ID;
    }

    @Override
    public RankingType getRankingType() {
      return RankingType.EVENT;
    }

  }

  /**
   * Score event with mandatory amount of controls, ranking by time
   */
  @Order(60.0)
  public static class ScatterPenaltyFormulaCode extends AbstractFormulaCode {

    private static final long serialVersionUID = 1L;
    public static final Long ID = 2305L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("ScatterPenaltyFormula");
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public String getFormula() {
      return "var requiredControls = 10; // change to minimum required controls" + FMilaUtility.LINE_SEPARATOR +
          "var penalty = 300 * 1000; // change to desired penalty (currently 300 seconds per control)" + FMilaUtility.LINE_SEPARATOR +
          "" + FMilaUtility.LINE_SEPARATOR +
          "var count = races.size();" + FMilaUtility.LINE_SEPARATOR +
          "for (k = 0; k < count; k++) {" + FMilaUtility.LINE_SEPARATOR +
          "   var race = races.get(k);" + FMilaUtility.LINE_SEPARATOR +
          "   var time = parseInt(race.getTime());" + FMilaUtility.LINE_SEPARATOR +
          "   var controlCount = race.getControlCount('OK'); // all punched controls" + FMilaUtility.LINE_SEPARATOR +
          "   if (controlCount < requiredControls) { " + FMilaUtility.LINE_SEPARATOR +
          "      time = time + penalty * (requiredControls - controlCount);" + FMilaUtility.LINE_SEPARATOR +
          "   }" + FMilaUtility.LINE_SEPARATOR +
          "   race.result = time;" + FMilaUtility.LINE_SEPARATOR +
          "}";
    }

    @Override
    public Long getFormatUid() {
      return RankingFormatCodeType.TimeCode.ID;
    }

    @Override
    public Long getDecimalPlaces() {
      return 0L;
    }

    @Override
    public Long getSortingUid() {
      return BibNoOrderCodeType.AscendingCode.ID;
    }

    @Override
    public RankingType getRankingType() {
      return RankingType.EVENT;
    }

  }

  /**
   * Rogaining (free order, score per control, time penalty, maximum overdue time)
   */
  @Order(70.0)
  public static class RogainingFormulaCode extends AbstractFormulaCode {

    private static final long serialVersionUID = 1L;
    public static final Long ID = 2306L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("RogainingFormula");
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public String getFormula() {
      return "" +
          "var timeLimit = 24 * 60 * 60 * 1000; // change to desired time limit (currently 24 hours)" + FMilaUtility.LINE_SEPARATOR +
          "var maxOverdue = 30 * 60 * 1000; // maximum overdue time (currently 30 minutes)" + FMilaUtility.LINE_SEPARATOR +
          "var penalty = -50; // change to desired penalty (currently 50 points per minute)" + FMilaUtility.LINE_SEPARATOR +
          "" + FMilaUtility.LINE_SEPARATOR +
          "// set the scores for each control" + FMilaUtility.LINE_SEPARATOR +
          "var points = {};" + FMilaUtility.LINE_SEPARATOR +
          "points['31'] = 10;" + FMilaUtility.LINE_SEPARATOR +
          "points['32'] = 50;" + FMilaUtility.LINE_SEPARATOR +
          "points['33'] = 90;" + FMilaUtility.LINE_SEPARATOR +
          "points['34'] = 60;" + FMilaUtility.LINE_SEPARATOR +
          "points['35'] = 30;" + FMilaUtility.LINE_SEPARATOR +
          "points['36'] = 50;" + FMilaUtility.LINE_SEPARATOR +
          "points['37'] = 40;" + FMilaUtility.LINE_SEPARATOR +
          "points['38'] = 80;" + FMilaUtility.LINE_SEPARATOR +
          "points['39'] = 70;" + FMilaUtility.LINE_SEPARATOR +
          "points['40'] = 40;" + FMilaUtility.LINE_SEPARATOR +
          "" + FMilaUtility.LINE_SEPARATOR +
          "var count = races.size();" + FMilaUtility.LINE_SEPARATOR +
          "for (k = 0; k < count; k++) {" + FMilaUtility.LINE_SEPARATOR +
          "   var race = races.get(k);" + FMilaUtility.LINE_SEPARATOR +
          "   var time = parseInt(race.getTime());" + FMilaUtility.LINE_SEPARATOR +
          "   var result = 0;" + FMilaUtility.LINE_SEPARATOR +
          "   var controlCount = race.getControls() != null ? race.getControls().size() : 0;" + FMilaUtility.LINE_SEPARATOR +
          "   for (c=0; c < controlCount; c++) {" + FMilaUtility.LINE_SEPARATOR +
          "      var control = race.getControls().get(c);" + FMilaUtility.LINE_SEPARATOR +
          "      if (control.getStatus() == 'OK') {" + FMilaUtility.LINE_SEPARATOR +
          "         var controlPoints = points[control.getControlNo()];" + FMilaUtility.LINE_SEPARATOR +
          "         if (!isNaN(controlPoints)) {" + FMilaUtility.LINE_SEPARATOR +
          "            result = result + controlPoints;" + FMilaUtility.LINE_SEPARATOR +
          "         }" + FMilaUtility.LINE_SEPARATOR +
          "      }" + FMilaUtility.LINE_SEPARATOR +
          "   } " + FMilaUtility.LINE_SEPARATOR +
          "   if (time > (timeLimit + maxOverdue)) {" + FMilaUtility.LINE_SEPARATOR +
          "      race.status = 'DISQ';" + FMilaUtility.LINE_SEPARATOR +
          "   }" + FMilaUtility.LINE_SEPARATOR +
          "   if (time > timeLimit) { " + FMilaUtility.LINE_SEPARATOR +
          "      var minutes = Math.ceil(Math.max(0,time - timeLimit)/60/1000);" + FMilaUtility.LINE_SEPARATOR +
          "      result = Math.max(0,result + (minutes*penalty));" + FMilaUtility.LINE_SEPARATOR +
          "   }" + FMilaUtility.LINE_SEPARATOR +
          "   race.result = result;" + FMilaUtility.LINE_SEPARATOR +
          "}";
    }

    @Override
    public Long getFormatUid() {
      return RankingFormatCodeType.PointsCode.ID;
    }

    @Override
    public Long getDecimalPlaces() {
      return 0L;
    }

    @Override
    public Long getSortingUid() {
      return BibNoOrderCodeType.AscendingCode.ID;
    }

    @Override
    public RankingType getRankingType() {
      return RankingType.EVENT;
    }

  }

  @Order(100.0)
  public static class SumTimeCode extends AbstractFormulaCode {

    private static final long serialVersionUID = 1L;
    public static final Long ID = 2350L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("RankingSummaryFormulaSumTime");
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public RankingType getRankingType() {
      return RankingType.SUMMARY;
    }

    @Override
    public String getFormula() {
      return "" +
          "var count = races.size();" + FMilaUtility.LINE_SEPARATOR +
          "for (k = 0; k < count; k++) {" + FMilaUtility.LINE_SEPARATOR +
          "   var race = races.get(k);" + FMilaUtility.LINE_SEPARATOR +
          "   race.result = race.getEventResultsSum(race.getEventRankings().size());" + FMilaUtility.LINE_SEPARATOR +
          "   race.setStatus(race.getEventStatus());" + FMilaUtility.LINE_SEPARATOR +
          "}";
    }

    @Override
    public Long getFormatUid() {
      return RankingFormatCodeType.TimeCode.ID;
    }

    @Override
    public Long getDecimalPlaces() {
      return 0L;
    }

    @Override
    public Long getSortingUid() {
      return BibNoOrderCodeType.AscendingCode.ID;
    }
  }

  @Order(110.0)
  public static class SumPointsCode extends AbstractFormulaCode {

    private static final long serialVersionUID = 1L;
    public static final Long ID = 2351L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("RankingSummaryFormulaSumPoints");
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public RankingType getRankingType() {
      return RankingType.SUMMARY;
    }

    @Override
    public String getFormula() {
      return "" +
          "var bestEventCount = Math.max(eventCount-1,1);" + FMilaUtility.LINE_SEPARATOR +
          "var count = races.size();" + FMilaUtility.LINE_SEPARATOR +
          "for (k = 0; k < count; k++) {" + FMilaUtility.LINE_SEPARATOR +
          "   var race = races.get(k);" + FMilaUtility.LINE_SEPARATOR +
          "   race.result = race.getEventResultsSum(bestEventCount);" + FMilaUtility.LINE_SEPARATOR +
          "   race.setStatus('OK');" + FMilaUtility.LINE_SEPARATOR +
          "}";
    }

    @Override
    public Long getFormatUid() {
      return RankingFormatCodeType.PointsCode.ID;
    }

    @Override
    public Long getDecimalPlaces() {
      return 0L;
    }

    @Override
    public Long getSortingUid() {
      return BibNoOrderCodeType.DescendingCode.ID;
    }
  }

  @Order(999.0)
  public static class CustomFormulaCode extends AbstractFormulaCode {

    private static final long serialVersionUID = 1L;
    public static final Long ID = 2399L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("CustomFormula");
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public String getFormula() {
      return "";
    }

    @Override
    public Long getFormatUid() {
      return null;
    }

    @Override
    public Long getDecimalPlaces() {
      return null;
    }

    @Override
    public Long getSortingUid() {
      return null;
    }

    @Override
    public RankingType getRankingType() {
      return null;
    }

  }

}
