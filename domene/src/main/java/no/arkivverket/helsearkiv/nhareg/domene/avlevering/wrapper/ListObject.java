package no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author robing
 * @param <T>
 */
@Data
@AllArgsConstructor
public class ListObject<T> {

    private T liste;
    
    private int total;
   
    private int page;
   
    private int number;
   
}
