var mod = angular.module('nha.common.diagnosis-service', [
    'nha.common.http-service'
]);

mod.factory('diagnosisService', ['httpService', diagnosisService]);

function diagnosisService(httpService) {

    function getDiagnosisServer(date, diagnosisCode, callback) {

        httpService.getAll("diagnosekoder?code=" + diagnosisCode + "&diagnoseDate=" + (date || ''), false)
            .success(function (data) {
                callback(extractMap(data));
            });
    }

    function extractMap(data) {
        var diagnosisMap = {};

        for (var i = 0; i < data.length; i++) {

            if (!diagnosisMap[data[i].code]) {
                diagnosisMap[data[i].code] = [];
            }

            var diagnosis = {
                selected: false,
                displayName: data[i].displayName,
                codeSystem: data[i].codeSystem,
                codeSystemVersion: data[i].codeSystemVersion
            };

            diagnosisMap[data[i].code].push(diagnosis);
        }
        return diagnosisMap;
    }

    return {
        getDiagnosisServer: getDiagnosisServer
    };

}
