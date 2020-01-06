package no.arkivverket.helsearkiv.nhareg.gender;

import no.arkivverket.helsearkiv.nhareg.common.EntityDAO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Gender;

import javax.ejb.Stateless;

@Stateless
public class GenderDAO extends EntityDAO<Gender> {

    public GenderDAO() {
        super(Gender.class, "code");
    }

}