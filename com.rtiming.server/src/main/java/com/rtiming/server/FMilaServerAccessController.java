package com.rtiming.server;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.server.commons.authentication.AnonymousAccessController;
import org.eclipse.scout.rt.server.commons.authentication.AnonymousAccessController.AnonymousAuthConfig;
import org.eclipse.scout.rt.server.commons.authentication.IAccessController;

public class FMilaServerAccessController implements IAccessController {

  private final AnonymousAccessController m_anonymousAccessController = BEANS.get(AnonymousAccessController.class);
  private final AnonymousAuthConfig m_config = new AnonymousAuthConfig();

  public FMilaServerAccessController init() {
    m_anonymousAccessController.init(m_config.withEnabled(true).withUsername("admin"));
    return this;
  }

  @Override
  public boolean handle(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
    return m_anonymousAccessController.handle(request, response, chain);
  }

  @Override
  public void destroy() {
    m_anonymousAccessController.destroy();
  }

}
