package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the rt_key database table.
 * 
 */
@Entity
@Table(name="rt_key")
public class RtKey implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="rt_key", sequenceName="rt_key_key_nr_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="rt_key")
	@Column(name="key_nr")
	private Long keyNr;

	public RtKey() {
	}

	public Long getKeyNr() {
		return this.keyNr;
	}

	public void setKeyNr(Long keyNr) {
		this.keyNr = keyNr;
	}

}