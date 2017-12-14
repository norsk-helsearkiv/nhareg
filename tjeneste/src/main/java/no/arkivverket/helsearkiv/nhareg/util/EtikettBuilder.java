package no.arkivverket.helsearkiv.nhareg.util;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Lagringsenhet;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by haraldk on 13/12/2017.
 */
public class EtikettBuilder {
    private static final String ARKIVSKAPER= "<VAR1>";
    private static final String AVTALEIDENTIFIKATOR= "<VAR2>";
    private static final String AVLEVERINGSIDENTIFIKATOR= "<VAR3>";
    private static final String AVTALEBESKRIVELSE= "<VAR4>";
    private static final String AVLEVERINGSBESKRIVELSE= "<VAR5>";
    private static final String ANTALL_JOURNALER= "<VAR6>";
    private static final String LAGRINGSENHET_IDENTIFIKATOR= "<VAR7>";



    public String buildContent(String template, Lagringsenhet le, Avlevering avl, Integer lagringsenhetAntall) throws IOException {
        String content = FileUtils.readFileToString(new File(template), "UTF-8");

        return content.replace(ARKIVSKAPER, avl.getArkivskaper())
                .replace(AVTALEIDENTIFIKATOR, avl.getAvtale().getAvtaleidentifikator())
                .replace(AVLEVERINGSIDENTIFIKATOR, avl.getAvleveringsidentifikator())
                .replace(AVTALEBESKRIVELSE, avl.getAvtale().getAvtalebeskrivelse())
                .replace(AVLEVERINGSBESKRIVELSE, avl.getAvleveringsbeskrivelse())
                .replace(ANTALL_JOURNALER, String.valueOf(lagringsenhetAntall))
                .replace(LAGRINGSENHET_IDENTIFIKATOR, le.getIdentifikator());
    }

}
