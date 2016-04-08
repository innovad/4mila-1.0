package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * The persistent class for the rt_punch database table.
 * 
 */
@Entity
@Table(name="rt_punch")
public class RtPunch implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RtPunchKey id;

	@Column(name="control_no")
	private String controlNo;

	private Long time;

	//bi-directional many-to-one association to RtPunchSession
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="client_nr", referencedColumnName="client_nr", insertable=false, updatable=false),
		@JoinColumn(name="punch_session_nr", referencedColumnName="punch_session_nr", insertable=false, updatable=false)
		})
	private RtPunchSession rtPunchSession;

	public RtPunch() {
	}

	public RtPunchKey getId() {
		return this.id;
	}

	public void setId(RtPunchKey id) {
		this.id = id;
	}

	public String getControlNo() {
		return this.controlNo;
	}

	public void setControlNo(String controlNo) {
		this.controlNo = controlNo;
	}

	public Long getTime() {
		return this.time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public RtPunchSession getRtPunchSession() {
		return this.rtPunchSession;
	}

	public void setRtPunchSession(RtPunchSession rtPunchSession) {
		this.rtPunchSession = rtPunchSession;
	}

}