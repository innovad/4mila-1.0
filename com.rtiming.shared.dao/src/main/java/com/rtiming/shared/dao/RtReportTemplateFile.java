package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "rt_report_template_file")
public class RtReportTemplateFile implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RtReportTemplateFileKey id;
	
	@Column(name = "template")
	private String templateFileName;
	
	@Column(name = "report_template_nr")
	private Long reportTemplateNr;
	
	@Column(name = "is_master")
	private Boolean isMaster;	
	
	@Column(name = "last_modified")
	private Long lastModified;	
	
	@ManyToOne
	@JoinColumns ({
		@JoinColumn(name="report_template_nr", referencedColumnName="report_template_nr", insertable=false, updatable=false),
		@JoinColumn(name="client_nr", referencedColumnName = "client_nr", insertable=false, updatable=false)
	})
	private RtReportTemplate reportTemplate;
	
	@Transient
	private String templateContent;

	public RtReportTemplateFileKey getId() {
		return id;
	}

	public void setId(RtReportTemplateFileKey id) {
		this.id = id;
	}
	
	public void setTemplateFileName(String templateFileName) {
		this.templateFileName = templateFileName;
	}
	
	public String getTemplateFileName() {
		return templateFileName;
	}
	
	public String getTemplateContent() {
		return templateContent;
	}
	
	public void setTemplateContent(String reportContent) {
		this.templateContent = reportContent;
	}
	
	public Long getReportTemplateNr() {
		return reportTemplateNr;
	}
	
	public void setReportTemplateNr(Long reportTemplateNr) {
		this.reportTemplateNr = reportTemplateNr;
	}
	
	public Boolean isMaster() {
		return isMaster;
	}
		
	public void setMaster(Boolean isMaster) {
		this.isMaster = isMaster;
	}
	
	public Long getLastModified() {
		return lastModified;
	}
	
	public void setLastModified(Long lastModified) {
		this.lastModified = lastModified;
	}
	
}
