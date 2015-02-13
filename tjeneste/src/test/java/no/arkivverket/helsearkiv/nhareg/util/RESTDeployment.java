package no.arkivverket.helsearkiv.nhareg.util;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avtale;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PersondataDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ListeObjekt;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.DagEllerAar;
import no.arkivverket.helsearkiv.nhareg.exception.IllegalArgumentExceptionMapper;
import no.arkivverket.helsearkiv.nhareg.tjeneste.EntitetsTjeneste;
import no.arkivverket.helsearkiv.nhareg.transformer.DatoEllerAarTilStringTransformer;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.collection.AbstractCollectionDecorator;
import org.apache.commons.collections4.iterators.AbstractUntypedIteratorDecorator;
import org.apache.commons.collections4.keyvalue.AbstractMapEntryDecorator;
import org.apache.commons.collections4.list.PredicatedList;
import org.apache.commons.collections4.map.AbstractSortedMapDecorator;
import org.apache.commons.collections4.sequence.CommandVisitor;
import org.apache.commons.collections4.set.AbstractSetDecorator;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class RESTDeployment {

    public static WebArchive deployment() {

        return NharegDeployment.deployment()
                //model
                .addPackage(Avtale.class.getPackage())
                .addPackage(ListeObjekt.class.getPackage())
                .addPackage(PersondataDTO.class.getPackage())
                .addPackage(DagEllerAar.class.getPackage())
                // exception
                .addPackage(IllegalArgumentExceptionMapper.class.getPackage())
                // tjeneste
                .addPackage(EntitetsTjeneste.class.getPackage())
                //transformer
                .addPackage(DatoEllerAarTilStringTransformer.class.getPackage())
                //util
                .addPackage(DatoValiderer.class.getPackage())
                //
                // Denne blokken er med for å få med commons-collections4 i testene
                //
                .addPackage(Transformer.class.getPackage())
                .addPackage(AbstractMapEntryDecorator.class.getPackage())
                .addPackage(AbstractUntypedIteratorDecorator.class.getPackage())
                .addPackage(AbstractSetDecorator.class.getPackage())
                .addPackage(AbstractCollectionDecorator.class.getPackage())
                .addPackage(CommandVisitor.class.getPackage())
                .addPackage(AbstractSortedMapDecorator.class.getPackage())
                .addPackage(PredicatedList.class.getPackage());

//                .addClass(AllocatedSeats.class)
//                .addClass(MediaPath.class)
//                .addClass(MediaManager.class);
    }

}
