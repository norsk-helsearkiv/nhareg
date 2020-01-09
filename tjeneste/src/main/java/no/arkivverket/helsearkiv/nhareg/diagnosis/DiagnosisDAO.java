package no.arkivverket.helsearkiv.nhareg.diagnosis;

import no.arkivverket.helsearkiv.nhareg.common.EntityDAO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Diagnosis;

import javax.ejb.Stateless;

@Stateless
public class DiagnosisDAO extends EntityDAO<Diagnosis> {

    public DiagnosisDAO() {
        super(Diagnosis.class, "uuid");
    }

}