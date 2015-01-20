angular.module( 'nha.home', [
  'ui.router',
  'nha.common.http-service'
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

.controller( 'HomeCtrl', function HomeController($scope, $location, $filter, httpService) {
  $scope.$watch(
    function() { return $filter('translate')('home.SOK'); },
    function(newval) { $scope.sok = newval; }
  );

  httpService.getAvtaler("min data");

  $scope.loggUt = function() {
    $location.path('/login');
  };
});