package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * The persistent class for the rt_account_client database table.
 * 
 */
@Entity
@Table(name="rt_account_client")
public class RtAccountClient implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RtAccountClientKey id;

	//bi-directional many-to-one association to RtClient
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="client_nr", insertable=false, updatable=false)
	private RtClient rtClient;

	public RtAccountClient() {
	}

	public RtAccountClientKey getId() {
		return this.id;
	}

	public void setId(RtAccountClientKey id) {
		this.id = id;
	}

	public RtClient getRtClient() {
		return this.rtClient;
	}

	public void setRtClient(RtClient rtClient) {
		this.rtClient = rtClient;
	}

}