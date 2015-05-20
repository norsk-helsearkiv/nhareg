package no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author robing
 */
public class PasientjournalDTO implements Serializable {
    
    private PersondataDTO persondata;
    private List<DiagnoseDTO> diagnoser;
    private String virksomhet;
    private String avtaleBeskrivelse;
    private String avleveringBeskrivelse;

    public String getVirksomhet() {
        return virksomhet;
    }

    public void setVirksomhet(String virksomhet) {
        this.virksomhet = virksomhet;
    }

    public String getAvtaleBeskrivelse() {
        return avtaleBeskrivelse;
    }

    public void setAvtaleBeskrivelse(String avtaleBeskrivelse) {
        this.avtaleBeskrivelse = avtaleBeskrivelse;
    }

    public String getAvleveringBeskrivelse() {
        return avleveringBeskrivelse;
    }

    public void setAvleveringBeskrivelse(String avleveringBeskrivelse) {
        this.avleveringBeskrivelse = avleveringBeskrivelse;
    }

    public String getAvleveringsidentifikator() {
        return avleveringsidentifikator;
    }

    public void setAvleveringsidentifikator(String avleveringsidentifikator) {
        this.avleveringsidentifikator = avleveringsidentifikator;
    }

    private String avleveringsidentifikator;


    public PersondataDTO getPersondata() {
        return persondata;
    }

    public void setPersondata(PersondataDTO persondata) {
        this.persondata = persondata;
    }

    public List<DiagnoseDTO> getDiagnoser() {
        return diagnoser;
    }

    public void setDiagnoser(List<DiagnoseDTO> diagnoser) {
        this.diagnoser = diagnoser;
    }
    
    
}
