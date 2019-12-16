package no.arkivverket.helsearkiv.nhareg.gender;

import no.arkivverket.helsearkiv.nhareg.common.EntityDAO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Kjønn;

import javax.ejb.Stateless;

@Stateless
public class GenderDAO extends EntityDAO<Kjønn> {

    public GenderDAO() {
        super(Kjønn.class, "code");
    }

}
