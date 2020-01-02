package no.arkivverket.helsearkiv.nhareg.diagnosis;

import no.arkivverket.helsearkiv.nhareg.common.EntityDAO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Diagnose;

import javax.ejb.Stateless;

@Stateless
public class DiagnosisDAO extends EntityDAO<Diagnose> {

    public DiagnosisDAO() {
        super(Diagnose.class, "uuid");
    }

}