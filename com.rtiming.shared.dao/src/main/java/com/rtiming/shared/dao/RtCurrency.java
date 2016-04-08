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
 * The persistent class for the rt_currency database table.
 * 
 */
@Entity
@Table(name="rt_currency")
public class RtCurrency implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RtCurrencyKey id;

	@Column(name="exchange_rate")
	private double exchangeRate;

	//bi-directional many-to-one association to RtClient
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="client_nr", insertable=false, updatable=false)
	private RtClient rtClient;

	//bi-directional many-to-one association to RtUc
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="client_nr", referencedColumnName="client_nr", insertable=false, updatable=false),
		@JoinColumn(name="currency_uid", referencedColumnName="uc_uid", insertable=false, updatable=false)
		})
	private RtUc rtUc;

	public RtCurrency() {
	}

	public RtCurrencyKey getId() {
		return this.id;
	}

	public void setId(RtCurrencyKey id) {
		this.id = id;
	}

	public double getExchangeRate() {
		return this.exchangeRate;
	}

	public void setExchangeRate(double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public RtClient getRtClient() {
		return this.rtClient;
	}

	public void setRtClient(RtClient rtClient) {
		this.rtClient = rtClient;
	}

	public RtUc getRtUc() {
		return this.rtUc;
	}

	public void setRtUc(RtUc rtUc) {
		this.rtUc = rtUc;
	}

}