angular.module( 'nha.home', [
  'ui.router',
  'nha.common.http-service',
  'nha.common.error-service',
  'nha.common.list-service',
  'nha.common.modal-service'
])

.config(["$stateProvider", function config( $stateProvider ) {
  $stateProvider.state( 'home', {
    url: '/',
    views: {
      "main": {
        controller: 'HomeCtrl',
        templateUrl: 'home/home.tpl.html'
      }
    }
  });
}])

.controller( 'HomeCtrl', ["$scope", "$location", "$filter", "httpService", "errorService", "listService", "modalService", "$modal", function HomeController($scope, $location, $filter, httpService, errorService, listService, modalService, $modal) {
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

  $scope.actionFolder = function(id) {
    httpService.genererAvlevering()
    .success(function(data, status, headers, config) {
      conole.log("TODO: handle action folder");
    }).error(function(data, status, headers, config) {
      errorService.errorCode(status);
    });
  };

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
    modalService.deleteModal(elementType, id, $scope.avtaler, element, function() {
      httpService.deleteElement("avtaler/" + id);
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
    modalService.deleteModal(elementType, id, $scope.avleveringer, element, function() {
      httpService.deleteElement("avleveringer/" + id);
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

  //Util
  $scope.loggUt = function() {
    console.log("TODO: logg ut");
    $location.path('/login');
  };

  $scope.actionAdd = function() {
    $location.path('/registrer');
  };
}]);