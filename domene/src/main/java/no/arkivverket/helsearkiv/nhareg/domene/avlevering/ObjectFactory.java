
package no.arkivverket.helsearkiv.nhareg.domene.avlevering;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the no.arkivverket.helsearkiv.nhareg.domene.avlevering package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Oppdateringsinfo_QNAME = new QName("http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv", "oppdateringsinfo");
    private final static QName _Avlevering_QNAME = new QName("http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv", "Avlevering");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: no.arkivverket.helsearkiv.nhareg.domene.avlevering
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Avlevering }
     * 
     */
    public Avlevering createAvlevering() {
        return new Avlevering();
    }

    /**
     * Create an instance of {@link Diagnosekode }
     * 
     */
    public Diagnosekode createDiagnosekode() {
        return new Diagnosekode();
    }

    /**
     * Create an instance of {@link CV }
     * 
     */
    public CV createCV() {
        return new CV();
    }

    /**
     * Create an instance of {@link Oppdateringsinfo }
     * 
     */
    public Oppdateringsinfo createOppdateringsinfo() {
        return new Oppdateringsinfo();
    }

    /**
     * Create an instance of {@link Henvisning }
     * 
     */
    public Henvisning createHenvisning() {
        return new Henvisning();
    }

    /**
     * Create an instance of {@link Kjønn }
     * 
     */
    public Kjønn createKjønn() {
        return new Kjønn();
    }

    /**
     * Create an instance of {@link Konsultasjon }
     * 
     */
    public Konsultasjon createKonsultasjon() {
        return new Konsultasjon();
    }

    /**
     * Create an instance of {@link Pasientjournal }
     * 
     */
    public Pasientjournal createPasientjournal() {
        return new Pasientjournal();
    }

    /**
     * Create an instance of {@link Virksomhet }
     * 
     */
    public Virksomhet createVirksomhet() {
        return new Virksomhet();
    }

    /**
     * Create an instance of {@link CS }
     * 
     */
    public CS createCS() {
        return new CS();
    }

    /**
     * Create an instance of {@link Identifikator }
     * 
     */
    public Identifikator createIdentifikator() {
        return new Identifikator();
    }

    /**
     * Create an instance of {@link Innleggelse }
     * 
     */
    public Innleggelse createInnleggelse() {
        return new Innleggelse();
    }

    /**
     * Create an instance of {@link Grunnopplysninger }
     * 
     */
    public Grunnopplysninger createGrunnopplysninger() {
        return new Grunnopplysninger();
    }

    /**
     * Create an instance of {@link Diagnose }
     * 
     */
    public Diagnose createDiagnose() {
        return new Diagnose();
    }

    /**
     * Create an instance of {@link Hendelse }
     * 
     */
    public Hendelse createHendelse() {
        return new Hendelse();
    }

    /**
     * Create an instance of {@link DatoEllerAar }
     * 
     */
    public DatoEllerAar createDatoEllerAar() {
        return new DatoEllerAar();
    }

    /**
     * Create an instance of {@link Kommune }
     * 
     */
    public Kommune createKommune() {
        return new Kommune();
    }

    /**
     * Create an instance of {@link Kontakt }
     * 
     */
    public Kontakt createKontakt() {
        return new Kontakt();
    }

    /**
     * Create an instance of {@link Bosted }
     * 
     */
    public Bosted createBosted() {
        return new Bosted();
    }

    /**
     * Create an instance of {@link Lagringsenhet }
     * 
     */
    public Lagringsenhet createLagringsenhet() {
        return new Lagringsenhet();
    }

    /**
     * Create an instance of {@link Avtale }
     * 
     */
    public Avtale createAvtale() {
        return new Avtale();
    }

    /**
     * Create an instance of {@link Supplerendeopplysninger }
     * 
     */
    public Supplerendeopplysninger createSupplerendeopplysninger() {
        return new Supplerendeopplysninger();
    }

    /**
     * Create an instance of {@link Journalidentifikator }
     * 
     */
    public Journalidentifikator createJournalidentifikator() {
        return new Journalidentifikator();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Oppdateringsinfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv", name = "oppdateringsinfo")
    public JAXBElement<Oppdateringsinfo> createOppdateringsinfo(Oppdateringsinfo value) {
        return new JAXBElement<Oppdateringsinfo>(_Oppdateringsinfo_QNAME, Oppdateringsinfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Avlevering }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.arkivverket.no/arkivverket/Arkivverket/Helsearkiv", name = "Avlevering")
    public JAXBElement<Avlevering> createAvlevering(Avlevering value) {
        return new JAXBElement<Avlevering>(_Avlevering_QNAME, Avlevering.class, null, value);
    }

}
