package com.rtiming.client.common.report;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.client.ui.form.fields.IFormField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractLinkButton;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.common.code.CODES;
import org.eclipse.scout.rt.shared.services.common.code.ICode;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.common.report.template.ReportTemplateForm;
import com.rtiming.client.common.report.template.ReportTemplateForm.MainBox.HelpLink;
import com.rtiming.client.common.report.template.TemplateBox;
import com.rtiming.client.test.AbstractFormTest;
import com.rtiming.shared.services.code.AbstractReportTypeCode;
import com.rtiming.shared.services.code.ReportTypeCodeType;

/**
 * @since 1.0.5
 */
@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestEnvironmentClientSession.class)
public class ReportTemplateFormTest extends AbstractFormTest<ReportTemplateForm> {

  @Override
  protected ReportTemplateForm getStartedForm() throws ProcessingException {
    ReportTemplateForm form = new ReportTemplateForm();
    form.startNew();
    return form;
  }

  @Override
  protected ReportTemplateForm getModifyForm() throws ProcessingException {
    ReportTemplateForm form = new ReportTemplateForm();
    form.setKey(getForm().getKey());
    form.startModify();
    return form;
  }

  @Override
  @After
  public void cleanup() throws ProcessingException {
  }

  @Override
  protected List<String> getFieldIdsToIgnore() {
    List<String> list = new ArrayList<String>();
    list.add(getForm().getFieldByClass(TemplateBox.class).getFieldId());
    return list;
  }

  @Test
  public void testDefault() throws Exception {
    ReportTemplateForm form = getStartedForm();
    Long typeUid = ReportTypeCodeType.ResultsCode.ID;
    Assert.assertEquals("Default is Result", typeUid, form.getReportTypeField().getValue());
    assertReportFileNames(form, typeUid);
    form.doClose();
  }

  @Test
  public void testValidateTemplateChange() throws Exception {
    ReportTemplateForm form = getStartedForm();
    for (ICode<?> code : CODES.getCodeType(ReportTypeCodeType.class).getCodes()) {
      Assert.assertTrue(code instanceof AbstractReportTypeCode);
      Long typeUid = ((AbstractReportTypeCode) code).getId();
      form.getReportTypeField().setValue(typeUid);
      assertReportFileNames(form, typeUid);
    }
    form.doClose();
  }

  @Test
  public void testValidateNull() throws Exception {
    ReportTemplateForm form = getStartedForm();
    form.getReportTypeField().setValue(null);
    for (IFormField field : form.getAllFields()) {
      if (field instanceof TemplateBox && field.isVisible()) {
        TemplateBox templateFileNameField = (TemplateBox) field;
        Assert.assertNull(templateFileNameField.getValue());
      }
    }
    form.doClose();
  }

  @Test
  public void testEditWithJasperSoftStudio() throws Exception {
    ReportTemplateForm form = getStartedForm();
    int openCounter = 0;
    for (IFormField field : form.getAllFields()) {
      if (field instanceof AbstractLinkButton &&
          !(field instanceof HelpLink) &&
          field.getParentField().isVisible()) {
        ((AbstractLinkButton) field).doClick();
        openCounter++;
      }
    }
    Assert.assertEquals("2 Files opened", 2, openCounter);
    form.doClose();
  }

  private void assertReportFileNames(ReportTemplateForm form, Long typeUid) {
    AbstractReportTypeCode code = (AbstractReportTypeCode) CODES.getCodeType(ReportTypeCodeType.class).getCode(typeUid);
    int count = 0;
    for (IFormField field : form.getAllFields()) {
      if (field instanceof TemplateBox && field.isVisible()) {
        TemplateBox templateFileNameField = (TemplateBox) field;
        Assert.assertEquals("Default File Name", code.getConfiguredDefaultTemplates().get(count).getTemplateFileName(), templateFileNameField.getFileName());
        count++;
      }
    }
  }

}
