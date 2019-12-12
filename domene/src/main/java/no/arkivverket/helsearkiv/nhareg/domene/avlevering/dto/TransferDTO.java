package no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransferDTO {
    
    private String transferId;
    
    private String arkivskaper;
    
    private String transferDescription;
    
    private boolean locked;
    
    private String storageUnit;
    
    private String updatedBy;
    
    private String processSteps;
    
    private LocalDateTime lastUpdated;
    
    private String agreementId;
    
}