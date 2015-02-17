package no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto;

import java.io.Serializable;

/**
 *
 * @author robing
 */
public class DiagnoseDTO implements Serializable {
    
    private String uuid;
    private String diagnosetekst;
    private String diagnosedato;
    private String diagnosekode;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

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
