package com.rtiming.shared.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.rtiming.shared.dao.util.UploadConfiguration;

/**
 * The persistent class for the rt_client database table.
 */
@Entity
@Table(name = "rt_client")
@UploadConfiguration(uploadOrder = 0, cleanup = false)
public class RtClient implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "client_nr")
  private Long clientNr;

  //bi-directional many-to-one association to RtAccount
  @OneToMany(mappedBy = "rtClient")
  private List<RtAccount> rtAccounts;

  //bi-directional many-to-one association to RtAccountClient
  @OneToMany(mappedBy = "rtClient")
  private List<RtAccountClient> rtAccountClients;

  //bi-directional many-to-one association to RtAdditionalInformationDef
  @OneToMany(mappedBy = "rtClient")
  private List<RtAdditionalInformationDef> rtAdditionalInformationDefs;

  //bi-directional many-to-one association to RtAddress
  @OneToMany(mappedBy = "rtClient")
  private List<RtAddress> rtAddresses;

  //bi-directional many-to-one association to RtCity
  @OneToMany(mappedBy = "rtClient")
  private List<RtCity> rtCities;

  //bi-directional many-to-one association to RtClub
  @OneToMany(mappedBy = "rtClient")
  private List<RtClub> rtClubs;

  //bi-directional many-to-one association to RtCountry
  @OneToMany(mappedBy = "rtClient")
  private List<RtCountry> rtCountries;

  //bi-directional many-to-one association to RtCurrency
  @OneToMany(mappedBy = "rtClient")
  private List<RtCurrency> rtCurrencies;

  //bi-directional many-to-one association to RtDefault
  @OneToMany(mappedBy = "rtClient")
  private List<RtDefault> rtDefaults;

  //bi-directional many-to-one association to RtEcard
  @OneToMany(mappedBy = "rtClient")
  private List<RtEcard> rtEcards;

  //bi-directional many-to-one association to RtEntry
  @OneToMany(mappedBy = "rtClient")
  private List<RtEntry> rtEntries;

  //bi-directional many-to-one association to RtEventClass
  @OneToMany(mappedBy = "rtClient")
  private List<RtEventClass> rtEventClasses;

  //bi-directional many-to-one association to RtEventStartblock
  @OneToMany(mappedBy = "rtClient")
  private List<RtEventStartblock> rtEventStartblocks;

  //bi-directional many-to-one association to RtFeeGroup
  @OneToMany(mappedBy = "rtClient")
  private List<RtFeeGroup> rtFeeGroups;

  //bi-directional many-to-one association to RtParticipation
  @OneToMany(mappedBy = "rtClient")
  private List<RtParticipation> rtParticipations;

  //bi-directional many-to-one association to RtPayment
  @OneToMany(mappedBy = "rtClient")
  private List<RtPayment> rtPayments;

  //bi-directional many-to-one association to RtPosition
  @OneToMany(mappedBy = "rtClient")
  private List<RtPosition> rtPositions;

  //bi-directional many-to-one association to RtRace
  @OneToMany(mappedBy = "rtClient")
  private List<RtRace> rtRaces;

  //bi-directional many-to-one association to RtRegistration
  @OneToMany(mappedBy = "rtClient")
  private List<RtRegistration> rtRegistrations;

  //bi-directional many-to-one association to RtRunner
  @OneToMany(mappedBy = "rtClient")
  private List<RtRunner> rtRunners;

  //bi-directional many-to-one association to RtStartlistSetting
  @OneToMany(mappedBy = "rtClient")
  private List<RtStartlistSetting> rtStartlistSettings;

  //bi-directional many-to-one association to RtStartlistSettingOption
  @OneToMany(mappedBy = "rtClient")
  private List<RtStartlistSettingOption> rtStartlistSettingOptions;

  //bi-directional many-to-one association to RtStartlistSettingVacant
  @OneToMany(mappedBy = "rtClient")
  private List<RtStartlistSettingVacant> rtStartlistSettingVacants;

  //bi-directional many-to-one association to RtUser
  @OneToMany(mappedBy = "rtClient")
  private List<RtUser> rtUsers;

  //bi-directional many-to-one association to RtUserRole
  @OneToMany(mappedBy = "rtClient")
  private List<RtUserRole> rtUserRoles;

  //bi-directional many-to-one association to RtWebSession
  @OneToMany(mappedBy = "rtClient")
  private List<RtWebSession> rtWebSessions;

  public RtClient() {
  }

  public Long getClientNr() {
    return this.clientNr;
  }

  public void setClientNr(Long clientNr) {
    this.clientNr = clientNr;
  }

  public List<RtAccount> getRtAccounts() {
    return this.rtAccounts;
  }

  public void setRtAccounts(List<RtAccount> rtAccounts) {
    this.rtAccounts = rtAccounts;
  }

  public RtAccount addRtAccount(RtAccount rtAccount) {
    getRtAccounts().add(rtAccount);
    rtAccount.setRtClient(this);

    return rtAccount;
  }

  public RtAccount removeRtAccount(RtAccount rtAccount) {
    getRtAccounts().remove(rtAccount);
    rtAccount.setRtClient(null);

    return rtAccount;
  }

  public List<RtAccountClient> getRtAccountClients() {
    return this.rtAccountClients;
  }

  public void setRtAccountClients(List<RtAccountClient> rtAccountClients) {
    this.rtAccountClients = rtAccountClients;
  }

  public RtAccountClient addRtAccountClient(RtAccountClient rtAccountClient) {
    getRtAccountClients().add(rtAccountClient);
    rtAccountClient.setRtClient(this);

    return rtAccountClient;
  }

  public RtAccountClient removeRtAccountClient(RtAccountClient rtAccountClient) {
    getRtAccountClients().remove(rtAccountClient);
    rtAccountClient.setRtClient(null);

    return rtAccountClient;
  }

  public List<RtAdditionalInformationDef> getRtAdditionalInformationDefs() {
    return this.rtAdditionalInformationDefs;
  }

  public void setRtAdditionalInformationDefs(List<RtAdditionalInformationDef> rtAdditionalInformationDefs) {
    this.rtAdditionalInformationDefs = rtAdditionalInformationDefs;
  }

  public RtAdditionalInformationDef addRtAdditionalInformationDef(RtAdditionalInformationDef rtAdditionalInformationDef) {
    getRtAdditionalInformationDefs().add(rtAdditionalInformationDef);
    rtAdditionalInformationDef.setRtClient(this);

    return rtAdditionalInformationDef;
  }

  public RtAdditionalInformationDef removeRtAdditionalInformationDef(RtAdditionalInformationDef rtAdditionalInformationDef) {
    getRtAdditionalInformationDefs().remove(rtAdditionalInformationDef);
    rtAdditionalInformationDef.setRtClient(null);

    return rtAdditionalInformationDef;
  }

  public List<RtAddress> getRtAddresses() {
    return this.rtAddresses;
  }

  public void setRtAddresses(List<RtAddress> rtAddresses) {
    this.rtAddresses = rtAddresses;
  }

  public RtAddress addRtAddress(RtAddress rtAddress) {
    getRtAddresses().add(rtAddress);
    rtAddress.setRtClient(this);

    return rtAddress;
  }

  public RtAddress removeRtAddress(RtAddress rtAddress) {
    getRtAddresses().remove(rtAddress);
    rtAddress.setRtClient(null);

    return rtAddress;
  }

  public List<RtCity> getRtCities() {
    return this.rtCities;
  }

  public void setRtCities(List<RtCity> rtCities) {
    this.rtCities = rtCities;
  }

  public RtCity addRtCity(RtCity rtCity) {
    getRtCities().add(rtCity);
    rtCity.setRtClient(this);

    return rtCity;
  }

  public RtCity removeRtCity(RtCity rtCity) {
    getRtCities().remove(rtCity);
    rtCity.setRtClient(null);

    return rtCity;
  }

  public List<RtClub> getRtClubs() {
    return this.rtClubs;
  }

  public void setRtClubs(List<RtClub> rtClubs) {
    this.rtClubs = rtClubs;
  }

  public RtClub addRtClub(RtClub rtClub) {
    getRtClubs().add(rtClub);
    rtClub.setRtClient(this);

    return rtClub;
  }

  public RtClub removeRtClub(RtClub rtClub) {
    getRtClubs().remove(rtClub);
    rtClub.setRtClient(null);

    return rtClub;
  }

  public List<RtCountry> getRtCountries() {
    return this.rtCountries;
  }

  public void setRtCountries(List<RtCountry> rtCountries) {
    this.rtCountries = rtCountries;
  }

  public RtCountry addRtCountry(RtCountry rtCountry) {
    getRtCountries().add(rtCountry);
    rtCountry.setRtClient(this);

    return rtCountry;
  }

  public RtCountry removeRtCountry(RtCountry rtCountry) {
    getRtCountries().remove(rtCountry);
    rtCountry.setRtClient(null);

    return rtCountry;
  }

  public List<RtCurrency> getRtCurrencies() {
    return this.rtCurrencies;
  }

  public void setRtCurrencies(List<RtCurrency> rtCurrencies) {
    this.rtCurrencies = rtCurrencies;
  }

  public RtCurrency addRtCurrency(RtCurrency rtCurrency) {
    getRtCurrencies().add(rtCurrency);
    rtCurrency.setRtClient(this);

    return rtCurrency;
  }

  public RtCurrency removeRtCurrency(RtCurrency rtCurrency) {
    getRtCurrencies().remove(rtCurrency);
    rtCurrency.setRtClient(null);

    return rtCurrency;
  }

  public List<RtDefault> getRtDefaults() {
    return this.rtDefaults;
  }

  public void setRtDefaults(List<RtDefault> rtDefaults) {
    this.rtDefaults = rtDefaults;
  }

  public RtDefault addRtDefault(RtDefault rtDefault) {
    getRtDefaults().add(rtDefault);
    rtDefault.setRtClient(this);

    return rtDefault;
  }

  public RtDefault removeRtDefault(RtDefault rtDefault) {
    getRtDefaults().remove(rtDefault);
    rtDefault.setRtClient(null);

    return rtDefault;
  }

  public List<RtEcard> getRtEcards() {
    return this.rtEcards;
  }

  public void setRtEcards(List<RtEcard> rtEcards) {
    this.rtEcards = rtEcards;
  }

  public RtEcard addRtEcard(RtEcard rtEcard) {
    getRtEcards().add(rtEcard);
    rtEcard.setRtClient(this);

    return rtEcard;
  }

  public RtEcard removeRtEcard(RtEcard rtEcard) {
    getRtEcards().remove(rtEcard);
    rtEcard.setRtClient(null);

    return rtEcard;
  }

  public List<RtEntry> getRtEntries() {
    return this.rtEntries;
  }

  public void setRtEntries(List<RtEntry> rtEntries) {
    this.rtEntries = rtEntries;
  }

  public RtEntry addRtEntry(RtEntry rtEntry) {
    getRtEntries().add(rtEntry);
    rtEntry.setRtClient(this);

    return rtEntry;
  }

  public RtEntry removeRtEntry(RtEntry rtEntry) {
    getRtEntries().remove(rtEntry);
    rtEntry.setRtClient(null);

    return rtEntry;
  }

  public List<RtEventClass> getRtEventClasses() {
    return this.rtEventClasses;
  }

  public void setRtEventClasses(List<RtEventClass> rtEventClasses) {
    this.rtEventClasses = rtEventClasses;
  }

  public RtEventClass addRtEventClass(RtEventClass rtEventClass) {
    getRtEventClasses().add(rtEventClass);
    rtEventClass.setRtClient(this);

    return rtEventClass;
  }

  public RtEventClass removeRtEventClass(RtEventClass rtEventClass) {
    getRtEventClasses().remove(rtEventClass);
    rtEventClass.setRtClient(null);

    return rtEventClass;
  }

  public List<RtEventStartblock> getRtEventStartblocks() {
    return this.rtEventStartblocks;
  }

  public void setRtEventStartblocks(List<RtEventStartblock> rtEventStartblocks) {
    this.rtEventStartblocks = rtEventStartblocks;
  }

  public RtEventStartblock addRtEventStartblock(RtEventStartblock rtEventStartblock) {
    getRtEventStartblocks().add(rtEventStartblock);
    rtEventStartblock.setRtClient(this);

    return rtEventStartblock;
  }

  public RtEventStartblock removeRtEventStartblock(RtEventStartblock rtEventStartblock) {
    getRtEventStartblocks().remove(rtEventStartblock);
    rtEventStartblock.setRtClient(null);

    return rtEventStartblock;
  }

  public List<RtFeeGroup> getRtFeeGroups() {
    return this.rtFeeGroups;
  }

  public void setRtFeeGroups(List<RtFeeGroup> rtFeeGroups) {
    this.rtFeeGroups = rtFeeGroups;
  }

  public RtFeeGroup addRtFeeGroup(RtFeeGroup rtFeeGroup) {
    getRtFeeGroups().add(rtFeeGroup);
    rtFeeGroup.setRtClient(this);

    return rtFeeGroup;
  }

  public RtFeeGroup removeRtFeeGroup(RtFeeGroup rtFeeGroup) {
    getRtFeeGroups().remove(rtFeeGroup);
    rtFeeGroup.setRtClient(null);

    return rtFeeGroup;
  }

  public List<RtParticipation> getRtParticipations() {
    return this.rtParticipations;
  }

  public void setRtParticipations(List<RtParticipation> rtParticipations) {
    this.rtParticipations = rtParticipations;
  }

  public RtParticipation addRtParticipation(RtParticipation rtParticipation) {
    getRtParticipations().add(rtParticipation);
    rtParticipation.setRtClient(this);

    return rtParticipation;
  }

  public RtParticipation removeRtParticipation(RtParticipation rtParticipation) {
    getRtParticipations().remove(rtParticipation);
    rtParticipation.setRtClient(null);

    return rtParticipation;
  }

  public List<RtPayment> getRtPayments() {
    return this.rtPayments;
  }

  public void setRtPayments(List<RtPayment> rtPayments) {
    this.rtPayments = rtPayments;
  }

  public RtPayment addRtPayment(RtPayment rtPayment) {
    getRtPayments().add(rtPayment);
    rtPayment.setRtClient(this);

    return rtPayment;
  }

  public RtPayment removeRtPayment(RtPayment rtPayment) {
    getRtPayments().remove(rtPayment);
    rtPayment.setRtClient(null);

    return rtPayment;
  }

  public List<RtPosition> getRtPositions() {
    return this.rtPositions;
  }

  public void setRtPositions(List<RtPosition> rtPositions) {
    this.rtPositions = rtPositions;
  }

  public RtPosition addRtPosition(RtPosition rtPosition) {
    getRtPositions().add(rtPosition);
    rtPosition.setRtClient(this);

    return rtPosition;
  }

  public RtPosition removeRtPosition(RtPosition rtPosition) {
    getRtPositions().remove(rtPosition);
    rtPosition.setRtClient(null);

    return rtPosition;
  }

  public List<RtRace> getRtRaces() {
    return this.rtRaces;
  }

  public void setRtRaces(List<RtRace> rtRaces) {
    this.rtRaces = rtRaces;
  }

  public RtRace addRtRace(RtRace rtRace) {
    getRtRaces().add(rtRace);
    rtRace.setRtClient(this);

    return rtRace;
  }

  public RtRace removeRtRace(RtRace rtRace) {
    getRtRaces().remove(rtRace);
    rtRace.setRtClient(null);

    return rtRace;
  }

  public List<RtRegistration> getRtRegistrations() {
    return this.rtRegistrations;
  }

  public void setRtRegistrations(List<RtRegistration> rtRegistrations) {
    this.rtRegistrations = rtRegistrations;
  }

  public RtRegistration addRtRegistration(RtRegistration rtRegistration) {
    getRtRegistrations().add(rtRegistration);
    rtRegistration.setRtClient(this);

    return rtRegistration;
  }

  public RtRegistration removeRtRegistration(RtRegistration rtRegistration) {
    getRtRegistrations().remove(rtRegistration);
    rtRegistration.setRtClient(null);

    return rtRegistration;
  }

  public List<RtRunner> getRtRunners() {
    return this.rtRunners;
  }

  public void setRtRunners(List<RtRunner> rtRunners) {
    this.rtRunners = rtRunners;
  }

  public RtRunner addRtRunner(RtRunner rtRunner) {
    getRtRunners().add(rtRunner);
    rtRunner.setRtClient(this);

    return rtRunner;
  }

  public RtRunner removeRtRunner(RtRunner rtRunner) {
    getRtRunners().remove(rtRunner);
    rtRunner.setRtClient(null);

    return rtRunner;
  }

  public List<RtStartlistSetting> getRtStartlistSettings() {
    return this.rtStartlistSettings;
  }

  public void setRtStartlistSettings(List<RtStartlistSetting> rtStartlistSettings) {
    this.rtStartlistSettings = rtStartlistSettings;
  }

  public RtStartlistSetting addRtStartlistSetting(RtStartlistSetting rtStartlistSetting) {
    getRtStartlistSettings().add(rtStartlistSetting);
    rtStartlistSetting.setRtClient(this);

    return rtStartlistSetting;
  }

  public RtStartlistSetting removeRtStartlistSetting(RtStartlistSetting rtStartlistSetting) {
    getRtStartlistSettings().remove(rtStartlistSetting);
    rtStartlistSetting.setRtClient(null);

    return rtStartlistSetting;
  }

  public List<RtStartlistSettingOption> getRtStartlistSettingOptions() {
    return this.rtStartlistSettingOptions;
  }

  public void setRtStartlistSettingOptions(List<RtStartlistSettingOption> rtStartlistSettingOptions) {
    this.rtStartlistSettingOptions = rtStartlistSettingOptions;
  }

  public RtStartlistSettingOption addRtStartlistSettingOption(RtStartlistSettingOption rtStartlistSettingOption) {
    getRtStartlistSettingOptions().add(rtStartlistSettingOption);
    rtStartlistSettingOption.setRtClient(this);

    return rtStartlistSettingOption;
  }

  public RtStartlistSettingOption removeRtStartlistSettingOption(RtStartlistSettingOption rtStartlistSettingOption) {
    getRtStartlistSettingOptions().remove(rtStartlistSettingOption);
    rtStartlistSettingOption.setRtClient(null);

    return rtStartlistSettingOption;
  }

  public List<RtStartlistSettingVacant> getRtStartlistSettingVacants() {
    return this.rtStartlistSettingVacants;
  }

  public void setRtStartlistSettingVacants(List<RtStartlistSettingVacant> rtStartlistSettingVacants) {
    this.rtStartlistSettingVacants = rtStartlistSettingVacants;
  }

  public RtStartlistSettingVacant addRtStartlistSettingVacant(RtStartlistSettingVacant rtStartlistSettingVacant) {
    getRtStartlistSettingVacants().add(rtStartlistSettingVacant);
    rtStartlistSettingVacant.setRtClient(this);

    return rtStartlistSettingVacant;
  }

  public RtStartlistSettingVacant removeRtStartlistSettingVacant(RtStartlistSettingVacant rtStartlistSettingVacant) {
    getRtStartlistSettingVacants().remove(rtStartlistSettingVacant);
    rtStartlistSettingVacant.setRtClient(null);

    return rtStartlistSettingVacant;
  }

  public List<RtUser> getRtUsers() {
    return this.rtUsers;
  }

  public void setRtUsers(List<RtUser> rtUsers) {
    this.rtUsers = rtUsers;
  }

  public RtUser addRtUser(RtUser rtUser) {
    getRtUsers().add(rtUser);
    rtUser.setRtClient(this);

    return rtUser;
  }

  public RtUser removeRtUser(RtUser rtUser) {
    getRtUsers().remove(rtUser);
    rtUser.setRtClient(null);

    return rtUser;
  }

  public List<RtUserRole> getRtUserRoles() {
    return this.rtUserRoles;
  }

  public void setRtUserRoles(List<RtUserRole> rtUserRoles) {
    this.rtUserRoles = rtUserRoles;
  }

  public RtUserRole addRtUserRole(RtUserRole rtUserRole) {
    getRtUserRoles().add(rtUserRole);
    rtUserRole.setRtClient(this);

    return rtUserRole;
  }

  public RtUserRole removeRtUserRole(RtUserRole rtUserRole) {
    getRtUserRoles().remove(rtUserRole);
    rtUserRole.setRtClient(null);

    return rtUserRole;
  }

  public List<RtWebSession> getRtWebSessions() {
    return this.rtWebSessions;
  }

  public void setRtWebSessions(List<RtWebSession> rtWebSessions) {
    this.rtWebSessions = rtWebSessions;
  }

  public RtWebSession addRtWebSession(RtWebSession rtWebSession) {
    getRtWebSessions().add(rtWebSession);
    rtWebSession.setRtClient(this);

    return rtWebSession;
  }

  public RtWebSession removeRtWebSession(RtWebSession rtWebSession) {
    getRtWebSessions().remove(rtWebSession);
    rtWebSession.setRtClient(null);

    return rtWebSession;
  }

}
