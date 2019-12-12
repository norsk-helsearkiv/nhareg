var mod = angular.module('nha.common.list-service', []);

mod.factory('listService', [listService]);

function listService() {
    var tittel;
    var data;
    var avlevering = null;
    var sok = null;
    var size = 15;

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
            return "&lagringsenhet=" + (sok.sokLagringsenhet ? sok.sokLagringsenhet : "") +
              "&fanearkid=" + (sok.sokFanearkId ? sok.sokFanearkId : "") +
              "&fodselsnummer=" + (sok.sokFodselsnummer ? sok.sokFodselsnummer : "") +
              "&navn=" + (sok.sokNavn ? sok.sokNavn : "") +
              "&fodt=" + (sok.sokFodt ? sok.sokFodt : "") +
              "&oppdatertAv=" + (sok.sokOppdatertAv ? sok.sokOppdatertAv : "") +
              "&sistOppdatert=" + (sok.sokSistOppdatert ? sok.sokSistOppdatert : "") + 
              "&transferId=" + (avlevering ? avlevering : "");
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