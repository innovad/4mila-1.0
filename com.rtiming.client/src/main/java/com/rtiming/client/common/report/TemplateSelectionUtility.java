package com.rtiming.client.common.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.BooleanUtility;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.util.IOUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.services.common.file.IRemoteFileService;
import org.eclipse.scout.rt.shared.services.common.file.RemoteFile;

import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.report.template.IReportTemplateProcessService;
import com.rtiming.shared.dao.RtReportTemplate;
import com.rtiming.shared.dao.RtReportTemplateFile;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;

/**
 * 
 */
public class TemplateSelectionUtility {

  public static Set<RtReportTemplateFile> getTemplateFiles(Long reportTypeUid, Long eventNr, List<RtReportTemplate> configuration) {
    if (configuration == null || reportTypeUid == null) {
      throw new IllegalArgumentException("reportTypeUid and configuration must be set");
    }

    // try to find a custom template
    Set<RtReportTemplateFile> files = null;

    // Match event-specific template
    for (RtReportTemplate template : configuration) {
      if (BooleanUtility.nvl(template.getActive()) && CompareUtility.equals(template.getTypeUid(), reportTypeUid) && CompareUtility.equals(template.getEventNr(), eventNr)) {
        files = template.getTemplateFiles();
        break;
      }
    }

    if (files == null) {
      // Match non-event-specific template
      for (RtReportTemplate template : configuration) {
        if (BooleanUtility.nvl(template.getActive()) && CompareUtility.equals(template.getTypeUid(), reportTypeUid) && template.getEventNr() == null) {
          files = template.getTemplateFiles();
          break;
        }
      }
    }

    return files;
  }

  /**
   * @param files
   *          a set of template files for a repor template
   * @return the path to the compiled, local jasper main template
   * @throws ProcessingException
   */
  public static String loadFilesIntoLocalDir(Collection<RtReportTemplateFile> files, ITemplateLoader loader, boolean compile) throws ProcessingException {
    if (files == null || loader == null) {
      throw new IllegalArgumentException("Arguments must be set");
    }

    // get last modified of master template
    Long lastModified = null;
    Long reportTemplateNr = null;
    String templateFilename = null;
    for (RtReportTemplateFile file : files) {
      if (file.isMaster()) {
        RemoteFile rf = loader.createRemoteFileFromTemplateFile(file);
        lastModified = rf.getLastModified();
        reportTemplateNr = file.getReportTemplateNr();
        templateFilename = file.getTemplateFileName();
        break;
      }
    }
    if (lastModified == null || reportTemplateNr == null) {
      throw new ProcessingException("Unable to get Master Template");
    }

    // create the local report directory (name structure: report-nr-lastModified)
    String systemTempDir = System.getProperty("java.io.tmpdir");
    String tempReportDirectory = systemTempDir + "report" + reportTemplateNr + "-" + lastModified;
    boolean dirCreated = IOUtility.createDirectory(tempReportDirectory);
    if (compile) {
      templateFilename = tempReportDirectory + FMilaUtility.FILE_SEPARATOR + StringUtility.removeSuffixes(templateFilename, IReportTemplateProcessService.TEMPLATE_SUFFIX_JRXML) + "jasper";
    }
    else {
      templateFilename = tempReportDirectory + FMilaUtility.FILE_SEPARATOR + templateFilename;
    }

    // the first time, download and compile all report files
    if (dirCreated) {
      for (RtReportTemplateFile file : files) {
        RemoteFile remoteJrxmlFile = loader.createRemoteFileFromTemplateFile(file);
        try {
          File localJrxmlFile = new File(tempReportDirectory + FMilaUtility.FILE_SEPARATOR + file.getTemplateFileName());
          IOUtility.writeContent(new FileOutputStream(localJrxmlFile), remoteJrxmlFile.extractData());
          if (compile) {
            String localJasperFile = StringUtility.removeSuffixes(localJrxmlFile.getAbsolutePath(), IReportTemplateProcessService.TEMPLATE_SUFFIX_JRXML) + "jasper";
            JasperCompileManager.compileReportToFile(localJrxmlFile.getAbsolutePath(), localJasperFile);
          }
        }
        catch (IOException | JRException e) {
          throw new VetoException(Texts.get("FileReadException") + " (" + remoteJrxmlFile.getPath() + (e.getLocalizedMessage() != null ? "\n\n" + e.getLocalizedMessage() : "") + ")", e);
        }
      }
    }

    // return the path to the master jasper file
    return templateFilename;
  }

  public static List<RtReportTemplateFile> sortTemplatesMasterFirst(Set<RtReportTemplateFile> templateFiles) {
    if (templateFiles == null) {
      throw new IllegalArgumentException("template list must be set");
    }
    List<RtReportTemplateFile> sortedTemplates = new ArrayList<RtReportTemplateFile>(templateFiles);
    Comparator<? super RtReportTemplateFile> comparator = new Comparator<RtReportTemplateFile>() {
      @Override
      public int compare(RtReportTemplateFile o1, RtReportTemplateFile o2) {
        if (o1 != null) {
          if (BooleanUtility.nvl(o1.isMaster())) {
            return -1;
          }
        }
        else if (o2 != null) {
          if (BooleanUtility.nvl(o2.isMaster())) {
            return 1;
          }
        }
        return 0;
      }
    };
    Collections.sort(sortedTemplates, comparator);
    return sortedTemplates;
  }

  public static String findDefaultTemplatePath(String pathInReportResourcesDirectory) throws ProcessingException {
    // TODO MIG
//    URL jasperFile = FMilaUtility.findFileLocation(FMilaReport.LOCAL_REPORT_DIR + pathInReportResourcesDirectory, Activator.getDefault().getBundle().getSymbolicName());
//    return jasperFile.getFile();
    return null;
  }

  static class CustomServerRemoteFileLoader implements ITemplateLoader {
    @Override
    public RemoteFile createRemoteFileFromTemplateFile(RtReportTemplateFile file) throws ProcessingException {
      String suffix = IOUtility.getFileExtension(file.getTemplateFileName());
      RemoteFile rf = new RemoteFile(IReportTemplateProcessService.SERVER_MAP_DIR + "/" + file.getId().getClientNr() + "/" + file.getId().getId() + "." + suffix, 0);
      rf = BEANS.get(IRemoteFileService.class).getRemoteFile(rf);
      return rf;
    }
  }

  static class DefaultLocalJarFileLoader implements ITemplateLoader {
    @Override
    public RemoteFile createRemoteFileFromTemplateFile(RtReportTemplateFile file) throws ProcessingException {
      String fullPath = findDefaultTemplatePath(file.getTemplateFileName());
      RemoteFile remoteFile = new RemoteFile("resources/reports/" + file.getTemplateFileName(), ManagementFactory.getRuntimeMXBean().getStartTime());
      try {
        remoteFile.readData(new File(fullPath));
      }
      catch (IOException e) {
        throw new ProcessingException("Could not read default Template", e);
      }
      return remoteFile;
    }
  }

  public static ITemplateLoader getCustomTemplateLoader() {
    return new CustomServerRemoteFileLoader();
  }

  public static ITemplateLoader getDefaultTemplateLoader() {
    return new DefaultLocalJarFileLoader();
  }

}
