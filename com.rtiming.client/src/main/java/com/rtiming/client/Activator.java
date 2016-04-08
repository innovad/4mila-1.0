//package com.rtiming.client;
//
//import com.rtiming.serial.SerialLibrary;
//import com.rtiming.serial.SerialUtility;
//import com.rtiming.shared.FMilaUtility;
//import com.rtiming.shared.FMilaUtility.OperatingSystem;
//
///**
// * The activator class controls the plug-in life cycle
// */
//public class Activator extends Plugin {
//
//  // The plug-in ID
//  public static final String PLUGIN_ID = "com.rtiming.client";
//
//  // The shared instance
//  private static Activator plugin;
//
//  /**
//   * The constructor
//   */
//  public Activator() {
//  }
//
//  @Override
//  public void start(BundleContext context) throws Exception {
//    super.start(context);
//    plugin = this;
//
//    // on Mac OS X, use PureJavaComm for Serial Communication
//    if (OperatingSystem.MACOSX.equals(FMilaUtility.getPlatform())) {
//      SerialUtility.setLibrary(SerialLibrary.PURE);
//    }
//  }
//
//  @Override
//  public void stop(BundleContext context) throws Exception {
//    plugin = null;
//    super.stop(context);
//  }
//
//  /**
//   * Returns the shared instance
//   * 
//   * @return the shared instance
//   */
//  public static Activator getDefault() {
//    return plugin;
//  }
//
//}
