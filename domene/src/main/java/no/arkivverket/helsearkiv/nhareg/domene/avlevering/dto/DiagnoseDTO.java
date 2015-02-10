package no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto;

import java.io.Serializable;

/**
 *
 * @author robing
 */
public class DiagnoseDTO implements Serializable {
    
    //I validation.xml skal denne være satt (required)
    private String diagnosetekst;
    
    //Dette feltet skal valideres som en DatoEllerAar i validation.xml
    //Kan være null
    private String diagnosedato;
    
    //Kan være null
    private String diagnosekode;

    public String getDiagnosetekst() {
        return diagnosetekst;
    }

    public void setDiagnosetekst(String diagnosetekst) {
        this.diagnosetekst = diagnosetekst;
    }

    public String getDiagnosedato() {
        return diagnosedato;
    }

    public void setDiagnosedato(String diagnosedato) {
        this.diagnosedato = diagnosedato;
    }

    public String getDiagnosekode() {
        return diagnosekode;
    }

    public void setDiagnosekode(String diagnosekode) {
        this.diagnosekode = diagnosekode;
    }
}
