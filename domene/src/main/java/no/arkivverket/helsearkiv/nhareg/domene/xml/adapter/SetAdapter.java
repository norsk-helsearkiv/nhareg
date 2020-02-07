package no.arkivverket.helsearkiv.nhareg.domene.xml.adapter;

import no.arkivverket.helsearkiv.nhareg.domene.additionalinfo.*;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Set;

/**
 * Generic adapter which initializes a new class, using .newInstance(), and adds it to the set if it is empty.
 * Contains static inner classes to initialize the class type.
 * All methods and inner classes are marked final to prevent overriding functionality.
 * This class reduces duplicate code for marshalling empty sets.
 * @param <T> Class type the set consists of.
 */
public abstract class SetAdapter<T> extends XmlAdapter<Set<T>, Set<T>> {

    private Class<T> tClass;

    public final void settClass(final Class<T> tClass) {
        this.tClass = tClass;
    }

    @Override
    public final Set<T> unmarshal(final Set<T> ignored) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final Set<T> marshal(final Set<T> value) throws IllegalAccessException, InstantiationException {
        if (value.isEmpty()) {
            value.add(tClass.newInstance());
        }

        return value;
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