package com.rtiming.server.common.web;

import java.util.HashMap;

public final class UploadConstraints {

  private final HashMap<String, String> constraints = new HashMap<String, String>();
  private static final UploadConstraints INSTANCE = new UploadConstraints();

  private UploadConstraints() {
    addConstraint("RT_CLIENT", "AND CLIENT_NR = :uploadClientNr");
    addConstraint("RT_EVENT", "AND EVENT_NR = :eventNr AND CLIENT_NR = :uploadClientNr");
    addConstraint("RT_RACE", "AND CLIENT_NR = :uploadClientNr AND EVENT_NR = :eventNr");
    addConstraint("RT_RACE_CONTROL", "AND CLIENT_NR = :uploadClientNr AND RACE_NR IN (SELECT RACE_NR FROM RT_RACE " + getConstraintWhere("RT_RACE") + ")");
    addConstraint("RT_PARTICIPATION", "AND CLIENT_NR = :uploadClientNr AND EVENT_NR = :eventNr");
    addConstraint("RT_COURSE", "AND EVENT_NR = :eventNr AND CLIENT_NR = :uploadClientNr");
    addConstraint("RT_RUNNER", "AND CLIENT_NR = :uploadClientNr AND RUNNER_NR IN (SELECT RUNNER_NR FROM RT_RACE R " + getConstraintWhere("RT_RACE") + ")");
    addConstraint("RT_CITY", "AND CLIENT_NR = :uploadClientNr AND CITY_NR IN (SELECT CITY_NR FROM RT_RACE R " + getConstraintWhere("RT_RACE") + " UNION SELECT CITY_NR FROM RT_RUNNER " + getConstraintWhere("RT_RUNNER") + ")");
    addConstraint("RT_COUNTRY", "AND CLIENT_NR = :uploadClientNr AND COUNTRY_UID IN (SELECT COUNTRY_UID FROM RT_CITY C " + getConstraintWhere("RT_CITY") + " UNION SELECT NATION_UID FROM RT_RACE R " + getConstraintWhere("RT_RACE") + ")");
    addConstraint("RT_UC", "AND CLIENT_NR = :uploadClientNr AND UC_UID IN (SELECT STARTBLOCK_UID FROM RT_EVENT_STARTBLOCK WHERE EVENT_NR = :eventNr AND CLIENT_NR = :uploadClientNr UNION SELECT CLASS_UID FROM RT_EVENT_CLASS WHERE EVENT_NR = :eventNr " + "UNION SELECT COUNTRY_UID FROM RT_COUNTRY " + getConstraintWhere("RT_COUNTRY") + ")");
    addConstraint("RT_UCL", getConstraint("RT_UC"));
    addConstraint("RT_EVENT_CLASS", "AND CLIENT_NR = :uploadClientNr AND EVENT_NR = :eventNr");
    addConstraint("RT_CONTROL", "AND CLIENT_NR = :uploadClientNr AND EVENT_NR = :eventNr");
    addConstraint("RT_COURSE_CONTROL", "AND CLIENT_NR = :uploadClientNr AND COURSE_NR IN (SELECT COURSE_NR FROM RT_COURSE WHERE CLIENT_NR = :uploadClientNr AND EVENT_NR = :eventNr)");
    addConstraint("RT_ENTRY", "AND CLIENT_NR = :uploadClientNr AND ENTRY_NR IN (SELECT ENTRY_NR FROM RT_PARTICIPATION P " + getConstraintWhere("RT_PARTICIPATION") + ")");
    addConstraint("RT_ECARD", "AND CLIENT_NR = :uploadClientNr AND ECARD_NR IN (SELECT ECARD_NR FROM RT_RACE R " + getConstraintWhere("RT_RACE") + " UNION SELECT ECARD_NR FROM RT_RUNNER " + getConstraintWhere("RT_RUNNER") + " )");
    addConstraint("RT_CLUB", "AND CLIENT_NR = :uploadClientNr AND CLUB_NR IN (SELECT CLUB_NR FROM RT_RACE RA " + getConstraintWhere("RT_RACE") + " UNION SELECT CLUB_NR FROM RT_RUNNER " + getConstraintWhere("RT_RUNNER") + ")");
    addConstraint("RT_ADDRESS", "AND CLIENT_NR = :uploadClientNr AND ADDRESS_NR IN (SELECT ADDRESS_NR FROM RT_RUNNER " + getConstraintWhere("RT_RUNNER") + " UNION SELECT ADDRESS_NR FROM RT_RACE " + getConstraintWhere("RT_RACE") + ")");
    addConstraint("RT_EVENT_MAP", "AND CLIENT_NR = :uploadClientNr AND EVENT_NR = :eventNr");
    addConstraint("RT_MAP", "AND CLIENT_NR = :uploadClientNr AND MAP_NR IN (SELECT MAP_NR FROM RT_EVENT_MAP EM " + getConstraintWhere("RT_EVENT_MAP") + ")");
    addConstraint("RT_EVENT_STARTBLOCK", "AND CLIENT_NR = :uploadClientNr AND EVENT_NR = :eventNr");
  }

  public static UploadConstraints getInstance() {
    return INSTANCE;
  }

  private void addConstraint(String tableName, String where) {
    constraints.put(tableName.toLowerCase(), where);
  }

  public String getConstraint(String tableName) {
    return constraints.get(tableName.toLowerCase()) != null ? constraints.get(tableName.toLowerCase()) : "";
  }

  private String getConstraintWhere(String tableName) {
    return "WHERE 1=1 " + getConstraint(tableName);
  }

}
