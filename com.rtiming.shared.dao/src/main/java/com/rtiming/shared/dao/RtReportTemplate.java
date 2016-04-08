package com.rtiming.shared.dao;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "rt_report_template")
public class RtReportTemplate implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RtReportTemplateKey id;
	
	@Column(name = "type_uid")
	private Long typeUid;

	@Column(name = "event_nr")
	private Long eventNr;

	@Column(name = "active")
	private Boolean active;
	
	@Column(name = "shortcut")
	private String shortcut;
			
	@OneToMany(mappedBy="reportTemplate", fetch=FetchType.EAGER, cascade=CascadeType.REMOVE)
	@OrderBy("report_template_file_nr")
	private Set<RtReportTemplateFile> templateFiles;
	
	public RtReportTemplateKey getId() {
		return id;
	}

	public void setId(RtReportTemplateKey id) {
		this.id = id;
	}

	public Long getTypeUid() {
		return typeUid;
	}

	public void setTypeUid(Long typeUid) {
		this.typeUid = typeUid;
	}

	public Long getEventNr() {
		return eventNr;
	}

	public void setEventNr(Long eventNr) {
		this.eventNr = eventNr;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
	public String getShortcut() {
		return shortcut;
	}
	
	public void setShortcut(String shortcut) {
		this.shortcut = shortcut;
	}
		
	public Set<RtReportTemplateFile> getTemplateFiles() {
		return templateFiles;
	}
	
	public void setTemplateFiles(Set<RtReportTemplateFile> templateFiles) {
		this.templateFiles = templateFiles;
	}
		
}
