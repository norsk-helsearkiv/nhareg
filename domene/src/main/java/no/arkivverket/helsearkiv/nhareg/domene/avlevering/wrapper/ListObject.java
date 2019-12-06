package no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ListObject<T> {

    private T liste;
    
    private int total;
   
    private int page;
   
    private int size;
   
}
