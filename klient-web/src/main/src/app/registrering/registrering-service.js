var mod = angular.module('nha.registrering.registrering-service', [
]);

mod.factory('registreringService', [registreringService]);

function registreringService() {

    var avlevering;
    var pasientjournalDTO;
    var avleveringsidentifikator;

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

    return {
        setAvlevering: setAvlevering,
        getAvlevering: getAvlevering,

        setPasientjournalDTO: setPasientjournalDTO,
        getPasientjournalDTO: getPasientjournalDTO,

        setAvleveringsidentifikator : setAvleveringsidentifikator,
        getAvleveringsidentifikator : getAvleveringsidentifikator
    };

}