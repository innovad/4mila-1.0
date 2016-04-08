package com.rtiming.client.entry.payment;

import java.util.List;
import java.util.Set;

import org.eclipse.scout.commons.CollectionUtility;
import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.annotations.FormData;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBigDecimalColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBooleanColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractDateColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.common.ui.action.AbstractSeparatorMenu;
import com.rtiming.client.common.ui.table.AbstractSummaryTable;
import com.rtiming.shared.Icons;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.database.sql.PaymentBean;
import com.rtiming.shared.entry.IRegistrationsOutlineService;
import com.rtiming.shared.entry.payment.IPaymentProcessService;
import com.rtiming.shared.entry.payment.PaymentTypeCodeType;
import com.rtiming.shared.settings.currency.CurrencyCodeType;

public class PaymentsTablePage extends AbstractPageWithTable<PaymentsTablePage.Table> implements IHelpEnabledPage {

  private final Long registrationNr;

  public PaymentsTablePage(Long registrationNr) {
    super();
    this.registrationNr = registrationNr;
  }

  @FormData
  public Long getRegistrationNr() {
    return registrationNr;
  }

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Payments");
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    importTableData(BEANS.get(IRegistrationsOutlineService.class).getPaymentTableData(getRegistrationNr()));
  }

  @Order(10.0)
  public class Table extends AbstractSummaryTable {

    @Override
    protected Class<? extends IMenu> getConfiguredDefaultMenu() {
      return EditMenu.class;
    }

    public CurrencyUidColumn getCurrencyUidColumn() {
      return getColumnSet().getColumnByClass(CurrencyUidColumn.class);
    }

    public DateColumn getDateColumn() {
      return getColumnSet().getColumnByClass(DateColumn.class);
    }

    @Override
    protected String getConfiguredDefaultIconId() {
      return Icons.PAYMENT;
    }

    public RegistrationColumn getRegistrationColumn() {
      return getColumnSet().getColumnByClass(RegistrationColumn.class);
    }

    public TypeUidColumn getTypeUidColumn() {
      return getColumnSet().getColumnByClass(TypeUidColumn.class);
    }

    public SummaryOrderColumn getSummaryOrderColumn() {
      return getColumnSet().getColumnByClass(SummaryOrderColumn.class);
    }

    public AmountColumn getAmountColumn() {
      return getColumnSet().getColumnByClass(AmountColumn.class);
    }

    public PaymentNoColumn getPaymentNoColumn() {
      return getColumnSet().getColumnByClass(PaymentNoColumn.class);
    }

    public PaymentNrColumn getPaymentNrColumn() {
      return getColumnSet().getColumnByClass(PaymentNrColumn.class);
    }

    @Order(10.0)
    public class SummaryOrderColumn extends AbstractBooleanColumn {

      @Override
      protected boolean getConfiguredAlwaysIncludeSortAtBegin() {
        return true;
      }

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }

      @Override
      protected int getConfiguredSortIndex() {
        return 1;
      }
    }

    @Order(20.0)
    public class PaymentNrColumn extends AbstractLongColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }
    }

    @Order(30.0)
    public class PaymentNoColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Number");
      }

      @Override
      protected int getConfiguredSortIndex() {
        return 2;
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }
    }

    @Order(40.0)
    public class AmountColumn extends AbstractBigDecimalColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Amount");
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }
    }

    @Order(50.0)
    public class CurrencyUidColumn extends AbstractSmartColumn<Long> {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Currency");
      }

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return CurrencyCodeType.class;

      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }
    }

    @Order(60.0)
    public class RegistrationColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Registration");
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }
    }

    @Order(70.0)
    public class DateColumn extends AbstractDateColumn {

      @Override
      protected boolean getConfiguredHasTime() {
        return true;
      }

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Date");
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }
    }

    @Order(80.0)
    public class TypeUidColumn extends AbstractSmartColumn<Long> {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("PaymentType");
      }

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return PaymentTypeCodeType.class;

      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }
    }

    @Order(10.0)
    public class NewMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return ScoutTexts.get("NewButton");
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(TableMenuType.EmptySpace);
      }

      @Override
      protected void execAction() throws ProcessingException {
        PaymentForm form = new PaymentForm();
        form.getRegistrationField().setValue(getRegistrationNr());
        form.startNew();
        form.waitFor();
        if (form.isFormStored()) {
          reloadPage();
        }
      }
    }

    @Order(20.0)
    public class EditMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return ScoutTexts.get("EditMenu");
      }

      @Override
      protected void execOwnerValueChanged(Object newOwnerValue) throws ProcessingException {
        setVisible(getSelectedRow() != null && (getSelectedRow().getRowIndex() != getTable().getRowCount() - 1));
      }

      @Override
      protected void execAction() throws ProcessingException {
        PaymentForm form = new PaymentForm();
        form.setPaymentNr(getTable().getPaymentNrColumn().getSelectedValue());
        form.startModify();
        form.waitFor();
        if (form.isFormStored()) {
          reloadPage();
        }
      }
    }

    @Order(30.0)
    public class SeparatorMenu extends AbstractSeparatorMenu {
    }

    @Order(40.0)
    public class DeleteMenu extends AbstractMenu {

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(TableMenuType.MultiSelection, TableMenuType.SingleSelection);
      }

      @Override
      protected void execOwnerValueChanged(Object newOwnerValue) throws ProcessingException {
        setVisible(getSelectedRow() != null && (getSelectedRow().getRowIndex() != getTable().getRowCount() - 1));
      }

      @Override
      protected String getConfiguredText() {
        return ScoutTexts.get("DeleteMenu");
      }

      @Override
      protected void execAction() throws ProcessingException {
        List<String> selectedValues = getPaymentNoColumn().getSelectedValues();
        for (int k = 0; k < selectedValues.size(); k++) {
          selectedValues.set(k, StringUtility.nvl(selectedValues.get(k), getAmountColumn().getDisplayTexts().get(k)));
        }
        if (MessageBoxes.showDeleteConfirmationMessage(Texts.get("Payments"), selectedValues)) {
          for (Long paymentNr : getTable().getPaymentNrColumn().getSelectedValues()) {
            PaymentBean formData = new PaymentBean();
            formData.setPaymentNr(paymentNr);
            BEANS.get(IPaymentProcessService.class).delete(formData);
          }
          reloadPage();
        }
      }

    }

    @Override
    protected Class<? extends AbstractBooleanColumn> getConfiguredSummaryOrderColumn() {
      return SummaryOrderColumn.class;
    }

    @Override
    protected Class<? extends AbstractStringColumn> getConfiguredSummaryTotalColumn() {
      return PaymentNoColumn.class;
    }
  }
}
