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
    "iofVersion",
    "eventIdOrEvent",
    "transaction"
})
@XmlRootElement(name = "TransactionList")
public class TransactionList {

    @XmlElement(name = "IOFVersion")
    protected IOFVersion iofVersion;
    @XmlElements({
        @XmlElement(name = "EventId", required = true, type = EventId.class),
        @XmlElement(name = "Event", required = true, type = Event.class)
    })
    protected List<Object> eventIdOrEvent;
    @XmlElement(name = "Transaction")
    protected List<Transaction> transaction;

    /**
     * Gets the value of the iofVersion property.
     * 
     * @return
     *     possible object is
     *     {@link IOFVersion }
     *     
     */
    public IOFVersion getIOFVersion() {
        return iofVersion;
    }

    /**
     * Sets the value of the iofVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link IOFVersion }
     *     
     */
    public void setIOFVersion(IOFVersion value) {
        this.iofVersion = value;
    }

    /**
     * Gets the value of the eventIdOrEvent property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the eventIdOrEvent property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEventIdOrEvent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EventId }
     * {@link Event }
     * 
     * 
     */
    public List<Object> getEventIdOrEvent() {
        if (eventIdOrEvent == null) {
            eventIdOrEvent = new ArrayList<Object>();
        }
        return this.eventIdOrEvent;
    }

    /**
     * Gets the value of the transaction property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the transaction property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTransaction().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Transaction }
     * 
     * 
     */
    public List<Transaction> getTransaction() {
        if (transaction == null) {
            transaction = new ArrayList<Transaction>();
        }
        return this.transaction;
    }

}
