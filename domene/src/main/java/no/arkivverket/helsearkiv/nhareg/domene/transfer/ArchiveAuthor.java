package no.arkivverket.helsearkiv.nhareg.domene.transfer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "arkivskaper")
public class ArchiveAuthor implements Serializable {
    
    @Id
    @Column(name = "uuid")
    private String uuid;

    @Column(name = "kode", unique = true)
    private String code;
    
    @Column(name = "navn", unique = true, nullable = false)
    private String name;
    
    @Column(name = "beskrivelse")
    private String description;
    
}