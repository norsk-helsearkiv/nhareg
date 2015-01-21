angular.module( 'nha.home', [
  'ui.router',
  'nha.common.http-service',
  'nha.common.error-service'
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

.controller( 'HomeCtrl', ["$scope", "$location", "$filter", "httpService", "errorService", function HomeController($scope, $location, $filter, httpService, errorService) {
  $scope.$watch(
    function() { return $filter('translate')('home.SOK'); },
    function(newval) { $scope.sok = newval; }
  );

  httpService.getAvtaler()
  .success(function(data, status, headers, config) {
    $scope.avtaler = data;
    $scope.setValgtAvtale(data[0]); 
  }).error(function(data, status, headers, config) {
    errorService.errorCode(status);
  });

  $scope.setValgtAvtale = function(avtale) {
    $scope.valgtAvtale = avtale;
    $scope.avleveringer = avtale.avleveringer;
  };

  $scope.loggUt = function() {
    $location.path('/login');
  };
}]);