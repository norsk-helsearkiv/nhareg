angular.module('nha.register', [
        'ui.router',
        'nha.common.http-service',
        'nha.common.error-service',
        'nha.register.register-service',
        'nha.common.diagnosis-service',
        'nha.common.modal-service',
        'cfp.hotkeys'
    ])

    .filter('parseCustomDate', function () {
        return function (date) {
            if (date === 'mors' || date === "ukjent") {
                return new Date("01.01.1000").getTime();
            }

            var sliceDay = date.slice(0, 3);
            var sliceMonth = date.slice(3, 6);
            var sliceYear = date.slice(6, 10);

            var validDate = sliceMonth + sliceDay + sliceYear;
            var parsedDate = new Date(validDate);

            return parsedDate.getTime();
        };
    })

    .config(function config($stateProvider) {
        $stateProvider.state('register', {
            url: '/registrer',
            views: {
                "main": {
                    controller: 'RegisterCtrl',
                    templateUrl: 'register/register.tpl.html'
                }
            }
        });
    })

    .controller('RegisterCtrl', function HomeController($scope, $location, $filter, httpService, errorService, registerService, diagnosisService, hotkeys, modalService, $window, $controller) {

        //Util
        $scope.navHome = function () {
            history.back();
        };

        //Hotkeylistener
        $window.onkeydown = function (event) {
            var keycode = event.charCode || event.keyCode;
            if (!event.ctrlKey) {
                return;
            }

            //For saving patient journal
            if (keycode === 80) { //p
                event.preventDefault();
                $scope.nyJournal();
            }
            if (keycode === 83) { //s
                event.preventDefault();
                $scope.nyEllerOppdater();
            }
        };

        //Setter verdier for å sørge for at undefined (null) blir håndtert riktig
        $scope.feilmeldinger = [];
        $scope.error = [];
        $scope.kjonn = [{kode: "M", tekst: ""}, {kode: "K", tekst: ""}, {kode: "U", tekst: ""}, {kode: "I", tekst: ""}];
        $scope.archiveCreators = [ "arkivskaper1", "arkivskaper2" ];

        //Tekster fra i18n
        $scope.$watch(
            function () {
                return $filter('translate')('register.gender.MALE');
            },
            function (newval) {
                $scope.kjonn[0].tekst = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('register.gender.FEMALE');
            },
            function (newval) {
                $scope.kjonn[1].tekst = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('register.gender.UNKNOWN');
            },
            function (newval) {
                $scope.kjonn[2].tekst = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('register.gender.NOT_SPECIFIED');
            },
            function (newval) {
                $scope.kjonn[3].tekst = newval;
            }
        );

        //Feil tekster
        $scope.feilTekster = {};

        $scope.$watch(
            function () {
                return $filter('translate')('formError.NotNull');
            },
            function (newval) {
                $scope.feilTekster['NotNull'] = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('formError.DagEllerAar');
            },
            function (newval) {
                $scope.feilTekster['DagEllerAar'] = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('formError.NotUnique');
            },
            function (newval) {
                $scope.feilTekster['NotUnique'] = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('formError.Size');
            },
            function (newval) {
                $scope.feilTekster['Size'] = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('formError.FodtEtterDodt');
            },
            function (newval) {
                $scope.feilTekster['FodtEtterDodt'] = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('formError.FeilFodselsnummer');
            },
            function (newval) {
                $scope.feilTekster['FeilFodselsnummer'] = newval;
            }
        );

        $scope.$watch(
            function () {
                return $filter('translate')('formError.FeilFanearkid');
            },
            function (newval) {
                $scope.feilTekster['FeilFanearkid'] = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('formError.FeilFanearkidNull');
            },
            function (newval) {
                $scope.feilTekster['FeilFanearkidNull'] = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('formError.EnObligatorisk');
            },
            function (newval) {
                $scope.feilTekster['EnObligatorisk'] = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('formError.fKontaktForFodt');
            },
            function (newval) {
                $scope.feilTekster['fKontaktForFodt'] = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('formError.sKontaktForFodt');
            },
            function (newval) {
                $scope.feilTekster['sKontaktForFodt'] = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('formError.fKontaktEtterDod');
            },
            function (newval) {
                $scope.feilTekster['fKontaktEtterDod'] = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('formError.sKontaktEtterDod');
            },
            function (newval) {
                $scope.feilTekster['sKontaktEtterDod'] = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('formError.fKontaktEttersKontakt');
            },
            function (newval) {
                $scope.feilTekster['fKontaktEttersKontakt'] = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('formError.UtenforGyldigPeriode');
            },
            function (newval) {
                $scope.feilTekster['UtenforGyldigPeriode'] = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('formError.UkjentDiagnosekode');
            },
            function (newval) {
                $scope.feilTekster['UkjentDiagnosekode'] = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('formError.manglermors');
            },
            function (newval) {
                $scope.feilTekster['manglermors'] = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('formError.DiagEtterDod');
            },
            function (newval) {
                $scope.feilTekster['DiagEtterDod'] = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('formError.DiagForFodt');
            },
            function (newval) {
                $scope.feilTekster['DiagForFodt'] = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('formError.DiagFormatFeil');
            },
            function (newval) {
                $scope.feilTekster['DiagFormatFeil'] = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('formError.sKontaktNotNull');
            },
            function (newval) {
                $scope.feilTekster['sKontaktNotNull'] = newval;
            }
        );

        httpService.getAll("admin/century", false).success(function (data) {
            $scope.century = data;
        }).error(function (status) {
            errorService.errorCode(status);
        });

        $scope.formData = {
            lagringsenheter: [],
            archiveCreators: []
        };

        $scope.formDiagnose = {};
        $scope.avlevering = registerService.getAvlevering();
        $scope.pasientjournalDTO = registerService.getPasientjournalDTO();
        $scope.avleveringsidentifikator = registerService.getAvleveringsidentifikator();
        $scope.virksomhet = registerService.getVirksomhet();
        $scope.valgtAvtale = registerService.getValgtAvtale();
        $scope.avleveringsbeskrivelse = registerService.getAvleveringsbeskrivelse();

        $scope.manageStorageUnits = function () {
            var lagringsenhetmaske;

            if ($scope.avlevering.lagringsenhetformat) {
                lagringsenhetmaske = $scope.avlevering.lagringsenhetformat;
            } else if ($scope.pasientjournalDTO.lagringsenhetformat) {
                lagringsenhetmaske = $scope.pasientjournalDTO.lagringsenhetformat;
            }

            if ($scope.formData.lagringsenheter === undefined || $scope.formData.lagringsenheter === null) {
                $scope.formData.lagringsenheter = [];
            }

            httpService.lastUsedStorageUnit()
                .success(function (data) {
                    if ($scope.formData.lagringsenheter.length === 0 && data) {
                        $scope.formData.lagringsenheter.push(data);
                    }

                    var modal = modalService.manageStorageUnits('common/modal-service/lagringsenhet-modal.tpl.html',
                        function () {
                            //TODO, callback for NHA-038
                        },
                        lagringsenhetmaske,
                        $scope.formData.lagringsenheter);

                    modal.result.then(function () {
                        switch ($scope.formData.fanearkid) {
                            case undefined:
                            case null:
                            case '':
                                document.getElementById("fanearkidInput").focus();
                                break;
                            default:
                                break;
                        }
                    });
                });
        };

        $scope.manageArchiveCreators = function () {

            if ($scope.formData.archiveCreators === undefined || $scope.formData.archiveCreators === null) {
                $scope.formData.archiveCreators = [];
            }

            var modal = modalService.manageArchiveCreators('common/modal-service/archive-creator-modal.tpl.html',
                function () {
                    //TODO, callback for NHA-038
                },
                $scope.archiveCreators,
                $scope.formData.archiveCreators);

            modal.result.then(function () {
                switch ($scope.formData.fanearkid) {
                    case undefined:
                    case null:
                    case '':
                        document.getElementById("fanearkidInput").focus();
                        break;
                    default:
                        break;
                }
            });
        };

        //Setter verdier fra registrering-service
        if ($scope.avlevering && !$scope.pasientjournalDTO) {
            //Ny pasientjouranl
            $scope.prevState = 0;
            $scope.state = 0;
            $scope.manageStorageUnits();

        } else if ($scope.pasientjournalDTO !== undefined) {
            //Endre pasientjournal
            $scope.prevState = 2;
            $scope.state = 2;

            $scope.formData = $scope.pasientjournalDTO.persondata;

            //Håndtering av kjønn - Sender kode til server, viser Tekst basert på i18n
            if ($scope.formData.kjonn !== undefined) {
                var funnet = false;
                for (var i = 0; i < $scope.kjonn.length; i++) {
                    if ($scope.kjonn[i].kode === $scope.formData.kjonn) {
                        $scope.formData.kjonn = $scope.kjonn[i];
                        funnet = true;
                    }
                }
                if (!funnet) {
                    $scope.formData.kjonn = {
                        "code": $scope.formData.kjonn,
                        "text": " "
                    };
                }
            }
        } else {
            //Ingen verdier er satt, naviger til home
            $scope.navHome();
        }

        //Nullstiller diagnose skjema
        $scope.resetDiagnose = function (keepDate) {
            var oldDiagnosedato = $scope.formDiagnose.diagnosedato;
            $scope.formDiagnose = {};
            $scope.diagnosetekstErSatt = false;
            $scope.diagnoseDatoErSatt = false;
            diagnosekode = "";

            if (keepDate) {
                document.getElementById("diagnosisDate-input").focus();
                $scope.formDiagnose.diagnosedato = oldDiagnosedato;
            }

        };

        $scope.sjekkDiagnoseFeltTomt = function (caller) {

            if ($scope.formDiagnose.diagnosekode || $scope.formDiagnose.diagnosetekst) {
                var tpl = 'common/modal-service/warning-modal.tpl.html';
                var tittel = $filter('translate')('modal.warning_diagnose.TITTEL');
                var beskrivelse = $filter('translate')('modal.warning_diagnose.BESKRIVELSE');

                modalService.warningModal(tpl, null, '', tittel, beskrivelse, function () {
                    $scope.formDiagnose.diagnosekode = null;
                    $scope.formDiagnose.diagnosedato = null;
                    $scope.formDiagnose.diagnosetekst = null;
                    caller();
                });
            } else {
                caller();
            }
        };

        $scope.nyJournal = function () {
            $scope.sjekkDiagnoseFeltTomt($scope.nyJournalCallback);
        };

        $scope.nyJournalCallback = function () {
            $scope.prevState = $scope.state;
            $scope.state = 3;
            $scope.resetDiagnose(false);
            $scope.nyEllerOppdater();
            $scope.manageStorageUnits();
        };

        $scope.nyEllerOppdater = function () {
            $scope.sjekkDiagnoseFeltTomt($scope.nyEllerOppdaterCallback);
        };

        $scope.nyEllerOppdaterCallback = function () {

            if (!$scope.sjekkDiagnoseFeltTomt) {//ikke ny journal hvis ikke feltet er tomt..
                return;
            }

            $scope.error = {};
            $scope.feilmeldinger = [];

            if ($scope.formData.lagringsenheter !== undefined && $scope.formData.lagringsenheter.length === 0) {
                delete $scope.formData.lagringsenheter;
            }

            var kjonn = $scope.formData.kjonn;
            if (kjonn !== undefined) {
                $scope.formData.kjonn = $scope.formData.kjonn.kode;
            }

            //NY
            if ($scope.state === 0) {
                //TODO popup for å finne lagringsenhet..
                httpService.create("avleveringer/" + $scope.avleveringsidentifikator + "/pasientjournaler", $scope.formData)
                    .success(function (data) {
                        $scope.pasientjournalDTO = data;
                        $scope.formData = data.persondata;
                        $scope.formData.kjonn = kjonn;
                        $scope.prevState = $scope.state;
                        $scope.state = 2;
                    }).error(function (data, status) {
                        $scope.formData.kjonn = kjonn;
                        $scope.setFeilmeldinger(data, status);
                    });
            }

            //Endre
            if ($scope.state === 2) {
                httpService.update("pasientjournaler/", $scope.pasientjournalDTO)
                    .success(function (data) {
                        var lagringsenheter = $scope.formData.lagringsenheter;
                        $scope.pasientjournalDTO = data;
                        $scope.formData = data.persondata;
                        $scope.formData.kjonn = kjonn;
                        $scope.formData.lagringsenheter = lagringsenheter;
                        $scope.prevState = $scope.state;
                        $scope.state = 2;
                        $scope.setFocus();
                    }).error(function (data, status) {
                        $scope.formData.kjonn = kjonn;
                        $scope.setFeilmeldinger(data, status);
                    });
            }
            //start en ny journal
            if ($scope.state === 3) {
                if (!$scope.pasientjournalDTO) {
                    return;
                }
                httpService.update("pasientjournaler/", $scope.pasientjournalDTO)
                    .success(function () {
                        var lagringsenheter = $scope.formData.lagringsenheter;
                        $scope.prevState = $scope.state;
                        $scope.state = 0;
                        $scope.formData = {
                            lagringsenheter: lagringsenheter
                        };
                        $scope.setFocus();
                    }).error(function (data, status) {
                        $scope.formData.kjonn = kjonn;
                        $scope.setFeilmeldinger(data, status);
                    });
            }
        };

        angular.extend(this, $controller('RegisterUtilitiesCtrl', {$scope: $scope}));

    });
