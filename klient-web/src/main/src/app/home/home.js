angular.module( 'nha.home', [
  'ui.router',
  'nha.common.http-service',
  'nha.common.error-service',
  'nha.common.list-service',
  'nha.common.modal-service',
  'nha.home.avleveringer-service'
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

.controller( 'HomeCtrl', function HomeController($scope, $location, $filter, httpService, errorService, listService, modalService, $modal, avleveringerService) {
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

  httpService.getAvtaler()
  .success(function(data, status, headers, config) {
    $scope.avtaler = data;
    $scope.setValgtAvtale(data[0]); 
  }).error(function(data, status, headers, config) {
    errorService.errorCode(status);
  });

  $scope.$watch(
    function() {
     return avleveringerService.getAvleveringer();
   },
    function(data) { 
      $scope.avleveringer = data; 
    }
  );

  $scope.setValgtAvtale = function(avtale) {
    avleveringerService.setAvleveringer(avtale);

    $scope.valgtAvtale = avtale;
  };

  $scope.actionList = function(avlevering) {
    httpService.getPasientjournaler(avlevering.avleveringsidentifikator)
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

  $scope.actionAdd = function() {
    $location.path('/registrer');
  };

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

  $scope.actionDelete = function(element, id) {
    modalService.deleteModal(element, id);
  };

  $scope.leggTilAvlevering = function() {
    avleveringerService.leggTil();
  };

  $scope.loggUt = function() {
    console.log("TODO: logg ut");
    $location.path('/login');
  };
});