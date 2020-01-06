package no.arkivverket.helsearkiv.nhareg.util;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Lagringsenhet;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class EtikettBuilder {
    
    private static final String ARKIVSKAPER= "<VAR1>";
    private static final String AVTALEIDENTIFIKATOR= "<VAR2>";
    private static final String AVLEVERINGSIDENTIFIKATOR= "<VAR3>";
    private static final String AVTALEBESKRIVELSE= "<VAR4>";
    private static final String AVLEVERINGSBESKRIVELSE= "<VAR5>";
    private static final String ANTALL_JOURNALER= "<VAR6>";
    private static final String LAGRINGSENHET_IDENTIFIKATOR= "<VAR7>";
    
    public String buildContent(final String template, final Lagringsenhet storageUnit, final Avlevering transfer,
                               final Integer storageUnitSize) throws IOException {
        final String content = FileUtils.readFileToString(new File(template), "UTF-8");

        return content.replace(ARKIVSKAPER, transfer.getArkivskaper())
                .replace(AVTALEIDENTIFIKATOR, transfer.getAgreement().getAgreementId())
                .replace(AVLEVERINGSIDENTIFIKATOR, transfer.getAvleveringsidentifikator())
                .replace(AVTALEBESKRIVELSE, transfer.getAgreement().getAvtalebeskrivelse())
                .replace(AVLEVERINGSBESKRIVELSE, transfer.getAvleveringsbeskrivelse())
                .replace(ANTALL_JOURNALER, String.valueOf(storageUnitSize))
                .replace(LAGRINGSENHET_IDENTIFIKATOR, storageUnit.getIdentifikator());
    }

}