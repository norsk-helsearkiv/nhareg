package no.arkivverket.helsearkiv.nhareg.util;

import no.arkivverket.helsearkiv.nhareg.common.DateOrYearConverter;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by haraldk on 07.05.15.
 */
public class SortPasientjournaler {

    public void sort(final List<Pasientjournal> pasientjournaler, final String order, final String direction) {
        if (order != null) {
            final FlexibleComparator comp = new FlexibleComparator(Order.valueOf(order));
            boolean asc = "asc".equals(direction);//"desc" for descending..
            pasientjournaler.sort(asc ? comp : Collections.reverseOrder(comp));
            
            return;
        }

        pasientjournaler.sort((value, other) -> {
            final Oppdateringsinfo updateInfoValue = value.getOppdateringsinfo();
            final Oppdateringsinfo updateInfoOther = other.getOppdateringsinfo();
            if (updateInfoValue == null && updateInfoOther == null) {
                return 0;
            }

            if (updateInfoValue == null) {
                return -1;
            }

            if (updateInfoOther == null) {
                return 1;
            }
            
            if (updateInfoValue.getSistOppdatert() == null && updateInfoOther.getSistOppdatert() == null) {
                return 0;
            }

            if (updateInfoValue.getSistOppdatert() != null) {
                return -1;
            }

            if (updateInfoOther.getSistOppdatert() != null) {
                return 1;
            }

            return updateInfoOther.getSistOppdatert().compareTo(updateInfoValue.getSistOppdatert());
        });
    }

    public enum Order {lagringsenhet, fodselsnummer, jnr, lnr, fanearkid, navn, faar, daar, oppdatertAv}

    class FlexibleComparator implements Comparator<Pasientjournal> {
        private Order sortingBy;

        public FlexibleComparator(final Order sortingBy){
            this.sortingBy = sortingBy;
        }
        
        public int compare(final Pasientjournal recordOne, final Pasientjournal recordTwo) {
            final Journalidentifikator journalOneId = recordOne.getJournalidentifikator();
            final Journalidentifikator journalTwoId = recordTwo.getJournalidentifikator();
            final Grunnopplysninger baseInfoOne = recordOne.getGrunnopplysninger();
            final Grunnopplysninger baseInfoTwo = recordTwo.getGrunnopplysninger();

            switch(sortingBy) {
                case lagringsenhet:
                    final int storageOneSize = recordOne.getLagringsenhet().size();
                    final int storageTwoSize = recordTwo.getLagringsenhet().size();
                    
                    if (storageOneSize > 0 && storageTwoSize > 0) {
                        final String recordOneId = recordOne.getLagringsenhet().get(0).getIdentifikator();
                        final String recordTwoId = recordTwo.getLagringsenhet().get(0).getIdentifikator();
                        return comp(recordOneId, recordTwoId);
                    }
                    
                    if (storageOneSize == 0 && storageTwoSize == 0)
                        return 0;
                    
                    return storageOneSize == 0 ? 1 : -1;

                case fodselsnummer:
                    final Identifikator recordOneId = baseInfoOne.getIdentifikator();
                    final Identifikator recordTwoId = baseInfoTwo.getIdentifikator();
                    
                    if (recordOneId != null && recordTwoId != null) {
                        return comp(recordOneId.getPID(), recordTwoId.getPID());
                    }
                    
                    if (recordOneId == null && recordTwoId == null) {
                        return 0;
                    }
                    
                    return recordOneId == null ? 1 : -1;
                
                case jnr:
                    if (journalOneId != null && journalTwoId != null) {
                        return comp(journalOneId.getJournalnummer(), journalTwoId.getJournalnummer());
                    }
                    
                    if (journalOneId == null && journalTwoId == null) {
                        return 0;
                    }
                    
                    return journalOneId == null ? 1 : -1;
                
                case fanearkid:
                    if (recordOne == null || recordTwo == null) {
                        return 0;
                    }

                    if (recordOne != null && recordTwo != null) {
                        return comp(recordOne.getFanearkid(), recordTwo.getFanearkid());
                    }
                
                    return journalOneId == null ? 1 : -1;
                
                case lnr:
                    if (journalOneId != null && journalTwoId !=null) {
                        return comp(journalOneId.getLøpenummer(), journalTwoId.getLøpenummer());
                    }
                    
                    if (journalOneId == null && journalTwoId ==null) {
                        return 0;
                    }
                    
                    return journalOneId == null ? 1 : -1;
                
                case navn:
                    if (baseInfoOne != null && baseInfoTwo != null) {
                        return comp(baseInfoOne.getPnavn(), baseInfoTwo.getPnavn());
                    }
                    
                    if (baseInfoOne == null && baseInfoTwo == null) {
                        return 0;
                    }
                
                    return baseInfoOne == null ? 1 : -1;
                
                case faar:
                    if (baseInfoOne != null && baseInfoTwo != null) {
                        final DatoEllerAar bornOne = baseInfoOne.getFødt();
                        final DatoEllerAar bornTwo = baseInfoTwo.getFødt();
                        return compDate(DateOrYearConverter.fromDateOrYear(bornOne),
                                        DateOrYearConverter.fromDateOrYear(bornTwo));
                    }
                    
                    if (baseInfoOne == null && baseInfoTwo == null) {
                        return 0;
                    }
                    
                    return baseInfoOne == null ? 1 : -1;
                
                case daar:
                    if (baseInfoOne != null && baseInfoTwo != null) {
                        final String deadOne = DateOrYearConverter.fromDateOrYear(baseInfoOne.getDød());
                        final String deadTwo = DateOrYearConverter.fromDateOrYear(baseInfoTwo.getDød());
                        return compDate(deadOne, deadTwo);
                    }
                    
                    if (baseInfoOne == null && baseInfoTwo == null) {
                        return 0;
                    }
                    
                    return baseInfoOne == null ? 1 : -1;
                
                case oppdatertAv:
                    final Oppdateringsinfo updateInfoOne = recordOne.getOppdateringsinfo();
                    final Oppdateringsinfo updateInfoTwo = recordTwo.getOppdateringsinfo();
                    
                    if (updateInfoOne == null && updateInfoTwo == null) {
                        return 0;
                    }

                    if (updateInfoOne != null && updateInfoTwo != null){
                        return comp(updateInfoOne.getOppdatertAv(), updateInfoTwo.getOppdatertAv());
                    }
                    
                    return updateInfoOne == null ? 1 : -1;
            }

            throw new RuntimeException("Practically unreachable code, can't be thrown");
        }

        public void setSortingBy(Order sortBy) {
            this.sortingBy = sortBy;
        }

        private int comp(final String value, final String other) {
            if (value != null && !value.isEmpty() && other != null && !other.isEmpty()) {
                return value.compareTo(other);
            }
            
            if ((value == null || value.isEmpty()) && (other == null || other.isEmpty())) {
                return 0;
            }

            return (value == null || value.isEmpty()) ? 1 : -1;
        }


        private int compDate(final String value, final String other) {
            final Date dateValue = createDate(value);
            final Date dateOther = createDate(other);
            
            if (dateValue != null && dateOther != null) {
                return dateValue.compareTo(dateOther);
            }
            
            if (dateValue == null && dateOther == null) {
                return 0;
            }
            return (dateValue == null) ? 1 : -1;
        }

        private Date createDate(String dateString) {
            if (dateString == null)
                dateString = "01.01.0001";
            final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            try {
                String dateToConvert = dateString;
                if (dateString.length() == 4) {//year
                    dateToConvert = "01.01." + dateString;
                }
 
                return format.parse(dateToConvert);
            } catch (ParseException e) {
                e.printStackTrace();
            }
 
            return null;
        }
    }

}