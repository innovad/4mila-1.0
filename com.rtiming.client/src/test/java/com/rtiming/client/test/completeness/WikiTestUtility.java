package com.rtiming.client.test.completeness;

import java.io.File;

/**
 * @author amo
 */
public class WikiTestUtility {

  public static String getRoot() {
    File workingDir = new File(".");
    System.out.println("Working Directory: " + workingDir.getAbsolutePath());

    File surefireDir = new File(workingDir.getAbsolutePath() + "./doc/data/pages"); // workingDir has already a dot
    System.out.println("Surefire Directory: " + surefireDir.getAbsolutePath());
    if (surefireDir.exists() && surefireDir.isDirectory()) {
      return surefireDir.getAbsolutePath();
    }

    // C:\Program Files (x86)\EasyPHP-DevServer-13.1VC9\data\localweb\doc\data\pages
    File devDir = new File("C:/Program Files (x86)/EasyPHP-DevServer-13.1VC9/data/localweb/doc/data/pages");
    System.out.println("Dev Directory: " + devDir.getAbsolutePath());

    return devDir.getAbsolutePath();
  }

}
