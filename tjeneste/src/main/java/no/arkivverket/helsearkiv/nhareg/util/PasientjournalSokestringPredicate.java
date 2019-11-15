package no.arkivverket.helsearkiv.nhareg.util;

//import com.sun.xml.internal.ws.util.StringUtils;
import java.util.List;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Grunnopplysninger;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Identifikator;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Lagringsenhet;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author arnfinns
 */
public class PasientjournalSokestringPredicate implements Predicate<Pasientjournal> {

    List<String> sokestrings;

    public PasientjournalSokestringPredicate(List<String> sokestrings) {
        this.sokestrings = sokestrings;
    }

    public boolean evaluate(Pasientjournal pasientjournal) {
        boolean evaluate = false;
        if (pasientjournal.getGrunnopplysninger() != null) {
            Grunnopplysninger grunnopplysninger = pasientjournal.getGrunnopplysninger();
            if (grunnopplysninger.getPnavn() != null) {
                String pnavn = grunnopplysninger.getPnavn();
                for (String sokestring : sokestrings) {
                    if (StringUtils.containsIgnoreCase(pnavn, sokestring)) {
                        evaluate = true;
                    }
                }
            }
            if (!evaluate) {
                if (grunnopplysninger.getIdentifikator() != null) {
                    Identifikator identifikator = grunnopplysninger.getIdentifikator();
                    String pid = identifikator.getPID();
                    for (String sokestring : sokestrings) {
                        if (StringUtils.containsIgnoreCase(pid, sokestring)) {
                            evaluate = true;
                        }
                    }
                }
            }
            if (!evaluate){
                for (Lagringsenhet e:pasientjournal.getLagringsenhet()){
                    String id = e.getIdentifikator();
                    for (String sokestring : sokestrings){
                        if (StringUtils.containsIgnoreCase(id, sokestring)){
                            evaluate = true;
                        }
                    }
                }
            }
        }
        return evaluate;
    }
}
