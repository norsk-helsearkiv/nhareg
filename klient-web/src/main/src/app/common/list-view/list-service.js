var mod = angular.module('nha.common.list-service', []);

mod.factory('listService', [listService]);

function listService() {
    var tittel;
    var data;
    var avlevering = null;
    var sok = null;

    function init(t, d) {
        tittel = t;
        data = d;
    }

    function setAvlevering(a) {
        avlevering = a;
        sok = null;
    }

    function getAvlevering() {
        return avlevering;
    }

    function setSok(s) {
        sok = s;
        avlevering = null;
    }

    function getTittel() {
        return tittel;
    }

    function getData() {
        return data;
    }

    function getQuery() {
        if (sok !== null && sok !== undefined) {
            return "&sokLagringsenhet=" + (sok.sokLagringsenhet ? sok.sokLagringsenhet : "") +
            "&sokFanearkId=" + (sok.sokFanearkId ? sok.sokFanearkId : "") +
            "&sokFodselsnummer=" + (sok.sokFodselsnummer ? sok.sokFodselsnummer : "") +
            "&sokNavn=" + (sok.sokNavn ? sok.sokNavn : "") +
            "&sokFodt=" + (sok.sokFodt ? sok.sokFodt : "") +
            "&sokOppdatertAv=" + (sok.sokOppdatertAv ? sok.sokOppdatertAv : "") +
            "&sokSistOppdatert=" + (sok.sokSistOppdatert ? sok.sokSistOppdatert : "");
        }

        return '';
    }
    
    function getSize() {
        return size;
    }
    
    function setSize(newSize) {
        size = newSize;
    }

    return {
        init: init,
        setAvlevering: setAvlevering,
        getAvlevering: getAvlevering,
        setSok: setSok,
        getTittel: getTittel,
        getData: getData,
        getQuery: getQuery,
        getSize: getSize,
        setSize: setSize
    };
}