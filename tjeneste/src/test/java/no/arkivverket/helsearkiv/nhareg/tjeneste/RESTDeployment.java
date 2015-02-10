package no.arkivverket.helsearkiv.nhareg.tjeneste;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avtale;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PersondataDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ListeObjekt;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.DagEllerAar;
import no.arkivverket.helsearkiv.nhareg.util.DiagnoseDTOTransformer;
import no.arkivverket.helsearkiv.nhareg.util.Konverterer;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.collection.AbstractCollectionDecorator;
import org.apache.commons.collections4.iterators.AbstractUntypedIteratorDecorator;
import org.apache.commons.collections4.keyvalue.AbstractMapEntryDecorator;
import org.apache.commons.collections4.map.AbstractSortedMapDecorator;
import org.apache.commons.collections4.sequence.CommandVisitor;
import org.apache.commons.collections4.set.AbstractSetDecorator;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class RESTDeployment {

    public static WebArchive deployment() {

        return NharegDeployment.deployment()
                .addPackage(Avtale.class.getPackage())
                .addPackage(EntitetsTjeneste.class.getPackage())
                .addPackage(ListeObjekt.class.getPackage())
                .addPackage(PersondataDTO.class.getPackage())
                .addPackage(Konverterer.class.getPackage())
                .addPackage(DagEllerAar.class.getPackage())
                .addPackage(DiagnoseDTOTransformer.class.getPackage())
                //
                // Denne blokken er med for å få med commons-collections4 i testene
                //
                .addPackage(Transformer.class.getPackage())
                .addPackage(AbstractMapEntryDecorator.class.getPackage())
                .addPackage(AbstractUntypedIteratorDecorator.class.getPackage())
                .addPackage(AbstractSetDecorator.class.getPackage())
                .addPackage(AbstractCollectionDecorator.class.getPackage())
                .addPackage(CommandVisitor.class.getPackage())
                .addPackage(AbstractSortedMapDecorator.class.getPackage());
        
//                .addClass(AllocatedSeats.class)
//                .addClass(MediaPath.class)
//                .addClass(MediaManager.class);
    }

}
