var mod = angular.module('nha.common.list-service', []);

mod.factory('listService', [listService]);

function listService() {
    var title;
    var subtitle;
    var data;
    var transfer = null;
    var search = null;
    var size = 15;
    var clean = false;

    function setTransfer(value) {
        transfer = value;
        search = null;
    }

    function getTransfer() {
        return transfer;
    }

    function setSearch(value) {
        search = value;
        transfer = null;
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
        if (search !== null && search !== undefined) {
            return "&lagringsenhet=" + (search.sokLagringsenhet ? search.sokLagringsenhet : "") +
              "&fanearkid=" + (search.sokFanearkId ? search.sokFanearkId : "") +
              "&fodselsnummer=" + (search.sokFodselsnummer ? search.sokFodselsnummer : "") +
              "&navn=" + (search.sokNavn ? search.sokNavn : "") +
              "&fodt=" + (search.sokFodt ? search.sokFodt : "") +
              "&oppdatertAv=" + (search.sokOppdatertAv ? search.sokOppdatertAv : "") +
              "&sistOppdatert=" + (search.sokSistOppdatert ? search.sokSistOppdatert : "") + 
              "&transferId=" + (transfer ? transfer : "");
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
        setTransfer: setTransfer,
        getTransfer: getTransfer,
        setSok: setSearch,
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