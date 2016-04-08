package com.rtiming.client.dataexchange;

import org.eclipse.scout.commons.CompareUtility;
import org.eclipse.scout.commons.exception.ProcessingException;

import com.rtiming.client.dataexchange.city.GeneralCityInterface;
import com.rtiming.client.dataexchange.city.GeonamesCityInterface;
import com.rtiming.client.dataexchange.city.SwissPostCityInterface;
import com.rtiming.client.dataexchange.iof.IOF203CourseImporter;
import com.rtiming.client.dataexchange.iof.IOF203ResultListInterface;
import com.rtiming.client.dataexchange.iof.IOF203RunnerInterface;
import com.rtiming.client.dataexchange.iof.IOF203StartListExporter;
import com.rtiming.client.dataexchange.iof3.IOF300CourseImporter;
import com.rtiming.client.dataexchange.iof3.IOF300ResultListInterface;
import com.rtiming.client.dataexchange.iof3.IOF300RunnerInterface;
import com.rtiming.client.dataexchange.iof3.IOF300StartListExporter;
import com.rtiming.client.dataexchange.oe2003.OE2003EntryListInterface;
import com.rtiming.client.dataexchange.oe2003.OE2003ResultListInterface;
import com.rtiming.client.dataexchange.oe2003.OE2003StartListInterface;
import com.rtiming.client.dataexchange.swiss.GO2OLEntriesInterface;
import com.rtiming.client.dataexchange.swiss.SwissOrienteeringRunnerInterface;
import com.rtiming.shared.dataexchange.DataExchangeStartFormData;
import com.rtiming.shared.dataexchange.ImportExportFormatCodeType;

public final class InterfaceMapper {

  private InterfaceMapper() {
  }

  public static AbstractInterface<?> createInterface(Long uid, DataExchangeStartFormData formData) throws ProcessingException {

    AbstractInterface<?> currentInterface = null;

    if (CompareUtility.equals(uid, ImportExportFormatCodeType.SwissOrienteeringRunnerDatabaseCode.ID)) {
      currentInterface = new SwissOrienteeringRunnerInterface(formData);
    }
    else if (CompareUtility.equals(uid, ImportExportFormatCodeType.GO2OLEntriesCode.ID)) {
      currentInterface = new GO2OLEntriesInterface(formData);
    }
    else if (CompareUtility.equals(uid, ImportExportFormatCodeType.GeneralPostalCode.ID)) {
      currentInterface = new GeneralCityInterface(formData);
    }
    else if (CompareUtility.equals(uid, ImportExportFormatCodeType.GeoNamesPostalCode.ID)) {
      currentInterface = new GeonamesCityInterface(formData);
    }
    else if (CompareUtility.equals(uid, ImportExportFormatCodeType.SwissPostPostalCode.ID)) {
      currentInterface = new SwissPostCityInterface(formData);
    }
    else if (CompareUtility.equals(uid, ImportExportFormatCodeType.OE2003EntryListCode.ID)) {
      currentInterface = new OE2003EntryListInterface(formData);
    }
    else if (CompareUtility.equals(uid, ImportExportFormatCodeType.OE2003ResultListCode.ID)) {
      currentInterface = new OE2003ResultListInterface(formData);
    }
    else if (CompareUtility.equals(uid, ImportExportFormatCodeType.IOFDataStandard203CourseDataCode.ID)) {
      currentInterface = new IOF203CourseImporter(formData);
    }
    else if (CompareUtility.equals(uid, ImportExportFormatCodeType.IOFDataStandard300CourseDataCode.ID)) {
      currentInterface = new IOF300CourseImporter(formData);
    }
    else if (CompareUtility.equals(uid, ImportExportFormatCodeType.IOFDataStandard203StartListCode.ID)) {
      currentInterface = new IOF203StartListExporter(formData);
    }
    else if (CompareUtility.equals(uid, ImportExportFormatCodeType.IOFDataStandard300StartListCode.ID)) {
      currentInterface = new IOF300StartListExporter(formData);
    }
    else if (CompareUtility.equals(uid, ImportExportFormatCodeType.IOFDataStandard203ResultsCode.ID)) {
      currentInterface = new IOF203ResultListInterface(formData);
    }
    else if (CompareUtility.equals(uid, ImportExportFormatCodeType.IOFDataStandard300ResultsCode.ID)) {
      currentInterface = new IOF300ResultListInterface(formData);
    }
    else if (CompareUtility.equals(uid, ImportExportFormatCodeType.OE2003StartListCode.ID)) {
      currentInterface = new OE2003StartListInterface(formData);
    }
    else if (CompareUtility.equals(uid, ImportExportFormatCodeType.IOFDataStandard203RunnerCode.ID)) {
      currentInterface = new IOF203RunnerInterface(formData);
    }
    else if (CompareUtility.equals(uid, ImportExportFormatCodeType.IOFDataStandard300RunnerCode.ID)) {
      currentInterface = new IOF300RunnerInterface(formData);
    }

    if (currentInterface == null) {
      throw new ProcessingException("Interface not defined, id=" + uid);
    }

    return currentInterface;
  }

}
