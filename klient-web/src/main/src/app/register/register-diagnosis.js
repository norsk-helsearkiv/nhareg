angular.module('nha.register')

    .controller('RegisterDiagnosisCtrl', function ($scope, httpService, errorService, diagnosisService, modalService) {

        var diagnosekode = "";

        $scope.diagnosetekstErSatt = false;
        $scope.diagnoseDatoErSatt = false;

        //Tar vare på verdi ved fokus, for å sammenligne etterpå, for å ikke endre teksten
        $scope.setDiagnoseKode = function () {
            if ($scope.formDiagnose === null || $scope.formDiagnose.diagnosekode === null) {
                return;
            }
            diagnosekode = $scope.formDiagnose.diagnosekode;
            $scope.setDiagnoseTekst(false);
        };

        $scope.setDiagnoseDato = function(){
            $scope.formDiagnose.diagnosedato = $scope.injectCentury($scope.formDiagnose.diagnosedato);

            var dato = $scope.formDiagnose.diagnosedato;
            $scope.formDiagnose.diagnosekode = null;
            $scope.formDiagnose.diagnosetekst = null;
            $scope.diagnosetekstErSatt = false;
            if (dato){
                $scope.diagnoseDatoErSatt = true;
                document.getElementById("diagnosekode-input").focus();
            }else{
                $scope.diagnoseDatoErSatt = false;
            }
        };

        var prevDiagnose="";

        //Setter diagnoseteksten når koden er endret
        $scope.setDiagnoseTekst = function (laasDiagnosetekst) {
            //Hvis koden ikke er endret
            if (diagnosekode === $scope.formDiagnose.diagnosekode) {
                return;
            }

            diagnosekode = $scope.formDiagnose.diagnosekode;

            //Setter koden til upper case
            if ($scope.formDiagnose.diagnosekode) {
                $scope.formDiagnose.diagnosekode = $scope.formDiagnose.diagnosekode.toUpperCase();
            }

            //Henter alle diagnoser fra tjenesten
            diagnosisService.getDiagnosisServer($scope.formDiagnose.diagnosedato, $scope.formDiagnose.diagnosekode,  function(diagnosekoder){

                if (diagnosekoder[$scope.formDiagnose.diagnosekode] && diagnosekoder[$scope.formDiagnose.diagnosekode].length > 1) {
                    //Viser en modal med en liste over valgene
                    var modal = modalService.velgModal('common/modal-service/list-modal.tpl.html', diagnosekoder[$scope.formDiagnose.diagnosekode], $scope.formDiagnose);
                    modal.result.then(function () {
                        //Dersom teksten er satt, settes fokus på legg til diagnose
                        if ($scope.formDiagnose.diagnosetekst) {
                            if (laasDiagnosetekst){
                                $scope.diagnosetekstErSatt = true;
                            }
                            document.getElementById("btn-diagnosis").focus();
                        } else {
                            $scope.diagnosetekstErSatt = false;
                            document.getElementById("diagnosekode-input").focus();
                        }
                        prevDiagnose = diagnosekode;
                    }, function () {
                        document.getElementById("diagnosekode-input").focus();
                        $scope.formDiagnose.diagnosekode = prevDiagnose;
                    });


                } else if (diagnosekoder[$scope.formDiagnose.diagnosekode]) {
                    //En diagnose med gitt verdi
                    $scope.formDiagnose.diagnosetekst = diagnosekoder[$scope.formDiagnose.diagnosekode][0].displayName;
                    $scope.formDiagnose.diagnosekodeverk = diagnosekoder[$scope.formDiagnose.diagnosekode][0].codeSystemVersion;
                    if (laasDiagnosetekst){
                        $scope.diagnosetekstErSatt = true;
                    }
                    document.getElementById("btn-diagnosis").focus();
                    prevDiagnose = diagnosekode;
                } else {
                    //Ingen resultat på gitt kode
                    $scope.diagnosetekstErSatt = false;
                    prevDiagnose = "";
                }
            });

        };

        //Legger til diagnose i skjema
        $scope.leggTilDiagnose = function () {
            $scope.error = {};
            $scope.feilmeldinger = [];
            if (!$scope.formDiagnose.diagnosekode) {
                $scope.formDiagnose.diagnosekode = null;
            }
            if ($scope.formDiagnose.diagnosekode === '') {
                $scope.formDiagnose.diagnosekode = null;
            }

            if ($scope.pasientjournalDTO.diagnoser == null) {
                $scope.pasientjournalDTO.diagnoser = [];
            }

            httpService.create("diagnoser/" + $scope.pasientjournalDTO.persondata.uuid, $scope.formDiagnose)
                .success(function (data) {
                    $scope.formDiagnose.uuid = data.uuid;
                    $scope.formDiagnose.oppdatertAv = data.oppdatertAv;
                    $scope.formDiagnose.oppdatertDato = data.oppdatertDato;
                    $scope.pasientjournalDTO.diagnoser.push($scope.formDiagnose);
                    $scope.resetDiagnose(true);
                    $scope.shouldFirstAndLastContactDateChange(data.diagnosedato);
                }).error(function (data, status) {
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
                var parsedDate = new Date(validDate);

                return parsedDate;
            }
        }

        $scope.sokDiagnoseDisplayNameLike = function (displayName) {
            if (displayName.length > 2) {
                var results = [];
                var diagnoseDate = $scope.formDiagnose.diagnosedato===undefined?"": $scope.formDiagnose.diagnosedato;

                return httpService.getAll("diagnosekoder?displayNameLike=" + displayName+"&diagnoseDate="+diagnoseDate, false)
                    .then(function (resp) {
                        return resp.data.map(function (item) {
                            var res = [];
                            res.code = item.code;
                            res.displayName = item.codeSystemVersion + " | " +item.code+" | "+ item.displayName;
                            return res;
                        });
                    });
            }
        };

        $scope.onSelectDiagnose = function ($item, $model, $label) {
            $scope.formDiagnose.diagnosekode = $item.code;
            $scope.formDiagnose.diagnosetekst = null;
            $scope.setDiagnoseTekst(true);
        };

        //Fjerner diagnose
        $scope.fjernDiagnose = function (diagnose) {
            if (diagnose.diagnosekode === '') {
                delete diagnose.diagnosekode;
            }
            httpService.deleteData("diagnoser/" + $scope.pasientjournalDTO.persondata.uuid, diagnose)
                .success(function (data, status, headers, config) {
                    for (var i = 0; i < $scope.pasientjournalDTO.diagnoser.length; i++) {
                        if (diagnose === $scope.pasientjournalDTO.diagnoser[i]) {
                            $scope.pasientjournalDTO.diagnoser.splice(i, 1);
                        }
                    }
                    $scope.resetDiagnose();
                }).error(function (data, status, headers, config) {
                errorService.errorCode(status);
            });

        };

        $scope.editingData = [];

        $scope.editDiagnose = function () {
            for (var i = 0, length = $scope.pasientjournalDTO.diagnoser.length; i < length; i++) {
                $scope.editingData[$scope.pasientjournalDTO.diagnoser[i].id] = false;
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
            httpService.update("diagnoser/" + $scope.pasientjournalDTO.persondata.uuid, diagnose)
                .success(function (data, status, headers, config) {
                    $scope.editingData[diagnose.uuid] = false;
                })
                .error(function (data, status, headers, config) {
                    if (status === 400) {
                        $scope.setFeilmeldinger(data, status);
                    } else {
                        errorService.errorCode(status);
                    }
                });
        };

    });