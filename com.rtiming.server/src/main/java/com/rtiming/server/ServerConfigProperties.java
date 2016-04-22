package com.rtiming.server;

import org.eclipse.scout.rt.platform.config.AbstractStringConfigProperty;

public final class ServerConfigProperties {
  private ServerConfigProperties() {
  }

  public static class DatabaseJdbcMappingNameProperty extends AbstractStringConfigProperty {

    @Override
    protected String getDefaultValue() {
      return "system";
    }

    @Override
    public String getKey() {
      return "jdbcMappingName";
    }
  }
}
