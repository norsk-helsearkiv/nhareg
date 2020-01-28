package no.arkivverket.helsearkiv.nhareg.domene.transfer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArchiveAuthorDTO implements Serializable {

    private String uuid;

    private String code;

    private String name;

    private String description;

}