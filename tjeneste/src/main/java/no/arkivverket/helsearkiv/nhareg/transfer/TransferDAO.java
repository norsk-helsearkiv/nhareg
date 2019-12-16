package no.arkivverket.helsearkiv.nhareg.transfer;

import no.arkivverket.helsearkiv.nhareg.common.EntityDAO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;

import javax.ejb.Stateless;
import java.util.Set;

@Stateless
public class TransferDAO extends EntityDAO<Avlevering> {

    public TransferDAO() {
        super(Avlevering.class, "avleveringsidentifikator");
    }

    public Set<Pasientjournal> getMedicalRecords(final String id) {
        final Avlevering result = super.fetchById(id);
        final Set<Pasientjournal> medicalRecords = result.getPasientjournal();
        
        // Force load of lazy resources
        for (Pasientjournal record: medicalRecords) {
            record.toString();
        }
        
        return medicalRecords;
    }
}
