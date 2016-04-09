package com.rtiming.client.ecard.download;

import java.util.List;

import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.IGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.tablefield.AbstractTableField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.client.ClientSession;
import com.rtiming.client.ecard.download.RaceControlsTableForm.MainBox.RaceControlField;
import com.rtiming.client.result.AbstractRaceControlTable;
import com.rtiming.shared.event.IEventsOutlineService;
import com.rtiming.shared.event.RaceControlRowData;

public class RaceControlsTableForm extends AbstractForm {

  public RaceControlsTableForm() throws ProcessingException {
    super();
  }

  private Long[] raceNrs;

  @Override
  protected String getConfiguredDisplayViewId() {
    return VIEW_ID_N;
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("RaceControl");
  }

  public void startNew() throws ProcessingException {
    startInternal(new NewHandler());
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public RaceControlField getRaceControlField() {
    return getFieldByClass(RaceControlField.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Override
    protected boolean getConfiguredBorderVisible() {
      return false;
    }

    @Override
    protected String getConfiguredBorderDecoration() {
      return IGroupBox.BORDER_DECORATION_EMPTY;
    }

    @Order(10.0)
    public class RaceControlField extends AbstractTableField<RaceControlField.Table> {

      @Override
      protected int getConfiguredGridH() {
        return 8;
      }

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("RaceControl");
      }

      @Override
      protected boolean getConfiguredLabelVisible() {
        return false;
      }

      @Order(10.0)
      public class Table extends AbstractRaceControlTable {

        @Override
        public boolean getConfiguredIsDeleteAllowed() {
          return false;
        }

        @Override
        protected boolean getConfiguredAutoResizeColumns() {
          return true;
        }

        @Override
        protected boolean getConfiguredMultiSelect() {
          // due to a scout bug, multi-select leads to wrong selection when used in the detail form
          // multi select works in the table page
          return false;
        }

        public void clearTable() throws ProcessingException {
          try {
            getTable().deleteAllRows();
            getTable().discardAllDeletedRows();
          }
          finally {
            getTable().setTableChanging(false);
          }
        }

        @Override
        public void reloadTable() throws ProcessingException {
          deselectAllRows();
          List<RaceControlRowData> list = BEANS.get(IEventsOutlineService.class).getRaceControlTableData(ClientSession.get().getSessionClientNr(), raceNrs);
          try {
            getTable().setTableChanging(true);
            getTable().deleteAllRows();
            getTable().discardAllDeletedRows();
            getTable().addRowsByMatrix(getTable().list2data(list));
          }
          finally {
            getTable().setTableChanging(false);
          }
          setSortEnabled(true);
          resetColumnSortOrder();
          setSortEnabled(false);
        }

        @Override
        protected Long[] getRaceNrs() {
          return raceNrs;
        }
      }
    }

  }

  public void setRaceNrs(Long[] raceNrs) {
    this.raceNrs = raceNrs;
  }

  public class NewHandler extends AbstractFormHandler {
  }
}
