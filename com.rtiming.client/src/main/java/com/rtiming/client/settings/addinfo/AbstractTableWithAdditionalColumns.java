package com.rtiming.client.settings.addinfo;

import java.util.List;

import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ExceptionHandler;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.util.collection.OrderedCollection;

import com.rtiming.client.ClientSession;
import com.rtiming.client.settings.addinfo.columns.AdditionalBooleanColumn;
import com.rtiming.client.settings.addinfo.columns.AdditionalDoubleColumn;
import com.rtiming.client.settings.addinfo.columns.AdditionalLongColumn;
import com.rtiming.client.settings.addinfo.columns.AdditionalSmartColumn;
import com.rtiming.client.settings.addinfo.columns.AdditionalStringColumn;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.common.database.sql.AdditionalInformationValueBean;
import com.rtiming.shared.entry.SharedCache;
import com.rtiming.shared.settings.addinfo.AdditionalInformationCodeType;
import com.rtiming.shared.settings.addinfo.AdditionalInformationTypeCodeType;

public abstract class AbstractTableWithAdditionalColumns extends AbstractTable {

  public abstract Long[] getEntityUids() throws ProcessingException;

  public Long getEventNr() throws ProcessingException {
    return null;
  }

  @Override
  protected void injectColumnsInternal(OrderedCollection<IColumn<?>> columnList) {
    super.injectColumnsInternal(columnList);
    try {
      for (Long entityUid : getEntityUids()) {
        List<AdditionalInformationValueBean> ais = SharedCache.getAddInfoForEntity(entityUid, ClientSession.get().getSessionClientNr());
        for (final AdditionalInformationValueBean ai : ais) {
          final String text = FMilaUtility.getCodeText(AdditionalInformationCodeType.class, ai.getAdditionalInformationUid());
          final String id = FMilaUtility.getCodeExtKey(AdditionalInformationCodeType.class, ai.getAdditionalInformationUid());
          AbstractColumn<?> column = null;
          if (CompareUtility.equals(ai.getTypeUid(), AdditionalInformationTypeCodeType.BooleanCode.ID)) {
            column = new AdditionalBooleanColumn(ai.getAdditionalInformationUid(), id, text);
          }
          else if (CompareUtility.equals(ai.getTypeUid(), AdditionalInformationTypeCodeType.DoubleCode.ID)) {
            column = new AdditionalDoubleColumn(ai.getAdditionalInformationUid(), id, text);
          }
          else if (CompareUtility.equals(ai.getTypeUid(), AdditionalInformationTypeCodeType.IntegerCode.ID)) {
            column = new AdditionalLongColumn(ai.getAdditionalInformationUid(), id, text);
          }
          else if (CompareUtility.equals(ai.getTypeUid(), AdditionalInformationTypeCodeType.SmartfieldCode.ID)) {
            column = new AdditionalSmartColumn(ai.getAdditionalInformationUid(), id, text);
          }
          else if (CompareUtility.equals(ai.getTypeUid(), AdditionalInformationTypeCodeType.TextCode.ID)) {
            column = new AdditionalStringColumn(ai.getAdditionalInformationUid(), id, text);
          }
          columnList.addLast(column);
        }
      }
    }
    catch (ProcessingException e) {
      BEANS.get(ExceptionHandler.class).handle(e);
    }
  }
}
