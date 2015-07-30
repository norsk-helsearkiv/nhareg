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
            if(!diagnoser[data[i].code]) {
              diagnoser[data[i].code] = [];
            }

            var diag = {
                selected : false,
                displayName:data[i].displayName,
                codeSystem:data[i].codeSystem,
                codeSystemVersion:data[i].codeSystemVersion
            };

              diagnoser[data[i].code].push(diag);
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
