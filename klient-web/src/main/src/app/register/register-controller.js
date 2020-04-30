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

    .controller('RegisterCtrl', function HomeController($rootScope, $scope, $location, $filter, httpService, errorService,
                                                        registerService, diagnosisService, hotkeys, modalService,
                                                        $window, $controller) {

        //Util
        $scope.navBack = function () {
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

        //Tekster fra i18n
        $scope.$watch(
            function () {
                return $filter('translate')('register.gender.MALE');
            },
            function (value) {
                $scope.kjonn[0].tekst = value;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('register.gender.FEMALE');
            },
            function (value) {
                $scope.kjonn[1].tekst = value;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('register.gender.UNKNOWN');
            },
            function (value) {
                $scope.kjonn[2].tekst = value;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('register.gender.NOT_SPECIFIED');
            },
            function (value) {
                $scope.kjonn[3].tekst = value;
            }
        );

        //Feil tekster
        $scope.feilTekster = {};

        $scope.$watch(
            function () {
                return $filter('translate')('formError.NotNull');
            },
            function (value) {
                $scope.feilTekster['NotNull'] = value;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('formError.DagEllerAar');
            },
            function (value) {
                $scope.feilTekster['DagEllerAar'] = value;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('formError.NotUnique');
            },
            function (value) {
                $scope.feilTekster['NotUnique'] = value;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('formError.Size');
            },
            function (value) {
                $scope.feilTekster['Size'] = value;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('formError.FodtEtterDodt');
            },
            function (value) {
                $scope.feilTekster['FodtEtterDodt'] = value;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('formError.FeilFodselsnummer');
            },
            function (value) {
                $scope.feilTekster['FeilFodselsnummer'] = value;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('formError.FeilFanearkid');
            },
            function (value) {
                $scope.feilTekster['FeilFanearkid'] = value;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('formError.FeilFanearkidNull');
            },
            function (value) {
                $scope.feilTekster['FeilFanearkidNull'] = value;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('formError.EnObligatorisk');
            },
            function (value) {
                $scope.feilTekster['EnObligatorisk'] = value;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('formError.fKontaktForFodt');
            },
            function (value) {
                $scope.feilTekster['fKontaktForFodt'] = value;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('formError.sKontaktForFodt');
            },
            function (value) {
                $scope.feilTekster['sKontaktForFodt'] = value;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('formError.fKontaktEtterDod');
            },
            function (value) {
                $scope.feilTekster['fKontaktEtterDod'] = value;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('formError.sKontaktEtterDod');
            },
            function (value) {
                $scope.feilTekster['sKontaktEtterDod'] = value;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('formError.fKontaktEttersKontakt');
            },
            function (value) {
                $scope.feilTekster['fKontaktEttersKontakt'] = value;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('formError.UtenforGyldigPeriode');
            },
            function (value) {
                $scope.feilTekster['UtenforGyldigPeriode'] = value;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('formError.UkjentDiagnosekode');
            },
            function (value) {
                $scope.feilTekster['UkjentDiagnosekode'] = value;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('formError.manglermors');
            },
            function (value) {
                $scope.feilTekster['manglermors'] = value;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('formError.DiagEtterDod');
            },
            function (value) {
                $scope.feilTekster['DiagEtterDod'] = value;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('formError.DiagForFodt');
            },
            function (value) {
                $scope.feilTekster['DiagForFodt'] = value;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('formError.DiagFormatFeil');
            },
            function (value) {
                $scope.feilTekster['DiagFormatFeil'] = value;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('formError.sKontaktNotNull');
            },
            function (value) {
                $scope.feilTekster['sKontaktNotNull'] = value;
            }
        );

        httpService.getAll("config/century", false)
            .then(function (response) {
                $scope.century = response.data;
            }, function (response) {
                errorService.errorCode(response.status);
            });

        httpService.getAll("authors/all")
            .then(function (response) {
                $scope.allArchiveAuthors = response.data;
            }, function (response) {
                errorService.errorCode(response.status);
            });

        httpService.get("config/fanearkid")
            .then(function (response) {
                $scope.fanearkidMaxLength = response.data;
            }, function () {
                // Default to 12.
                $scope.fanearkidMaxLength = 12;
            });

        $scope.formData = {
            lagringsenheter: [],
            fanearkid: null
        };

        $scope.formDiagnose = {};
        $scope.avlevering = registerService.getTransfer();
        $scope.medicalRecordDTO = registerService.getMedicalRecordDTO();
        $scope.avleveringsidentifikator = registerService.getTransferId();
        $scope.virksomhet = registerService.getBusiness();
        $scope.valgtAvtale = registerService.getChosenAgreement();
        $scope.avleveringsbeskrivelse = registerService.getTransferDescription();

        $scope.manageStorageUnits = function () {
            var lagringsenhetmaske;

            var avlevering = registerService.getTransfer();
            if (avlevering !== null && avlevering !== undefined) {
                lagringsenhetmaske = avlevering.lagringsenhetformat;
            }

            if ($scope.formData.lagringsenheter === undefined || $scope.formData.lagringsenheter === null) {
                $scope.formData.lagringsenheter = [];
            }

            httpService.lastUsedStorageUnit()
                .then(function (response) {
                    var data = response.data;
                    if ($scope.formData.lagringsenheter.length === 0 && data) {
                        $scope.formData.lagringsenheter.push(data);
                    }

                    var modal = modalService.manageStorageUnits('common/modal-service/storage-unit-modal.tpl.html',
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
                }, function () {
                    // ignored
                });
        };

        $scope.manageArchiveAuthors = function () {
            if ($scope.formData.archiveAuthors === undefined || $scope.formData.archiveAuthors === null) {
                $scope.formData.archiveAuthors = [];
            }

            var modal = modalService.manageArchiveAuthors('common/modal-service/archive-author-modal.tpl.html',
                function () {
                    //TODO, callback for NHA-038
                },
                $scope.formData.archiveAuthors,
                $scope.allArchiveAuthors);

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

        $scope.showArchiveAuthors = function () {
            if ($scope.formData.archiveAuthors === undefined) {
                return null;
            }

            var selectedArchiveAuthors = [];
            $scope.formData.archiveAuthors.map( function (element) {
                selectedArchiveAuthors.push(element.code);
            });

            return selectedArchiveAuthors;
        };

        //Setter verdier fra registrering-service
        if ($scope.avlevering && !$scope.medicalRecordDTO) {
            //Ny pasientjouranl
            $scope.prevState = 0;
            $scope.state = 0;
            $scope.formData.archiveAuthors = [];
            switch ($scope.avlevering.arkivskaper) {
                case null:
                case undefined:
                case '':
                    break;
                default:
                    $scope.formData.archiveAuthors.push($scope.avlevering.arkivskaper);
            }
            $scope.manageStorageUnits();
        } else if ($scope.medicalRecordDTO !== undefined) {
            //Endre pasientjournal
            $scope.prevState = 2;
            $scope.state = 2;

            $scope.formData = $scope.medicalRecordDTO;

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
            $rootScope.navHome();
        }

        //Nullstiller diagnose skjema
        $scope.resetDiagnose = function (keepDate) {
            var oldDiagnosedato = $scope.formDiagnose.diagnosedato;
            $scope.formDiagnose = {};
            $scope.diagnosetekstErSatt = false;
            $scope.diagnoseDatoErSatt = false;
            $scope.diagnosekode = "";

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
            // ikke ny journal hvis ikke feltet er tomt..
            if (!$scope.sjekkDiagnoseFeltTomt) {
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

            function updateScopeData(data) {
                $scope.medicalRecordDTO = data;
                $scope.formData = data;
                $scope.formData.kjonn = kjonn;
                $scope.prevState = $scope.state;
                $scope.state = 2;
            }

            //NY
            if ($scope.state === 0) {
                $scope.formData.avleveringsidentifikator = $scope.avleveringsidentifikator;
                httpService.create("pasientjournaler/", $scope.formData)
                    .then(function(response) {
                        updateScopeData(response.data);
                    }, function(response) {
                        $scope.formData.kjonn = kjonn;
                        $scope.setFeilmeldinger(response.data, response.status);
                    });
            }

            //Endre
            if ($scope.state === 2) {
                $scope.formData.avleveringsidentifikator = $scope.avleveringsidentifikator;
                httpService.update("pasientjournaler/", $scope.medicalRecordDTO)
                    .then(function (response) {
                        var lagringsenheter = $scope.formData.lagringsenheter;
                        updateScopeData(response.data);
                        $scope.formData.lagringsenheter = lagringsenheter;
                        $scope.setFocus();
                    }, function(response) {
                        $scope.formData.kjonn = kjonn;
                        $scope.setFeilmeldinger(response.data, response.status);
                    });
            }

            //start en ny journal
            if ($scope.state === 3) {
                if (!$scope.medicalRecordDTO) {
                    return;
                }
                $scope.formData.avleveringsidentifikator = $scope.avleveringsidentifikator;
                httpService.update("pasientjournaler/", $scope.medicalRecordDTO)
                    .then(function() {
                        var lagringsenheter = $scope.formData.lagringsenheter;
                        $scope.prevState = $scope.state;
                        $scope.state = 0;
                        $scope.formData = {
                            lagringsenheter: lagringsenheter
                        };
                        $scope.setFocus();
                    }, function(response) {
                        $scope.formData.kjonn = kjonn;
                        $scope.setFeilmeldinger(response.data, response.status);
                    });
            }
        };

        angular.extend(this, $controller('RegisterUtilitiesCtrl', {$scope: $scope}));

    });
