angular.module('nha.register')

    .controller('RegisterDiagnosisCtrl', function ($scope, httpService, errorService, 
                                                   diagnosisService, modalService) {

        var diagnosekode = "";

        $scope.diagnosetekstErSatt = false;
        $scope.diagnoseDatoErSatt = false;

        $scope.setDiagnoseDato = function () {
            $scope.formDiagnose.diagnosedato = 
                $scope.injectCentury($scope.formDiagnose.diagnosedato);

            var dato = $scope.formDiagnose.diagnosedato;
            $scope.formDiagnose.diagnosekode = null;
            $scope.formDiagnose.diagnosetekst = null;
            $scope.diagnosetekstErSatt = false;

            if (dato) {
                $scope.diagnoseDatoErSatt = true;
                document.getElementById("diagnosekode-input").focus();
            } else {
                $scope.diagnoseDatoErSatt = false;
            }
        };

        // Tar vare på verdi ved fokus, for å sammenligne etterpå, for å ikke endre teksten
        $scope.setDiagnosisCode = function () {
            if ($scope.formDiagnose === null || $scope.formDiagnose.diagnosekode === null) {
                return;
            }

            var codeObject = $scope.formDiagnose.diagnosekode;
            $scope.formDiagnose.diagnosekode = $scope.formDiagnose.diagnosekode.code;
            $scope.formDiagnose.diagnosetekst = codeObject.displayName;
        };

        var prevDiagnose = "";

        // Setter diagnoseteksten når koden er endret
        $scope.setDiagnoseTekst = function (laasDiagnosetekst) {
            // Hvis koden ikke er endret
            if (diagnosekode === $scope.formDiagnose.diagnosekode) {
                return;
            }

            diagnosekode = $scope.formDiagnose.diagnosekode;

            // Setter koden til upper case
            if ($scope.formDiagnose.diagnosekode) {
                $scope.formDiagnose.diagnosekode = $scope.formDiagnose.diagnosekode.toUpperCase();
            }

            // Henter alle diagnoser fra tjenesten
            diagnosisService.getDiagnosisServer($scope.formDiagnose.diagnosedato, $scope.formDiagnose.diagnosekode,
                function(diagnosekoder) {
                    if (diagnosekoder[$scope.formDiagnose.diagnosekode] && 
                        diagnosekoder[$scope.formDiagnose.diagnosekode].length > 1) {
                        // Viser en modal med en liste over valgene
                        var modal = modalService.velgModal('common/modal-service/list-modal.tpl.html', 
                            diagnosekoder[$scope.formDiagnose.diagnosekode],
                            $scope.formDiagnose);
                        modal.result.then(function () {
                            // Dersom teksten er satt, settes fokus på legg til diagnose
                            if ($scope.formDiagnose.diagnosetekst) {
                                if (laasDiagnosetekst) {
                                    $scope.diagnosetekstErSatt = true;
                                }
                                document.getElementById("btn-diagnosis").focus();
                            } else {
                                $scope.diagnosetekstErSatt = false;
                                document.getElementById("diagnosekode-input").focus();
                            }
                            prevDiagnose = diagnosekode;
                        }, function() {
                            document.getElementById("diagnosekode-input").focus();
                            $scope.formDiagnose.diagnosekode = prevDiagnose;
                        });
                    } else if (diagnosekoder[$scope.formDiagnose.diagnosekode]) {
                        // En diagnose med gitt verdi
                        $scope.formDiagnose.diagnosetekst = diagnosekoder[$scope.formDiagnose.diagnosekode][0].displayName;
                        $scope.formDiagnose.diagnosekodeverk = diagnosekoder[$scope.formDiagnose.diagnosekode][0].codeSystemVersion;
                        if (laasDiagnosetekst) {
                            $scope.diagnosetekstErSatt = true;
                        }
                        document.getElementById("btn-diagnosis").focus();
                        prevDiagnose = diagnosekode;
                    } else {
                        // Ingen resultat på gitt kode
                        $scope.diagnosetekstErSatt = false;
                        prevDiagnose = "";
                    }
                });
        };

        // Legger til diagnose i skjema
        $scope.leggTilDiagnose = function() {
            $scope.error = {};
            $scope.feilmeldinger = [];
            
            if (!$scope.formDiagnose.diagnosekode) {
                $scope.formDiagnose.diagnosekode = null;
            }
            
            if ($scope.formDiagnose.diagnosekode === '') {
                $scope.formDiagnose.diagnosekode = null;
            }

            if ($scope.medicalRecordDTO.diagnoser == null) {
                $scope.medicalRecordDTO.diagnoser = [];
            }

            httpService.create("diagnoser/" + $scope.medicalRecordDTO.uuid, $scope.formDiagnose)
                .then(function (response) {
                    var data = response.data;
                    $scope.formDiagnose.uuid = data.uuid;
                    $scope.formDiagnose.oppdatertAv = data.oppdatertAv;
                    $scope.formDiagnose.oppdatertDato = data.oppdatertDato;
                    $scope.medicalRecordDTO.diagnoser.push($scope.formDiagnose);
                    $scope.resetDiagnose(true);
                    $scope.shouldFirstAndLastContactDateChange(data.diagnosedato);
                }, function (response) {
                    var status = response.status;
                    var data = response.data;
                    if (status === 400) {
                        $scope.setFeilmeldinger(data, status);
                    } else {
                        errorService.errorCode(status);
                    }
                });
        };

        $scope.shouldFirstAndLastContactDateChange = function (diagnosisDate) {
            if (checkIfContactDateIsEmpty($scope.formData.fKontakt)) {
                $scope.formData.fKontakt = diagnosisDate;
            } else {
                setFirstContactDate(diagnosisDate);
            }

            if (checkIfContactDateIsEmpty($scope.formData.sKontakt)) {
                $scope.formData.sKontakt = diagnosisDate;
            } else {
                setLastContactDate(diagnosisDate);
            }

            $scope.nyEllerOppdater();
        };

        function setFirstContactDate (diagnosisDate) {
            if (parseDate(diagnosisDate) <= parseDate($scope.formData.fKontakt)) {
                $scope.formData.fKontakt = diagnosisDate;
            }
        }

        function setLastContactDate (diagnosisDate) {
            if (parseDate(diagnosisDate) >= parseDate($scope.formData.sKontakt)) {
                $scope.formData.sKontakt = diagnosisDate;
            }
        }

        function checkIfContactDateIsEmpty (contactDate) {
            switch (contactDate) {
                case undefined:
                case null:
                case '':
                    return true;
                default:
                    return false;
            }
        }

        function parseDate (date) {
            if(date.length === 4) {
                var parsedYear = new Date(date);
                return parsedYear.setHours(0,0,0,0);
            } else {
                var sliceDay = date.slice(0, 3);
                var sliceMonth = date.slice(3, 6);
                var sliceYear = date.slice(6, 10);

                var validDate = sliceMonth + sliceDay + sliceYear;

                return new Date(validDate);
            }
        }

        $scope.searchDiagnosisCodes = function (code) {
            var diagnosisDate = $scope.formDiagnose.diagnosedato == null ? "" : $scope.formDiagnose.diagnosedato;

            return httpService.getAll("diagnosekoder?code=" + code + "&date=" + diagnosisDate + "&page=1&size=50")
                .then(function (response) {
                    return response.data;
                }, function (response) {
                    if (response.status === 400) {
                        $scope.setFeilmeldinger(response.data, response.status);
                    } else {
                        errorService.errorCode(response.status);
                    }
                });
        };

        $scope.sokDiagnoseDisplayNameLike = function (displayName) {
            if (displayName.length > 2) {
                var diagnosisDate = $scope.formDiagnose.diagnosedato == null ? "" : $scope.formDiagnose.diagnosedato;
                var diagnosisCode = $scope.formDiagnose.diagnosekode == null ? "" : $scope.formDiagnose.diagnosekode;

                return httpService.getAll("diagnosekoder?name=" + displayName + "&date=" + diagnosisDate + "&code=" + diagnosisCode +
                    "&page=1&size=50", false)
                    .then(function (response) {
                        return response.data.map(
                            function (item) {
                                var res = [];
                                res.code = item.code;
                                res.displayName = item.codeSystemVersion + " | "  + item.code + " | " + item.displayName;
                                return res;
                            });
                    }, function (response) {
                        if (response.status === 400) {
                            $scope.setFeilmeldinger(response.data, response.status);
                        } else {
                            errorService.errorCode(response.status);
                        }
                    });
            } else {
                return { 'displayName': null };
            }
        };

        $scope.onSelectDiagnose = function ($item) {
            $scope.formDiagnose.diagnosekode = $item.code;
            $scope.formDiagnose.diagnosetekst = $item.displayName;
            $scope.setDiagnoseTekst(true);
        };

        // Fjerner diagnose
        $scope.fjernDiagnose = function (diagnose) {
            if (diagnose.diagnosekode === '') {
                delete diagnose.diagnosekode;
            }
            httpService.deleteData("diagnoser/" + $scope.medicalRecordDTO.uuid, diagnose)
                .then(function () {
                    for (var i = 0; i < $scope.medicalRecordDTO.diagnoser.length; i++) {
                        if (diagnose === $scope.medicalRecordDTO.diagnoser[i]) {
                            $scope.medicalRecordDTO.diagnoser.splice(i, 1);
                        }
                    }
                    $scope.resetDiagnose();
                }, function (response) {
                    errorService.errorCode(response.status);
                });
        };

        $scope.editingData = [];

        $scope.editDiagnose = function () {
            for (var i = 0, length = $scope.medicalRecordDTO.diagnoser.length; i < length; i++) {
                $scope.editingData[$scope.medicalRecordDTO.diagnoser[i].id] = false;
            }
        };

        $scope.modify = function (diagnose) {
            if ($scope.editingData.length === 0) {
                $scope.editDiagnose();
            }
            $scope.editingData[diagnose.uuid] = true;
        };

        $scope.editDiagnosekode = function (diagnose) {
            diagnose.diagnosekode = diagnose.diagnosekode.toUpperCase();
            var diagnosekoder = diagnosisService.getDiagnosisServer();
            diagnose.diagnosetekst = diagnosekoder[diagnose.diagnosekode].displayName;
        };

        $scope.update = function (diagnose) {
            //TODO
            diagnose.diagnosedato = $scope.injectCentury(diagnose.diagnosedato);
            $scope.feilmeldinger = [];
            httpService.update("diagnoser/" + $scope.medicalRecordDTO.uuid, diagnose)
                .success(function () {
                    $scope.editingData[diagnose.uuid] = false;
                })
                .error(function (data, status) {
                    if (status === 400) {
                        $scope.setFeilmeldinger(data, status);
                    } else {
                        errorService.errorCode(status);
                    }
                });
        };

    });