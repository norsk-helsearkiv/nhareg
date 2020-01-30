package no.arkivverket.helsearkiv.nhareg.util;

import no.arkivverket.helsearkiv.nhareg.domene.transfer.StorageUnit;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Transfer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LabelBuilder {
    
    private static final String ARCHIVE_AUTHOR = "<VAR1>";
    private static final String AGREEMENT_ID = "<VAR2>";
    private static final String TRANSFER_ID = "<VAR3>";
    private static final String AGREEMENT_DESCRIPTION = "<VAR4>";
    private static final String TRANSFER_DESCRIPTION = "<VAR5>";
    private static final String RECORD_NUMBER = "<VAR6>";
    private static final String STORAGE_UNIT_ID = "<VAR7>";
    
    public String buildContent(final String template, final StorageUnit storageUnit, final Transfer transfer,
                               final Integer storageUnitSize) throws IOException {
        final byte[] encodedContent = Files.readAllBytes(Paths.get(template));
        final String content = new String(encodedContent, StandardCharsets.UTF_8);

        return content.replace(ARCHIVE_AUTHOR, transfer.getArchiveAuthor().getName())
                .replace(AGREEMENT_ID, transfer.getAgreement().getAgreementId())
                .replace(TRANSFER_ID, transfer.getTransferId())
                .replace(AGREEMENT_DESCRIPTION, transfer.getAgreement().getAgreementDescription())
                .replace(TRANSFER_DESCRIPTION, transfer.getTransferDescription())
                .replace(RECORD_NUMBER, String.valueOf(storageUnitSize))
                .replace(STORAGE_UNIT_ID, storageUnit.getId());
    }

}