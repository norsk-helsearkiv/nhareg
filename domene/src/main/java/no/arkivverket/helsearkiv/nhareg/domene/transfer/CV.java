package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.Data;
import no.arkivverket.helsearkiv.nhareg.domene.additionalinfo.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 *
 *  Benyttes for registrering av koder i kodeverk som identifiseres entydig av en OID, og som det ikke er naturlig 
 *  å benytte datatype CS for.  
 *  Merk:  
 *  Bruk av datatype CV forutsettes at i spesifikasjonene er angitt hvilke konkrete kodeverk som skal kunne benyttes
 *  ved registrering i dette attributtet. Denne datatypen skal kun benyttes for kodeverk som kan identifiseres 
 *  gjennom en OID.  
 *
 */
@XmlTransient
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "CV",
    propOrder = {
        "codeSystem",
    })
@XmlSeeAlso({
    AdmissionType.class,
    EpisodeSubject.class,
    CareLevel.class,
    ContactType.class,
    ActivityLocation.class,
    ActivityType.class,
    DepartmentAdmissionActivity.class,
    InitiativeType.class,
    ServiceSubject.class,
    ProcedureCode.class,
})
@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class CV implements Serializable {

    @NotNull
    @Id
    protected String code;

    @NotNull
    @Id
    @XmlElement(name = "CodeSystem", namespace = "http://www.arkivverket.no/standarder/nha/avlxml/avlsup-mdk")
    protected String codeSystem;

    @NotNull
    @Id
    protected String codeSystemVersion;

    protected String displayName;

    protected String originalText;

}