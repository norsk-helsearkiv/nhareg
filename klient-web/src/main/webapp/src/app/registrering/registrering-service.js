var mod = angular.module('nha.registrering.registrering-service', [
]);

mod.factory('registreringService', [registreringService]);

function registreringService() {

    var avlevering;
    var pasientjournalDTO;

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

    return {
        setAvlevering: setAvlevering,
        getAvlevering: getAvlevering,

        setPasientjournalDTO: setPasientjournalDTO,
        getPasientjournalDTO: getPasientjournalDTO
    };

}