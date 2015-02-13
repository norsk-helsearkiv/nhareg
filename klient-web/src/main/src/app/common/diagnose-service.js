var mod = angular.module('nha.common.diagnose-service', [
    'nha.common.http-service',
    'nha.common.error-service'
]);

mod.factory('diagnoseService', ['httpService', 'errorService', diagnoseService]);

function diagnoseService(httpService, errorService) {

    var diagnoser;

    function getDiagnoser() {
        if(diagnoser) {
            return diagnoser;
        }
        diagnoser = [];

        httpService.hentAlle("diagnosekoder", false)
        .success(function(data, status, headers, config) {
          for(var i = 0; i < data.length; i++) {
            diagnoser[data[i].code] = data[i].displayName;
          }
        }).error(function(data, status, headers, config) {
          errorService.errorCode(status);
        });

        return diagnoser;
    }

    return {
        getDiagnoser: getDiagnoser
    };

}
