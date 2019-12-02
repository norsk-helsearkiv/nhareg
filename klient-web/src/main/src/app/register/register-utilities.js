angular.module('nha.register')

    .controller('RegisterUtilitiesCtrl', function($scope, $filter, httpService, errorService) {

        //Setting focus on the right element
        $scope.setFocus = function () {
            //New patient journal
            if ($scope.state === 0) {
                if ($scope.formData.lagringsenheter.length !== 0) {
                    document.getElementById("fanearkidInput").focus();
                } else {
                    document.getElementById("lagringsenhet").focus();
                }
            }
            //Updating patient journal
            else if ($scope.state === 2) {
                document.getElementById("diagnosedato").focus();
            }
        };

        $scope.setFocus();

        var hoppOver = false;

        $scope.setFocusEtterNavn = function () {
            if ($scope.formData !== undefined) {
                if ($scope.formData.navn !== undefined && $scope.formData.navn.length > 0) {
                    var str = $scope.formData.navn;
                    $scope.formData.navn = str.replace(/\S+/g,
                        function(txt){
                            return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();
                        });
                }
            }

            if (hoppOver) {
                document.getElementById("ddato").focus();
            }
            hoppOver = false;
        };

        var fodselsnummer;

        $scope.setFnr = function () {
            if ($scope.formData && $scope.formData.fodselsnummer) {
                fodselsnummer = $scope.formData.fodselsnummer;
            }
        };

        $scope.getAarhundreFromFnr = function (fnr) {
            var individ = Number(fnr.substring(6, 9));
            var aar = Number(fnr.substring(4, 6));

            var d1 = Number(fnr.substring(0, 1));//for d-nummersjekk
            var m1 = Number(fnr.substring(2, 3));//for h-nummersjekk
            var dnummer = d1 > 3;
            var hnummer = m1 > 3;

            if (dnummer) {
                if (individ >= 0 && individ <= 499) {
                    return 19;
                }
                if (individ >= 500 && individ <= 999) {
                    return 20;
                }
            }
            if (hnummer) {
                if ((individ >= 0 && individ <= 499) && (aar >= 0 && aar <= 99)) {
                    return 19;
                }
                if ((individ >= 500 && individ <= 749) && (aar >= 55 && aar < 99)) {
                    return 18;
                }
                if ((individ >= 500 && individ <= 999) && (aar >= 0 && aar <= 39)) {
                    return 20;
                }

            } else {
                if ((individ >= 0 && individ <= 99) && (aar >= 0 && aar <= 39)) {
                    return 20;
                }
                if ((individ >= 0 && individ <= 499) && (aar >= 0 && aar <= 99)) {
                    return 19;
                }
                if ((individ >= 500 && individ <= 749) && (aar >= 55 && aar <= 99)) {
                    return 18;
                }
                if ((individ >= 500 && individ <= 999) && (aar >= 0 && aar <= 39)) {
                    return 20;
                }
                if ((individ >= 900 && individ <= 999) && (aar >= 40 && aar <= 99)) {
                    return 19;
                }
            }
        };

        kjonnFromFodselsnummer = function (fnr) {
            var individsifre = fnr.substring(6, 9);
            var kjonn = Number(individsifre.substring(2, 3));
            return kjonn % 2 === 0 ? $scope.kjonn[1] : $scope.kjonn[0];
        };

        $scope.populerFelt = function () {
            if ($scope.formData.fodselsnummer !== undefined && $scope.formData.fodselsnummer !=='') {
                var fnrs = $scope.formData.fodselsnummer.replace(/\D/g, '');
                fnrs = fnrs.substr(0, 11);
                $scope.formData.fodselsnummer = fnrs;
            }else{
                return;
            }

            httpService.get("pasientjournaler/valider/" + $scope.formData.fodselsnummer)
                .success(function () {
                    $scope.error = {};
                    $scope.feilmeldinger = [];
                    $scope.validerOgTrekkUt();
                })
                .error(function (data, status) {
                    if (status === 400) {
                        $scope.setFeilmeldinger(data, status);
                    } else {
                        errorService.errorCode(status);
                    }
                });
        };

        $scope.validerOgTrekkUt = function(){
            if (($scope.formData.fodselsnummer === undefined || fodselsnummer === $scope.formData.fodselsnummer || $scope.formData.fodselsnummer.length != 11) && $scope.formData.fodt !== '') {
                return;
            }

            //Valider nr
            var aarhundre = $scope.getAarhundreFromFnr($scope.formData.fodselsnummer);
            if (!aarhundre) {
                return;
            }

            var kjonnValidert = false, datoValidert = false;
            if ($scope.formData.fodselsnummer.length == 11) {

                var kjonn = kjonnFromFodselsnummer($scope.formData.fodselsnummer);
                if (kjonn) {
                    $scope.formData.kjonn = kjonn;
                    kjonnValidert = true;
                }
            }

            //Valider dato
            var fdato = $scope.formData.fodselsnummer.substring(0, 6);

            var d1 = Number(fdato.substring(0, 1));//for d-nummersjekk
            var d2 = Number(fdato.substring(1, 2));
            var m1 = Number(fdato.substring(2, 3));//for h-nummersjekk
            var m2 = Number(fdato.substring(3, 4));
            var y1 = Number(fdato.substring(4, 5));
            var y2 = Number(fdato.substring(5, 6));
            var dag = d1 + "" + d2, mnd = m1 + "" + m2, aar;

            if (d1 > 3) { //indikerer d-nummer
                dag = (d1 - 4) + "" + d2;
                mnd = m1 + "" + m2;
            }
            if (m1 > 3) { //indikerer h-nummer
                mnd = (m1 - 4) + "" + m2;
                dag = d1 + "" + d2;
            }
            aar = y1 + "" + y2;

            if (dag > 0 && dag < 32 && mnd > 0 && mnd < 13 && aar >= 0) {
                datoValidert = true;
                $scope.formData.fodt = dag + "." + mnd + "." + aarhundre + aar;
            }

            hoppOver = kjonnValidert && datoValidert;
        };

        $scope.injectCenturiesPJ = function(){
            $scope.formData.fodt = $scope.injectCentury($scope.formData.fodt);
            $scope.formData.dod = $scope.injectCentury($scope.formData.dod);
            $scope.formData.fKontakt = $scope.injectCentury($scope.formData.fKontakt);
            $scope.formData.sKontakt = $scope.injectCentury($scope.formData.sKontakt);
        };

        $scope.injectCentury = function(date){
            if (date === undefined){
                return;
            }
            //case 1 ddMMyy - legg til århundre res: ddMMyyyy
            var regexp1 = new RegExp("^[0-9]{6}$");
            if (regexp1.test(date)){
                return insertAt(date);
            }
            //case 2 d.M.yy - legg til århundre res: d.M.yyyy
            var regexp2 = new RegExp("^\\d{1}[.\\,\\-]\\d{1}[.\\,\\-]\\d{2}$");
            if (regexp2.test(date)){
                return insertAt(date);
            }
            //case 4 dd.MM.yy - legg til århundre res: dd.MM.yyyy
            var regexp4 = new RegExp("^\\d{2}[.\\,\\-]\\d{2}[.\\,\\-]\\d{2}$");
            if (regexp4.test(date)){
                return insertAt(date);
            }
            //case 3 yy - legg til århundre res: yyyy
            var regexp3 = new RegExp("^[0-9]{2}$");
            if (regexp3.test(date)){
                return insertAt(date);
            }
            return date;
        };

        var insertAt = function(date){
            return [date.slice(0, date.length-2), $scope.century, date.slice(date.length-2, date.length)].join('');
        };

        $scope.setFeilmeldinger = function (data, status) {
            if (status != 400) {
                errorService.errorCode(status);
                return;
            }

            var index = 0 ;

            angular.forEach(data, function (element) {
                $scope.error[element.attribute] = element.constraint;

                var felt = document.getElementById(element.attribute).innerHTML;

                if (felt !== undefined) {
                    if (element.message !== undefined && element.message !== "may not be null"){ //sett inn variabler i feilmeldingen
                        var predefined = 'feltfeil.' + element.constraint;
                        var errorMessage = $filter('translate')(predefined, element.message);
                        $scope.feilTekster[element.constraint] = errorMessage;
                    }
                    var txt = $scope.feilTekster[element.constraint];
                    var elm = { indeks:index, feilmelding:txt, felt:felt };
                    var pos = $scope.feilmeldinger.map(function(e) { return e.indeks; }).indexOf(elm.index);

                    if (pos === -1){
                        $scope.feilmeldinger.push(elm);
                    }
                }

                index++;
            });

            $scope.feilmeldinger.sort(compare);
        };

        function compare(a, b) {
            if (a.indeks < b.indeks) {
                return -1;
            }
            if (a.indeks > b.indeks) {
                return 1;
            }
            return 0;
        }

    });