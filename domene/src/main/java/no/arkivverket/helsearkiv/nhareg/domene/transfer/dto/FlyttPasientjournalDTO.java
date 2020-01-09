package no.arkivverket.helsearkiv.nhareg.domene.transfer.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FlyttPasientjournalDTO implements Serializable {

    private List<String> pasientjournalUuids;
    
    private String lagringsenhetIdentifikator;

}