package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * The persistent class for the rt_startlist_setting_vacant database table.
 * 
 */
@Entity
@Table(name="rt_startlist_setting_vacant")
public class RtStartlistSettingVacant implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RtStartlistSettingVacantKey id;

	//bi-directional many-to-one association to RtClient
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="client_nr", insertable=false, updatable=false)
	private RtClient rtClient;

	//bi-directional many-to-one association to RtStartlistSetting
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="client_nr", referencedColumnName="client_nr", insertable=false, updatable=false),
		@JoinColumn(name="startlist_setting_nr", referencedColumnName="startlist_setting_nr", insertable=false, updatable=false)
		})
	private RtStartlistSetting rtStartlistSetting;

	public RtStartlistSettingVacant() {
	}

	public RtStartlistSettingVacantKey getId() {
		return this.id;
	}

	public void setId(RtStartlistSettingVacantKey id) {
		this.id = id;
	}

	public RtClient getRtClient() {
		return this.rtClient;
	}

	public void setRtClient(RtClient rtClient) {
		this.rtClient = rtClient;
	}

	public RtStartlistSetting getRtStartlistSetting() {
		return this.rtStartlistSetting;
	}

	public void setRtStartlistSetting(RtStartlistSetting rtStartlistSetting) {
		this.rtStartlistSetting = rtStartlistSetting;
	}

}