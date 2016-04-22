package com.rtiming.client.settings.addinfo;

import java.util.Set;

import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.ValueFieldMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBooleanColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.AbstractDoubleColumn;
import com.rtiming.client.common.EmptyWorkaroundTablePage;
import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.common.ui.action.AbstractSeparatorMenu;
import com.rtiming.shared.Icons;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.EntityCodeType;
import com.rtiming.shared.settings.ISettingsOutlineService;
import com.rtiming.shared.settings.addinfo.AdditionalInformationAdministrationFormData;
import com.rtiming.shared.settings.addinfo.AdditionalInformationTypeCodeType;
import com.rtiming.shared.settings.addinfo.IAdditionalInformationAdministrationProcessService;

public class AdditionalInformationAdministrationTablePage extends AbstractPageWithTable<AdditionalInformationAdministrationTablePage.Table> implements IHelpEnabledPage {

  private final Long parentUid;

  public AdditionalInformationAdministrationTablePage(Long parentUid) {
    super();
    this.parentUid = parentUid;
  }

  @Override
  protected void execInitPage() throws ProcessingException {
    // hide rows for sub table (smartfield)
    getTable().getEntityColumn().setDisplayable(getParentUid() == null);
    getTable().getMaximumColumn().setDisplayable(getParentUid() == null);
    getTable().getMinimumColumn().setDisplayable(getParentUid() == null);
    getTable().getEventsColumn().setDisplayable(getParentUid() == null);
    getTable().getMandatoryColumn().setDisplayable(getParentUid() == null);
  }

  public Long getParentUid() {
    return parentUid;
  }

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("AdditionalInformation");
  }

  @Override
  protected IPage execCreateChildPage(ITableRow row) throws ProcessingException {
    if (getTable().getTypeColumn().getValue(row) == AdditionalInformationTypeCodeType.SmartfieldCode.ID && getParentUid() == null) {
      // display child values for smartfield type
      return new AdditionalInformationAdministrationTablePage(getTable().getAdditionalInformationUidColumn().getValue(row));
    }
    else {
      return new EmptyWorkaroundTablePage();
    }
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    importTableData(BEANS.get(ISettingsOutlineService.class).getAdditionalInformationAdministrationTableData(getParentUid()));
  }

  @Order(10.0)
  public class Table extends AbstractTable {

    @Override
    protected Class<? extends IMenu> getConfiguredDefaultMenu() {
      if (CompareUtility.equals(getTypeColumn().getSelectedValue(), AdditionalInformationTypeCodeType.SmartfieldCode.ID) && getParentUid() == null) {
        return null;
      }
      return EditMenu.class;
    }

    @Override
    protected String getConfiguredDefaultIconId() {
      return Icons.ADDITIONAL_INFORMATION;
    }

    public NameColumn getNameColumn() {
      return getColumnSet().getColumnByClass(NameColumn.class);
    }

    public TypeColumn getTypeColumn() {
      return getColumnSet().getColumnByClass(TypeColumn.class);
    }

    public MinimumColumn getMinimumColumn() {
      return getColumnSet().getColumnByClass(MinimumColumn.class);
    }

    public MaximumColumn getMaximumColumn() {
      return getColumnSet().getColumnByClass(MaximumColumn.class);
    }

    public FeeGroupColumn getFeeGroupColumn() {
      return getColumnSet().getColumnByClass(FeeGroupColumn.class);
    }

    public EntityColumn getEntityColumn() {
      return getColumnSet().getColumnByClass(EntityColumn.class);
    }

    public EventsColumn getEventsColumn() {
      return getColumnSet().getColumnByClass(EventsColumn.class);
    }

    public MandatoryColumn getMandatoryColumn() {
      return getColumnSet().getColumnByClass(MandatoryColumn.class);
    }

    public AdditionalInformationUidColumn getAdditionalInformationUidColumn() {
      return getColumnSet().getColumnByClass(AdditionalInformationUidColumn.class);
    }

    @Order(10.0)
    public class AdditionalInformationUidColumn extends AbstractLongColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }

      @Override
      protected boolean getConfiguredPrimaryKey() {
        return true;
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }
    }

    @Order(20.0)
    public class NameColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return ScoutTexts.get("Name");
      }

      @Override
      protected int getConfiguredSortIndex() {
        return 1;
      }

      @Override
      protected int getConfiguredWidth() {
        return 200;
      }
    }

    @Order(30.0)
    public class EntityColumn extends AbstractSmartColumn<Long> {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Entity");
      }

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return EntityCodeType.class;

      }

      @Override
      protected int getConfiguredWidth() {
        return 200;
      }
    }

    @Order(40.0)
    public class TypeColumn extends AbstractSmartColumn<Long> {

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return AdditionalInformationTypeCodeType.class;
      }

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Type");
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }
    }

    @Order(50.0)
    public class MinimumColumn extends AbstractDoubleColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Minimum");
      }

      @Override
      protected int getConfiguredWidth() {
        return 90;
      }
    }

    @Order(60.0)
    public class MaximumColumn extends AbstractDoubleColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Maximum");
      }

      @Override
      protected int getConfiguredWidth() {
        return 90;
      }
    }

    @Order(70.0)
    public class FeeGroupColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("FeeGroup");
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }
    }

    @Order(80.0)
    public class MandatoryColumn extends AbstractBooleanColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Mandatory");
      }

      @Override
      protected int getConfiguredWidth() {
        return 75;
      }
    }

    @Order(90.0)
    public class EventsColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Events");
      }

      @Override
      protected int getConfiguredWidth() {
        return 300;
      }

      @Override
      protected boolean getConfiguredTextWrap() {
        return true;
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
        return CollectionUtility.<IMenuType> hashSet(ValueFieldMenuType.Null);
      }

      @Override
      protected void execAction() throws ProcessingException {
        AdditionalInformationAdministrationForm form = new AdditionalInformationAdministrationForm();
        form.getSmartfieldField().setValue(getParentUid());
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
      protected void execAction() throws ProcessingException {
        AdditionalInformationAdministrationForm form = new AdditionalInformationAdministrationForm();
        form.setAdditionalInformationUid(getAdditionalInformationUidColumn().getSelectedValue());
        form.getSmartfieldField().setValue(getParentUid());
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
      protected String getConfiguredText() {
        return ScoutTexts.get("DeleteMenu");
      }

      @Override
      protected void execAction() throws ProcessingException {
        if (MessageBoxes.showDeleteConfirmationMessage(Texts.get("AdditionalInformation"), getNameColumn().getSelectedValues())) {
          for (Long additionalInformationUid : getTable().getAdditionalInformationUidColumn().getSelectedValues()) {
            AdditionalInformationAdministrationFormData formData = new AdditionalInformationAdministrationFormData();
            formData.setAdditionalInformationUid(additionalInformationUid);
            BEANS.get(IAdditionalInformationAdministrationProcessService.class).delete(formData);
          }
          reloadPage();
        }
      }
    }
  }
}
