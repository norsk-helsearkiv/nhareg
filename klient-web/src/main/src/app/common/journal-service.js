var mod = angular.module('nha.common.journal-service', [
    'ui.bootstrap'
]);

mod.factory('journalService', ['$modal', '$location', journalService]);

function journalService() {
    var data;

    function setData(newData) {
        data = newData;
    }

    function getData() {
        return data;
    }

    return {
        getData: getData,
        setData: setData
    };
}