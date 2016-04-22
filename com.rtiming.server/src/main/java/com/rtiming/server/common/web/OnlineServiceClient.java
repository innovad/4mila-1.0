// TODO MIG
//package com.rtiming.server.common.web;
//
//import java.net.URL;
//
//import org.eclipse.scout.rt.platform.logger.IScoutLogger;
//import org.eclipse.scout.rt.platform.logger.ScoutLogManager;
//
//import com._4mila._4mila.jaxws.online.OnlineService;
//import com._4mila._4mila.jaxws.online.OnlineServiceSoap;
//import com.rtiming.server.Activator;
//import com.rtiming.shared.FMilaUtility;
//
//@ScoutWebServiceClient(authenticationHandler = IAuthenticationHandler.NONE.class)
//public class OnlineServiceClient extends AbstractWebServiceClient<OnlineService, OnlineServiceSoap> {
//
//  private static Logger logger = LoggerFactory.getLogger(OnlineServiceClient.class);
//
//  @Override
//  public URL getConfiguredWsdlLocation() {
//    try {
//      return FMilaUtility.findFileLocation("WEB-INF/wsdl/consumer/online.wsdl", "");
//    }
//    catch (Exception e) {
//      logger.warn("Failed opening WSDL: " + e.getMessage());
//      return null;
//    }
//  }
//
//}
