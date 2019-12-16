package no.arkivverket.helsearkiv.nhareg.medicalrecord;

import no.arkivverket.helsearkiv.nhareg.common.Roller;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;

/**
 * <p>
 * Dette er en stateless service, vi deklarer den som EJB for å få
 * transaksjonsstøtte.
 * </p>
 */
@Stateless
@RolesAllowed(value = {Roller.ROLE_ADMIN, Roller.ROLE_BRUKER})
public class PasientjournalTjeneste {

    // public List<RecordTransferDTO> hentPasientjournalerForLagringsenhet(String identifikator) {
    //     List<Pasientjournal> res = sokPasientjournalerForLagringsenhet(identifikator);
    //     List<RecordTransferDTO> finalList = new ArrayList<>();
    //    
    //     for (Pasientjournal pasientjournal : res) {
    //         if (pasientjournal.getSlettet() == null || !pasientjournal.getSlettet()) {
    //             RecordTransferDTO sokeresultatDTO = MedicalRecordMapper.convertToRecordTransferDTO(pasientjournal);
    //             finalList.add(sokeresultatDTO);
    //         }
    //     }
    //    
    //     return finalList;
    // }

    // public List<Pasientjournal> sokPasientjournalerForLagringsenhet(String identifikator) {
    //     String select = "SELECT p "
    //                     + "FROM Pasientjournal p "
    //                     + "INNER JOIN p.lagringsenhet l "
    //                     + "WHERE l.identifikator = :identifikator ";
    //     final Query query = getEntityManager().createQuery(select);
    //     query.setParameter("identifikator", identifikator);
    //
    //     return (List<Pasientjournal>) query.getResultList();
    // }

}
