// TODO MIG
//package com.rtiming.server;
//
//import org.eclipse.scout.commons.logger.IScoutLogger;
//import org.eclipse.scout.commons.logger.ScoutLogManager;
//
//import com.rtiming.server.common.database.DatabaseService;
//import com.rtiming.shared.FMilaUtility;
//import com.rtiming.shared.FMilaUtility.OperatingSystem;
//
//public class ServerApplication {
//  private static Logger logger = LoggerFactory.getLogger(ServerApplication.class);
//
//  @Override
//  public Object start(IApplicationContext context) throws Exception {
//    // init the client/server application
//    DatabaseService svc = new DatabaseService();
//    svc.setupApplication();
//
//    // log to app console
//    System.out.println("******************************************************************");
//    System.out.println("* 4mila Server started.                                          *");
//    if (OperatingSystem.MACOSX.equals(FMilaUtility.getPlatform())) {
//      System.out.println("* Click the 'Cancel' button to STOP and quit the 4mila server    *");
//    }
//    System.out.println("******************************************************************");
//
//    return EXIT_OK;
//  }
//
//  @Override
//  public void stop() {
//    System.out.println("STOP");
//  }
//
//}
