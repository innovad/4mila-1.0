package com.rtiming.shared.common.report.template;

import java.util.Set;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

import com.rtiming.shared.dao.RtReportTemplateFile;
import com.rtiming.shared.dao.RtReportTemplateKey;

public class ReportTemplateFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public ReportTemplateFormData() {
  }

  public KeyProperty getKeyProperty() {
    return getPropertyByClass(KeyProperty.class);
  }

  /**
   * access method for property Key.
   */
  public RtReportTemplateKey getKey() {
    return getKeyProperty().getValue();
  }

  /**
   * access method for property Key.
   */
  public void setKey(RtReportTemplateKey key) {
    getKeyProperty().setValue(key);
  }

  public TemplateFilesProperty getTemplateFilesProperty() {
    return getPropertyByClass(TemplateFilesProperty.class);
  }

  /**
   * access method for property TemplateFiles.
   */
  public Set<RtReportTemplateFile> getTemplateFiles() {
    return getTemplateFilesProperty().getValue();
  }

  /**
   * access method for property TemplateFiles.
   */
  public void setTemplateFiles(Set<RtReportTemplateFile> templateFiles) {
    getTemplateFilesProperty().setValue(templateFiles);
  }

  public Active getActive() {
    return getFieldByClass(Active.class);
  }

  public Event getEvent() {
    return getFieldByClass(Event.class);
  }

  public ReportType getReportType() {
    return getFieldByClass(ReportType.class);
  }

  public Shortcut getShortcut() {
    return getFieldByClass(Shortcut.class);
  }

  public class KeyProperty extends AbstractPropertyData<RtReportTemplateKey> {
    private static final long serialVersionUID = 1L;

    public KeyProperty() {
    }
  }

  public class TemplateFilesProperty extends AbstractPropertyData<Set<RtReportTemplateFile>> {
    private static final long serialVersionUID = 1L;

    public TemplateFilesProperty() {
    }
  }

  public static class Active extends AbstractValueFieldData<Boolean> {
    private static final long serialVersionUID = 1L;

    public Active() {
    }
  }

  public static class Event extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Event() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class ReportType extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public ReportType() {
    }

    /**
     * list of derived validation rules.
     */
 
  }

  public static class Shortcut extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public Shortcut() {
    }

    /**
     * list of derived validation rules.
     */
     
  }
}
