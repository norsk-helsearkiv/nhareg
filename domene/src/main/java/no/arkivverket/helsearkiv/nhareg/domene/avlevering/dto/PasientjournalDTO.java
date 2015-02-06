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
