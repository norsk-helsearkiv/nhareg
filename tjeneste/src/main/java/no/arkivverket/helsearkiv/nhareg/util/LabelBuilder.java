package no.arkivverket.helsearkiv.nhareg.util;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.StorageUnit;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Transfer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LabelBuilder {
    
    private static final String ARCHIVECREATOR = "<VAR1>";
    private static final String AGREEMENTID = "<VAR2>";
    private static final String TRANSFERID = "<VAR3>";
    private static final String AGREEMENTDESCRIPTION= "<VAR4>";
    private static final String TRANSFERDESCRIPTION = "<VAR5>";
    private static final String RECORD_NUMBER = "<VAR6>";
    private static final String STORAGEUNITID = "<VAR7>";
    
    public String buildContent(final String template, final StorageUnit storageUnit, final Transfer transfer,
                               final Integer storageUnitSize) throws IOException {
        final byte[] encodedContent = Files.readAllBytes(Paths.get(template));
        final String content = new String(encodedContent, StandardCharsets.UTF_8);

        return content.replace(ARCHIVECREATOR, transfer.getArchiveAuthor().getName())
                .replace(AGREEMENTID, transfer.getAgreement().getAgreementId())
                .replace(TRANSFERID, transfer.getTransferId())
                .replace(AGREEMENTDESCRIPTION, transfer.getAgreement().getAgreementDescription())
                .replace(TRANSFERDESCRIPTION, transfer.getTransferDescription())
                .replace(RECORD_NUMBER, String.valueOf(storageUnitSize))
                .replace(STORAGEUNITID, storageUnit.getId());
    }

}