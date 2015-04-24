angular.module( 'nha.registrering', [
  'ui.router',
  'nha.common.http-service',
  'nha.common.error-service',
  'nha.registrering.registrering-service',
  'nha.common.diagnose-service'
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

.controller( 'RegistrerCtrl', function HomeController($scope, $location, $filter, httpService, errorService, registreringService, diagnoseService) {
  //Util
  $scope.navHome = function() {
    history.back();
  };
  $scope.loggUt = function() {
    $location.path('/login');
  };

  //Setter verdier for å sørge for at undefined (null) blir håndtert riktig
  $scope.feilmeldinger = [];
  $scope.error = [];
  $scope.kjonn = [{kode: "M", tekst : ""}, {kode: "K", tekst : ""}, {kode: "U", tekst : ""}, {kode: "I", tekst : ""}];
  $scope.state = 0; //0 = ny, 1 = legg til diagnoser, 2 = endre
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
    }
  };

kjonnFromFodselsnummer = function(fnr){
    var individsifre = fnr.substring(6,9);
    var kjonn = Number(individsifre.substring(2,3));
    return kjonn%2===0?$scope.kjonn[1]:$scope.kjonn[0];
};

gyldigFodselsnummer = function (fnr) {
  var faktor1 = [3, 7, 6, 1, 8, 9, 4, 5, 2];
  var faktor2 = [5, 4, 3, 2, 7, 6, 5, 4, 3];
  var nestSisteFnrSiffer = fnr.charAt(9);
  var sisteFnrSiffer = fnr.charAt(10);
 
  var summerSjekksum = function (sum, verdi, index) {
    var ettSiffer = fnr.charAt(index);
    return sum + verdi * ettSiffer;
  };
 
  var finnKontrollSiffer = function(sjekksum) {
    var kontrollSiffer = 11 - (sjekksum % 11);
    return kontrollSiffer == 11 ? 0 : kontrollSiffer;
  };
 
  var forsteSjekksum = _.reduce(faktor1, summerSjekksum, 0);
  var forsteKontrollsiffer = finnKontrollSiffer(forsteSjekksum);
 
  var andreSjekksum = _.reduce(faktor2, summerSjekksum, forsteKontrollsiffer*2);
  var andreKontrollsiffer = finnKontrollSiffer(andreSjekksum);
 
  return forsteKontrollsiffer == nestSisteFnrSiffer && andreKontrollsiffer == sisteFnrSiffer;
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
    if (gyldigFodselsnummer($scope.formData.fodselsnummer)){
    //if($scope.formData.fodselsnummer.length == 11) {
    var kjonn = kjonnFromFodselsnummer($scope.formData.fodselsnummer);
    if (kjonn){
        $scope.formData.kjonn = kjonn;
        kjonnValidert = true;
    }
    /*
      if(($scope.formData.fodselsnummer % 2) === 0) {
        $scope.formData.kjonn = $scope.kjonn[0];
        kjonnValidert = true;
      } else {
        $scope.formData.kjonn = $scope.kjonn[1];
        kjonnValidert = true;
      }
*/
      //Valider dato
      var fdato = $scope.formData.fodselsnummer.substring(0, 6);
      var dag = Number(fdato.substring(0,2));
      var mnd = Number(fdato.substring(2,4));
      var aar = Number(fdato.substring(4,6));

      if(dag > 0 && dag < 32 && mnd > 0 && mnd < 13 && aar > 0) {
        datoValidert = true;
        $scope.formData.fodt = fdato.substring(0,2) + "." + fdato.substring(2,4) + "." + aarhundre + fdato.substring(4,6); 
      }
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
    if($scope.formDiagnose.diagnosekode === diagnosekode) {
      return;
    }
    var diagnosekoder = diagnoseService.getDiagnoser();
    $scope.formDiagnose.diagnosetekst = diagnosekoder[$scope.formDiagnose.diagnosekode];
    if($scope.formDiagnose.diagnosetekst) {
      $scope.diagnosetekstErSatt = true;
      document.getElementById("btn-diagnose").focus();
    } else {
      $scope.diagnosetekstErSatt = false;
    }
  };

  //Nullstiller diagnose skjema
  var resetDiagnose = function() {
    $scope.formDiagnose = {};
    $scope.diagnosetekstErSatt = false;
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
      var results = [];
      return httpService.hentAlle("diagnosekoder?displayNameLike="+displayName, false)
      .then(function(resp){
        return resp.data.map(function(item){
            var res = [];
            res.code = item.code;
            res.displayName = item.displayName;
            return res;
        });
      });
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

});