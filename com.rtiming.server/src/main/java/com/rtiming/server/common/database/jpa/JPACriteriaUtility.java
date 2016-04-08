package com.rtiming.server.common.database.jpa;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;

import org.eclipse.scout.commons.StringUtility;

import com.rtiming.shared.dao.RtRunner;
import com.rtiming.shared.dao.RtRunner_;
import com.rtiming.shared.race.TimePrecisionCodeType;

public final class JPACriteriaUtility {

  private JPACriteriaUtility() {
  }

  public static Expression<String> runnerNameJPA(Path<RtRunner> runner) {
    CriteriaBuilder b = JPA.getCriteriaBuilder();
    return b.coalesce(b.concat(runner.get(RtRunner_.lastName), b.concat(", ", runner.get(RtRunner_.firstName))), runner.get(RtRunner_.lastName));
  }

  public static Expression<String> runnerNameTokenPlain(Path<RtRunner> runner) {
    CriteriaBuilder b = JPA.getCriteriaBuilder();
    Expression<String> runnerNameToken = b.concat(runner.get(RtRunner_.lastName),
        b.concat(b.coalesce(runner.get(RtRunner_.firstName), ""), runner.get(RtRunner_.lastName))
        );
    return runnerNameToken;
  }

  public static Expression<String> runnerNameTokenDefaultsRemoved(Path<RtRunner> runner) {
    Expression<String> runnerNameToken = removeDefaultTokens(runnerNameTokenPlain(runner));
    return runnerNameToken;
  }

  public static Expression<String> removeDefaultTokens(Expression<String> string) {
    return removeTokens(string, ",", " ", "(", ")", "-", ".");
  }

  protected static Expression<String> replace(Expression<String> string, String searchString, String replacementString) {
    CriteriaBuilder b = JPA.getCriteriaBuilder();
    return b.function(
        "replace", String.class,
        string,
        b.literal(searchString),
        b.literal(replacementString)
        );
  }

  private static Expression<String> removeTokens(Expression<String> string, String... tokens) {
    for (String token : tokens) {
      string = replace(string, token, "");
    }
    return string;
  }

  public static String removeDefaultTokens(String s) {
    return removeTokens(s, ",", " ", "(", ")", "-", ".");
  }

  private static String removeTokens(String s, String... tokens) {
    for (String token : tokens) {
      s = StringUtility.replace(s, token, "");
    }
    return s;
  }

  public static String formatTimeAsStringSQL(String databaseColumn, String timePrecisionColumn) {
    // make sure the zero's are correctly formatted, e.g. 0:00, 0:01, 12:00, 1:00:25
    // convert negative times to non-negative times
    String timeNonNegative = "ABS((" + databaseColumn + "))";
    String convertToString = " REPLACE(REPLACE(LTRIM(REPLACE(REPLACE(LTRIM(LTRIM(TO_CHAR((" + timeNonNegative + ") * interval '1 millisecond','HH24:MI:SS.MS'),'0'),':'),':00:','AtempA'),'00:','BtempB'),'0'),'BtempB','0:'),'AtempA',':00:') ";
    String formatted = convertToString;
    formatted = " CASE WHEN (" + timePrecisionColumn + ")=" + TimePrecisionCodeType.Precision1sCode.ID + " THEN SUBSTRING(" + convertToString + " FROM 1 FOR CHAR_LENGTH(" + convertToString + ")-4) ELSE (" + formatted + ") END ";
    formatted = " CASE WHEN (" + timePrecisionColumn + ")=" + TimePrecisionCodeType.Precision10sCode.ID + " THEN SUBSTRING(" + convertToString + " FROM 1 FOR CHAR_LENGTH(" + convertToString + ")-2) ELSE (" + formatted + ") END ";
    formatted = " CASE WHEN (" + timePrecisionColumn + ")=" + TimePrecisionCodeType.Precision100sCode.ID + " THEN SUBSTRING(" + convertToString + " FROM 1 FOR CHAR_LENGTH(" + convertToString + ")-1) ELSE (" + formatted + ") END ";
    // add '-' as prefix if time is negative
    String sql = " (CASE WHEN (" + timeNonNegative + ")=(" + databaseColumn + ") THEN '' ELSE '-' END) ";
    return sql + "||" + formatted;
  }

}
