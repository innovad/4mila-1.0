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
    "serviceIdOrService",
    "serviceOrderNumber",
    "requestedQuantity",
    "deliveredQuantity",
    "comment",
    "modifyDate"
})
@XmlRootElement(name = "ServiceRequest")
public class ServiceRequest {

    @XmlElements({
        @XmlElement(name = "ServiceId", required = true, type = ServiceId.class),
        @XmlElement(name = "Service", required = true, type = Service.class)
    })
    protected List<Object> serviceIdOrService;
    @XmlElement(name = "ServiceOrderNumber")
    protected ServiceOrderNumber serviceOrderNumber;
    @XmlElement(name = "RequestedQuantity", required = true)
    protected RequestedQuantity requestedQuantity;
    @XmlElement(name = "DeliveredQuantity")
    protected DeliveredQuantity deliveredQuantity;
    @XmlElement(name = "Comment")
    protected Comment comment;
    @XmlElement(name = "ModifyDate")
    protected ModifyDate modifyDate;

    /**
     * Gets the value of the serviceIdOrService property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the serviceIdOrService property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getServiceIdOrService().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ServiceId }
     * {@link Service }
     * 
     * 
     */
    public List<Object> getServiceIdOrService() {
        if (serviceIdOrService == null) {
            serviceIdOrService = new ArrayList<Object>();
        }
        return this.serviceIdOrService;
    }

    /**
     * Gets the value of the serviceOrderNumber property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceOrderNumber }
     *     
     */
    public ServiceOrderNumber getServiceOrderNumber() {
        return serviceOrderNumber;
    }

    /**
     * Sets the value of the serviceOrderNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceOrderNumber }
     *     
     */
    public void setServiceOrderNumber(ServiceOrderNumber value) {
        this.serviceOrderNumber = value;
    }

    /**
     * Gets the value of the requestedQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link RequestedQuantity }
     *     
     */
    public RequestedQuantity getRequestedQuantity() {
        return requestedQuantity;
    }

    /**
     * Sets the value of the requestedQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestedQuantity }
     *     
     */
    public void setRequestedQuantity(RequestedQuantity value) {
        this.requestedQuantity = value;
    }

    /**
     * Gets the value of the deliveredQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link DeliveredQuantity }
     *     
     */
    public DeliveredQuantity getDeliveredQuantity() {
        return deliveredQuantity;
    }

    /**
     * Sets the value of the deliveredQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link DeliveredQuantity }
     *     
     */
    public void setDeliveredQuantity(DeliveredQuantity value) {
        this.deliveredQuantity = value;
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
