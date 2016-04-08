package com.rtiming.shared.common.file;

import java.io.File;
import java.io.FilenameFilter;
import java.io.Serializable;

/**
 * 
 */
public class AllFilenameFilter implements FilenameFilter, Serializable {

  private static final long serialVersionUID = 1L;

  @Override
  public boolean accept(File dir, String name) {
    return true;
  }

}
