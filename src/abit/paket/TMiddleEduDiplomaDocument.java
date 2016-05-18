//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.04.25 at 05:02:12 PM GMT+03:00 
//


package abit.paket;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for TMiddleEduDiplomaDocument complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TMiddleEduDiplomaDocument">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="UID" type="{}TUID"/>
 *         &lt;element name="OriginalReceivedDate" type="{}TDate" minOccurs="0"/>
 *         &lt;element name="DocumentSeries" type="{}TDocumentSeries"/>
 *         &lt;element name="DocumentNumber" type="{}TDocumentNumber"/>
 *         &lt;element name="DocumentDate" type="{}TDate" minOccurs="0"/>
 *         &lt;element name="DocumentOrganization" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="500"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="RegistrationNumber" type="{}TDocumentNumber" minOccurs="0"/>
 *         &lt;element name="QualificationTypeID" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" minOccurs="0"/>
 *         &lt;element name="SpecialityID" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" minOccurs="0"/>
 *         &lt;element name="SpecializationID" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" minOccurs="0"/>
 *         &lt;element name="EndYear" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;minInclusive value="1800"/>
 *               &lt;maxExclusive value="3000"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="GPA" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TMiddleEduDiplomaDocument", propOrder = {

})
public class TMiddleEduDiplomaDocument {

    @XmlElement(name = "UID", required = true)
    protected String uid;
    @XmlElement(name = "OriginalReceivedDate")
    protected XMLGregorianCalendar originalReceivedDate;
    @XmlElement(name = "DocumentSeries", required = true)
    protected String documentSeries;
    @XmlElement(name = "DocumentNumber", required = true)
    protected String documentNumber;
    @XmlElement(name = "DocumentDate")
    protected XMLGregorianCalendar documentDate;
    @XmlElement(name = "DocumentOrganization")
    protected String documentOrganization;
    @XmlElement(name = "RegistrationNumber")
    protected String registrationNumber;
    @XmlElement(name = "QualificationTypeID")
    @XmlSchemaType(name = "unsignedInt")
    protected Long qualificationTypeID;
    @XmlElement(name = "SpecialityID")
    @XmlSchemaType(name = "unsignedInt")
    protected Long specialityID;
    @XmlElement(name = "SpecializationID")
    @XmlSchemaType(name = "unsignedShort")
    protected Integer specializationID;
    @XmlElement(name = "EndYear")
    protected Long endYear;
    @XmlElement(name = "GPA")
    protected Float gpa;

    /**
     * Gets the value of the uid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUID() {
        return uid;
    }

    /**
     * Sets the value of the uid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUID(String value) {
        this.uid = value;
    }

    /**
     * Gets the value of the originalReceivedDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getOriginalReceivedDate() {
        return originalReceivedDate;
    }

    /**
     * Sets the value of the originalReceivedDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setOriginalReceivedDate(XMLGregorianCalendar value) {
        this.originalReceivedDate = value;
    }

    /**
     * Gets the value of the documentSeries property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentSeries() {
        return documentSeries;
    }

    /**
     * Sets the value of the documentSeries property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentSeries(String value) {
        this.documentSeries = value;
    }

    /**
     * Gets the value of the documentNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentNumber() {
        return documentNumber;
    }

    /**
     * Sets the value of the documentNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentNumber(String value) {
        this.documentNumber = value;
    }

    /**
     * Gets the value of the documentDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDocumentDate() {
        return documentDate;
    }

    /**
     * Sets the value of the documentDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDocumentDate(XMLGregorianCalendar value) {
        this.documentDate = value;
    }

    /**
     * Gets the value of the documentOrganization property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentOrganization() {
        return documentOrganization;
    }

    /**
     * Sets the value of the documentOrganization property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentOrganization(String value) {
        this.documentOrganization = value;
    }

    /**
     * Gets the value of the registrationNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    /**
     * Sets the value of the registrationNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegistrationNumber(String value) {
        this.registrationNumber = value;
    }

    /**
     * Gets the value of the qualificationTypeID property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getQualificationTypeID() {
        return qualificationTypeID;
    }

    /**
     * Sets the value of the qualificationTypeID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setQualificationTypeID(Long value) {
        this.qualificationTypeID = value;
    }

    /**
     * Gets the value of the specialityID property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getSpecialityID() {
        return specialityID;
    }

    /**
     * Sets the value of the specialityID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setSpecialityID(Long value) {
        this.specialityID = value;
    }

    /**
     * Gets the value of the specializationID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSpecializationID() {
        return specializationID;
    }

    /**
     * Sets the value of the specializationID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSpecializationID(Integer value) {
        this.specializationID = value;
    }

    /**
     * Gets the value of the endYear property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getEndYear() {
        return endYear;
    }

    /**
     * Sets the value of the endYear property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setEndYear(Long value) {
        this.endYear = value;
    }

    /**
     * Gets the value of the gpa property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getGPA() {
        return gpa;
    }

    /**
     * Sets the value of the gpa property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setGPA(Float value) {
        this.gpa = value;
    }

}
