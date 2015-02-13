angular.module( 'nha.common.list-view', [
  'nha.common.list-service',
  'nha.common.http-service',
  'nha.common.error-service',
  'nha.common.modal-service',
  'nha.registrering.registrering-service',
  'ui.router'
])

.config(function config( $stateProvider ) {
  $stateProvider.state( 'list', {
    url: '/list',
    views: {
      "main": {
        controller: 'ListCtrl',
        templateUrl: 'common/list-view/list-view.tpl.html'
      }
    }
  });
})

.controller( 'ListCtrl', function HomeController($scope, $location, listService, httpService, errorService, modalService, $filter, registreringService) {
  var antall = 15;
  $scope.$watch(
    function() { return $filter('translate')('konfig.ANTALL'); },
    function(newval) { antall = Number(newval); }
  );
  $scope.tekster = {
    "tooltip" : {}
  };
  $scope.$watch(
    function() { return $filter('translate')('home.PASIENTSOK'); },
    function(newval) { $scope.tekster.pasientsok = newval; }
  );
  $scope.$watch(
      function() { return $filter('translate')('common.PASIENTJOURNAL'); },
      function(newval) { $scope.tekster.pasientjournal = newval; }
  );

  $scope.navHome = function() {
    $location.path('/home');
  };

  $scope.navLoggut = function() {
    $location.path('/login');
  };
  
  $scope.data = listService.getData();
  if($scope.data === undefined) {
    $scope.navHome();
  }  

  var setTittel = function(data) {
    if(data.antall > data.total) {
      $scope.tittel.underTittel = " " + data.total + " / " + data.total + " ";
    } else {
      $scope.tittel.underTittel = " " + data.antall + " / " + data.total + " ";
    }
  };

  $scope.tittel = listService.getTittel();
  setTittel($scope.data);

  $scope.aktivSide = 1;
  var sider = 0;
  $scope.antallSider = function() {
    if($scope.data.total <= $scope.data.antall) {
      return new Array(1);
    }
    sider = Math.round($scope.data.total / $scope.data.antall) + 1;
    return new Array(Math.round(sider)); 
  };

  $scope.actionSok = function() {
    var txt = $scope.tekster.sokeresultat;
    var viser = $scope.tekster.viser;
    listService.setSok($scope.sokInput);
    httpService.hentAlle("pasientjournaler?side=1&antall=" + antall + listService.getQuery())
    .success(function(data, status, headers, config) {

      setTittel(data);
      $scope.tittel.tittel = $scope.tekster.pasientsok;

      $scope.data = data;
      $scope.aktivSide = 1;

      $scope.antallSider();
    }).error(function(data, status, headers, config) {
      errorService.errorCode(status);
    });
  };

  $scope.navPage = function(index) {
    httpService.hentAlle("pasientjournaler?side=" + index + "&antall=" + antall + listService.getQuery())
    .success(function(data, status, headers, config) {

      setTittel(data);

      $scope.data = data;
      $scope.aktivSide = index;
    
    }).error(function(data, status, headers, config) {
      errorService.errorCode(status);
    });
  };

  $scope. $watch(
    function() { 
      return sider; 
    },
    function(newval) { 
      document.getElementById("paging-ctrl").style.width = (36 * newval) + 'px'; 
    }
  );

  $scope.actionFjernPasientjournal = function(pasientjournal) {
    modalService.deleteModal($scope.tekster.pasientjournal, pasientjournal.navn + " (" + pasientjournal.fodselsnummer + ") ", function() {
      httpService.deleteElement("pasientjournaler/" + pasientjournal.uuid)
      .success(function(data, status, headers, config) {
        fjern($scope.data.liste, pasientjournal);
        --$scope.data.antall;
        --$scope.data.total;
        setTittel($scope.data);
      }).error(function(data, status, headers, config) {
        errorService.errorCode(status);
      });
    });
  };

  $scope.actionVisJournal = function(pasientjournal) {
    httpService.hent("pasientjournaler/" + pasientjournal)
    .success(function(data, status, headers, config) {
        registreringService.setPasientjournalDTO(data);
        $location.path('/registrer');
      }).error(function(data, status, headers, config) {
        errorService.errorCode(status);
      });
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