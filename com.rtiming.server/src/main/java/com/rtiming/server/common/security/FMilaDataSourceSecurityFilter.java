package com.rtiming.server.common.security;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.scout.rt.platform.security.SimplePrincipal;
import org.eclipse.scout.rt.server.commons.servlet.filter.authentication.DataSourceSecurityFilter;
import org.eclipse.scout.rt.server.commons.servlet.filter.authentication.PrincipalHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rtiming.shared.FMilaUtility;

public class FMilaDataSourceSecurityFilter extends DataSourceSecurityFilter {

  private static final Logger LOG = LoggerFactory.getLogger(DataSourceSecurityFilter.class);

  @Override
  protected int negotiate(HttpServletRequest req, HttpServletResponse resp, PrincipalHolder holder) throws IOException, ServletException {
    if (isFirstLogin()) {
      holder.setPrincipal(new SimplePrincipal(FMilaUtility.ADMIN_USER));
      return STATUS_CONTINUE_WITH_PRINCIPAL;
    }
    return super.negotiate(req, resp, holder);
  }

  protected boolean isFirstLogin() throws ServletException {
    Connection databaseConnection = null;
    PreparedStatement stmt = null;
    try {

      databaseConnection = createJdbcDirectConnection();

      stmt = databaseConnection.prepareStatement("SELECT COUNT(1) FROM RT_USER");
      stmt.execute();
      ResultSet resultSet = stmt.getResultSet();
      return (resultSet.next() && resultSet.getLong(1) == 0);
    }
    catch (Exception e) {
      LOG.error("Cannot SELECT user/pass.", e);
      throw new ServletException(e.getMessage(), e);
    }
    finally {
      try {
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
      }
      catch (SQLException e) {
        LOG.warn("Exception in close stmt!", e);
      }
      try {
        if (databaseConnection != null) {
          databaseConnection.close();
          databaseConnection = null;
        }
      }
      catch (SQLException e) {
        LOG.warn("Exception in close connection!", e);
      }
    }
  }

}
