package com.rtiming.server.common.web;

public class ColumnDefinition {

  private final String name;
  private final int type;

  public ColumnDefinition(String name, int type) {
    super();
    this.name = name;
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public int getType() {
    return type;
  }

  @Override
  public String toString() {
    return "ColumnDefinition [name=" + name + ", type=" + type + "]";
  }

}
