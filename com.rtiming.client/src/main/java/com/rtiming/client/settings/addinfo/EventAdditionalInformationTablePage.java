package com.rtiming.client.settings.addinfo;

import java.util.Set;

import org.eclipse.scout.commons.CollectionUtility;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.common.ui.action.AbstractSeparatorMenu;
import com.rtiming.shared.Icons;
import com.rtiming.shared.Texts;
import com.rtiming.shared.event.IEventsOutlineService;
import com.rtiming.shared.settings.addinfo.AdditionalInformationCodeType;
import com.rtiming.shared.settings.addinfo.EventAdditionalInformationFormData;
import com.rtiming.shared.settings.addinfo.IEventAdditionalInformationProcessService;

public class EventAdditionalInformationTablePage extends AbstractPageWithTable<EventAdditionalInformationTablePage.Table> implements IHelpEnabledPage {

  private final Long eventNr;

  public EventAdditionalInformationTablePage(Long eventNr) {
    super();
    this.eventNr = eventNr;
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    importTableData(BEANS.get(IEventsOutlineService.class).getEventAdditionalInformationTableData(getEventNr()));
  }

  public Long getEventNr() {
    return eventNr;
  }

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("AdditionalInformation");
  }

  @Order(10.0)
  public class Table extends AbstractTable {

    @Override
    protected String getConfiguredDefaultIconId() {
      return Icons.ADDITIONAL_INFORMATION;
    }

    public AdditionalInformationUidColumn getAdditionalInformationUidColumn() {
      return getColumnSet().getColumnByClass(AdditionalInformationUidColumn.class);
    }

    @Order(10.0)
    public class AdditionalInformationUidColumn extends AbstractSmartColumn<Long> {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("AdditionalInformation");
      }

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return AdditionalInformationCodeType.class;

      }

      @Override
      protected int getConfiguredWidth() {
        return 250;
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
        EventAdditionalInformationForm form = new EventAdditionalInformationForm();
        form.getEventField().setValue(getEventNr());
        form.startNew();
        form.waitFor();
        if (form.isFormStored()) {
          reloadPage();
        }
      }
    }

    @Order(15.0)
    public class SeparatorMenu extends AbstractSeparatorMenu {
    }

    @Order(20.0)
    public class DeleteMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return ScoutTexts.get("DeleteMenu");
      }

      @Override
      protected void execAction() throws ProcessingException {
        if (MessageBoxes.showDeleteConfirmationMessage(Texts.get("AdditionalInformation"))) {
          EventAdditionalInformationFormData addInfo = new EventAdditionalInformationFormData();
          addInfo.getEvent().setValue(getEventNr());
          addInfo.getAdditionalInformation().setValue(getAdditionalInformationUidColumn().getSelectedValue());
          BEANS.get(IEventAdditionalInformationProcessService.class).delete(addInfo);
          reloadPage();
        }
      }

    }
  }
}
