package com.rtiming.shared.dataexchange;

import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;

public abstract class AbstractImportExportCode extends AbstractCode<Long> {

  private static final long serialVersionUID = 1L;

  public abstract boolean isImport();

  public abstract boolean isExport();

  public abstract boolean isEventDependent();

  public abstract boolean isLanguageDependent();

  public abstract String getFileExtension();

  public Boolean getConfiguredIgnoreHeaderRow() {
    return null;
  }

  public Character getConfiguredDefaultFieldSeparator() {
    return null;
  }

  public Character getConfiguredDefaultTextEnclosing() {
    return null;
  }

}
