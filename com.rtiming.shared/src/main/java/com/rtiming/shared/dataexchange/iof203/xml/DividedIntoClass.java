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
    "eventClass"
})
@XmlRootElement(name = "DividedIntoClass")
public class DividedIntoClass {

    @XmlElement(name = "EventClass")
    protected List<EventClass> eventClass;

    /**
     * Gets the value of the eventClass property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the eventClass property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEventClass().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EventClass }
     * 
     * 
     */
    public List<EventClass> getEventClass() {
        if (eventClass == null) {
            eventClass = new ArrayList<EventClass>();
        }
        return this.eventClass;
    }

}
