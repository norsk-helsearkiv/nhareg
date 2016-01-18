package no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto;

import java.io.Serializable;

/**
 *
 * @author robing
 */

public class PersondataDTO implements Serializable {


    private String[] lagringsenheter;
    private String fanearkid;
    private String journalnummer;
    private String lopenummer;
    private String fodselsnummer;
    
    private String navn;
    //Kode - Visningsnavnet kan endres, men koden er fast
    private String kjonn;
    
    private String fodt;
    private String dod;
    private String fKontakt;
    private String sKontakt;
    
    //For Oppdatering trenger vi iden
    private String uuid;
    
    public PersondataDTO() {
    }

    public String getFanearkid(){
        return fanearkid;
    }
    public void setFanearkid(String fanearkid){
        this.fanearkid = fanearkid;
    }
    public String[] getLagringsenheter() {
        return lagringsenheter;
    }

    public void setLagringsenheter(String[] lagringsenheter) {
        this.lagringsenheter = lagringsenheter;
    }

    public String getJournalnummer() {
        return journalnummer;
    }

    public void setJournalnummer(String journalnummer) {
        this.journalnummer = journalnummer;
    }

    public String getLopenummer() {
        return lopenummer;
    }

    public void setLopenummer(String lopenummer) {
        this.lopenummer = lopenummer;
    }

    public String getFodselsnummer() {
        return fodselsnummer;
    }

    public void setFodselsnummer(String fodselsnummer) {
        this.fodselsnummer = fodselsnummer;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getKjonn() {
        return kjonn;
    }

    public void setKjonn(String kjonn) {
        this.kjonn = kjonn;
    }

    public String getFodt() {
        return fodt;
    }

    public void setFodt(String fodt) {
        this.fodt = fodt;
    }

    public String getDod() {
        return dod;
    }

    public void setDod(String dod) {
        this.dod = dod;
    }

    public String getfKontakt() {
        return fKontakt;
    }

    public void setfKontakt(String fKontakt) {
        this.fKontakt = fKontakt;
    }

    public String getsKontakt() {
        return sKontakt;
    }

    public void setsKontakt(String sKontakt) {
        this.sKontakt = sKontakt;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    
    
}
