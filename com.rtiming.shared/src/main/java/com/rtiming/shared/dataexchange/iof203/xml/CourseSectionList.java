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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "startPointCode",
    "courseSection",
    "finishPointCode",
    "distanceToFinish"
})
@XmlRootElement(name = "CourseSectionList")
public class CourseSectionList {

    @XmlElement(name = "StartPointCode")
    protected StartPointCode startPointCode;
    @XmlElement(name = "CourseSection", required = true)
    protected List<CourseSection> courseSection;
    @XmlElement(name = "FinishPointCode")
    protected FinishPointCode finishPointCode;
    @XmlElement(name = "DistanceToFinish")
    protected List<DistanceToFinish> distanceToFinish;

    /**
     * Gets the value of the startPointCode property.
     * 
     * @return
     *     possible object is
     *     {@link StartPointCode }
     *     
     */
    public StartPointCode getStartPointCode() {
        return startPointCode;
    }

    /**
     * Sets the value of the startPointCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link StartPointCode }
     *     
     */
    public void setStartPointCode(StartPointCode value) {
        this.startPointCode = value;
    }

    /**
     * Gets the value of the courseSection property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the courseSection property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCourseSection().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CourseSection }
     * 
     * 
     */
    public List<CourseSection> getCourseSection() {
        if (courseSection == null) {
            courseSection = new ArrayList<CourseSection>();
        }
        return this.courseSection;
    }

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
     * Gets the value of the distanceToFinish property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the distanceToFinish property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDistanceToFinish().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DistanceToFinish }
     * 
     * 
     */
    public List<DistanceToFinish> getDistanceToFinish() {
        if (distanceToFinish == null) {
            distanceToFinish = new ArrayList<DistanceToFinish>();
        }
        return this.distanceToFinish;
    }

}
