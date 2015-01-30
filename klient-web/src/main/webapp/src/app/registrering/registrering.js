angular.module( 'nha.registrering', [
  'ui.router',
  'nha.registrering.registrering-service'
])

.config(["$stateProvider", function config( $stateProvider ) {
  $stateProvider.state( 'registrer', {
    url: '/registrer',
    views: {
      "main": {
        controller: 'RegistrerCtrl',
        templateUrl: 'registrering/registrering.tpl.html'
      }
    }
  });
}])

.controller( 'RegistrerCtrl', ["$scope", "$location", "$filter", "registreringService", function HomeController($scope, $location, $filter, registreringService) {
  console.log("TODO: Fjern hardkoding");

  $scope.$watch(
    function() { return $filter('translate')('home.SOK'); },
    function(newval) { $scope.sok = newval; }
  );

  $scope.avlevering = { // registreringService.getAvlevering();
    "avleveringsidentifikator" : "1234567",
    "avleveringsbeskrivelse" : "min første øæå",
    "avtale" : {
      "avtaleidentifikator" : "234567",
      "avtaledato" : "2015-01-20",
      "avtalebeskrivelse" : "Avtale 1"
    },
    "arkivskaper" : "Robin",
    "pasientjournal" : []
  };
  $scope.kjonn = [{"tekst" : "Mann"}, {"tekst" : "Kvinne"}];
  var hoppOver = false;

  $scope.lagringsenheter = [];
  $scope.keyAddLagringsenhet = function() {
    if($scope.lagringsenhet === undefined || $scope.lagringsenhet === '') {
      return;
    }
    for(var i = 0; i < $scope.lagringsenheter.length; i++) {
      if($scope.lagringsenhet === $scope.lagringsenheter[i]) {
        $scope.lagringsenhet = "";
        return;
      }
    }

    $scope.lagringsenheter.push($scope.lagringsenhet);
    $scope.lagringsenhet = "";
  };

  $scope.actionFjernLagringsenhet = function(enhet) {
    for(var i = 0; i < $scope.lagringsenheter.length; i++) {
      if(enhet === $scope.lagringsenheter[i]) {
        $scope.lagringsenheter.splice(i, 1);
        document.getElementById("lagringsenhet").focus();
      }
    }
  };

  var fnr;
  $scope.setFnr = function() {
    if($scope.formData && $scope.formData.fnr) {
      fnr = $scope.formData.fnr;  
    }
  };

  var validerFodselsnr = function(fnr) {
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

  $scope.populerFelt = function() {
    if($scope.formData.fnr === undefined || fnr === $scope.formData.fnr || $scope.formData.fnr.length != 11) {
      return;
    }

    //Valider nr
    var aarhundre = validerFodselsnr($scope.formData.fnr);
    if(!aarhundre) {
      return;
    }

    var kjonnValidert = false, datoValidert = false;
    if($scope.formData.fnr.length == 11) {
      if(($scope.formData.fnr % 2) === 0) {
        $scope.formData.valgtKjonn = $scope.kjonn[0];
        kjonnValidert = true;
      } else {
        $scope.formData.valgtKjonn = $scope.kjonn[1];
        kjonnValidert = true;
      }

      //Valider dato
      var fdato = $scope.formData.fnr.substring(0, 6);
      var dag = Number(fdato.substring(0,2));
      var mnd = Number(fdato.substring(2,4));
      var aar = Number(fdato.substring(4,6));

      if(dag > 0 && dag < 32 && mnd > 0 && mnd < 13 && aar > 0) {
        datoValidert = true;
        $scope.formData.fdato = fdato.substring(0,2) + "." + fdato.substring(2,4) + "." + aarhundre + aar; 
      }
    }

    hoppOver = kjonnValidert && datoValidert;

  };

  $scope.setFocusEtterNavn = function() {
    if($scope.formData !== undefined) {
      if($scope.formData.navn.length > 0) {
        $scope.formData.navn = $scope.formData.navn.substring(0,1).toUpperCase() + $scope.formData.navn.substring(1);
      }  
    }

    if(hoppOver) {
      document.getElementById("ddato").focus();
    }
    hoppOver = false;
  };

  $scope.submit = function() {

    console.log($scope.formData);

  };
}]);