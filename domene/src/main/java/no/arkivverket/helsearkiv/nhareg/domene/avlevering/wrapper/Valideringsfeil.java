package no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper;

import java.io.Serializable;

/**
 *  Valideringsfeil med attributt og constraint som feiler.
 * @author robing
 */
public class Valideringsfeil implements Serializable {
    
    private String attributt;
    private String constriant;

    public Valideringsfeil(String attributt, String constriant) {
        setAttributt(attributt);
        setConstriant(constriant);
    }

    public String getAttributt() {
        return attributt;
    }

    public void setAttributt(String attributt) {
        this.attributt = attributt;
    }

    public String getConstriant() {
        return constriant;
    }

    public void setConstriant(String constriant) {
        this.constriant = constriant;
    }

}
