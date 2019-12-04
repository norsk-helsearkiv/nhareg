package no.arkivverket.helsearkiv.nhareg.utilities;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.collection.AbstractCollectionDecorator;
import org.apache.commons.collections4.iterators.AbstractUntypedIteratorDecorator;
import org.apache.commons.collections4.keyvalue.AbstractMapEntryDecorator;
import org.apache.commons.collections4.list.PredicatedList;
import org.apache.commons.collections4.map.AbstractSortedMapDecorator;
import org.apache.commons.collections4.sequence.CommandVisitor;
import org.apache.commons.collections4.set.AbstractSetDecorator;
import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import no.arkivverket.helsearkiv.nhareg.auth.UserService;
import no.arkivverket.helsearkiv.nhareg.domene.auth.Bruker;
import no.arkivverket.helsearkiv.nhareg.domene.auth.dto.BrukerDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avtale;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.DatoEllerAar;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PasientjournalDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PersondataDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ListObject;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValideringsfeilException;
import no.arkivverket.helsearkiv.nhareg.domene.felles.GyldigeDatoformater;
import no.arkivverket.helsearkiv.nhareg.domene.konfig.Konfigparam;
import no.arkivverket.helsearkiv.nhareg.exception.IllegalArgumentExceptionMapper;
import no.arkivverket.helsearkiv.nhareg.tjeneste.EntitetsTjeneste;
import no.arkivverket.helsearkiv.nhareg.tjeneste.KonfigparamTjeneste;
import no.arkivverket.helsearkiv.nhareg.transformer.DatoEllerAarTilStringTransformer;
import no.arkivverket.helsearkiv.nhareg.util.DatoValiderer;

public class RESTDeployment {

    public static WebArchive deployment() {
        return NharegDeployment.deployment()
            //model
            .addPackage(Avtale.class.getPackage())
            .addPackage(ListObject.class.getPackage())
            .addPackage(PersondataDTO.class.getPackage())
            .addPackage(DatoEllerAar.class.getPackage())
            .addPackage(GyldigeDatoformater.class.getPackage())
            .addPackage(Konfigparam.class.getPackage())
            .addPackage(KonfigparamTjeneste.class.getPackage())
            .addPackage(ValideringsfeilException.class.getPackage())
            .addPackage(UserService.class.getPackage())
            .addPackage(Bruker.class.getPackage())
            .addPackage(BrukerDTO.class.getPackage())
            .addPackage(PasientjournalDTO.class.getPackage())
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
            .addPackage(PredicatedList.class.getPackage())
            //
            // Blokk for StringUtils og relaterte pakker
            //
            .addPackage(StringUtils.class.getPackage())
            .addPackage(CharSequenceUtils.class.getPackage())
            .addPackage(CharSequenceTranslator.class.getPackage())
            .addPackage(ToStringStyle.class.getPackage());
    }
}
