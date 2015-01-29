var mod = angular.module('nha.registrering.registrering-service', [
]);

mod.factory('registreringService', [registreringService]);

function registreringService() {

    var avlevering;

    function setAvlevering(a) {
        avlevering = a;
    }

    function getAvlevering() {
        return avlevering;
    }

    return {
        setAvlevering: setAvlevering,
        getAvlevering: getAvlevering
    };

}