package no.arkivverket.helsearkiv.nhareg.domene.transfer.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListObject<T> {

    @JsonProperty(value = "liste")
    private T list;
    
    private int total;
   
    private int page;
   
    private int size;
   
}