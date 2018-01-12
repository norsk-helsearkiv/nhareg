package no.arkivverket.helsearkiv.nhareg.util;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Oppdateringsinfo;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import no.arkivverket.helsearkiv.nhareg.transformer.DatoEllerAarTilStringTransformer;
import org.apache.commons.lang3.StringUtils;
import org.picketbox.util.StringUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by haraldk on 07.05.15.
 */
public class SortPasientjournaler {


    public void sort(List<Pasientjournal> pasientjournaler, final String order, final String direction){
        if (order!=null) {
            FlexibleComparator comp = new FlexibleComparator(Order.valueOf(order));
            boolean asc = "asc".equals(direction);//"desc" for descending..
            Collections.sort(pasientjournaler, asc?comp:Collections.reverseOrder(comp));
            return;
        }

        Collections.sort(pasientjournaler, new Comparator<Pasientjournal>() {
            public int compare(Pasientjournal o1, Pasientjournal o2) {
                Oppdateringsinfo io1 = o1.getOppdateringsinfo();
                Oppdateringsinfo io2 = o2.getOppdateringsinfo();
                if (io1==null&&io2==null)
                    return 0;
                if (io1==null)
                    return -1;
                if (io2 == null)
                    return 1;
                if (io1.getSistOppdatert() == null && io2.getSistOppdatert() == null) {
                    return 0;
                }
                if (io1.getSistOppdatert() != null) {
                    return -1;
                }
                if (io2.getSistOppdatert() != null) {
                    return 1;
                }
                int val =  io2.getSistOppdatert().compareTo(io1.getSistOppdatert());
                return val;
            }
        });
    }

    public enum Order {lagringsenhet, fodselsnummer, jnr, lnr, fanearkid, navn, faar, daar, oppdatertAv}

     class FlexibleComparator implements Comparator<Pasientjournal> {
        private Order sortingBy;
        private DatoEllerAarTilStringTransformer trans = new DatoEllerAarTilStringTransformer();

         public FlexibleComparator(final Order sortingBy){
             this.sortingBy = sortingBy;
         }
        public int compare(Pasientjournal p1, Pasientjournal p2) {
            switch(sortingBy) {
                case lagringsenhet:
                    if (p1.getLagringsenhet().size() > 0 && p2.getLagringsenhet().size() > 0)
                        return comp(p1.getLagringsenhet().get(0).getIdentifikator(), p2.getLagringsenhet().get(0).getIdentifikator());
                    if (p1.getLagringsenhet().size() == 0 && p2.getLagringsenhet().size() == 0)
                        return 0;
                    return p1.getLagringsenhet().size() == 0 ? 1 : -1;

                case fodselsnummer:
                    if (p1.getGrunnopplysninger().getIdentifikator()!=null&&p2.getGrunnopplysninger().getIdentifikator()!=null){
                        int v = comp(p1.getGrunnopplysninger().getIdentifikator().getPID(), p2.getGrunnopplysninger().getIdentifikator().getPID());
                        return v;
                    }
                    if (p1.getGrunnopplysninger().getIdentifikator()==null && p2.getGrunnopplysninger().getIdentifikator()==null){
                        return 0;
                    }
                    return p1.getGrunnopplysninger().getIdentifikator()==null?1:-1;
                case jnr:
                    if (p1.getJournalidentifikator()!=null&&p2.getJournalidentifikator()!=null){
                        return comp(p1.getJournalidentifikator().getJournalnummer(), p2.getJournalidentifikator().getJournalnummer());
                    }
                    if (p1.getJournalidentifikator()==null&&p2.getJournalidentifikator()==null){
                        return 0;
                    }
                    return p1.getJournalidentifikator()==null?1:-1;
                case fanearkid:
                    if (p1!=null&&p2!=null){
                        return comp(p1.getFanearkid(), p2.getFanearkid());
                    }
                    if (p1==null&&p2==null){
                        return 0;
                    }
                    return p1.getJournalidentifikator()==null?1:-1;
                case lnr:
                    if (p1.getJournalidentifikator()!=null&&p2.getJournalidentifikator()!=null){
                        return comp(p1.getJournalidentifikator().getLøpenummer(), p2.getJournalidentifikator().getLøpenummer());
                    }
                    if (p1.getJournalidentifikator()==null&&p2.getJournalidentifikator()==null){
                        return 0;
                    }
                    return p1.getJournalidentifikator()==null?1:-1;
                case navn:
                    if (p1.getGrunnopplysninger()!=null&&p2.getGrunnopplysninger()!=null){
                        return comp(p1.getGrunnopplysninger().getPnavn(), p2.getGrunnopplysninger().getPnavn());
                    }
                    if (p1.getGrunnopplysninger()==null&&p2.getGrunnopplysninger()==null){
                        return 0;
                    }
                    return p1.getGrunnopplysninger()==null?1:-1;
                case faar:
                    if (p1.getGrunnopplysninger()!=null&&p2.getGrunnopplysninger()!=null){
                        return compDate(trans.transform(p1.getGrunnopplysninger().getFødt()), trans.transform(p2.getGrunnopplysninger().getFødt()));
                    }
                    if (p1.getGrunnopplysninger()==null&&p2.getGrunnopplysninger()==null){
                        return 0;
                    }
                    return p1.getGrunnopplysninger()==null?1:-1;
                case daar:
                    if (p1.getGrunnopplysninger()!=null&&p2.getGrunnopplysninger()!=null){
                        String p1Dod = trans.transform(p1.getGrunnopplysninger().getDød());
                        String p2Dod = trans.transform(p2.getGrunnopplysninger().getDød());
                        return compDate(p1Dod, p2Dod);
                    }
                    if (p1.getGrunnopplysninger()==null&&p2.getGrunnopplysninger()==null){
                        return 0;
                    }
                    return p1.getGrunnopplysninger()==null?1:-1;
                case oppdatertAv:
                    if (p1.getOppdateringsinfo()!=null&&p2.getOppdateringsinfo()!=null){
                        return comp(p1.getOppdateringsinfo().getOppdatertAv(), p2.getOppdateringsinfo().getOppdatertAv());
                    }
                    if (p1.getOppdateringsinfo()==null&&p2.getOppdateringsinfo()==null){
                        return 0;
                    }
                    return p1.getOppdateringsinfo()==null?1:-1;
            }
            throw new RuntimeException("Practically unreachable code, can't be thrown");
        }

        public void setSortingBy(Order sortBy) {
            this.sortingBy = sortBy;
        }

        private int comp(String s1, String s2){
            if (!StringUtils.isBlank(s1) && !StringUtils.isBlank(s2)){
                return s1.compareTo(s2);
            }
            if (StringUtils.isBlank(s1) && StringUtils.isBlank(s2)){
                return 0;
            }

            return (StringUtils.isBlank(s1))?1:-1;
        }
         

        private int compDate(String s1, String s2){
            Date d1 = createDate(s1);
            Date d2 = createDate(s2);
            if (d1!=null && d2!=null){
                return d1.compareTo(d2);
            }
            if (d1==null&&d2==null){
                return 0;
            }
            return (d1 == null) ? 1 : -1;
        }

        private Date createDate(String dateIn){
            if (dateIn==null)
                dateIn = "01.01.0001";
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            try {
                String dateToConvert = dateIn;
                if (dateIn!=null&&dateIn.length()==4) {//year
                    dateToConvert = "01.01."+dateIn;
                }
                return format.parse(dateToConvert);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
