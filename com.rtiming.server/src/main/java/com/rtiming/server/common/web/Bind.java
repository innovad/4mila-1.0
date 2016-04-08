package com.rtiming.server.common.web;

public class Bind {

  private final String name;
  private final Object value;
  private final int sqlType;

  public Bind(String name, Object value, int sqlType) {
    super();
    this.name = name;
    this.value = value;
    this.sqlType = sqlType;
  }

  public String getName() {
    return name;
  }

  public Object getValue() {
    return value;
  }

  public int getSqlType() {
    return sqlType;
  }

}
