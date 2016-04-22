/*******************************************************************************
 * Copyright (c) 2010 BSI Business Systems Integration AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the BSI CRM Software License v1.0
 * which accompanies this distribution as bsi-v10.html
 *
 * Contributors:
 *     BSI Business Systems Integration AG - initial API and implementation
 ******************************************************************************/
package com.rtiming.server.common.security;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.shared.FMilaUtility.Architecture;
import com.rtiming.shared.common.security.Download;

import org.junit.Assert;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class UpdateServiceTest {

  @Test
  public void testParseDownloadObject1() throws Exception {
    UpdateService service = BEANS.get(UpdateService.class);
    Download result = service.parseDownloadObject("4mila-1.0.0.201205131046-win32-install.exe", 4444444);
    Assert.assertTrue("Version match", result.getVersion().startsWith("1.0.0"));
    Assert.assertEquals("File match", "4mila-1.0.0.201205131046-win32-install.exe", result.getFile());
    Assert.assertTrue("Architecture match", result.getArchitecture().equals(Architecture.WIN32));
    Assert.assertEquals("Size match", "4MB", result.getSize());
  }

  @Test
  public void testParseDownloadObject2() throws Exception {
    UpdateService service = BEANS.get(UpdateService.class);
    Download result = service.parseDownloadObject("4mila-1.0.0.201205131046-win64-install.exe", 4444444);
    Assert.assertTrue("Version match", result.getVersion().startsWith("1.0.0"));
    Assert.assertEquals("File match", "4mila-1.0.0.201205131046-win64-install.exe", result.getFile());
    Assert.assertTrue("Architecture match", result.getArchitecture().equals(Architecture.WIN64));
  }

  @Test
  public void testParseDownloadObjectDate() throws Exception {
    UpdateService service = BEANS.get(UpdateService.class);
    Download result = service.parseDownloadObject("4mila-1.0.0.201205131046-win64-install.exe", 4444444);
    Assert.assertEquals("File match", "201205131046", DateUtility.format(result.getDate(), "yyyyMMddHHmm"));
  }

  @Test
  public void testParseDownloadObjectUnknownOperatingSystem() throws Exception {
    UpdateService service = BEANS.get(UpdateService.class);
    Download result = service.parseDownloadObject("4mila-1.0.0.201205131046-DUMMY-install.exe", 4444444);
    Assert.assertTrue("Version match", result.getVersion().startsWith("1.0.0"));
    Assert.assertEquals("File match", "4mila-1.0.0.201205131046-DUMMY-install.exe", result.getFile());
    Assert.assertTrue("Architecture match", result.getArchitecture().equals(Architecture.UNKNOWN));
  }

  @Test(expected = ProcessingException.class)
  public void testParseDownloadObjectInvalid() throws Exception {
    UpdateService service = BEANS.get(UpdateService.class);
    Download result = service.parseDownloadObject("", 0);
    Assert.assertEquals("File match", "201205131046", DateUtility.format(result.getDate(), "yyyyMMddHHmm"));
  }

}
