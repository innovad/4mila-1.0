package com.rtiming.client.dataexchange;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.booleanfield.AbstractBooleanField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractRadioButton;
import org.eclipse.scout.rt.client.ui.form.fields.filechooserfield.AbstractFileChooserField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.radiobuttongroup.AbstractRadioButtonGroup;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.CODES;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;
import org.mozilla.universalchardet.UniversalDetector;

import com.rtiming.client.ClientSession;
import com.rtiming.client.common.help.AbstractHelpLinkButton;
import com.rtiming.client.dataexchange.DataExchangeStartForm.MainBox.CancelButton;
import com.rtiming.client.dataexchange.DataExchangeStartForm.MainBox.FileBox;
import com.rtiming.client.dataexchange.DataExchangeStartForm.MainBox.FileBox.CharacterSetField;
import com.rtiming.client.dataexchange.DataExchangeStartForm.MainBox.FileBox.FieldSeparatorField;
import com.rtiming.client.dataexchange.DataExchangeStartForm.MainBox.FileBox.FileField;
import com.rtiming.client.dataexchange.DataExchangeStartForm.MainBox.FileBox.IgnoreHeaderRowField;
import com.rtiming.client.dataexchange.DataExchangeStartForm.MainBox.FileBox.TextEnclosingField;
import com.rtiming.client.dataexchange.DataExchangeStartForm.MainBox.FormatField;
import com.rtiming.client.dataexchange.DataExchangeStartForm.MainBox.ImportExportGroup;
import com.rtiming.client.dataexchange.DataExchangeStartForm.MainBox.ImportExportGroup.ExportButton;
import com.rtiming.client.dataexchange.DataExchangeStartForm.MainBox.ImportExportGroup.ImportButton;
import com.rtiming.client.dataexchange.DataExchangeStartForm.MainBox.OkButton;
import com.rtiming.client.dataexchange.DataExchangeStartForm.MainBox.OptionsBox;
import com.rtiming.client.dataexchange.DataExchangeStartForm.MainBox.OptionsBox.EventField;
import com.rtiming.client.dataexchange.DataExchangeStartForm.MainBox.OptionsBox.LanguageField;
import com.rtiming.client.event.AbstractEventField;
import com.rtiming.shared.Texts;
import com.rtiming.shared.dataexchange.AbstractImportExportCode;
import com.rtiming.shared.dataexchange.DataExchangeStartFormData;
import com.rtiming.shared.dataexchange.ImportExportFormatCodeType;
import com.rtiming.shared.settings.IDefaultProcessService;
import com.rtiming.shared.settings.user.LanguageCodeType;

@FormData(value = DataExchangeStartFormData.class, sdkCommand = SdkCommand.CREATE)
public class DataExchangeStartForm extends AbstractForm {

  private Long importNr;

  public DataExchangeStartForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Import");
  }

  @FormData
  public Long getImportNr() {
    return importNr;
  }

  @FormData
  public void setImportNr(Long importNr) {
    this.importNr = importNr;
  }

  public void startNew() throws ProcessingException {
    startInternal(new NewHandler());
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  public EventField getEventField() {
    return getFieldByClass(EventField.class);
  }

  public ExportButton getExportButton() {
    return getFieldByClass(ExportButton.class);
  }

  public FieldSeparatorField getFieldSeparatorField() {
    return getFieldByClass(FieldSeparatorField.class);
  }

  public FileBox getFileBox() {
    return getFieldByClass(FileBox.class);
  }

  public FormatField getFormatField() {
    return getFieldByClass(FormatField.class);
  }

  public CharacterSetField getCharacterSetField() {
    return getFieldByClass(CharacterSetField.class);
  }

  public FileField getFileField() {
    return getFieldByClass(FileField.class);
  }

  public IgnoreHeaderRowField getIgnoreHeaderRowField() {
    return getFieldByClass(IgnoreHeaderRowField.class);
  }

  public ImportButton getImportButton() {
    return getFieldByClass(ImportButton.class);
  }

  public ImportExportGroup getImportExportGroup() {
    return getFieldByClass(ImportExportGroup.class);
  }

  public LanguageField getLanguageField() {
    return getFieldByClass(LanguageField.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  public OptionsBox getOptionsBox() {
    return getFieldByClass(OptionsBox.class);
  }

  public TextEnclosingField getTextEnclosingField() {
    return getFieldByClass(TextEnclosingField.class);
  }

  private void persistSettings() {
    if (!isFormLoading()) {
      // TODO MIG
//      IEclipsePreferences pref = new UserScope().getNode(com.rtiming.client.Activator.PLUGIN_ID);
//      pref.putLong(FormatField.class.getName(), getFormatField().getValue());
//      pref.putBoolean(ImportExportGroup.class.getName(), BooleanUtility.nvl(getImportExportGroup().getValue()));
//      pref.put(CharacterSetField.class.getName(), StringUtility.nvl(getCharacterSetField().getValue(), System.getProperty("file.encoding")));
//      pref.put(FieldSeparatorField.class.getName(), getFieldSeparatorField().getValue() == null ? "" : String.valueOf(getFieldSeparatorField().getValue()));
//      pref.put(TextEnclosingField.class.getName(), getTextEnclosingField().getValue() == null ? "" : String.valueOf(getTextEnclosingField().getValue()));
//      pref.putBoolean(IgnoreHeaderRowField.class.getName(), BooleanUtility.nvl(getIgnoreHeaderRowField().getValue()));
    }
  }

  private void loadSettings() {
    // TODO MIG
//    IEclipsePreferences pref = new UserScope().getNode(com.rtiming.client.Activator.PLUGIN_ID);
//    Long formatUid = pref.getLong(FormatField.class.getName(), ImportExportFormatCodeType.IOFDataStandard203CourseDataCode.ID);
//    boolean isImport = pref.getBoolean(ImportExportGroup.class.getName(), true);
//    String characterSet = pref.get(CharacterSetField.class.getName(), System.getProperty("file.encoding"));
//    String fieldSeparatorStr = pref.get(FieldSeparatorField.class.getName(), "");
//    Character fieldSeparator = (fieldSeparatorStr.length() > 0) ? fieldSeparatorStr.charAt(0) : null;
//    String textEnclosingStr = pref.get(TextEnclosingField.class.getName(), "");
//    Character textEnclosing = (textEnclosingStr.length() > 0) ? textEnclosingStr.charAt(0) : null;
//    boolean ignoreHeaderRow = pref.getBoolean(IgnoreHeaderRowField.class.getName(), true);
//
//    // set default settings
//    getFormatField().setValue(formatUid);
//    getImportExportGroup().setValue(isImport);
//    getCharacterSetField().setValue(characterSet);
//    getFieldSeparatorField().setValue(fieldSeparator);
//    getTextEnclosingField().setValue(textEnclosing);
//    getIgnoreHeaderRowField().setValue(ignoreHeaderRow);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class FormatField extends AbstractSmartField<Long> {

      @Override
      protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
        return ImportExportFormatCodeType.class;
      }

      @Override
      protected int getConfiguredGridW() {
        return 2;
      }

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Format");
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

      @Override
      protected void execChangedValue() throws ProcessingException {
        if (getValue() != null) {
          AbstractImportExportCode code = (AbstractImportExportCode) BEANS.get(ImportExportFormatCodeType.class).getCode(getValue());

          // Import/Export
          if (code.isImport() && !code.isExport()) {
            // Import Only
            getImportExportGroup().setValue(true);
            getImportExportGroup().setEnabled(false);
          }
          else if (code.isExport() && !code.isImport()) {
            // Export Only
            getImportExportGroup().setValue(false);
            getImportExportGroup().setEnabled(false);
          }
          else {
            getImportExportGroup().setEnabled(true);
          }

          // Event
          if (!code.isEventDependent()) {
            getEventField().setEnabled(false);
            getEventField().setMandatory(false);
            getEventField().setValue(null);
          }
          else {
            getEventField().setEnabled(true);
            getEventField().setMandatory(true);
            getEventField().setValue(BEANS.get(IDefaultProcessService.class).getDefaultEventNr());
          }

          // Language
          if (!code.isLanguageDependent()) {
            getLanguageField().setEnabled(false);
            getLanguageField().setMandatory(false);
            getLanguageField().setValue(null);
          }
          else {
            getLanguageField().setEnabled(true);
            getLanguageField().setMandatory(true);
            getLanguageField().setValue(ClientSession.get().getLanguageUid());
          }

          // File Extension
          // TODO MIG getFileField().setFileExtensions(new String[]{code.getFileExtension()});

          // CSV File Format
          // Separator
          if (code.getConfiguredDefaultFieldSeparator() != null) {
            getFieldSeparatorField().setValue(code.getConfiguredDefaultFieldSeparator());
            getFieldSeparatorField().setVisible(true);
            getFieldSeparatorField().setMandatory(true);
          }
          else {
            getFieldSeparatorField().setVisible(false);
            getFieldSeparatorField().setMandatory(false);
          }

          // Text Enclosing
          if (code.getConfiguredDefaultFieldSeparator() != null) {
            getTextEnclosingField().setValue(code.getConfiguredDefaultTextEnclosing());
            getTextEnclosingField().setVisible(true);
          }
          else {
            getTextEnclosingField().setVisible(false);
          }

          if (code.getConfiguredIgnoreHeaderRow() != null) {
            getIgnoreHeaderRowField().setValue(code.getConfiguredIgnoreHeaderRow());
            getIgnoreHeaderRowField().setVisible(true);
          }
          else {
            getIgnoreHeaderRowField().setVisible(false);
          }

          // finally persist settings
          // will be default on next load
          persistSettings();
        }
      }
    }

    @Order(20.0)
    public class ImportExportGroup extends AbstractRadioButtonGroup<Boolean> {

      @Override
      protected int getConfiguredGridW() {
        return 2;
      }

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Type");
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

      @Override
      protected void execChangedValue() throws ProcessingException {
        // set import or export
        if (getValue()) {
          if (getWizardStep() != null) {
            getWizardStep().setTitle(Texts.get("Import"));
          }
          // TODO MIG getFileField().setTypeLoad(true);
        }
        else {
          if (getWizardStep() != null) {
            getWizardStep().setTitle(Texts.get("Export"));
          }
          // TODO MIG getFileField().setTypeLoad(false);
        }
        // settings
        persistSettings();
      }

      @Order(10.0)
      public class ExportButton extends AbstractRadioButton {

        @Override
        protected String getConfiguredLabel() {
          return Texts.get("Export");
        }

        @Override
        protected Object getConfiguredRadioValue() {
          return false;
        }
      }

      @Order(20.0)
      public class ImportButton extends AbstractRadioButton {

        @Override
        protected String getConfiguredLabel() {
          return Texts.get("Import");
        }

        @Override
        protected Object getConfiguredRadioValue() {
          return true;
        }
      }
    }

    @Order(30.0)
    public class FileBox extends AbstractGroupBox {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("File");
      }

      @Order(10.0)
      public class FileField extends AbstractFileChooserField {

        @Override
        public List<String> getConfiguredFileExtensions() {
          return Arrays.asList(new String[]{"csv", "txt", "xml"});
        }

        @Override
        public int getConfiguredGridW() {
          return 2;
        }

        @Override
        protected String getConfiguredLabel() {
          return Texts.get("File");
        }

        @Override
        protected boolean getConfiguredMandatory() {
          return true;
        }

// TODO MIG
//        @Override
//        protected boolean getConfiguredShowDirectory() {
//          return true;
//        }

// TODO MIG        
//        @Override
//        public boolean getConfiguredTypeLoad() {
//          return true;
//        }

        @Override
        protected void execChangedValue() throws ProcessingException {

          /*
          Construct an instance of org.mozilla.universalchardet.UniversalDetector.
          Feed some data (typically several thousands bytes) to the detector by calling UniversalDetector.handleData().
          Notify the detector of the end of data by calling UniversalDetector.dataEnd().
          Get the detected encoding name by calling UniversalDetector.getDetectedCharset().
          Don't forget to call UniversalDetector.reset() before you reuse the detector instance.
           */

          if (getImportExportGroup().getValue()) {
            try {
              byte[] buf = new byte[4096];
              UniversalDetector detector = new UniversalDetector(null);

              File f = new File(getValue().getFilename());
              FileInputStream fis;
              fis = new FileInputStream(f);

              int nread;
              while (fis.available() > 0 && !detector.isDone()) {
                nread = fis.read(buf);
                detector.handleData(buf, 0, nread);
              }

              detector.dataEnd();
              fis.close();

              if (detector.getDetectedCharset() != null) {
                String detectedCharset = detector.getDetectedCharset();
                Charset cs = Charset.forName(detectedCharset);
                getCharacterSetField().setValue(cs.toString());
              }
            }
            catch (Exception e) {
              if (e instanceof FileNotFoundException) {
                throw new VetoException(TEXTS.get("FileReadException"));
              }
              else {
                throw new ProcessingException(e.getMessage());
              }
            }
          }

        }
      }

      @Order(20.0)
      public class FieldSeparatorField extends AbstractSmartField<Character> {

        @Override
        protected String getConfiguredLabel() {
          return Texts.get("FieldSeparator");
        }

        @Override
        protected Class<? extends LookupCall<Character>> getConfiguredLookupCall() {
          return FieldSeparatorCharacterLookupCall.class;
        }

        @Override
        protected boolean getConfiguredMandatory() {
          return true;
        }

        @Override
        protected void execChangedValue() throws ProcessingException {
          persistSettings();
        }
      }

      @Order(40.0)
      public class TextEnclosingField extends AbstractSmartField<Character> {

        @Override
        protected String getConfiguredLabel() {
          return Texts.get("TextEnclosing");
        }

        @Override
        protected Class<? extends LookupCall<Character>> getConfiguredLookupCall() {
          return TextEnclosingCharacterLookupCall.class;
        }

        @Override
        protected void execChangedValue() throws ProcessingException {
          persistSettings();
        }
      }

      @Order(30.0)
      public class CharacterSetField extends AbstractSmartField<String> {

        @Override
        protected String getConfiguredLabel() {
          return Texts.get("CharacterSet");
        }

        @Override
        protected Class<? extends LookupCall<String>> getConfiguredLookupCall() {
          return CharacterSetLookupCall.class;
        }

        @Override
        protected boolean getConfiguredMandatory() {
          return true;
        }

        @Override
        protected void execChangedValue() throws ProcessingException {
          persistSettings();
        }
      }

      @Order(50.0)
      public class IgnoreHeaderRowField extends AbstractBooleanField {

        @Override
        protected String getConfiguredLabel() {
          return Texts.get("IgnoreHeaderRow");
        }

        @Override
        protected void execChangedValue() throws ProcessingException {
          persistSettings();
        }

      }
    }

    @Order(40.0)
    public class OptionsBox extends AbstractGroupBox {

      @Override
      protected String getConfiguredLabel() {
        return ScoutTexts.get("Options");
      }

      @Order(10.0)
      public class EventField extends AbstractEventField {
      }

      @Order(20.0)
      public class LanguageField extends AbstractSmartField<Long> {

        @Override
        protected String getConfiguredLabel() {
          return ScoutTexts.get("Language");
        }

        @Override
        protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
          return LanguageCodeType.class;
        }
      }
    }

    @Order(58.0)
    public class HelpLink extends AbstractHelpLinkButton {
    }

    @Order(60.0)
    public class OkButton extends AbstractOkButton {
    }

    @Order(70.0)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      getOkButton().setVisible(false);
      getCancelButton().setVisible(false);

      // load default settings
      loadSettings();
    }

    @Override
    public void execStore() throws ProcessingException {
      if (getImportExportGroup().getValue()) {
        // check file existence on import
// TODO MIG        
//        File check = new File(getFileField().getValue());
//        if (!check.exists()) {
//          throw new VetoException(TEXTS.get("FileReadException"));
//        }
      }
    }
  }
}
