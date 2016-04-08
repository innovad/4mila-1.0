// TODO MIG
//package com.rtiming.server;
//
//import org.eclipse.scout.rt.platform.Platform;
//import org.eclipse.scout.rt.server.ServiceTunnelServlet;
//import org.eclipse.scout.rt.shared.servicetunnel.ServiceTunnelRequest;
//import org.eclipse.scout.rt.shared.servicetunnel.ServiceTunnelResponse;
//
//public class FMilaServiceTunnelServlet extends ServiceTunnelServlet {
//
//  private static final long serialVersionUID = 1L;
//
//  @Override
//  protected ServiceTunnelResponse runServerJobTransactionWithDelegate(ServiceTunnelRequest req, Bundle[] loaderBundles, Version requestMinVersion, boolean debug) throws Exception {
//    return new FMilaTransactionDelegate(loaderBundles, requestMinVersion, debug).invoke(req);
//  }
//
//  @Override
//  protected Version initRequestMinVersion(ServletConfig config) {
//    // improve scout version check by checking also the qualifier part of the version string
//    return Platform.getProduct().getDefiningBundle().getVersion();
//  }
//
//}
