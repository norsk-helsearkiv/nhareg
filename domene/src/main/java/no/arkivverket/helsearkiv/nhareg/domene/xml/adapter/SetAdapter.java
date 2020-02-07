package no.arkivverket.helsearkiv.nhareg.domene.xml.adapter;

import no.arkivverket.helsearkiv.nhareg.domene.additionalinfo.*;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.ArchiveAuthor;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Diagnosis;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.MedicalRecord;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Generic adapter which initializes a new class, using .newInstance(), and adds it to the set if it is empty.
 * Contains static inner classes to initialize the class type.
 * @param <T> Class type the set consists of.
 */
public abstract class SetAdapter<T> extends XmlAdapter<Set<T>, Set<T>> {

    private Class<T> tClass;

    public final void settClass(final Class<T> tClass) {
        this.tClass = tClass;
    }

    @Override
    public Set<T> unmarshal(final Set<T> ignored) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<T> marshal(final Set<T> value) throws IllegalAccessException, InstantiationException {
        if (value.isEmpty()) {
            value.add(tClass.newInstance());
        }

        return value;
    }

    public final static class DiagnosisAdapter extends SetAdapter<Diagnosis> {
        public DiagnosisAdapter() {
            this.settClass(Diagnosis.class);
        }

        @Override
        public Set<Diagnosis> marshal(final Set<Diagnosis> value) throws IllegalAccessException, InstantiationException {
            value.forEach(diagnosis -> {
                final String codingSystem = diagnosis.getDiagnosisCodingSystem();
                if (codingSystem == null || codingSystem.isEmpty()) {
                    diagnosis.setDiagnosisCodingSystem("0");
                }
            });
            
            if (value.isEmpty()) {
                return new HashSet<Diagnosis>() {{
                    new Diagnosis();
                }};
            }
            
            return super.marshal(value);
        }
    }

    public final static class MedicalRecordAdapter extends SetAdapter<MedicalRecord> {
        public MedicalRecordAdapter() {
            settClass(MedicalRecord.class);
        }

        @Override
        public Set<MedicalRecord> marshal(final Set<MedicalRecord> value) throws IllegalAccessException, InstantiationException {
            // Filter out deleted records
            final Set<MedicalRecord> filtered = value.stream()
                                                     .filter(record -> record.getDeleted() == null ||
                                                         (record.getDeleted() != null && !record.getDeleted()))
                                                     .collect(Collectors.toSet());

            return super.marshal(filtered);
        }
    }

    public final static class ArchiveAuthorAdapter extends SetAdapter<ArchiveAuthor> {
        public ArchiveAuthorAdapter() {
            this.settClass(ArchiveAuthor.class);
        }

        // @Override
        // public Set<ArchiveAuthor> marshal(final Set<ArchiveAuthor> value) {
        //     if (value.isEmpty()) {
        //         return new HashSet<ArchiveAuthor>() {{ new ArchiveAuthor(); }};
        //     }
        //    
        //     return value;
        // }
    }

    public final static class CaseReferenceAdapter extends SetAdapter<CaseReference> {
        public CaseReferenceAdapter() {
            settClass(CaseReference.class);
        }
    }

    public final static class ContactAdapter extends SetAdapter<Contact> {
        public ContactAdapter() {
            settClass(Contact.class);
        }
    }

    public final static class HospitalAdmissionAdapter extends SetAdapter<HospitalAdmission> {
        public HospitalAdmissionAdapter() {
            settClass(HospitalAdmission.class);
        }
    }

    public final static class InitiativeAdapter extends SetAdapter<Initiative> {
        public InitiativeAdapter() {
            settClass(Initiative.class);
        }
    }

    public final static class ProcedureAdapter extends SetAdapter<Procedure> {
        public ProcedureAdapter() {
            settClass(Procedure.class);
        }
    }

    public final static class ProcedureCodeAdapter extends SetAdapter<ProcedureCode> {
        public ProcedureCodeAdapter() {
            settClass(ProcedureCode.class);
        }
    }

    public final static class ReferencePeriodAdapter extends SetAdapter<ReferencePeriod> {
        public ReferencePeriodAdapter() {
            settClass(ReferencePeriod.class);
        }
    }

    public final static class ServiceAdapter extends SetAdapter<Service> {
        public ServiceAdapter() {
            settClass(Service.class);
        }
    }

}