//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.07.09 at 05:12:48 PM MESZ 
//


package com.rtiming.shared.dataexchange.iof203.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "organisationIdOrOrganisation",
    "clubIdOrClub",
    "classIdOrClassShortNameOrClazz",
    "position",
    "modifyDate"
})
@XmlRootElement(name = "ResultSelection")
public class ResultSelection {

    @XmlElements({
        @XmlElement(name = "OrganisationId", type = OrganisationId.class),
        @XmlElement(name = "Organisation", type = Organisation.class)
    })
    protected List<Object> organisationIdOrOrganisation;
    @XmlElements({
        @XmlElement(name = "ClubId", type = ClubId.class),
        @XmlElement(name = "Club", type = Club.class)
    })
    protected List<Object> clubIdOrClub;
    @XmlElements({
        @XmlElement(name = "ClassId", type = ClassId.class),
        @XmlElement(name = "ClassShortName", type = ClassShortName.class),
        @XmlElement(name = "Class", type = Class.class)
    })
    protected List<Object> classIdOrClassShortNameOrClazz;
    @XmlElement(name = "Position", required = true)
    protected String position;
    @XmlElement(name = "ModifyDate", required = true)
    protected ModifyDate modifyDate;

    /**
     * Gets the value of the organisationIdOrOrganisation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the organisationIdOrOrganisation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOrganisationIdOrOrganisation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OrganisationId }
     * {@link Organisation }
     * 
     * 
     */
    public List<Object> getOrganisationIdOrOrganisation() {
        if (organisationIdOrOrganisation == null) {
            organisationIdOrOrganisation = new ArrayList<Object>();
        }
        return this.organisationIdOrOrganisation;
    }

    /**
     * Gets the value of the clubIdOrClub property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the clubIdOrClub property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getClubIdOrClub().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ClubId }
     * {@link Club }
     * 
     * 
     */
    public List<Object> getClubIdOrClub() {
        if (clubIdOrClub == null) {
            clubIdOrClub = new ArrayList<Object>();
        }
        return this.clubIdOrClub;
    }

    /**
     * Gets the value of the classIdOrClassShortNameOrClazz property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the classIdOrClassShortNameOrClazz property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getClassIdOrClassShortNameOrClazz().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ClassId }
     * {@link ClassShortName }
     * {@link Class }
     * 
     * 
     */
    public List<Object> getClassIdOrClassShortNameOrClazz() {
        if (classIdOrClassShortNameOrClazz == null) {
            classIdOrClassShortNameOrClazz = new ArrayList<Object>();
        }
        return this.classIdOrClassShortNameOrClazz;
    }

    /**
     * Gets the value of the position property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPosition() {
        return position;
    }

    /**
     * Sets the value of the position property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPosition(String value) {
        this.position = value;
    }

    /**
     * Gets the value of the modifyDate property.
     * 
     * @return
     *     possible object is
     *     {@link ModifyDate }
     *     
     */
    public ModifyDate getModifyDate() {
        return modifyDate;
    }

    /**
     * Sets the value of the modifyDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link ModifyDate }
     *     
     */
    public void setModifyDate(ModifyDate value) {
        this.modifyDate = value;
    }

}
