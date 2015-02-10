angular.module( 'nha.common.list-view', [
  'nha.common.list-service',
  'nha.common.http-service',
  'nha.common.error-service',
  'nha.common.modal-service',
  'nha.common.journal-service',
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

.controller( 'ListCtrl', function HomeController($scope, $location, listService, httpService, errorService, modalService, $filter, journalService) {
  $scope.tittel = listService.getTittel();
  $scope.data = listService.getData();
  $scope.tekster = {};
  $scope.$watch(
      function() { return $filter('translate')('common.PASIENTJOURNAL'); },
      function(newval) { $scope.tekster.pasientjournal = newval; }
  );

  if($scope.data === undefined) {
    $location.path('/');
  }

  $scope.navHome = function() {
    $location.path('/home');
  };

  $scope.navLoggut = function() {
    $location.path('/login');
  };

  $scope.actionFjernPasientjournal = function(pasientjournal) {
    modalService.deleteModal($scope.tekster.pasientjournal, pasientjournal.navn + " (" + pasientjournal.fodselsnummer + ") ", function() {
      httpService.deleteElement("pasientjournaler/" + id)
      .success(function(data, status, headers, config) {
        fjern($scope.data, element);
      }).error(function(data, status, headers, config) {
        errorService.errorCode(status);
      });
    });
  };

  $scope.actionVisJournal = function(pasientjournal) {
    httpService.hent("pasientjournaler/" + pasientjournal)
    .success(function(data, status, headers, config) {
        console.log(data);
        journalService.setData(data);
        $location.path('/registrer');
      }).error(function(data, status, headers, config) {
        errorService.errorCode(status);
      });
  };
});