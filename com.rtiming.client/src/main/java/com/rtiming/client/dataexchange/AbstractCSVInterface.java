package com.rtiming.client.dataexchange;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.csv.CsvHelper;
import org.eclipse.scout.rt.shared.csv.IDataConsumer;
import org.eclipse.scout.rt.shared.services.common.code.CODES;
import org.eclipse.scout.rt.shared.services.common.code.ICode;

import com.rtiming.client.ClientSession;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.dataexchange.AbstractCSVDataBean;
import com.rtiming.shared.dataexchange.AbstractDataBean;
import com.rtiming.shared.dataexchange.CSVElement;
import com.rtiming.shared.dataexchange.DataExchangeStartFormData;
import com.rtiming.shared.dataexchange.DataExchangeUtility;
import com.rtiming.shared.settings.user.LanguageCodeType;

public abstract class AbstractCSVInterface<Bean extends AbstractCSVDataBean> extends AbstractInterface {

  private SortedMap<Integer, Field> importedFields;
  private SortedMap<Integer, Field> allFields;

  private final boolean isIgnoreHeaderRow;
  private final char separator;
  private final Character wrapper;
  private Locale locale;

  protected AbstractCSVInterface(DataExchangeStartFormData interfaceConfig) throws ProcessingException {
    super(interfaceConfig);
    this.isIgnoreHeaderRow = interfaceConfig.getIgnoreHeaderRow().getValue();
    this.separator = interfaceConfig.getFieldSeparator().getValue();
    wrapper = interfaceConfig.getTextEnclosing().getValue();
    if (getLanguageUid() != null) {
      ICode<?> language = CODES.getCodeType(LanguageCodeType.class).getCode(getLanguageUid());
      locale = new Locale(language.getExtKey());
    }
    init();
  }

  private void init() {

    // load csv definition
    importedFields = new TreeMap<Integer, Field>();
    allFields = new TreeMap<Integer, Field>();
    Field[] list = DataExchangeUtility.getAllFields(createNewBean(null).getClass());
    for (Field field : list) {
      CSVElement csv = field.getAnnotation(CSVElement.class);
      if (csv != null) {
        if (!csv.ignore()) {
          importedFields.put(csv.value(), field);
        }
        allFields.put(csv.value(), field);
      }
    }
  }

  protected boolean getConfiguredTranslatedColumnHeaders() {
    return false;
  }

  @Override
  public final void fileToPreview() {
    try {
      CsvHelper helper = new CsvHelper(ClientSession.get().getLocale(), StringUtility.emptyIfNull(separator), StringUtility.emptyIfNull(wrapper), FMilaUtility.LINE_SEPARATOR);
      List<String> cols = new ArrayList<String>();
      int colCount = getCSVColumnHeaders(true).size();

      // fix CsvHelper behaviour, allow 10 additional columns, instead NPE
      for (int i = 0; i < colCount + 10; i++) {
        if (i < colCount) {
          cols.add("string");
        }
        else {
          cols.add(CsvHelper.IGNORED_COLUMN_NAME);
        }
      }
      helper.setColumnNames(cols);
      helper.setColumnTypes(cols);

      BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(getFullPathName()), Charset.forName(getCharacterSet())));
      helper.importData(new CSVRowConsumer(), br, false, false, isIgnoreHeaderRow ? 1 : 0, -1, true);
    }
    catch (Exception e) {
      e.printStackTrace();
    }

  }

  private class CSVRowConsumer implements IDataConsumer {
    @Override
    public void processRow(int lineNr, List<Object> row) throws ProcessingException {
      importLine(row);
    }
  }

  @Override
  public final void createPreviewData() throws ProcessingException {

    int rowCounter = 0;

    if (getData() != null) {
      int numRows = getData().size();
      getPreview().setData(new Object[numRows][getColumnHeaders().size()]);
      getPreview().setErrors(new String[numRows][getColumnHeaders().size()]);

      for (Bean bean : getData()) {
        // output data
        int columnCounter = 0;
        List<Object> data = bean.getData();
        for (Object csvCellObject : data) {
          try {
            // apply data
            if (csvCellObject instanceof String[]) {
              int multiCounter = 0;
              for (String csvCellString : (String[]) csvCellObject) {
                if (csvCellString != null) {
                  getPreview().getData()[rowCounter][columnCounter + multiCounter] = csvCellString;
                  getPreview().getErrors()[rowCounter][columnCounter + multiCounter] = doPreCheck(allFields.get(columnCounter + 1), csvCellString, bean);
                }
                multiCounter++;
              }
            }
            else if (csvCellObject instanceof String) {
              String csvCellString = (String) csvCellObject;
              getPreview().getData()[rowCounter][columnCounter] = csvCellString;
              getPreview().getErrors()[rowCounter][columnCounter] = doPreCheck(allFields.get(columnCounter + 1), csvCellString, bean);
            }
            else if (csvCellObject != null) {
              throw new IllegalArgumentException("CSV data must be String: " + csvCellObject);
            }
          }
          catch (Exception e) {
            e.printStackTrace();
          }
          columnCounter++;
        }
        rowCounter++;
      }
    }
  }

  /**
   * @param field
   * @param value
   * @return
   */
  private String doPreCheck(Field field, String value, AbstractDataBean bean) {
    String warning = null;
    // apply pre-checks
    // mandatory
    if (field.getAnnotation(CSVElement.class).isMandatory()) {
      if (StringUtility.isNullOrEmpty(value)) {
        String[] title = field.getAnnotation(CSVElement.class).title();
        warning = Texts.get("ImportWarningMandatory") + " " + title[0];
        bean.setErrorMessage(warning);
      }
    }
    // year of birth
    else if (field.getAnnotation(CSVElement.class).isYear()) {
      if (!StringUtility.isNullOrEmpty(value) && DataExchangeUtility.parseYear(value) == null) {
        warning = Texts.get("ImportWarningYearOfBirth", value);
        bean.setErrorMessage(warning);
      }
    }
    // sex
    else if (field.getAnnotation(CSVElement.class).isSex()) {
      if (!StringUtility.isNullOrEmpty(value) && DataExchangeUtility.parseSex(value) == null) {
        warning = Texts.get("ImportWarningSex", value);
        bean.setErrorMessage(warning);
      }
    }
    else {
      // max length
      long maxLength = field.getAnnotation(CSVElement.class).maxLength();
      if (maxLength > 0) {
        if (StringUtility.length(value) > maxLength) {
          warning = Texts.get("ImportWarningMaxLength", value, String.valueOf(maxLength));
          bean.setErrorMessage(warning);
        }
      }

      // min length
      long minLength = field.getAnnotation(CSVElement.class).minLength();
      if (StringUtility.length(value) < minLength) {
        warning = Texts.get("ImportWarningMinLength", value, String.valueOf(minLength));
        bean.setErrorMessage(warning);
      }
    }
    return warning;
  }

  @Override
  public final void previewToFile(ProgressMonitor monitor) throws ProcessingException {
    File file = new File(getFullPathName());
    CsvHelper helper = new CsvHelper(Locale.getDefault(), separator, wrapper, FMilaUtility.LINE_SEPARATOR);
    Object[][] data = getPreview().getData();
    if (data == null) {
      data = new Object[0][getColumnHeaders().size()];
    }
    helper.exportData(data, file, getCharacterSet(), getColumnHeaders(), isIgnoreHeaderRow, null, false);

    int length = data != null ? data.length : 0;
    monitor.addInfo(TEXTS.get("File"), getFullPathName());
    monitor.addInfo(TEXTS.get("NumberOfLines"), NumberUtility.format(length));
    monitor.addInfo(TEXTS.get("FileSize"), FMilaUtility.formatFileSize(file.length()));
    monitor.update(length, length);
  }

  @Override
  public final ArrayList<String> getColumnHeaders() throws ProcessingException {
    return getCSVColumnHeaders(false);
  }

  private ArrayList<String> getCSVColumnHeaders(boolean includeIgnored) throws ProcessingException {
    ArrayList<String> columnHeader = new ArrayList<String>();
    SortedMap<Integer, Field> fields = importedFields;
    if (includeIgnored) {
      fields = allFields;
    }
    for (Field f : fields.values()) {
      f.setAccessible(true);
      CSVElement csv = f.getAnnotation(CSVElement.class);
      if (csv != null && (includeIgnored || !csv.ignore())) {
        if (f.getType().equals(String[].class)) {
          // Multi Fields
          int multiLength = 0;
          if (getData() != null) {
            for (Bean b : getData()) {
              try {
                String[] multiField = (String[]) f.get(b);
                if (multiField != null) {
                  multiLength = Math.max(multiLength, multiField.length);
                }
              }
              catch (Exception e) {
                e.printStackTrace();
              }
            }
          }
          String[] titles = csv.title();
          for (int i = 0; i < multiLength / titles.length; i++) {
            for (String title : titles) {
              columnHeader.add(getTranslatedTitle(title) + (i + 1));
            }
          }
        }
        else {
          // Standard Single Fields
          columnHeader.add(getTranslatedTitle(csv.title()[0]));
        }
      }
    }
    return columnHeader;
  }

  private String getTranslatedTitle(String title) throws ProcessingException {
    if (getConfiguredTranslatedColumnHeaders()) {
      return TEXTS.get(locale, title);
    }
    return title;
  }

  private void importLine(List<Object> originalRow) {
    AbstractCSVDataBean bean = (AbstractCSVDataBean) createNewBean(null);

    List<Object> trimmedRow = new ArrayList<Object>(originalRow.size());
    for (int i = 0; i < originalRow.size(); i++) {
      String string = (String) originalRow.get(i);
      if (string != null) {
        string = string.trim();
      }
      trimmedRow.add(string);
    }

    bean.setData(trimmedRow);
    addBean(bean);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void addBean(AbstractDataBean bean) {
    super.addBean(bean);
  }

  @SuppressWarnings("unchecked")
  @Override
  public ArrayList<Bean> getData() {
    return super.getData();
  }

}
