angular.module('nha.home')

    .controller('PatientSearchCtrl', function($rootScope, $scope, $location, $filter, httpService, errorService, listService, modalService, registerService, stateService) {
        //$scope.sok = stateService.sokState;
        $scope.sok = {};

        $scope.actionRensSok = function () {
            $scope.sok.lagringsenhet = '';
            $scope.sok.fanearkId = '';
            $scope.sok.fodselsnummer = '';
            $scope.sok.navn = '';
            $scope.sok.fodt = '';
            $scope.sok.oppdatertAv = '';
            $scope.sok.sistOppdatert = '';
        };

        $scope.actionSok = function () {
            var sokeresultat = $scope.text.sokeresultat;
            var viser = $scope.text.viser;

            registerService.setAvlevering(undefined);
            
            var sok = {
                sokLagringsenhet: $scope.sok.lagringsenhet,
                sokFanearkId: $scope.sok.fanearkId,
                sokFodselsnummer: $scope.sok.fodselsnummer,
                sokNavn: $scope.sok.navn,
                sokFodt: $scope.sok.fodt,
                sokOppdatertAv: $scope.sok.oppdatertAv,
                sokSistOppdatert: $scope.sok.sistOppdatert
            };

            listService.setSok(sok);
            stateService.sokState = $scope.sok;

            httpService.getAll("pasientjournaler?page=1&size=" + $scope.size + listService.getQuery())
                .success(function (data, status, headers, config) {
                    var tittel = {
                        "tittel": sokeresultat,
                        "underTittel": viser + " " + data.size + " / " + data.total + " " + sokeresultat.toLowerCase()
                    };

                    listService.init(tittel, data);
                    listService.setSok(sok);

                    $location.path('/list');
                }).error(function (data, status, headers, config) {
                    errorService.errorCode(status);
            });
        };
    });