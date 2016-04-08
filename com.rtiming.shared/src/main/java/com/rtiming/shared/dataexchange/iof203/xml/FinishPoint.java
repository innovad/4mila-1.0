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
    "finishPointCode",
    "description",
    "controlPosition",
    "punchingUnitOrPunchPattern",
    "punchingUnitType",
    "mapPosition",
    "comment",
    "modifyDate"
})
@XmlRootElement(name = "FinishPoint")
public class FinishPoint {

    @XmlElement(name = "FinishPointCode", required = true)
    protected FinishPointCode finishPointCode;
    @XmlElement(name = "Description")
    protected Description description;
    @XmlElement(name = "ControlPosition")
    protected ControlPosition controlPosition;
    @XmlElements({
        @XmlElement(name = "PunchingUnit", type = PunchingUnit.class),
        @XmlElement(name = "PunchPattern", type = PunchPattern.class)
    })
    protected List<Object> punchingUnitOrPunchPattern;
    @XmlElement(name = "PunchingUnitType")
    protected PunchingUnitType punchingUnitType;
    @XmlElement(name = "MapPosition")
    protected MapPosition mapPosition;
    @XmlElement(name = "Comment")
    protected Comment comment;
    @XmlElement(name = "ModifyDate")
    protected ModifyDate modifyDate;

    /**
     * Gets the value of the finishPointCode property.
     * 
     * @return
     *     possible object is
     *     {@link FinishPointCode }
     *     
     */
    public FinishPointCode getFinishPointCode() {
        return finishPointCode;
    }

    /**
     * Sets the value of the finishPointCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link FinishPointCode }
     *     
     */
    public void setFinishPointCode(FinishPointCode value) {
        this.finishPointCode = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link Description }
     *     
     */
    public Description getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link Description }
     *     
     */
    public void setDescription(Description value) {
        this.description = value;
    }

    /**
     * Gets the value of the controlPosition property.
     * 
     * @return
     *     possible object is
     *     {@link ControlPosition }
     *     
     */
    public ControlPosition getControlPosition() {
        return controlPosition;
    }

    /**
     * Sets the value of the controlPosition property.
     * 
     * @param value
     *     allowed object is
     *     {@link ControlPosition }
     *     
     */
    public void setControlPosition(ControlPosition value) {
        this.controlPosition = value;
    }

    /**
     * Gets the value of the punchingUnitOrPunchPattern property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the punchingUnitOrPunchPattern property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPunchingUnitOrPunchPattern().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PunchingUnit }
     * {@link PunchPattern }
     * 
     * 
     */
    public List<Object> getPunchingUnitOrPunchPattern() {
        if (punchingUnitOrPunchPattern == null) {
            punchingUnitOrPunchPattern = new ArrayList<Object>();
        }
        return this.punchingUnitOrPunchPattern;
    }

    /**
     * Gets the value of the punchingUnitType property.
     * 
     * @return
     *     possible object is
     *     {@link PunchingUnitType }
     *     
     */
    public PunchingUnitType getPunchingUnitType() {
        return punchingUnitType;
    }

    /**
     * Sets the value of the punchingUnitType property.
     * 
     * @param value
     *     allowed object is
     *     {@link PunchingUnitType }
     *     
     */
    public void setPunchingUnitType(PunchingUnitType value) {
        this.punchingUnitType = value;
    }

    /**
     * Gets the value of the mapPosition property.
     * 
     * @return
     *     possible object is
     *     {@link MapPosition }
     *     
     */
    public MapPosition getMapPosition() {
        return mapPosition;
    }

    /**
     * Sets the value of the mapPosition property.
     * 
     * @param value
     *     allowed object is
     *     {@link MapPosition }
     *     
     */
    public void setMapPosition(MapPosition value) {
        this.mapPosition = value;
    }

    /**
     * Gets the value of the comment property.
     * 
     * @return
     *     possible object is
     *     {@link Comment }
     *     
     */
    public Comment getComment() {
        return comment;
    }

    /**
     * Sets the value of the comment property.
     * 
     * @param value
     *     allowed object is
     *     {@link Comment }
     *     
     */
    public void setComment(Comment value) {
        this.comment = value;
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
