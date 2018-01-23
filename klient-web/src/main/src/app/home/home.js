angular.module('nha.home', [
        'ui.router',
        'nha.common.http-service',
        'nha.common.error-service',
        'nha.common.list-service',
        'nha.common.modal-service',
        'nha.registrering.registrering-service'
    ])
    .filter('parseCustomDate', function(){
        return function(date){
            var d = new Date(date);
            return d.getTime();
        };
    })
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
        $stateProvider.state('homeBrukere', {
            url: '/brukere',
            views: {
                "main": {
                    controller: 'HomeCtrl',
                    templateUrl: 'home/home.tpl.html'
                }
            }
        });

    })

    .controller('HomeCtrl', function HomeController($rootScope, $scope, $location, $filter, httpService, errorService, listService, modalService, registreringService, stateService, $modal, $window, $cookies) {


        $scope.$on('$stateChangeSuccess', function () {
            $scope.sokVisible = false;
            $scope.lagringsenheterVisible = false;
            $scope.brukereVisible = false;
            var path = $location.path();

            if (path === '/') {
                $scope.sokVisible = true;
            } else if (path === '/lagringsenheter') {
                $scope.lagringsenheterVisible = true;

            } else if (path === '/brukere') {
                $scope.brukereVisible = true;
            }
        });


        httpService.brukerRolle().success(function (data) {
            $rootScope.userrole = data;

        }).error(function (status) {
            $scope.loggUt();
        });

        httpService.brukerNavn().success(function (data) {
            $rootScope.username = data;

        }).error(function (status) {
        });

        httpService.hentAlle("admin/roller", false).success(function (data) {
            $scope.roller = data;
        }).error(function (status) {
        });
        $scope.endrePassord = function () {
            var modal = modalService.endrePassord();
            modal.result.then(function () {
                //TODO
            });
        };
        httpService.hent("admin/resetPassord", false).success(function (data) {
            if (data === 'true') {
                $scope.endrePassord();
            }
        });


        var antall = 15;

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
                return $filter('translate')('home.tooltip.FAVORITE');
            },
            function (newval) {
                $scope.text.tooltip.favorite = newval;
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

        $scope.defaultAvlevering = null;
        
        httpService.hentAlle("avtaler", false)
            .success(function (data, status, headers, config) {
                $scope.avtaler = data;
                httpService.hent("avtaler/default", false)
                    .success(function(avtaleIdent, status){
                        if (avtaleIdent){
                            $scope.defaultAvlevering = avtaleIdent;
                            //TODO må testes litt....
                            for (var i = 0; i < data.length; i++) {
                                if (data[i].avtaleidentifikator === avtaleIdent){
                                    $scope.setValgtAvtale(data[i]);
                                    break;
                                }
                            }
                        }else{
                            $scope.setValgtAvtale(data[0]);
                        }
                }).error(function (data, status, headers, config) {
                    errorService.errorCode(status);
                });

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
        $scope.actionRensSok = function () {
            $scope.sok.lagringsenhet = '';
            $scope.sok.fanearkId = '';
            $scope.sok.fodselsnummer = '';
            $scope.sok.navn = '';
            $scope.sok.fodt = '';
            $scope.sok.oppdatertAv = '';
            $scope.sok.sistOppdatert = '';
        };
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
            stateService.sokState = $scope.sok;

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
            modalService.nyModal('common/modal-service/ny-avlevering.tpl.html', $scope.avleveringer, "avleveringer/ny", validering);
        };

        $scope.actionEndreAvlevering = function (avlevering) {
            modalService.endreModal('common/modal-service/ny-avlevering.tpl.html', $scope.avleveringer, "avleveringer/ny", validering, avlevering);
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

        $scope.actionSettDefaultAvlevering = function(avlevering){
            httpService.hent("avleveringer/"+avlevering.avleveringsidentifikator+"/aktiv")
                .success(function(data, status){
                    $scope.setValgtAvtale($scope.valgtAvtale);
                }).error(function(data, status){
                    errorService.errorCode(status);
            });
        };

        $scope.actionVisAvlevering = function (avlevering) {
            registreringService.setAvleveringsbeskrivelse(avlevering.avleveringsbeskrivelse);
            //TODO sjekk at dette ikke medfører feil
            //registreringService.setAvlevering(undefined);
            registreringService.setAvlevering(avlevering);
            httpService.hentAlle("pasientjournaler?side=1&antall=" + antall + "&avlevering=" + avlevering.avleveringsidentifikator)
                .success(function (data, status, headers, config) {

                    var tittel = {
                        "tittel": avlevering.avtale.virksomhet.foretaksnavn + " / " + avlevering.avtale.avtalebeskrivelse + " / " + avlevering.avleveringsbeskrivelse,
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
            httpService.logout();
            $window.location="logout";
        };

        $scope.actionLeggTilPasientjournald = function (avlevering) {
            registreringService.setAvlevering(avlevering);
            registreringService.setPasientjournalDTO(null);
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
        $scope.lagringsenheter = {};
        $scope.lagrSok = {};
        $scope.lagrPasientjournaler = [];

        $scope.lagrActionSok = function () {

            httpService.hentAlle("lagringsenheter/sok?identifikatorSok=" + $scope.lagrSok.lagringsenhet, false)
                .success(function (data) {
                    $scope.lagringsenheter = data;
                }).error(function (data, status) {
                    errorService.errorCode(status);
            });
        };

        $scope.selectedRow = null;// initialize our variable to null
        $scope.selectedRowIndex = null;
        $scope.selectedRowViewIndex = null;
        $scope.setClickedRow = function (item, index) {  //function that sets the value of selectedRow to current item.uuid
            if (item === $scope.selectedRow) {
                $scope.selectedRow = null;
                $scope.selectedRowIndex = null;
                $scope.selectedRowViewIndex = null;
            } else {
                for (var i=0;i<$scope.lagringsenheter.length;i++){
                    if ($scope.lagringsenheter[i].uuid === item){
                        $scope.selectedRowIndex = i;
                        break;
                    }
                }
                $scope.selectedRow = item;
                $scope.selectedRowViewIndex = index;
            }
        };

        $scope.lagrActionEndreLagringsenhet = function (lagringsenhet) {
            var uuid = lagringsenhet.uuid;
            httpService.hent("lagringsenheter/"+uuid+"/maske", false)
                .success(function (data, status, headers, config) {
                    var maske = data;
                    var modal = modalService.endreLagringsenhet('common/modal-service/endre-lagringsenhet-modal.tpl.html',
                        'lagringsenheter/',
                        lagringsenhet,
                        maske
                    );
                    modal.result.then(function () {
                        //TODO
                    });
                }).error(function (data, status, headers, config) {
                errorService.errorCode(status);
            });


        };
        $scope.lagrActionHentPasientjournaler = function () {
            if ($scope.selectedRowIndex > -1) {
                var valgtLagringsenhet = $scope.lagringsenheter[$scope.selectedRowIndex];
                httpService.hentAlle("lagringsenheter/" + valgtLagringsenhet.identifikator + "/pasientjournaler", false)
                    .success(function (data, status, headers, config) {
                        $scope.lagrPasientjournaler = data;
                    }).error(function (data, status, headers, config) {
                    errorService.errorCode(status);
                });
            }
        };

        $scope.lagrSelection = {
            allSelected: false
        };
        $scope.lagrToggleAll = function () {
            var toggleStatus = !$scope.lagrSelection.allSelected;
            angular.forEach($scope.lagrPasientjournaler, function (itm) {
                itm.selected = toggleStatus;
            });
        };
        $scope.lagrOptionToggled = function () {
            $scope.lagrSelection.allSelected = $scope.lagrPasientjournaler.every(function (itm) {
                return itm.selected;
            });
        };


        $scope.lagrFlytt = {
            lagringsenhet: ""
        };

        $scope.lagrActionFlytt = function () {
            var selectedPasientjournaler = [];
            angular.forEach($scope.lagrPasientjournaler, function (pasientjournal) {
                if (pasientjournal.selected) {
                    selectedPasientjournaler.push(pasientjournal.uuid);
                }
            });

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

        $scope.bruker = {};
        $scope.bruker.printerzpl = '127.0.0.1';
        $scope.brukere = [];

        $scope.selectedBrukerRow = null;  // initialize our variable to null

        $scope.velgBruker = function (index) {
            if (index === $scope.selectedBrukerRow) {
                $scope.selectedBrukerRow = null;
                $scope.bruker.brukernavn = null;
                $scope.bruker.rolle = null;
                $scope.bruker.printerzpl = null;
            } else {
                var valgtBruker = $scope.brukere[index];
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

            httpService.hentAlle("admin/brukere", false)
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

            httpService.ny("admin/brukere", $scope.bruker)
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