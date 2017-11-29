var mod = angular.module('nha.common.diagnose-service', [
    'nha.common.http-service',
    'nha.common.error-service'
]);

mod.factory('diagnoseService', ['httpService', 'errorService', diagnoseService]);

function diagnoseService(httpService, errorService) {
    
    function getDiagnoserServer(dato, diagnosekode, callback) {

        httpService.hentAlle("diagnosekoder?code=" + diagnosekode + "&diagnoseDate=" + (dato || ''), false)
            .success(function (data) {
                callback(extractMap(data));
            });
    }

    function extractMap(data) {
        var diagMap = {};
        for (var i = 0; i < data.length; i++) {
            if (!diagMap[data[i].code]) {
                diagMap[data[i].code] = [];
            }

            var diag = {
                selected: false,
                displayName: data[i].displayName,
                codeSystem: data[i].codeSystem,
                codeSystemVersion: data[i].codeSystemVersion
            };
            diagMap[data[i].code].push(diag);
        }
        return diagMap;
    }

    return {
        getDiagnoserServer: getDiagnoserServer
    };

}
