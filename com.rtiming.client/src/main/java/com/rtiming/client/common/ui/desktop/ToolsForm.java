package com.rtiming.client.common.ui.desktop;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.session.ClientSessionProvider;
import org.eclipse.scout.rt.client.ui.basic.table.ITable;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractLinkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.client.common.excel.ExcelUtility;
import com.rtiming.client.common.report.ReportType;
import com.rtiming.client.common.report.dynamic.FMilaGenericTableReport;
import com.rtiming.client.common.report.dynamic.GenericTableReportContent;
import com.rtiming.client.common.ui.desktop.ToolsForm.MainBox.ToolsBox;
import com.rtiming.client.common.ui.desktop.ToolsForm.MainBox.ToolsBox.DataExchangeButton;
import com.rtiming.client.common.ui.desktop.ToolsForm.MainBox.ToolsBox.LogoutButton;
import com.rtiming.client.common.ui.desktop.ToolsForm.MainBox.ToolsBox.NewEntryButton;
import com.rtiming.client.dataexchange.DataExchangeWizard;
import com.rtiming.client.ecard.download.DownloadedECardsSearchForm;
import com.rtiming.client.entry.EntryForm;
import com.rtiming.client.event.EventNodePage;
import com.rtiming.client.result.SingleEventSearchForm;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.common.security.permission.CreateDataExchangePermission;
import com.rtiming.shared.services.code.ReportTypeCodeType;

public class ToolsForm extends AbstractForm {

  public ToolsForm() throws ProcessingException {
    super();
  }

  public class FormHandler extends AbstractFormHandler {
  }

  public DataExchangeButton getDataExchangeButton() {
    return getFieldByClass(DataExchangeButton.class);
  }

  public LogoutButton getLogoutButton() {
    return getFieldByClass(LogoutButton.class);
  }

  public NewEntryButton getNewEntryButton() {
    return getFieldByClass(NewEntryButton.class);
  }

  public ToolsBox getToolsBox() {
    return getFieldByClass(ToolsBox.class);
  }

  public void startForm() throws ProcessingException {
    startInternal(new FormHandler());
  }

  @Override
  protected boolean getConfiguredAskIfNeedSave() {
    return false;
  }

  @Override
  protected int getConfiguredDisplayHint() {
    return DISPLAY_HINT_VIEW;
  }

  @Override
  protected String getConfiguredDisplayViewId() {
    return VIEW_ID_SE;
  }

  @Override
  protected int getConfiguredModalityHint() {
    return IForm.MODALITY_HINT_MODELESS;
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Override
    protected int getConfiguredGridColumnCount() {
      return 1;
    }

    @Order(30.0)
    public class ToolsBox extends AbstractGroupBox {

      @Override
      protected String getConfiguredBorderDecoration() {
        return BORDER_DECORATION_EMPTY;
      }

      @Order(10.0)
      public class DataExchangeButton extends AbstractLinkButton {

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("DataExchangeMenu");
        }

        @Override
        protected boolean getConfiguredProcessButton() {
          return false;
        }

        @Override
        protected void execInitField() throws ProcessingException {
          setVisiblePermission(new CreateDataExchangePermission());
        }

        @Override
        protected void execClickAction() throws ProcessingException {
          DataExchangeWizard wiz = new DataExchangeWizard();
          wiz.start();
        }

      }

      @Order(20.0)
      public class NewEntryButton extends AbstractLinkButton {

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("NewEntryMenu");
        }

        @Override
        protected void execInitField() throws ProcessingException {
          setLabel(getConfiguredLabel() + " (" + StringUtility.uppercase(FMilaUtility.NEW_ENTRY_KEY_STROKE) + ")");
        }

        @Override
        protected boolean getConfiguredProcessButton() {
          return false;
        }

        @Override
        protected void execClickAction() throws ProcessingException {
          EntryForm form = new EntryForm();
          form.startNew();
          form.waitFor();
          if (form.isFormStored()) {
            getDesktop().getOutline().getActivePage().reloadPage();
          }
        }

      }

      @Order(30.0)
      public class ExcelExportButton extends AbstractLinkButton {

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("ExcelExport");
        }

        @Override
        protected boolean getConfiguredProcessButton() {
          return false;
        }

        @Override
        protected void execClickAction() throws ProcessingException {
          ITable table = getDesktop().getOutline().getDetailTable();
          ExcelUtility.export(table);
        }

      }

      @Order(35.0)
      public class PDFExportButton extends AbstractLinkButton {

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("PDFExport");
        }

        @Override
        protected boolean getConfiguredProcessButton() {
          return false;
        }

        @Override
        protected void execClickAction() throws ProcessingException {
          FMilaGenericTableReport report = buildReport();
          report.createReport(ReportType.PDF, false, null, true);
        }

      }

      @Order(36.0)
      public class HTMLExportButton extends AbstractLinkButton {

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("HTMLExport");
        }

        @Override
        protected boolean getConfiguredProcessButton() {
          return false;
        }

        @Override
        protected void execClickAction() throws ProcessingException {
          FMilaGenericTableReport report = buildReport();
          report.createReport(ReportType.HTML, false, null, true);
        }

      }

      @Order(40.0)
      public class LogoutButton extends AbstractLinkButton {

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Logout");
        }

        @Override
        protected void execInitField() throws ProcessingException {
          setVisible(FMilaUtility.isWebClient());
        }

        @Override
        protected boolean getConfiguredProcessButton() {
          return false;
        }

        @Override
        public void execClickAction() throws ProcessingException {
          ClientSessionProvider.currentSession().stop();
        }

      }

    }
  }

  /**
   * @return the nearest eventNr
   */
  private Long findEventNr() {
    IForm currentSearchForm = getDesktop().getPageSearchForm();
    if (currentSearchForm != null && currentSearchForm instanceof SingleEventSearchForm) {
      return ((SingleEventSearchForm) currentSearchForm).getEventField().getValue();
    }
    if (currentSearchForm != null && currentSearchForm instanceof DownloadedECardsSearchForm) {
      return ((DownloadedECardsSearchForm) currentSearchForm).getEventField().getValue();
    }

    IPage page = getDesktop().getOutline().getActivePage().getParentPage();
    while (page != null) {
      if (page instanceof EventNodePage) {
        return ((EventNodePage) page).getEventNr();
      }
      page = page.getParentPage();
    }

    return null;
  }

  private FMilaGenericTableReport buildReport() throws ProcessingException {
    FMilaGenericTableReport report = new FMilaGenericTableReport();
    List<GenericTableReportContent> contents = new ArrayList<>();
    GenericTableReportContent content = new GenericTableReportContent();
    content.setTable(getDesktop().getPageDetailTable());
    contents.add(content);
    report.setContent(contents);
    report.setTemplate(ReportTypeCodeType.DefaultCode.ID, findEventNr());
    return report;
  }

}
