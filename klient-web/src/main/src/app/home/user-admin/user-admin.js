angular.module('nha.home')

    .controller('UserAdminCtrl', function($rootScope, $scope, $location, $filter, httpService, errorService){

        $scope.bruker = {};
        $scope.bruker.printerzpl = '127.0.0.1';
        $scope.brukere = [];

        $scope.selectedBrukerRow = null;  // initialize our variable to null

        $scope.velgBruker = function (valgtBruker, index) {
            if (index === $scope.selectedBrukerRow) {
                $scope.selectedBrukerRow = null;
                $scope.bruker.brukernavn = null;
                $scope.bruker.rolle = null;
                $scope.bruker.printerzpl = null;
            } else {
                var rolleIndex = $scope.roller.map(function (e) {
                    return e.navn;
                }).indexOf(valgtBruker.rolle.navn);
                $scope.selectedBrukerRow = index;
                $scope.bruker.brukernavn = valgtBruker.brukernavn;
                $scope.bruker.printerzpl = valgtBruker.printerzpl;
                $scope.bruker.rolle = $scope.roller[rolleIndex];
            }
        };

        $scope.hentBrukere = function () {

            httpService.getAll("admin/brukere", false)
                .success(function (data, status, headers, config) {
                    $scope.brukere = data;
                }).error(function (data, status, headers, config) {
                errorService.errorCode(status);
            });
        };

        var resetBruker = function () {
            $scope.bruker = {};
        };

        var sjekkPassord = function () {
            return $scope.bruker.password === $scope.bruker.passwordConfirm;
        };

        $scope.oppdaterBruker = function () {
            $scope.error = [];
            if (!sjekkPassord()) {
                $scope.error['passord'] = $filter('translate')('home.brukere.PASSORD_ULIKT');
                return;
            }

            httpService.create("admin/brukere", $scope.bruker)
                .success(function (data, status, headers, config) {
                    $scope.hentBrukere();
                    resetBruker();

                }).error(function (data, status, headers, config) {
                if (status != 400) {
                    errorService.errorCode(status);
                    return;
                } else {
                    if (data[0].attributt === 'passord') {
                        $scope.error['passord'] = $filter('translate')('home.brukere.PASSORD_FEIL');
                    }
                }
            });
        };

        $scope.error = [];

        $scope.checkError = function (attributt) {
            var err = $scope.error[attributt] !== undefined;
            return err;
        };

        var setFeilmeldinger = function (data, status) {

            angular.forEach(data, function (element) {
                $scope.error[element.attributt] = element.constriant;
            });
        };

    });