package no.arkivverket.helsearkiv.nhareg.transfer;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PasientjournalSokeresultatDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ListObject;
import no.arkivverket.helsearkiv.nhareg.medicalrecord.MedicalRecordMapper;

import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TransferService implements TransferServiceInterface {

    @Inject
    private TransferDAO transferDAO;
    
    @Inject
    private MedicalRecordMapper medicalRecordMapper;
    
    @Override
    public Avlevering getAvlevering(final String id) {
        return transferDAO.fetchById(id);
    }

    @Override
    public ListObject getMedicalRecords(final String id, final MultivaluedMap<String, String> queryParameters) {
        final Set<Pasientjournal> medicalRecords = transferDAO.getMedicalRecords(id);
        final String page = queryParameters.getFirst("page");
        final String size = queryParameters.getFirst("size");

        List<PasientjournalSokeresultatDTO> searchResultDTO = medicalRecordMapper.mapToSearchResultDTOList(new ArrayList<>(medicalRecords));
        
        return new ListObject<>(searchResultDTO, searchResultDTO.size(), Integer.parseInt(page), Integer.parseInt(size));
    }
}
