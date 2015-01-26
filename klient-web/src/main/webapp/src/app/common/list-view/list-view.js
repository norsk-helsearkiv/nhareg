angular.module( 'nha.common.list-view', [
  'nha.common.list-service',
  'nha.common.http-service',
  'nha.common.error-service',
  'ui.router'
])

.config(["$stateProvider", function config( $stateProvider ) {
  $stateProvider.state( 'list', {
    url: '/list',
    views: {
      "main": {
        controller: 'ListCtrl',
        templateUrl: 'common/list-view/list-view.tpl.html'
      }
    }
  });
}])

.controller( 'ListCtrl', ["$scope", "$location", "listService", "httpService", "errorService", function HomeController($scope, $location, listService, httpService, errorService) {
  $scope.tittel = listService.getTittel();
  $scope.data = listService.getData();

  $scope.navHome = function() {
    $location.path('/home');
  };

  $scope.navLoggut = function() {
    $location.path('/login');
  };

  $scope.actionVisJournal = function(id) {
    httpService.getPasientjournal(id)
    .success(function(data, status, headers, config) {
      $location.path('/registrer');
    }).error(function(data, status, headers, config) {
      errorService.errorCode(status);
    });    
  };
}]);