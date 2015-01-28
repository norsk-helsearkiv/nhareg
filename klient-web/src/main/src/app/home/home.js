angular.module( 'nha.home', [
  'ui.router',
  'nha.common.http-service',
  'nha.common.error-service',
  'nha.common.list-service',
  'nha.common.modal-service',
  'nha.registrering.registrering-service'
])

.config(function config( $stateProvider ) {
  $stateProvider.state( 'home', {
    url: '/',
    views: {
      "main": {
        controller: 'HomeCtrl',
        templateUrl: 'home/home.tpl.html'
      }
    }
  });
})

.controller( 'HomeCtrl', function HomeController($scope, $location, $filter, httpService, errorService, listService, modalService, registreringService, $modal) {
  //Tekster i vinduet lastet fra kontroller
    $scope.text = {
      "tooltip" : {}
    };
    $scope.$watch(
      function() { return $filter('translate')('home.PASIENTSOK'); },
      function(newval) { $scope.text.pasientsok = newval; }
    );
    $scope.$watch(
      function() { return $filter('translate')('home.AVLEVERING'); },
      function(newval) { $scope.text.avlevering = newval; }
    );
    $scope.$watch(
      function() { return $filter('translate')('home.AVTALE'); },
      function(newval) { $scope.text.avtale = newval; }
    );
    $scope.$watch(
      function() { return $filter('translate')('home.tooltip.LIST'); },
      function(newval) { $scope.text.tooltip.list = newval; }
    );
    $scope.$watch(
      function() { return $filter('translate')('home.tooltip.ADD'); },
      function(newval) { $scope.text.tooltip.add = newval; }
    );
    $scope.$watch(
      function() { return $filter('translate')('home.tooltip.FOLDER'); },
      function(newval) { $scope.text.tooltip.folder = newval; }
    );
    $scope.$watch(
      function() { return $filter('translate')('home.tooltip.DELETE'); },
      function(newval) { $scope.text.tooltip.deleteElement = newval; }
    );

  httpService.hentAlle("avtaler", false)
  .success(function(data, status, headers, config) {
    $scope.avtaler = data;
    $scope.setValgtAvtale(data[0]); 
  }).error(function(data, status, headers, config) {
    errorService.errorCode(status);
  });

  $scope.actionSok = function(sokestring) {
    console.log("TODO: Implementere søk");

    var tittel = {
      "tittel" : "Søkeresultat",
      "underTittel" : "viser 4 av 4 resultat"
    };

    var data = {
      "total" : 4,
      "side": 1,
      "sideantall": 4,
      "pasientjournal" : [
        {
          "personnummer": "12345678901",
          "navn" : "Batman"
        },
        {
          "personnummer": "22345678901",
          "navn" : "Robin"
        },
        {
          "personnummer": "32345678901",
          "navn" : "Joker"
        },
        {
        "personnummer": "42345678901",
        "navn" : "Harley Quinn"
        }
      ]
    };

    listService.init(tittel, data);
    $location.path('/list');

  };

  //Avtale
  $scope.setValgtAvtale = function(avtale) {
    httpService.hentAlle("avtaler/" + avtale.avtaleidentifikator + "/avleveringer", false)
    .success(function(data, status, headers, config) {
      $scope.avleveringer = data;
      $scope.valgtAvtale = avtale;
    }).error(function(data, status, headers, config) {
      errorService.errorCode(status);
    });
  };

  $scope.actionDeleteAvtale = function(elementType, id, element) {
    modalService.deleteModal(elementType, id, function() {
      httpService.deleteElement("avtaler/" + id)
      .success(function(data, status, headers, config) {
        fjern($scope.avtaler, element);
        $scope.setValgtAvtale($scope.avtaler[0]);
      }).error(function(data, status, headers, config) {
        errorService.errorCode(status);
      });
    });
  };

  $scope.actionLeggTilAvtale = function() {
    modalService.nyModal('common/modal-service/ny-avtale.tpl.html', $scope.avtaler, "avtaler", function(formData) {
      formData.error = {};
      var success = true;

      if(formData.avtaleidentifikator === undefined || formData.avtaleidentifikator === ''){
        formData.error.avtaleidentifikator = "ID kan ikke være tom";
        success = false;
      }
      if(formData.avtalebeskrivelse === undefined || formData.avtalebeskrivelse === ''){
        formData.error.avtalebeskrivelse = "Beskrivelsen kan ikke være tom";
        success = false;
      }
      if(formData.avtaledato === undefined || formData.avtaledato === ''){
        formData.error.avtaledato = "Dato må være satt";
        success = false;
      }

      if(success) {
        formData.error = undefined;
      }
      return success;
    });
  };

  //Avlevering
  $scope.actionLeggTilAvlevering = function() {
    modalService.nyModal('common/modal-service/ny-avlevering.tpl.html', $scope.avleveringer, "avleveringer", function(formData) {
      formData.error = {};
      var success = true;

      if(formData.avleveringsidentifikator === undefined || formData.avleveringsidentifikator === ''){
        formData.error.avleveringsidentifikator = "ID kan ikke være tom";
        success = false;
      }

      if(success) {
        formData.error = undefined;
        formData.avtale = $scope.valgtAvtale;
      }
      return success;
    });
  };

  $scope.actionFjernAvlevering = function(elementType, id, element) {
    modalService.deleteModal(elementType, id, function() {
      httpService.deleteElement("avleveringer/" + id)
      .success(function(data, status, headers, config) {
        fjern($scope.avleveringer, element);
      }).error(function(data, status, headers, config) {
        errorService.errorCode(status);
      });
    });
  };

  $scope.actionVisAvlevering = function(avlevering) {
    httpService.hentAlle("avleveringer/" + avlevering.avleveringsidentifikator + "/pasientjournaler")
    .success(function(data, status, headers, config) {
    
      var tittel = {
        "tittel" : avlevering.avleveringsbeskrivelse,
        "underTittel" : avlevering.arkivskaper
      };
      listService.init(tittel, data);
      $location.path('/list');
    
    }).error(function(data, status, headers, config) {
      errorService.errorCode(status);
    });
  };

  $scope.actionAvleveringLeveranse = function(avlevering) {
    window.location = httpService.getRoot() + "avleveringer/" + avlevering.avleveringsidentifikator + "/leveranse";
  };

  //Util
  $scope.loggUt = function() {
    console.log("TODO: logg ut");
    $location.path('/login');
  };

  $scope.actionLeggTilPasientjournald = function(avlevering) {
    registreringService.setAvlevering(avlevering);
    $location.path('/registrer');
  };

  //Hjelpe metode for å fjerne fra liste
  var fjern = function(list, element) {
    for(var i = 0; i < list.length; i++) {
      if(element === list[i]) {
          list.splice(i, 1);
      }
    }
  };

});