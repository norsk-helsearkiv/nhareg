var mod = angular.module('nha.registrering.registrering-service', [
]);

mod.factory('registreringService', [registreringService]);

function registreringService() {

    var avlevering;
    var pasientjournalDTO;
    var avleveringsidentifikator;
    var valgtAvtale;
    var virksomhet;
    var avleveringsbeskrivelse;

    function setPasientjournalDTO(data) {
        pasientjournalDTO = data;
    }

    function getPasientjournalDTO() {
        return pasientjournalDTO;
    }

    function setAvlevering(a) {
        avlevering = a;
    }

    function getAvlevering() {
        return avlevering;
    }

    function getAvleveringsidentifikator(){
        return avleveringsidentifikator;
    }
    function setAvleveringsidentifikator(ident){
        avleveringsidentifikator = ident;
    }
    function setValgtAvtale(avtale){
        valgtAvtale = avtale;
    }
    function getValgtAvtale(){
        return valgtAvtale;
    }
    function setVirksomhet(v){
        virksomhet = v;
    }
    function getVirksomhet(){
        return virksomhet;
    }
    function setAvleveringsbeskrivelse(beskrivelse){
        avleveringsbeskrivelse = beskrivelse;
    }

    function getAvleveringsbeskrivelse(){
        return avleveringsbeskrivelse;
    }

    return {
        setAvlevering: setAvlevering,
        getAvlevering: getAvlevering,

        setPasientjournalDTO: setPasientjournalDTO,
        getPasientjournalDTO: getPasientjournalDTO,

        setAvleveringsidentifikator : setAvleveringsidentifikator,
        getAvleveringsidentifikator : getAvleveringsidentifikator,

        getValgtAvtale : getValgtAvtale,
        setValgtAvtale : setValgtAvtale,

        getVirksomhet : getVirksomhet,
        setVirksomhet : setVirksomhet,

        setAvleveringsbeskrivelse : setAvleveringsbeskrivelse,
        getAvleveringsbeskrivelse : getAvleveringsbeskrivelse

};

}