package com.rtiming.client.dataexchange;

import org.eclipse.scout.commons.annotations.FormData;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.customizer.ITableCustomizer;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.labelfield.AbstractLabelField;
import org.eclipse.scout.rt.client.ui.form.fields.tablefield.AbstractTableField;

import com.rtiming.client.dataexchange.DataExchangePreviewForm.MainBox.NextErrorButton;
import com.rtiming.client.dataexchange.DataExchangePreviewForm.MainBox.PreviewDataField;
import com.rtiming.client.dataexchange.DataExchangePreviewForm.MainBox.PreviewDataField.Table;
import com.rtiming.client.dataexchange.DataExchangePreviewForm.MainBox.PreviewInfoField;
import com.rtiming.shared.Texts;
import com.rtiming.shared.dataexchange.DataExchangePreviewFormData;

@FormData(value = DataExchangePreviewFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class DataExchangePreviewForm extends AbstractForm {

  private final int columnCount;

  public DataExchangePreviewForm(int columnCount) throws ProcessingException {
    super(false);
    this.columnCount = columnCount;
    callInitializer();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Preview");
  }

  public void startNew() throws ProcessingException {
    startInternal(new DataExchangePreviewForm.NewHandler());
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public NextErrorButton getNextErrorButton() {
    return getFieldByClass(NextErrorButton.class);
  }

  public PreviewDataField getPreviewDataField() {
    return getFieldByClass(PreviewDataField.class);
  }

  public PreviewInfoField getPreviewInfoField() {
    return getFieldByClass(PreviewInfoField.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class PreviewInfoField extends AbstractLabelField {

      @Override
      protected boolean getConfiguredLabelVisible() {
        return false;
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }
    }

    @Order(20.0)
    public class NextErrorButton extends AbstractButton {

      @Override
      protected int getConfiguredDisplayStyle() {
        return DISPLAY_STYLE_LINK;
      }

      @Override
      protected int getConfiguredHorizontalAlignment() {
        return 1;
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("NextError");
      }

      @Override
      protected boolean getConfiguredProcessButton() {
        return false;
      }

      @Override
      protected void execClickAction() throws ProcessingException {
        Table table = getPreviewDataField().getTable();
        int selectedRow = -1;
        if (table.getSelectedRow() != null) {
          selectedRow = table.getSelectedRow().getRowIndex();
        }
        if ((selectedRow + 1) >= table.getRowCount()) {
          selectedRow = -1;
        }
        boolean selected = selectNextError(selectedRow, table);
        if (!selected) {
          selectNextError(0, table);
        }
      }

      private boolean selectNextError(int fromRow, Table table) {
        for (int r = Math.max(fromRow, 0); r < table.getRowCount(); r++) {
          for (int c = 0; c < table.getColumnCount(); c++) {
            if (table.getRow(r).getCell(c).getBackgroundColor() != null && r > fromRow) {
              table.deselectAllRows();
              table.selectRow(r);
              return true;
            }
          }
        }
        return false;
      }
    }

    @Order(30.0)
    public class PreviewDataField extends AbstractTableField<PreviewDataField.Table> {

      @Override
      protected int getConfiguredGridW() {
        return 2;
      }

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Preview");
      }

      @Override
      protected boolean getConfiguredLabelVisible() {
        return false;
      }

// TODO MIG      
//      @Override
//      protected boolean getConfiguredTableStatusVisible() {
//        return true;
//      }

      @Order(10.0)
      public class Table extends AbstractTable {

        @Override
        protected void execInitTable() throws ProcessingException {
          setAutoResizeColumns(columnCount <= 10);
        }

        @Override
        protected boolean getConfiguredScrollToSelection() {
          return true;
        }

        @Override
        protected ITableCustomizer createTableCustomizer() {
          return new DataExchangePreviewTableCustomizer(columnCount);
        }

      }
    }

  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {

    }

    @Override
    public void execStore() throws ProcessingException {

    }
  }
}
