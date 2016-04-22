package com.rtiming.client.common.report;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.FileUtility;
import org.eclipse.scout.rt.platform.util.IOUtility;
import org.eclipse.scout.rt.shared.services.common.file.RemoteFile;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.dao.RtReportTemplate;
import com.rtiming.shared.dao.RtReportTemplateFile;
import com.rtiming.shared.services.code.ReportTypeCodeType;

/**
 * @author amo
 */
public class TemplateSelectionUtilityTest {

  @Rule
  public ExpectedException expectedEx = ExpectedException.none();

  @Test(expected = IllegalArgumentException.class)
  public void testNull1() throws Exception {
    TemplateSelectionUtility.getTemplateFiles(null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNull2() throws Exception {
    TemplateSelectionUtility.getTemplateFiles(ReportTypeCodeType.DefaultCode.ID, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNull3() throws Exception {
    TemplateSelectionUtility.getTemplateFiles(null, null, new ArrayList<RtReportTemplate>());
  }

  @Test
  public void test1() throws Exception {
    Set<RtReportTemplateFile> result = TemplateSelectionUtility.getTemplateFiles(ReportTypeCodeType.DefaultCode.ID, null, new ArrayList<RtReportTemplate>());
    Assert.assertNull("Result empty", result);
  }

  @Test
  public void testEventNull() throws Exception {
    Set<RtReportTemplateFile> templateFiles = new HashSet<>();
    RtReportTemplate template = createTemplate(ReportTypeCodeType.DefaultCode.ID, null, true, templateFiles);
    ArrayList<RtReportTemplate> configuration = createConfiguration(template);
    Set<RtReportTemplateFile> result = TemplateSelectionUtility.getTemplateFiles(ReportTypeCodeType.DefaultCode.ID, null, configuration);
    Assert.assertNotNull("Result not empty", result);
    Assert.assertEquals("Result ok", templateFiles, result);
  }

  @Test
  public void testEventNotNull() throws Exception {
    Set<RtReportTemplateFile> templateFiles = new HashSet<>();
    RtReportTemplate template = createTemplate(ReportTypeCodeType.DefaultCode.ID, null, true, templateFiles);
    ArrayList<RtReportTemplate> configuration = createConfiguration(template);
    Set<RtReportTemplateFile> result = TemplateSelectionUtility.getTemplateFiles(ReportTypeCodeType.DefaultCode.ID, 4L, configuration);
    Assert.assertNotNull("Result not empty", result);
    Assert.assertEquals("Result ok", templateFiles, result);
  }

  @Test
  public void testMatch1() throws Exception {
    Set<RtReportTemplateFile> templateFiles = new HashSet<>();
    RtReportTemplate template1 = createTemplate(ReportTypeCodeType.ResultsCode.ID, null, true, null);
    RtReportTemplate template2 = createTemplate(ReportTypeCodeType.DefaultCode.ID, null, true, templateFiles);
    ArrayList<RtReportTemplate> configuration = createConfiguration(template1, template2);
    Set<RtReportTemplateFile> result = TemplateSelectionUtility.getTemplateFiles(ReportTypeCodeType.DefaultCode.ID, 4L, configuration);
    Assert.assertNotNull("Result not empty", result);
    Assert.assertEquals("Result ok", templateFiles, result);
  }

  @Test
  public void testMatch2() throws Exception {
    Set<RtReportTemplateFile> templateFiles = new HashSet<>();
    RtReportTemplate template1 = createTemplate(ReportTypeCodeType.DefaultCode.ID, null, true, templateFiles);
    RtReportTemplate template2 = createTemplate(ReportTypeCodeType.DefaultCode.ID, null, true, null);
    ArrayList<RtReportTemplate> configuration = createConfiguration(template1, template2);
    Set<RtReportTemplateFile> result = TemplateSelectionUtility.getTemplateFiles(ReportTypeCodeType.DefaultCode.ID, 4L, configuration);
    Assert.assertNotNull("Result not empty", result);
    Assert.assertEquals("Result ok", templateFiles, result);
  }

  @Test
  public void testMatch3() throws Exception {
    Set<RtReportTemplateFile> templateFiles = new HashSet<>();
    RtReportTemplate template1 = createTemplate(ReportTypeCodeType.DefaultCode.ID, 3L, true, null);
    RtReportTemplate template2 = createTemplate(ReportTypeCodeType.DefaultCode.ID, null, true, templateFiles);
    ArrayList<RtReportTemplate> configuration = createConfiguration(template1, template2);
    Set<RtReportTemplateFile> result = TemplateSelectionUtility.getTemplateFiles(ReportTypeCodeType.DefaultCode.ID, 4L, configuration);
    Assert.assertNotNull("Result not empty", result);
    Assert.assertEquals("Result ok", templateFiles, result);
  }

  @Test
  public void testMatch4() throws Exception {
    Set<RtReportTemplateFile> templateFiles = new HashSet<>();
    RtReportTemplate template1 = createTemplate(ReportTypeCodeType.DefaultCode.ID, null, true, null);
    RtReportTemplate template2 = createTemplate(ReportTypeCodeType.DefaultCode.ID, 4L, true, templateFiles);
    ArrayList<RtReportTemplate> configuration = createConfiguration(template1, template2);
    Set<RtReportTemplateFile> result = TemplateSelectionUtility.getTemplateFiles(ReportTypeCodeType.DefaultCode.ID, 4L, configuration);
    Assert.assertNotNull("Result not empty", result);
    Assert.assertEquals("Result ok", templateFiles, result);
  }

  @Test
  public void testMatch5() throws Exception {
    Set<RtReportTemplateFile> templateFiles = new HashSet<>();
    RtReportTemplate template1 = createTemplate(ReportTypeCodeType.DefaultCode.ID, null, false, null);
    RtReportTemplate template2 = createTemplate(ReportTypeCodeType.DefaultCode.ID, null, true, templateFiles);
    ArrayList<RtReportTemplate> configuration = createConfiguration(template1, template2);
    Set<RtReportTemplateFile> result = TemplateSelectionUtility.getTemplateFiles(ReportTypeCodeType.DefaultCode.ID, 4L, configuration);
    Assert.assertNotNull("Result not empty", result);
    Assert.assertEquals("Result ok", templateFiles, result);
  }

  private RtReportTemplate createTemplate(Long reportTypeUid, Long eventNr, Boolean active, Set<RtReportTemplateFile> files) {
    RtReportTemplate template = new RtReportTemplate();
    template.setTypeUid(reportTypeUid);
    template.setTemplateFiles(files);
    template.setEventNr(eventNr);
    template.setActive(active);
    return template;
  }

  private ArrayList<RtReportTemplate> createConfiguration(RtReportTemplate... templates) {
    ArrayList<RtReportTemplate> configuration = new ArrayList<RtReportTemplate>();
    for (RtReportTemplate template : templates) {
      configuration.add(template);
    }
    return configuration;
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLoad1() throws Exception {
    TemplateSelectionUtility.loadFilesIntoLocalDir(null, null, true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLoad2() throws Exception {
    RemoteFile test = new RemoteFile("", 0);
    TemplateSelectionUtility.loadFilesIntoLocalDir(null, new TestRemoteFileLoader(test), true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLoad3() throws Exception {
    List<RtReportTemplateFile> files = new ArrayList<>();
    TemplateSelectionUtility.loadFilesIntoLocalDir(files, null, true);
  }

  @Test
  public void testLoad4a() throws Exception {
    expectedEx.expect(ProcessingException.class);
    expectedEx.expectMessage("Unable to get Master Template");

    List<RtReportTemplateFile> files = new ArrayList<>();
    RemoteFile test = new RemoteFile("", 0);
    TemplateSelectionUtility.loadFilesIntoLocalDir(files, new TestRemoteFileLoader(test), true);
  }

  @Test
  public void testLoad4b() throws Exception {
    expectedEx.expect(ProcessingException.class);
    expectedEx.expectMessage("Unable to get Master Template");

    List<RtReportTemplateFile> files = new ArrayList<>();
    RtReportTemplateFile file = new RtReportTemplateFile();
    file.setMaster(false);
    files.add(file);
    RemoteFile test = new RemoteFile("", 0);
    TemplateSelectionUtility.loadFilesIntoLocalDir(files, new TestRemoteFileLoader(test), true);
  }

  @Test
  public void testLoad5() throws Exception {
    List<RtReportTemplateFile> files = new ArrayList<>();
    RtReportTemplateFile file = new RtReportTemplateFile();
    file.setMaster(true);
    file.setTemplateFileName("masterTemplate.jrxml");
    file.setReportTemplateNr(1L);
    files.add(file);
    Long time = System.currentTimeMillis();
    RemoteFile test = createJrxmlRemoteFile(time);
    // load first time
    String result = TemplateSelectionUtility.loadFilesIntoLocalDir(files, new TestRemoteFileLoader(test), true);
    String expectedPath = System.getProperty("java.io.tmpdir") + "report1-" + time + FMilaUtility.FILE_SEPARATOR + "masterTemplate.jasper";
    Assert.assertEquals("Result path", expectedPath, result);
    // load second time
    result = TemplateSelectionUtility.loadFilesIntoLocalDir(files, new TestRemoteFileLoader(test), true);
    Assert.assertEquals("Result path", expectedPath, result);
  }

  @Test
  public void testLoadNoCompile6() throws Exception {
    List<RtReportTemplateFile> files = new ArrayList<>();
    RtReportTemplateFile file = new RtReportTemplateFile();
    file.setMaster(true);
    file.setTemplateFileName("masterTemplate.jrxml");
    file.setReportTemplateNr(1L);
    files.add(file);
    Long time = System.currentTimeMillis();
    RemoteFile test = createJrxmlRemoteFile(time);
    // load first time
    String result = TemplateSelectionUtility.loadFilesIntoLocalDir(files, new TestRemoteFileLoader(test), false);
    String expectedPath = System.getProperty("java.io.tmpdir") + "report1-" + time + FMilaUtility.FILE_SEPARATOR + "masterTemplate.jrxml";
    Assert.assertEquals("Result path", expectedPath, result);
  }

  @Test
  public void testLoadCache() throws Exception {
    List<RtReportTemplateFile> files = new ArrayList<>();
    RtReportTemplateFile file = new RtReportTemplateFile();
    file.setMaster(true);
    file.setTemplateFileName("masterTemplate.jrxml");
    file.setReportTemplateNr(1L);
    files.add(file);
    Long time = 333L;
    String expectedDir = System.getProperty("java.io.tmpdir") + "report1-" + time + FMilaUtility.FILE_SEPARATOR;
    IOUtility.deleteDirectory(expectedDir);
    boolean created = IOUtility.createDirectory(expectedDir);
    Assert.assertTrue("Directory created", created);
    RemoteFile test = createJrxmlRemoteFile(time);
    String result = TemplateSelectionUtility.loadFilesIntoLocalDir(files, new TestRemoteFileLoader(test), true);
    Assert.assertEquals("Result path", expectedDir + "masterTemplate.jasper", result);
    List<File> cachedDir = FileUtility.listTree(new File(expectedDir), true, true);
    Assert.assertEquals("No files created in Cache Dir", 1, cachedDir.size());
  }

  private RemoteFile createJrxmlRemoteFile(Long time) throws IOException {
    RemoteFile test = new RemoteFile("", time);
    test.readData(new StringReader("<jasperReport xmlns=\"http://jasperreports.sourceforge.net/jasperreports\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd\" name=\"resultsEvent\" language=\"groovy\"/>"));
    return test;
  }

  class TestRemoteFileLoader implements ITemplateLoader {
    private final RemoteFile rf;

    public TestRemoteFileLoader(RemoteFile rf) {
      this.rf = rf;
    }

    @Override
    public RemoteFile createRemoteFileFromTemplateFile(RtReportTemplateFile file) {
      return rf;
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSort1() throws Exception {
    TemplateSelectionUtility.sortTemplatesMasterFirst(null);
  }

  @Test
  public void testSort2() throws Exception {
    Set<RtReportTemplateFile> list = new HashSet<>();
    List<RtReportTemplateFile> result = TemplateSelectionUtility.sortTemplatesMasterFirst(list);
    Assert.assertEquals("0", 0, result.size());
  }

  @Test
  public void testSort3() throws Exception {
    Set<RtReportTemplateFile> list = createFileList(true);
    List<RtReportTemplateFile> result = TemplateSelectionUtility.sortTemplatesMasterFirst(list);
    Assert.assertEquals("1", 1, result.size());
    Assert.assertTrue("Master", result.get(0).isMaster());
  }

  @Test
  public void testSort4() throws Exception {
    Set<RtReportTemplateFile> list = createFileList(false, true);
    List<RtReportTemplateFile> result = TemplateSelectionUtility.sortTemplatesMasterFirst(list);
    Assert.assertEquals("2", 2, result.size());
    Assert.assertTrue("Master", result.get(0).isMaster());
    Assert.assertFalse("Not Master", result.get(1).isMaster());
  }

  @Test
  public void testSort5() throws Exception {
    Set<RtReportTemplateFile> list = createFileList(true, false);
    List<RtReportTemplateFile> result = TemplateSelectionUtility.sortTemplatesMasterFirst(list);
    Assert.assertEquals("2", 2, result.size());
    Assert.assertTrue("Master", result.get(0).isMaster());
    Assert.assertFalse("Not Master", result.get(1).isMaster());
  }

  @Test
  public void testSort6() throws Exception {
    Set<RtReportTemplateFile> list = createFileList(false, false, false, false, true, false, false);
    List<RtReportTemplateFile> result = TemplateSelectionUtility.sortTemplatesMasterFirst(list);
    Assert.assertEquals("7", 7, result.size());
    Assert.assertTrue("Master", result.get(0).isMaster());
    Assert.assertFalse("Not Master", result.get(1).isMaster());
    Assert.assertFalse("Not Master", result.get(2).isMaster());
    Assert.assertFalse("Not Master", result.get(3).isMaster());
    Assert.assertFalse("Not Master", result.get(4).isMaster());
    Assert.assertFalse("Not Master", result.get(5).isMaster());
    Assert.assertFalse("Not Master", result.get(6).isMaster());
  }

  @Test
  public void testSort7() throws Exception {
    Set<RtReportTemplateFile> list = createFileList(null, true);
    List<RtReportTemplateFile> result = TemplateSelectionUtility.sortTemplatesMasterFirst(list);
    Assert.assertEquals("2", 2, result.size());
    Assert.assertTrue("Master", result.get(0).isMaster());
    Assert.assertNull("Not Master", result.get(1).isMaster());
  }

  @Test
  public void testSort8() throws Exception {
    Set<RtReportTemplateFile> list = createFileList(false, true, true);
    List<RtReportTemplateFile> result = TemplateSelectionUtility.sortTemplatesMasterFirst(list);
    Assert.assertEquals("3", 3, result.size());
    Assert.assertTrue("Master", result.get(0).isMaster());
    Assert.assertTrue("Master", result.get(1).isMaster());
    Assert.assertFalse("Not Master", result.get(2).isMaster());
  }

  private Set<RtReportTemplateFile> createFileList(Boolean... isMaster) {
    Set<RtReportTemplateFile> list = new HashSet<>();
    for (Boolean is : isMaster) {
      RtReportTemplateFile file1 = new RtReportTemplateFile();
      file1.setMaster(is);
      list.add(file1);
    }
    return list;
  }

}
