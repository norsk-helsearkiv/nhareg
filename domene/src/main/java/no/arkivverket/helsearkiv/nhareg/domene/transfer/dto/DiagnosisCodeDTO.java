package no.arkivverket.helsearkiv.nhareg.domene.transfer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisCodeDTO {
    
    @JsonProperty(value = "code")
    private String code;
    
    @JsonProperty(value = "codeSystem")
    private String codeSystem;
    
    @JsonProperty(value = "codeSystemVersion")
    private String codeSystemVersion;

    @JsonProperty(value = "displayName")
    private String displayName;

    @JsonProperty(value = "originalText")
    private String originalText;
    
}