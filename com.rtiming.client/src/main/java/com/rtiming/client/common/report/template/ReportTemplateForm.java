package com.rtiming.client.common.report.template;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.IFormField;
import org.eclipse.scout.rt.client.ui.form.fields.booleanfield.AbstractBooleanField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.FileUtility;
import org.eclipse.scout.rt.platform.util.IOUtility;
import org.eclipse.scout.rt.platform.util.collection.OrderedCollection;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.CODES;
import org.eclipse.scout.rt.shared.services.common.code.ICode;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

import com.rtiming.client.common.help.AbstractHelpLinkButton;
import com.rtiming.client.common.report.TemplateSelectionUtility;
import com.rtiming.client.common.report.template.ReportTemplateForm.MainBox.ActiveField;
import com.rtiming.client.common.report.template.ReportTemplateForm.MainBox.CancelButton;
import com.rtiming.client.common.report.template.ReportTemplateForm.MainBox.EventField;
import com.rtiming.client.common.report.template.ReportTemplateForm.MainBox.OkButton;
import com.rtiming.client.common.report.template.ReportTemplateForm.MainBox.ReportTypeField;
import com.rtiming.client.common.report.template.ReportTemplateForm.MainBox.ShortcutField;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.common.database.sql.BeanUtility;
import com.rtiming.shared.common.report.template.IReportTemplateProcessService;
import com.rtiming.shared.common.report.template.ReportTemplateFormData;
import com.rtiming.shared.common.security.permission.UpdateReportTemplatePermission;
import com.rtiming.shared.dao.RtReportTemplateFile;
import com.rtiming.shared.dao.RtReportTemplateKey;
import com.rtiming.shared.event.EventLookupCall;
import com.rtiming.shared.services.code.AbstractReportTypeCode;
import com.rtiming.shared.services.code.AbstractReportTypeCode.ReportTemplateType;
import com.rtiming.shared.services.code.ReportTypeCodeType;

@FormData(value = ReportTemplateFormData.class, sdkCommand = SdkCommand.CREATE)
public class ReportTemplateForm extends AbstractForm {

  private RtReportTemplateKey key;
  private Set<RtReportTemplateFile> templateFiles;
  private List<ITemplateField> templateFields;
  private File localTemporaryReportDir;

  public ReportTemplateForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("ReportTemplate");
  }

  @FormData
  public RtReportTemplateKey getKey() {
    return key;
  }

  @FormData
  public void setKey(RtReportTemplateKey key) {
    this.key = key;
  }

  @FormData
  public Set<RtReportTemplateFile> getTemplateFiles() {
    return templateFiles;
  }

  @FormData
  public void setTemplateFiles(Set<RtReportTemplateFile> templateFiles) {
    this.templateFiles = templateFiles;
  }

  public void startModify() throws ProcessingException {
    startInternal(new ModifyHandler());
  }

  public void startNew() throws ProcessingException {
    startInternal(new NewHandler());
  }

  public ActiveField getActiveField() {
    return getFieldByClass(ActiveField.class);
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  public EventField getEventField() {
    return getFieldByClass(EventField.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  public ReportTypeField getReportTypeField() {
    return getFieldByClass(ReportTypeField.class);
  }

  public ShortcutField getShortcutField() {
    return getFieldByClass(ShortcutField.class);
  }

  private void validateReportType() throws ProcessingException {
    Long id = getReportTypeField().getValue();
    if (id != null) {
      ICode<?> code = CODES.getCodeType(ReportTypeCodeType.class).getCode(id);
      if (code instanceof AbstractReportTypeCode) {
        AbstractReportTypeCode reportCode = (AbstractReportTypeCode) code;
        ReportTemplateType type = reportCode.getConfiguredReportTemplateType();

        int countVisibleFields = 0;
        for (ITemplateField field : templateFields) {
          boolean typeMatch = type.equals(field.getTemplateType());
          field.setVisible(typeMatch);
          field.setMandatory(typeMatch);
          if (!typeMatch) {
            field.setValue(null);
          }
          else if (getHandler() instanceof ReportTemplateForm.NewHandler) {
            List<RtReportTemplateFile> defaultTemplates = reportCode.getConfiguredDefaultTemplates();
            RtReportTemplateFile defaultTemplate = (defaultTemplates.toArray(new RtReportTemplateFile[defaultTemplates.size()]))[countVisibleFields];
            String templateFileName = defaultTemplate.getTemplateFileName();
            File copy = new File(localTemporaryReportDir.getAbsolutePath() + FMilaUtility.FILE_SEPARATOR + templateFileName);
            try {
              FileUtility.copyFile(new File(TemplateSelectionUtility.findDefaultTemplatePath(templateFileName)), copy);
            }
            catch (IOException e) {
              throw new ProcessingException("Failed to load default templates", e);
            }
            field.setValue(copy.getAbsolutePath());
            field.setDirectory(localTemporaryReportDir);
            countVisibleFields++;
          }
        }
      }
    }
    else {
      for (ITemplateField field : templateFields) {
        field.setValue(null);
      }
    }
  }

  private void readTemplateDataForStorage() throws ProcessingException {
    if (getTemplateFiles() == null) {
      setTemplateFiles(new HashSet<RtReportTemplateFile>());
    }
    for (RtReportTemplateFile file : getTemplateFiles()) {
      file.setTemplateFileName(null);
      file.setTemplateContent(null);
    }
    int count = 0;
    for (IFormField field : getAllFields()) {
      if (field instanceof TemplateBox && field.isVisible()) {
        TemplateBox templateField = (TemplateBox) field;
        RtReportTemplateFile file = new RtReportTemplateFile();
        if (count < getTemplateFiles().size()) {
          file = CollectionUtility.toArray(getTemplateFiles(), RtReportTemplateFile.class)[count];
        }
        try {
          file.setTemplateContent(IOUtility.getContent(new FileReader(templateField.getValue())));
          file.setLastModified(IOUtility.getFileLastModified(templateField.getValue()));
        }
        catch (FileNotFoundException e) {
          throw new ProcessingException("Unable to read file", e);
        }
        file.setTemplateFileName(templateField.getFileName());
        file.setMaster(templateField.isMaster());
        getTemplateFiles().add(file);
        count++;
      }
    }
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Override
    protected void injectFieldsInternal(OrderedCollection<IFormField> fieldList) {
      TemplateBox field1 = new TemplateBox(TEXTS.get("ReportTemplateHeaderFooter"), IReportTemplateProcessService.TEMPLATE_SUFFIX_JRXML, ReportTemplateType.GENERIC_TABLE, true);
      TemplateBox field2 = new TemplateBox(TEXTS.get("ReportTemplateStyle"), IReportTemplateProcessService.TEMPLATE_SUFFIX_JRTX, ReportTemplateType.GENERIC_TABLE, false);
      TemplateBox field3 = new TemplateBox(TEXTS.get("ReportTemplateMain"), IReportTemplateProcessService.TEMPLATE_SUFFIX_JRXML, ReportTemplateType.CUSTOM_TEMPLATE, true);
      TemplateBox field4 = new TemplateBox(TEXTS.get("ReportTemplateTable"), IReportTemplateProcessService.TEMPLATE_SUFFIX_JRXML, ReportTemplateType.CUSTOM_TEMPLATE, false);
      templateFields = new ArrayList<>();
      templateFields.add(field1);
      templateFields.add(field2);
      templateFields.add(field3);
      templateFields.add(field4);
      fieldList.addLast(field1);
      fieldList.addLast(field2);
      fieldList.addLast(field3);
      fieldList.addLast(field4);
      super.injectFieldsInternal(fieldList);
    }

    @Order(10.0)
    public class ShortcutField extends AbstractStringField {

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Shortcut");
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

      @Override
      protected int getConfiguredMaxLength() {
        return 60;
      }

    }

    @Order(20.0)
    public class ReportTypeField extends AbstractSmartField<Long> {

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return ReportTypeCodeType.class;
      }

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("ReportType");
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

      @Override
      protected void execChangedValue() throws ProcessingException {
        validateReportType();
      }
    }

    @Order(30.0)
    public class EventField extends AbstractSmartField<Long> {

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Event");
      }

      @Override
      protected Class<? extends LookupCall<Long>> getConfiguredLookupCall() {
        return EventLookupCall.class;

      }
    }

    @Order(40.0)
    public class ActiveField extends AbstractBooleanField {

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Active");
      }
    }

    @Order(50.0)
    public class HelpLink extends AbstractHelpLinkButton {
    }

    @Order(60.0)
    public class OkButton extends AbstractOkButton {
    }

    @Order(70.0)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  @Override
  protected boolean execValidate() throws ProcessingException {
    for (ITemplateField field : templateFields) {
      if (field.isVisible()) {
        long currentLastMod = IOUtility.getFileLastModified(field.getDirectory().getAbsolutePath() + FMilaUtility.FILE_SEPARATOR + field.getFileName());
        if (currentLastMod != field.getLocalLastModified()) {
          touch();
          break;
        }
      }
    }
    return super.execValidate();
  }

  public class ModifyHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      String systemTempDir = System.getProperty("java.io.tmpdir");
      String localDirPath = systemTempDir + "modifyReportTemplate" + getKey().getId();
      IOUtility.createDirectory(localDirPath);
      localTemporaryReportDir = new File(localDirPath);
      if (!localTemporaryReportDir.exists()) {
        throw new ProcessingException("Cannot create local report directory, " + localTemporaryReportDir.getAbsolutePath());
      }

      IReportTemplateProcessService service = BEANS.get(IReportTemplateProcessService.class);
      ReportTemplateFormData formData = new ReportTemplateFormData();
      exportFormData(formData);
      formData = BeanUtility.reportTemplateBean2FormData(service.load(BeanUtility.reportTemplateFormData2bean(formData).getId()));
      importFormData(formData);
      setEnabledPermission(new UpdateReportTemplatePermission());

      validateReportType();
      getReportTypeField().setEnabled(false);

      boolean closeForm = ReportTemplateFormUtility.loadFilesFromRemoteServer(getTemplateFiles(), templateFields, localTemporaryReportDir);
      if (closeForm) {
        doClose();
      }
    }

    @Override
    public void execStore() throws ProcessingException {
      readTemplateDataForStorage();
      IReportTemplateProcessService service = BEANS.get(IReportTemplateProcessService.class);
      ReportTemplateFormData formData = new ReportTemplateFormData();
      exportFormData(formData);
      formData = BeanUtility.reportTemplateBean2FormData(service.store(BeanUtility.reportTemplateFormData2bean(formData)));
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      localTemporaryReportDir = IOUtility.createTempDirectory("template");

      IReportTemplateProcessService service = BEANS.get(IReportTemplateProcessService.class);
      ReportTemplateFormData formData = new ReportTemplateFormData();
      exportFormData(formData);
      formData = BeanUtility.reportTemplateBean2FormData(service.prepareCreate(BeanUtility.reportTemplateFormData2bean(formData)));
      importFormData(formData);

      // validate and set default
      getReportTypeField().setValue(ReportTypeCodeType.ResultsCode.ID);
    }

    @Override
    public void execStore() throws ProcessingException {
      readTemplateDataForStorage();
      IReportTemplateProcessService service = BEANS.get(IReportTemplateProcessService.class);
      ReportTemplateFormData formData = new ReportTemplateFormData();
      exportFormData(formData);
      formData = BeanUtility.reportTemplateBean2FormData(service.create(BeanUtility.reportTemplateFormData2bean(formData)));
      importFormData(formData);
    }

    @SuppressWarnings("unchecked")
    private Class<? extends AbstractReportTypeCode> getReportTypeCodeClass() {
      Class<? extends ICode> reportTypeCodeClass = CODES.getCodeType(ReportTypeCodeType.class).getCode(getReportTypeField().getValue()).getClass();
      Class<? extends AbstractReportTypeCode> clazz = null;
      if (AbstractReportTypeCode.class.isAssignableFrom(reportTypeCodeClass)) {
        clazz = (Class<? extends AbstractReportTypeCode>) reportTypeCodeClass;
      }
      return clazz;
    }
  }
}
