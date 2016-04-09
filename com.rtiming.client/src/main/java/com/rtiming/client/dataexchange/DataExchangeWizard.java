package com.rtiming.client.dataexchange;

import java.util.ArrayList;

import org.eclipse.scout.rt.client.ui.basic.table.HeaderCell;
import org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.client.ui.form.fields.GridData;
import org.eclipse.scout.rt.client.ui.wizard.AbstractWizard;
import org.eclipse.scout.rt.client.ui.wizard.AbstractWizardStep;
import org.eclipse.scout.rt.client.ui.wizard.DefaultWizardContainerForm;
import org.eclipse.scout.rt.client.ui.wizard.IWizardContainerForm;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.client.FMilaClientUtility;
import com.rtiming.client.dataexchange.DataExchangePreviewForm.MainBox.PreviewDataField.Table;
import com.rtiming.shared.Texts;
import com.rtiming.shared.dataexchange.DataExchangeStartFormData;

public class DataExchangeWizard extends AbstractWizard {

  private AbstractInterface<?> currentInterface;
  private Object[][] m_previewData;
  private ArrayList<String> m_columnHeaders;
  private DataExchangeStartForm startForm;

  public DataExchangeWizard() {
    super();
  }

// TODO MIG  
//  @Override
//  protected int getConfiguredModalityHint() {
//    return IForm.MODALITY_HINT_MODAL;
//  }

  @Override
  protected String getConfiguredTitle() {
    return ScoutTexts.get("RepProgSync");
  }

  @Order(10.0)
  public class InterfaceSettingsStep extends AbstractWizardStep<DataExchangeStartForm> {

    @Override
    protected String getConfiguredTitle() {
      return Texts.get("Import");
    }

    @Override
    protected void execActivate(int stepKind) throws ProcessingException {
      if (stepKind == STEP_NEXT) {
        startForm = new DataExchangeStartForm();
        startForm.startWizardStep(this, DataExchangeStartForm.NewHandler.class);
        setWizardForm(startForm);
        setForm(startForm);
      }
      else {
        setWizardForm(startForm);
        setForm(startForm);
      }
    }

    @Override
    protected void execDeactivate(int stepKind) throws ProcessingException {
      if (stepKind == STEP_NEXT) {
        DataExchangeStartForm form = getForm();
        form.doSaveWithoutMarkerChange();

        DataExchangeStartFormData formData = new DataExchangeStartFormData();
        form.exportFormData(formData);

        currentInterface = InterfaceMapper.createInterface(form.getFormatField().getValue(), formData);

        if (currentInterface.isImport()) {
          // (1 Import) load from file
          currentInterface.fileToPreview();
        }
        else {
          // (1 Export) load from database OR
          currentInterface.databaseToPreview();
        }
        // (2 Export and Import) create preview
        currentInterface.createPreviewData();

        m_previewData = currentInterface.getPreview().getData();
        m_columnHeaders = currentInterface.getColumnHeaders();
      }
    }

  }

  @Order(20.0)
  public class PreviewStep extends AbstractWizardStep<DataExchangePreviewForm> {

    @Override
    protected String getConfiguredTitle() {
      return Texts.get("Preview");
    }

    @Override
    protected void execActivate(int stepKind) throws ProcessingException {

      // add preview data
      DataExchangePreviewForm form = null;
      boolean previewHasErrors = false;

      // create preview table
      if (m_previewData != null && m_previewData.length > 0) {
        form = new DataExchangePreviewForm(m_previewData[0].length);
        form.startWizardStep(this, DataExchangePreviewForm.NewHandler.class);

        Table previewTable = form.getPreviewDataField().getTable();
        previewTable.addRowsByMatrix(m_previewData);
        boolean firstRow = true;
        for (int i = 0; i < previewTable.getRowCount(); i++) {
          for (int k = 0; k < m_previewData[i].length; k++) {
            if (firstRow) {
              // set header texts
              IColumn<?> col = previewTable.getColumns().get(i);
              col.setDisplayable(true);
              col.setVisible(true);
              HeaderCell c = (HeaderCell) col.getHeaderCell();
              c.setText(m_columnHeaders.get(k));
              col.setInitialSortIndex(1);
            }
            // apply pre-checks warnings
            if (currentInterface.getPreview().getErrors() != null) {
              if (currentInterface.getPreview().getErrors()[i][k] != null) {
                previewTable.getRow(i).getCellForUpdate(k).setTooltipText(currentInterface.getPreview().getErrors()[i][k]);
                previewTable.getRow(i).getCellForUpdate(k).setBackgroundColor("F78181");
                previewTable.getRow(i).setForegroundColor("FF0000");
                previewHasErrors = true;
              }
            }
          }
          firstRow = false;
        }
      }
      else {
        form = new DataExchangePreviewForm(0);
        form.startWizardStep(this, DataExchangePreviewForm.NewHandler.class);
      }

      if (previewHasErrors) {
        form.getPreviewInfoField().setVisible(true);
        form.getNextErrorButton().setVisible(true);
        form.getPreviewInfoField().setValue(TEXTS.get("InvalidRowsWarning"));
        form.getNextErrorButton().doClick();
      }

      setWizardForm(form);
    }
  }

  @Order(30.0)
  public class FinalizationStep extends AbstractWizardStep<DataExchangeFinalizationForm> {

    @Override
    protected String getConfiguredTitle() {
      return Texts.get("Finalization");
    }

    @Override
    protected void execActivate(int stepKind) throws ProcessingException {
      DataExchangeFinalizationForm form = new DataExchangeFinalizationForm();
      form.startWizardStep(this, DataExchangeFinalizationForm.NewHandler.class);
      setWizardForm(form);
      getContainerForm().getWizardCancelButton().setEnabled(false);
      getContainerForm().getWizardPreviousStepButton().setEnabled(false);

      if (currentInterface.isImport()) {
        // (3 Import) store to Database
        currentInterface.previewToDatabase(new ProgressMonitor(form.getInfoField()));
      }
      else {
        // (3 Export) export as CSV or XML
        currentInterface.previewToFile(new ProgressMonitor(form.getInfoField()));
      }

      form.setFullPathName(currentInterface.getFullPathName());

    }
  }

  @Override
  protected IWizardContainerForm execCreateContainerForm() throws ProcessingException {

    DefaultWizardContainerForm containerForm = (DefaultWizardContainerForm) super.execCreateContainerForm();
    containerForm.getWizardCancelButton().setVisible(true);
    // containerForm.setDisplayHint(getDisplayHint()); TODO MIG

    GridData data = containerForm.getRootGroupBox().getGridData();
    data.widthInPixel = 950;
    data.heightInPixel = 400;
    containerForm.getRootGroupBox().setGridDataInternal(data);

    // TODO MIG
    // containerForm.setDisplayViewId(getDisplayViewId());
    // containerForm.setModal(isModal());

    return containerForm;

  }

  @Override
  protected void execFinish() throws ProcessingException {
    super.execFinish();
    IPage activePage = getDesktop().getOutline().getActivePage();
    if (activePage != null) {
      if (!FMilaClientUtility.isTestEnvironment()) {
        activePage.reloadPage();
      }
    }
  }

}
