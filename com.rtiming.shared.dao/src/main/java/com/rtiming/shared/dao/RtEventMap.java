package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.rtiming.shared.dao.util.UploadConfiguration;


/**
 * The persistent class for the rt_event_class database table.
 * 
 */
@Entity
@Table(name="rt_event_map")
@UploadConfiguration(uploadOrder = 95)
public class RtEventMap implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RtEventMapKey id;


	public RtEventMap() {
	}

	public RtEventMapKey getId() {
		return this.id;
	}

	public void setId(RtEventMapKey id) {
		this.id = id;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="client_nr", referencedColumnName="client_nr", insertable=false, updatable=false),
		@JoinColumn(name="map_nr", referencedColumnName="map_nr", insertable=false, updatable=false)
		})
	private RtMap rtMap;

}