package com.rtiming.server;

public final class FMilaServerUtility {

  private FMilaServerUtility() {
  }

  public static boolean isTestEnvironment() {
// TODO MIG    
//    Bundle server = Platform.getBundle(com.rtiming.server.Activator.PLUGIN_ID);
//    Bundle[] fragments = Platform.getFragments(server);
//    if (fragments != null) {
//      for (Bundle fragment : fragments) {
//        if (fragment.getSymbolicName().contains("test")) {
//          return true;
//        }
//      }
//    }
    return false;
  }

}
