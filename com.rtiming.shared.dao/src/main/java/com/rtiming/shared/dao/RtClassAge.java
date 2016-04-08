package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * The persistent class for the rt_class_age database table.
 * 
 */
@Entity
@Table(name="rt_class_age")
public class RtClassAge implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RtClassAgeKey id;

	@Column(name="age_from")
	private Long ageFrom;

	@Column(name="age_to")
	private Long ageTo;

	@Column(name="class_uid")
	private Long classUid;

	@Column(name="sex_uid")
	private Long sexUid;

	public RtClassAge() {
	}

	public RtClassAgeKey getId() {
		return this.id;
	}

	public void setId(RtClassAgeKey id) {
		this.id = id;
	}

	public Long getAgeFrom() {
		return this.ageFrom;
	}

	public void setAgeFrom(Long ageFrom) {
		this.ageFrom = ageFrom;
	}

	public Long getAgeTo() {
		return this.ageTo;
	}

	public void setAgeTo(Long ageTo) {
		this.ageTo = ageTo;
	}

	public Long getClassUid() {
		return this.classUid;
	}

	public void setClassUid(Long classUid) {
		this.classUid = classUid;
	}

	public Long getSexUid() {
		return this.sexUid;
	}

	public void setSexUid(Long sexUid) {
		this.sexUid = sexUid;
	}

}