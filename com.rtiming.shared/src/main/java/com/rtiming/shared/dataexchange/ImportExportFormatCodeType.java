package com.rtiming.shared.dataexchange;

import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;

import com.rtiming.shared.Texts;

public class ImportExportFormatCodeType extends AbstractCodeType<Long, Long> {

  private static final long serialVersionUID = 1L;
  public static final long ID = 1300L;
  private static String VERSION203 = " (2.0.3)";
  private static String VERSION300 = " (3.0.0)";

  public ImportExportFormatCodeType() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredText() {
    return Texts.get("ImportExportType");
  }

  @Override
  public Long getId() {
    return ID;
  }

  @Order(10.0)
  public static class IOFDataStandard203CourseDataCode extends AbstractImportExportCode {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1353L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("IOFDataStandardCourseData") + VERSION203;
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public boolean isImport() {
      return true;
    }

    @Override
    public boolean isEventDependent() {
      return true;
    }

    @Override
    public boolean isExport() {
      return false;
    }

    @Override
    public String getFileExtension() {
      return "xml";
    }

    @Override
    public boolean isLanguageDependent() {
      return false;
    }

  }

  @Order(15.0)
  public static class IOFDataStandard300CourseDataCode extends AbstractImportExportCode {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1367L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("IOFDataStandardCourseData") + VERSION300;
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public boolean isImport() {
      return true;
    }

    @Override
    public boolean isEventDependent() {
      return true;
    }

    @Override
    public boolean isExport() {
      return false;
    }

    @Override
    public String getFileExtension() {
      return "xml";
    }

    @Override
    public boolean isLanguageDependent() {
      return false;
    }

  }

  @Order(20.0)
  public static class IOFDataStandard300EntryListCode extends AbstractImportExportCode {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1354L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("IOFDataStandardEntryList") + VERSION300;
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public boolean isImport() {
      return true;
    }

    @Override
    public boolean isExport() {
      return false;
    }

    @Override
    public boolean isEventDependent() {
      return true;
    }

    @Override
    public String getFileExtension() {
      return "xml";
    }

    @Override
    public boolean isLanguageDependent() {
      return false;
    }

    @Override
    protected boolean getConfiguredActive() {
      // disable until implemented
      return false;
    }

  }

  @Order(30.0)
  public static class IOFDataStandard203StartListCode extends AbstractImportExportCode {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1355L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("IOFDataStandardStartList") + VERSION203;
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public boolean isImport() {
      return false;
    }

    @Override
    public boolean isExport() {
      return true;
    }

    @Override
    public boolean isEventDependent() {
      return true;
    }

    @Override
    public String getFileExtension() {
      return "xml";
    }

    @Override
    public boolean isLanguageDependent() {
      return false;
    }

  }

  @Order(35.0)
  public static class IOFDataStandard300StartListCode extends AbstractImportExportCode {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1364L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("IOFDataStandardStartList") + VERSION300;
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public boolean isImport() {
      return false;
    }

    @Override
    public boolean isExport() {
      return true;
    }

    @Override
    public boolean isEventDependent() {
      return true;
    }

    @Override
    public String getFileExtension() {
      return "xml";
    }

    @Override
    public boolean isLanguageDependent() {
      return false;
    }

  }

  @Order(40.0)
  public static class IOFDataStandard203ResultsCode extends AbstractImportExportCode {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1356L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("IOFDataStandardResults") + VERSION203;
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public boolean isImport() {
      return false;
    }

    @Override
    public boolean isExport() {
      return true;
    }

    @Override
    public boolean isEventDependent() {
      return true;
    }

    @Override
    public String getFileExtension() {
      return "xml";
    }

    @Override
    public boolean isLanguageDependent() {
      return false;
    }

  }

  @Order(45.0)
  public static class IOFDataStandard300ResultsCode extends AbstractImportExportCode {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1363L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("IOFDataStandardResults") + VERSION300;
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public boolean isImport() {
      return false;
    }

    @Override
    public boolean isExport() {
      return true;
    }

    @Override
    public boolean isEventDependent() {
      return true;
    }

    @Override
    public String getFileExtension() {
      return "xml";
    }

    @Override
    public boolean isLanguageDependent() {
      return false;
    }

  }

  @Order(50.0)
  public static class IOFDataStandard203RunnerCode extends AbstractImportExportCode {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1357L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("IOFDataStandardRunner") + VERSION203;
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public boolean isImport() {
      return true;
    }

    @Override
    public boolean isExport() {
      return true;
    }

    @Override
    public boolean isEventDependent() {
      return false;
    }

    @Override
    public String getFileExtension() {
      return "xml";
    }

    @Override
    public boolean isLanguageDependent() {
      return false;
    }

  }

  @Order(45.0)
  public static class IOFDataStandard300RunnerCode extends AbstractImportExportCode {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1365L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("IOFDataStandardRunner") + VERSION300;
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public boolean isImport() {
      return true;
    }

    @Override
    public boolean isExport() {
      return true;
    }

    @Override
    public boolean isEventDependent() {
      return false;
    }

    @Override
    public String getFileExtension() {
      return "xml";
    }

    @Override
    public boolean isLanguageDependent() {
      return false;
    }

  }

  @Order(60.0)
  public static class OE2003EntryListCode extends AbstractImportExportCode {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1358L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("OE2003EntryList");
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public boolean isImport() {
      return true;
    }

    @Override
    public boolean isExport() {
      return false;
    }

    @Override
    public boolean isEventDependent() {
      return true;
    }

    @Override
    public String getFileExtension() {
      return "csv";
    }

    @Override
    public Character getConfiguredDefaultFieldSeparator() {
      return ';';
    }

    @Override
    public Character getConfiguredDefaultTextEnclosing() {
      return '"';
    }

    @Override
    public Boolean getConfiguredIgnoreHeaderRow() {
      return true;
    }

    @Override
    public boolean isLanguageDependent() {
      return true;
    }

  }

  @Order(70.0)
  public static class OE2003StartListCode extends AbstractImportExportCode {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1359L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("OE2003StartList");
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public boolean isImport() {
      return false;
    }

    @Override
    public boolean isExport() {
      return true;
    }

    @Override
    public boolean isEventDependent() {
      return true;
    }

    @Override
    public String getFileExtension() {
      return "csv";
    }

    @Override
    public Character getConfiguredDefaultFieldSeparator() {
      return ';';
    }

    @Override
    public Character getConfiguredDefaultTextEnclosing() {
      return '"';
    }

    @Override
    public Boolean getConfiguredIgnoreHeaderRow() {
      return true;
    }

    @Override
    public boolean isLanguageDependent() {
      return true;
    }

  }

  @Order(80.0)
  public static class OE2003ResultListCode extends AbstractImportExportCode {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1360L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("OE2003ResultList");
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public boolean isImport() {
      return false;
    }

    @Override
    public boolean isExport() {
      return true;
    }

    @Override
    public boolean isEventDependent() {
      return true;
    }

    @Override
    public String getFileExtension() {
      return "csv";
    }

    @Override
    public Character getConfiguredDefaultFieldSeparator() {
      return ';';
    }

    @Override
    public Character getConfiguredDefaultTextEnclosing() {
      return '"';
    }

    @Override
    public Boolean getConfiguredIgnoreHeaderRow() {
      return true;
    }

    @Override
    public boolean isLanguageDependent() {
      return true;
    }

  }

  @Order(90.0)
  public static class SwissOrienteeringRunnerDatabaseCode extends AbstractImportExportCode {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1351L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("SwissOrienteeringRunnerDatabase");
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public boolean isImport() {
      return true;
    }

    @Override
    public boolean isEventDependent() {
      return false;
    }

    @Override
    public boolean isExport() {
      return true;
    }

    @Override
    public String getFileExtension() {
      return "csv";
    }

    @Override
    public Character getConfiguredDefaultFieldSeparator() {
      return '\t';
    }

    @Override
    public Character getConfiguredDefaultTextEnclosing() {
      return '"';
    }

    @Override
    public Boolean getConfiguredIgnoreHeaderRow() {
      return true;
    }

    @Override
    public boolean isLanguageDependent() {
      return false;
    }

  }

  @Order(100.0)
  public static class GO2OLEntriesCode extends AbstractImportExportCode {

    private static final long serialVersionUID = 1L;
    public static final Long ID = 1362L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("GO2OLEntriesCode");
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public boolean isImport() {
      return true;
    }

    @Override
    public boolean isExport() {
      return false;
    }

    @Override
    public boolean isEventDependent() {
      return true;
    }

    @Override
    public boolean isLanguageDependent() {
      return false;
    }

    @Override
    public String getFileExtension() {
      return "csv";
    }

    @Override
    public Character getConfiguredDefaultFieldSeparator() {
      return ';';
    }

    @Override
    public Character getConfiguredDefaultTextEnclosing() {
      return '"';
    }

    @Override
    public Boolean getConfiguredIgnoreHeaderRow() {
      return true;
    }

  }

  @Order(105.0)
  public static class GeneralPostalCode extends AbstractImportExportCode {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1366L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("GeneralPostalCode");
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public boolean isImport() {
      return true;
    }

    @Override
    public boolean isEventDependent() {
      return false;
    }

    @Override
    public boolean isExport() {
      return false;
    }

    @Override
    public String getFileExtension() {
      return "txt";
    }

    @Override
    public Character getConfiguredDefaultFieldSeparator() {
      return ';';
    }

    @Override
    public Character getConfiguredDefaultTextEnclosing() {
      return null;
    }

    @Override
    public Boolean getConfiguredIgnoreHeaderRow() {
      return true;
    }

    @Override
    public boolean isLanguageDependent() {
      return false;
    }

  }

  @Order(110.0)
  public static class GeoNamesPostalCode extends AbstractImportExportCode {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1352L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("GeoNamesPostalCode");
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public boolean isImport() {
      return true;
    }

    @Override
    public boolean isEventDependent() {
      return false;
    }

    @Override
    public boolean isExport() {
      return false;
    }

    @Override
    public String getFileExtension() {
      return "txt";
    }

    @Override
    public Character getConfiguredDefaultFieldSeparator() {
      return '\t';
    }

    @Override
    public Character getConfiguredDefaultTextEnclosing() {
      return null;
    }

    @Override
    public Boolean getConfiguredIgnoreHeaderRow() {
      return false;
    }

    @Override
    public boolean isLanguageDependent() {
      return false;
    }

  }

  @Order(120.0)
  public static class SwissPostPostalCode extends AbstractImportExportCode {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1361L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("SwissPostPostalCode");
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public boolean isImport() {
      return true;
    }

    @Override
    public boolean isExport() {
      return false;
    }

    @Override
    public boolean isEventDependent() {
      return false;
    }

    @Override
    public boolean isLanguageDependent() {
      return false;
    }

    @Override
    public String getFileExtension() {
      return "txt";
    }

    @Override
    public Character getConfiguredDefaultFieldSeparator() {
      return '\t';
    }

    @Override
    public Character getConfiguredDefaultTextEnclosing() {
      return null;
    }

  }

}
