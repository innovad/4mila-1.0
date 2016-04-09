package com.rtiming.client.common.report.template;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.UUID;

import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractLinkButton;
import org.eclipse.scout.rt.client.ui.form.fields.filechooserfield.AbstractFileChooserField;
import org.eclipse.scout.rt.client.ui.form.fields.sequencebox.AbstractSequenceBox;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.IOUtility;

import com.rtiming.shared.Texts;
import com.rtiming.shared.services.code.AbstractReportTypeCode.ReportTemplateType;

@Order(50.0)
public class TemplateBox extends AbstractSequenceBox implements ITemplateField {

  private final FileNameField innerFileNameField;
  private final OpenTemplateLink innerLinkButton;

  public TemplateBox(String label, String fileSuffix, ReportTemplateType templateType, boolean isMaster) {
    super(false);
    innerFileNameField = new FileNameField(label, fileSuffix, templateType, isMaster);
    innerLinkButton = new OpenTemplateLink();
    this.callInitializer();
  }

  @Override
  protected int getConfiguredGridW() {
    return 2;
  }

// TODO MIG  
//  @Override
//  protected void injectFieldsInternal(List<IFormField> fieldList) {
//    fieldList.add(innerFileNameField);
//    fieldList.add(innerLinkButton);
//    super.injectFieldsInternal(fieldList);
//  }

  private FileNameField getFileNameField() {
    return innerFileNameField;
  }

// TODO MIG  
//  @Override
//  public void setValue(String absolutePath) {
//    getFileNameField().setValue(absolutePath);
//  }
//

  @Override
  public void setDirectory(File localTemporaryReportDir) {
    // TODO MIG getFileNameField().setDirectory(localTemporaryReportDir);
  }

  @Override
  public void setServerLastModified(long lastModified) {
    getFileNameField().setServerLastModified(lastModified);
  }

  @Override
  public long getServerLastModified() {
    return getFileNameField().getServerLastModified();
  }

  @Override
  public void setLocalLastModified(long lastModified) {
    getFileNameField().setLocalLastModified(lastModified);
  }

  @Override
  public ReportTemplateType getTemplateType() {
    return getFileNameField().getTemplateType();
  }

  @Override
  public long getLocalLastModified() {
    return getFileNameField().getLocalLastModified();
  }

  @Override
  public File getDirectory() {
    // TODO MIG return null;
    // return getFileNameField().getDirectory();
    return null;
  }

  @Override
  public String getFileName() {
    return getFileNameField().getFileName();
  }

  @Override
  public String getValue() {
    return getFileNameField().getValue().getFilename();
  }

  @Override
  public boolean isMaster() {
    return getFileNameField().isMaster();
  }

  class FileNameField extends AbstractFileChooserField {

    private String templateContent;
    private final ReportTemplateType templateType;
    private final boolean isMaster;
    private long serverLastModified;
    private long localLastModified;

    public FileNameField(String label, String fileSuffix, ReportTemplateType templateType, boolean isMaster) {
      super();
      this.templateType = templateType;
      this.isMaster = isMaster;
      this.serverLastModified = 0;
      this.localLastModified = 0;
      // TODO MIG setFileExtensions(new String[]{fileSuffix});
      setLabel(label);
    }

    @Override
    protected int getConfiguredGridW() {
      return 2;
    }

    @Override
    public String getFieldId() {
      return UUID.randomUUID().toString();
    }

    @Override
    protected boolean getConfiguredMandatory() {
      return true;
    }

// TODO MIG    
//    @Override
//    protected boolean getConfiguredTypeLoad() {
//      return true;
//    }

    public ReportTemplateType getTemplateType() {
      return templateType;
    }

    public void setServerLastModified(long lastModified) {
      this.serverLastModified = lastModified;
    }

    public long getServerLastModified() {
      return serverLastModified;
    }

    public void setLocalLastModified(long lastModified) {
      this.localLastModified = lastModified;
    }

    public long getLocalLastModified() {
      return localLastModified;
    }

    @Override
    protected void execChangedValue() throws ProcessingException {
      String path = getValue().getFilename();
      if (IOUtility.fileExists(path)) {
        try {
          templateContent = IOUtility.getContent(new FileReader(new File(path)));
        }
        catch (FileNotFoundException e) {
          throw new ProcessingException("Cannot read file", e);
        }
      }
    }

    public boolean isMaster() {
      return isMaster;
    }

  }

  class OpenTemplateLink extends AbstractLinkButton {

    @Override
    protected String getConfiguredLabel() {
      return Texts.get("EditMenu");
    }

    @Override
    protected void execClickAction() throws ProcessingException {
      // TODO MIG FMilaClientUtility.openDocument(innerFileNameField.getValue());
    }

    @Override
    protected String getConfiguredTooltipText() {
      return Texts.get("EditTemplateTooltip");
    }

  }

  // TODO MIG
  @Override
  public void setValue(String absolutePath) {
  }

}
