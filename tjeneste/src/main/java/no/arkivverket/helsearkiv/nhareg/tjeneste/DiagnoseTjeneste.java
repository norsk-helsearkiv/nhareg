package no.arkivverket.helsearkiv.nhareg.tjeneste;

import java.util.Calendar;
import java.util.UUID;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.ws.rs.Path;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Diagnose;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Oppdateringsinfo;

/**
 * <p>
 * JAX-RS endepunkt for håndtering av {@link Diagnose}r. Arver metodene fra
 * {@link EntitetsTjeneste}i tillegg til egne metoder.
 * </p>
 *
 */
@Path("/diagnoser")
/**
 * <p>
 * Dette er en stateless service, vi deklarer den som EJB for å få
 * transaksjonsstøtte.
 * </p>
 */
@Stateless
public class DiagnoseTjeneste extends EntitetsTjeneste<Diagnose, String> {

    @Resource
    private SessionContext sessionContext;

    public DiagnoseTjeneste() {
        super(Diagnose.class, String.class, "uuid");

    }

    @Override
    public Diagnose create(Diagnose entity) {
        if (entity != null) {
            entity.setUuid(UUID.randomUUID().toString());
        }
        entity.setOppdateringsinfo(konstruerOppdateringsinfo());
        return super.create(entity);
    }
    
    

    /**
     * NB! Det er tvilsomt/feil at denne skal brukes i noen sammenhenger.
     * 
     * @param kode
     * @return 
     */
    public Diagnose hentDiagnoseMedKode(String kode) {
        String jpql = "SELECT d FROM Diagnose d WHERE d.diagnosekode.code = :kode";
        Query q = getEntityManager().createQuery(jpql);
        q.setParameter("kode", kode);
        try {
            return (Diagnose) q.getResultList().get(0);
        } catch (NoResultException nre) {
            return null;
        }
    }

    private Oppdateringsinfo konstruerOppdateringsinfo() {
        Oppdateringsinfo oppdateringsinfo = new Oppdateringsinfo();
        oppdateringsinfo.setOppdatertAv(sessionContext.getCallerPrincipal().getName());
        oppdateringsinfo.setSistOppdatert(Calendar.getInstance());
        return oppdateringsinfo;
    }

}
