// TODO MIG
//package com.rtiming.server;
//
//import java.lang.reflect.Method;
//import java.util.regex.Pattern;
//
//import org.eclipse.scout.rt.platform.exception.ProcessingException;
//import org.eclipse.scout.rt.platform.exception.VetoException;
//import org.eclipse.scout.rt.shared.ScoutTexts;
//import org.eclipse.scout.rt.shared.servicetunnel.ServiceTunnelRequest;
//import org.eclipse.scout.rt.shared.servicetunnel.ServiceTunnelResponse;
//
//import com.rtiming.shared.FMilaUtility;
//
//public class FMilaTransactionDelegate extends DefaultTransactionDelegate {
//
//  public static final Pattern FMILA_PROCESS_NAMES_PATTERN = Pattern.compile("(set|put|add|remove|store|write|create|prepareCreate|insert|update|delete)([A-Z].*)?");
//  private final Version requestMinVersion;
//
//  /**
//   * @param loaderBundles
//   * @param requestMinVersion
//   * @param debug
//   */
//  public FMilaTransactionDelegate(Bundle[] loaderBundles, Version requestMinVersion, boolean debug) {
//    super(loaderBundles, requestMinVersion, debug);
//    this.requestMinVersion = requestMinVersion;
//  }
//
//  @Override
//  protected Class<? extends IValidationStrategy> findInputValidationStrategyByPolicy(Object serviceImpl, Method op) {
//    if (FMILA_PROCESS_NAMES_PATTERN.matcher(op.getName()).matches()) {
//      return IValidationStrategy.PROCESS.class;
//    }
//    return super.findInputValidationStrategyByPolicy(serviceImpl, op);
//  }
//
//  @Override
//  protected ServiceTunnelResponse invokeImpl(ServiceTunnelRequest serviceReq) throws Throwable {
//    if (requestMinVersion != null) {
//      String v = serviceReq.getVersion();
//      if (v == null) {
//        v = "0.0.0";
//      }
//      Version requestVersion = Version.parseVersion(v);
//      if (requestVersion.compareTo(requestMinVersion) != 0) {
//        ServiceTunnelResponse serviceRes = new ServiceTunnelResponse(null, null, new VersionMismatchException(requestVersion.toString(), requestMinVersion.toString()));
//        return serviceRes;
//      }
//    }
//    return super.invokeImpl(serviceReq);
//  }
//
//  @Override
//  protected Throwable replaceOutboundException(Throwable t) {
//    Throwable p;
//    if (t instanceof VetoException) {
//      VetoException ve = (VetoException) t;
//      p = new VetoException(ve.getStatus().getTitle(), ve.getMessage());
//    }
//    else {
//      p = new ProcessingException(ScoutTexts.get("RequestProblem"), FMilaUtility.getStackTrace(t));
//    }
//    return p;
//  }
//
//}
