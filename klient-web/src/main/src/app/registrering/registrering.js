angular.module( 'nha.registrering', [
  'ui.router',
  'nha.common.http-service',
  'nha.common.error-service',
  'nha.registrering.registrering-service',
  'nha.common.diagnose-service',
  'nha.common.modal-service',
  'cfp.hotkeys'
])

.config(function config( $stateProvider ) {
  $stateProvider.state( 'registrer', {
    url: '/registrer',
    views: {
      "main": {
        controller: 'RegistrerCtrl',
        templateUrl: 'registrering/registrering.tpl.html'
      }
    }
  });
})

.controller( 'RegistrerCtrl', function HomeController($scope, $location, $filter, httpService, errorService, registreringService, diagnoseService, hotkeys, modalService, $window) {
  //Util
  $scope.navHome = function() {
    history.back();
  };
  $scope.loggUt = function() {
    $location.path('/login');
  };

  //Hotkeylistener for ctrl+n og ctrl+s
  $window.onkeydown = function(event) {
    var keycode = event.charCode || event.keyCode;
    if(!event.ctrlKey) {
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

/*
  hotkeys.bindTo($scope)
  .add({
  combo:'alt+s',
  description:'Lagre',
  callback: function(){ 
    $scope.nyEllerOppdater();
  }
  })
  .add({
  combo:'alt+n',
  description:'ny journal',
  callback: function(){
    $scope.nyJournal();
  }
  });
*/

  //Setter verdier for å sørge for at undefined (null) blir håndtert riktig
  $scope.feilmeldinger = [];
  $scope.error = [];
  $scope.kjonn = [{kode: "M", tekst : ""}, {kode: "K", tekst : ""}, {kode: "U", tekst : ""}, {kode: "I", tekst : ""}];
  $scope.state = 0; //0 = ny, 1 = legg til diagnoser, 2 = endre
        $scope.prevState = 0;
  var hoppOver = false;

    //Tekster fra i18n
    $scope.$watch(
      function() { return $filter('translate')('registrer.kjonn.MANN'); },
      function(newval) { $scope.kjonn[0].tekst = newval; }
    );
    $scope.$watch(
      function() { return $filter('translate')('registrer.kjonn.KVINNE'); },
      function(newval) { $scope.kjonn[1].tekst = newval; }
    );
    $scope.$watch(
      function() { return $filter('translate')('registrer.kjonn.IKKE_KJENT'); },
      function(newval) { $scope.kjonn[2].tekst = newval; }
    );
    $scope.$watch(
      function() { return $filter('translate')('registrer.kjonn.IKKE_SPESIFISERT'); },
      function(newval) { $scope.kjonn[3].tekst = newval; }
    );

    //Feil tekster
    $scope.feilTekster = {};
    $scope.$watch(
      function() { return $filter('translate')('feltfeil.NotNull'); },
      function(newval) { $scope.feilTekster['NotNull'] = newval; }
    );
    $scope.$watch(
      function() { return $filter('translate')('feltfeil.DagEllerAar'); },
      function(newval) { $scope.feilTekster['DagEllerAar'] = newval; }
    );
    $scope.$watch(
      function() { return $filter('translate')('feltfeil.NotUnique'); },
      function(newval) { $scope.feilTekster['NotUnique'] = newval; }
    );
    $scope.$watch(
      function() { return $filter('translate')('feltfeil.Size'); },
      function(newval) { $scope.feilTekster['Size'] = newval; }
    );
    $scope.$watch(
      function() { return $filter('translate')('feltfeil.FodtEtterDodt'); },
      function(newval) { $scope.feilTekster['FodtEtterDodt'] = newval; }
    );
    $scope.$watch(
        function() { return $filter('translate')('feltfeil.FeilFodselsnummer'); },
        function(newval) { $scope.feilTekster['FeilFodselsnummer'] = newval; }
    );
    $scope.$watch(
        function() { return $filter('translate')('feltfeil.EnObligatorisk'); },
        function(newval) { $scope.feilTekster['EnObligatorisk'] = newval; }
    );
    $scope.$watch(
      function() { return $filter('translate')('feltfeil.fKontaktForFodt'); },
      function(newval) { $scope.feilTekster['fKontaktForFodt'] = newval; }
    );
    $scope.$watch(
      function() { return $filter('translate')('feltfeil.sKontaktForFodt'); },
      function(newval) { $scope.feilTekster['sKontaktForFodt'] = newval; }
    );
    $scope.$watch(
      function() { return $filter('translate')('feltfeil.fKontaktEtterDod'); },
      function(newval) { $scope.feilTekster['fKontaktEtterDod'] = newval; }
    );
    $scope.$watch(
      function() { return $filter('translate')('feltfeil.sKontaktEtterDod'); },
      function(newval) { $scope.feilTekster['sKontaktEtterDod'] = newval; }
    );
    $scope.$watch(
      function() { return $filter('translate')('feltfeil.fKontaktEttersKontakt'); },
      function(newval) { $scope.feilTekster['fKontaktEttersKontakt'] = newval; }
    );

  $scope.formData = {
    lagringsenheter : []
  };
  $scope.formDiagnose = {};
  $scope.avlevering = registreringService.getAvlevering();
  $scope.pasientjournalDTO = registreringService.getPasientjournalDTO();
  $scope.avleveringsidentifikator = registreringService.getAvleveringsidentifikator();

  //Setter verdier fra registrering-service
  if($scope.avlevering !== undefined) {
    //Ny pasientjouranl
    $scope.state = 0;
  } else 
  if($scope.pasientjournalDTO !== undefined) {
    //Endre pasientjournal
    $scope.state = 2;

    $scope.formData = $scope.pasientjournalDTO.persondata;


    //Håndtering av kjønn - Sender kode til server, viser Tekst basert på i18n
    if($scope.formData.kjonn !== undefined) {
      var funnet = false;
      for(var i = 0; i < $scope.kjonn.length; i++) {
        if($scope.kjonn[i].kode === $scope.formData.kjonn) {
          $scope.formData.kjonn = $scope.kjonn[i];
          funnet = true;
        }
      }
      if(!funnet) {
        $scope.formData.kjonn = {
          "kode" : $scope.formData.kjonn,
          "tekst" : " "
        };
      }
    }
  } else {
    //Ingen verdier er satt, naviger til home
    $scope.navHome();
  }

  //Hjelpemetode for å sette focus på rett felt
  var setFocus = function() {
    //Ny
    if($scope.state === 0) {
      if($scope.formData.lagringsenheter.length !== 0) {
        document.getElementById("journalnummerInput").focus();
      } else {
        document.getElementById("lagringsenhet").focus();
      }  
    } else
    //Legg til diagnoser
    if($scope.state === 1) {
      document.getElementById("diagnoseDato").focus();
    } else
    //Oppdater
    if($scope.state === 2) {
      document.getElementById("lagringsenhet").focus();
    }
  };
  setFocus();

  $scope.keyAddLagringsenhet = function() {
    if($scope.lagringsenhet === undefined || $scope.lagringsenhet === '') {
      return;
    }
    if($scope.formData.lagringsenheter === undefined || $scope.formData.lagringsenheter === null) {
      $scope.formData.lagringsenheter = [];
    }

    for(var i = 0; i < $scope.formData.lagringsenheter.length; i++) {
      if($scope.lagringsenhet === $scope.formData.lagringsenheter[i]) {
        $scope.lagringsenhet = "";
        return;
      }
    }
    $scope.formData.lagringsenheter.push($scope.lagringsenhet);
    $scope.lagringsenhet = "";
  };

  $scope.actionFjernLagringsenhet = function(enhet) {
    for(var i = 0; i < $scope.formData.lagringsenheter.length; i++) {
      if(enhet === $scope.formData.lagringsenheter[i]) {
        $scope.formData.lagringsenheter.splice(i, 1);
        document.getElementById("lagringsenhet").focus();
      }
    }
  };

  var fodselsnummer;
  $scope.setFnr = function() {
    if($scope.formData && $scope.formData.fodselsnummer) {
      fodselsnummer = $scope.formData.fodselsnummer;
    }
  };

  var getAarhundreFromFnr = function(fnr) {
      var individ = Number(fnr.substring(6,9));
      var aar = Number(fnr.substring(5,6));

      //var i18551899 =
      if ((individ >= 500 && individ <= 749) && aar > 54){
          //1800
          return 18;
      }
      if (individ >= 0 && individ <= 499){
          //1900
          return 19;
      }
      if ((individ >= 900 && individ <= 999) && aar > 39){
          //1900
          return 19;
      }
      if ((individ >= 500 && individ <= 999) && aar < 40){
          //2000
          return 20;
      }
      

/*
    var personnr = fnr.substring(6,9);
    var i = Number(personnr.substring(0,1));
    var nr = Number(personnr.substring(1,3));


    if(i === 0) {
      if(nr <= 39) {
        return 20;
      } else {
        return 19;
      }
    }
    if(i >= 1 && i <= 4) {
      return 19;
    }
    if(i >= 5 && i <= 7) {
      if(nr <= 54) {
        return 20;
      } else {
        return 18;
      }
    }
    if(i === 8 && nr <= 54) {
      return 20;
    }
    if(i === 9) {
      if(nr <= 39) {
        return 20;
      } else {
        return 19;
      }
    }*/
  };

  kjonnFromFodselsnummer = function(fnr){
      var individsifre = fnr.substring(6,9);
      var kjonn = Number(individsifre.substring(2,3));
      return kjonn%2===0?$scope.kjonn[1]:$scope.kjonn[0];
  };

  $scope.populerFelt = function() {
    if($scope.formData.fodselsnummer === undefined || fodselsnummer === $scope.formData.fodselsnummer || $scope.formData.fodselsnummer.length != 11) {
      return;
    }

    //Valider nr
    var aarhundre = getAarhundreFromFnr($scope.formData.fodselsnummer);
    if(!aarhundre) {
      return;
    }

    var kjonnValidert = false, datoValidert = false;
    if($scope.formData.fodselsnummer.length == 11) {

        var kjonn = kjonnFromFodselsnummer($scope.formData.fodselsnummer);
        if (kjonn) {
            $scope.formData.kjonn = kjonn;
            kjonnValidert = true;
        }
    }

      //Valider dato
      var fdato = $scope.formData.fodselsnummer.substring(0, 6);

        var d1 = Number(fdato.substring(0,1));//for d-nummersjekk
        var d2 = Number(fdato.substring(1,2));
        var m1 = Number(fdato.substring(2,3));//for h-nummersjekk
        var m2 = Number(fdato.substring(3, 4));
        var y1 = Number(fdato.substring(4,5));
        var y2 = Number(fdato.substring(5,6));
        var dag=d1+""+d2, mnd=m1+""+m2, aar;

        if (d1>3){ //indikerer d-nummer
            dag = (d1-4)+""+d2;
            mnd = m1+""+m2;
        }
        if (m1>3){ //indikerer h-nummer
            mnd = (m1-4)+""+m2;
            dag = d1+""+d2;
        }
        aar = y1+""+y2;

      if(dag > 0 && dag < 32 && mnd > 0 && mnd < 13 && aar > 0) {
        datoValidert = true;
        $scope.formData.fodt = dag + "." + mnd + "." + aarhundre + aar;
      }


    hoppOver = kjonnValidert && datoValidert;
  };

  $scope.setFocusEtterNavn = function() {
    if($scope.formData !== undefined) {
      if($scope.formData.navn !== undefined && $scope.formData.navn.length > 0) {
        $scope.formData.navn = $scope.formData.navn.substring(0,1).toUpperCase() + $scope.formData.navn.substring(1);
      }  
    }

    if(hoppOver) {
      document.getElementById("ddato").focus();
    }
    hoppOver = false;
  };

  var getDagEllerAar = function(verdi) {
    if(verdi === undefined) {
      return {
        dato : undefined,
        aar : undefined
      };
    }
    if(verdi.indexOf(".") > -1) {
      return {
        dato : verdi,
        aar : undefined
      };
    } else {
      return {
        dato : undefined,
        aar : verdi
      };
    }
  };

  function compare(a,b) {
      if (a.indeks < b.indeks) {
         return -1;
      }
      if (a.indeks > b.indeks) {
        return 1;
      }
      return 0;
  }

  var setFeilmeldinger = function(data, status) {
    if(status != 400) {
      errorService.errorCode(status);
      return;
    }

    angular.forEach(data, function(element) {
      $scope.error[element.attributt] = element.constriant;

      var index;
      var felt;
      var feil;

      //Konverter attributt
      if(element.attributt === 'lagringsenheter') {
        index = 0;
        felt = document.getElementById('labelLagringsenhet').innerHTML;
      }
      if(element.attributt === 'journalnummer') {
        index = 1;
        felt = document.getElementById('journalnummer').innerHTML;
      }
      if(element.attributt === 'lopenummer') {
        index = 2;
        felt = document.getElementById('lopenummer').innerHTML;
      }
      if(element.attributt === 'fodselsnummer') {
        index = 3;
        felt = document.getElementById('fodselsnummer').innerHTML;
      }
      if(element.attributt === 'navn') {
        index = 4;
        felt = document.getElementById('navn').innerHTML;
      }
      if(element.attributt === 'kjonn') {
        index = 5;
        felt = document.getElementById('kjonn').innerHTML;
      }
      if(element.attributt === 'fodt') {
        index = 6;
        felt = document.getElementById('fodt').innerHTML;
      }
      if(element.attributt === 'dod') {
        index = 7;
        felt = document.getElementById('dod').innerHTML;
      }
      if(element.attributt === 'fKontakt') {
        index = 8;
        felt = document.getElementById('fKontakt').innerHTML;
      }
      if(element.attributt === 'sKontakt') {
        index = 9;
        felt = document.getElementById('sKontakt').innerHTML;
      }
      if(element.attributt === 'diagnosedato') {
        index = 10;
        felt = document.getElementById('diagnosedato').innerHTML;
      }
        if(element.attributt === 'diagnosedatotab') {
            index = 10;
            felt = document.getElementById('diagnosedato_table').innerHTML;
        }
      if(element.attributt === 'diagnosekode') {
        index = 11;
        felt = document.getElementById('diagnosekode').innerHTML;
      }    
      if(element.attributt === 'diagnosetekst') {
        index = 12;
        felt = document.getElementById('diagnosetekst').innerHTML;
      }         
                         
      if(felt !== undefined) {
        $scope.feilmeldinger.push({
          indeks : index,
          felt : felt,
          feilmelding : $scope.feilTekster[element.constriant]
        });  
      }
    });
    $scope.feilmeldinger.sort(compare);
  };
    
  //setter state til endre(2) og kjører en oppdatering
  $scope.nyJournal = function(){
    $scope.prevState = $scope.state;
    $scope.state = 3;
    $scope.nyEllerOppdater();
  };

  $scope.nyEllerOppdater = function() {
    $scope.error = {};
    $scope.feilmeldinger = [];

    if($scope.formData.lagringsenheter !== undefined && $scope.formData.lagringsenheter.length === 0) {
      delete $scope.formData.lagringsenheter;
    }

    var kjonn = $scope.formData.kjonn;
    if(kjonn !== undefined) {
      $scope.formData.kjonn = $scope.formData.kjonn.kode;
    }

    //NY
    if($scope.state === 0) {
      httpService.ny("avleveringer/" + $scope.avleveringsidentifikator + "/pasientjournaler", $scope.formData)
      .success(function(data, status, headers, config) {
        $scope.pasientjournalDTO = data;
        $scope.formData = data.persondata;
        $scope.formData.kjonn = kjonn;
        $scope.state = 2;
      }).error(function(data, status, headers, config) {
        $scope.formData.kjonn = kjonn;
        setFeilmeldinger(data, status);  
      });
    }

    //Endre
    if($scope.state === 2) {
      httpService.oppdater("pasientjournaler/", $scope.pasientjournalDTO)
      .success(function(data, status, headers, config) {
        var lagringsenheter = $scope.formData.lagringsenheter;
        $scope.pasientjournalDTO = data;
        $scope.formData = data.persondata;
        $scope.formData.kjonn = kjonn;
        $scope.formData.lagringsenheter = lagringsenheter;
        $scope.state = 2;
        setFocus();
      }).error(function(data, status, headers, config) {
        $scope.formData.kjonn = kjonn;
        setFeilmeldinger(data, status); 
      });
    }
      //start en ny journal
      if($scope.state === 3) {
          $scope.state = $scope.prevState;
          if (!$scope.pasientjournalDTO){
              return;
          }
          httpService.oppdater("pasientjournaler/", $scope.pasientjournalDTO)
              .success(function(data, status, headers, config) {
                  var lagringsenheter = $scope.formData.lagringsenheter;
                  $scope.state = 0;
                  $scope.formData = {
                   lagringsenheter : lagringsenheter
                   };
                  setFocus();
              }).error(function(data, status, headers, config) {
                  $scope.formData.kjonn = kjonn;
                  setFeilmeldinger(data, status);
              });
      }
  };

  var diagnosekode = "";
  $scope.diagnosetekstErSatt = false;
  //Tar vare på verdi ved fokus, for å sammenligne etterpå, for å ikke endre teksten
  $scope.setDiagnoseKode = function() {
    if($scope.formDiagnose === null || $scope.formDiagnose.diagnosekode === null) {
      return;
    }
    diagnose = $scope.formDiagnose.diagnosekode;
  };

  //Setter diagnoseteksten når koden er endret
  $scope.setDiagnoseTekst = function() {
    //Hvis koden ikke er endret
    if(diagnosekode === $scope.formDiagnose.diagnosekode) {
      return;
    }
    diagnosekode = $scope.formDiagnose.diagnosekode;
    
    //Setter koden til upper case
    if($scope.formDiagnose.diagnosekode) {
      $scope.formDiagnose.diagnosekode = $scope.formDiagnose.diagnosekode.toUpperCase();  
    }
    
    //Henter alle diagnoser fra tjenesten
    var diagnosekoder = diagnoseService.getDiagnoser();

    //Hvis vi har flere matcher på samme id (fra flere kodeverk)
    if(diagnosekoder[$scope.formDiagnose.diagnosekode] && diagnosekoder[$scope.formDiagnose.diagnosekode].length > 1) {
      //Viser en modal med en liste over valgene
      var modal = modalService.velgModal('common/modal-service/liste-modal.tpl.html', 
          diagnosekoder[$scope.formDiagnose.diagnosekode], 
          $scope.formDiagnose);
      modal.result.then(function() {
        //Dersom teksten er satt, settes fokus på legg til diagnose    
        if($scope.formDiagnose.diagnosetekst) {
          $scope.diagnosetekstErSatt = true;
          document.getElementById("btn-diagnose").focus();
        } else {
          $scope.diagnosetekstErSatt = false;
          document.getElementById("diagnosekode-input").focus();
        }
      }, function() {
        document.getElementById("diagnosekode-input").focus();
      });
    } else if(diagnosekoder[$scope.formDiagnose.diagnosekode]) {
      //En diagnose med gitt verdi
      $scope.formDiagnose.diagnosetekst = diagnosekoder[$scope.formDiagnose.diagnosekode][0].displayName;
      $scope.formDiagnose.diagnosekodeverk = diagnosekoder[$scope.formDiagnose.diagnosekode][0].codeSystemVersion;

      $scope.diagnosetekstErSatt = true;
      document.getElementById("btn-diagnose").focus();
    } else {
      //Ingen resultat på gitt kode
      $scope.diagnosetekstErSatt = false;
    }
  };

  //Nullstiller diagnose skjema
  var resetDiagnose = function() {
    $scope.formDiagnose = {};
    $scope.diagnosetekstErSatt = false;
    diagnosekode = "";
    document.getElementById("diagnoseDato").focus();

  };

  //Legger til diagnose i skjema
  $scope.leggTilDiagnose = function() {
    $scope.error = {};
    $scope.feilmeldinger = [];
    if (!$scope.formDiagnose.diagnosekode){
        $scope.formDiagnose.diagnosekode=null;
    }
    if ($scope.formDiagnose.diagnosekode===''){
        $scope.formDiagnose.diagnosekode=null;
    }

    if($scope.pasientjournalDTO.diagnoser == null) {
      $scope.pasientjournalDTO.diagnoser = [];
    }

    httpService.ny("pasientjournaler/" + $scope.pasientjournalDTO.persondata.uuid + "/diagnoser", $scope.formDiagnose)
    .success(function(data, status, headers, config) {
      $scope.formDiagnose.uuid=data.uuid;
      $scope.formDiagnose.oppdatertAv = data.oppdatertAv;
      $scope.pasientjournalDTO.diagnoser.push($scope.formDiagnose);

      resetDiagnose();
    }).error(function(data, status, headers, config) {
      if(status === 400) {
        setFeilmeldinger(data, status);   
      } else {
        errorService.errorCode(status);
      }
    });
  };


  $scope.sokDiagnoseDisplayNameLike =function(displayName){
      if (displayName.length > 2) {
          var results = [];
          return httpService.hentAlle("diagnosekoder?displayNameLike=" + displayName, false)
              .then(function (resp) {
                  return resp.data.map(function (item) {
                      var res = [];
                      res.code = item.code;
                      res.displayName = item.codeSystemVersion+" | "+ item.displayName;
                      return res;
                  });
              });
      }
  };

  $scope.onSelectDiagnose = function($item, $model, $label){
        $scope.formDiagnose.diagnosekode= $item.code;
        $scope.formDiagnose.diagnosetekst=null;
        $scope.setDiagnoseTekst();
  };


  //Fjerner diagnose
  $scope.fjernDiagnose = function(diagnose) {
  if (diagnose.diagnosekode===''){
    delete diagnose.diagnosekode;
  }
    httpService.slett("pasientjournaler/" + $scope.pasientjournalDTO.persondata.uuid + "/diagnoser", diagnose)
    .success(function(data, status, headers, config) {
      for(var i = 0; i < $scope.pasientjournalDTO.diagnoser.length; i++) {
        if(diagnose === $scope.pasientjournalDTO.diagnoser[i]) {
            $scope.pasientjournalDTO.diagnoser.splice(i, 1);
        }
      }
      resetDiagnose();
    }).error(function(data, status, headers, config) {
        errorService.errorCode(status);
    });

  };


    $scope.editingData = [];

    $scope.editDiagnose = function(){
        for (var i = 0, length = $scope.pasientjournalDTO.diagnoser.length; i < length; i++) {
            $scope.editingData[$scope.pasientjournalDTO.diagnoser[i].id] = false;
        }
    };
    $scope.modify = function(diagnose){
        if ($scope.editingData.length===0){
            $scope.editDiagnose();
        }
        $scope.editingData[diagnose.uuid] = true;
    };
    $scope.editDiagnosekode = function(diagnose){
        diagnose.diagnosekode = diagnose.diagnosekode.toUpperCase();
        var diagnosekoder = diagnoseService.getDiagnoser();
        diagnose.diagnosetekst = diagnosekoder[diagnose.diagnosekode].displayName;
    };

    $scope.update = function(diagnose){
        $scope.feilmeldinger = [];
        httpService.oppdater("pasientjournaler/"+$scope.pasientjournalDTO.persondata.uuid+"/diagnoser", diagnose)
            .success(function(data, status, headers, config){
                $scope.editingData[diagnose.uuid] = false;
            })
            .error(function(data, status, headers, config){
                if(status === 400) {
                    setFeilmeldinger(data, status);
                } else {
                    errorService.errorCode(status);
                }
            });
    };

});