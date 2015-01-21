angular.module( 'nha.registrering', [
  'ui.router'
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

.controller( 'RegistrerCtrl', ["$scope", "$location", "$filter", function HomeController($scope, $location, $filter) {
  $scope.$watch(
    function() { return $filter('translate')('home.SOK'); },
    function(newval) { $scope.sok = newval; }
  );
}]);