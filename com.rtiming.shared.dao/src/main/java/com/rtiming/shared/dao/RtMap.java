package com.rtiming.shared.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.rtiming.shared.dao.util.UploadConfiguration;


/**
 * The persistent class for the rt_map database table.
 * 
 */
@Entity
@Table(name="rt_map")
@UploadConfiguration(uploadOrder = 90, cleanup = false)
public class RtMap implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RtMapKey key;

	@Column(name="evt_file_last_update")
	private Date evtFileLastUpdate;

	private String format;

	private Integer h;

	private String name;

	@Column(name="ne_x")
	private Double neX;

	@Column(name="ne_y")
	private Double neY;

	@Column(name="nw_x")
	private Double nwX;

	@Column(name="nw_y")
	private Double nwY;

	@Column(name="origin_x")
	private Double originX;

	@Column(name="origin_y")
	private Double originY;

	private Double resolution;

	private Long scale;

	@Column(name="se_x")
	private Double seX;

	@Column(name="se_y")
	private Double seY;

	@Column(name="sw_x")
	private Double swX;

	@Column(name="sw_y")
	private Double swY;

	private Integer w;
	
	@Transient
	private byte[] mapData;
	
	@Transient
	boolean isFileChanged;
	
	@Transient
	Long newEventNr;
	
	@OneToMany(mappedBy="rtMap") 
	private List<RtEventMap> rtEventMaps;
	
	public RtMap() {
	}

	public RtMapKey getId() {
		return this.key;
	}

	public void setId(RtMapKey id) {
		this.key = id;
	}

	public Date getEvtFileLastUpdate() {
		return this.evtFileLastUpdate;
	}

	public void setEvtFileLastUpdate(Date evtFileLastUpdate) {
		this.evtFileLastUpdate = evtFileLastUpdate;
	}

	public String getFormat() {
		return this.format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public Integer getH() {
		return this.h;
	}

	public void setH(Integer h) {
		this.h = h;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getNeX() {
		return this.neX;
	}

	public void setNeX(Double neX) {
		this.neX = neX;
	}

	public Double getNeY() {
		return this.neY;
	}

	public void setNeY(Double neY) {
		this.neY = neY;
	}

	public Double getNwX() {
		return this.nwX;
	}

	public void setNwX(Double nwX) {
		this.nwX = nwX;
	}

	public Double getNwY() {
		return this.nwY;
	}

	public void setNwY(Double nwY) {
		this.nwY = nwY;
	}

	public Double getOriginX() {
		return this.originX;
	}

	public void setOriginX(Double originX) {
		this.originX = originX;
	}

	public Double getOriginY() {
		return this.originY;
	}

	public void setOriginY(Double originY) {
		this.originY = originY;
	}

	public Double getResolution() {
		return this.resolution;
	}

	public void setResolution(Double resolution) {
		this.resolution = resolution;
	}

	public Long getScale() {
		return this.scale;
	}

	public void setScale(Long scale) {
		this.scale = scale;
	}

	public Double getSeX() {
		return this.seX;
	}

	public void setSeX(Double seX) {
		this.seX = seX;
	}

	public Double getSeY() {
		return this.seY;
	}

	public void setSeY(Double seY) {
		this.seY = seY;
	}

	public Double getSwX() {
		return this.swX;
	}

	public void setSwX(Double swX) {
		this.swX = swX;
	}

	public Double getSwY() {
		return this.swY;
	}

	public void setSwY(Double swY) {
		this.swY = swY;
	}

	public Integer getW() {
		return this.w;
	}

	public void setW(Integer w) {
		this.w = w;
	}
	
	public byte[] getMapData() {
		return mapData;
	}
	
	public void setMapData(byte[] mapData) {
		this.mapData = mapData;
	}
	
	public boolean getIsFileChanged() {
		return isFileChanged;
	}
	
	public void setIsFileChanged(boolean isFileChanged) {
		this.isFileChanged = isFileChanged;
	}
	
	public Long getNewEventNr() {
		return newEventNr;
	}
	
	public void setNewEventNr(Long newEventNr) {
		this.newEventNr = newEventNr;
	}

}