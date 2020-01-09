package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * <p>Java class for Gender complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Gender">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv}CS">
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "kjonn")
@Entity
@Table(name = "kjonn")
public class Gender extends CS implements Serializable {
}