package no.arkivverket.helsearkiv.nhareg.auth;

import no.arkivverket.helsearkiv.nhareg.domene.auth.Bruker;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Created by haraldk on 15.04.15.
 */
@Stateless
public class UserServiceBean implements UserService{
    @PersistenceContext(name = "primary")
    private EntityManager em;

    @Override
    public Bruker findByUsername(String username) {
        return em.find(Bruker.class, username);
    }

    private String plainToHash(String userPass){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(userPass.getBytes());
            byte byteData[] = md.digest();
            String pass = DatatypeConverter.printBase64Binary(byteData);
            //String pass =  new String(Base64.getEncoder().encode(byteData));
            return pass;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveUser(Bruker user) {
        //TODO
    }
}
