var mod = angular.module('nha.common.list-service', []);

mod.factory('listService', [listService]);

function listService() {
    var title;
    var subtitle;
    var data;
    var avlevering = null;
    var sok = null;
    var size = 15;
    var clean = false;

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

    function setTitle(value) {
        title = value;
    }
    
    function getTitle() {
        return title;
    }
    
    function setSubtitle(value) {
        subtitle = value;
    }
    
    function getSubtitle() {
        return subtitle;
    }
    
    function setData(value) {
        data = value;
    }

    function getData() {
        return data;
    }

    function createSubtitle(data) {
        var max = (data.page * data.size);
        if (max > data.total) {
            max = data.total;
        }
        
        return ((data.page - 1) * data.size) + 1 + "..." + max + " / " + data.total;
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

    function setSize(value) {
        size = value;
    }

    function setClean(value) {
        clean = value;
    }
    
    function getClean() {
        return clean;
    }
    
    return {
        setAvlevering: setAvlevering,
        getAvlevering: getAvlevering,
        setSok: setSok,
        setTitle: setTitle,
        getTitle: getTitle,
        setSubtitle: setSubtitle,
        getSubtitle: getSubtitle,
        setData: setData,
        getData: getData,
        getQuery: getQuery,
        getSize: getSize,
        setSize: setSize,
        createSubtitle: createSubtitle,
        setClean: setClean,
        getClean: getClean
    };
}