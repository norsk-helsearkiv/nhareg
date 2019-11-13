package no.arkivverket.helsearkiv.nhareg.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.bind.DatatypeConverter;

import no.arkivverket.helsearkiv.nhareg.domene.auth.Bruker;
import no.arkivverket.helsearkiv.nhareg.domene.auth.Rolle;


/**
 * Created by haraldk on 15.04.15.
 */
@Stateless
public class UserServiceBean implements UserService {

    @PersistenceContext(name = "primary")
    private EntityManager em;

    public String getRolle(final String username) {
        return findByUsername(username).getRolle().getNavn();
    }

    public Bruker findByUsername(final String username) {
        return em.find(Bruker.class, username);
    }

    public List<Bruker> getAllBrukere() {
        String select = "SELECT b FROM Bruker b";
        final Query query = em.createQuery(select);
        return query.getResultList();
    }

    public Bruker createBruker(Bruker bruker) {
        return em.merge(bruker);
    }
    
    public List<Rolle> getRoller() {
        String select = "SELECT b FROM Rolle b";
        final Query query = em.createQuery(select);
        
        return query.getResultList();
    }

    private static String plainToHash(final String userPass) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(userPass.getBytes());
            byte[] byteData = messageDigest.digest();
            
            return DatatypeConverter.printBase64Binary(byteData);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    public void updateLagringsenhet(final String username, final String lagringsenhet) {
        Bruker bruker = findByUsername(username);
        bruker.setLagringsenhet(lagringsenhet);
        em.persist(bruker);
    }

    public String getLagringsenhet(final String username) {
        return findByUsername(username).getLagringsenhet();
    }

    public void updateDefaultAvlevering(final String username, final String avleveringsidentifikator) {
        Bruker bruker = findByUsername(username);
        if (avleveringsidentifikator.equals(bruker.getDefaultAvleveringsUuid())) {
            bruker.setDefaultAvleveringsUuid(null);
        } else {
            bruker.setDefaultAvleveringsUuid(avleveringsidentifikator);
        }
        em.persist(bruker);
    }

    public void saveUser(final Bruker user) {
        //TODO
    }
}
