package com.rtiming.server.common.infodisplay;

import java.io.File;

import org.eclipse.scout.rt.platform.util.IOUtility;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.shared.FMilaUtility;

/**
 * @author amo
 */
@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class InfoDisplayFilesInstallerTest {

  @Test
  public void testCopy() throws Exception {
    File dir = IOUtility.createTempDirectory("temp");
    String version = "1.0";
    boolean copied = InfoDisplayFilesInstaller.installFiles(dir.getAbsolutePath(), version);

    System.out.println("Files copied to " + dir);
    Assert.assertTrue("Files have been copied", copied);
  }

  @Test
  public void testAlreadyExists() throws Exception {
    File dir = IOUtility.createTempDirectory("temp2");
    String version = "2.0";
    IOUtility.createDirectory(dir.getAbsolutePath() + FMilaUtility.FILE_SEPARATOR + "infodisplay" + FMilaUtility.FILE_SEPARATOR + version);
    boolean copied = InfoDisplayFilesInstaller.installFiles(dir.getAbsolutePath(), version);

    System.out.println("Files copied to " + dir);
    Assert.assertFalse("Files have been copied", copied);
  }

}
