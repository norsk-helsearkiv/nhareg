angular.module('nha.home', [
        'ui.router',
        'nha.common.http-service',
        'nha.common.error-service',
        'nha.common.list-service',
        'nha.common.modal-service',
        'nha.registrering.registrering-service',
        'nha.common.diagnose-service'
    ])

    .config(function config($stateProvider) {
        $stateProvider.state('home', {
            url: '/',
            views: {
                "main": {
                    controller: 'HomeCtrl',
                    templateUrl: 'home/home.tpl.html'
                }
            }
        });
        $stateProvider.state('homeLagringsenheter', {
            url: '/lagringsenheter',
            views: {
                "main": {
                    controller: 'HomeCtrl',
                    templateUrl: 'home/home.tpl.html'
                }
            }
        });

    })

    .controller('HomeCtrl', function HomeController($rootScope, $scope, $location, $filter, httpService, errorService, listService, modalService, registreringService, diagnoseService, stateService, $modal, $window) {


        $scope.$on('$stateChangeSuccess', function() {
            $scope.sokVisible = false;
            $scope.lagringsenheterVisible = false;
            var path = $location.path();

            if(path === '/') {
                $scope.sokVisible = true;
            } else if(path === '/lagringsenheter') {
                $scope.lagringsenheterVisible = true;
            }
        });


        httpService.brukerRolle().success(function (data, status, headers, config) {
            $rootScope.userrole = data;

        }).error(function () {


        });


        var antall = 15;
        //Henter ned diagnosene, dette tar litt tid så gjøres ved oppstart, en gang.
        diagnoseService.getDiagnoser();

        //Tekster i vinduet lastet fra kontroller
        $scope.text = {
            "tooltip": {}
        };
        $scope.$watch(
            function () {
                return $filter('translate')('konfig.ANTALL');
            },
            function (newval) {
                antall = Number(newval);
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('home.PASIENTSOK');
            },
            function (newval) {
                $scope.text.pasientsok = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('home.AVLEVERING');
            },
            function (newval) {
                $scope.text.avlevering = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('home.AVTALE');
            },
            function (newval) {
                $scope.text.avtale = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('home.SOKERESULTAT');
            },
            function (newval) {
                $scope.text.sokeresultat = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('home.VISER');
            },
            function (newval) {
                $scope.text.viser = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('home.tooltip.LIST');
            },
            function (newval) {
                $scope.text.tooltip.list = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('home.tooltip.ADD');
            },
            function (newval) {
                $scope.text.tooltip.add = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('home.tooltip.FOLDER');
            },
            function (newval) {
                $scope.text.tooltip.folder = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('home.tooltip.ENDRE');
            },
            function (newval) {
                $scope.text.tooltip.endre = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('home.tooltip.DELETE');
            },
            function (newval) {
                $scope.text.tooltip.deleteElement = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('home.tooltip.LAAS');
            },
            function (newval) {
                $scope.text.tooltip.laas = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('home.tooltip.LAAST');
            },
            function (newval) {
                $scope.text.tooltip.laast = newval;
            }
        );

        $scope.sok = stateService.sokState;

        httpService.hentAlle("avtaler", false)
            .success(function (data, status, headers, config) {
                $scope.avtaler = data;
                $scope.setValgtAvtale(data[0]);
            }).error(function (data, status, headers, config) {
            errorService.errorCode(status);
        });
        httpService.hent("avtaler/virksomhet", false)
            .success(function (data, status, headers, config) {
                $scope.virksomhet = data;
            }).error(function (data, status, headers, config) {
            errorService.errorCode(status);
        });

        $scope.sok = {};
        $scope.actionSok = function (sokestring) {
            var txt = $scope.text.sokeresultat;
            var viser = $scope.text.viser;
            registreringService.setAvlevering(undefined);
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
            stateService.sokState=$scope.sok;

            httpService.hentAlle("pasientjournaler?side=1&antall=" + antall + listService.getQuery())
                .success(function (data, status, headers, config) {

                    var tittel = {
                        "tittel": txt,
                        "underTittel": viser + " " + data.antall + " / " + data.total + " " + txt.toLowerCase()
                    };
                    listService.init(tittel, data);
                    listService.setSok(sok);
                    $location.path('/list');


                }).error(function (data, status, headers, config) {
                errorService.errorCode(status);
            });
        };

        //Avtale
        $scope.setValgtAvtale = function (avtale) {
            if (avtale === undefined) {
                return;
            }
            httpService.hentAlle("avtaler/" + avtale.avtaleidentifikator + "/avleveringer", false)
                .success(function (data, status, headers, config) {
                    $scope.avleveringer = data;
                    $scope.valgtAvtale = avtale;
                }).error(function (data, status, headers, config) {
                errorService.errorCode(status);
            });
        };

        $scope.actionDeleteAvtale = function (elementType, id, element) {
            modalService.deleteModal(elementType, id, function () {
                httpService.deleteElement("avtaler/" + id)
                    .success(function (data, status, headers, config) {
                        fjern($scope.avtaler, element);
                        $scope.setValgtAvtale($scope.avtaler[0]);
                    }).error(function (data, status, headers, config) {
                    errorService.errorCode(status);
                });
            });
        };

        var validerAvtale = function (formData) {
            formData.error = {};
            var success = true;

            if (formData.avtaleidentifikator === undefined || formData.avtaleidentifikator === '') {
                formData.error.avtaleidentifikator = "ID kan ikke være tom";
                success = false;
            }
            if (formData.avtalebeskrivelse === undefined || formData.avtalebeskrivelse === '') {
                formData.error.avtalebeskrivelse = "Beskrivelsen kan ikke være tom";
                success = false;
            }
            if (formData.avtaledato === undefined || formData.avtaledato === '') {
                formData.error.avtaledato = "Dato må være satt";
                success = false;
            }

            if (success) {
                formData.error = undefined;
            }
            return success;
        };

        $scope.actionLeggTilAvtale = function () {
            modalService.nyModal('common/modal-service/ny-avtale.tpl.html', $scope.avtaler, "avtaler", validerAvtale);
        };

        $scope.actionEndreAvtale = function (avtale) {
            modalService.endreModal('common/modal-service/ny-avtale.tpl.html', $scope.avtaler, "avtaler", validerAvtale, avtale);
        };

        //Avlevering
        var validering = function (formData) {
            formData.error = {};
            var success = true;

            if (formData.avleveringsidentifikator === undefined || formData.avleveringsidentifikator === '') {
                formData.error.avleveringsidentifikator = "ID kan ikke være tom";
                success = false;
            }

            if (success) {
                formData.error = undefined;
                formData.avtale = $scope.valgtAvtale;
            }
            return success;

        };
        $scope.actionLeggTilAvlevering = function () {
            modalService.nyModal('common/modal-service/ny-avlevering.tpl.html', $scope.avleveringer, "avleveringer", validering);
        };

        $scope.actionEndreAvlevering = function (avlevering) {
            modalService.endreModal('common/modal-service/ny-avlevering.tpl.html', $scope.avleveringer, "avleveringer", validering, avlevering);
        };

        $scope.actionFjernAvlevering = function (elementType, id, element) {
            modalService.deleteModal(elementType, id, function () {
                httpService.deleteElement("avleveringer/" + id)
                    .success(function (data, status, headers, config) {
                        fjern($scope.avleveringer, element);
                    }).error(function (data, status, headers, config) {
                    errorService.errorCode(status);
                });
            });
        };

        $scope.actionVisAvlevering = function (avlevering) {
            registreringService.setAvleveringsbeskrivelse(avlevering.avleveringsbeskrivelse);
            registreringService.setAvlevering(undefined);
            httpService.hentAlle("pasientjournaler?side=1&antall=" + antall + "&avlevering=" + avlevering.avleveringsidentifikator)
                .success(function (data, status, headers, config) {

                    var tittel = {
                        "tittel": avlevering.avleveringsbeskrivelse,
                        "underTittel": avlevering.arkivskaper
                    };
                    listService.init(tittel, data);
                    listService.setAvlevering(avlevering.avleveringsidentifikator);
                    $location.path('/list');

                }).error(function (data, status, headers, config) {
                errorService.errorCode(status);
            });
        };

        $scope.actionAvleveringLeveranse = function (avlevering) {
            window.location = httpService.getRoot() + "avleveringer/" + avlevering.avleveringsidentifikator + "/leveranse";
        };

        //Util
        $scope.loggUt = function () {
            httpService.logout()
                .success(function (status, headers, config) {
                    $window.location.reload();

                }).error(function () {
                //alert('logout error');
                $window.location.reload();
            });
        };

        $scope.actionLeggTilPasientjournald = function (avlevering) {
            registreringService.setAvlevering(avlevering);
            registreringService.setVirksomhet($scope.virksomhet.foretaksnavn);
            registreringService.setValgtAvtale($scope.valgtAvtale.avtalebeskrivelse);
            registreringService.setAvleveringsidentifikator(avlevering.avleveringsidentifikator);
            registreringService.setAvleveringsbeskrivelse(avlevering.avleveringsbeskrivelse);
            $location.path('/registrer');
        };
        $scope.actionLaasAvlevering = function (avlevering) {
            var tpl = 'common/modal-service/warning-modal.tpl.html';
            var url = "avleveringer/" + avlevering.avleveringsidentifikator + "/laas";
            var id = avlevering.avleveringsidentifikator;
            var tittel = $filter('translate')('modal.warning_laas.TITTEL');
            var beskrivelse = $filter('translate')('modal.warning_laas.BESKRIVELSE');
            modalService.warningModal(tpl, url, id, tittel, beskrivelse, function () {
                $scope.setValgtAvtale($scope.valgtAvtale);
            });
        };
        $scope.actionLaasOppAvlevering = function (avlevering) {
            var tpl = 'common/modal-service/warning-modal.tpl.html';
            var url = "avleveringer/" + avlevering.avleveringsidentifikator + "/laasOpp";
            var id = avlevering.avleveringsidentifikator;
            var tittel = $filter('translate')('modal.warning_laas_opp.TITTEL');
            modalService.warningModal(tpl, url, id, tittel, null, function () {
                $scope.setValgtAvtale($scope.valgtAvtale);
            });
        };
        //Hjelpe metode for å fjerne fra liste
        var fjern = function (list, element) {
            for (var i = 0; i < list.length; i++) {
                if (element === list[i]) {
                    list.splice(i, 1);
                }
            }
        };
        $scope.lagringsenheter = [];
        $scope.lagrSok = {};
        $scope.lagrPasientjournaler = [];

        $scope.lagrActionSok = function(){

            httpService.hentAlle("lagringsenheter/sok?identifikatorSok="+$scope.lagrSok.lagringsenhet, false)
                .success(function (data, status, headers, config) {
                    $scope.lagringsenheter = data;
                }).error(function (data, status, headers, config) {
                    errorService.errorCode(status);
            });
        };

        $scope.selectedRow = null;  // initialize our variable to null
        $scope.setClickedRow = function(index){  //function that sets the value of selectedRow to current index
            if (index === $scope.selectedRow){
                $scope.selectedRow = null;
            }else{
                $scope.selectedRow = index;
            }
        };
        
        $scope.lagrActionEndreLagringsenhet = function(lagringsenhet){
            var modal = modalService.endreLagringsenhet('common/modal-service/endre-lagringsenhet-modal.tpl.html',
                'lagringsenheter/',
                lagringsenhet);
            modal.result.then(function () {
                //TODO
            });
        };
        $scope.lagrActionHentPasientjournaler = function(){
            if ($scope.selectedRow>-1){
                var valgtLagringsenhet = $scope.lagringsenheter[$scope.selectedRow];
                httpService.hentAlle("lagringsenheter/"+valgtLagringsenhet.identifikator+"/pasientjournaler", false)
                    .success(function (data, status, headers, config) {
                        $scope.lagrPasientjournaler = data;
                    }).error(function (data, status, headers, config) {
                        errorService.errorCode(status);
                });
            }
        };

        $scope.lagrSelection = {
            allSelected : false
        };
        $scope.lagrToggleAll = function() {
            var toggleStatus = !$scope.lagrSelection.allSelected;
            angular.forEach($scope.lagrPasientjournaler, function(itm){ itm.selected = toggleStatus; });
        };
        $scope.lagrOptionToggled = function(){
            $scope.lagrSelection.allSelected = $scope.lagrPasientjournaler.every(function(itm){ return itm.selected; });
        };


        $scope.lagrFlytt = {
            lagringsenhet : ""
        };

        $scope.lagrActionFlytt = function(){
            var selectedPasientjournaler = [];
            angular.forEach($scope.lagrPasientjournaler,function(pasientjournal){
                if (pasientjournal.selected){
                    selectedPasientjournaler.push(pasientjournal.uuid);
                }
            });

            //TODO warning
            var tpl = 'common/modal-service/warning-modal.tpl.html';
            var url = "lagringsenheter/flytt";
            var identifikator = $scope.lagrFlytt.lagringsenhet;
            var tittel = $filter('translate')('modal.warning_flytt.TITTEL');
            var beskrivelse = $filter('translate')('modal.warning_flytt.BESKRIVELSE');
            modalService.warningFlyttLagringsenheter(tpl, url, '', tittel, beskrivelse,
                function (removedUuids) {
                    $scope.lagrActionHentPasientjournaler();
                },
                selectedPasientjournaler,
                identifikator
            );

        };

    });