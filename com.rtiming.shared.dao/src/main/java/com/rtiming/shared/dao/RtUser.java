package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * The persistent class for the rt_user database table.
 * 
 */
@Entity
@Table(name="rt_user")
public class RtUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RtUserKey id;

	@Column(name="language_uid")
	private Long languageUid;

	private String password;

	private String username;

	//bi-directional many-to-one association to RtClient
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="client_nr", insertable=false, updatable=false)
	private RtClient rtClient;

	public RtUser() {
	}

	public RtUserKey getId() {
		return this.id;
	}

	public void setId(RtUserKey id) {
		this.id = id;
	}

	public Long getLanguageUid() {
		return this.languageUid;
	}

	public void setLanguageUid(Long languageUid) {
		this.languageUid = languageUid;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public RtClient getRtClient() {
		return this.rtClient;
	}

	public void setRtClient(RtClient rtClient) {
		this.rtClient = rtClient;
	}

}