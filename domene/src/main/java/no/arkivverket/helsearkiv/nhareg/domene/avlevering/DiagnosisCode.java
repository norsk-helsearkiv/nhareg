package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 *
 * Kode som angir hvilken diagnose det gjelder.
 * Eksempel på kodeverk:
 * Code System: ICD-10  [2.16.840.1.113883.6.3] (icd10)
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "diagnosekode")
@Entity
@Table(name = "diagnosekode")
public class DiagnosisCode extends CV implements Serializable {
}