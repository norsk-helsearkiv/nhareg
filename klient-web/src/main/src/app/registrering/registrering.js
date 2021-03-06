angular.module('nha.registrering', [
        'ui.router',
        'nha.common.http-service',
        'nha.common.error-service',
        'nha.registrering.registrering-service',
        'nha.common.diagnose-service',
        'nha.common.modal-service',
        'cfp.hotkeys'
    ])
    .filter('parseCustomDate', function(){
        return function(date){
            if (date==='mors'||date=="ukjent"){
                return new Date("01.01.1000").getTime();
            }
            var d = new Date(date);
            return d.getTime();
        };
    })
    .config(function config($stateProvider) {
        $stateProvider.state('registrer', {
            url: '/registrer',
            views: {
                "main": {
                    controller: 'RegistrerCtrl',
                    templateUrl: 'registrering/registrering.tpl.html'
                }
            }
        });
    })
    
    .controller('RegistrerCtrl', function HomeController($scope, $location, $filter, httpService, errorService, registreringService, diagnoseService, hotkeys, modalService, $window) {
        //Util
        $scope.navHome = function () {
            history.back();
        };


        //Hotkeylistener for ctrl+n og ctrl+s
        $window.onkeydown = function (event) {
            var keycode = event.charCode || event.keyCode;
            if (!event.ctrlKey) {
                return;
            }

            if (keycode === 80) { //p for pasientjournal (ny)
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
        $scope.state = 0; //0 = ny, 1 = legg til diagnoser, 2 = endre
        $scope.prevState = 0;
        var hoppOver = false;

        //Tekster fra i18n
        $scope.$watch(
            function () {
                return $filter('translate')('registrer.kjonn.MANN');
            },
            function (newval) {
                $scope.kjonn[0].tekst = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('registrer.kjonn.KVINNE');
            },
            function (newval) {
                $scope.kjonn[1].tekst = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('registrer.kjonn.IKKE_KJENT');
            },
            function (newval) {
                $scope.kjonn[2].tekst = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('registrer.kjonn.IKKE_SPESIFISERT');
            },
            function (newval) {
                $scope.kjonn[3].tekst = newval;
            }
        );

        //Feil tekster
        $scope.feilTekster = {};
        $scope.$watch(
            function () {
                return $filter('translate')('feltfeil.NotNull');
            },
            function (newval) {
                $scope.feilTekster['NotNull'] = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('feltfeil.DagEllerAar');
            },
            function (newval) {
                $scope.feilTekster['DagEllerAar'] = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('feltfeil.NotUnique');
            },
            function (newval) {
                $scope.feilTekster['NotUnique'] = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('feltfeil.Size');
            },
            function (newval) {
                $scope.feilTekster['Size'] = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('feltfeil.FodtEtterDodt');
            },
            function (newval) {
                $scope.feilTekster['FodtEtterDodt'] = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('feltfeil.FeilFodselsnummer');
            },
            function (newval) {
                $scope.feilTekster['FeilFodselsnummer'] = newval;
            }
        );

        $scope.$watch(
            function () {
                return $filter('translate')('feltfeil.FeilFanearkid');
            },
            function (newval) {
                $scope.feilTekster['FeilFanearkid'] = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('feltfeil.FeilFanearkidNull');
            },
            function (newval) {
                $scope.feilTekster['FeilFanearkidNull'] = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('feltfeil.EnObligatorisk');
            },
            function (newval) {
                $scope.feilTekster['EnObligatorisk'] = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('feltfeil.fKontaktForFodt');
            },
            function (newval) {
                $scope.feilTekster['fKontaktForFodt'] = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('feltfeil.sKontaktForFodt');
            },
            function (newval) {
                $scope.feilTekster['sKontaktForFodt'] = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('feltfeil.fKontaktEtterDod');
            },
            function (newval) {
                $scope.feilTekster['fKontaktEtterDod'] = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('feltfeil.sKontaktEtterDod');
            },
            function (newval) {
                $scope.feilTekster['sKontaktEtterDod'] = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('feltfeil.fKontaktEttersKontakt');
            },
            function (newval) {
                $scope.feilTekster['fKontaktEttersKontakt'] = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('feltfeil.UtenforGyldigPeriode');
            },
            function (newval) {
                $scope.feilTekster['UtenforGyldigPeriode'] = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('feltfeil.UkjentDiagnosekode');
            },
            function (newval) {
                $scope.feilTekster['UkjentDiagnosekode'] = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('feltfeil.manglermors');
            },
            function (newval) {
                $scope.feilTekster['manglermors'] = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('feltfeil.DiagEtterDod');
            },
            function (newval) {
                $scope.feilTekster['DiagEtterDod'] = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('feltfeil.DiagForFodt');
            },
            function (newval) {
                $scope.feilTekster['DiagForFodt'] = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('feltfeil.DiagFormatFeil');
            },
            function (newval) {
                $scope.feilTekster['DiagFormatFeil'] = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('feltfeil.sKontaktNotNull');
            },
            function (newval) {
                $scope.feilTekster['sKontaktNotNull'] = newval;
            }
        );

        httpService.hentAlle("admin/century", false).success(function (data) {
            $scope.century = data;
        }).error(function(status){
            errorService.errorCode(status);
        });


        $scope.formData = {
            lagringsenheter: []
        };
        $scope.lagringseneheterModel = $scope.formData.lagringsenheter.map(function(el){return el.name;}).join(",");

        $scope.formDiagnose = {};
        $scope.avlevering = registreringService.getAvlevering();
        $scope.pasientjournalDTO = registreringService.getPasientjournalDTO();
        $scope.avleveringsidentifikator = registreringService.getAvleveringsidentifikator();
        $scope.virksomhet = registreringService.getVirksomhet();
        $scope.valgtAvtale = registreringService.getValgtAvtale();
        $scope.avleveringsbeskrivelse = registreringService.getAvleveringsbeskrivelse();

        $scope.velgLagringsenhet = function() {

            var lagringsenhetmaske;
            if ($scope.avlevering.lagringsenhetformat) {
                //TODO...
                lagringsenhetmaske = $scope.avlevering.lagringsenhetformat;
            } else if ($scope.pasientjournalDTO.lagringsenhetformat){
                lagringsenhetmaske = $scope.pasientjournalDTO.lagringsenhetformat;
            }

            if ($scope.formData.lagringsenheter === undefined || $scope.formData.lagringsenheter === null) {
                $scope.formData.lagringsenheter = [];
            }

            httpService.sistBrukteLagringsenhet()
                .success(function (data) {
                    if ($scope.formData.lagringsenheter.length===0 && data){
                        $scope.formData.lagringsenheter.push(data);
                    }

                    var modal = modalService.velgLagringsenhet('common/modal-service/lagringsenhet-modal.tpl.html', 
                        function(){
                            console.log("TODO, callback for NHA-038");
                            }, 
                        lagringsenhetmaske, 
                        $scope.formData.lagringsenheter);
                    modal.result.then(function () {
                        var formdata = $scope.formData;
                        //TODO finish this!!
                    });
                });
        };

        //Setter verdier fra registrering-service
        if ($scope.avlevering && !$scope.pasientjournalDTO) {
            //Ny pasientjouranl
            $scope.state = 0;
            $scope.velgLagringsenhet();

        } else if ($scope.pasientjournalDTO !== undefined) {
            //Endre pasientjournal
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
                        "kode": $scope.formData.kjonn,
                        "tekst": " "
                    };
                }
            }
        } else {
            //Ingen verdier er satt, naviger til home
            $scope.navHome();
        }

        //Hjelpemetode for å sette focus på rett felt
        var setFocus = function () {
            //Ny
            if ($scope.state === 0) {
                if ($scope.formData.lagringsenheter.length !== 0) {
                    //document.getElementById("journalnummerInput").focus();
                    document.getElementById("fanearkidInput").focus();
                } else {
                    document.getElementById("lagringsenhet").focus();
                }
            } else
            //Legg til diagnoser
            if ($scope.state === 1) {
                document.getElementById("diagnosedato").focus();
            } else
            //Oppdater
            if ($scope.state === 2) {
                document.getElementById("diagnosedato").focus();
            }
        };
        setFocus();

        $scope.keyAddLagringsenhet = function () {//TODO FJERN NÅR NHA-038 er ferdig..
            if ($scope.lagringsenhet === undefined || $scope.lagringsenhet === '') {
                return;
            }
            if ($scope.formData.lagringsenheter === undefined || $scope.formData.lagringsenheter === null) {
                $scope.formData.lagringsenheter = [];
            }

            for (var i = 0; i < $scope.formData.lagringsenheter.length; i++) {
                if ($scope.lagringsenhet === $scope.formData.lagringsenheter[i]) {
                    $scope.lagringsenhet = "";
                    return;
                }
            }
            $scope.formData.lagringsenheter.push($scope.lagringsenhet);
            $scope.lagringsenhet = "";
        };

        $scope.actionFjernLagringsenhet = function (enhet) {//TODO fjernes når NHA-038 er ferdig...
            for (var i = 0; i < $scope.formData.lagringsenheter.length; i++) {
                if (enhet === $scope.formData.lagringsenheter[i]) {
                    $scope.formData.lagringsenheter.splice(i, 1);
                    document.getElementById("lagringsenhet").focus();
                }
            }
        };

        var fodselsnummer;
        $scope.setFnr = function () {
            if ($scope.formData && $scope.formData.fodselsnummer) {


                fodselsnummer = $scope.formData.fodselsnummer;
            }
        };

        var getAarhundreFromFnr = function (fnr) {
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


            httpService.hent("pasientjournaler/valider/" + $scope.formData.fodselsnummer)
                .success(function (data, status, headers, config) {
                    $scope.error = {};
                    $scope.feilmeldinger = [];
                    $scope.validerOgTrekkUt();
                })
                .error(function (data, status, headers, config) {
                    if (status === 400) {
                        setFeilmeldinger(data, status);
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
            var aarhundre = getAarhundreFromFnr($scope.formData.fodselsnummer);
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

        function compare(a, b) {
            if (a.indeks < b.indeks) {
                return -1;
            }
            if (a.indeks > b.indeks) {
                return 1;
            }
            return 0;
        }

        var setFeilmeldinger = function (data, status) {
            if (status != 400) {
                errorService.errorCode(status);
                return;
            }

            angular.forEach(data, function (element) {
                $scope.error[element.attributt] = element.constriant;

                var index;
                var felt;
                var feil;

                //Konverter attributt
                if (element.attributt === 'lagringsenheter') {
                    index = 0;
                    felt = document.getElementById('labelLagringsenhet').innerHTML;
                }
                if (element.attributt === 'journalnummer') {
                    index = 1;
                    felt = document.getElementById('journalnummer').innerHTML;
                }
                if (element.attributt === 'lopenummer') {
                    index = 2;
                    felt = document.getElementById('lopenummer').innerHTML;
                }
                if (element.attributt === 'fodselsnummer') {
                    index = 3;
                    felt = document.getElementById('fodselsnummer').innerHTML;
                }
                if (element.attributt === 'navn') {
                    index = 4;
                    felt = document.getElementById('navn').innerHTML;
                }
                if (element.attributt === 'kjonn') {
                    index = 5;
                    felt = document.getElementById('kjonn').innerHTML;
                }
                if (element.attributt === 'fodt') {
                    index = 6;
                    felt = document.getElementById('fodt').innerHTML;
                }
                if (element.attributt === 'dod') {
                    index = 7;
                    felt = document.getElementById('dod').innerHTML;
                }
                if (element.attributt === 'fKontakt') {
                    index = 8;
                    felt = document.getElementById('fKontakt').innerHTML;
                }
                if (element.attributt === 'sKontakt') {
                    index = 9;
                    felt = document.getElementById('sKontakt').innerHTML;
                }
                if (element.attributt === 'diagnosedato') {
                    index = 10;
                    felt = document.getElementById('diagnosedato').innerHTML;
                }
                if (element.attributt === 'diagnosedatotab') {
                    index = 10;
                    felt = document.getElementById('diagnosedato_table').innerHTML;
                }
                if (element.attributt === 'diagnosekodetab') {
                    index = 12;
                    felt = document.getElementById('diagnosekode_table').innerHTML;
                }
                if (element.attributt === 'diagnosekode') {
                    index = 13;
                    felt = document.getElementById('diagnosekode').innerHTML;
                }
                if (element.attributt === 'diagnosetekst') {
                    index = 14;
                    felt = document.getElementById('diagnosetekst').innerHTML;
                }
                if (element.attributt === 'fanearkid') {
                    index = 15;
                    felt = document.getElementById('fanearkid').innerHTML;
                }
                if (felt !== undefined) {
                    if (element.message !== undefined){ //sett inn variabler i feilmeldingen
                        var predefined = 'feltfeil.'+element.constriant;
                        var fm = $filter('translate')(predefined,element.message);
                        $scope.feilTekster[element.constriant] = fm;
                    }
                    var txt = $scope.feilTekster[element.constriant];
                    var elm = {indeks:index,  feilmelding:txt, felt:felt };
                    var pos = $scope.feilmeldinger.map(function(e) { return e.indeks; }).indexOf(elm.index);

                    if (pos === -1){
                        $scope.feilmeldinger.push(elm);
                    }
                }
            });
            $scope.feilmeldinger.sort(compare);
        };

        $scope.injectCenturiesPJ = function(event){

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

        $scope.sjekkDiagnoseFeltTomt = function(caller){

            if ($scope.formDiagnose.diagnosekode ||
                $scope.formDiagnose.diagnosetekst){

                var tpl = 'common/modal-service/warning-modal.tpl.html';
                var tittel = $filter('translate')('modal.warning_diagnose.TITTEL');
                var beskrivelse = $filter('translate')('modal.warning_diagnose.BESKRIVELSE');
                    modalService.warningModal(tpl, null, '', tittel, beskrivelse, function () {
                        $scope.formDiagnose.diagnosekode=null;
                        $scope.formDiagnose.diagnosedato=null;
                        $scope.formDiagnose.diagnosetekst=null;
                        caller();
                    });
            }else{
                caller();
            }
        };

        $scope.nyJournal = function(){
            $scope.sjekkDiagnoseFeltTomt($scope.nyJournalCallback);
        };

        $scope.nyJournalCallback = function () {
            
            $scope.prevState = $scope.state;
            $scope.state = 3;
            resetDiagnose(false);
            $scope.nyEllerOppdater();
            $scope.velgLagringsenhet();
        };

        $scope.nyEllerOppdater = function(){
            $scope.sjekkDiagnoseFeltTomt($scope.nyEllerOppdaterCallback);
        };

        $scope.nyEllerOppdaterCallback = function () {

            if (!$scope.sjekkDiagnoseFeltTomt){//ikke ny journal hvis ikke feltet er tomt..
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
                httpService.ny("avleveringer/" + $scope.avleveringsidentifikator + "/pasientjournaler", $scope.formData)
                    .success(function (data, status, headers, config) {
                        $scope.pasientjournalDTO = data;
                        $scope.formData = data.persondata;
                        $scope.formData.kjonn = kjonn;
                        $scope.state = 2;
                    }).error(function (data, status, headers, config) {
                    $scope.formData.kjonn = kjonn;
                    setFeilmeldinger(data, status);
                });
            }

            //Endre
            if ($scope.state === 2) {
                httpService.oppdater("pasientjournaler/", $scope.pasientjournalDTO)
                    .success(function (data, status, headers, config) {
                        var lagringsenheter = $scope.formData.lagringsenheter;
                        $scope.pasientjournalDTO = data;
                        $scope.formData = data.persondata;
                        $scope.formData.kjonn = kjonn;
                        $scope.formData.lagringsenheter = lagringsenheter;
                        $scope.state = 2;
                        setFocus();
                    }).error(function (data, status, headers, config) {
                    $scope.formData.kjonn = kjonn;
                    setFeilmeldinger(data, status);
                });
            }
            //start en ny journal
            if ($scope.state === 3) {
                $scope.state = $scope.prevState;
                if (!$scope.pasientjournalDTO) {
                    return;
                }
                httpService.oppdater("pasientjournaler/", $scope.pasientjournalDTO)
                    .success(function (data, status, headers, config) {
                        var lagringsenheter = $scope.formData.lagringsenheter;
                        $scope.state = 0;
                        $scope.formData = {
                            lagringsenheter: lagringsenheter
                        };
                        setFocus();
                    }).error(function (data, status, headers, config) {
                    $scope.formData.kjonn = kjonn;
                    setFeilmeldinger(data, status);
                });
            }
        };

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
            diagnoseService.getDiagnoserServer($scope.formDiagnose.diagnosedato, $scope.formDiagnose.diagnosekode,  function(diagnosekoder){
                
                if (diagnosekoder[$scope.formDiagnose.diagnosekode] && diagnosekoder[$scope.formDiagnose.diagnosekode].length > 1) {
                    //Viser en modal med en liste over valgene
                    var modal = modalService.velgModal('common/modal-service/liste-modal.tpl.html', diagnosekoder[$scope.formDiagnose.diagnosekode], $scope.formDiagnose);
                    modal.result.then(function () {
                        //Dersom teksten er satt, settes fokus på legg til diagnose
                        if ($scope.formDiagnose.diagnosetekst) {
                            if (laasDiagnosetekst){
                                $scope.diagnosetekstErSatt = true;
                            }
                            document.getElementById("btn-diagnose").focus();
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
                    document.getElementById("btn-diagnose").focus();
                    prevDiagnose = diagnosekode;
                } else {
                    //Ingen resultat på gitt kode
                    $scope.diagnosetekstErSatt = false;
                    prevDiagnose = "";
                }
            });

            
        };

        //Nullstiller diagnose skjema
        var resetDiagnose = function (keepDate) {
            var oldDiagnosedato = $scope.formDiagnose.diagnosedato;
            $scope.formDiagnose = {};
            $scope.diagnosetekstErSatt = false;
            $scope.diagnoseDatoErSatt = false;
            diagnosekode = "";

            if (keepDate){
                document.getElementById("diagnosedato").focus();

                $scope.formDiagnose.diagnosedato = oldDiagnosedato;
            }

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

            httpService.ny("pasientjournaler/" + $scope.pasientjournalDTO.persondata.uuid + "/diagnoser", $scope.formDiagnose)
                .success(function (data, status, headers, config) {
                    $scope.formDiagnose.uuid = data.uuid;
                    $scope.formDiagnose.oppdatertAv = data.oppdatertAv;
                    $scope.formDiagnose.oppdatertDato = data.oppdatertDato;
                    $scope.pasientjournalDTO.diagnoser.push($scope.formDiagnose);

                    resetDiagnose(true);
                }).error(function (data, status, headers, config) {
                if (status === 400) {
                    setFeilmeldinger(data, status);
                } else {
                    errorService.errorCode(status);
                }
            });
        };


        $scope.sokDiagnoseDisplayNameLike = function (displayName) {
            if (displayName.length > 2) {
                var results = [];
                var diagnoseDate = $scope.formDiagnose.diagnosedato===undefined?"": $scope.formDiagnose.diagnosedato;

                return httpService.hentAlle("diagnosekoder?displayNameLike=" + displayName+"&diagnoseDate="+diagnoseDate, false)
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
            httpService.slett("pasientjournaler/" + $scope.pasientjournalDTO.persondata.uuid + "/diagnoser", diagnose)
                .success(function (data, status, headers, config) {
                    for (var i = 0; i < $scope.pasientjournalDTO.diagnoser.length; i++) {
                        if (diagnose === $scope.pasientjournalDTO.diagnoser[i]) {
                            $scope.pasientjournalDTO.diagnoser.splice(i, 1);
                        }
                    }
                    resetDiagnose();
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
            var diagnosekoder = diagnoseService.getDiagnoser();
            diagnose.diagnosetekst = diagnosekoder[diagnose.diagnosekode].displayName;
        };

        $scope.update = function (diagnose) {
            //TODO
            diagnose.diagnosedato = $scope.injectCentury(diagnose.diagnosedato);
            $scope.feilmeldinger = [];
            httpService.oppdater("pasientjournaler/" + $scope.pasientjournalDTO.persondata.uuid + "/diagnoser", diagnose)
                .success(function (data, status, headers, config) {
                    $scope.editingData[diagnose.uuid] = false;
                })
                .error(function (data, status, headers, config) {
                    if (status === 400) {
                        setFeilmeldinger(data, status);
                    } else {
                        errorService.errorCode(status);
                    }
                });
        };
    });
