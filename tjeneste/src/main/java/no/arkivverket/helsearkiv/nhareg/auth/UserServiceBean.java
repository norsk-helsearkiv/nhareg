package no.arkivverket.helsearkiv.nhareg.auth;

import no.arkivverket.helsearkiv.nhareg.domene.auth.Bruker;
import no.arkivverket.helsearkiv.nhareg.domene.auth.Rolle;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;


/**
 * Created by haraldk on 15.04.15.
 */
@Stateless
public class UserServiceBean implements UserService{
    @PersistenceContext(name = "primary")
    private EntityManager em;


    public String getRolle(final String username){
        return findByUsername(username).getRolle().getNavn();
    }

    public Bruker findByUsername(final String username) {
        return em.find(Bruker.class, username);
    }

    public List<Bruker> getAllBrukere() {
        String select = "SELECT b"
                + "        FROM Bruker b";
        final Query query = em.createQuery(select);
        return query.getResultList();
    }

    public Bruker createBruker(Bruker bruker){
        Bruker b = em.merge(bruker);
        return b;
    }
    public List<Rolle> getRoller(){
        String select = "SELECT b"
                + "        FROM Rolle b";
        final Query query = em.createQuery(select);
        return query.getResultList();
    }

    private static String plainToHash(final String userPass){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(userPass.getBytes());
            byte byteData[] = md.digest();
            String pass = DatatypeConverter.printBase64Binary(byteData);
            return pass;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void main (String[] args){
        System.out.println(plainToHash("truls"));
        System.out.println(plainToHash("admin"));
         //wFJL0uiR9Idsk1OyYd9ZJ0HsteHi9+VUafr2hykoN20=
         //jGl25bVBBBW96Qi9Te4V37Fnqchz/Eu4qB9vKrRIqRg=

    }

    public void updateLagringsenhet(final String username, final String lagringsenhet){
        Bruker b = findByUsername(username);
        b.setLagringsenhet(lagringsenhet);
        em.persist(b);
    }

    public String getLagringsenhet(final String username){
        return findByUsername(username).getLagringsenhet();
    }
    public void saveUser(final Bruker user) {
        //TODO
    }
}
