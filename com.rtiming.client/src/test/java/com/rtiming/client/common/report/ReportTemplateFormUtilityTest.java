package com.rtiming.client.common.report;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.scout.rt.platform.util.IOUtility;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.rtiming.client.common.report.template.ITemplateField;
import com.rtiming.client.common.report.template.ReportTemplateFormUtility;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.dao.RtReportTemplateFile;

public class ReportTemplateFormUtilityTest {

  @Test(expected = IllegalArgumentException.class)
  public void testLoadNull1() throws Exception {
    ReportTemplateFormUtility.loadFilesFromRemoteServer(null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLoadNull2() throws Exception {
    Set<RtReportTemplateFile> templateFiles = new HashSet<>();
    ReportTemplateFormUtility.loadFilesFromRemoteServer(templateFiles, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLoadNull3() throws Exception {
    ReportTemplateFormUtility.loadFilesFromRemoteServer(null, createTemplateFieldList(), null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLoadNull4() throws Exception {
    File localDir = Mockito.mock(File.class);
    ReportTemplateFormUtility.loadFilesFromRemoteServer(null, null, localDir);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLoadNull5() throws Exception {
    Set<RtReportTemplateFile> templateFiles = new HashSet<>();
    ReportTemplateFormUtility.loadFilesFromRemoteServer(templateFiles, createTemplateFieldList(), null);
  }

  @Test
  public void testLoadEmpty() throws Exception {
    File localDir = Mockito.mock(File.class);
    Set<RtReportTemplateFile> templateFiles = new HashSet<>();
    ReportTemplateFormUtility.loadFilesFromRemoteServer(templateFiles, createTemplateFieldList(), localDir);
  }

  @Test
  public void testLoadNoTemplateFiles() throws Exception {
    File localDir = Mockito.mock(File.class);
    Set<RtReportTemplateFile> templateFiles = new HashSet<>();
    List<ITemplateField> templateFields = createTemplateFieldList(createTemplateField(0));
    ReportTemplateFormUtility.loadFilesFromRemoteServer(templateFiles, templateFields, localDir);
  }

  @Test
  public void testLoad1() throws Exception {
    File localDir = Mockito.mock(File.class);

    Set<RtReportTemplateFile> templateFiles = new HashSet<>();
    RtReportTemplateFile file = new RtReportTemplateFile();
    file.setLastModified(1234567L);
    templateFiles.add(file);

    List<ITemplateField> templateFields = createTemplateFieldList(createTemplateField(0));

    ReportTemplateFormUtility.loadFilesFromRemoteServer(templateFiles, templateFields, localDir);
  }

  @Test
  public void testLoadNewFile() throws Exception {
    File localDir = IOUtility.createTempDirectory("test");

    // test server template TEST.txt
    Set<RtReportTemplateFile> templateFiles = new HashSet<>();
    RtReportTemplateFile file = createServerTemplate(1234567);
    templateFiles.add(file);

    // mocked field
    List<ITemplateField> templateFields = new ArrayList<>();
    ITemplateField field = createTemplateField(0);
    templateFields.add(field);

    boolean doClose = ReportTemplateFormUtility.loadFilesFromRemoteServer(templateFiles, templateFields, localDir);
    ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
    Mockito.verify(field).setValue(argument.capture());
    // Assert server template was created in local temp directory
    Assert.assertEquals("Value", localDir.getAbsolutePath() + FMilaUtility.FILE_SEPARATOR + "TEST.txt", argument.getValue());
    Assert.assertFalse("Do not close", doClose);
  }

  @Test
  public void testLoadExistingNewFileCancel() throws Exception {
    File localDir = IOUtility.createTempDirectory("test");
    File existingFile = new File(localDir + FMilaUtility.FILE_SEPARATOR + "TEST.txt");
    existingFile.createNewFile();
    long timeCreated = IOUtility.getFileLastModified(existingFile.getAbsolutePath());

    // test server template TEST.txt
    Set<RtReportTemplateFile> templateFiles = new HashSet<>();
    RtReportTemplateFile file = createServerTemplate(1234567);
    templateFiles.add(file);

    // mocked field
    List<ITemplateField> templateFields = new ArrayList<>();
    ITemplateField field = createTemplateField(timeCreated - 1000);
    templateFields.add(field);

    boolean doClose = ReportTemplateFormUtility.loadFilesFromRemoteServer(templateFiles, templateFields, localDir);
    ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
    Mockito.verify(field).setValue(argument.capture());
    // Assert server template path in local temp directory
    String expectedPath = localDir.getAbsolutePath() + FMilaUtility.FILE_SEPARATOR + "TEST.txt";
    Assert.assertEquals("Value", expectedPath, argument.getValue());
    Assert.assertEquals(timeCreated, IOUtility.getFileLastModified(expectedPath));
    Assert.assertTrue("Do not close", doClose);
  }

  @Test
  public void testLoadExistingOldFile() throws Exception {
    File localDir = IOUtility.createTempDirectory("test");
    File existingFile = new File(localDir + FMilaUtility.FILE_SEPARATOR + "TEST.txt");
    existingFile.createNewFile();
    FileWriter writer = new FileWriter(existingFile);
    writer.write("TEST1");
    writer.close();
    long timeCreated = IOUtility.getFileLastModified(existingFile.getAbsolutePath());
    Thread.sleep(1000);

    // test server template TEST.txt
    Set<RtReportTemplateFile> templateFiles = new HashSet<>();
    RtReportTemplateFile file = createServerTemplate(timeCreated + 1000);
    file.setTemplateContent("TEST2");
    templateFiles.add(file);

    // mocked field
    List<ITemplateField> templateFields = new ArrayList<>();
    ITemplateField field = createTemplateField(timeCreated + 1000);
    templateFields.add(field);

    boolean doClose = ReportTemplateFormUtility.loadFilesFromRemoteServer(templateFiles, templateFields, localDir);
    ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
    Mockito.verify(field).setValue(argument.capture());
    // Assert server template path in local temp directory
    String expectedPath = localDir.getAbsolutePath() + FMilaUtility.FILE_SEPARATOR + "TEST.txt";
    Assert.assertEquals("Value", expectedPath, argument.getValue());
    System.out.println("Creation Time " + timeCreated);
    System.out.println("Updated " + IOUtility.getFileLastModified(expectedPath));
    Assert.assertTrue("File was updated", timeCreated < IOUtility.getFileLastModified(expectedPath));
    Assert.assertEquals("Content", "TEST2", IOUtility.getContent(new FileReader(existingFile)));
    Assert.assertFalse("Do not close", doClose);
  }

  @Test
  public void testLoadSameContentDifferentDate() throws Exception {
    File localDir = IOUtility.createTempDirectory("test2");
    File existingFile = new File(localDir + FMilaUtility.FILE_SEPARATOR + "TEST.txt");
    existingFile.createNewFile();
    FileWriter writer = new FileWriter(existingFile);
    writer.write("TEST1");
    writer.close();
    long timeCreated = IOUtility.getFileLastModified(existingFile.getAbsolutePath());
    Thread.sleep(1000);

    // test server template TEST.txt
    Set<RtReportTemplateFile> templateFiles = new HashSet<>();
    RtReportTemplateFile file = createServerTemplate(timeCreated - 100000);
    file.setTemplateContent("TEST1");
    templateFiles.add(file);

    // mocked field
    List<ITemplateField> templateFields = new ArrayList<>();
    ITemplateField field = createTemplateField(timeCreated - 100000);
    templateFields.add(field);

    boolean doClose = ReportTemplateFormUtility.loadFilesFromRemoteServer(templateFiles, templateFields, localDir);
    ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
    Mockito.verify(field).setValue(argument.capture());
    // Assert server template path in local temp directory
    String expectedPath = localDir.getAbsolutePath() + FMilaUtility.FILE_SEPARATOR + "TEST.txt";
    Assert.assertEquals("Value", expectedPath, argument.getValue());
    Assert.assertTrue("File was updated", timeCreated < IOUtility.getFileLastModified(expectedPath));
    Assert.assertEquals("Content", "TEST1", IOUtility.getContent(new FileReader(existingFile)));
    Assert.assertFalse("Do not close", doClose);
  }

  private ITemplateField createTemplateField(long serverLastModified) {
    ITemplateField field = Mockito.mock(ITemplateField.class);
    Mockito.when(field.isVisible()).thenReturn(true);
    Mockito.when(field.getServerLastModified()).thenReturn(serverLastModified);
    return field;
  }

  private List<ITemplateField> createTemplateFieldList(ITemplateField... fields) {
    List<ITemplateField> templateFields = new ArrayList<>();
    for (ITemplateField field : fields) {
      templateFields.add(field);
    }
    return templateFields;
  }

  private RtReportTemplateFile createServerTemplate(long timestamp) {
    RtReportTemplateFile file = new RtReportTemplateFile();
    file.setLastModified(timestamp);
    file.setTemplateFileName("TEST.txt");
    file.setTemplateContent("TESTCONTENT");
    return file;
  }
}
