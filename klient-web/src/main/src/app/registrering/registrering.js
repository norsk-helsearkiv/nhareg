angular.module( 'nha.registrering', [
  'ui.router',
  'nha.registrering.registrering-service'
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

.controller( 'RegistrerCtrl', function HomeController($scope, $location, $filter, registreringService) {
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
  console.log("TODO: Fjern hardkoding");
  
  $scope.kjonn = [{"tekst" : "Mann"}, {"tekst" : "Dame"}];

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

  $scope.submit = function() {
    console.log("Legger til pasientjournal i avlevering");
  };
});