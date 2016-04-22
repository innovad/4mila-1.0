package com.rtiming.shared.results;

import java.io.Serializable;

/**
 * 
 */
public class ResultClazzRowData implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long parentUid;
  private Long clazzUid;
  private String outline;
  private String parent;
  private String clazz;
  private Long clazzTypeUid;
  private Long sortCode;
  private Long entries;
  private Long runners;
  private Long processed;
  private Long missing;

  public Long getParentUid() {
    return parentUid;
  }

  public void setParentUid(Long parentUid) {
    this.parentUid = parentUid;
  }

  public Long getClazzUid() {
    return clazzUid;
  }

  public void setClazzUid(Long clazzUid) {
    this.clazzUid = clazzUid;
  }

  public String getOutline() {
    return outline;
  }

  public void setOutline(String outline) {
    this.outline = outline;
  }

  public String getParent() {
    return parent;
  }

  public void setParent(String parent) {
    this.parent = parent;
  }

  public String getClazz() {
    return clazz;
  }

  public void setClazz(String clazz) {
    this.clazz = clazz;
  }

  public Long getClazzTypeUid() {
    return clazzTypeUid;
  }

  public void setClazzTypeUid(Long clazzTypeUid) {
    this.clazzTypeUid = clazzTypeUid;
  }

  public Long getSortCode() {
    return sortCode;
  }

  public void setSortCode(Long sortCode) {
    this.sortCode = sortCode;
  }

  public Long getEntries() {
    return entries;
  }

  public void setEntries(Long entries) {
    this.entries = entries;
  }

  public Long getRunners() {
    return runners;
  }

  public void setRunners(Long runners) {
    this.runners = runners;
  }

  public Long getProcessed() {
    return processed;
  }

  public void setProcessed(Long processed) {
    this.processed = processed;
  }

  public Long getMissing() {
    return missing;
  }

  public void setMissing(Long missing) {
    this.missing = missing;
  }

}
