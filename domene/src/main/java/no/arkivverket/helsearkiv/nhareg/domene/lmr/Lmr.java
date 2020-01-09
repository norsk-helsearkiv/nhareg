package no.arkivverket.helsearkiv.nhareg.domene.lmr;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "mperson")
public class Lmr {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mpid;
    
    @Column
    @Size(min = 11, max = 11)
    private String fnr;
    
    @Column
    private LocalDate fdato;

    @Column
    @Size(min = 1, max = 1)
    private String status;

    @Column
    private LocalDate ddato;

    @Column
    private String enavn;

    @Column
    private String fnavn;
    
    @Column
    private String mnavn;
    
    @Column
    private String hnavn;
    
    @Column
    private String fkomn;
    
    @Column
    private String fland;
    
    @Column
    private String fsted;

    @Column
    private String badr;

    @Column
    private String bpost;

    @Column
    private String bsted;
    
    @Column
    private String hadr;

    @Column
    private String hpost;
    
    @Column
    private String hsted;

    @Column
    private String bkomn;
    
    @Column
    private String hkomn;
    
    @Column
    @Size(min = 1, max = 1)
    private String kjonn;

}