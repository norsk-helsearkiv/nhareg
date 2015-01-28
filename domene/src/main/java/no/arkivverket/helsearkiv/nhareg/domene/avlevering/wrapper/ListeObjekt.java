package no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper;

/**
 *
 * @author robing
 * @param <T>
 */
public class ListeObjekt<T> {

    private int total;
    private int side;
    private int antall;
    private T liste;
    
    public ListeObjekt(T liste, int total, int side, int antall) {
        setListe(liste);
        setTotal(total);
        setSide(side);
        setAntall(antall);
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSide() {
        return side;
    }

    public void setSide(int side) {
        this.side = side;
    }

    public int getAntall() {
        return antall;
    }

    public void setAntall(int antall) {
        this.antall = antall;
    }

    public T getListe() {
        return liste;
    }

    public void setListe(T liste) {
        this.liste = liste;
    }
}
